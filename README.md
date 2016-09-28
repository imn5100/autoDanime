AutoDAnime
====

Anime automatic download tool

<br />
<p>磁链爬取+ magnet->torrent + torrent->file</p>
主要内容
-----
*爬取DMHY动画磁链的爬虫任务，目前爬取的数据存储于redis和mysql中-java实现*
*磁链分析，筛选追番列表任务，通过读取配置的追番列表+计算磁链的（种子数+下载量+完成下载量）完成排序，筛选最佳资源下载-java实现*
*将磁链下载为种子的脚本-python 调用libtorrent实现-(java轮询调用)*
*文件下载脚本-python 调用aria2 rpc实现，可通过aria2管理插件(YAAW OR webui-aria2)查看下载进度-(java轮询调用)*
其他关联
-----
*lucene实现提供动画搜索服务 * 目前搜索相关业务已转移至search-center项目下，本项目中通过dubbo调用soa接口实现搜索*
*本项目的python未完全实现版本在python_test中，可以和本项目共用mysql与redis中的数据，*
*后续项目核心会转到python_test 中。建立更加自动化、轻量的资源爬取-》资源筛选-》资源下载 脚本程序，*
依赖环境
-----
本项目的依赖环境：(暂只支持windows环境)
jdk1.7+,python2.7.
DB : mysql,redis.
运行项目所需的 python库：redis,requests,libtorrent.
其他程序依赖： Aria2 + 任意一款 Aria2 GUI 推荐   : <a href="https://github.com/binux/yaaw">YAAW</a>||<a href="https://github.com/acgotaku/YAAW-for-Chrome">YAAW-for-Chrome<a>||<a href="https://github.com/ziahamza/webui-aria2">webui-aria2<a/>.







