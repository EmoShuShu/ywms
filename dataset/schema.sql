/*
 Navicat Premium Data Transfer

 Source Server         : ywms
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : ywms

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 12/06/2025 01:59:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `reportid` int NOT NULL AUTO_INCREMENT,
  `weekSendReportNumber` int NOT NULL,
  `weekFinishedInTimeReportNumber` int NOT NULL,
  `weekUnfinishedInTimeReportNumber` int NOT NULL,
  `monthSendReportNumber` int NOT NULL,
  `monthFinishedInTimeReportNumber` int NOT NULL,
  `monthUnfinishedInTimeReportNumber` int NOT NULL,
  `yearSendReportNumber` int NOT NULL,
  `yearFinishedInTimeReportNumber` int NOT NULL,
  `yearUnfinishedInTimeReportNumber` int NOT NULL,
  `totalSendReportNumber` int NOT NULL,
  `totalFinishedInTimeReportNumber` int NOT NULL,
  `totalUnfinishedInTimeReportNumber` int NOT NULL,
  PRIMARY KEY (`reportid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of report
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for responseorder
-- ----------------------------
DROP TABLE IF EXISTS `responseorder`;
CREATE TABLE `responseorder`  (
  `responseId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseStatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '1：已完成\n\n​ 2：无法完成',
  `responseUserId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `operatorName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseDepartment` int NOT NULL COMMENT '1 ：故障维修部门\n\n​ 2 ：维护部门\n\n​ 3 : 后勤保障部门',
  `workOrderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`responseId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of responseorder
-- ----------------------------
BEGIN;
INSERT INTO `responseorder` (`responseId`, `responseDescription`, `responseStatus`, `responseUserId`, `operatorName`, `responseDepartment`, `workOrderId`) VALUES ('R320250612001106164', '联单选改身先查。车种白之多所业。任学天清西马。切具本西市线会住色名。青复没出了道。', '1', '3', '测试操作人员', 0, '220250611205744807');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` int NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `identityId` int NOT NULL COMMENT '一对一，一个用户对应一个ID',
  `identityName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `identityNumber` int NOT NULL COMMENT '1 ： 省市各部门人员，申请者\n\n​ 2 ： 各级审批人员\n\n​ 3 ： 操作人员',
  `departmentA` int NOT NULL COMMENT '1 ：区级\n\n​ 2 ：市级\n\n​ 3 : 省级',
  `departmentB` int NOT NULL COMMENT '1 ：区级\n\n​ 2 ：市级\n\n​ 3 : 省级',
  `departmentC` int NOT NULL COMMENT '1 ：故障维修部门\n\n​ 2 ：维护部门\n\n​ 3 : 后勤保障部门',
  PRIMARY KEY (`identityId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`userId`, `password`, `identityId`, `identityName`, `identityNumber`, `departmentA`, `departmentB`, `departmentC`) VALUES (222222, '222222', 1, '测试审批人员', 2, 0, 3, 0), (111111, '111111', 2, '测试申请者', 1, 1, 0, 0), (333333, '333333', 3, '测试操作人员', 3, 0, 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for workorder
-- ----------------------------
DROP TABLE IF EXISTS `workorder`;
CREATE TABLE `workorder`  (
  `orderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `issueDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `orderStatus` int NOT NULL COMMENT '-1 ： 工单被打回\n\n​ 1 : 进行区审批\n\n​ 2 ：进行市审批\n\n​ 3 ：进行省审批\n\n​ 4 ：审批通过\n\n​ 5 : 工单完成\n\n​ 6 : 工单无法完成',
  `applicantName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `applicantId` int NOT NULL COMMENT '1、2、3分别对应区、市、省',
  `applicantIdentity` int NOT NULL,
  `recipientId` int NULL DEFAULT NULL,
  `recipientName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NOT NULL COMMENT '1 ：故障维修问题\n2 ：维护问题 \n3 : 后勤缺失，应交由后勤保障部门',
  `department` int NULL DEFAULT NULL COMMENT '1 ：故障维修部门\n2 ：维护部门 \n3 : 后勤保障部门',
  `sendTime` datetime NOT NULL,
  `finishTime` datetime NULL DEFAULT NULL,
  `deadline` datetime NOT NULL,
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of workorder
-- ----------------------------
BEGIN;
INSERT INTO `workorder` (`orderId`, `issueDescription`, `orderStatus`, `applicantName`, `applicantId`, `applicantIdentity`, `recipientId`, `recipientName`, `type`, `department`, `sendTime`, `finishTime`, `deadline`) VALUES ('1', '1', 1, '1', 1, 1, 1, '1', 1, 1, '2025-06-04 19:03:05', '2025-06-12 19:03:13', '2025-06-16 19:03:16'), ('2', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '1', 1, 1, 0, NULL, 1, 0, '2025-06-11 19:10:54', NULL, '2025-06-11 19:02:21'), ('220250611204417854', '影值包们深。克细领八。接备报区史量感心。', 4, '测试申请者', 2, 1, 0, NULL, 2, 0, '2025-06-11 20:44:18', NULL, '2025-06-11 19:02:21'), ('220250611205435088', '数千老报物强花。却叫此进龙农。题我生内划。', -1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 20:54:35', NULL, '2025-06-11 19:02:21'), ('220250611205715965', '数千老报物强花。却叫此进龙农。题我生内划。', -1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 20:57:16', NULL, '2025-06-11 19:02:21'), ('220250611205744807', '数千老报物强花。却叫此进龙农。题我生内划。', 5, '测试申请者', 2, 1, 3, '测试操作人员', 1, 3, '2025-06-11 20:57:45', '2025-06-12 00:14:59', '2025-06-11 19:02:21'), ('220250611205755138', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 20:57:55', NULL, '2025-06-11 19:02:21'), ('220250611205824482', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 20:58:24', NULL, '2025-06-11 19:02:21'), ('220250611210017672', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 21:00:18', NULL, '2025-06-11 19:02:21'), ('220250611210527576', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 21:05:28', NULL, '2025-06-11 19:02:21'), ('220250611211155137', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 21:11:55', NULL, '2025-06-11 19:02:21'), ('220250611211313134', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 21:13:13', NULL, '2025-06-11 19:02:21'), ('220250611211352442', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '测试申请者', 2, 1, 0, NULL, 1, 0, '2025-06-11 21:13:52', NULL, '2025-06-11 19:02:21'), ('3', '数千老报物强花。却叫此进龙农。题我生内划。', 1, '1', 1, 1, 0, NULL, 1, 0, '2025-06-11 19:12:57', NULL, '2025-06-11 19:02:21');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
