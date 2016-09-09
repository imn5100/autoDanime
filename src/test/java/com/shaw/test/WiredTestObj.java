package com.shaw.test;

import com.shaw.bo.DmhyData;
import com.shaw.constant.Constants;
import com.shaw.service.DmhySpiderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WiredTestObj extends SpringTestCase {

	@Autowired
	private DmhySpiderService dmhySpiderService;

	@Autowired
	private Jedis singleRedisClient;

	public static final String searchUrl = "http://share.dmhy.org/topics/list/page/%s?keyword=%s";

	@Test
	public void testSpider() throws Exception {
		String keyword = "";
		int page = 1;
		String encodeKeyword = URLEncoder.encode(keyword, "utf-8");
		String url = String.format(searchUrl, page, encodeKeyword);
		dmhySpiderService.executeSpider(url);
	}

	@Test
	public void testMapper() throws Exception {
		dmhySpiderService.screenDayMagnet();
	}

	@Test
	// 查看redis和mysql数据不一致的情况 ,数据库包含，redis无
	public void checkMysqlToRedis() throws Exception {
		List<DmhyData> list = dmhySpiderService.selectByBaseParam(new HashMap<String, Object>());
		List<DmhyData> redundancys = new ArrayList<DmhyData>();
		for (DmhyData key : list) {
			if (!singleRedisClient.hexists(Constants.DMHY_MAP_TITLES_MAGNET, key.getTitle())) {
				System.err.println(key.toString());
				redundancys.add(key);
			}
		}
		System.out.println(redundancys.size());
		for (DmhyData data : redundancys) {
			singleRedisClient.hset(Constants.DMHY_MAP_TITLES_MAGNET, data.getTitle(), data.getMagnetLink());
		}
	}

	/**
	 * 问题。 python 也操作过redis的同一map，java存储 title-magnet
	 * 时，是当作String对象存储的，而python则是当字符串存储。导致java 获取到未序列化的 magnet 解析式报错 这里使用
	 * singleRedisClient，直接取得原本的字符串，不进行反序列化操作。
	 */
	@Test
	// 查看redis和mysql数据不一致的情况 ,redis包含,数据库无
	// 并清除redis 多余无用数据
	public void checkRedisToMysql() throws Exception {
		Map<String, String> map = singleRedisClient.hgetAll(Constants.DMHY_MAP_TITLES_MAGNET);
		List<String> titles = new ArrayList<String>();
		for (String key : map.keySet()) {
			if (dmhySpiderService.selectOneByTitle(key) == null) {
				System.out.println(key + ":" + map.get(key));
				titles.add(key);
			}
		}
		for (String title : titles) {
			System.out.println(title);
		}
		// singleRedisClient.hdel(Constants.DMHY_MAP_TITLES_MAGNET,
		// titles.toArray(new String[] {}));
	}

	
	/**
	 * 查看redis和 mysql是否一致
	 * */
	@Test
	public void selectMysqlWithRedisDataSize() {
		Long redisSize = singleRedisClient.hlen(Constants.DMHY_MAP_TITLES_MAGNET);
		Long mysqlSize = dmhySpiderService.countByBaseParam(null);
		System.out.println("redisSize:" + redisSize);
		System.out.println("mysqlSize:" + mysqlSize);
	}

	/**
	 * 清理 数据
	 */
	@Test
	public void clear() throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		// 查询 某时间点之前的数据，清理。
		param.put("endTime", "2016/08/15");
		List<DmhyData> list = dmhySpiderService.selectByBaseParam(param);

		// 这里为了确保数据一致性，必须单条删除。否则要手动改 redis和mysql
		for (DmhyData key : list) {
			if (dmhySpiderService.deleteById(key.getId()) == 1) {
				singleRedisClient.hdel(Constants.DMHY_MAP_TITLES_MAGNET, key.getTitle());
			}
		}
	}
}
