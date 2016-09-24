package com.shaw.task;

import com.shaw.service.DmhySpiderService;
import com.shaw.service.impl.DmhySpiderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlerTask {
	@Autowired
	private DmhySpiderService dmhySpiderService;
	private Logger logger = LoggerFactory.getLogger(DmhySpiderServiceImpl.class);

	// 抓取数据并分析，6小时一次。
	@Scheduled(cron = "0 0 0/6 * * ?")
	public void startCrawler() throws Exception {
		logger.info("startCrawler");
		dmhySpiderService.executeSpider(null);
		logger.info("start screenMagnet");
		dmhySpiderService.screenDayMagnet();
	}

	// aria2 支持直接magnet下载文件，有两种模式 1是直接 通过magnet下载文件，二是通过 libtorrent 下载种子后下载文件。
	// 磁链转化下载为种子，每8小时一次。
	@Scheduled(cron = "0 0 0/8 * * ?")
	public void startDownloadTorrent() throws Exception {
		logger.info("start DownloadTorrent");
		ProcessBuilder procB = new ProcessBuilder("python", "pythonScript/Magnet2Torrent.py");
		procB.start();
	}

	// 启用aria2 下载文件 错开其他任务时间，开始下载任务
	@Scheduled(cron = "0 0 10,13,15,17,19 * * ?")
	public void startDownloadFile() throws Exception {
		logger.info("start DownloadFile");
		ProcessBuilder procB = new ProcessBuilder("python", "pythonScript/Aria2Rpc.py");
		procB.start();
	}
}
