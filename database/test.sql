/*
Navicat MySQL Data Transfer

Source Server         : reevoo
Source Server Version : 80020
Source Host           : reevoo-test-beta.crvfzsr4389e.us-east-1.rds.amazonaws.com:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-08-10 10:09:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for edit_log
-- ----------------------------
DROP TABLE IF EXISTS `edit_log`;
CREATE TABLE `edit_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `editor_id` bigint NOT NULL,
  `edit_time` datetime NOT NULL,
  `shortener_id` varchar(24) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `editor_id` (`editor_id`),
  CONSTRAINT `edit_log_ibfk_1` FOREIGN KEY (`editor_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shorten_log
-- ----------------------------
DROP TABLE IF EXISTS `shorten_log`;
CREATE TABLE `shorten_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `short_url` varchar(6) NOT NULL,
  `creator_id` bigint NOT NULL,
  `create_time` datetime NOT NULL,
  `visit_count` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `short_url` (`short_url`),
  KEY `creator_id` (`creator_id`),
  CONSTRAINT `shorten_log_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `role` int NOT NULL,
  `visit_count` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for visit_log
-- ----------------------------
DROP TABLE IF EXISTS `visit_log`;
CREATE TABLE `visit_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shortener_id` varchar(24) NOT NULL,
  `visit_time` datetime NOT NULL,
  `ip` varchar(15) NOT NULL,
  `device` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
