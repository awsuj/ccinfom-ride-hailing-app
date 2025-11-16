CREATE DATABASE  IF NOT EXISTS `ridehailing` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ridehailing`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: ridehailing
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL,
  `customer_name` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `occupation` varchar(45) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone_number` bigint DEFAULT NULL,
  `payment_details` varchar(20) DEFAULT NULL,
  `average_rating` double DEFAULT NULL,
  `current_transaction` int DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `current_transaction` (`current_transaction`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`current_transaction`) REFERENCES `transactions` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'John Doe','Male',28,'Computer Scientist','Brgy. 105 Vitas, Las Piñas, Metro Manila',9958554011,'Cash',4.5,NULL),(2,'Juan Dela Cruz','Male',24,'Student','Brgy. 715, Malate, Manila, NCR',9174567890,'GCash',4.6,NULL),(3,'Maria Santos','Female',31,'Nurse','Brgy. Little Baguio, San Juan City, NCR',9223456781,'Credit Card',4.9,NULL),(4,'Ramon Reyes','Male',43,'Family Driver','Brgy. Bangkal, Makati City, NCR',9052345678,'Cash',4.1,NULL),(5,'Aira Villanueva','Female',28,'Teacher','Brgy. Buhangin, Mandaluyong City, NCR',9368123456,'Maya',4.3,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `driver_id` int NOT NULL,
  `license_num` varchar(13) DEFAULT NULL,
  `driver_name` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone_number` bigint DEFAULT NULL,
  `date_of_employment` date DEFAULT NULL,
  `date_of_resignation` date DEFAULT NULL,
  `total_income` double DEFAULT NULL,
  `average_rating` double DEFAULT NULL,
  `current_transaction` int DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  KEY `current_transaction` (`current_transaction`),
  CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`current_transaction`) REFERENCES `transactions` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'N03-12,123456','Alexander Mejia','Male',19,'24A J. Ruiz St. Brg. Salapan, San Juan City',9190000123,'2025-11-11',NULL,42000,4.7,NULL),(2,'N12-34-567890','Mark Angelo Cruz','Male',34,'Brgy. San Antonio, Pasig City, NCR',9175557890,'2023-02-15',NULL,385000,4.7,NULL),(3,'P98-76-543210','Liza Mae Navarro','Female',29,'Brgy. Poblacion, Makati City, NCR',9612345678,'2024-05-10',NULL,212500,4.5,NULL),(4,'R55-21-334455','Jerome D. Alcantara','Male',41,'Brgy. Dolores, Taytay, Rizal',9083456789,'2021-11-01','2025-07-31',640000,4.2,NULL),(5,'Q33-44-556677','Sheila Ann Dela Pena','Female',37,'Brgy. Dagat, Valenzuela City, NCR',9771234567,'2022-08-20',NULL,498300,4.8,NULL);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL,
  `customer_id` int DEFAULT NULL,
  `driver_id` int DEFAULT NULL,
  `vehicle_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `pickup_point` varchar(100) DEFAULT NULL,
  `dropoff_point` varchar(100) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `fulfillment_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `customer_id` (`customer_id`),
  KEY `driver_id` (`driver_id`),
  KEY `vehicle_id` (`vehicle_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`),
  CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,1,1,1,'2024-12-12','07:42:00','Universidad ng Pilipinas','De La Salle University',560,242.00,'Completed'),(2,2,2,2,'2025-11-05','08:15:00','DLSU Taft Gate, Malate, Manila','SM Mall of Asia, Pasay',800,165.00,'Completed'),(3,3,3,3,'2025-11-06','18:42:00','Ayala Malls Manila Bay, Pasay City','Solaire Manila',707,120.00,'Completed'),(4,4,4,4,'2025-11-07','22:05:00','Glorietta 4, Makati','BGC High Street, Taguig',660,220.00,'Completed'),(5,5,5,5,'2025-11-08','07:30:00','Robinsons Manila','SM City Manila',580,140.00,'Completed');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vehicle_id` int NOT NULL,
  `driver_id` int DEFAULT NULL,
  `registration_num` varchar(50) DEFAULT NULL,
  `plate_num` varchar(7) DEFAULT NULL,
  `car_brand` varchar(50) DEFAULT NULL,
  `model_name` varchar(50) DEFAULT NULL,
  `color` varchar(30) DEFAULT NULL,
  `number_of_seats` int DEFAULT NULL,
  `service_record` text,
  `fuel_type` varchar(20) DEFAULT NULL,
  `year_acquired` year DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`),
  KEY `driver_id` (`driver_id`),
  CONSTRAINT `vehicle_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,1,'REG-2023-001','NBK3149','Dodge','Challenger','Black',NULL,'Regular maintenance up to date','Gasoline',2024),(2,2,'REG-2023-002','NDX4521','Toyota','Vios 1.3 XE','Silver',NULL,'10-km PMS at Toyota Manila Bay (2024-05-12)','Gasoline',2022),(3,3,'REG-2024-003','NAH3186','Honda','City 1.5 S','Crystal Black',NULL,'Brake pads replaced (2025-01-18)','Gasoline',2023),(4,4,'REG-2022-004','NBM7724','Mitsubishi','Xpander GLS','Titanium Gray',NULL,'40k-km service (2025-03-08)','Gasoline',2021),(5,5,'REG-2023-005','NCP6409','Hyundai','Accent 1.6 CRDi','Polar White',NULL,'Fuel filter change (2024-11-02)','Diesel',2022);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-16 20:04:18
