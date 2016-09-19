package com.shaw.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.DmhyData;
import com.shaw.lucene.DmhyDataIndex;
import com.shaw.service.DmhySpiderService;

public class LuceneTest extends SpringTestCase {
    @Autowired
    private DmhySpiderService dmhySpiderService;
    @Autowired
    private DmhyDataIndex dmhyDataIndex;

    @Test
    public void addIndex() throws Exception {
        List<DmhyData> datas = dmhySpiderService.selectByBaseParam(null);
        dmhyDataIndex.addIndexList(datas);
        System.out.println("success");
    }

    @Test
    public void testUpdateIndex() throws Exception {
        DmhyData data = dmhySpiderService.selectOneByTitle("Re：從零開始的異世界生活 SP 22 720P MP4 繁");
        data.setDownNum(0);
        dmhySpiderService.update(data);
    }

    @Test
    public void testDelete() throws Exception {
        dmhyDataIndex.deleteIndex("347");
    }

    public static void main(String[] args) throws Exception {
        DmhyDataIndex dmhyDataIndex = new DmhyDataIndex();
        List<DmhyData> datas = dmhyDataIndex.searchAnime("Ozmafia");
        for (DmhyData data : datas) {
            System.out.println(data);
        }
    }
}
