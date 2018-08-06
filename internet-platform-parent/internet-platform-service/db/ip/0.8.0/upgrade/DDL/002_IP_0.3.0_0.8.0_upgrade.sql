ALTER TABLE `ip_hirer` ADD COLUMN `HIRER_CODE` VARCHAR(30) NULL  AFTER `LOGIN_NAME`;
ALTER TABLE `ip_user` ADD COLUMN `USER_CODE` VARCHAR(30) NULL  AFTER `EXTENSION`;
ALTER TABLE `ip_role` ADD COLUMN `ROLE_CODE` VARCHAR(30) NULL  AFTER `DISP_ORDER`;

ALTER TABLE `ip_user`
ADD COLUMN `USER_TYPE`  varchar(2) NULL DEFAULT '1' COMMENT '用户类型 0：租户管理员  1：普通用户' AFTER `USER_CODE`;

ALTER TABLE `ip_hirer`
ADD COLUMN `DATA_ID`  varchar(60) NULL COMMENT '分区表ip_data_partition的主键ID' AFTER `LOGIN_NAME`;

ALTER TABLE `ip_hirer`
ADD COLUMN `DB_URL`  varchar(255) NULL COMMENT '租户所属schema' AFTER `DATA_ID`;
ALTER TABLE `ip_hirer`
ADD COLUMN `IS_VALID`  varchar(10) NULL DEFAULT '0' COMMENT '是否生效 0未生效 1已生效' AFTER `DB_URL`;

ALTER TABLE `ip_menu`
ADD COLUMN `PERMISSION`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'controller定义的资源权限标识' AFTER `MENU_LOGO`;


ALTER TABLE `ip_company` 
ADD COLUMN `agcfs_dw_type`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold1`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold2`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold3`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold4`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold5`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold6`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold7`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold8`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold9`  varchar(50) NULL;

ALTER TABLE `ip_company` 
ADD COLUMN `hold10`  varchar(50) NULL;

alter table  ip_company  add IS_FINANCIAL   VARCHAR(10)  default '0' COMMENT '是否财政单位';  

alter table  ip_company  add CO_NAME1   VARCHAR(120) COMMENT '别名1';        	      		

alter table  ip_company  add CO_NAME2   VARCHAR(120) COMMENT '别名2';        	       		

alter table  ip_company  add CO_NAME3   VARCHAR(120) COMMENT '别名3';  

ALTER TABLE `ip_menu`
MODIFY COLUMN `URL`  varchar(320) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL' AFTER `IS_LEAF`;



ALTER TABLE `ip_hirer`
ADD COLUMN `DB_SCHEMA`  varchar(100) NULL COMMENT '对应的mysql数据库的schema' AFTER `IS_VALID`,
ADD COLUMN `MYCAT_SCHEMA`  varchar(100) NULL COMMENT '对应mycat的schema' AFTER `DB_SCHEMA`;




