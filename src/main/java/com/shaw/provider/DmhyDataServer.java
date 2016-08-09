package com.shaw.provider;

import java.util.List;
import java.util.Map;

import com.shaw.bo.DmhyData;

public interface DmhyDataServer {
	List<DmhyData> selectByBaseParam(Map<String, Object> param);
}
