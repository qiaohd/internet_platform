-- ----------------------------
-- Table structure for `ask_for_leave`
-- ----------------------------
DROP TABLE IF EXISTS `ask_for_leave`;
CREATE TABLE `ask_for_leave` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `start_date` varchar(50) CHARACTER SET utf8 NOT NULL,
  `end_date` varchar(50) CHARACTER SET utf8 NOT NULL,
  `type` varchar(50) CHARACTER SET utf8 NOT NULL,
  `reason` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  `uid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `proposer` varchar(50) CHARACTER SET utf8 NOT NULL,
  `cur_processinstance_id` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `task_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of ask_for_leave
-- ----------------------------

-- ----------------------------
-- Table structure for `ticket_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ticket_detail`;
CREATE TABLE `ticket_detail` (
  `id` varchar(60) NOT NULL,
  `business_id` varchar(60) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `uid` varchar(60) DEFAULT NULL,
  `suggestion` varchar(60) DEFAULT NULL,
  `bpm_uid` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_data_partition`;
CREATE TABLE `ip_data_partition` (
`data_id`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`host`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主机名/ip' ,
`port`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '端口号' ,
`user_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名' ,
`password`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码' ,
`schema_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名/schema' ,
`area_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_driver`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库驱动' ,
`url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库url（程序拼接）' ,
`create_date`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_date`  datetime NULL DEFAULT NULL COMMENT '更新时间' ,
PRIMARY KEY (`data_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;


DROP TABLE IF EXISTS `gen_scheme`;
CREATE TABLE `gen_scheme` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `scheme_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `category` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '分类',
  `package_name` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '生成模块名',
  `sub_module_name` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '生成子模块名',
  `function_name` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '生成功能名',
  `function_name_simple` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '生成功能名（简写）',
  `function_author` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '生成功能作者',
  `gen_table_id` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '生成表编号',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `schemeType` varchar(10) COLLATE utf8_bin DEFAULT '',
  `replaceFile` varchar(10) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `gen_scheme_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='生成方案';


DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `table_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `table_comments` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `class_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '实体类名称',
  `parent_table` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '关联父表',
  `parent_table_fk` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '关联父表外键',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `layout` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '页面布局',
  `isGen` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `hirer_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gen_table_name` (`table_name`),
  KEY `gen_table_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务表';

DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `gen_table_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `column_comments` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `jdbc_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '列的数据类型的字节长度',
  `java_type` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否主键',
  `is_null` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否可为空',
  `is_insert` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否为插入字段',
  `is_edit` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否编辑字段',
  `is_list` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否列表字段',
  `is_query` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否查询字段',
  `query_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）',
  `show_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）',
  `dict_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '字典类型',
  `settings` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '其它设置（扩展字段JSON）',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `isUse` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gen_table_column_table_id` (`gen_table_id`),
  KEY `gen_table_column_name` (`column_name`),
  KEY `gen_table_column_sort` (`sort`),
  KEY `gen_table_column_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务表字段';

DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `category` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '分类',
  `file_path` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '生成文件路径',
  `file_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '生成文件名',
  `content` text COLLATE utf8_bin COMMENT '内容',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `gen_template_del_falg` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='代码模板表';