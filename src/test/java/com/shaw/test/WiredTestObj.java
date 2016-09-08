package com.shaw.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.DmhyData;
import com.shaw.constant.Constants;
import com.shaw.service.DmhySpiderService;
import com.shaw.service.impl.RedisClient;

import redis.clients.jedis.Jedis;

public class WiredTestObj extends SpringTestCase {

	@Autowired
	private DmhySpiderService dmhySpiderService;

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private Jedis singleRedisClient;

	@Test
	public void testSpider() throws Exception {
		dmhySpiderService.executeSpider();
	}

	@Test
	public void testMapper() throws Exception {
		dmhySpiderService.screenDayMagnet();
	}

	@Test
	// 查看redis和mysql数据不一致的情况 ,数据库包含，redis无
	public void checkMysqlToRedis() throws Exception {
		List<DmhyData> list = dmhySpiderService.selectByBaseParam(new HashMap<String, Object>());
		int count = 0;
		for (DmhyData key : list) {
			if (!redisClient.hexists(Constants.DMHY_MAP_TITLES_MAGNET, key.getTitle())) {
				System.err.println(key.toString());
				count++;
			}
		}
		System.out.println(count);
	}

	@Test
	// 查看redis和mysql数据不一致的情况 ,数据库包含，redis无
	/**
	 * 问题。 python 也操作过redis的同一map，java存储 title-magnet
	 * 时，是当作String对象存储的，而python则是当字符串存储。导致java 获取到未序列化的 magnet 解析式报错
	 *
	 */
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
				redisClient.hdel(Constants.DMHY_MAP_TITLES_MAGNET, key.getTitle());
			}
		}

	}
}
