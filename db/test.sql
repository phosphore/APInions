/*
Navicat MySQL Data Transfer

Source Server         : TrinityDB
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2013-10-02 21:18:13
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `account`
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
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'admin@local', 'admin', '00:00:00 01/01/1970');

-- ----------------------------
-- Table structure for `play_evolutions`
-- ----------------------------
DROP TABLE IF EXISTS `play_evolutions`;
CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text,
  `revert_script` text,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of play_evolutions
-- ----------------------------

-- ----------------------------
-- Table structure for `questions`
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions` (
  `QuestionA` text,
  `QuestionB` text,
  `QuestionC` text,
  `QuestionD` text,
  `QuestionE` text,
  `QuestionF` text,
  `QuestionG` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES ('Question A', 'Question B', 'Question C', 'Question D', 'Question E', 'Question F', 'Question G');

-- ----------------------------
-- Table structure for `votes`
-- ----------------------------
DROP TABLE IF EXISTS `votes`;
CREATE TABLE `votes` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Vote` int(11) NOT NULL,
  `IP` text NOT NULL,
  `Time` text NOT NULL,
  `par_a` text,
  `par_b` text,
  `par_c` text,
  `par_d` text,
  `par_e` text,
  `par_f` text,
  `par_g` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of votes
-- ----------------------------
INSERT INTO `votes` VALUES ('1', '3', '186.249.80.3', '16:33:13 2013/09/24', '-', '-', '-', null, null, null, null);
INSERT INTO `votes` VALUES ('2', '4', '77.238.32.254', '16:37:15 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('3', '3', '8.8.8.8', '16:40:16 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('4', '4', '8.8.4.4', '16:55:18 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('5', '5', '92.19.243.138', '17:06:05 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('6', '4', '92.19.243.138', '17:11:07 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('7', '3', '92.19.243.138', '17:16:10 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('8', '4', '92.19.243.138', '17:32:11 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('9', '5', '92.19.243.138', '17:33:13 2013/09/24', '-', '-', '-', '-', '-', '-', '-');
INSERT INTO `votes` VALUES ('10', '5', '0.0.0.0', '17:42:14 2013/09/24', '-', '-', '-', '-', '-', '-', null);
