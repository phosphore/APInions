/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 23/09/2013 16:57:58
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `registration_date` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for votes
-- ----------------------------
DROP TABLE IF EXISTS `votes`;
CREATE TABLE `votes` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Vote` int(11) NOT NULL,
  `IP` text NOT NULL,
  `Time` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Tests Records 
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'admin@local', 'admin', '00:00:00 01/01/1970');
INSERT INTO `votes` VALUES ('1', '7', '186.249.80.3', '16:33:13 2013/09/24');
INSERT INTO `votes` VALUES ('2', '8', '77.238.32.254', '16:37:15 2013/09/24');
INSERT INTO `votes` VALUES ('3', '9', '8.8.8.8', '16:40:16 2013/09/24');
INSERT INTO `votes` VALUES ('4', '10', '8.8.4.4', '16:55:18 2013/09/24');
INSERT INTO `votes` VALUES ('5', '8', '92.19.243.138', '17:06:05 2013/09/24');
INSERT INTO `votes` VALUES ('6', '9', '92.19.243.138', '17:11:07 2013/09/24');
INSERT INTO `votes` VALUES ('7', '8', '92.19.243.138', '17:16:10 2013/09/24');
INSERT INTO `votes` VALUES ('8', '10', '92.19.243.138', '17:32:11 2013/09/24');
INSERT INTO `votes` VALUES ('9', '8', '92.19.243.138', '17:33:13 2013/09/24');
INSERT INTO `votes` VALUES ('10', '10', '0.0.0.0', '17:42:14 2013/09/24');
