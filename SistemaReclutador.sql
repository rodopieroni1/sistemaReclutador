-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: sistemareclutador
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `aplicacion`
--

DROP TABLE IF EXISTS `aplicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aplicacion` (
  `idaplicacion` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `id_perfil` int NOT NULL,
  `id_oferta` int NOT NULL,
  `estadoaplicaciones` bit(1) DEFAULT NULL,
  PRIMARY KEY (`idaplicacion`),
  KEY `fk_aplicacion_oferta1_idx` (`id_oferta`),
  KEY `perfil_idx` (`id_perfil`),
  CONSTRAINT `fk_aplicacion_oferta1` FOREIGN KEY (`id_oferta`) REFERENCES `oferta` (`id_oferta`),
  CONSTRAINT `FKcrst8qnbddot1s6r4uoul9sc4` FOREIGN KEY (`id_perfil`) REFERENCES `perfil` (`id_perfil`),
  CONSTRAINT `FKkd0may2oqmemy40ix9afnbndc` FOREIGN KEY (`id_oferta`) REFERENCES `oferta` (`id_oferta`),
  CONSTRAINT `perfil` FOREIGN KEY (`id_perfil`) REFERENCES `perfil` (`id_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aplicacion`
--

LOCK TABLES `aplicacion` WRITE;
/*!40000 ALTER TABLE `aplicacion` DISABLE KEYS */;
INSERT INTO `aplicacion` VALUES (2,'2025-03-15 11:16:42.000000',2,2,_binary ''),(3,'2025-03-15 11:16:42.000000',4,3,_binary '\0'),(5,'2025-03-15 11:16:42.000000',7,3,_binary '\0'),(6,'2025-05-17 09:33:41.631903',3,241,_binary ''),(15,'2025-05-17 10:45:35.520836',37,239,_binary ''),(16,'2025-05-19 09:14:40.991887',37,239,_binary ''),(17,'2025-05-20 11:48:35.621565',36,241,_binary ''),(18,'2025-05-20 14:23:16.971110',36,241,_binary ''),(19,'2025-05-20 14:48:40.150910',36,242,_binary ''),(20,'2025-05-21 09:19:42.328137',36,239,_binary ''),(21,'2025-05-21 09:55:28.917750',36,242,_binary '');
/*!40000 ALTER TABLE `aplicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `archivos`
--

DROP TABLE IF EXISTS `archivos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenido` tinyblob,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archivos`
--

LOCK TABLES `archivos` WRITE;
/*!40000 ALTER TABLE `archivos` DISABLE KEYS */;
/*!40000 ALTER TABLE `archivos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresas`
--

DROP TABLE IF EXISTS `empresas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresas` (
  `id_empresa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `historia_empresa` text,
  `observaciones` text,
  `cuit` bigint NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_empresa`),
  UNIQUE KEY `UK_n7eoj3klpbnacndhifwyhxpso` (`cuit`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresas`
--

LOCK TABLES `empresas` WRITE;
/*!40000 ALTER TABLE `empresas` DISABLE KEYS */;
INSERT INTO `empresas` VALUES (2,'Empresa B','Direccion 456','Historia de Empresa B','Observacion B',123,NULL),(3,'Empresa C','Direccion 789','Historia de Empresa C','Observacion C',1234,NULL),(4,'Empresa 523','Dirección 344','Historia de Empresa 153','Observaciones 730',12345,NULL),(5,'Empresa 189','Dirección 758','Historia de Empresa 221','Observaciones 832',123456,NULL),(6,'Empresa 496','Dirección 982','Historia de Empresa 422','Observaciones 162',654321,NULL),(7,'Empresa 546','Dirección 242','Historia de Empresa 572','Observaciones 135',54321,NULL),(8,'Empresa 956','Dirección 378','Historia de Empresa 18','Observaciones 955',4321,NULL),(9,'Empresa 723','Dirección 749','Historia de Empresa 574','Observaciones 625',1425,NULL),(10,'Empresa 403','Dirección 138','Historia de Empresa 482','Observaciones 994',142536,NULL),(11,'Empresa 526','Dirección 645','Historia de Empresa 647','Observaciones 301',425,NULL),(12,'Empresa 564','Dirección 916','Historia de Empresa 885','Observaciones 678',4253,NULL),(13,'Empresa 735','Dirección 640','Historia de Empresa 994','Observaciones 52',42536,NULL),(14,'Empresa 276','Dirección 225','Historia de Empresa 294','Observaciones 794',2536,NULL),(15,'Empresa 87','Dirección 53','Historia de Empresa 6','Observaciones 867',253,NULL),(16,'Empresa 318','Dirección 989','Historia de Empresa 989','Observaciones 979',536,NULL),(17,'Empresa 926','Dirección 692','Historia de Empresa 682','Observaciones 332',5365,NULL),(18,'Empresa 616','Dirección 84','Historia de Empresa 571','Observaciones 603',748,NULL),(19,'Empresa 302','Dirección 699','Historia de Empresa 588','Observaciones 841',7485,NULL),(20,'Empresa 443','Dirección 693','Historia de Empresa 132','Observaciones 583',74859,NULL),(21,'Empresa 519','Dirección 846','Historia de Empresa 672','Observaciones 823',748596,NULL),(22,'Empresa 98','Dirección 21','Historia de Empresa 809','Observaciones 980',485,NULL),(23,'Empresa 476','Dirección 437','Historia de Empresa 756','Observaciones 468',4859,NULL),(24,'Empresa 74','Dirección 963','Historia de Empresa 594','Observaciones 79',48596,NULL),(25,'Empresa 614','Dirección 831','Historia de Empresa 311','Observaciones 65',859,NULL),(26,'Empresa 389','Dirección 752','Historia de Empresa 591','Observaciones 698',8596,NULL),(27,'Empresa 715','Dirección 483','Historia de Empresa 269','Observaciones 895',596,NULL),(28,'Empresa 669','Dirección 660','Historia de Empresa 291','Observaciones 474',5968,NULL),(29,'Empresa 498','Dirección 68','Historia de Empresa 844','Observaciones 17',999,NULL),(30,'Empresa 554','Dirección 716','Historia de Empresa 920','Observaciones 450',888,NULL),(31,'Empresa 491','Dirección 102','Historia de Empresa 40','Observaciones 890',777,NULL),(32,'Empresa 330','Dirección 978','Historia de Empresa 900','Observaciones 568',666,NULL),(33,'Empresa 138','Dirección 986','Historia de Empresa 517','Observaciones 626',555,NULL),(34,'Empresa 577','Dirección 6','Historia de Empresa 301','Observaciones 487',444,NULL),(35,'Empresa 529','Dirección 186','Historia de Empresa 340','Observaciones 142',333,NULL),(36,'Empresa 689','Dirección 17','Historia de Empresa 20','Observaciones 48',222,NULL),(37,'Empresa 181','Dirección 758','Historia de Empresa 247','Observaciones 961',111,NULL),(38,'Empresa 64','Dirección 436','Historia de Empresa 988','Observaciones 630',1111,NULL),(39,'Empresa 184','Dirección 33','Historia de Empresa 609','Observaciones 947',2222,NULL),(40,'Empresa 909','Dirección 700','Historia de Empresa 776','Observaciones 777',3333,NULL),(41,'Empresa 560','Dirección 465','Historia de Empresa 644','Observaciones 825',4444,NULL),(42,'Empresa 191','Dirección 482','Historia de Empresa 836','Observaciones 733',5555,NULL),(43,'Empresa 155','Dirección 576','Historia de Empresa 414','Observaciones 341',6666,NULL),(44,'Empresa 461','Dirección 284','Historia de Empresa 35','Observaciones 325',7777,NULL),(45,'Empresa 518','Dirección 613','Historia de Empresa 511','Observaciones 717',8888,NULL),(46,'Empresa 52','Dirección 108','Historia de Empresa 383','Observaciones 591',9999,NULL),(47,'Empresa 805','Dirección 254','Historia de Empresa 852','Observaciones 498',9988,NULL),(48,'Empresa 935','Dirección 179','Historia de Empresa 89','Observaciones 910',9977,NULL),(49,'Empresa 282','Dirección 679','Historia de Empresa 549','Observaciones 707',9966,NULL),(50,'Empresa 887','Dirección 314','Historia de Empresa 908','Observaciones 597',9955,NULL),(51,'Empresa 263','Dirección 520','Historia de Empresa 812','Observaciones 500',9944,NULL),(52,'Empresa 63','Dirección 814','Historia de Empresa 882','Observaciones 968',9933,NULL),(53,'Empresa 192','Dirección 56','Historia de Empresa 703','Observaciones 345',9922,NULL),(112,'Importante empresa','sin direccion','-','-',1456,'pieroni.rodrigo@gmail.com'),(174,'Lo Bruno Estructuras','SANTIAGO DEL ESTERO','aaaaa','nada',78899,'pieroni.rodrigo@gmail.com');
/*!40000 ALTER TABLE `empresas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferta`
--

DROP TABLE IF EXISTS `oferta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferta` (
  `id_oferta` int NOT NULL AUTO_INCREMENT,
  `id_empresa` int NOT NULL,
  `nombreOferta` varchar(100) NOT NULL,
  `descripcion_oferta` text,
  `foto_oferta` tinytext,
  `estadoOferta` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_oferta`),
  KEY `FKgbkukihi3etmltt1aods9j8tw` (`id_empresa`),
  CONSTRAINT `FKgbkukihi3etmltt1aods9j8tw` FOREIGN KEY (`id_empresa`) REFERENCES `empresas` (`id_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oferta`
--

LOCK TABLES `oferta` WRITE;
/*!40000 ALTER TABLE `oferta` DISABLE KEYS */;
INSERT INTO `oferta` VALUES (2,2,'Contador','Un párrafo es un conjunto de oraciones que desarrollan una idea principal o tema específico dentro de un texto. Se caracteriza por comenzar con una letra mayúscula y terminar con un punto y aparte, formando un bloque de texto separado. ','images (2).jfif',NULL),(3,3,'Abogado Jr','Un párrafo es un conjunto de oraciones que desarrollan una idea principal o tema específico dentro de un texto. Se caracteriza por comenzar con una letra mayúscula y terminar con un punto y aparte, formando un bloque de texto separado. ','images (2).jfif',NULL),(4,2,'Community Manager','Un párrafo es un conjunto de oraciones que desarrollan una idea principal o tema específico dentro de un texto. Se caracteriza por comenzar con una letra mayúscula y terminar con un punto y aparte, formando un bloque de texto separado. ','images (2).jfif',NULL),(5,2,'Contador','Nueva oferta de empleo','images (2).jfif',NULL),(12,9,'Abogado Jr','Descripción de la Oferta 889','images (2).jfif',NULL),(13,19,'Modelo','Descripción de la Oferta 731','images (2).jfif',NULL),(14,41,'Contador','Descripción de la Oferta 841','images (2).jfif',NULL),(15,40,'Ceo','Descripción de la Oferta 447','images (2).jfif',NULL),(16,30,'Software Dev','Descripción de la Oferta 561','images (2).jfif',NULL),(17,25,'Modelo','Descripción de la Oferta 439','images (2).jfif',NULL),(18,12,'Modelo','Descripción de la Oferta 210','images (2).jfif',NULL),(19,52,'Contador','Descripción de la Oferta 343','images (2).jfif',NULL),(20,42,'Community Manager','Descripción de la Oferta 949','images (2).jfif',NULL),(21,11,'Abogado Jr','Descripción de la Oferta 80','images (2).jfif',NULL),(22,34,'Abogado Jr','Descripción de la Oferta 292','images (2).jfif',NULL),(23,46,'Software Dev','Descripción de la Oferta 989','images (2).jfif',NULL),(24,35,'Contador','Descripción de la Oferta 860','images (2).jfif',NULL),(25,21,'Software Dev','Descripción de la Oferta 459','images (2).jfif',NULL),(26,7,'Abogado Jr','Descripción de la Oferta 258','images (2).jfif',NULL),(27,29,'Ceo','Descripción de la Oferta 593','images (2).jfif',NULL),(28,39,'Software Dev','Descripción de la Oferta 627','images (2).jfif',NULL),(29,51,'Abogado Jr','Descripción de la Oferta 36','images (2).jfif',NULL),(30,3,'Modelo','Descripción de la Oferta 727','images (2).jfif',NULL),(31,10,'Software Dev','Descripción de la Oferta 478','images (2).jfif',NULL),(32,48,'Abogado Jr','Descripción de la Oferta 420','images (2).jfif',NULL),(33,53,'Abogado Jr','Descripción de la Oferta 199','images (2).jfif',NULL),(34,43,'Community Manager','Descripción de la Oferta 453','images (2).jfif',NULL),(35,5,'Modelo','Descripción de la Oferta 190','images (2).jfif',NULL),(36,6,'Community Manager','Descripción de la Oferta 946','images (2).jfif',NULL),(37,47,'Abogado Jr','Descripción de la Oferta 596','images (2).jfif',NULL),(38,4,'Ceo','Descripción de la Oferta 407','images (2).jfif',NULL),(39,33,'Abogado Jr','Descripción de la Oferta 743','images (2).jfif',NULL),(40,23,'Software Dev','Descripción de la Oferta 69','images (2).jfif',NULL),(41,20,'Policia','Descripción de la Oferta 792','images (2).jfif',NULL),(42,31,'Mecanico','Descripción de la Oferta 318','images (2).jfif',NULL),(43,2,'Software Dev','Descripción de la Oferta 44','images (2).jfif',NULL),(44,8,'Maestranza','Descripción de la Oferta 380','images (2).jfif',NULL),(45,38,'Abogado Jr','Descripción de la Oferta 52','images (2).jfif',NULL),(46,18,'Community Manager','Descripción de la Oferta 789','images (2).jfif',NULL),(47,24,'Modelo','Descripción de la Oferta 3','images (2).jfif',NULL),(48,15,'Abogado Jr','Descripción de la Oferta 992','images (2).jfif',NULL),(49,45,'Community Manager','Descripción de la Oferta 243','images (2).jfif',NULL),(50,28,'Modelo','Descripción de la Oferta 301','images (2).jfif',NULL),(51,27,'Community Manager','Descripción de la Oferta 791','images (2).jfif',NULL),(52,50,'Abogado Jr','Descripción de la Oferta 926','images (2).jfif',NULL),(53,49,'Ceo','Descripción de la Oferta 616','images (2).jfif',NULL),(54,13,'Abogado Jr','Descripción de la Oferta 264','images (2).jfif',NULL),(55,32,'Ceo','Descripción de la Oferta 979','images (2).jfif',NULL),(56,22,'Community Manager','Descripción de la Oferta 590','Contador.png',NULL),(57,17,'Maestranza','Descripción de la Oferta 805','images (2).jfif',NULL),(58,36,'Community Manager','Descripción de la Oferta 708','images (2).jfif',NULL),(59,44,'Maestranza','Descripción de la Oferta 70','images (2).jfif',NULL),(60,26,'Community Manager','Descripción de la Oferta 302','images (2).jfif',NULL),(61,16,'Contador','Descripción de la Oferta 723','Contador.png',NULL),(62,37,'Modelo','Descripción de la Oferta 707','Contador.png',NULL),(63,14,'Contador','Descripción de la Oferta 125','Contador.png',NULL),(78,5,'Cocinero','Lo Bruno','Contador.png',NULL),(79,5,'Community Manager','Cesca','Contador.png',NULL),(80,5,'Community Manager','Cesca','Contador.png',NULL),(83,7,'Maestranza','pANADERIA','Contador.png',NULL),(84,4,'Contador','Gemsa automotores selecciona Vendedor','Contador.png',NULL),(85,7,'Cocinero','Oferta Fhurt utomotores','Contador.png',NULL),(86,14,'Contador','oportunidad en Novara','Contador.png',NULL),(87,4,'Maestranza','Nala Neumaticos','Contador.png',NULL),(88,8,'Modelo','Vea','Contador.png',NULL),(89,16,'Modelo','Chango Mas','Contador.png',NULL),(90,14,'Conductor','La Martina','Contador.png',NULL),(91,40,'Contador','copacabana','Contador.png',NULL),(92,12,'Maestranza','La banda','Contador.png',NULL),(93,112,'Conductor','cscsc','Contador.png',NULL),(94,7,'Conductor','Super Nataly','Contador.png',NULL),(95,6,'Maestranza','El principe','Contador.png',NULL),(96,5,'Contador','asddasda','Contador.png',NULL),(99,5,'Chef','fota','Contador.png',NULL),(100,6,'Contador','Belgrano','Contador.png',NULL),(101,9,'Conductor','lA VEREDA','Contador.png',NULL),(102,3,'Contador','VDF','Contador.png',NULL),(103,7,'Maestranza','Oferta San Esteban','Contador.png',NULL),(104,4,'Community Manager','Tito','Contador.png',NULL),(105,10,'Conductor','Telefe','juan-roman-riquelme_416x234.jpg',NULL),(106,4,'Contador','Super Oriente','juan-roman-riquelme_416x234.jpg',NULL),(107,3,'Maestranza','Cerecet','juan-roman-riquelme_416x234.jpg',NULL),(108,29,'Community Manager','Estudio Alsina','juan-roman-riquelme_416x234.jpg',NULL),(109,4,'Community Manager','CrearOferta','juan-roman-riquelme_416x234.jpg',NULL),(110,7,'Contador','OfertaPirelli','juan-roman-riquelme_416x234.jpg',NULL),(111,6,'Conductor','Basbus','juan-roman-riquelme_416x234.jpg',NULL),(112,3,'Contador','casc','juan-roman-riquelme_416x234.jpg',NULL),(113,5,'Community Manager','ElGordo','juan-roman-riquelme_416x234.jpg',NULL),(114,3,'Conductor','sum','juan-roman-riquelme_416x234.jpg',NULL),(115,4,'Contador','Parque Sur','juan-roman-riquelme_416x234.jpg',NULL),(116,8,'Community Manager','Autoahorro','juan-roman-riquelme_416x234.jpg',NULL),(117,4,'Software Dev','novara','juan-roman-riquelme_416x234.jpg',NULL),(228,53,'Conductor','Empleado administrativo','EstudioAlsina.png',NULL),(229,53,'Community Manager','Ingeniero en Sistema','images (1).jfif',NULL),(230,51,'Software Dev','Abogado Jr','cludia.png',NULL),(231,51,'Contador','Service Jr','incertidumbre.jpg',NULL),(232,44,'Contador','Se solicita panadero profecional','fotoPerfil.jpg',NULL),(233,52,'Community Manager','Maestransa','Final-Argentina-vs-France.jpg',NULL),(234,51,'Software Dev','Maestra de grado','cludia.jpg',NULL),(235,53,'Software Dev','Coach Gym Amanecer','tatuajeflaco.jpg',NULL),(236,31,'Software Dev','Verdulero Vea','02e32b67-327d-4fc8-acec-dc5d917253bc.jfif',NULL),(237,50,'Contador','Profesor Ed. Fisica','juan-roman-riquelme_416x234.jpg',NULL),(238,174,'Abogado','Contador','Contador.png',NULL),(239,112,'Contador','Jefe Mantenimiento','Contador.png',NULL),(240,112,'Abogado','Mecanicao','taller-mecanico.jpg',NULL),(241,112,'Contador','eMPRESA pRUEBA','Emulsions-preparation-features-rheonics-viscosity.jpeg',NULL),(242,112,'Abogado','Empresa','images (2).jfif',NULL);
/*!40000 ALTER TABLE `oferta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfil` (
  `id_perfil` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `clave` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `foto_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `documento_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (2,'DNI002','Perfil 4','vmartinez','111111',NULL,'Direccion Dos','http://localhost:8080/uploads/fotos/fotoPerfil.jpg','http://localhost:8080/uploads/documentos/CLAUDIA_MARGARITA_CURRICULUM.docx'),(3,'DNI003','Perfil Tres','jcruzPier','666666',NULL,'Direccion Tres',NULL,NULL),(4,'30712082','Perfil 30','cesper','555555',NULL,'Direccion 30',NULL,NULL),(7,'30712082','Rodrigo','pieroni.rodrigo@gmail.com','1234567','pieroni.rodrigo@gmail.com','manzana 13, lote 12, barrio lomas del golf',NULL,NULL),(36,'30712082','Rodrigo n: Pieroni','rpieroni','$2a$10$FNTkL7htTm7hJYbedeqB9OCkg3O8nEwwA.Bfev1fkWx.XixVfDkey','pieroni.rodrigo@gmail.com','manzana 13, lote 12, barrio lomas del golf','http://localhost:8080/uploads/fotos/fotoPerfil.jpg','http://localhost:8080/uploads/documentos/C-V--Pieroni_Rodrigo.pdf'),(37,'29123689','Claudia Esperguin','cesperguin','$2a$10$Y3J3vSWc6IRqOVSW15SkeuWA7yNLAK0yFd23ijCNjOZzi2r4sZdV.','esperguin.claudia@gmail.com','manzana 13, lote 12, barrio lomas del golf','http://localhost:8080/uploads/fotos/cludia.png','http://localhost:8080/uploads/documentos/DATOS_PERSONALES_ALUMNOS_T.T__1_.pdf');
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `clave` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `tipo_usuario` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'rpieroni','Rodrigo Pieroni','1234567','pieroni.rodrigo@gmail.com','administrador');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-22 11:45:13
