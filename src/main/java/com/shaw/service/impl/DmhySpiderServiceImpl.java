package com.shaw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.DmhyData;
import com.shaw.constant.Constants;
import com.shaw.mapper.DmhyDataMapper;
import com.shaw.service.DmhySpiderService;
import com.shaw.utils.PropertiesUtil;
import com.shaw.utils.TimeUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class DmhySpiderServiceImpl implements DmhySpiderService {
	@Value("#{ config['dmhy.url'] }")
	private String DMHY_URL;

	private Logger logger = LoggerFactory.getLogger(DmhySpiderServiceImpl.class);
	@Autowired
	private DmhyDataMapper dmhyDataMapper;
	/**
	 * 为了保证 这里存储的数据 python也能正常使用，不再使用redisTemplate。避免序列化操作，导致map中的string被序列化后存储。
	 * 这里只需要使用 jedis的单连接就够了。
	 */
	@Autowired
	private Jedis singleRedisClient;
	@Autowired
	private HttpClient httpClient;

	public void executeSpider() throws Exception {
		// DMHY长期不稳定设置连接超时时间避免卡死
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(1000 * 15);
		GetMethod getMethod = buildRequestGetMethod(DMHY_URL);
		// 执行请求获取response并解析
		int response = httpClient.executeMethod(getMethod);
		if (response == HttpStatus.SC_OK) {
			String html = getMethod.getResponseBodyAsString();
			Document doc = Jsoup.parse(html);
			Element table = doc.getElementsByTag("tbody").get(0);
			// tr
			Elements datas = table.children();
			List<DmhyData> list = new ArrayList<DmhyData>();
			Map<String, String> redisData = new HashMap<String, String>();
			int updateCount = 0;
			//获取当前时间保证，每一批插入的数据都有时间标示
			Long currentTime = System.currentTimeMillis();
			// 迭代
			for (Element data : datas) {
				String time, classi, title, magnetLink, size, seedNum, publisher, comNum, downNum;
				time = data.child(0).child(0).ownText();
				classi = data.child(1).getElementsByTag("font").get(0).ownText();
				title = data.child(2).select("a[target=_blank]").get(0).ownText();
				magnetLink = data.child(3).select("a[class=download-arrow arrow-magnet]").get(0).attr("href");
				size = data.child(4).ownText();
				seedNum = data.child(5).child(0).ownText();
				downNum = data.child(6).child(0).ownText();
				comNum = data.child(7).ownText();
				publisher = data.child(8).child(0).ownText();
				if (allIsNotBlank(time, classi, title, magnetLink, size, seedNum, publisher)) {
					if (singleRedisClient.hexists(Constants.DMHY_MAP_TITLES_MAGNET, title)) {
						DmhyData oldData = selectOneByTitle(title);
						if (oldData != null) {
							oldData.setSeedNum(valueOfInteger(seedNum));
							oldData.setComNum(valueOfInteger(comNum));
							oldData.setDownNum(valueOfInteger(downNum));
							oldData.setMagnetLink(magnetLink);
							update(oldData);
							updateCount++;
						}
						continue;
					}
					DmhyData anime = new DmhyData();
					anime.setClassi(classi);
					anime.setComNum(valueOfInteger(comNum));
					anime.setDownNum(valueOfInteger(downNum));
					anime.setPublisher(publisher);
					anime.setSeedNum(valueOfInteger(seedNum));
					anime.setSize(size);
					anime.setTitle(title);
					anime.setTime(time);
					anime.setMagnetLink(magnetLink);
					anime.setCreateTime(currentTime);
					list.add(anime);
					redisData.put(anime.getTitle(), anime.getMagnetLink());
				}
			}
			try {
				if (list.size() > 0 && redisData.keySet().size() > 0) {
					this.dmhyDataMapper.batchInsert(list);
					singleRedisClient.hmset(Constants.DMHY_MAP_TITLES_MAGNET, redisData);
				}
				logger.info("更新了" + updateCount + "条记录");
				logger.info("新抓取" + list.size() + "条记录");
			} catch (Exception e) {
				logger.error("executeSpider SQL EXECUTE  ERROR:" + e.getMessage());
				throw e;
			}
		}

	}

	// 筛选：必须需求:分类为动画(動畫)（熟肉）。2.出现多组取 种子数+下载数+完成数 最大值下载3.未下载的动画
	@Override
	public void screenDayMagnet() {
		int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		// 获取今日更新动画
		String[] titles = PropertiesUtil.getConfiguration().getStringArray("a" + weekDay);
		if (titles.length <= 0)
			return;
		for (String title : titles) {
			if (singleRedisClient.exists(String.format(Constants.DOWNLOAD_START, title))) {
				continue;
			}
			Date startTime = TimeUtils.getStartTimeOfDay();
			Date endTime = TimeUtils.getEndTimeOfDay();
			String startTimeStr = TimeUtils.getFormatTimeByFormat(startTime, "yyyy/MM/dd HH:mm");
			String endTimeStr = TimeUtils.getFormatTimeByFormat(endTime, "yyyy/MM/dd HH:mm");
			String classi = "動畫";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startTime", startTimeStr);
			params.put("endTime", endTimeStr);
			params.put("classi", classi);
			params.put("title", title);
			List<DmhyData> datas = this.selectByBaseParam(params);
			if (datas == null || datas.size() <= 0) {
				continue;
			}
			DmhyData[] sortData = new DmhyData[datas.size()];
			sortData = datas.toArray(sortData);
			Arrays.sort(sortData);
			DmhyData downloadData = sortData[0];
			downloadData.setSimpleName(title + TimeUtils.getFormatTimeByFormat(new Date(), "MMddHHmm"));
			String jsonStr = JSONObject.toJSONString(downloadData);
			// 将需要下载的magnet放入缓冲set 队列中，等待python脚本消费
			this.singleRedisClient.lpush(Constants.MAGNET_QUEUE, jsonStr);
			// 设置已下载状态，避免重复下载
			this.singleRedisClient.set(String.format(Constants.DOWNLOAD_START, title), title);
			// 设置 已下载标识的过期时间为当天凌晨后过期。
			if (singleRedisClient.expireAt(String.format(Constants.DOWNLOAD_START, title), endTime.getTime()) != 1) {
				logger.error("key : " + String.format(Constants.DOWNLOAD_START, title)
						+ "set expireat fail ,please delete it by  manual ");
			}

		}
	}

	@Override
	public void screenDayMagnet(String startTime, String endTime) {

	}

	@Override
	public List<DmhyData> selectAnimeByTitle(String title) {
		return this.dmhyDataMapper.selectAnimeByTitle('%' + title + '%');
	}

	@Override
	public Integer batchInsert(List<DmhyData> list) {
		return this.dmhyDataMapper.batchInsert(list);
	}

	@Override
	public DmhyData selectById(Integer id) {
		return this.dmhyDataMapper.selectById(id);
	}

	@Override
	public Integer update(DmhyData data) {
		return this.dmhyDataMapper.update(data);
	}

	@Override
	public List<DmhyData> selectByBaseParam(Map<String, Object> params) {
		String title = (String) params.get("title");
		if (StringUtils.isNotBlank(title)) {
			title = "%" + title + "%";
			params.put("title", title);
		}
		return this.dmhyDataMapper.selectByBaseParam(params);
	}

	@Override
	public DmhyData selectOneByTitle(String title) {
		return this.dmhyDataMapper.selectOneByTitle(title);
	}

	private GetMethod buildRequestGetMethod(String uri) {
		GetMethod getMethod = new GetMethod(uri);
		/*
		 * 初始化http请求参数
		 */
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		getMethod.addRequestHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		getMethod.addRequestHeader("Accept-Language", "gzip, deflate, sdch");
		getMethod.addRequestHeader("Content-Type", "gzip");
		getMethod.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod.addRequestHeader("Connection", "keep-alive");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		getMethod.addRequestHeader("Cookie",
				"Hm_lvt_e4918ccc327a268ee93dac21d5a7d53c=1456210991,1456283743,1456283923,1456285631; Hm_lpvt_e4918ccc327a268ee93dac21d5a7d53c=1456294179");
		getMethod.addRequestHeader("Host", "dmhy.dandanplay.com");
		getMethod.addRequestHeader("referer", "http://share.dmhy.org");
		return getMethod;

	}

	private int valueOfInteger(String num) {
		if (StringUtils.isBlank(num))
			return 0;
		int result = 0;
		try {
			result = Integer.valueOf(num);
		} catch (Exception e) {

		}
		return result;
	}

	private boolean allIsNotBlank(String... strs) {
		for (String s : strs) {
			if (StringUtils.isBlank(s))
				return false;
		}
		return true;
	}

	@Override
	public Integer deleteById(Integer id) {
		return this.dmhyDataMapper.deleteById(id);
	}
}
