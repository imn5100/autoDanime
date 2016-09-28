AutoDAnime
====

Anime automatic download tool
<br />
主要内容
-----
1.爬取DMHY动画磁链的爬虫任务，目前爬取的数据存储于redis和mysql中-java实现<br>
2.磁链分析，筛选追番列表任务，通过读取配置的追番列表+计算磁链的（种子数+下载量+完成下载量）完成排序，筛选最佳资源下载-java实现<br>
3.将磁链下载为种子的脚本-python 调用libtorrent实现-(java轮询调用)<br>
4.文件下载脚本-python 调用aria2 rpc实现，可通过aria2管理插件(YAAW OR webui-aria2)查看下载进度-(java轮询调用)<br>
其他关联
-----
1.lucene实现提供动画搜索服务 * 目前搜索相关业务已转移至search-center项目下，本项目中通过dubbo调用soa接口实现搜索<br>
2.本项目的python未完全实现版本在python_test中，可以和本项目共用mysql与redis中的数据<br>
3.后续项目核心会转到python_test 中。建立更加自动化、轻量的资源爬取-》资源筛选-》资源下载 脚本程序<br>
依赖环境
-----
本项目的依赖环境：<br>
jdk1.7 + python2.7.<br>
DB : mysql,redis.<br>
运行项目所需的python扩展库：redis,requests,libtorrent.<br>
其他程序依赖： <a href="https://github.com/aria2/aria2/releases" target="_blank">ARIA2</a> + 任意Aria2 GUI<a href="https://github.com/binux/yaaw" target="_blank">YAAW</a>||<a href="https://github.com/acgotaku/YAAW-for-Chrome" target="_blank">YAAW-for-Chrome<a>||<a href="https://github.com/ziahamza/webui-aria2" target="_blank">webui-aria2<a/>.<br>







