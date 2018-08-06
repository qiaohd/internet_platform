/*
Navicat MySQL Data Transfer

Source Server         : dep
Source Server Version : 50711
Source Host           : 10.11.112.27:3306
Source Database       : internet_mysql

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-04-22 12:59:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ip_company`
-- ----------------------------
DROP TABLE IF EXISTS `ip_company`;
CREATE TABLE `ip_company` (
  `CO_ID` varchar(60) NOT NULL,
  `HIRER_ID` varchar(60) NOT NULL COMMENT '租户号',
  `CO_CODE` decimal(10,0) NOT NULL,
  `PARENT_CO_ID` varchar(60) DEFAULT NULL COMMENT '上级单位编号',
  `CO_NAME` varchar(30) DEFAULT NULL COMMENT '单位名称',
  `CO_FULLNAME` varchar(50) DEFAULT NULL COMMENT '单位全称',
  `LINKMAN` varchar(20) DEFAULT NULL COMMENT '单位联系人',
  `IS_ENABLED` varchar(1) DEFAULT '1',
  PRIMARY KEY (`CO_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_HIRER` (`HIRER_ID`),
  KEY `FK_IP_COMPA_REFERENCE_IP_COMPA` (`PARENT_CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_company
-- ----------------------------
INSERT INTO `ip_company` VALUES ('0a5f2252-ea3a-48cc-8d55-76dfbaeef827', 'a3999e5a-f6fe-4786-ab19-833b701e875b', '111', null, null, '12345678', null, '1');
INSERT INTO `ip_company` VALUES ('11b96e5a-6ab4-4fdf-8cbf-d6a885e66969', '15a26ead-aac9-4a9a-beb2-407e47f2b158', '111', null, null, 'SSS', null, '1');
INSERT INTO `ip_company` VALUES ('14bfa6bb', 'eb0f63a2-6772-429d-8712-f27deca7e9a4', '111', null, null, 'QQ', null, '1');
INSERT INTO `ip_company` VALUES ('182be0c5', '68896ad5-1658-47f1-8206-8f23aecfe1a7', '111', null, null, 'shanghai', null, '1');
INSERT INTO `ip_company` VALUES ('1c383cd3', '7d0014d8-b0ce-4cd5-9c92-b2ea5ae3a6f0', '111', null, null, '12345678', null, '1');
INSERT INTO `ip_company` VALUES ('21f4a381-30d4-4f1c-806f-a8f1cda015c0', '7a7b5f0b-238b-480a-bbbd-2a6726540243', '111', null, null, 'hu', null, '1');
INSERT INTO `ip_company` VALUES ('229afa3d-aee8-411e-b858-064adcb5e690', '71ef282e-f202-45bc-b376-e4121aa82925', '111', null, null, 'dalian2', null, '1');
INSERT INTO `ip_company` VALUES ('28bd44d7-cc70-4c60-9abc-221ef94ea94d', '66061c71-1185-46bc-a767-455541e82754', '111', null, null, '1', null, '1');
INSERT INTO `ip_company` VALUES ('3246790f-50bf-415c-9b93-b6b81a9292c3', '26ef3669-f80d-4ab2-bc96-6292ee591928', '111', null, null, 'po', null, '1');
INSERT INTO `ip_company` VALUES ('32d53339-e413-4af3-bb2a-87fea54422f5', 'd2d4cb1b-77d2-4396-8f2a-03c99cd42677', '111', null, null, 'wuhao', null, '1');
INSERT INTO `ip_company` VALUES ('34173cb3', 'b55f42ae-c27a-4013-aa27-1461b77301ed', '111', null, null, '单位火狐', null, '1');
INSERT INTO `ip_company` VALUES ('3773fa66-ddd7-4b35-86c7-d8d30ca953b6', '53273a65-0bd1-4d02-937c-9f88b48f081e', '111', null, null, 'KI', null, '1');
INSERT INTO `ip_company` VALUES ('38a9bfa2-1c22-4c01-a11f-2c8ecfe958f1', '2bc6c8f4-4c10-415e-9f00-d7a15bc5d573', '111', null, null, '12345678', null, '1');
INSERT INTO `ip_company` VALUES ('39d161c5-4a89-44b0-be56-2c32acd396c1', 'ecd78a67-cda5-4a87-9150-2aa61c0c5770', '111', null, null, 'dd', null, '1');
INSERT INTO `ip_company` VALUES ('459e009f-253a-4745-8edd-714d8a074104', '70087803-577b-4fd3-9a0e-1d4eddac66f1', '111', null, null, 'YIHAO', null, '1');
INSERT INTO `ip_company` VALUES ('4cd3b64f-0185-45cf-9a15-92306153eb61', '111ab43b-cde0-492c-b319-985536d99e2c', '111', null, null, 'ww', null, '1');
INSERT INTO `ip_company` VALUES ('54229abf', '634b5e27-5cab-4d3e-b7de-1a3b09b9e3be', '111', null, null, '上海', null, '1');
INSERT INTO `ip_company` VALUES ('628f7579-6f06-4caf-a4ea-6d2f56159d65', 'ab573f90-05d7-4b5b-b21f-108ef41a52ef', '111', null, null, '12345678', null, '1');
INSERT INTO `ip_company` VALUES ('642e92ef', 'ca8db660-8858-4c84-a7da-fca918508658', '111', null, null, '名称test1', null, '1');
INSERT INTO `ip_company` VALUES ('66f041e1', '08bb2fa6-7897-446d-9b52-7855b55a61a9', '111', null, null, 'dalian', null, '1');
INSERT INTO `ip_company` VALUES ('670db6b2-039f-492c-b827-96b9eadc7c56', 'b690e7cc-537d-49b6-896f-1596b8184eb7', '111', null, null, 'NVXIAOXIE', null, '1');
INSERT INTO `ip_company` VALUES ('6a87a08f-ea64-4c9e-aa0c-be4313ff7271', 'd8d0f1c9-c7e1-4b4a-92d7-691b576fcc75', '111', null, null, '111', null, '1');
INSERT INTO `ip_company` VALUES ('6ea9ab1b', '9419ca9d-ddb0-4ba8-8ac7-084884580d12', '111', null, null, 'yonyou', null, '1');
INSERT INTO `ip_company` VALUES ('70b063c7-694c-453a-a99c-6b031a2044e6', '84d9b81f-5acf-4d1d-a79f-4795ebeb8c27', '111', null, null, '', null, '1');
INSERT INTO `ip_company` VALUES ('70efdf2e', '2cd0707f-82ae-4066-b461-f87898eddf8b', '111', null, null, '11111', null, '1');
INSERT INTO `ip_company` VALUES ('71460a9f-6f1c-4c22-8f51-9f7ba6c18187', '8d94e7f9-e4e9-4766-8385-18adf0fc3cd6', '111', null, null, '女小写', null, '1');
INSERT INTO `ip_company` VALUES ('74a56a5f-f552-4d44-afe3-127b80aa0cfe', '9375feab-8916-40f7-8d5f-acd3ca2aa18a', '111', null, null, 'SSS', null, '1');
INSERT INTO `ip_company` VALUES ('7c8db545-8bc6-4b7a-bc48-8dfa6ee04ccf', '0a147e1d-e8cc-4883-a65f-46c5fc8466ce', '111', null, null, 'SANHAO', null, '1');
INSERT INTO `ip_company` VALUES ('7d7f3c2b-b778-467f-9562-990ce3f33220', '5fbf19a8-e206-4afe-b6eb-f7e5e5abc545', '111', null, null, '1', null, '1');
INSERT INTO `ip_company` VALUES ('83e4dfd2-b2e3-4df3-bfdd-a99d8b1e65a1', 'a946055c-b696-4ddb-8796-0465596f5d01', '111', null, null, 'GH', null, '1');
INSERT INTO `ip_company` VALUES ('8613985e', 'cfd0b3c7-8cf7-4602-91b3-90ac2d1168b3', '111', null, null, '123123', null, '1');
INSERT INTO `ip_company` VALUES ('87543f02-f8b7-4a42-9b37-85d87140fd7e', '2c381126-05b3-44c8-9cea-6e330d25826c', '111', null, null, '1234', null, '1');
INSERT INTO `ip_company` VALUES ('8f14e45f', '782aecb5-27d5-4482-a5d8-ee28acb847fd', '111', null, null, '12345678', null, '1');
INSERT INTO `ip_company` VALUES ('93db85ed', 'b69f8051-6a16-4cf8-b796-d908fb602f1d', '111', null, null, 'yonyou', null, '1');
INSERT INTO `ip_company` VALUES ('95c1d710-75b9-4719-b32c-6dd1d6b88de3', 'c527a130-2530-488a-be89-05a3aa81865d', '111', null, null, 'okp', null, '1');
INSERT INTO `ip_company` VALUES ('969f1bdc-071c-4860-b520-0895269e1599', 'c6762fdb-8236-4b1c-bfc6-0d0697e03bb4', '111', null, null, '1234', null, '1');
INSERT INTO `ip_company` VALUES ('98c68d74-9317-47e8-a3eb-9d9db972f8c7', '8d992a9a-14eb-42ef-b7fd-f654b1e7778a', '111', null, null, 'ERHAO', null, '1');
INSERT INTO `ip_company` VALUES ('9a115815', '84cb1a31-1dc7-4871-82e3-093025092e1a', '111', null, null, 's', null, '1');
INSERT INTO `ip_company` VALUES ('a06a5e8c-26df-4c91-b28b-7b0713e1ae7f', 'd4573719-ac7a-4ea3-a842-1f92494443b4', '111', null, null, 'dalian1', null, '1');
INSERT INTO `ip_company` VALUES ('a1d0c6e8', 'f273c434-00fd-4986-af83-b2b72806d348', '111', null, null, 'hangzhou', null, '1');
INSERT INTO `ip_company` VALUES ('a2d1abc0-3ce5-40e5-801e-97884fa4465f', '45bcd241-f935-4054-bbb1-e89100aed269', '111', null, null, 'f', null, '1');
INSERT INTO `ip_company` VALUES ('a3f390d8', '2', '111', null, null, 'QD', null, '1');
INSERT INTO `ip_company` VALUES ('ad61ab14', '507b1959-e092-404f-a7cc-7a76e2e2804c', '111', null, null, 'G', null, '1');
INSERT INTO `ip_company` VALUES ('b1462afd-67b2-4d91-97a4-3336364ec582', 'b348982a-cc7c-4bb9-9e5a-5ce3e8341e04', '111', null, null, 'jiaoyan', null, '1');
INSERT INTO `ip_company` VALUES ('b4b70b4a-9f66-43fc-b7dc-e02294b6e71d', '4d94c5bb-802d-463e-8c87-baee42c1386a', '111', null, null, '12345677', null, '1');
INSERT INTO `ip_company` VALUES ('bdcf5788-8cd5-47c3-9d29-1a8e4ac8bc6c', 'b27a6ef2-f35c-40a6-8aa4-b7552ada3264', '111', null, null, 'hj', null, '1');
INSERT INTO `ip_company` VALUES ('c0c7c76d', '1', '111', null, null, 'QIAO', null, '1');
INSERT INTO `ip_company` VALUES ('c16a5320', '48f8ecff-1a8a-4a91-b120-361973eae6c6', '111', null, null, 'TEST0331', null, '1');
INSERT INTO `ip_company` VALUES ('c20ad4d7', '3', '111', null, null, 'D', null, '1');
INSERT INTO `ip_company` VALUES ('c7e1249f', '583dfc83-2b10-4cca-9327-400fc1874011', '111', null, null, 'F', null, '1');
INSERT INTO `ip_company` VALUES ('c902ffac-0330-4507-9daa-22731361e2e9', '5be00215-6a08-4586-9677-e0b3e37d9266', '111', null, null, '1', null, '1');
INSERT INTO `ip_company` VALUES ('c937cff7-66ae-4473-821b-2daf3196ecb5', '5c034efc-a8a6-495f-84f8-9ca72e509cfa', '111', null, null, 'tet1', null, '1');
INSERT INTO `ip_company` VALUES ('cc7c5d1b-2f30-489d-a0f3-ebf3761ef82b', 'fd3e5afb-2712-40b9-87a4-0338aaa4d4e5', '111', null, null, 'S', null, '1');
INSERT INTO `ip_company` VALUES ('d077170e-0341-4ede-88ad-02134510b80f', '30c07717-dd2f-4a45-a7d4-cd01bdafef9f', '111', null, null, 'chongfu', null, '1');
INSERT INTO `ip_company` VALUES ('d1fe173d', 'ddd00466-8dae-4464-b21a-cd9579364201', '111', null, null, 'ssssss', null, '1');
INSERT INTO `ip_company` VALUES ('d2ddea18', '5f9add7b-7cec-48c8-b7be-8a2b0b09d99f', '111', null, null, 'shanghai', null, '1');
INSERT INTO `ip_company` VALUES ('d31a5d25-9e08-430b-812f-43b190974b35', '6d710358-16e8-445a-bc46-7779285ef0dc', '111', null, null, 'SD', null, '1');
INSERT INTO `ip_company` VALUES ('d645920e', '88947207-ab97-4be1-ad4b-4c1d29b34454', '111', null, null, 'ty', null, '1');
INSERT INTO `ip_company` VALUES ('f19a0e95-d645-40aa-95cd-09e283d7a4cf', '70a326dc-5f4e-4824-b4e8-da6d690734a2', '111', null, null, 'SIHAO', null, '1');

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
  PRIMARY KEY (`HIRER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_hirer
-- ----------------------------
INSERT INTO `ip_hirer` VALUES ('0624ad54-032b-4393-a354-7bcf66c3d26a', 'KL', null, '9bc55dc91294f48a3590d8cc912a72d5c3081275', 'KL', null, '未注册', '1', null, '345@qq.com', null, null, null, null, null, null, '6e6b7dda785df4b5', null);
INSERT INTO `ip_hirer` VALUES ('08bb2fa6-7897-446d-9b52-7855b55a61a9', 'le999', null, 'c937c3b237e3cf33fcbb56165b50400655d76adb', 'le999', null, '18387654321', '1', null, '未注册', null, null, null, null, null, null, '276f82408b253833', null);
INSERT INTO `ip_hirer` VALUES ('0a147e1d-e8cc-4883-a65f-46c5fc8466ce', 'SANHAO', null, '36316e5aef9af05358b7772c0fd39252295fa4aa', 'SANHAO', null, '18612345678', '1', null, '未注册', null, null, null, null, null, null, '6a133a0a3b92c17c', null);
INSERT INTO `ip_hirer` VALUES ('1', 'QIAO', null, '6720855f3829e737d8cefd45a9d9e9efd9f104c5', 'QIAO', null, '未注册', '1', null, '460168509@qq.com', null, null, null, null, null, null, '6e6062259821b793', null);
INSERT INTO `ip_hirer` VALUES ('111ab43b-cde0-492c-b319-985536d99e2c', 'ww', null, '9a2d939c977ac9b0dd31d517ea0e9f34f0c07f35', 'ww', null, '17612345678', '1', null, '未注册', null, null, null, null, null, null, 'b29b954bf72c4fc1', null);
INSERT INTO `ip_hirer` VALUES ('11e5446a-3150-4f7c-93c3-fff75fc721e1', '2', null, 'dffd356669fdf829ed84c6a1ea41c93f47f19f40', '2', null, '未注册', '1', null, '22@qq.com', null, null, null, null, null, null, 'a6f9281ca7844b61', null);
INSERT INTO `ip_hirer` VALUES ('15a26ead-aac9-4a9a-beb2-407e47f2b158', 'SSSSSD', null, '2daa231ab4545b4dc8d84dac3554b67b9f139db1', 'SSSSSD', null, '未注册', '1', null, 'SDSDSD@qq.com', null, null, null, null, null, null, '0d010dd8aac48a9e', null);
INSERT INTO `ip_hirer` VALUES ('2', 'QHD', null, '9dc30bbf01fe0c5b80978da208423599def0051c', 'QHD', null, '未注册', '1', null, '123@qq.com', null, null, null, null, null, null, '65be0f3f8a66b65c', null);
INSERT INTO `ip_hirer` VALUES ('26ef3669-f80d-4ab2-bc96-6292ee591928', 'po', null, '6d6ffccab5de7a6868daf6d147af7aee18658d87', 'po', null, '未注册', '1', null, '245@qq.com', null, null, null, null, null, null, 'a2be3cf81322b160', null);
INSERT INTO `ip_hirer` VALUES ('2bc6c8f4-4c10-415e-9f00-d7a15bc5d573', '1234578', null, 'b0b0fcfa5c2b807da0ac79f657274929c0d575d2', '1234578', null, '123456789', '1', null, '未注册', null, null, null, null, null, null, '3588d909f8d9bac7', null);
INSERT INTO `ip_hirer` VALUES ('2c381126-05b3-44c8-9cea-6e330d25826c', '1234', null, 'abcff069ba48721f54482fe2edc55bdd929ed6ee', '1234', null, '未注册', '1', null, 'eqweqw@yonyou.com', null, null, null, null, null, null, 'e048ce5adc02a26a', null);
INSERT INTO `ip_hirer` VALUES ('2cd0707f-82ae-4066-b461-f87898eddf8b', 'taobaosong', null, 'c3a9a860e1532ef40ed141ef8196b9d6904a8906', 'taobaosong', null, '18510906564', '1', null, '未注册', null, null, null, null, null, null, 'a514e72f1f8552bc', null);
INSERT INTO `ip_hirer` VALUES ('3', 'D', null, '079e21dd2c0486833124789c30c6bb6ad9c883df', 'D', null, '未注册', '1', null, '12345@qq.com', null, null, null, null, null, null, '1454926867a56130', null);
INSERT INTO `ip_hirer` VALUES ('30c07717-dd2f-4a45-a7d4-cd01bdafef9f', 'chongfu', null, '4bea80c2a94230e8abfe33385326880f3fe0e07e', 'chongfu', null, '17612345678', '1', null, '未注册', null, null, null, null, null, null, 'be875b90cbc87907', null);
INSERT INTO `ip_hirer` VALUES ('45bcd241-f935-4054-bbb1-e89100aed269', 'h', null, '87df60268036705c73e2d149919f3da1c61ec1fa', 'h', null, '未注册', '1', null, '111@qq.com', null, null, null, null, null, null, '87f4bbe478f6e146', null);
INSERT INTO `ip_hirer` VALUES ('48f8ecff-1a8a-4a91-b120-361973eae6c6', 'TEST0331', null, '0436f2cac69d480562714820fab1326a2fb3254f', 'TEST0331', null, '17600880397', '1', null, '未注册', null, null, null, null, null, null, '0d317b596c2a5544', null);
INSERT INTO `ip_hirer` VALUES ('4d94c5bb-802d-463e-8c87-baee42c1386a', '12345677', null, '28836274e2592886ed0382117aa7ce778d296878', '12345677', null, '13512345677', '1', null, '未注册', null, null, null, null, null, null, 'de73e33513d60f75', null);
INSERT INTO `ip_hirer` VALUES ('507b1959-e092-404f-a7cc-7a76e2e2804c', 'G', null, 'e4f5a4f10a50dbd549621cc640c1a83e12bc4e7d', 'G', null, '13800138000', '1', null, '未注册', null, null, null, null, null, null, '4925a07976673c30', null);
INSERT INTO `ip_hirer` VALUES ('53273a65-0bd1-4d02-937c-9f88b48f081e', 'KI', null, '942f350aa32458ee9cf1868d60bf6a8cafd1b347', 'KI', null, '未注册', '1', null, '145@qq.com', null, null, null, null, null, null, 'd1ab825d7bce61fe', null);
INSERT INTO `ip_hirer` VALUES ('5be00215-6a08-4586-9677-e0b3e37d9266', '12', null, 'd5b9014f7574cf1a745a79bc8140cf6bce8a8356', '12', null, '17012345679', '1', null, '未注册', null, null, null, null, null, null, '797356d88d9b176e', null);
INSERT INTO `ip_hirer` VALUES ('5c034efc-a8a6-495f-84f8-9ca72e509cfa', 'tet1', null, '77b01f1086c5dbf7a647d27049a7d08556e8363e', 'tet1', null, '未注册', '1', null, 'wangwei5@yonyou.com', null, null, null, null, null, null, 'ebd5d5a9518454fe', null);
INSERT INTO `ip_hirer` VALUES ('5f9add7b-7cec-48c8-b7be-8a2b0b09d99f', 'aa', null, 'dbf7663e0b13120ffd1479ff2bdb463c54e270cb', 'aa', null, '13612345678', '1', null, '未注册', null, null, null, null, null, null, 'a3c3d14e5d355831', null);
INSERT INTO `ip_hirer` VALUES ('5fbf19a8-e206-4afe-b6eb-f7e5e5abc545', '1', null, 'dbdd17cea56b03a6606dfd41398c5c34093327fe', '1', null, '17612345678', '1', null, '未注册', null, null, null, null, null, null, '12c05c48a7d9f36e', null);
INSERT INTO `ip_hirer` VALUES ('634b5e27-5cab-4d3e-b7de-1a3b09b9e3be', 'Le', null, 'bfe354fca6f786cfa24ef4285c508cd894a0784f', 'Le', null, '未注册', '1', null, 'winner@sina.com', null, null, null, null, null, null, 'e9d7bbf384c05a94', null);
INSERT INTO `ip_hirer` VALUES ('66061c71-1185-46bc-a767-455541e82754', '123456789', null, '9036731fa380fc9f87519a31c439f65d8f0d44bc', '123456789', null, '17089765432', '1', null, '未注册', null, null, null, null, null, null, '81ec6f87809cbcd5', null);
INSERT INTO `ip_hirer` VALUES ('68896ad5-1658-47f1-8206-8f23aecfe1a7', 'le9', null, 'bb08ebcab09127fc08222706d10604494662fcf3', 'le9', null, '未注册', '1', null, 'well@sina.cn', null, null, null, null, null, null, '2502cfa5c4d75203', null);
INSERT INTO `ip_hirer` VALUES ('6d710358-16e8-445a-bc46-7779285ef0dc', 'SDD', null, 'cc5092b779f8e0df80b7730ffe7a8a85e146c50c', 'SDD', null, '未注册', '1', null, 'SDFDSFD@qq.com', null, null, null, null, null, null, 'efe4e995caccfb75', null);
INSERT INTO `ip_hirer` VALUES ('70087803-577b-4fd3-9a0e-1d4eddac66f1', 'YIHAO', null, 'e55275adcf9fb04859a92264abe681231088717f', 'YIHAO', null, '17612345678', '1', null, '未注册', null, null, null, null, null, null, '8cfdc95785e96500', null);
INSERT INTO `ip_hirer` VALUES ('70a326dc-5f4e-4824-b4e8-da6d690734a2', 'SIHAO', null, '0e489a2522e8b3c539609f43bbcb85283d05622d', 'SIHAO', null, '17012345678', '1', null, '未注册', null, null, null, null, null, null, '751e31eb283b3dc0', null);
INSERT INTO `ip_hirer` VALUES ('71ef282e-f202-45bc-b376-e4121aa82925', 'Le_win1', null, '97bcb1f1c58f258a25f9c80fa124db99d8e80673', 'Le_win1', null, '未注册', '1', null, 'Le_win1@sina.com', null, null, null, null, null, null, '48aba4028c4c2b5b', null);
INSERT INTO `ip_hirer` VALUES ('782aecb5-27d5-4482-a5d8-ee28acb847fd', 'WANGWEI', null, 'f3e364441cc71ca51186c7e9185fe4fa5bb48dcc', 'WANGWEI', null, '未注册', '1', null, 'wangwei15@yonyou.com', null, null, null, null, null, null, '3207e90e60d3871d', null);
INSERT INTO `ip_hirer` VALUES ('7a7b5f0b-238b-480a-bbbd-2a6726540243', 'hu', null, '9cc1e937f159132d2c45855f91deb0277d5a15e1', 'hu', null, '未注册', '1', null, '112@qq.com', null, null, null, null, null, null, '9b91baa7f94153de', null);
INSERT INTO `ip_hirer` VALUES ('7cf48038-626c-4095-8cb1-54504475813f', 'kj', null, 'ec7db0c265628184c03b33eb029f9a1b25d27be1', 'kj', null, '未注册', '1', null, '234@qq.com', null, null, null, null, null, null, '48f8ae8e8535279e', null);
INSERT INTO `ip_hirer` VALUES ('7d0014d8-b0ce-4cd5-9c92-b2ea5ae3a6f0', 'test', null, '0862dc28bf7e5358d434715dd5f9ddd6e997d004', 'test', null, '124153414', '1', null, '未注册', null, null, null, null, null, null, 'fb710f6df8241aa7', null);
INSERT INTO `ip_hirer` VALUES ('7fb1c565-ac50-451c-a739-73295117027b', '胡智王', null, 'a1f04888f6272726a701e35c4ff3869785b3de0c', '胡智王', null, '18611147626', '1', null, '未注册', null, null, null, null, null, null, 'd8996b52e14a8039', null);
INSERT INTO `ip_hirer` VALUES ('84cb1a31-1dc7-4871-82e3-093025092e1a', 'S', null, 'f34929821c3bf6c1a24733a2ef5cb8d00354b1fa', 'S', null, '未注册', '1', null, '123456@qq.com', null, null, null, null, null, null, '08818c774f410bb0', null);
INSERT INTO `ip_hirer` VALUES ('84d9b81f-5acf-4d1d-a79f-4795ebeb8c27', '', null, '3966bcca465fd2844e9c1fec6995e4e8972e2f0a', '', null, '', '1', null, '未注册', null, null, null, null, null, null, '259228a8755e2d79', null);
INSERT INTO `ip_hirer` VALUES ('88947207-ab97-4be1-ad4b-4c1d29b34454', 'ty', null, '4c02fadb2b9f4551a4cbdf09394c33d842eb0ce3', 'ty', null, '未注册', '1', null, '22221@qq.com', null, null, null, null, null, null, '6a914c4a08431b9d', null);
INSERT INTO `ip_hirer` VALUES ('8d94e7f9-e4e9-4766-8385-18adf0fc3cd6', '女', null, 'ed6c13e15a768e87c4d4da51c936b19f7a1ec70d', '女', null, '未注册', '1', null, 'wangwei15@yonyou.com', null, null, null, null, null, null, 'd5f4ca99004b1067', null);
INSERT INTO `ip_hirer` VALUES ('8d992a9a-14eb-42ef-b7fd-f654b1e7778a', 'ERHAO', null, '12e62b0d0e6260382a1e80f0bd273490f88c7b87', 'ERHAO', null, '17687654321', '1', null, '未注册', null, null, null, null, null, null, '466e23e6530fc7af', null);
INSERT INTO `ip_hirer` VALUES ('9375feab-8916-40f7-8d5f-acd3ca2aa18a', 'SS', null, 'ab6057a1d10ec661b80ef08b7c1c63dae23fa2de', 'SS', null, '未注册', '1', null, 'sdsd@qq.com', null, null, null, null, null, null, '2b190e6dbc45d450', null);
INSERT INTO `ip_hirer` VALUES ('9419ca9d-ddb0-4ba8-8ac7-084884580d12', 'admin', null, '4feaba37e1de5cf977ead7787dc29eeef68bdd00', 'admin', null, '未注册', '1', null, 'demo@sina.com', null, null, null, null, null, null, 'ed219c06f1a17420', null);
INSERT INTO `ip_hirer` VALUES ('a3999e5a-f6fe-4786-ab19-833b701e875b', '12345678', null, '9967f4e4dbac11c2ccba00fe97b9e69b3c70e903', '12345678', null, '13512345678', '1', null, '未注册', null, null, null, null, null, null, '94d1e7a71d543c7b', null);
INSERT INTO `ip_hirer` VALUES ('a946055c-b696-4ddb-8796-0465596f5d01', 'GH', null, 'aa154ace5442a2759e7e02ea0369b3bb64abc7a7', 'GH', null, '17809008900', '1', null, '未注册', null, null, null, null, null, null, '65683a61a45764ee', null);
INSERT INTO `ip_hirer` VALUES ('ab573f90-05d7-4b5b-b21f-108ef41a52ef', '1234567', null, 'd49e9bf943ffabb7d9ffe6bea65c71d12b5186cc', '1234567', null, '未注册', '1', null, 'wangwei2@yonyou.com', null, null, null, null, null, null, '1fc656232919975c', null);
INSERT INTO `ip_hirer` VALUES ('b27a6ef2-f35c-40a6-8aa4-b7552ada3264', 'hj', null, '073177e03c514959ae1735fca74778308ee483db', 'hj', null, '未注册', '1', null, '134@qq.com', null, null, null, null, null, null, '587cfeae69f6e4ab', null);
INSERT INTO `ip_hirer` VALUES ('b348982a-cc7c-4bb9-9e5a-5ce3e8341e04', 'jiaoyan', null, '22452c7280150623dc6e02237c4c47770eef2514', 'jiaoyan', null, '未注册', '1', null, 'wangwei23@yonyou.com', null, null, null, null, null, null, 'd684c45c835ffa08', null);
INSERT INTO `ip_hirer` VALUES ('b55f42ae-c27a-4013-aa27-1461b77301ed', '火狐注册', null, 'eb639ff16344bf9a19ed9ce64a71b3cf7a50d76e', '火狐注册', null, '未注册', '1', null, 'huohu@yonyou.com', null, null, null, null, null, null, 'a1a969f253d4d63b', null);
INSERT INTO `ip_hirer` VALUES ('b690e7cc-537d-49b6-896f-1596b8184eb7', 'NV', null, '28dd0c7ce85de5b9e8a9c77505a8838d3e924210', 'NV', null, '未注册', '1', null, 'wangwei15@yonyou.com', null, null, null, null, null, null, 'c6c795f17b7023f9', null);
INSERT INTO `ip_hirer` VALUES ('b69f8051-6a16-4cf8-b796-d908fb602f1d', 'shipn', null, '379ead997fadc4f87e6958ea4694b0be8d7a06b7', 'shipn', null, '未注册', '1', null, 'shipn@yonyou.com', null, null, null, null, null, null, '1a729f37a46f6edf', null);
INSERT INTO `ip_hirer` VALUES ('c527a130-2530-488a-be89-05a3aa81865d', 'okp', null, '1efba2c7c18ed6bb503f87dbc941b359cc0c62fc', 'okp', null, '未注册', '1', null, '3211@qq.com', null, null, null, null, null, null, '01e8abb0bcf51bfa', null);
INSERT INTO `ip_hirer` VALUES ('c6762fdb-8236-4b1c-bfc6-0d0697e03bb4', '123456', null, 'da10e89e00e327fc0ae91bc07f75a26520a2e205', '123456', null, '未注册', '1', null, 'wangwei11@yonyou.com', null, null, null, null, null, null, '76a3be45df7fa9fa', null);
INSERT INTO `ip_hirer` VALUES ('c9c6411d-01d5-489e-9b3c-7f3e575ad18a', 'k', null, 'f107608ffc2db360c6b05ec76bc42575610f657c', 'k', null, '未注册', '1', null, '2222@qq.com', null, null, null, null, null, null, '5ef0ba6728fee50d', null);
INSERT INTO `ip_hirer` VALUES ('ca8db660-8858-4c84-a7da-fca918508658', 'test1', null, '4fb073a607b651f43892acc11a213905809e4ad7', 'test1', null, '未注册', '1', null, 'wangwei1@yonyou.com', null, null, null, null, null, null, '20fcbbce2dc1ed15', null);
INSERT INTO `ip_hirer` VALUES ('cfd0b3c7-8cf7-4602-91b3-90ac2d1168b3', '123', null, 'b87fe5aca7ce7489c4b0f636452d20d65ffdff01', '123', null, '未注册', '1', null, '39292929292@12312.com', null, null, null, null, null, null, '27be9aaa94ce066b', null);
INSERT INTO `ip_hirer` VALUES ('d2d4cb1b-77d2-4396-8f2a-03c99cd42677', 'wuhao', null, 'f53d88b2da17d818295aab3babdd7dab43ffc46e', 'wuhao', null, '13912345678', '1', null, '未注册', null, null, null, null, null, null, '10234faa16611738', null);
INSERT INTO `ip_hirer` VALUES ('d4573719-ac7a-4ea3-a842-1f92494443b4', 'Le_win', null, '41b4bfc4e31d1d02ad60a00a7b1cac36971daf79', 'Le_win', null, '未注册', '1', null, 'Le_win@sina.com', null, null, null, null, null, null, '8940944a012788ed', null);
INSERT INTO `ip_hirer` VALUES ('d8d0f1c9-c7e1-4b4a-92d7-691b576fcc75', '1111', null, '0560d99f5ab752a674cdd684862b19e6fe1d77ca', '1111', null, '11111', '1', null, '未注册', null, null, null, null, null, null, 'cae032adf5e2eadd', null);
INSERT INTO `ip_hirer` VALUES ('ddd00466-8dae-4464-b21a-cd9579364201', 'sssss', null, '141c56b23f5e43f913e003860a7e27790b0d99fe', 'sssss', null, '未注册', '1', null, 'shipnqq@yonyou.com', null, null, null, null, null, null, 'ce7ec2bfe6755776', null);
INSERT INTO `ip_hirer` VALUES ('eb0f63a2-6772-429d-8712-f27deca7e9a4', 'QQ', null, 'd0aa8c37d8e8139439ee676982dff3efdac22e1b', 'QQ', null, '未注册', '1', null, '222@qq.com', null, null, null, null, null, null, 'fa2bbcf6e2e1bd13', null);
INSERT INTO `ip_hirer` VALUES ('ecd78a67-cda5-4a87-9150-2aa61c0c5770', 'dddd', null, '895b33d3c96a1b6d8b583ce4d8189687addc60a6', 'dddd', null, '14312345678', '1', null, '未注册', null, null, null, null, null, null, '8ad2f967c1066b92', null);
INSERT INTO `ip_hirer` VALUES ('f273c434-00fd-4986-af83-b2b72806d348', 'shenzhen', null, 'b4269bd0629db6fde0aa1a9c02135bf8ccba494a', 'shenzhen', null, '18287654321', '1', null, '未注册', null, null, null, null, null, null, '9e77f413b4037ff0', null);
INSERT INTO `ip_hirer` VALUES ('fd3e5afb-2712-40b9-87a4-0338aaa4d4e5', 'FF', null, 'cc92eadb3d56d3179367bdc2bb818fa50f27603a', 'FF', null, '未注册', '1', null, '23444@qq.com', null, null, null, null, null, null, '898abcd988dcbf85', null);

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
  `URL` varchar(40) DEFAULT NULL COMMENT '菜单URL',
  `AUTH_LEVEL` varchar(1) NOT NULL DEFAULT '0' COMMENT '授权级别(0,1,2,3)',
  `IS_SHOW` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否显示(1是,0否)',
  `MENU_DESC` varchar(40) DEFAULT NULL COMMENT '菜单描述',
  `IS_JUMP` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`),
  KEY `FK_IP_MENU_REFERENCE_IP_MENU` (`PARENT_MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_menu
-- ----------------------------
INSERT INTO `ip_menu` VALUES ('3a0801f5-ed82-4817-8ba5-a8a56dc21b76', '无', '0', '系统管理', '1', '3', '0', '', '1', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('52eb10c9-7c0b-473e-9f81-0b1e9bcc62ec', '文档二级', 'f80133c9-6532-4581-85a4-6177b73f2c27', '三级', '3', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('73dae08f-4569-445c-9a23-5dd6428bf652', '文档1(1)', 'a4eb02c2-1f38-4061-8ec2-f40ac0fe0dc5', '文档test', '3', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('97313665-2319-4e0d-bd05-a5aa28b9b2d5', '文档', '9f7dc2ce-644c-4fcc-839e-168bae64cdcb', '测试搜索3', '2', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('9f7dc2ce-644c-4fcc-839e-168bae64cdcb', '无', '0', '文档', '1', '2', '0', '', '1', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('a4eb02c2-1f38-4061-8ec2-f40ac0fe0dc5', '文档1', 'e742f733-a7c0-4bc2-815c-21e5475f9fda', '文档1(1)', '2', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('b41a2180-93e2-4932-8fe1-9db935cbb690', '无', '0', '公告', '1', '4', '0', '#/ip/doc/doc', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('b645f6c4-63fe-485e-85db-25abe3aba4bc', 'd', 'fddfa77f-b731-4272-9f69-5dee78e90a0c', 'f', '2', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('b947bea1-5c60-43de-ad57-24ddf832a85b', '无', '0', '考勤', '1', '9', '0', '', '1', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('c8b8c9d5-b93e-4fd1-b94c-7a7af3fecc54', '无', '0', '工作台', '1', '1', '0', '', '1', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('dca5b8fb-063c-4e6c-890e-4842a2ff2c26', '文档', '9f7dc2ce-644c-4fcc-839e-168bae64cdcb', '测试', '2', '2', '0', '', '1', '0', '', '0');
INSERT INTO `ip_menu` VALUES ('e39b94cf-0468-4166-bbf4-364c01b88421', '无', '0', '日历', '1', '3', '0', '#/ip/calendar/rili', '2', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('e742f733-a7c0-4bc2-815c-21e5475f9fda', '无', '0', '文档1', '1', '3', '0', '', '1', '1', '', '0');
INSERT INTO `ip_menu` VALUES ('f80133c9-6532-4581-85a4-6177b73f2c27', '文档', '9f7dc2ce-644c-4fcc-839e-168bae64cdcb', '文档二级', '2', '2', '0', '', '1', '0', '', '0');

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
  `CELLPHONE_NO` varchar(2) DEFAULT NULL COMMENT '手机号码',
  `IS_ENABLED` decimal(10,0) DEFAULT '1',
  `EMPLOYEE_NO` varchar(20) DEFAULT NULL COMMENT '员工号',
  `DUTY` varchar(20) DEFAULT NULL COMMENT '职务',
  `NATIVE_PLACE` varchar(40) DEFAULT NULL COMMENT '原籍',
  `GRADUATE_SCHOOLl` varchar(20) DEFAULT NULL COMMENT '毕业学校',
  `MAJOR` varchar(30) DEFAULT NULL COMMENT '专业',
  `EDUCATION` varchar(20) DEFAULT NULL COMMENT '学历',
  `GRADUATIOIN_TIME` date DEFAULT NULL COMMENT '毕业时间',
  `REMARK` varchar(50) DEFAULT NULL,
  `SALT` varchar(50) DEFAULT NULL,
  `login_ts` bigint(30) DEFAULT NULL,
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
  `CO_CODE` decimal(10,0) NOT NULL,
  `CO_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`THE_ID`),
  KEY `FK_IP_UC_REFERENCE_USER` (`USER_ID`),
  KEY `FK_IP_UC_REFERENCE_COMPA` (`CO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_company
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
  PRIMARY KEY (`THE_ID`),
  KEY `FK_IP_UR_REFERENCE_ROLE` (`ROLE_ID`),
  KEY `FK_IP_UR_REFERENCE_USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_role
-- ----------------------------
