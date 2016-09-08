package com.shaw.provider;

import com.shaw.bo.DmhyData;
import com.shaw.service.DmhySpiderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class DmhyDataServiceImpl implements DmhyDataService {

	@Autowired
	private DmhySpiderService dmhySpiderService;

	@Override
	public List<DmhyData> selectByBaseParam(Map<String, Object> param) {
		if (param != null && param.size() >= 1)
			return dmhySpiderService.selectByBaseParam(param);
		else
			return null;
	}

}
