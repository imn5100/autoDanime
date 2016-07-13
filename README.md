# AADTask<br />
简单的爬虫(Java)+简单的下载脚本(Python)--&gt;简单的新番下载程序。<br />
全自动化下载你想看并且有资源的新番。<br />
基本路线:<br />
1.爬虫爬取DMHY首页网页资源，结构化数据，存入MySql。<br />
2.基于补番列表配置筛选最优磁链资源，存入待下载磁链队列(Redis)。<br />
3.Python脚本执行：磁链队列出队，磁链转化为种子。magnet-&gt;torrent。存入待下载种子队列。<br />
4.Python脚本执行:种子队列出队，开启种子下载。<br />
5.以上4个模块，由Spring ScheduledTask控制调度时间和运行频率。&nbsp;<br />
<br />
<br />
依赖环境:JDK1.7+ , Pythin2.7(扩展包:libtorrent,bencode,redis) , Mysql , Redis<br />
<br />
<br />
核心配置文件:src/main/resource/config.properties。因为是基于模糊查询筛选的下载资源。如果这里配置番名的关键字而不是全名，获取的资源范围会更大，取到优质资源的概率也就更高。简繁区分：DMHY本身资源来源复杂,程序无法明确下载简体或繁体字幕资源。单配置繁体番名更易获取繁体字幕资源。相对应，简体也一样。<br />
例:<br />
此处a1-a7表示周一-周日,等号后对应当日更新且你钟意的新番,以逗号','分隔。<br />
a1=<br />
a2=蒼之彼方的四重奏<br />
a3=<br />
a4=為美好的世界献上祝福,春&amp;夏推理事件簿<br />
a5=只有我不存在的城市,粗点心战争<br />
a6=红壳的潘多拉,重裝武器<br />
a7=<br />
<br />
<br />
下载的资源：/resource 下。直接运行python脚本则在 pythonScript/resource 下。<br />
<br />
<br />
缺点：1.依赖环境过多，导致稳定性下降。2.多数据源-导致数据可能不一致。积累太多导致BUG。3.可视化程度低<br />
后续工作：1.所有模块以python实现。2.减少依赖环境。3.爬虫优化，下载脚本优化。4.可视化研究。<br />
<br />
<br />
PS：<br />
1.python新手，代码见笑。后期将大量改动。<br />
2.程序爬取的资源来自DMHY，爬虫只是模拟浏览器进行一次GET请求，执行频率低,绝不会对网站造成任何损害。爱护花园，拒绝危害性爬虫(多线程，海量请求)。<br />
3.一切得到的资源仅供个人学习交流，切勿商用。<br />
*花园网址切勿广泛传播。<br />
<br />
<br />
<br />
