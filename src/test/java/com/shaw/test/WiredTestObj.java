package com.shaw.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.DmhyData;
import com.shaw.service.DmhySpiderService;
import com.shaw.service.impl.DmhySpiderServiceImpl;
import com.shaw.service.impl.RedisClient;

public class WiredTestObj extends SpringTestCase {

	@Autowired
	private DmhySpiderService dmhySpiderService;

	@Autowired
	private RedisClient redisClient;

	@Test
	public void testSpider() throws Exception {
		dmhySpiderService.executeSpider();
	}

	@Test
	public void testMapper() throws Exception {
		dmhySpiderService.screenDayMagnet();
	}

	@Test
	//查看redis和mysql数据不一致的情况
	public void test() throws Exception {
		List<DmhyData> list = dmhySpiderService.selectByBaseParam(new HashMap<String, Object>());
		Map<String, Object> map = redisClient.hgetAll(DmhySpiderServiceImpl.DMHY_MAP_TITLES_MAGNET);
		for (DmhyData key : list) {
			String str = (String) map.get(key.getTitle());
			if (StringUtils.isBlank(str)) {
				System.err.println(key.toString());
			}
		}
	}
}
