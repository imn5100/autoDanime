package com.shaw.provider;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.DmhyData;
import com.shaw.mapper.DmhyDataMapper;

public class DmhyDataServerImpl implements DmhyDataServer {

	@Autowired
	private DmhyDataMapper dmhyDataMapper;

	@Override
	public List<DmhyData> selectByBaseParam(Map<String, Object> param) {
		if (param != null && param.size() > 1)
			return dmhyDataMapper.selectByBaseParam(param);
		else
			return null;
	}

}
