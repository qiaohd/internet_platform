-- ----------------------------
-- Table structure for  `ip_user_hide_menu`
-- ----------------------------
SET FOREIGN_KEY_CHECKS=0; 
DROP TABLE IF EXISTS `ip_user_hide_menu`;
CREATE TABLE `ip_user_hide_menu` (
  `user_id` varchar(60) DEFAULT NULL COMMENT '用户ID（关联用户表或租户表）',
  `menu_id` varchar(60) DEFAULT NULL COMMENT '菜单ID，关联菜单表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ip_user_hide_menu
-- ----------------------------