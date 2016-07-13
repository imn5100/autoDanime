package com.shaw.service;

import java.util.List;
import java.util.Map;

import com.shaw.bo.DmhyData;

public interface DmhySpiderService {
	void executeSpider() throws Exception;

	List<DmhyData> selectAnimeByTitle(String title);

	Integer batchInsert(List<DmhyData> list);

	DmhyData selectById(Integer id);

	Integer update(DmhyData data);

	List<DmhyData> selectByBaseParam(Map<String, Object> params);

	void screenDayMagnet();

	DmhyData selectOneByTitle(String title);
}
