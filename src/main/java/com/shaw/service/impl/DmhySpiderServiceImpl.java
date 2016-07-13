package com.shaw.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.DmhyData;
import com.shaw.mapper.DmhyDataMapper;
import com.shaw.service.DmhySpiderService;
import com.shaw.utils.PropertiesUtil;
import com.shaw.utils.TimeUtils;

@Service
public class DmhySpiderServiceImpl implements DmhySpiderService {
	public static final String MAGNET_QUEUE = "Magnet_Queue";
	public static final String DOWNLOAD_START = "Download_Start_%s";
	public static final String DMHY_MAP_TITLES_MAGNET = "DMHY_Map_Titles_Magnet";
	public static final String DMHY_ERR_LIST = "DMHY_err_List";

	private Logger logger = LoggerFactory.getLogger(DmhySpiderServiceImpl.class);
	@Autowired
	private DmhyDataMapper dmhyDataMapper;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private HttpClient httpClient;

	public void executeSpider() throws Exception {
		/*
		 * 初始化http请求参数
		 */
		// DMHY长期不问题设置连接超时时间避免卡死
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		GetMethod getMethod = new GetMethod("http://dmhy.dandanplay.com/topics/list/page/1");
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
		getMethod.addRequestHeader("refer", "http://dmhy.dandanplay.com");
		// 执行请求获取response并解析
		int response = httpClient.executeMethod(getMethod);
		if (response == HttpStatus.SC_OK) {
			String html = getMethod.getResponseBodyAsString();
			Document doc = Jsoup.parse(html);
			Element table = doc.getElementsByTag("tbody").get(0);
			Elements datas = table.children();
			System.out.println();
			List<DmhyData> list = new ArrayList<DmhyData>();
			Map<String, Object> redisData = new HashMap<String, Object>();
			int updateCount = 0;
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
					if (redisClient.hexists(DMHY_MAP_TITLES_MAGNET, title)) {
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
					list.add(anime);
					redisData.put(anime.getTitle(), anime.getMagnetLink());
				}
			}
			try {
				if (list.size() > 0 && redisData.keySet().size() > 0) {
					this.dmhyDataMapper.batchInsert(list);
					redisClient.hmset(DMHY_MAP_TITLES_MAGNET, redisData);
				}
				logger.info("更新了" + updateCount + "条记录");
				logger.info("新抓取" + list.size() + "条记录");
				System.out.println("更新了" + updateCount + "条记录");
				System.out.println("新抓取" + list.size() + "条记录");
			} catch (Exception e) {
				System.err.println("SLQ EXECUTE  ERROR");
				throw e;
			}
		}

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

	// 筛选：必须需求:分类为动画(動畫)（熟肉）。2.出现多组取 种子数+下载数+完成数 最大值下载3.未下载的动画
	@Override
	public void screenDayMagnet() {
		int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		// 获取今日更新动画
		String[] titles = PropertiesUtil.getConfiguration().getStringArray("a" + weekDay);
		if (titles.length <= 0)
			return;
		for (String title : titles) {
			if (redisClient.exists(String.format(DOWNLOAD_START, title))) {
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
			Arrays.sort(datas.toArray());
			DmhyData downloadData = datas.get(0);
			downloadData.setSimpleName(title + TimeUtils.getFormatTimeByFormat(new Date(), "MMddHHmm"));
			String jsonStr = JSONObject.toJSONString(downloadData);
			// 将需要下载的magnet放入缓冲set 队列中，等待python脚本消费
			this.redisClient.lpush(MAGNET_QUEUE, jsonStr);
			// 设置已下载状态，避免重复下载
			this.redisClient.set(String.format(DOWNLOAD_START, title), title);
			// 设置 已下载标识的过期时间为当天凌晨后过期。
			if (!redisClient.expireat(String.format(DOWNLOAD_START, title), endTime)) {
				logger.error("key : " + String.format(DOWNLOAD_START, title)
						+ "set expireat fail ,please delete it by  manual ");
			}

		}
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
}
