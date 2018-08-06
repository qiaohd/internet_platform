DROP TABLE IF EXISTS `ip_notice`;
CREATE TABLE `ip_notice` (
  `notice_id` varchar(100) NOT NULL,
  `hirer_id` varchar(100) NOT NULL,
  `attachment_url` varchar(500) DEFAULT NULL COMMENT '附件路径',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_content` varchar(500) DEFAULT NULL COMMENT '公告内容',
  `isPublish` varchar(1) DEFAULT NULL COMMENT '是否发布(0-未发布，只保存,1-发布)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_index_config`;
CREATE TABLE `ip_index_config` (
  `config_id` varchar(60) NOT NULL COMMENT '主键',
  `menu_id` varchar(60) DEFAULT NULL,
  `menu_name` varchar(100) NOT NULL COMMENT '权限菜单名',
  `catalog` varchar(20) NOT NULL COMMENT '索引分类',
  `router_addr` varchar(100) NOT NULL COMMENT '路由地址',
  `interface_addr` varchar(100) NOT NULL COMMENT '接口地址',
  `inter_param` varchar(100) DEFAULT NULL COMMENT '接口参数名称',
  `isUse` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `ip_company_syn`;
CREATE TABLE `ip_company_syn` (
  `CO_ID` varchar(60) DEFAULT NULL COMMENT '部门id',
  `CO_CODE_TMP` varchar(60) DEFAULT NULL COMMENT '部门原始code',
  `HIRER_ID` varchar(60) DEFAULT NULL COMMENT '租户id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_hirer_syn`;
CREATE TABLE `ip_hirer_syn` (
  `hirer_id` varchar(60) DEFAULT NULL COMMENT '租户id',
  `cellphone_no` varchar(60) DEFAULT NULL COMMENT '租户电话号码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_role_syn`;
CREATE TABLE `ip_role_syn` (
  `ROLE_ID` varchar(60) DEFAULT NULL COMMENT '角色id',
  `ROLE_CODE` varchar(60) DEFAULT NULL COMMENT '角色code',
  `HIRER_ID` varchar(60) DEFAULT NULL COMMENT '租户id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ip_menu_icon`;
CREATE TABLE `ip_menu_icon` (
  `MENU_ID` varchar(60) NOT NULL,
  `MENU_NAME` varchar(20) NOT NULL,
  `URL` varchar(320) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `USER_ID` varchar(60) NOT NULL,
  `ICON_ID` varchar(100) NOT NULL,
  `ICON_PATH` varchar(320) NOT NULL,
  PRIMARY KEY (`MENU_ID`,`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

