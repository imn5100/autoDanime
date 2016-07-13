package com.shaw.mapper;

import java.util.List;
import java.util.Map;

import com.shaw.bo.DmhyData;

public interface DmhyDataMapper {
	List<DmhyData> selectAnimeByTitle(String title);

	List<DmhyData> selectByBaseParam(Map<String, Object> params);

	Integer batchInsert(List<DmhyData> list);

	DmhyData selectById(Integer id);

	Integer update(DmhyData data);

	DmhyData selectOneByTitle(String title);
}