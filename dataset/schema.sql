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

 Date: 19/06/2025 15:53:52
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
-- Records of report
-- ----------------------------

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
  `approverIdA` int NULL DEFAULT NULL,
  `approverIdB` int NULL DEFAULT NULL,
  `approverIdC` int NULL DEFAULT NULL,
  PRIMARY KEY (`responseId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of responseorder
-- ----------------------------

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
  PRIMARY KEY (`identityId` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (33333333, '33333333Aa', 9, '测试3级操作人员', 3, 0, 0, 3);
INSERT INTO `user` VALUES (33332222, '33332222Aa', 8, '测试2级操作人员', 3, 0, 0, 2);
INSERT INTO `user` VALUES (33331111, '33331111Aa', 7, '测试1级操作人员', 3, 0, 0, 1);
INSERT INTO `user` VALUES (22223333, '22223333Aa', 6, '测试3级审批人员', 2, 0, 3, 0);
INSERT INTO `user` VALUES (22222222, '22222222Aa', 5, '测试2级审批人员', 2, 0, 2, 0);
INSERT INTO `user` VALUES (22221111, '22221111Aa', 4, '测试1级审批人员', 2, 0, 1, 0);
INSERT INTO `user` VALUES (11113333, '11113333Aa', 3, '测试3级人员', 1, 3, 0, 0);
INSERT INTO `user` VALUES (11112222, '11112222Aa', 2, '测试2级申请人员', 1, 2, 0, 0);
INSERT INTO `user` VALUES (11111111, '11111111Aa', 1, '测试1级申请人员', 1, 1, 0, 0);

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

-- ----------------------------
-- Records of workorder
-- ----------------------------
INSERT INTO `workorder` VALUES ('3928758329', '飞机翅膀断了', 1, '11111111', 1, 1, NULL, NULL, 1, NULL, '2025-06-04 11:18:29', NULL, '2025-06-27 11:18:49', NULL, NULL, NULL, 4);

SET FOREIGN_KEY_CHECKS = 1;
