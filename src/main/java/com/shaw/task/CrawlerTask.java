package com.shaw.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shaw.service.DmhySpiderService;
import com.shaw.service.impl.DmhySpiderServiceImpl;

@Component
public class CrawlerTask {
	@Autowired
	private DmhySpiderService dmhySpiderService;
	private Logger logger = LoggerFactory.getLogger(DmhySpiderServiceImpl.class);

	// 抓取数据并分析，没六小时一次。
	@Scheduled(cron = "0 0 0/6 * * ?")
	public void startCrawler() throws Exception {
		logger.info("startCrawler");
		dmhySpiderService.executeSpider();
		logger.info("start screenMagnet");
		dmhySpiderService.screenDayMagnet();
	}

	// 磁链转化下载为种子，每8小时一次。
	@Scheduled(cron = "0 0 0/8 * * ?")
	public void startDownloadTorrent() throws Exception {
		logger.info("start DownloadTorrent");
		ProcessBuilder procB = new ProcessBuilder("python", "pythonScript/Magnet2Torrent.py");
		procB.start();
	}

	// 下载过于耗时，最好转移到另外平台下载
	@Scheduled(cron = "0 0 10,12,14,16,18 * * ?")
	public void startDownloadFile() throws Exception {
		logger.info("start DownloadFile");
		ProcessBuilder procB = new ProcessBuilder("python", "pythonScript/TorrentDownload.py");
		procB.start();
	}
}
