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
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `occupation` varchar(45) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'John Doe','Male',28,'Computer Scientist','9958554011','johndoe@gmail.com','johndoe'),(2,'Juan Dela Cruz','Male',24,'Student','9174567890','juandelacruz@gmail.com','juandelacruz'),(3,'Maria Santos','Female',31,'Nurse','9223456781','mariasantos@gmail.com','mariasantos'),(4,'Ramon Reyes','Male',43,'Family Driver','9052345678','ramonreyes@gmail.com','ramonreyes'),(5,'Aira Villanueva','Female',28,'Teacher','9368123456','airavillanueva@gmail.com','airavillanueva');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_wallet`
--

DROP TABLE IF EXISTS `customer_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_wallet` (
  `wallet_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `customer_id` (`customer_id`),
  CONSTRAINT `customer_wallet_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_wallet`
--

LOCK TABLES `customer_wallet` WRITE;
/*!40000 ALTER TABLE `customer_wallet` DISABLE KEYS */;
INSERT INTO `customer_wallet` VALUES (1,1,1000.00),(2,2,2000.00),(3,3,750.00),(4,4,800.00),(5,5,1200.00);
/*!40000 ALTER TABLE `customer_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `driver_id` int NOT NULL AUTO_INCREMENT,
  `license_num` varchar(13) DEFAULT NULL,
  `driver_name` varchar(45) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `date_of_employment` date DEFAULT NULL,
  `date_of_resignation` date DEFAULT NULL,
  `password` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `license_num` (`license_num`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'N03-12,123456','Alexander Mejia','Male',19,'9190000123','alexmejia@gmail.com','2025-11-11',NULL,'alexandermejia'),(2,'N12-34-567890','Mark Angelo Cruz','Male',34,'9175557890','markcruz@gmail.com','2023-02-15',NULL,'markcruz'),(3,'P98-76-543210','Liza Mae Navarro','Female',29,'9612345678','lizanavarro@gmail.com','2024-05-10',NULL,'lizanavarro'),(4,'R55-21-334455','Jerome D. Alcantara','Male',41,'9083456789','jeromealcantara@gmail.com','2021-11-01','2025-07-31','jeromealcantara'),(5,'Q33-44-556677','Sheila Ann Dela Pena','Female',37,'9771234567','shieladelapena@gmail.com','2022-08-20',NULL,'sheiladelapena');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver_wallet`
--

DROP TABLE IF EXISTS `driver_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver_wallet` (
  `wallet_id` int NOT NULL AUTO_INCREMENT,
  `driver_id` int DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `driver_id` (`driver_id`),
  CONSTRAINT `driver_wallet_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver_wallet`
--

LOCK TABLES `driver_wallet` WRITE;
/*!40000 ALTER TABLE `driver_wallet` DISABLE KEYS */;
INSERT INTO `driver_wallet` VALUES (1,1,242.00),(2,2,165.00),(3,3,120.00),(4,4,220.00),(5,5,140.00);
/*!40000 ALTER TABLE `driver_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `vehicle_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `pickup_point` varchar(100) DEFAULT NULL,
  `dropoff_point` varchar(100) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `fulfillment_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `customer_id` (`customer_id`),
  KEY `vehicle_id` (`vehicle_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,1,1,'2024-12-12','07:42:00','Universidad ng Pilipinas','De La Salle University',242.00,'COMPLETED'),(2,2,2,'2025-11-05','08:15:00','DLSU Taft Gate, Malate, Manila','SM Mall of Asia, Pasay',165.00,'COMPLETED'),(3,3,3,'2025-11-06','18:42:00','Ayala Malls Manila Bay, Pasay City','Solaire Manila',120.00,'COMPLETED'),(4,4,4,'2025-11-07','22:05:00','Glorietta 4, Makati','BGC High Street, Taguig',220.00,'COMPLETED'),(5,5,5,'2025-11-08','07:30:00','Robinsons Manila','SM City Manila',140.00,'COMPLETED');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `driver_id` int DEFAULT NULL,
  `registration_num` varchar(50) DEFAULT NULL,
  `plate_num` varchar(7) DEFAULT NULL,
  `car_brand` varchar(50) DEFAULT NULL,
  `model_name` varchar(50) DEFAULT NULL,
  `color` varchar(30) DEFAULT NULL,
  `number_of_seats` int DEFAULT NULL,
  `fuel_type` varchar(20) DEFAULT NULL,
  `year_acquired` year DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`),
  UNIQUE KEY `registration_num` (`registration_num`),
  UNIQUE KEY `plate_num` (`plate_num`),
  UNIQUE KEY `driver_id_2` (`driver_id`),
  UNIQUE KEY `unique_driver` (`driver_id`),
  UNIQUE KEY `uq_driver` (`driver_id`),
  KEY `driver_id` (`driver_id`),
  CONSTRAINT `vehicle_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,1,'REG-2023-001','NBK3149','Dodge','Challenger','Black',4,'Gasoline',2024),(2,2,'REG-2023-002','NDX4521','Toyota','Vios 1.3 XE','Silver',4,'Gasoline',2022),(3,3,'REG-2024-003','NAH3186','Honda','City 1.5 S','Crystal Black',4,'Gasoline',2023),(4,4,'REG-2022-004','NBM7724','Mitsubishi','Xpander GLS','Titanium Gray',6,'Gasoline',2021),(5,5,'REG-2023-005','NCP6409','Hyundai','Accent 1.6 CRDi','Polar White',4,'Diesel',2022);
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

-- Dump completed on 2025-11-18 13:42:47
