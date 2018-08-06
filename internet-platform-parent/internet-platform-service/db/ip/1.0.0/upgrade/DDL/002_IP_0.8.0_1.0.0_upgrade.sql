INSERT INTO `ip_menu` VALUES ('2a029157-a2ab-4a21-bca0-d6873b7034e6', '无', '0', '公告', '1', 3, '1', '#/ip/workspace/noticeList/noticeList', '2', '1', '', '0', 'Icon3pages/ip/menu/images/icon/TaskIcon3.png', '');
INSERT INTO `ip_index_config` VALUES ('4d270509-2d1e-45db-aebc-d81194d5f78a', '2a029157-a2ab-4a21-bca0-d6873b7034e6', '公告', '公告', '#/ip/workspace/noticeDetail/noticeDetail', 'notice/get', 'noticeId', 'Y');

ALTER TABLE `ip_company`
ADD COLUMN `CO_CODE_TMP` varchar(30) NULL COMMENT '部门原始code' AFTER `CO_NAME3`;


ALTER TABLE `ip_notice`
MODIFY COLUMN `notice_content`  varchar(3000) NOT NULL COMMENT '公告标题';

