-- ============================================================
-- 领课教育系统 - 基础表结构 (从 Mapper XML 反向工程)
-- 注意: 此文件基于代码反向生成, 可能与官方SQL有细微差异
-- 执行顺序: 1.本文件 2.tutor-platform-init.sql 3.tutor-platform-seed.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS `roncoo_education` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `roncoo_education`;

-- ===== system 服务表 =====

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `mobile` char(11) DEFAULT NULL,
  `mobile_salt` varchar(36) DEFAULT NULL,
  `mobile_psw` varchar(100) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理员';

CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `config_type` tinyint DEFAULT 1,
  `content_type` tinyint DEFAULT 1,
  `config_name` varchar(100) DEFAULT NULL,
  `config_key` varchar(100) DEFAULT NULL,
  `config_value` text,
  `config_show` bit(1) DEFAULT b'1',
  `remark` varchar(255) DEFAULT NULL,
  `sort` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置';

CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `parent_id` bigint DEFAULT 0,
  `is_show` bit(1) DEFAULT b'1',
  `menu_type` tinyint DEFAULT 1 COMMENT '1目录 2菜单 3按钮',
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_icon` varchar(100) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `component` varchar(200) DEFAULT NULL,
  `apis` varchar(500) DEFAULT NULL,
  `permission` varchar(200) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `role_name` varchar(50) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

CREATE TABLE IF NOT EXISTS `sys_role_user` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色用户关联';

CREATE TABLE IF NOT EXISTS `sys_menu_role` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `menu_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单角色关联';

CREATE TABLE IF NOT EXISTS `sys_log` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `user_id` bigint DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `operation` varchar(200) DEFAULT NULL,
  `method` varchar(200) DEFAULT NULL,
  `path` varchar(500) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';

CREATE TABLE IF NOT EXISTS `website_carousel` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `carousel_url` varchar(500) DEFAULT NULL,
  `carousel_target` varchar(500) DEFAULT NULL,
  `carousel_title` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图';

CREATE TABLE IF NOT EXISTS `website_nav` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `nav_title` varchar(100) DEFAULT NULL,
  `nav_url` varchar(500) DEFAULT NULL,
  `target` varchar(50) DEFAULT '_blank',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导航';

CREATE TABLE IF NOT EXISTS `website_link` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `link_name` varchar(100) DEFAULT NULL,
  `link_url` varchar(500) DEFAULT NULL,
  `link_target` varchar(50) DEFAULT '_blank',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接';

CREATE TABLE IF NOT EXISTS `website_app` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `app_type` tinyint DEFAULT 1,
  `app_url` varchar(500) DEFAULT NULL,
  `app_img` varchar(500) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='APP版本';

-- ===== user 服务表 =====

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `mobile` varchar(20) DEFAULT NULL,
  `mobile_salt` varchar(36) DEFAULT NULL,
  `mobile_psw` varchar(100) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `user_sex` tinyint DEFAULT 0,
  `user_age` date DEFAULT NULL,
  `user_head` varchar(500) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `union_id` varchar(100) DEFAULT NULL,
  `open_id` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `register_source` tinyint DEFAULT 1,
  `user_type` tinyint NOT NULL DEFAULT 0 COMMENT '用户类型 0未设置 1教员 2学员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

CREATE TABLE IF NOT EXISTS `users_account` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `user_id` bigint NOT NULL,
  `available_amount` decimal(15,2) DEFAULT 0.00,
  `freeze_amount` decimal(15,2) DEFAULT 0.00,
  `sign` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户';

CREATE TABLE IF NOT EXISTS `users_account_consume` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `user_id` bigint NOT NULL,
  `consume_type` tinyint DEFAULT NULL,
  `consume_amount` decimal(15,2) DEFAULT 0.00,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费记录';

CREATE TABLE IF NOT EXISTS `users_log` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `user_id` bigint DEFAULT NULL,
  `login_status` tinyint DEFAULT NULL,
  `login_ip` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `browser` varchar(200) DEFAULT NULL,
  `os` varchar(200) DEFAULT NULL,
  `login_client` tinyint DEFAULT NULL,
  `register_source` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';

CREATE TABLE IF NOT EXISTS `lecturer` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `user_id` bigint DEFAULT NULL,
  `lecturer_name` varchar(50) DEFAULT NULL,
  `lecturer_mobile` char(11) DEFAULT NULL,
  `lecturer_position` varchar(100) DEFAULT NULL,
  `lecturer_head` varchar(500) DEFAULT NULL,
  `introduce` text,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讲师';

CREATE TABLE IF NOT EXISTS `order_info` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `user_id` bigint NOT NULL,
  `order_no` bigint DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `course_name` varchar(200) DEFAULT NULL,
  `course_price` decimal(15,2) DEFAULT 0.00,
  `ruling_price` decimal(15,2) DEFAULT 0.00,
  `pay_type` tinyint DEFAULT NULL,
  `order_status` tinyint DEFAULT 1,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

CREATE TABLE IF NOT EXISTS `order_pay` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `order_no` bigint DEFAULT NULL,
  `serial_number` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `course_id` bigint DEFAULT NULL,
  `ruling_price` decimal(15,2) DEFAULT 0.00,
  `pay_type` tinyint DEFAULT NULL,
  `order_status` tinyint DEFAULT 1,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录';

CREATE TABLE IF NOT EXISTS `msg` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `sort` int NOT NULL DEFAULT 0,
  `msg_type` tinyint DEFAULT 1,
  `msg_title` varchar(200) DEFAULT NULL,
  `msg_text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';

CREATE TABLE IF NOT EXISTS `msg_user` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `msg_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `is_read` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息用户';

