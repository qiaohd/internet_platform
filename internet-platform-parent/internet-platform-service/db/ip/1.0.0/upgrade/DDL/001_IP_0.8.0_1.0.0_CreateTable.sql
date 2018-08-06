DROP TABLE IF EXISTS `ip_notice`;
CREATE TABLE `ip_notice` (
  `notice_id` varchar(100) NOT NULL,
  `hirer_id` varchar(100) NOT NULL,
  `attachment_url` varchar(500) DEFAULT NULL COMMENT '����·��',
  `notice_title` varchar(50) NOT NULL COMMENT '�������',
  `notice_content` varchar(500) DEFAULT NULL COMMENT '��������',
  `isPublish` varchar(1) DEFAULT NULL COMMENT '�Ƿ񷢲�(0-δ������ֻ����,1-����)',
  `create_date` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_date` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_index_config`;
CREATE TABLE `ip_index_config` (
  `config_id` varchar(60) NOT NULL COMMENT '����',
  `menu_id` varchar(60) DEFAULT NULL,
  `menu_name` varchar(100) NOT NULL COMMENT 'Ȩ�޲˵���',
  `catalog` varchar(20) NOT NULL COMMENT '��������',
  `router_addr` varchar(100) NOT NULL COMMENT '·�ɵ�ַ',
  `interface_addr` varchar(100) NOT NULL COMMENT '�ӿڵ�ַ',
  `inter_param` varchar(100) DEFAULT NULL COMMENT '�ӿڲ�������',
  `isUse` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `ip_company_syn`;
CREATE TABLE `ip_company_syn` (
  `CO_ID` varchar(60) DEFAULT NULL COMMENT '����id',
  `CO_CODE_TMP` varchar(60) DEFAULT NULL COMMENT '����ԭʼcode',
  `HIRER_ID` varchar(60) DEFAULT NULL COMMENT '�⻧id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_hirer_syn`;
CREATE TABLE `ip_hirer_syn` (
  `hirer_id` varchar(60) DEFAULT NULL COMMENT '�⻧id',
  `cellphone_no` varchar(60) DEFAULT NULL COMMENT '�⻧�绰����'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ip_role_syn`;
CREATE TABLE `ip_role_syn` (
  `ROLE_ID` varchar(60) DEFAULT NULL COMMENT '��ɫid',
  `ROLE_CODE` varchar(60) DEFAULT NULL COMMENT '��ɫcode',
  `HIRER_ID` varchar(60) DEFAULT NULL COMMENT '�⻧id'
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

