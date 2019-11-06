-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.18


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema las
--

CREATE DATABASE IF NOT EXISTS las;
USE las;

--
-- Definition of table `batch`
--

DROP TABLE IF EXISTS `batch`;
CREATE TABLE `batch` (
  `idbatch` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(45) NOT NULL,
  `description` varchar(450) NOT NULL,
  `current` tinyint(1) NOT NULL,
  PRIMARY KEY (`idbatch`),
  UNIQUE KEY `Index_2` (`subject`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `batch`
--

/*!40000 ALTER TABLE `batch` DISABLE KEYS */;
INSERT INTO `batch` (`idbatch`,`subject`,`description`,`current`) VALUES 
 (1,'C Programming','Programmin Lab in C',0),
 (2,'VB Script','An unique scripting language, but well outdated !! :(',0),
 (3,'Advanced Java Programming','Server Side Java Programming',1);
/*!40000 ALTER TABLE `batch` ENABLE KEYS */;


--
-- Definition of table `info`
--

DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `name` varchar(50) NOT NULL,
  `usn` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`usn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `info`
--

/*!40000 ALTER TABLE `info` DISABLE KEYS */;
INSERT INTO `info` (`name`,`usn`,`password`) VALUES 
 ('','',''),
 ('Vinayaka','007','vinnu123'),
 ('Nithesh','09mca60018','nithesh'),
 ('Vinayaka','09mca60030','vinnu313'),
 ('asdf1','324','sdfff'),
 ('asdf1asd','324jhg','lkjlk'),
 ('asdf1asd1','324jhg5','jljkh'),
 ('Admin','admin','admin'),
 ('asdf','asd','sdf');
/*!40000 ALTER TABLE `info` ENABLE KEYS */;


--
-- Definition of table `lab`
--

DROP TABLE IF EXISTS `lab`;
CREATE TABLE `lab` (
  `idlab` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(150) NOT NULL,
  PRIMARY KEY (`idlab`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lab`
--

/*!40000 ALTER TABLE `lab` DISABLE KEYS */;
/*!40000 ALTER TABLE `lab` ENABLE KEYS */;


--
-- Definition of table `labquestions`
--

DROP TABLE IF EXISTS `labquestions`;
CREATE TABLE `labquestions` (
  `idlab` int(10) unsigned NOT NULL,
  `questionid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idlab`,`questionid`),
  KEY `questionFK` (`questionid`),
  CONSTRAINT `labFK` FOREIGN KEY (`idlab`) REFERENCES `lab` (`idlab`),
  CONSTRAINT `questionFK` FOREIGN KEY (`questionid`) REFERENCES `questions` (`questionid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `labquestions`
--

/*!40000 ALTER TABLE `labquestions` DISABLE KEYS */;
/*!40000 ALTER TABLE `labquestions` ENABLE KEYS */;


--
-- Definition of table `marks`
--

DROP TABLE IF EXISTS `marks`;
CREATE TABLE `marks` (
  `usn` varchar(45) NOT NULL,
  `execution` decimal(10,7) NOT NULL,
  `writeup` decimal(10,7) NOT NULL,
  `viva` decimal(10,7) NOT NULL,
  `batchid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`batchid`,`usn`),
  CONSTRAINT `FK_marks_1` FOREIGN KEY (`batchid`) REFERENCES `batch` (`idbatch`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `marks`
--

/*!40000 ALTER TABLE `marks` DISABLE KEYS */;
INSERT INTO `marks` (`usn`,`execution`,`writeup`,`viva`,`batchid`) VALUES 
 ('09mca60018','10.0000000','10.0000000','5.0000000',1),
 ('09mca60018','10.0000000','10.0000000','10.0000000',2),
 ('09mca60018','10.0000000','20.0000000','7.0000000',3);
/*!40000 ALTER TABLE `marks` ENABLE KEYS */;


--
-- Definition of table `questionmap`
--

DROP TABLE IF EXISTS `questionmap`;
CREATE TABLE `questionmap` (
  `usn` varchar(45) NOT NULL,
  `questionid` int(10) unsigned NOT NULL,
  UNIQUE KEY `UNIQUE` (`usn`),
  KEY `FK_QUESTIONS` (`questionid`),
  CONSTRAINT `FK_QUESTIONS` FOREIGN KEY (`questionid`) REFERENCES `questions` (`questionid`),
  CONSTRAINT `FK_USN` FOREIGN KEY (`usn`) REFERENCES `info` (`usn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `questionmap`
--

/*!40000 ALTER TABLE `questionmap` DISABLE KEYS */;
INSERT INTO `questionmap` (`usn`,`questionid`) VALUES 
 ('09mca60018',5),
 ('09mca60030',5);
/*!40000 ALTER TABLE `questionmap` ENABLE KEYS */;


--
-- Definition of table `questions`
--

DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions` (
  `questionid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question` varchar(500) NOT NULL,
  PRIMARY KEY (`questionid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `questions`
--

/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` (`questionid`,`question`) VALUES 
 (1,'Question 1'),
 (2,'Question 2'),
 (3,'Question 3'),
 (4,'Question 4'),
 (5,'Question 5'),
 (6,'Question 6'),
 (7,'Question 7'),
 (8,'Question 8'),
 (9,'Question 9'),
 (10,'Question 10');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;


--
-- Definition of table `viva`
--

DROP TABLE IF EXISTS `viva`;
CREATE TABLE `viva` (
  `questionid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question` varchar(500) NOT NULL,
  PRIMARY KEY (`questionid`),
  UNIQUE KEY `unique` (`question`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `viva`
--

/*!40000 ALTER TABLE `viva` DISABLE KEYS */;
INSERT INTO `viva` (`questionid`,`question`) VALUES 
 (40,'Question 1'),
 (49,'Question 10'),
 (50,'Question 11'),
 (51,'Question 12'),
 (52,'Question 13'),
 (53,'Question 14'),
 (54,'Question 15'),
 (55,'Question 16'),
 (56,'Question 17'),
 (57,'Question 18'),
 (58,'Question 19'),
 (41,'Question 2'),
 (59,'Question 20'),
 (60,'Question 21'),
 (61,'Question 22'),
 (62,'Question 23'),
 (63,'Question 24'),
 (64,'Question 25'),
 (65,'Question 26'),
 (66,'Question 27'),
 (67,'Question 28'),
 (72,'Question 29'),
 (42,'Question 3'),
 (73,'Question 30'),
 (74,'Question 31'),
 (43,'Question 4'),
 (44,'Question 5'),
 (45,'Question 6'),
 (46,'Question 7'),
 (47,'Question 8'),
 (48,'Question 9');
/*!40000 ALTER TABLE `viva` ENABLE KEYS */;


--
-- Definition of table `vivamap`
--

DROP TABLE IF EXISTS `vivamap`;
CREATE TABLE `vivamap` (
  `usn` varchar(45) NOT NULL,
  `questionid` int(10) unsigned NOT NULL,
  `answer` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`usn`,`questionid`),
  KEY `FK_vivamap_1` (`questionid`),
  CONSTRAINT `FK_vivamap_1` FOREIGN KEY (`questionid`) REFERENCES `viva` (`questionid`),
  CONSTRAINT `FK_vivamap_2` FOREIGN KEY (`usn`) REFERENCES `info` (`usn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vivamap`
--

/*!40000 ALTER TABLE `vivamap` DISABLE KEYS */;
INSERT INTO `vivamap` (`usn`,`questionid`,`answer`) VALUES 
 ('09mca60018',43,'Answer 4'),
 ('09mca60018',48,'Answer 9'),
 ('09mca60018',49,'Answer 10'),
 ('09mca60018',53,'Answer 14'),
 ('09mca60018',58,'Answer 19'),
 ('09mca60018',62,'Answer 23'),
 ('09mca60018',64,'Answer 25'),
 ('09mca60018',66,'Answer 27'),
 ('09mca60018',67,'Answer 28'),
 ('09mca60018',73,'Answer 30');
/*!40000 ALTER TABLE `vivamap` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
