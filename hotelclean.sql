CREATE DATABASE  IF NOT EXISTS `hotel` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hotel`;
-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel
-- ------------------------------------------------------
-- Server version	5.7.15-log

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booking` (
  `bID` int(11) NOT NULL AUTO_INCREMENT,
  `cID` int(11) NOT NULL,
  `rID` int(11) NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `totalDays` int(11) DEFAULT NULL,
  PRIMARY KEY (`bID`),
  KEY `cID_idx` (`cID`),
  KEY `rID_idx` (`rID`),
  CONSTRAINT `cID` FOREIGN KEY (`cID`) REFERENCES `customer` (`cID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `rID` FOREIGN KEY (`rID`) REFERENCES `room` (`rID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (22,36,100,'2016-12-03','2016-12-10',7),(23,37,101,'2016-12-03','2016-12-10',7),(24,39,100,'2016-12-03','2016-12-10',7),(25,40,402,'2016-12-03','2016-12-10',7),(26,41,303,'2016-12-03','2016-12-10',7),(27,42,303,'2016-12-03','2016-12-10',7),(28,43,205,'2016-12-03','2016-12-10',7);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `hotel`.`booking_AFTER_INSERT` AFTER INSERT ON `booking` FOR EACH ROW
BEGIN
insert into invoice(cID, balance, openDate, paid, updatedAt) values (NEW.cID, datediff(NEW.endDate,NEW.startDate) * (SELECT price FROM room JOIN roomtype WHERE room.rID = NEW.rID AND room.roomType = roomtype.typeID), CURDATE(), 0,  NOW());
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `cID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `phone` int(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`cID`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `room_idx` (`room`),
  CONSTRAINT `room` FOREIGN KEY (`room`) REFERENCES `room` (`rID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (36,'Doge','1343 Meme St',1234567123,'Doge@gmail.com',100),(37,'Amy','1343 Wong St',1234323421,'Amy@gmail.com',101),(38,'John','4123 Wayne Street',654456789,'John@gmail.com',201),(39,'Billy','5654 Willy Street',1989873445,'Billy@gmail.com',100),(40,'Max','98 Amillion',2045678129,'max@gmail.com',402),(41,'Mandy','4123 Handy Street',1345464789,'Mandy@gmail.com',303),(42,'Randy','4123 Handy Street',1945464789,'Randy@gmail.com',303),(43,'Po','423 Handy Street',1465464789,'Po@gmail.com',205);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `invID` int(11) NOT NULL AUTO_INCREMENT,
  `cID` int(11) NOT NULL,
  `balance` decimal(10,2) unsigned zerofill NOT NULL,
  `openDate` date DEFAULT NULL,
  `closeDate` date DEFAULT NULL,
  `chargeType` varchar(30) DEFAULT NULL,
  `paid` tinyint(3) unsigned zerofill DEFAULT NULL,
  `updatedAt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`invID`),
  KEY `custID_idx` (`cID`),
  CONSTRAINT `custID` FOREIGN KEY (`cID`) REFERENCES `customer` (`cID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (17,36,00001050.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(18,37,00001050.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(19,39,00001050.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(20,40,00001400.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(21,41,00001400.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(22,42,00001400.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05'),(23,43,00000700.00,'2016-12-03','2016-12-03','DEBIT',001,'2016-12-04 05:07:05');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoicearchive`
--

DROP TABLE IF EXISTS `invoicearchive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoicearchive` (
  `archiveID` int(11) NOT NULL AUTO_INCREMENT,
  `invID` int(11) DEFAULT NULL,
  `cID` int(11) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `openDate` date DEFAULT NULL,
  `closeDate` date DEFAULT NULL,
  `chargeType` varchar(45) DEFAULT NULL,
  `paid` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`archiveID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoicearchive`
--

LOCK TABLES `invoicearchive` WRITE;
/*!40000 ALTER TABLE `invoicearchive` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoicearchive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `rID` int(11) NOT NULL DEFAULT '100',
  `roomType` int(11) DEFAULT NULL,
  `assignedTo` int(11) DEFAULT NULL,
  PRIMARY KEY (`rID`),
  KEY `roomType_idx` (`roomType`),
  KEY `assignedTo_idx` (`assignedTo`),
  CONSTRAINT `assignedTo` FOREIGN KEY (`assignedTo`) REFERENCES `customer` (`cID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `roomType` FOREIGN KEY (`roomType`) REFERENCES `roomtype` (`typeID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (100,1,39),(101,1,37),(102,2,NULL),(103,2,NULL),(104,3,NULL),(105,3,NULL),(200,1,NULL),(201,1,NULL),(202,2,NULL),(203,2,NULL),(204,3,NULL),(205,3,43),(300,1,NULL),(301,1,NULL),(302,2,NULL),(303,2,42),(304,3,NULL),(305,3,NULL),(400,1,NULL),(401,1,NULL),(402,2,40),(403,2,NULL),(404,3,NULL),(405,3,NULL);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `hotel`.`room_AFTER_UPDATE` AFTER UPDATE ON `room` FOR EACH ROW
BEGIN
IF (NEW.assignedTo IS NOT NULL) THEN
insert into booking(cID, rID, startDate, endDate, totalDays) values (NEW.assignedTo, NEW.rID, CURDATE(), CURDATE() + INTERVAL 7 DAY, 7);
END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `roomtype`
--

DROP TABLE IF EXISTS `roomtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roomtype` (
  `typeID` int(11) NOT NULL AUTO_INCREMENT,
  `class` varchar(50) NOT NULL,
  `price` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`typeID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomtype`
--

LOCK TABLES `roomtype` WRITE;
/*!40000 ALTER TABLE `roomtype` DISABLE KEYS */;
INSERT INTO `roomtype` VALUES (1,'economy',0000000150),(2,'luxury',0000000200),(3,'middle',0000000100);
/*!40000 ALTER TABLE `roomtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'hotel'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-03 21:14:02
