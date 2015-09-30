CREATE DATABASE  IF NOT EXISTS `mobilesale` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mobilesale`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: mobilesale
-- ------------------------------------------------------
-- Server version	5.6.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `business`
--

DROP TABLE IF EXISTS `business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business` (
  `business_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `img` int(10) DEFAULT NULL,
  PRIMARY KEY (`business_id`),
  KEY `img_id_idx` (`img`),
  CONSTRAINT `img_id` FOREIGN KEY (`img`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business`
--

LOCK TABLES `business` WRITE;
/*!40000 ALTER TABLE `business` DISABLE KEYS */;
INSERT INTO `business` VALUES (1,'高清影视',NULL),(2,'光纤宽带',NULL),(3,'智能手机',NULL);
/*!40000 ALTER TABLE `business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_detail`
--

DROP TABLE IF EXISTS `business_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_detail` (
  `business_detail_id` int(10) NOT NULL AUTO_INCREMENT,
  `business_id` int(10) DEFAULT NULL,
  `resource_id` int(10) DEFAULT NULL,
  `title` varchar(30) NOT NULL,
  `icon` int(10) DEFAULT NULL,
  `subtitle` varchar(30) DEFAULT NULL,
  `sort` int(10) DEFAULT '0',
  PRIMARY KEY (`business_detail_id`),
  KEY `business_id_idx` (`business_id`),
  KEY `resource_id_idx` (`resource_id`),
  KEY `icon_id_idx` (`icon`),
  CONSTRAINT `business_id` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `icon_id` FOREIGN KEY (`icon`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `resource_id` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_detail`
--

LOCK TABLES `business_detail` WRITE;
/*!40000 ALTER TABLE `business_detail` DISABLE KEYS */;
INSERT INTO `business_detail` VALUES (1,1,14,'精彩无限',13,'宣传视频',0),(2,1,16,'95套直播',15,'电视频道',0),(3,1,18,'点播最新',17,'大片热剧',0),(4,1,20,'回看暂停',19,'精彩节目',0),(5,1,22,'您身边的',21,'高清影视',0),(6,2,33,'e家欢乐购',32,'马上办礼',0),(7,2,35,'光速来袭',34,'\"翼\"不可挡',0),(8,2,37,'宽带包年',36,'一键续约',0),(9,3,57,'品牌云集',56,'购机送费',0),(10,3,59,'资费便宜',58,'智能省钱',0),(11,3,61,'上网更快',60,'信号更强',0),(12,3,63,'辐射更小',62,'通讯保密',0);
/*!40000 ALTER TABLE `business_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` int(10) NOT NULL COMMENT '301:package page filter in phone service\n302:contract page filter in phone service\n303:phone page filter  in phone service\n101:package page filter  in iTV service\n102:contract page filter  in iTV service\n201:package page filter in broadband service\n202:contract page filter in broadband service',
  `business_id` int(10) DEFAULT NULL COMMENT '1:iTV\n2:broadband\n3:phone',
  `subclass` varchar(15) NOT NULL COMMENT '01:package\n02:contract\n03:phone',
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `business_id_idx` (`business_id`),
  CONSTRAINT `business_id_in_category` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (101,1,'01','高清影视-资费套餐'),(102,1,'02','高清影视-优惠合约'),(201,2,'01','光纤宽带-资费套餐'),(202,2,'02','光纤宽带-优惠合约'),(301,3,'01','智能手机-资费套餐'),(302,3,'02','智能手机-优惠合约'),(303,3,'03','智能手机-货架');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customer_id` int(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `cellphone` varchar(45) DEFAULT NULL,
  `identifier` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter`
--

DROP TABLE IF EXISTS `filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter` (
  `filter_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(18) NOT NULL,
  `category_id` int(10) DEFAULT NULL,
  `used` int(10) DEFAULT '0' COMMENT 'default 0\n0: non used\n1: used',
  `sort` int(10) DEFAULT '0',
  PRIMARY KEY (`filter_id`),
  KEY `category_id_idx` (`category_id`),
  CONSTRAINT `category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter`
--

LOCK TABLES `filter` WRITE;
/*!40000 ALTER TABLE `filter` DISABLE KEYS */;
INSERT INTO `filter` VALUES (1,'天翼年欢惠',303,1,1),(2,'本地特惠',303,1,2),(3,'品牌',303,1,3),(4,'价位',303,1,4),(5,'特点',303,1,5),(6,'选择套餐用户类型',301,1,1),(7,'月费范围',301,1,2),(8,'套餐特点',301,1,3),(9,'优惠',302,1,1),(10,'在网时长',302,1,2);
/*!40000 ALTER TABLE `filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `item_id` int(10) NOT NULL AUTO_INCREMENT,
  `filter_id` int(10) DEFAULT NULL,
  `name` varchar(18) NOT NULL,
  `sort` int(10) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `filter_id_idx` (`filter_id`),
  CONSTRAINT `filter_id` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,1,'天翼年欢惠',1),(2,2,'本地特惠',1),(3,3,'全部',1),(4,3,'苹果',2),(5,3,'三星',3),(6,3,'小米',4),(7,3,'华为',5),(8,4,'100-500',1),(9,4,'500-2000',2),(10,4,'2000-6000',3),(11,5,'销量好',1),(12,5,'大屏',2),(13,5,'触摸',3),(14,5,'功能多',4),(15,6,'首次购机',1),(16,6,'电信天翼用户',2),(17,6,'移动全球通',3),(18,6,'移动动感地带',4),(19,6,'移动神州行',5),(20,6,'联通3G套餐',6),(21,6,'联通如意通',7),(22,6,'联通世界风',8),(23,6,'联通新势力',9),(24,7,'不限',1),(25,7,'0-50',2),(26,7,'51-100',3),(27,7,'101-200',4),(28,7,'200以上',5),(29,8,'不限',1),(30,8,'上网多',2),(31,8,'通话多',3),(32,9,'全部',1),(33,9,'预存话费送手机',2),(34,9,'购机入网送话费',3),(35,10,'全部',1),(36,10,'24个月',2),(37,10,'30个月',3),(38,10,'36个月',4);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_object_relation`
--

DROP TABLE IF EXISTS `item_object_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_object_relation` (
  `item_object_relation_id` int(10) NOT NULL AUTO_INCREMENT,
  `item_id` int(10) NOT NULL,
  `object_id` int(10) NOT NULL COMMENT 'phone id; phone package id; phone contract id; iTV package id; iTV contract id; broadband contract id; broadband package id',
  `category_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`item_object_relation_id`),
  KEY `item_id_in_relation_idx` (`item_id`),
  KEY `category_id_idx` (`category_id`),
  CONSTRAINT `category_id_in_relation` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `item_id` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_object_relation`
--

LOCK TABLES `item_object_relation` WRITE;
/*!40000 ALTER TABLE `item_object_relation` DISABLE KEYS */;
INSERT INTO `item_object_relation` VALUES (2,1,1,303),(3,4,1,303),(4,10,1,303),(5,11,1,303),(6,32,1,302),(7,34,1,302),(8,35,1,302),(9,36,1,302),(10,32,2,302),(11,34,2,302),(12,35,2,302),(13,36,2,302),(14,32,3,302),(15,33,3,302),(16,35,3,302),(17,38,3,302),(18,1,2,303),(19,2,2,303),(20,6,2,303),(21,9,2,303),(22,12,2,303),(23,14,2,303),(24,13,2,303),(25,11,2,303),(26,2,3,303),(27,7,3,303),(28,10,3,303),(29,12,3,303),(30,11,3,303),(31,13,3,303);
/*!40000 ALTER TABLE `item_object_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `order_id` int(10) NOT NULL,
  `customer_id` int(10) DEFAULT NULL,
  `total_price` int(10) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `business_id` int(10) DEFAULT NULL,
  `object_id` int(10) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `create_time` varchar(32) DEFAULT NULL,
  `modify_time` varchar(32) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `customer_id_idx` (`customer_id`),
  KEY `business_id_idx` (`business_id`),
  KEY `business_id_idx_in_order` (`business_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `business_id_in_order` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-03',NULL,NULL),(2,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-01',NULL,NULL),(3,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-16',NULL,NULL),(4,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-08',NULL,NULL),(5,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-09',NULL,NULL),(6,NULL,NULL,NULL,NULL,NULL,NULL,'2014-01-11',NULL,NULL);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `package`
--

DROP TABLE IF EXISTS `package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package` (
  `package_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `business_id` int(10) DEFAULT NULL,
  `price` int(10) DEFAULT '0',
  `attr_name1` varchar(18) DEFAULT NULL,
  `attr_value1` varchar(18) DEFAULT NULL,
  `attr_name2` varchar(18) DEFAULT NULL,
  `attr_value2` varchar(18) DEFAULT NULL,
  `attr_name3` varchar(18) DEFAULT NULL,
  `attr_value3` varchar(18) DEFAULT NULL,
  `attr_name4` varchar(18) DEFAULT NULL,
  `attr_value4` varchar(18) DEFAULT NULL,
  `attr_name5` varchar(18) DEFAULT NULL,
  `attr_value5` varchar(18) DEFAULT NULL,
  `attr_name6` varchar(18) DEFAULT NULL,
  `attr_value6` varchar(18) DEFAULT NULL,
  `vs_name` varchar(45) DEFAULT NULL,
  `vs_value` varchar(90) DEFAULT NULL,
  `desc1` varchar(45) DEFAULT NULL,
  `desc2` varchar(45) DEFAULT NULL,
  `phone_number` int(10) DEFAULT '0',
  `filter_item_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`package_id`),
  KEY `business_id_idx` (`business_id`),
  KEY `filter_item_id_idx` (`filter_item_id`),
  CONSTRAINT `business_id_in_package` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `filter_item_id` FOREIGN KEY (`filter_item_id`) REFERENCES `item` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `package`
--

LOCK TABLES `package` WRITE;
/*!40000 ALTER TABLE `package` DISABLE KEYS */;
INSERT INTO `package` VALUES (1,'高清影视尊享套餐',1,169,'年费','169/年','直播','所有频道','点播','所有片源','回放','所有节目',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(2,'高清影视豪华套餐',1,129,'年费','129/年','直播','所有频道','点播','部分片源','回放','无',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(3,'高清影视特惠套餐',1,89,'年费','89/年','直播','中央台、所有省级卫视','点播','部分片源','回放','无',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(4,'天翼乐享3G上网版_49元套餐',3,49,'月费','49元','国内流量','200MB','WIFI时长','30小时','国内语音','100分钟','短信条数','30条','彩信条数','6条','资费优势','同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。',NULL,NULL,0,NULL),(5,'天翼乐享3G上网版_69元套餐',3,69,'月费','69元','国内流量','300MB','WIFI时长','30小时','国内语音','150分钟','短信条数','30条','彩信条数','6条','资费优势','同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。',NULL,NULL,0,NULL),(6,'天翼乐享3G上网版_89元套餐',3,89,'月费','89元','国内流量','400MB','WIFI时长','30小时','国内语音','240分钟','短信条数','30条','彩信条数','6条','资费优势','同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。',NULL,NULL,0,NULL),(7,'天翼乐享3G上网版_129元套餐',3,129,'月费','129元','国内流量','600MB','WIFI时长','60小时','国内语音','330分钟','短信条数','60条','彩信条数','12条','资费优势','同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。',NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `phone_id` int(10) NOT NULL AUTO_INCREMENT,
  `ad_desc` varchar(45) DEFAULT NULL,
  `thumbnail` int(10) DEFAULT NULL,
  `sale_icon` int(10) DEFAULT NULL,
  `price` int(10) DEFAULT NULL,
  `sale_price` int(10) DEFAULT NULL,
  `ad_desc1` varchar(45) DEFAULT NULL,
  `ad_desc2` varchar(45) DEFAULT NULL,
  `ad_desc3` varchar(45) DEFAULT NULL,
  `ad_desc4` varchar(45) DEFAULT NULL,
  `series` varchar(30) DEFAULT NULL,
  `brand` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `start_time` varchar(30) DEFAULT NULL,
  `look_design` varchar(30) DEFAULT NULL,
  `os` varchar(30) DEFAULT NULL,
  `smartphone` varchar(30) DEFAULT NULL,
  `cpu_core` varchar(30) DEFAULT NULL,
  `cpu_rate` varchar(30) DEFAULT NULL,
  `keyborad` varchar(30) DEFAULT NULL,
  `input` varchar(30) DEFAULT NULL,
  `op_sign` varchar(30) DEFAULT NULL,
  `net_standard` varchar(30) DEFAULT NULL,
  `net_rate` varchar(30) DEFAULT NULL,
  `browser` varchar(30) DEFAULT NULL,
  `device_mem` varchar(30) DEFAULT NULL,
  `run_mem` varchar(30) DEFAULT NULL,
  `card_mem` varchar(30) DEFAULT NULL,
  `extend_mem` varchar(30) DEFAULT NULL,
  `screen_size` varchar(20) DEFAULT NULL,
  `screen_color` varchar(30) DEFAULT NULL,
  `screen_resolution` varchar(30) DEFAULT NULL,
  `gravity` varchar(30) DEFAULT NULL,
  `touch` varchar(30) DEFAULT NULL,
  `music` varchar(30) DEFAULT NULL,
  `video` varchar(30) DEFAULT NULL,
  `ebook` varchar(30) DEFAULT NULL,
  `camera` varchar(30) DEFAULT NULL,
  `sensor` varchar(30) DEFAULT NULL,
  `video_maker` varchar(30) DEFAULT NULL,
  `photo_mode` varchar(30) DEFAULT NULL,
  `continue_photo` varchar(30) DEFAULT NULL,
  `resolution_photo` varchar(30) DEFAULT NULL,
  `camera_other` varchar(30) DEFAULT NULL,
  `sub_camera` varchar(30) DEFAULT NULL,
  `auto_focus` varchar(30) DEFAULT NULL,
  `gps` varchar(30) DEFAULT NULL,
  `wifi` varchar(30) DEFAULT NULL,
  `bluetooth` varchar(30) DEFAULT NULL,
  `office` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `calculator` varchar(30) DEFAULT NULL,
  `device_size` varchar(30) DEFAULT NULL,
  `device_quality` varchar(30) DEFAULT NULL,
  `device_matierial` varchar(30) DEFAULT NULL,
  `battery_category` varchar(30) DEFAULT NULL,
  `battery_capacity` varchar(30) DEFAULT NULL,
  `speek_time` varchar(30) DEFAULT NULL,
  `idle_time` varchar(30) DEFAULT NULL,
  `headset` varchar(30) DEFAULT NULL,
  `attr1` varchar(30) DEFAULT NULL,
  `attr2` varchar(30) DEFAULT NULL,
  `attr3` varchar(30) DEFAULT NULL,
  `attr4` varchar(30) DEFAULT NULL,
  `attr5` varchar(30) DEFAULT NULL,
  `attr6` varchar(30) DEFAULT NULL,
  `attr7` varchar(30) DEFAULT NULL,
  `attr8` varchar(30) DEFAULT NULL,
  `attr9` varchar(30) DEFAULT NULL,
  `attr10` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`phone_id`),
  KEY `thumbnail_idx` (`thumbnail`),
  KEY `sale_icon_id_idx` (`sale_icon`),
  CONSTRAINT `sale_icon_id` FOREIGN KEY (`sale_icon`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `thumbnail_id` FOREIGN KEY (`thumbnail`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
INSERT INTO `phone` VALUES (1,'无与伦比的高端大气上档次',64,NULL,4800,3800,'','','','','iPhone','Apple','5S','2013年','是','iOS','是','4','2000','触摸屏','触摸屏','否','GSM/CDMA2000','500','Safari','2GB','2GB','SD','8GB','4.4','256','1280x768','是','是 ','是','是','是','是','是','是','是','是','768X768','无','有','是','有','有','有','有','有','有','5','500g','铝合金','锂','1200Amh','12hours','24hours','无',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'屌丝神器 绝对超值',74,NULL,1788,1588,'看片利器','游戏神机','物美价廉','发烧级神作','小米','小米','2S','2013','','android','是','4','3200','','','','GSM/CDMA2000','','','8GB','8GB','','','4.5','328','','','','有','有','有','有','','','','','','','','','有','有','有','','','','4.5','450G','塑料','锂','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'强悍配置 非凡体验',83,NULL,2688,2088,'','','','','Ascend','华为','P6','2013','','android','是','','','','','','GSM/CDMA2000','','','8GB','8GB','SD','16GB','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_color`
--

DROP TABLE IF EXISTS `phone_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_color` (
  `phone_color_id` int(10) NOT NULL AUTO_INCREMENT,
  `phone_id` int(10) DEFAULT NULL,
  `color` varchar(15) NOT NULL,
  `value` varchar(10) DEFAULT NULL,
  `img1` int(10) DEFAULT NULL,
  `img2` int(10) DEFAULT NULL,
  `img3` int(10) DEFAULT NULL,
  `img4` int(10) DEFAULT NULL,
  `comment` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`phone_color_id`),
  KEY `phone_id_idx` (`phone_id`),
  KEY `img1_idx` (`img1`),
  KEY `img2_id_idx` (`img2`),
  KEY `img3_id_idx` (`img3`),
  KEY `img4_id_idx` (`img4`),
  CONSTRAINT `img1_id` FOREIGN KEY (`img1`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `img2_id` FOREIGN KEY (`img2`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `img3_id` FOREIGN KEY (`img3`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `img4_id` FOREIGN KEY (`img4`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_id` FOREIGN KEY (`phone_id`) REFERENCES `phone` (`phone_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_color`
--

LOCK TABLES `phone_color` WRITE;
/*!40000 ALTER TABLE `phone_color` DISABLE KEYS */;
INSERT INTO `phone_color` VALUES (1,1,'土豪金','#fff9a5',44,45,46,47,'Apple'),(2,1,'炫酷黑','#000000',48,49,50,51,'Apple'),(3,1,'典雅白','#ffffff',52,53,54,55,'Apple'),(4,2,'劲酷黑','#000000',66,67,68,69,'小米'),(5,2,'珍珠白','#ffffff',70,71,72,73,'小米'),(6,3,'冷静黑','#000000',75,76,77,78,'华为'),(7,3,'温柔粉','#ff9af5',79,80,81,82,'华为');
/*!40000 ALTER TABLE `phone_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_contract`
--

DROP TABLE IF EXISTS `phone_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_contract` (
  `phone_contract_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) NOT NULL,
  `phone_id` int(10) DEFAULT NULL,
  `type` int(10) DEFAULT NULL COMMENT '0: non iPhone\n1: iPhone',
  `price` int(10) DEFAULT NULL,
  `attr_name1` varchar(30) DEFAULT NULL,
  `attr_value1` varchar(30) DEFAULT NULL,
  `attr_name2` varchar(30) DEFAULT NULL,
  `attr_value2` varchar(30) DEFAULT NULL,
  `attr_name3` varchar(30) DEFAULT NULL,
  `attr_value3` varchar(30) DEFAULT NULL,
  `attr_name4` varchar(30) DEFAULT NULL,
  `attr_value4` varchar(30) DEFAULT NULL,
  `attr_name5` varchar(30) DEFAULT NULL,
  `attr_value5` varchar(30) DEFAULT NULL,
  `attr_name6` varchar(30) DEFAULT NULL,
  `attr_value6` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`phone_contract_id`),
  KEY `phone_id_in_contract_idx` (`phone_id`),
  CONSTRAINT `phone_id_in_contract` FOREIGN KEY (`phone_id`) REFERENCES `phone` (`phone_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_contract`
--

LOCK TABLES `phone_contract` WRITE;
/*!40000 ALTER TABLE `phone_contract` DISABLE KEYS */;
INSERT INTO `phone_contract` VALUES (1,'289合约',NULL,1,2890,'月费','289元','手机款','2890元','总赠送话费','2398元','入网当月返还','322元','次月起24月返还','107元','',''),(2,'129合约',NULL,1,3998,'月费','129元','手机款','3998元','总赠送话费','1290元','入网当月返还','162元','次月起24月返还','47元','',''),(3,'389合约',NULL,1,5288,'月费','389元','手机款','0元','预存话费','5288元','总赠送话费','5288元','入网当月返还','440元','次月起36月返还','202元'),(4,'华为 24合约',NULL,0,168,'在网时长','24月','话费返还','68元/月','','','','','','','','');
/*!40000 ALTER TABLE `phone_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_detail`
--

DROP TABLE IF EXISTS `product_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_detail` (
  `product_detail_id` int(10) NOT NULL AUTO_INCREMENT,
  `business_id` int(10) DEFAULT NULL,
  `title` varchar(15) NOT NULL,
  `img_id` int(10) DEFAULT NULL,
  `phone_id` int(10) DEFAULT NULL,
  `sort` int(10) DEFAULT '0',
  PRIMARY KEY (`product_detail_id`),
  KEY `business_id_idx` (`business_id`),
  KEY `img_id_idx` (`img_id`),
  KEY `phone_id_idx` (`phone_id`),
  CONSTRAINT `business_id_in_product` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `img_id_in_product` FOREIGN KEY (`img_id`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_id_in_product` FOREIGN KEY (`phone_id`) REFERENCES `phone` (`phone_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_detail`
--

LOCK TABLES `product_detail` WRITE;
/*!40000 ALTER TABLE `product_detail` DISABLE KEYS */;
INSERT INTO `product_detail` VALUES (1,1,'功能介绍',27,NULL,0),(2,1,'操作模拟',29,NULL,0),(3,1,'无忧安装',30,NULL,0),(4,1,'优惠套餐',31,NULL,0),(5,3,'优惠套餐',65,NULL,0);
/*!40000 ALTER TABLE `product_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promotion` (
  `promotion_id` int(10) NOT NULL AUTO_INCREMENT,
  `img` int(10) NOT NULL,
  `detail_img` int(10) DEFAULT NULL,
  `business_id` int(10) DEFAULT NULL,
  `filter_item_id` int(10) DEFAULT NULL,
  `phone_id` int(10) DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`promotion_id`),
  KEY `business_id_in_promotion_idx` (`business_id`),
  KEY `detail_img_id_in_promotion_idx` (`detail_img`),
  KEY `img_id_in_promotion_idx` (`img`),
  KEY `filter_item_id_in_promotion_idx` (`filter_item_id`),
  KEY `phone_id_idx` (`phone_id`),
  CONSTRAINT `business_id_in_promotion` FOREIGN KEY (`business_id`) REFERENCES `business` (`business_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `detail_img_id` FOREIGN KEY (`detail_img`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `filter_item_id_in_promotion` FOREIGN KEY (`filter_item_id`) REFERENCES `item` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `img_id_in_promotion` FOREIGN KEY (`img`) REFERENCES `resource` (`resource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_id_in_promotion` FOREIGN KEY (`phone_id`) REFERENCES `phone` (`phone_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
INSERT INTO `promotion` VALUES (1,38,39,3,1,NULL,'天翼年欢惠'),(2,40,41,1,NULL,NULL,'高清影视特惠'),(3,42,43,2,NULL,NULL,'存网费得手机');
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `resource_id` int(10) NOT NULL AUTO_INCREMENT,
  `path` varchar(1000) NOT NULL,
  `type` int(10) NOT NULL COMMENT '0:other\n1:video\n2:image',
  `length` int(10) DEFAULT '0',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'/home/img1',0,0),(2,'/home/img2',0,0),(3,'/home/img3',0,0),(4,'/home/img4',0,0),(5,'/resource/1389169625383.jpg',2,879394),(6,'/resource/1389169626258.jpg',2,879394),(7,'/resource/1389172018505.jpg',2,620888),(8,'/resource/1389172044834.jpg',2,777835),(9,'/resource/1389172090811.jpg',2,561276),(10,'/resource/1389177257863.jpg',2,595284),(11,'/resource/634618879.jpg',2,104915),(12,'/resource/213167459.jpg',2,104915),(13,'/resource/1359141698.png',2,3233),(14,'/resource/1264301094.png',2,253527),(15,'/resource/1873349296.png',2,3270),(16,'/resource/1862383953.png',2,253527),(17,'/resource/698472557.png',2,3114),(18,'/resource/696741187.png',2,418317),(19,'/resource/755914252.png',2,3578),(20,'/resource/758799869.png',2,481115),(21,'/resource/986794241.png',2,3468),(22,'/resource/1023922510.png',2,603579),(23,'/resource/1655050530.png',2,233567),(24,'/resource/153659070.png',2,233567),(25,'/resource/1787634014.png',2,233567),(26,'/resource/2094223571.png',2,233567),(27,'/resource/888846834.png',2,233567),(28,'/resource/1175046381.png',2,249519),(29,'/resource/1032319041.jpg',2,173018),(30,'/resource/1048478757.png',2,249519),(31,'/resource/264945284.png',2,234751),(32,'/resource/967083285.png',2,3455),(33,'/resource/955733192.jpg',2,230410),(34,'/resource/740919068.png',2,3470),(35,'/resource/744189434.jpg',2,95194),(36,'/resource/595152047.png',2,3268),(37,'/resource/595152047.jpg',2,197973),(38,'/resource/194943410.png',2,599102),(39,'/resource/234957296.png',2,597211),(40,'/resource/1831173213.png',2,322745),(41,'/resource/1732100371.png',2,791712),(42,'/resource/1870991995.png',2,611061),(43,'/resource/431253949.png',2,382247),(44,'/resource/566512210.png',2,193601),(45,'/resource/558240108.png',2,147751),(46,'/resource/561318100.png',2,73027),(47,'/resource/1599141831.png',2,100135),(48,'/resource/1551046335.png',2,193601),(49,'/resource/1541812361.png',2,147751),(50,'/resource/975436189.png',2,73027),(51,'/resource/1545082727.png',2,100135),(52,'/resource/923114788.png',2,193601),(53,'/resource/959665934.png',2,147751),(54,'/resource/119159725.png',2,73027),(55,'/resource/962743925.png',2,100135),(56,'/resource/1525909307.png',2,3085),(57,'/resource/1528987298.png',2,890013),(58,'/resource/14177244.png',2,3268),(59,'/resource/1853763752.png',2,129548),(60,'/resource/1705756432.png',2,3778),(61,'/resource/192232511.png',2,645476),(62,'/resource/358896934.png',2,3490),(63,'/resource/739170483.png',2,414776),(64,'/resource/775755399.png',2,27720),(65,'/resource/43611357.png',2,512289),(66,'/resource/1236770770.png',2,189148),(67,'/resource/1102301030.png',2,64877),(68,'/resource/1105379021.png',2,52533),(69,'/resource/1034992839.png',2,165030),(70,'/resource/1625121150.png',2,189148),(71,'/resource/237066051.png',2,64877),(72,'/resource/1628199142.png',2,52533),(73,'/resource/1730607683.png',2,165030),(74,'/resource/1050136083.png',2,19698),(75,'/resource/1540550285.png',2,166893),(76,'/resource/1531316311.png',2,193859),(77,'/resource/1389510060.png',2,32949),(78,'/resource/1534394302.png',2,84569),(79,'/resource/739601860.png',2,166893),(80,'/resource/730367886.png',2,193859),(81,'/resource/40200278.png',2,32949),(82,'/resource/733638252.png',2,84569),(83,'/resource/1699927452.png',2,27310);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `EID` varchar(32) NOT NULL,
  `token` varchar(48) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`EID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='The management system user: include Admin, Operator and so on';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','827CCB0EEA8A706C4C34A16891F84E7B','1',NULL),(2,'vincent','827CCB0EEA8A706C4C34A16891F84E7B','2',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `view_filter_item`
--

DROP TABLE IF EXISTS `view_filter_item`;
/*!50001 DROP VIEW IF EXISTS `view_filter_item`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_filter_item` (
  `item_id` tinyint NOT NULL,
  `item_name` tinyint NOT NULL,
  `filter_id` tinyint NOT NULL,
  `filter_name` tinyint NOT NULL,
  `business_id` tinyint NOT NULL,
  `business_name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_filter_item`
--

/*!50001 DROP TABLE IF EXISTS `view_filter_item`*/;
/*!50001 DROP VIEW IF EXISTS `view_filter_item`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_filter_item` AS select `i`.`item_id` AS `item_id`,`i`.`name` AS `item_name`,`f`.`filter_id` AS `filter_id`,`f`.`name` AS `filter_name`,`b`.`business_id` AS `business_id`,`b`.`name` AS `business_name` from (((`category` `c` join `filter` `f` on((`c`.`category_id` = `f`.`category_id`))) join `item` `i` on((`f`.`filter_id` = `i`.`filter_id`))) join `business` `b` on((`b`.`business_id` = `c`.`business_id`))) where (`f`.`used` = 1) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-17 16:54:06
