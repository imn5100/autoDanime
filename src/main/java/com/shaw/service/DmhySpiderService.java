package com.shaw.service;

import com.shaw.bo.DmhyData;

import java.util.List;
import java.util.Map;

public interface DmhySpiderService {
    void executeSpider(String speicalUrl) throws Exception;

    List<DmhyData> selectAnimeByTitle(String title);

    Integer batchInsert(List<DmhyData> list);

    DmhyData selectById(Integer id);

    Integer update(DmhyData data);

    List<DmhyData> selectByBaseParam(Map<String, Object> params);

    void screenDayMagnet();

    void screenDayMagnet(String startTime, String endTime);

    DmhyData selectOneByTitle(String title);

    Integer deleteById(Integer id);
    
    Long countByBaseParam(Map<String, Object> params);
}
