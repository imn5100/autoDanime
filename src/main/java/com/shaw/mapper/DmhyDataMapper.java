package com.shaw.mapper;

import com.shaw.bo.DmhyData;

import java.util.List;
import java.util.Map;

public interface DmhyDataMapper {
	List<DmhyData> selectAnimeByTitle(String title);

	List<DmhyData> selectByBaseParam(Map<String, Object> params);

	Integer batchInsert(List<DmhyData> list);

	DmhyData selectById(Integer id);

	Integer update(DmhyData data);

	DmhyData selectOneByTitle(String title);

	Integer deleteById(Integer id);
}