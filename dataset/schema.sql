/*
 Navicat Premium Dump SQL

 Source Server         : ywms
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : ywms

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 17/06/2025 18:11:47
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for responseorder
-- ----------------------------
DROP TABLE IF EXISTS `responseorder`;
CREATE TABLE `responseorder`  (
  `responseId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseStatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '1：已完成\n\n​ 2：无法完成',
  `responseUserId` int NOT NULL,
  `operatorName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responseDepartment` int NOT NULL COMMENT '1 ：故障维修部门\n\n​ 2 ：维护部门\n\n​ 3 : 后勤保障部门',
  `workOrderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`responseId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
  `approverIdA` int NULL DEFAULT NULL,
  `approverIdB` int NULL DEFAULT NULL,
  `approverIdC` int NULL DEFAULT NULL,
  `allocatedId` int NULL DEFAULT NULL,
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
