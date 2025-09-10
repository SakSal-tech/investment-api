-- MySQL dump 10.13  Distrib 9.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: investment
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `investment`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `investment` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `investment`;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset` (
  `asset_id` char(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `portfolio_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`asset_id`),
  KEY `FK6n6rcwlesgwma1kyxc9c1iwh3` (`portfolio_id`),
  CONSTRAINT `FK6n6rcwlesgwma1kyxc9c1iwh3` FOREIGN KEY (`portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset`
--

LOCK TABLES `asset` WRITE;
/*!40000 ALTER TABLE `asset` DISABLE KEYS */;
INSERT INTO `asset` VALUES ('07f307f9-678f-4cf7-9b83-5cf5e6647821','Sustainable Green Fund',500000,'271e8756-108f-4bab-a01f-c8dccd44bbe5'),('1f6f138b-fb4a-4b7e-9cd6-a56b5d9544c5','Global Equity Fund',60000,'7ad11722-3431-46b0-90d1-6da0b95f24f8'),('3e015d33-1d07-4c91-9a5d-cc10e4a2c9ec','Corporate Bond Fund',150000,'aabb4eaa-8fbb-40b6-869c-29723e8c1d0f'),('4da98a76-3422-4ab3-9aa1-42082a3d8440','Corporate Bond',40000,'aabb4eaa-8fbb-40b6-869c-29723e8c1d0f'),('5266a00c-8280-4741-b82c-ac4d8e26290f','Microsoft Stock',25000,'dfb9801c-0ecb-40c5-aa34-fb79e678602a'),('56f2b107-6cdf-4e15-adb7-d076aee60e19','Global Equity Fund',60000,'7ad11722-3431-46b0-90d1-6da0b95f24f8'),('db349d30-5b9e-40e7-b11c-5bafb8f94c04','Global Equity Fund',250000,'7ad11722-3431-46b0-90d1-6da0b95f24f8');
/*!40000 ALTER TABLE `asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_price_history`
--

DROP TABLE IF EXISTS `asset_price_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_price_history` (
  `price_history_id` char(36) NOT NULL,
  `closing_price` double DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `trading_date` date DEFAULT NULL,
  `asset_id` char(36) NOT NULL,
  PRIMARY KEY (`price_history_id`),
  KEY `FKoq9grhnctlyisgm5pd58v7sks` (`asset_id`),
  CONSTRAINT `FKoq9grhnctlyisgm5pd58v7sks` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_price_history`
--

LOCK TABLES `asset_price_history` WRITE;
/*!40000 ALTER TABLE `asset_price_history` DISABLE KEYS */;
INSERT INTO `asset_price_history` VALUES ('14309a43-d3ba-49ff-a96c-9f0c12b20b4b',507.97,'MSFT','2025-09-04','5266a00c-8280-4741-b82c-ac4d8e26290f'),('4f98016b-be98-4194-a8ef-3e4647539e20',241.5,'IBM','2025-09-02','56f2b107-6cdf-4e15-adb7-d076aee60e19'),('64a3c4ef-dd49-45f0-98f2-3c71f455d0ab',244.1,'IBM','2025-09-03','56f2b107-6cdf-4e15-adb7-d076aee60e19'),('a957a5e9-90db-40fc-a2b7-9d20825be3de',247.18,'IBM','2025-09-04','56f2b107-6cdf-4e15-adb7-d076aee60e19'),('b4799b9a-fea3-457b-b8b8-62d7924d27e5',505.35,'MSFT','2025-09-03','5266a00c-8280-4741-b82c-ac4d8e26290f'),('dca024af-2e0a-454b-8b71-87c8e37cca00',505.12,'MSFT','2025-09-02','5266a00c-8280-4741-b82c-ac4d8e26290f');
/*!40000 ALTER TABLE `asset_price_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `client_id` char(36) NOT NULL,
  `active` bit(1) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `national_insurance_number` varchar(255) DEFAULT NULL,
  `post_code` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES ('2b96c3b2-3305-467b-8699-366c5867dc68',_binary '\0','321 Elm Street','2025-09-06','1988-05-10','liam.harrison@example.com','Liam',NULL,'XY12 4YZ','Harrison',NULL),('53b738c0-8c34-11f0-b6e8-84ba590b0166',_binary '','123 Main St','2025-09-07','1980-05-15','alice@example.com','Alice','AB123456C','AB12 3CD','Smith','0123456789'),('53b7436c-8c34-11f0-b6e8-84ba590b0166',_binary '','456 Elm St','2025-09-07','1975-10-22','bob@example.com','Bob','CD987654E','CD45 6EF','Johnson','0987654321'),('68a2e452-8c34-11f0-b6e8-84ba590b0166',_binary '','123 Main St','2025-09-07','1980-05-15','alice@example.com','Alice','AB123456C','AB12 3CD','Smith','0123456789'),('68a2e87c-8c34-11f0-b6e8-84ba590b0166',_binary '','456 Elm St','2025-09-07','1975-10-22','bob@example.com','Bob','CD987654E','CD45 6EF','Johnson','0987654321'),('7d45f747-99d3-430e-8a24-de5d416156ce',_binary '','123 Main Street','2025-09-02','1990-01-01','john.doe@example.com','John','AB123456C','AB12 3CD','Doe','07123456789'),('995cffcc-7c82-11f0-a429-84ba590b0166',_binary '\0','789 Pine Rd','2025-08-18','1978-11-05','carol.williams@yahoo.com','Carol',NULL,'EF56 7GH','Williams','07345678901'),('995daf18-7c82-11f0-a429-84ba590b0166',_binary '','456 Maple Avenue','2025-08-18','2000-02-28','david.brown@example.com','David',NULL,'AB12 3CD','Brown','07456789012'),('995e6d8b-7c82-11f0-a429-84ba590b0166',_binary '\0','654 Cedar Ct','2025-08-18','1995-09-15','eve.davis@protonmail.com','Eve',NULL,'IJ10 1KL','Davis','07567890123'),('acb3acd6-e525-49ec-9196-c0dfd1f698bb',_binary '','123 Main Street','2025-09-02','1990-01-01','john.doe@example.com','John','AB123456C','AB12 3CD','Doe','07123456789'),('d0b1f647-6bac-4449-a155-bd2e122c02e8',_binary '\0','456 Maple Avenue',NULL,'1993-11-20','david.brown@example.com','David',NULL,'SM2 3CD','Brown',NULL),('f8727704-68c1-4a78-a00d-e9d2c68af354',_binary '\0','123 Oak Street',NULL,'1985-07-15','david.brown@example.com','David',NULL,'AB12 3CD','Brown',NULL),('fea17503-7d0e-11f0-a429-84ba590b0166',_binary '','123 Main St','2025-08-18','1985-04-12','alice.smith@example.com','Alice',NULL,'AB12 3CD','Smith','07123456789');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portfolio`
--

DROP TABLE IF EXISTS `portfolio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `portfolio` (
  `portfolio_type` varchar(31) NOT NULL,
  `portfolio_id` char(36) NOT NULL,
  `created_at` date DEFAULT NULL,
  `investment_goal` varchar(255) DEFAULT NULL,
  `portfolio_name` varchar(255) DEFAULT NULL,
  `risk_level` int DEFAULT NULL,
  `total_stress_test` double DEFAULT NULL,
  `total_var` double DEFAULT NULL,
  `total_value` decimal(38,2) DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `client_id` char(36) DEFAULT NULL,
  `compliance_status` varchar(255) DEFAULT NULL,
  `esg_score_env` double DEFAULT NULL,
  `esg_score_gov` double DEFAULT NULL,
  `esg_score_social` double DEFAULT NULL,
  `excluded_sectors` varchar(255) DEFAULT NULL,
  `impact_target_carbon` double DEFAULT NULL,
  `impact_target_water` double DEFAULT NULL,
  `last_updated` date DEFAULT NULL,
  `overall_esg_score` double DEFAULT NULL,
  `preferred_sectors` varchar(255) DEFAULT NULL,
  `theme_focus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`portfolio_id`),
  KEY `FKpxcrdn47gewbwxl04370f5ohc` (`client_id`),
  CONSTRAINT `FKpxcrdn47gewbwxl04370f5ohc` FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio`
--

LOCK TABLES `portfolio` WRITE;
/*!40000 ALTER TABLE `portfolio` DISABLE KEYS */;
INSERT INTO `portfolio` VALUES ('SustainablePortfolio','271e8756-108f-4bab-a01f-c8dccd44bbe5','2025-09-02','Impact Investing - Climate Action','Sustainable Green Fund - Updated',5,7000,15000,750000.00,'2025-09-07','7d45f747-99d3-430e-8a24-de5d416156ce',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Portfolio','57707f67-a235-487a-b132-0975ee3cc8c9','2025-09-07','Growth and Income','Balanced Growth Portfolio',3,NULL,NULL,100000.00,'2025-09-07','7d45f747-99d3-430e-8a24-de5d416156ce',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('SustainablePortfolio','7ad11722-3431-46b0-90d1-6da0b95f24f8','2025-09-02','Impact Investing','Sustainable Green Fund',4,NULL,NULL,500000.00,'2025-09-02','fea17503-7d0e-11f0-a429-84ba590b0166',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Portfolio','aabb4eaa-8fbb-40b6-869c-29723e8c1d0f','2025-09-02','Growth and Income','Balanced Growth Portfolio',3,NULL,NULL,100000.00,'2025-09-02','7d45f747-99d3-430e-8a24-de5d416156ce',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Portfolio','dfb9801c-0ecb-40c5-aa34-fb79e678602a','2025-09-02','Long-term Growth','Green Growth Fund',3,NULL,NULL,150000.00,'2025-09-02','7d45f747-99d3-430e-8a24-de5d416156ce',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `portfolio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portfolio_backup`
--

DROP TABLE IF EXISTS `portfolio_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `portfolio_backup` (
  `portfolio_type` varchar(31) NOT NULL,
  `portfolio_id` char(36) NOT NULL,
  `created_at` date DEFAULT NULL,
  `investment_goal` varchar(255) DEFAULT NULL,
  `portfolio_name` varchar(255) DEFAULT NULL,
  `risk_level` int DEFAULT NULL,
  `total_stress_test` double DEFAULT NULL,
  `total_var` double DEFAULT NULL,
  `total_value` decimal(38,2) DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `compliance_status` varchar(255) DEFAULT NULL,
  `last_updated` date DEFAULT NULL,
  `overall_esg_score` int DEFAULT NULL,
  `client_id` char(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio_backup`
--

LOCK TABLES `portfolio_backup` WRITE;
/*!40000 ALTER TABLE `portfolio_backup` DISABLE KEYS */;
INSERT INTO `portfolio_backup` VALUES ('SustainablePortfolio','13e5b610-1d53-4859-bf04-20d27d94bd5e','2025-09-02',NULL,NULL,NULL,NULL,NULL,NULL,'2025-09-02','Compliant','2025-09-01',8,'995daf18-7c82-11f0-a429-84ba590b0166'),('Portfolio','44afabf1-2f95-4e2d-958c-2be7f9ea7249','2025-09-01',NULL,'Retirement Bonds Secure',NULL,NULL,NULL,NULL,'2025-09-01',NULL,NULL,NULL,'995c2d4c-7c82-11f0-a429-84ba590b0166'),('Portfolio','53558e98-6903-4aed-b350-1f2baa4add11','2025-09-01',NULL,'Equity Growth Fund',NULL,NULL,NULL,NULL,'2025-09-01',NULL,NULL,NULL,'fea17503-7d0e-11f0-a429-84ba590b0166'),('Portfolio','61a84500-27fb-4e04-b086-f7d7f916f509','2025-09-02','Stable Income','Retirement Bonds Secure',1,NULL,NULL,500000.00,'2025-09-02',NULL,NULL,NULL,'995c2d4c-7c82-11f0-a429-84ba590b0166'),('SustainablePortfolio','90cbf3bd-1049-4863-86e3-cba819c8dced','2025-09-02','Impact Investing','Sustainable Green Fund',4,NULL,NULL,500000.00,'2025-09-02','Compliant','2025-09-01',81,'fea17503-7d0e-11f0-a429-84ba590b0166'),('Portfolio','a30beb92-f53b-4f76-8aee-e3e382b33769','2025-09-02','Stable Income','Retirement Bonds Secure',1,NULL,NULL,500000.00,'2025-09-02',NULL,NULL,NULL,'995c2d4c-7c82-11f0-a429-84ba590b0166'),('Portfolio','c1a10adb-882d-4415-972a-266624fc94b7','2025-09-01',NULL,'My Portfolio',NULL,NULL,NULL,NULL,'2025-09-01',NULL,NULL,NULL,'fea17503-7d0e-11f0-a429-84ba590b0166');
/*!40000 ALTER TABLE `portfolio_backup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `risk`
--

DROP TABLE IF EXISTS `risk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `risk` (
  `calculation_date` date DEFAULT NULL,
  `confidence_level` double DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `details_json` longtext,
  `scenario` varchar(255) DEFAULT NULL,
  `time_horizon` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `asset_id` char(36) DEFAULT NULL,
  `risk_id` char(36) NOT NULL,
  PRIMARY KEY (`risk_id`),
  KEY `FKj1bhe7lh4qisygm63ct2xt4in` (`asset_id`),
  CONSTRAINT `FKj1bhe7lh4qisygm63ct2xt4in` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `risk`
--

LOCK TABLES `risk` WRITE;
/*!40000 ALTER TABLE `risk` DISABLE KEYS */;
INSERT INTO `risk` VALUES ('2025-09-03',0.95,'USD','95% 1-day VaR for MSFT','{\"ticker\": \"MSFT\", \"historicalVolatility\": 0.22}',NULL,'1 day','VaR',3000,'5266a00c-8280-4741-b82c-ac4d8e26290f','36162996-70b7-419d-835c-5db3c3223889'),(NULL,NULL,NULL,'Fund volatility',NULL,NULL,NULL,'VAR',3000,'1f6f138b-fb4a-4b7e-9cd6-a56b5d9544c5','395a8421-cf2f-4eb4-af16-2f640ee75a77'),(NULL,NULL,NULL,'Fund volatility',NULL,NULL,NULL,'VAR',3000,'56f2b107-6cdf-4e15-adb7-d076aee60e19','42a9b4e5-552c-4fab-a0bf-847722125e91'),(NULL,NULL,NULL,'99% Expected Shortfall on bond portfolio',NULL,NULL,NULL,'ExpectedShortfall',8000,'3e015d33-1d07-4c91-9a5d-cc10e4a2c9ec','6d97004f-ca42-46bb-b1c6-fcfba322c5d6'),('2025-09-03',0.99,'USD','99% 10-day VaR for corporate bond','{\"method\": \"variance-covariance\", \"volatility\": 0.15}',NULL,'10 days','VaR',5000,'4da98a76-3422-4ab3-9aa1-42082a3d8440','8f03dfad-5c61-411c-9e1f-8a1e83e80ccb'),(NULL,NULL,NULL,'Equity drawdown under recession scenario',NULL,NULL,NULL,'StressTest',40000,'db349d30-5b9e-40e7-b11c-5bafb8f94c04','ab40232a-5731-4564-b8bf-79880922e967'),('2025-09-03',0.99,'USD','Equity drawdown under 2008 scenario','{\"marketDrop\": -20, \"holdingPeriod\": \"10 days\"}','2008 Financial Crisis','10 days','StressTest',12000,'1f6f138b-fb4a-4b7e-9cd6-a56b5d9544c5','c9a06dc6-07f3-4e1b-bf24-f32e8e8c3dde'),(NULL,NULL,NULL,'95% 1-day Value at Risk',NULL,NULL,NULL,'VaR',20000,'07f307f9-678f-4cf7-9b83-5cf5e6647821','d1832740-9c9e-47dd-95d4-79cb711f1171'),('2025-09-03',0.99,'USD','95% 1-day Value at Risk','{\"method\": \"historical simulation\", \"dataPoints\": 250, \"lossThreshold\": 25000}','COVID-19 Market Crash','10 days','StressTest',15000,'1f6f138b-fb4a-4b7e-9cd6-a56b5d9544c5','d5deae6f-5aa4-49f8-ace5-8161443af5d0'),('2025-09-03',0.99,'USD','99% Expected Shortfall on bond portfolio','{\"interestRateShock\": \"+150bps\", \"lossEstimate\": 7000}',NULL,'10 days','ExpectedShortfall',7000,'3e015d33-1d07-4c91-9a5d-cc10e4a2c9ec','e22d7201-1dea-4d42-b74d-eda37c8dc8bb'),(NULL,NULL,NULL,'Credit risk for bond',NULL,NULL,NULL,'VAR',1500,'4da98a76-3422-4ab3-9aa1-42082a3d8440','f4304f7f-1c1d-4709-b8f4-4613fc9019e1'),(NULL,NULL,NULL,'Market downturn scenario',NULL,NULL,NULL,'StressTest',2000,'5266a00c-8280-4741-b82c-ac4d8e26290f','fb745368-9cf9-4ad1-a321-e9f9a89ee9f2');
/*!40000 ALTER TABLE `risk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sustainable_portfolio_esg_scores`
--

DROP TABLE IF EXISTS `sustainable_portfolio_esg_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustainable_portfolio_esg_scores` (
  `sustainable_portfolio_portfolio_id` char(36) NOT NULL,
  `esg_scores` double DEFAULT NULL,
  `esg_scores_key` varchar(255) NOT NULL,
  PRIMARY KEY (`sustainable_portfolio_portfolio_id`,`esg_scores_key`),
  CONSTRAINT `FKecyrqv2pwwgsuykrb7mi7vo11` FOREIGN KEY (`sustainable_portfolio_portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sustainable_portfolio_esg_scores`
--

LOCK TABLES `sustainable_portfolio_esg_scores` WRITE;
/*!40000 ALTER TABLE `sustainable_portfolio_esg_scores` DISABLE KEYS */;
INSERT INTO `sustainable_portfolio_esg_scores` VALUES ('271e8756-108f-4bab-a01f-c8dccd44bbe5',30,'environment'),('271e8756-108f-4bab-a01f-c8dccd44bbe5',26,'governance'),('271e8756-108f-4bab-a01f-c8dccd44bbe5',25,'social');
/*!40000 ALTER TABLE `sustainable_portfolio_esg_scores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sustainable_portfolio_excluded_sectors`
--

DROP TABLE IF EXISTS `sustainable_portfolio_excluded_sectors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustainable_portfolio_excluded_sectors` (
  `sustainable_portfolio_portfolio_id` char(36) NOT NULL,
  `excluded_sectors` varchar(255) DEFAULT NULL,
  KEY `FKptgdc1k8pl32vli06sj7xwdlj` (`sustainable_portfolio_portfolio_id`),
  CONSTRAINT `FKptgdc1k8pl32vli06sj7xwdlj` FOREIGN KEY (`sustainable_portfolio_portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sustainable_portfolio_excluded_sectors`
--

LOCK TABLES `sustainable_portfolio_excluded_sectors` WRITE;
/*!40000 ALTER TABLE `sustainable_portfolio_excluded_sectors` DISABLE KEYS */;
INSERT INTO `sustainable_portfolio_excluded_sectors` VALUES ('271e8756-108f-4bab-a01f-c8dccd44bbe5','tobacco'),('271e8756-108f-4bab-a01f-c8dccd44bbe5','fossil fuels');
/*!40000 ALTER TABLE `sustainable_portfolio_excluded_sectors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sustainable_portfolio_impact_targets`
--

DROP TABLE IF EXISTS `sustainable_portfolio_impact_targets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustainable_portfolio_impact_targets` (
  `sustainable_portfolio_portfolio_id` char(36) NOT NULL,
  `impact_targets` double DEFAULT NULL,
  `impact_targets_key` varchar(255) NOT NULL,
  PRIMARY KEY (`sustainable_portfolio_portfolio_id`,`impact_targets_key`),
  CONSTRAINT `FKn4aca1pjub7vs0uatqcw7eblp` FOREIGN KEY (`sustainable_portfolio_portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sustainable_portfolio_impact_targets`
--

LOCK TABLES `sustainable_portfolio_impact_targets` WRITE;
/*!40000 ALTER TABLE `sustainable_portfolio_impact_targets` DISABLE KEYS */;
INSERT INTO `sustainable_portfolio_impact_targets` VALUES ('271e8756-108f-4bab-a01f-c8dccd44bbe5',30,'carbonReduction'),('271e8756-108f-4bab-a01f-c8dccd44bbe5',5,'communityProjects'),('271e8756-108f-4bab-a01f-c8dccd44bbe5',15,'waterConservation');
/*!40000 ALTER TABLE `sustainable_portfolio_impact_targets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sustainable_portfolio_preferred_sectors`
--

DROP TABLE IF EXISTS `sustainable_portfolio_preferred_sectors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustainable_portfolio_preferred_sectors` (
  `sustainable_portfolio_portfolio_id` char(36) NOT NULL,
  `preferred_sectors` varchar(255) DEFAULT NULL,
  KEY `FK12bb6y3go5d7btovc3aq6h8qr` (`sustainable_portfolio_portfolio_id`),
  CONSTRAINT `FK12bb6y3go5d7btovc3aq6h8qr` FOREIGN KEY (`sustainable_portfolio_portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sustainable_portfolio_preferred_sectors`
--

LOCK TABLES `sustainable_portfolio_preferred_sectors` WRITE;
/*!40000 ALTER TABLE `sustainable_portfolio_preferred_sectors` DISABLE KEYS */;
INSERT INTO `sustainable_portfolio_preferred_sectors` VALUES ('271e8756-108f-4bab-a01f-c8dccd44bbe5','renewable energy'),('271e8756-108f-4bab-a01f-c8dccd44bbe5','education');
/*!40000 ALTER TABLE `sustainable_portfolio_preferred_sectors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sustainable_portfolio_theme_focus`
--

DROP TABLE IF EXISTS `sustainable_portfolio_theme_focus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sustainable_portfolio_theme_focus` (
  `sustainable_portfolio_portfolio_id` char(36) NOT NULL,
  `theme_focus` varchar(255) DEFAULT NULL,
  KEY `FK5bfkqcs3w9hh187r6enia9dxp` (`sustainable_portfolio_portfolio_id`),
  CONSTRAINT `FK5bfkqcs3w9hh187r6enia9dxp` FOREIGN KEY (`sustainable_portfolio_portfolio_id`) REFERENCES `portfolio` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sustainable_portfolio_theme_focus`
--

LOCK TABLES `sustainable_portfolio_theme_focus` WRITE;
/*!40000 ALTER TABLE `sustainable_portfolio_theme_focus` DISABLE KEYS */;
INSERT INTO `sustainable_portfolio_theme_focus` VALUES ('271e8756-108f-4bab-a01f-c8dccd44bbe5','climate'),('271e8756-108f-4bab-a01f-c8dccd44bbe5','human rights'),('271e8756-108f-4bab-a01f-c8dccd44bbe5','renewable energy');
/*!40000 ALTER TABLE `sustainable_portfolio_theme_focus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` char(36) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `client_id` char(36) DEFAULT NULL,
  `raw_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKrb7eox526ilbewv2wuv5bnsrt` (`client_id`),
  CONSTRAINT `FKrl8au09hfjd9742b89li2rb3` FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('67f4e32b-8b6e-11f0-b6e8-84ba590b0166','alice.smith@example.com',_binary '','$2a$10$ExampleHash1!',NULL,'alice.smith',NULL,'Password1!'),('67f4ecec-8b6e-11f0-b6e8-84ba590b0166','bob.johnson@example.com',_binary '','$2a$10$9mzDe.n35RXDdxo3mwRKcOuIpWEUF5wsKbPi6913mXLeNT3WwGLUy',NULL,'bob.johnson',NULL,'SecurePass2@'),('67f4ee77-8b6e-11f0-b6e8-84ba590b0166','carol.williams@example.com',_binary '','$2a$10$ExampleHash3!',NULL,'carol.williams',NULL,'MyPass123#'),('67f4ef5e-8b6e-11f0-b6e8-84ba590b0166','david.brown@example.com',_binary '','$2a$10$ExampleHash4!',NULL,'david.brown',NULL,'StrongPwd4$'),('67f4f017-8b6e-11f0-b6e8-84ba590b0166','emma.jones@example.com',_binary '','$2a$10$ExampleHash5!',NULL,'emma.jones',NULL,'TopSecret5%');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-10 13:25:56
