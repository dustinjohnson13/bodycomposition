DROP SCHEMA IF EXISTS bodycomposition;
CREATE SCHEMA bodycomposition;

connect bodycomposition;

DROP TABLE IF EXISTS `daily_entry`;
CREATE TABLE `daily_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `weight` decimal(4,1) NOT NULL,
  `body_fat` decimal(3,1) NOT NULL,
  `water_percentage` decimal(3,1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `date` (`date`),
  KEY `daily_entry_water_percentage_idx` (`water_percentage`)
);

DROP TABLE IF EXISTS `daily_trend`;
CREATE TABLE `daily_trend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `weight` decimal(4,1) NOT NULL,
  `body_fat` decimal(3,1) NOT NULL,
  `water_percentage` decimal(3,1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `date` (`date`),
  KEY `daily_trend_water_percentage_idx` (`water_percentage`)
);