CREATE TABLE IF NOT EXISTS `region` (
  `id` bigint NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status_id` tinyint NOT NULL DEFAULT 1,
  `parent_id` bigint DEFAULT 0,
  `region_type` tinyint DEFAULT 1,
  `region_name` varchar(100) DEFAULT NULL,
  `merger_name` varchar(200) DEFAULT NULL,
  `level` tinyint DEFAULT 1,
  `province_code` varchar(20) DEFAULT NULL,
  `city_code` varchar(20) DEFAULT NULL,
  `district_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区';

-- ===== 种子数据: 超级管理员 =====
-- 密码: RonCoo.123 (SHA1 with salt)

INSERT IGNORE INTO `sys_user` (`id`, `status_id`, `sort`, `mobile`, `mobile_salt`, `mobile_psw`, `real_name`, `remark`)
VALUES (1, 1, 0, '18302045627', 'a95aaa81a0e94e8aa2df16549fc13d07', 'e07b64e05b614f069febc2b7dbe8ed17ffd3bd1a', '超级管理员', '系统初始化');

INSERT IGNORE INTO `sys_role` (`id`, `status_id`, `sort`, `role_name`, `remark`)
VALUES (1, 1, 0, '超级管理员', '拥有所有权限');

INSERT IGNORE INTO `sys_role_user` (`id`, `status_id`, `role_id`, `user_id`)
VALUES (1, 1, 1, 1);

-- ===== 种子数据: 系统配置 =====
INSERT IGNORE INTO `sys_config` (`id`, `config_type`, `content_type`, `config_name`, `config_key`, `config_value`, `config_show`, `sort`)
VALUES
(1, 1, 1, 'RSA登录私钥', 'rsaLoginPrivateKey', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJvU/nAMpYnE0a6+XPMP/5Z6dbGxGi5j6hn6FmZ9FMVZX5mFKPKA1anZ0xK0Ff7GS5g7q/cqF7LWWV7GnCHjRwwPwc7oL6FmZW0z6F3lyhQ7r7e0YBbedkU0JX0XK2reDlrFhFNxBg6JnGQfn6aY+L5Z7BWJHa1RbsF3PzVGNAgMBAAECgYEAgiS+0xv/HJlV2eeZ4K5n4PkMNMHK5+VBbEqiCNrIh3iPr6jQQ5qe24V1EZ0RDvX0y/IJX0LiLz9fJvag5nK37B1TYCWfJfFfa6I4N3e7K0KD/sIZ/0cFV7V3SMc0G7aEF5qV1JYAB+u3VHOBuijR5bN0Z8S5f2RkXP+YEdW4GECQQDN4qGZ0JfG5qNB3kIR2mJKFfbg8g1VQz0ey+QRUPwYvnnvxkUQ0h39L0vw8K07rAMOjCXqDZG0FT+BN5HV19FAkEAw2kJ8mlL5TYUx0ISx/bL6N+8eAcl+FdB+BJ5S8CwxcnT0Nlb+YPMBa1PJK8v0MJb8a5XKCT+ZZWw2f9mPq2uQJBAK+Ay5w6CuSfnA5kPK0Z3EBbXUBfVqjzJ+8l8DIj7N0k7a0CVcMZ5LfVW1XnhiIrPiJz0JWvxQ+RNhnhp3vsUCQFGsjnPz7PEhbB2WdbHzNPhIDKP9hb2KkmFxL0U2TGGKX0SD4a+MsMVFr7ekUjJKjWE1BFn5pDjP0s0rfJPhm0CQQCl1GdPYBV5nVYMp2VG2NeZH3k+vBq3dV0JY0LOAm7C5GJd0G/LQg8YnhYLFuBq0fRKLGF7Qs/RfJLB1FLpVMU', 0, 0),
(2, 1, 1, 'RSA登录公钥', 'rsaLoginPublicKey', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCb1P5wDKWJxNGuvlzzD/+WenWxsRouY+oZ+hZmfRTFWV+ZhSjygtWp2dMShBX+xkuYO6v3Khey1llexpwh40cMD8HO6C+hZmVtM+hd5coUO6+3tGAW3nZFNCV9Fytq3XRaxYRTcQYOiZxkH5+mmPi+WewViR2tUW7Bdz81RjQIDAQAB', 0, 0),
(3, 1, 1, '短信平台', 'smsPlatform', '5', 0, 0),
(4, 1, 1, '存储平台', 'storagePlatform', '2', 0, 0),
(5, 1, 1, '网站标题', 'websiteTitle', '家教在线平台', 1, 0),
(6, 1, 1, '网站关键词', 'websiteKeyword', '家教,在线家教,找家教,做家教', 1, 0),
(7, 1, 1, '网站描述', 'websiteDesc', '家教在线平台 - 找家教、做家教、一站式服务', 1, 0),
(8, 1, 1, '网站版权', 'websiteCopyright', '© 2024 家教在线平台', 1, 0),
-- ===== 腾讯云短信配置（由管理员在后台填入）=====
(20, 5, 1, '腾讯云短信签名', 'tencentSmsSignName', '', 0, 20),
(21, 5, 1, '腾讯云短信AppID', 'tencentSmsSdkAppId', '', 0, 21),
(22, 5, 1, '腾讯云SecretId', 'tencentSmsAccessKeyId', '', 0, 22),
(23, 5, 1, '腾讯云SecretKey', 'tencentSmsAccessKeySecret', '', 0, 23),
(24, 5, 1, '腾讯云验证码模板ID', 'tencentSmsAuthCode', '', 0, 24),
(25, 5, 1, '腾讯云购买通知模板ID', 'tencentSmsPurchaseCode', '', 0, 25),
(26, 5, 1, '腾讯云地域', 'tencentSmsRegion', 'ap-guangzhou', 0, 26);
