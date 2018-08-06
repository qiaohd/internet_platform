/*
Navicat MySQL Data Transfer

Source Server         : mylocal
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : my_ip_dev

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2016-11-17 09:19:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ask_for_leave`
-- ----------------------------
DROP TABLE IF EXISTS `ask_for_leave`;
CREATE TABLE `ask_for_leave` (
  `id` varchar(50) character set utf8 NOT NULL,
  `start_date` varchar(50) character set utf8 NOT NULL,
  `end_date` varchar(50) character set utf8 NOT NULL,
  `type` varchar(50) character set utf8 NOT NULL,
  `reason` varchar(100) character set utf8 default NULL,
  `status` int(1) default '1',
  `uid` varchar(50) character set utf8 default NULL,
  `proposer` varchar(50) character set utf8 NOT NULL,
  `cur_processinstance_id` varchar(100) character set utf8 default NULL,
  `task_id` varchar(255) character set utf8 default NULL,
  `create_time` timestamp NOT NULL default '0000-00-00 00:00:00' on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of ask_for_leave
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_scheme`
-- ----------------------------
DROP TABLE IF EXISTS `gen_scheme`;
CREATE TABLE `gen_scheme` (
  `id` varchar(64) collate utf8_bin NOT NULL COMMENT '编号',
  `scheme_name` varchar(200) collate utf8_bin default NULL COMMENT '名称',
  `category` varchar(2000) collate utf8_bin default NULL COMMENT '分类',
  `package_name` varchar(500) collate utf8_bin default NULL COMMENT '生成包路径',
  `module_name` varchar(30) collate utf8_bin default NULL COMMENT '生成模块名',
  `sub_module_name` varchar(30) collate utf8_bin default NULL COMMENT '生成子模块名',
  `function_name` varchar(500) collate utf8_bin default NULL COMMENT '生成功能名',
  `function_name_simple` varchar(100) collate utf8_bin default NULL COMMENT '生成功能名（简写）',
  `function_author` varchar(100) collate utf8_bin default NULL COMMENT '生成功能作者',
  `gen_table_id` varchar(200) collate utf8_bin default NULL COMMENT '生成表编号',
  `create_by` varchar(64) collate utf8_bin default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(64) collate utf8_bin default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) collate utf8_bin default NULL COMMENT '备注信息',
  `del_flag` char(1) collate utf8_bin NOT NULL default '0' COMMENT '删除标记（0：正常；1：删除）',
  `schemeType` varchar(10) collate utf8_bin default '',
  `replaceFile` varchar(10) collate utf8_bin default '',
  PRIMARY KEY  (`id`),
  KEY `gen_scheme_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='生成方案';

-- ----------------------------
-- Records of gen_scheme
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_table`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` varchar(64) collate utf8_bin NOT NULL COMMENT '编号',
  `table_name` varchar(200) collate utf8_bin default NULL COMMENT '名称',
  `table_comments` varchar(500) collate utf8_bin default NULL COMMENT '描述',
  `class_name` varchar(100) collate utf8_bin default NULL COMMENT '实体类名称',
  `parent_table` varchar(200) collate utf8_bin default NULL COMMENT '关联父表',
  `parent_table_fk` varchar(100) collate utf8_bin default NULL COMMENT '关联父表外键',
  `create_by` varchar(64) collate utf8_bin default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(64) collate utf8_bin default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) collate utf8_bin default NULL COMMENT '备注信息',
  `del_flag` char(1) collate utf8_bin NOT NULL default '0' COMMENT '删除标记（0：正常；1：删除）',
  `layout` varchar(30) collate utf8_bin default NULL COMMENT '页面布局',
  `isGen` char(1) collate utf8_bin NOT NULL default '0',
  `hirer_id` varchar(50) collate utf8_bin default NULL,
  PRIMARY KEY  (`id`),
  KEY `gen_table_name` (`table_name`),
  KEY `gen_table_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_table_column`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `id` varchar(64) collate utf8_bin NOT NULL COMMENT '编号',
  `gen_table_id` varchar(64) collate utf8_bin default NULL COMMENT '归属表编号',
  `column_name` varchar(200) collate utf8_bin default NULL COMMENT '名称',
  `column_comments` varchar(500) collate utf8_bin default NULL COMMENT '描述',
  `jdbc_type` varchar(100) collate utf8_bin default NULL COMMENT '列的数据类型的字节长度',
  `java_type` varchar(500) collate utf8_bin default NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) collate utf8_bin default NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) collate utf8_bin default NULL COMMENT '是否主键',
  `is_null` char(1) collate utf8_bin default NULL COMMENT '是否可为空',
  `is_insert` char(1) collate utf8_bin default NULL COMMENT '是否为插入字段',
  `is_edit` char(1) collate utf8_bin default NULL COMMENT '是否编辑字段',
  `is_list` char(1) collate utf8_bin default NULL COMMENT '是否列表字段',
  `is_query` char(1) collate utf8_bin default NULL COMMENT '是否查询字段',
  `query_type` varchar(200) collate utf8_bin default NULL COMMENT '查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）',
  `show_type` varchar(200) collate utf8_bin default NULL COMMENT '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）',
  `dict_type` varchar(200) collate utf8_bin default NULL COMMENT '字典类型',
  `settings` varchar(2000) collate utf8_bin default NULL COMMENT '其它设置（扩展字段JSON）',
  `sort` decimal(10,0) default NULL COMMENT '排序（升序）',
  `create_by` varchar(64) collate utf8_bin default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(64) collate utf8_bin default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) collate utf8_bin default NULL COMMENT '备注信息',
  `del_flag` char(1) collate utf8_bin NOT NULL default '0' COMMENT '删除标记（0：正常；1：删除）',
  `isUse` varchar(30) collate utf8_bin default NULL,
  PRIMARY KEY  (`id`),
  KEY `gen_table_column_table_id` (`gen_table_id`),
  KEY `gen_table_column_name` (`column_name`),
  KEY `gen_table_column_sort` (`sort`),
  KEY `gen_table_column_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_template`
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template` (
  `id` varchar(64) collate utf8_bin NOT NULL COMMENT '编号',
  `name` varchar(200) collate utf8_bin default NULL COMMENT '名称',
  `category` varchar(2000) collate utf8_bin default NULL COMMENT '分类',
  `file_path` varchar(500) collate utf8_bin default NULL COMMENT '生成文件路径',
  `file_name` varchar(200) collate utf8_bin default NULL COMMENT '生成文件名',
  `content` text collate utf8_bin COMMENT '内容',
  `create_by` varchar(64) collate utf8_bin default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(64) collate utf8_bin default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) collate utf8_bin default NULL COMMENT '备注信息',
  `del_flag` char(1) collate utf8_bin NOT NULL default '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY  (`id`),
  KEY `gen_template_del_falg` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码模板表';

-- ----------------------------
-- Records of gen_template
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_company`
-- ----------------------------
DROP TABLE IF EXISTS `ip_company`;
CREATE TABLE `ip_company` (
  `CO_ID` varchar(60) NOT NULL,
  `HIRER_ID` varchar(60) NOT NULL COMMENT '租户号',
  `CO_CODE` varchar(30) NOT NULL,
  `PARENT_CO_ID` varchar(60) default NULL COMMENT '上级单位编号',
  `CO_NAME` varchar(30) default NULL COMMENT '单位名称',
  `CO_FULLNAME` varchar(50) default NULL COMMENT '单位全称',
  `LINKMAN` varchar(20) default NULL COMMENT '单位联系人',
  `IS_ENABLED` varchar(1) default '1',
  `CO_DESC` varchar(300) default NULL COMMENT '描述',
  `LEVEL_NUM` int(2) default '1' COMMENT '部门级别',
  `DEPT_DETAIL` varchar(300) default NULL,
  `DISP_ORDER` int(10) default '0' COMMENT '顺序序号',
  `agcfs_dw_type` varchar(50) default NULL,
  `hold1` varchar(50) default NULL,
  `hold2` varchar(50) default NULL,
  `hold3` varchar(50) default NULL,
  `hold4` varchar(50) default NULL,
  `hold5` varchar(50) default NULL,
  `hold6` varchar(50) default NULL,
  `hold7` varchar(50) default NULL,
  `hold8` varchar(50) default NULL,
  `hold9` varchar(50) default NULL,
  `hold10` varchar(50) default NULL,
  `IS_FINANCIAL` varchar(10) default '0' COMMENT '是否财政单位',
  `CO_NAME1` varchar(120) default NULL COMMENT '别名1',
  `CO_NAME2` varchar(120) default NULL COMMENT '别名2',
  `CO_NAME3` varchar(120) default NULL COMMENT '别名3',
  PRIMARY KEY  (`CO_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_HIRER` (`HIRER_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_COMPA` (`PARENT_CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_company
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_data_partition`
-- ----------------------------
DROP TABLE IF EXISTS `ip_data_partition`;
CREATE TABLE `ip_data_partition` (
  `data_id` varchar(60) NOT NULL COMMENT '主键',
  `host` varchar(100) NOT NULL COMMENT '主机名/ip',
  `port` varchar(20) NOT NULL COMMENT '端口号',
  `user_name` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `schema_name` varchar(100) default NULL COMMENT '服务名/schema',
  `area_name` varchar(100) default NULL,
  `db_driver` varchar(255) NOT NULL COMMENT '数据库驱动',
  `url` varchar(255) default NULL COMMENT '数据库url（程序拼接）',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_date` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_data_partition
-- ----------------------------

-- ----------------------------
-- Table structure for `ip_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `ip_dictionary`;
CREATE TABLE `ip_dictionary` (
  `dic_id` varchar(60) NOT NULL COMMENT '主键ID',
  `dic_type` varchar(30) default NULL COMMENT '字典表类型',
  `dic_name` varchar(50) default NULL COMMENT '字典名称',
  PRIMARY KEY  (`dic_id`)
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
  `detail_key` varchar(60) default NULL COMMENT '键值',
  `detail_info` varchar(100) default NULL COMMENT '字典详细信',
  PRIMARY KEY  (`the_id`)
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
  `HIRER_SHORT_NAME` varchar(20) default NULL COMMENT '租户简称',
  `PASSWORD` varchar(50) NOT NULL,
  `LINKMAN` varchar(20) NOT NULL COMMENT '联系人姓名',
  `PHONE_NO` varchar(14) default NULL COMMENT '联系人电话',
  `CELLPHONE_NO` varchar(14) NOT NULL COMMENT '联系人手机',
  `SEX` varchar(1) default NULL COMMENT '联系人性别',
  `DUTY` varchar(20) default NULL COMMENT '联系人职务',
  `EMAIL` varchar(30) default NULL,
  `FAX` varchar(13) default NULL,
  `HIRER_TYPE` varchar(10) default NULL COMMENT '租户类型',
  `REGION` varchar(20) default NULL COMMENT '所属地区',
  `ADDRESS` varchar(50) default NULL COMMENT '通讯地址',
  `WEBSITE` varchar(50) default NULL COMMENT '网址',
  `POSTCODE` varchar(6) default NULL COMMENT '邮编',
  `SALT` varchar(50) default NULL,
  `login_ts` bigint(30) default NULL,
  `HIRER_NO` varchar(30) NOT NULL COMMENT '租户号',
  `CREATE_DATE` date default NULL COMMENT '创建时间',
  `UPDATE_DATE` date default NULL COMMENT '更新日期',
  `HIRER_LOGO_URL` varchar(100) default NULL COMMENT '租户LOGO路径',
  `HIRER_PIC_URL` varchar(100) default NULL COMMENT '租户个人设置头像',
  `LOGIN_NAME` varchar(45) default NULL COMMENT '登陆账号',
  `DATA_ID` varchar(60) default NULL COMMENT '分区表ip_data_partition的主键ID',
  `DB_URL` varchar(255) default NULL COMMENT '租户所属schema',
  `IS_VALID` varchar(10) default '0' COMMENT '是否生效 0未生效 1已生效',
  `DB_SCHEMA` varchar(100) default NULL COMMENT '对应的mysql数据库的schema',
  `MYCAT_SCHEMA` varchar(100) default NULL COMMENT '对应mycat的schema',
  `HIRER_CODE` varchar(30) default NULL,
  PRIMARY KEY  (`HIRER_ID`)
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
  `PARENT_MENU_ID` varchar(60) default NULL COMMENT '上级菜单编号',
  `MENU_NAME` varchar(20) NOT NULL,
  `LEVEL_NUM` varchar(10) NOT NULL default '1' COMMENT '菜单层级(值集1，2，3)',
  `DISP_ORDER` int(11) NOT NULL default '0' COMMENT '排序号',
  `IS_LEAF` varchar(1) NOT NULL default '1' COMMENT '是否末级节点(1-是，0-否)',
  `URL` varchar(320) default NULL COMMENT '菜单URL',
  `AUTH_LEVEL` varchar(1) NOT NULL default '0' COMMENT '授权级别(0,1,2,3)',
  `IS_SHOW` varchar(1) NOT NULL default '1' COMMENT '是否显示(1是,0否)',
  `MENU_DESC` varchar(40) default NULL COMMENT '菜单描述',
  `IS_JUMP` varchar(1) default NULL,
  `MENU_LOGO` varchar(100) default NULL COMMENT '菜单logo的，对应前端的class',
  `PERMISSION` varchar(200) NOT NULL COMMENT 'controller定义的资源权限标识',
  PRIMARY KEY  (`MENU_ID`),
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
  `THE_CODE` varchar(60) default NULL COMMENT '区划编码',
  `THE_NAME` varchar(100) default NULL COMMENT '区划名称',
  `PARENT_ID` varchar(60) default NULL COMMENT '父级ID',
  `IS_VALID` varchar(2) default '1' COMMENT '是否启用区划,0:未启用 1:已启用',
  `CREATE_DATE` date default NULL COMMENT '创建时间',
  `UPDATE_DATE` date default NULL COMMENT '更新时间',
  PRIMARY KEY  (`THE_ID`)
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
  `CO_ID` varchar(60) default NULL,
  `ROLE_NAME` varchar(20) default NULL,
  `ROLE_TYPE` varchar(2) default '1',
  `ROLE_DESC` varchar(300) default NULL COMMENT '角色描述',
  `DISP_ORDER` int(10) default '0' COMMENT '排序序号',
  `ROLE_CODE` varchar(30) default NULL,
  PRIMARY KEY  (`ROLE_ID`),
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
  PRIMARY KEY  (`THE_ID`),
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
  `USER_SEX` varchar(1) default NULL COMMENT '用户性别 （1男0女）',
  `PASSWORD` varchar(50) NOT NULL,
  `USER_EMAIL` varchar(30) default NULL,
  `PHONE_NO` varchar(14) default NULL COMMENT '电话号码',
  `CELLPHONE_NO` varchar(30) default NULL COMMENT '手机号码',
  `IS_ENABLED` varchar(20) default '1',
  `EMPLOYEE_NO` varchar(20) default NULL COMMENT '员工号',
  `DUTY` varchar(20) default NULL COMMENT '职务',
  `NATIVE_PLACE` varchar(40) default NULL COMMENT '原籍',
  `GRADUATE_SCHOOL` varchar(20) default NULL COMMENT '毕业学校',
  `MAJOR` varchar(30) default NULL COMMENT '专业',
  `EDUCATION` varchar(20) default NULL COMMENT '学历',
  `GRADUATIOIN_TIME` date default NULL COMMENT '毕业时间',
  `REMARK` varchar(50) default NULL,
  `SALT` varchar(50) default NULL,
  `login_ts` bigint(30) default NULL,
  `USER_PIC_URL` varchar(100) default NULL COMMENT '用户头像',
  `EXTENSION` varchar(45) default NULL,
  `USER_CODE` varchar(30) default NULL,
  `USER_TYPE` varchar(2) default '1' COMMENT '用户类型 0：租户管理员  1：普通用户',
  PRIMARY KEY  (`USER_ID`),
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
  PRIMARY KEY  (`THE_ID`),
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
  `user_id` varchar(60) default NULL COMMENT '用户ID（关联用户表或租户表）',
  `menu_id` varchar(60) default NULL COMMENT '菜单ID，关联菜单表'
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
  `USER_ID` varchar(60) default NULL,
  `ROLE_ID` varchar(60) default NULL,
  `CO_ID` varchar(60) default NULL,
  `IS_PART_TIME` varchar(2) default NULL COMMENT '是否兼职0：不是兼职  1：兼职',
  `DISP_ORDER` int(10) default '0' COMMENT '排序序号',
  PRIMARY KEY  (`THE_ID`),
  KEY `FK_IP_UR_REFERENCE_ROLE` (`ROLE_ID`),
  KEY `FK_IP_UR_REFERENCE_USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `ticket_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ticket_detail`;
CREATE TABLE `ticket_detail` (
  `id` varchar(60) NOT NULL,
  `business_id` varchar(60) default NULL,
  `status` int(1) default NULL,
  `uid` varchar(60) default NULL,
  `suggestion` varchar(60) default NULL,
  `bpm_uid` varchar(100) default NULL,
  `create_time` timestamp NOT NULL default '0000-00-00 00:00:00' on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ticket_detail
-- ----------------------------
