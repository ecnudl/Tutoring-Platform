-- ============================================================
-- 家教平台 种子数据脚本 (基础字典)
-- tutor-platform-seed.sql
-- ============================================================

USE `roncoo_education`;
SET NAMES utf8mb4;

-- ===========================================================
-- 一、科目分类（12个一级分类，对齐 ttgood.com 分类体系）
-- ID: 10001 ~ 10012
-- ===========================================================
INSERT INTO `dict_subject_category` (`id`, `parent_id`, `category_name`, `sort`) VALUES
(10001, 0, '幼儿早教',   1),
(10002, 0, '小学',       2),
(10003, 0, '初中',       3),
(10004, 0, '高中',       4),
(10005, 0, '英语/留学',  5),
(10006, 0, '大学/考研',  6),
(10007, 0, '语言类',     7),
(10008, 0, '音乐/舞蹈',  8),
(10009, 0, '美术/书法',  9),
(10010, 0, '体育/棋牌',  10),
(10011, 0, '计算机/IT',  11),
(10012, 0, '职业/生活',  12);

-- ===========================================================
-- 二、科目字典（关联分类）
-- ID: 2001 ~ 2099
-- ===========================================================
INSERT INTO `dict_subject` (`id`, `category_id`, `subject_name`, `is_hot`, `sort`) VALUES
-- 幼儿早教
(2001, 10001, '学前教育',     1, 1),
(2002, 10001, '幼儿陪读',     0, 2),
(2003, 10001, '启蒙认知',     0, 3),
-- 小学
(2010, 10002, '小学语文',     1, 1),
(2011, 10002, '小学数学',     1, 2),
(2012, 10002, '小学英语',     1, 3),
(2013, 10002, '小学全科',     1, 4),
(2014, 10002, '小学奥数',     0, 5),
(2015, 10002, '小学作文',     0, 6),
-- 初中
(2020, 10003, '初中语文',     1, 1),
(2021, 10003, '初中数学',     1, 2),
(2022, 10003, '初中英语',     1, 3),
(2023, 10003, '初中物理',     1, 4),
(2024, 10003, '初中化学',     1, 5),
(2025, 10003, '初中生物',     0, 6),
(2026, 10003, '初中历史',     0, 7),
(2027, 10003, '初中地理',     0, 8),
(2028, 10003, '初中政治',     0, 9),
(2029, 10003, '初中全科',     0, 10),
-- 高中
(2030, 10004, '高中语文',     1, 1),
(2031, 10004, '高中数学',     1, 2),
(2032, 10004, '高中英语',     1, 3),
(2033, 10004, '高中物理',     1, 4),
(2034, 10004, '高中化学',     1, 5),
(2035, 10004, '高中生物',     0, 6),
(2036, 10004, '高中历史',     0, 7),
(2037, 10004, '高中地理',     0, 8),
(2038, 10004, '高中政治',     0, 9),
-- 英语/留学
(2040, 10005, '新概念英语',   1, 1),
(2041, 10005, '英语口语',     1, 2),
(2042, 10005, '商务英语',     0, 3),
(2043, 10005, '雅思',         1, 4),
(2044, 10005, '托福',         1, 5),
(2045, 10005, 'SAT',          0, 6),
(2046, 10005, 'GRE',          0, 7),
-- 大学/考研
(2050, 10006, '大学高数',     0, 1),
(2051, 10006, '大学英语',     0, 2),
(2052, 10006, '考研数学',     0, 3),
(2053, 10006, '考研英语',     0, 4),
(2054, 10006, '考研政治',     0, 5),
(2055, 10006, '考研专业课',   0, 6),
-- 语言类
(2060, 10007, '日语',         1, 1),
(2061, 10007, '韩语',         0, 2),
(2062, 10007, '法语',         0, 3),
(2063, 10007, '德语',         0, 4),
(2064, 10007, '西班牙语',     0, 5),
(2065, 10007, '意大利语',     0, 6),
(2066, 10007, '俄语',         0, 7),
(2067, 10007, '对外汉语',     1, 8),
-- 音乐/舞蹈
(2070, 10008, '钢琴',         1, 1),
(2071, 10008, '吉他',         1, 2),
(2072, 10008, '小提琴',       0, 3),
(2073, 10008, '古筝',         0, 4),
(2074, 10008, '声乐',         0, 5),
(2075, 10008, '架子鼓',       0, 6),
(2076, 10008, '民族舞',       0, 7),
(2077, 10008, '拉丁舞',       0, 8),
(2078, 10008, '芭蕾舞',       0, 9),
-- 美术/书法
(2080, 10009, '素描',         1, 1),
(2081, 10009, '水彩',         0, 2),
(2082, 10009, '国画',         0, 3),
(2083, 10009, '油画',         0, 4),
(2084, 10009, '书法',         1, 5),
(2085, 10009, '卡通画',       0, 6),
-- 体育/棋牌
(2090, 10010, '游泳',         0, 1),
(2091, 10010, '羽毛球',       0, 2),
(2092, 10010, '网球',         0, 3),
(2093, 10010, '围棋',         1, 4),
(2094, 10010, '中国象棋',     0, 5),
(2095, 10010, '国际象棋',     0, 6),
(2096, 10010, '跆拳道',       0, 7),
-- 计算机/IT
(2100, 10011, '编程',         1, 1),
(2101, 10011, '计算机基础',   0, 2),
(2102, 10011, 'Python',       0, 3),
(2103, 10011, 'C/C++',        0, 4),
(2104, 10011, 'Java',         0, 5),
(2105, 10011, '网页设计',     0, 6),
-- 职业/生活
(2110, 10012, '心理辅导',     0, 1),
(2111, 10012, '演讲与口才',   0, 2),
(2112, 10012, '摄影',         0, 3);

