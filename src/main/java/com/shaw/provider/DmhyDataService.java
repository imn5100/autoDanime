package com.shaw.provider;

import com.shaw.bo.DmhyData;

import java.util.List;
import java.util.Map;

public interface DmhyDataService {
	List<DmhyData> selectByBaseParam(Map<String, Object> param);
}
