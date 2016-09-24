package com.shaw.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.shaw.result.Result;
import com.shaw.service.IBlogSearchService;
import com.shaw.service.IDmhyDataSearchService;
import com.shaw.vo.BlogVo;
import com.shaw.vo.DmhyDataVo;

public class SearchTest extends SpringTestCase {

	@Resource
	private IDmhyDataSearchService dmhyDataSearchService;
	@Resource
	private IBlogSearchService blogSearchService;

	@Test
	public void testSearchDmhyData() throws Exception {
		String keyword = "魔法少女☆伊莉雅 12";
		Result<List<DmhyDataVo>> result = dmhyDataSearchService.searchDmhyData(keyword, null);
		if (result != null && result.isSuccess() && result.getModel() != null) {
			for (DmhyDataVo vo : result.getModel()) {
				System.out.println(vo.toString());
			}
		}
	}

	@Test
	public void testSearchBlog() {
		String keyword = "spring";
		Result<List<BlogVo>> result = blogSearchService.searchBlog(keyword, null);
		if (result != null && result.isSuccess() && result.getModel() != null) {
			for (BlogVo vo : result.getModel()) {
				System.out.println(vo.getTitle());
			}
		}
	}

}