-- ===========================================================
-- 三、年级字典
-- ===========================================================
INSERT INTO `dict_grade` (`id`, `grade_name`, `grade_level`, `sort`) VALUES
(3001, '幼儿园',       0, 0),
(3002, '小学一年级',   1, 1),
(3003, '小学二年级',   1, 2),
(3004, '小学三年级',   1, 3),
(3005, '小学四年级',   1, 4),
(3006, '小学五年级',   1, 5),
(3007, '小学六年级',   1, 6),
(3008, '初一',         2, 7),
(3009, '初二',         2, 8),
(3010, '初三',         2, 9),
(3011, '高一',         3, 10),
(3012, '高二',         3, 11),
(3013, '高三',         3, 12),
(3014, '大学',         4, 13),
(3015, '成人',         5, 14);

-- ===========================================================
-- 四、教员标签字典
-- ===========================================================
INSERT INTO `dict_tutor_tag` (`id`, `tag_name`, `sort`) VALUES
(4001, '经验丰富',   1),
(4002, '耐心细致',   2),
(4003, '提分显著',   3),
(4004, '名校毕业',   4),
(4005, '在职教师',   5),
(4006, '上门辅导',   6),
(4007, '可网课',     7),
(4008, '免费试讲',   8),
(4009, '基础巩固',   9),
(4010, '小升初',     10),
(4011, '中考冲刺',   11),
(4012, '高考冲刺',   12),
(4013, '认证教员',   13),
(4014, '明星教员',   14);

-- ===========================================================
-- 五、后台菜单（家教管理 + 新增地理/科目/内容管理菜单）
-- ===========================================================

-- 一级菜单: 家教管理
INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(800000, NOW(), NOW(), 1, 0, '家教管理', '', 'el-icon-s-custom', 1, 50)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

