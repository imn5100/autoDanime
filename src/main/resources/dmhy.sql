DROP TABLE IF EXISTS `dmhy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dmhy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` varchar(45) NOT NULL,
  `classi` varchar(45) NOT NULL,
  `title` varchar(500) NOT NULL,
  `magnetLink` text NOT NULL,
  `size` varchar(45) NOT NULL,
  `seedNum` varchar(45) NOT NULL,
  `downNum` varchar(45) NOT NULL,
  `comNum` varchar(45) NOT NULL,
  `publisher` varchar(45) NOT NULL,
  `create_time` bigint(20) DEFAULT '0' COMMENT '插入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`),
  KEY `tiltle_index` (`title`),
  KEY `id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1764 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



