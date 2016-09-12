package com.shaw.service;

import com.shaw.bo.DmhyData;

import java.util.List;
import java.util.Map;

public interface DmhySpiderService {
    void executeSpider(String speicalUrl) throws Exception;

    List<DmhyData> selectAnimeByTitle(String title);

    Integer batchInsert(List<DmhyData> list) throws Exception;

    DmhyData selectById(Integer id);

    Integer update(DmhyData data) throws Exception;

    List<DmhyData> selectByBaseParam(Map<String, Object> params);

    void screenDayMagnet();

    void screenDayMagnet(String startTime, String endTime);

    DmhyData selectOneByTitle(String title);

    Integer deleteById(Integer id) throws Exception;

    Long countByBaseParam(Map<String, Object> params);

    Integer safeBatchInsert(List<DmhyData> list) throws Exception;
}