-- 二级菜单: 教员/需求/预约/申请/学员/反馈/VIP (含 apis 用于 RBAC 网关白名单)
INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`, `apis`) VALUES
(800100, NOW(), NOW(), 1, 800000, '教员审核',   '/tutor/audit',         '', 1, 1, '/user/admin/tutor-audit/page,/user/admin/tutor-audit/view,/user/admin/tutor-audit/audit,/user/admin/tutor-audit/pending-count'),
(800101, NOW(), NOW(), 1, 800000, '教员列表',   '/tutor/list',          '', 1, 2, '/user/admin/tutor/page,/user/admin/tutor/view,/user/admin/tutor/save,/user/admin/tutor/edit,/user/admin/tutor/delete'),
(800102, NOW(), NOW(), 1, 800000, '需求审核',   '/requirement/audit',   '', 1, 3, '/user/admin/requirement-audit/page,/user/admin/requirement-audit/view,/user/admin/requirement-audit/approve,/user/admin/requirement-audit/reject,/user/admin/requirement-audit/match,/user/admin/requirement-audit/pending-count'),
(800103, NOW(), NOW(), 1, 800000, '需求列表',   '/requirement/list',    '', 1, 4, '/user/admin/requirement/page,/user/admin/requirement/view,/user/admin/requirement/edit,/user/admin/requirement/delete'),
(800104, NOW(), NOW(), 1, 800000, '预约管理',   '/reservation/list',    '', 1, 5, '/user/admin/reservation/page,/user/admin/reservation/view,/user/admin/reservation/match,/user/admin/reservation/reject,/user/admin/reservation/publish-requirement,/user/admin/reservation/pending-count'),
(800105, NOW(), NOW(), 1, 800000, '申请管理',   '/application/list',    '', 1, 6, '/user/admin/application/page,/user/admin/application/view'),
(800106, NOW(), NOW(), 1, 800000, '学员列表',   '/student/list',        '', 1, 7, '/user/admin/student/page,/user/admin/student/view'),
(800107, NOW(), NOW(), 1, 800000, '反馈管理',   '/feedback/list',       '', 1, 8, '/user/admin/feedback/page,/user/admin/feedback/view,/user/admin/feedback/audit-pass,/user/admin/feedback/audit-reject'),
(800108, NOW(), NOW(), 1, 800000, 'VIP管理',    '/vip/list',            '', 1, 9, '/user/admin/vip/page,/user/admin/vip/view,/user/admin/vip/save,/user/admin/vip/edit')
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`), `apis`=VALUES(`apis`);

-- 一级菜单: 地理配置
INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(810000, NOW(), NOW(), 1, 0, '地理配置', '', 'el-icon-location', 1, 51)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(810100, NOW(), NOW(), 1, 810000, '城市管理',   '/city/list',           '', 1, 1),
(810101, NOW(), NOW(), 1, 810000, '区域管理',   '/district/list',       '', 1, 2),
(810102, NOW(), NOW(), 1, 810000, '高校管理',   '/university/list',     '', 1, 3)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

-- 一级菜单: 科目配置
INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(820000, NOW(), NOW(), 1, 0, '科目配置', '', 'el-icon-notebook-2', 1, 52)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(820100, NOW(), NOW(), 1, 820000, '科目分类',   '/subject-category/list', '', 1, 1),
(820101, NOW(), NOW(), 1, 820000, '科目管理',   '/dict/subject',          '', 1, 2),
(820102, NOW(), NOW(), 1, 820000, '年级管理',   '/dict/grade',            '', 1, 3),
(820103, NOW(), NOW(), 1, 820000, '标签管理',   '/dict/tag',              '', 1, 4)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

-- 一级菜单: 内容管理
INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(830000, NOW(), NOW(), 1, 0, '内容管理', '', 'el-icon-document', 1, 53)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);

INSERT INTO `sys_menu` (`id`, `gmt_create`, `gmt_modified`, `status_id`, `parent_id`, `menu_name`, `path`, `menu_icon`, `menu_type`, `sort`) VALUES
(830100, NOW(), NOW(), 1, 830000, '轮播图管理',   '/content/banner',        '', 1, 1),
(830101, NOW(), NOW(), 1, 830000, '文章管理',     '/content/article',       '', 1, 2),
(830102, NOW(), NOW(), 1, 830000, '首页配置',     '/homepage/config',       '', 1, 3),
(830103, NOW(), NOW(), 1, 830000, '价格参考',     '/price-reference/list',  '', 1, 4)
ON DUPLICATE KEY UPDATE `menu_name`=VALUES(`menu_name`);
