/*
Navicat MySQL Data Transfer

Source Server         : 12343
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : ip_v0.3.0

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-08-31 16:20:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ip_company`
-- ----------------------------
DROP TABLE IF EXISTS `ip_company`;
CREATE TABLE `ip_company` (
  `CO_ID` varchar(60) NOT NULL,
  `HIRER_ID` varchar(60) NOT NULL COMMENT '租户号',
  `CO_CODE` varchar(30) NOT NULL,
  `PARENT_CO_ID` varchar(60) DEFAULT NULL COMMENT '上级单位编号',
  `CO_NAME` varchar(30) DEFAULT NULL COMMENT '单位名称',
  `CO_FULLNAME` varchar(50) DEFAULT NULL COMMENT '单位全称',
  `LINKMAN` varchar(20) DEFAULT NULL COMMENT '单位联系人',
  `IS_ENABLED` varchar(1) DEFAULT '1',
  `CO_DESC` varchar(300) DEFAULT NULL COMMENT '描述',
  `LEVEL_NUM` int(2) DEFAULT '1' COMMENT '部门级别',
  `DEPT_DETAIL` varchar(300) DEFAULT NULL,
  `DISP_ORDER` int(10) DEFAULT '0' COMMENT '顺序序号',
  PRIMARY KEY (`CO_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_HIRER` (`HIRER_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_COMPA` (`PARENT_CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_company
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `ip_dictionary`;
CREATE TABLE `ip_dictionary` (
  `dic_id` varchar(60) NOT NULL COMMENT '主键ID',
  `dic_type` varchar(30) DEFAULT NULL COMMENT '字典表类型',
  `dic_name` varchar(50) DEFAULT NULL COMMENT '字典名称',
  PRIMARY KEY (`dic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_dictionary_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ip_dictionary_detail`;
CREATE TABLE `ip_dictionary_detail` (
  `the_id` varchar(60) NOT NULL COMMENT '主键ID',
  `dic_id` varchar(60) NOT NULL COMMENT '字典表ID',
  `detail_key` varchar(60) DEFAULT NULL COMMENT '键值',
  `detail_info` varchar(100) DEFAULT NULL COMMENT '字典详细信',
  PRIMARY KEY (`the_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_dictionary_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_hirer`
-- ----------------------------
DROP TABLE IF EXISTS `ip_hirer`;
CREATE TABLE `ip_hirer` (
  `HIRER_ID` varchar(60) NOT NULL,
  `HIRER_NAME` varchar(30) NOT NULL COMMENT '租户名称',
  `HIRER_SHORT_NAME` varchar(20) DEFAULT NULL COMMENT '租户简称',
  `PASSWORD` varchar(50) NOT NULL,
  `LINKMAN` varchar(20) NOT NULL COMMENT '联系人姓名',
  `PHONE_NO` varchar(14) DEFAULT NULL COMMENT '联系人电话',
  `CELLPHONE_NO` varchar(14) NOT NULL COMMENT '联系人手机',
  `SEX` varchar(1) DEFAULT NULL COMMENT '联系人性别',
  `DUTY` varchar(20) DEFAULT NULL COMMENT '联系人职务',
  `EMAIL` varchar(30) DEFAULT NULL,
  `FAX` varchar(13) DEFAULT NULL,
  `HIRER_TYPE` varchar(10) DEFAULT NULL COMMENT '租户类型',
  `REGION` varchar(20) DEFAULT NULL COMMENT '所属地区',
  `ADDRESS` varchar(50) DEFAULT NULL COMMENT '通讯地址',
  `WEBSITE` varchar(50) DEFAULT NULL COMMENT '网址',
  `POSTCODE` varchar(6) DEFAULT NULL COMMENT '邮编',
  `SALT` varchar(50) DEFAULT NULL,
  `login_ts` bigint(30) DEFAULT NULL,
  `HIRER_NO` varchar(30) NOT NULL COMMENT '租户号',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` date DEFAULT NULL COMMENT '更新日期',
  `HIRER_LOGO_URL` varchar(100) DEFAULT NULL COMMENT '租户LOGO路径',
  `HIRER_PIC_URL` varchar(100) DEFAULT NULL COMMENT '租户个人设置头像',
  `LOGIN_NAME` varchar(45) DEFAULT NULL COMMENT '登陆账号',
  PRIMARY KEY (`HIRER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_hirer
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_menu`
-- ----------------------------
DROP TABLE IF EXISTS `ip_menu`;
CREATE TABLE `ip_menu` (
  `MENU_ID` varchar(60) NOT NULL,
  `PARENT_MENU_NAME` varchar(50) NOT NULL,
  `PARENT_MENU_ID` varchar(60) DEFAULT NULL COMMENT '上级菜单编号',
  `MENU_NAME` varchar(20) NOT NULL,
  `LEVEL_NUM` varchar(10) NOT NULL DEFAULT '1' COMMENT '菜单层级(值集1，2，3)',
  `DISP_ORDER` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `IS_LEAF` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否末级节点(1-是，0-否)',
  `URL` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `AUTH_LEVEL` varchar(1) NOT NULL DEFAULT '0' COMMENT '授权级别(0,1,2,3)',
  `IS_SHOW` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否显示(1是,0否)',
  `MENU_DESC` varchar(40) DEFAULT NULL COMMENT '菜单描述',
  `IS_JUMP` varchar(1) DEFAULT NULL,
  `MENU_LOGO` varchar(100) DEFAULT NULL COMMENT '菜单logo的，对应前端的class',
  PRIMARY KEY (`MENU_ID`),
  KEY `FK_IP_MENU_REFERENCE_IP_MENU` (`PARENT_MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_region`
-- ----------------------------
DROP TABLE IF EXISTS `ip_region`;
CREATE TABLE `ip_region` (
  `THE_ID` varchar(60) NOT NULL COMMENT '区划ID',
  `THE_CODE` varchar(60) DEFAULT NULL COMMENT '区划编码',
  `THE_NAME` varchar(100) DEFAULT NULL COMMENT '区划名称',
  `PARENT_ID` varchar(60) DEFAULT NULL COMMENT '父级ID',
  `IS_VALID` varchar(2) DEFAULT '1' COMMENT '是否启用区划,0:未启用 1:已启用',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` date DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`THE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_region
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_role`
-- ----------------------------
DROP TABLE IF EXISTS `ip_role`;
CREATE TABLE `ip_role` (
  `ROLE_ID` varchar(60) NOT NULL,
  `HIRER_ID` varchar(60) NOT NULL,
  `CO_ID` varchar(60) DEFAULT NULL,
  `ROLE_NAME` varchar(20) DEFAULT NULL,
  `ROLE_TYPE` varchar(2) DEFAULT '1',
  `ROLE_DESC` varchar(300) DEFAULT NULL COMMENT '角色描述',
  `DISP_ORDER` int(10) DEFAULT '0' COMMENT '排序序号',
  PRIMARY KEY (`ROLE_ID`),
  KEY `FK_IP_ROLE_REFERENCE_IP_HIRER` (`HIRER_ID`),
  KEY `FK_IP_ROLE_REFERENCE_IP_COMPA` (`CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_role
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `ip_role_menu`;
CREATE TABLE `ip_role_menu` (
  `THE_ID` varchar(60) NOT NULL,
  `MENU_ID` varchar(60) NOT NULL,
  `ROLE_ID` varchar(60) NOT NULL,
  `MENU_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`THE_ID`),
  KEY `FK_IP_ROLE__REFERENCE_IP_ROLE` (`ROLE_ID`),
  KEY `FK_IP_ROLE__REFERENCE_IP_MENU` (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_user`
-- ----------------------------
DROP TABLE IF EXISTS `ip_user`;
CREATE TABLE `ip_user` (
  `USER_ID` varchar(60) NOT NULL,
  `HIRER_ID` varchar(60) NOT NULL,
  `LOGIN_NAME` varchar(20) NOT NULL COMMENT '登录名',
  `USER_NAME` varchar(20) NOT NULL,
  `USER_SEX` varchar(1) DEFAULT NULL COMMENT '用户性别 （1男0女）',
  `PASSWORD` varchar(50) NOT NULL,
  `USER_EMAIL` varchar(30) DEFAULT NULL,
  `PHONE_NO` varchar(14) DEFAULT NULL COMMENT '电话号码',
  `CELLPHONE_NO` varchar(30) DEFAULT NULL COMMENT '手机号码',
  `IS_ENABLED` varchar(20) DEFAULT '1',
  `EMPLOYEE_NO` varchar(20) DEFAULT NULL COMMENT '员工号',
  `DUTY` varchar(20) DEFAULT NULL COMMENT '职务',
  `NATIVE_PLACE` varchar(40) DEFAULT NULL COMMENT '原籍',
  `GRADUATE_SCHOOL` varchar(20) DEFAULT NULL COMMENT '毕业学校',
  `MAJOR` varchar(30) DEFAULT NULL COMMENT '专业',
  `EDUCATION` varchar(20) DEFAULT NULL COMMENT '学历',
  `GRADUATIOIN_TIME` date DEFAULT NULL COMMENT '毕业时间',
  `REMARK` varchar(50) DEFAULT NULL,
  `SALT` varchar(50) DEFAULT NULL,
  `login_ts` bigint(30) DEFAULT NULL,
  `USER_PIC_URL` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `EXTENSION` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `FK_IP_USER_REFERENCE_IP_HIRER` (`HIRER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_user_company`
-- ----------------------------
DROP TABLE IF EXISTS `ip_user_company`;
CREATE TABLE `ip_user_company` (
  `THE_ID` varchar(60) NOT NULL,
  `USER_ID` varchar(60) NOT NULL,
  `CO_ID` varchar(60) NOT NULL,
  `CO_CODE` varchar(30) NOT NULL,
  `CO_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`THE_ID`),
  KEY `FK_IP_UC_REFERENCE_USER` (`USER_ID`),
  KEY `FK_IP_UC_REFERENCE_COMPA` (`CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_company
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_user_hide_menu`
-- ----------------------------
DROP TABLE IF EXISTS `ip_user_hide_menu`;
CREATE TABLE `ip_user_hide_menu` (
  `user_id` varchar(60) DEFAULT NULL COMMENT '用户ID（关联用户表或租户表）',
  `menu_id` varchar(60) DEFAULT NULL COMMENT '菜单ID，关联菜单表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_hide_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `ip_user_role`;
CREATE TABLE `ip_user_role` (
  `THE_ID` varchar(60) NOT NULL,
  `USER_ID` varchar(60) DEFAULT NULL,
  `ROLE_ID` varchar(60) DEFAULT NULL,
  `CO_ID` varchar(60) DEFAULT NULL,
  `IS_PART_TIME` varchar(2) DEFAULT NULL COMMENT '是否兼职0：不是兼职  1：兼职',
  `DISP_ORDER` int(10) DEFAULT '0' COMMENT '排序序号',
  PRIMARY KEY (`THE_ID`),
  KEY `FK_IP_UR_REFERENCE_ROLE` (`ROLE_ID`),
  KEY `FK_IP_UR_REFERENCE_USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_role
-- ----------------------------
