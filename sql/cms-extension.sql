-- ==========================================================
-- 591 家教网 CMS 扩展 - 公告/FAQ 表 + sys_config 站点配置键
-- ==========================================================

-- ========== 1. 公告表 ==========
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement` (
  `id` BIGINT(20) UNSIGNED NOT NULL COMMENT '主键',
  `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '状态(1:上架,0:下架)',
  `sort` INT(11) NOT NULL DEFAULT '0' COMMENT '排序值（越小越靠前）',
  `category` VARCHAR(32) NOT NULL DEFAULT 'notice' COMMENT '分类：float/notice/edu/notify',
  `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告正文（可选）',
  `link_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接（可选）',
  `is_top` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否置顶',
  PRIMARY KEY (`id`),
  KEY `idx_cat_status` (`category`, `status_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告管理';

-- ========== 2. FAQ 表 ==========
DROP TABLE IF EXISTS `sys_faq`;
CREATE TABLE `sys_faq` (
  `id` BIGINT(20) UNSIGNED NOT NULL COMMENT '主键',
  `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '状态(1:显示,0:隐藏)',
  `sort` INT(11) NOT NULL DEFAULT '0' COMMENT '排序值',
  `category` VARCHAR(32) NOT NULL COMMENT '分类：parent/tutor/price/account/contact',
  `category_icon` VARCHAR(16) DEFAULT NULL COMMENT '分类图标 emoji',
  `category_label` VARCHAR(64) DEFAULT NULL COMMENT '分类名称',
  `question` VARCHAR(500) NOT NULL COMMENT '问题',
  `answer` TEXT NOT NULL COMMENT '答案',
  PRIMARY KEY (`id`),
  KEY `idx_cat` (`category`, `status_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='FAQ 帮助';

-- ========== 3. sys_config 新增站点配置键 ==========
-- config_type=6 表示"站点信息"分组（1:系统 2:支付 3:视频 4:存储 5:短信）
INSERT IGNORE INTO `sys_config` (`id`, `config_type`, `content_type`, `config_name`, `config_key`, `config_value`, `config_show`, `sort`) VALUES
-- 站点信息
(100, 6, 1, '品牌名',               'siteBrandName',       '591家教网', 1, 0),
(101, 6, 1, '品牌副标语',           'siteBrandSlogan',     '名校名师优质家教平台', 1, 1),
(102, 6, 1, '客服热线',             'siteHotline',         '13795420591', 1, 2),
(103, 6, 1, '客服邮箱',             'siteCsEmail',         'service@591jiajiao.com', 1, 3),
(104, 6, 1, '客服微信号',           'siteCsWechat',        '', 1, 4),
(105, 6, 1, '客服工作时间',         'siteWorkTime',        '周一至周日 9:00-21:00', 1, 5),
(106, 6, 1, 'ICP 备案号',           'siteIcpNo',           '', 1, 6),
(107, 6, 1, '公安网安备案号',       'sitePublicSecurityNo','', 1, 7),
(108, 6, 1, '底部品牌描述',         'siteFooterDesc',      '专业家教服务平台，提供上门一对一辅导。严选教员，安心托付。', 1, 8),
(109, 6, 1, '版权文案',             'siteCopyright',       '© 2026 591家教网 版权所有', 1, 9),
-- 协议三件套（正文）
(110, 6, 1, '用户服务协议正文 HTML', 'agreementUserHtml',       '', 0, 20),
(111, 6, 1, '免责声明正文 HTML',     'agreementDisclaimerHtml', '', 0, 21),
(112, 6, 1, '隐私政策正文 HTML',     'agreementPrivacyHtml',    '', 0, 22),
-- 请家教表单文案
(113, 6, 1, '请家教页面副标题',     'qjjSubtitle',         '我们提供多种方式帮您找到满意的家教老师', 1, 30),
(114, 6, 1, '请家教提交成功提示',   'qjjSuccessTip',       '需求提交成功！我们将在24小时内联系您，请保持电话畅通。', 1, 31),
-- JSON 数组类配置
(115, 6, 1, '友情链接 JSON',         'siteFriendLinksJson', '[{"name":"天天家教网","url":"https://www.ttgood.com"}]', 1, 40),
(116, 6, 1, '底部菜单组 JSON',       'siteFooterMenusJson',
'[{"group":"找家教","items":[{"label":"教员库","href":"/jy"},{"label":"学员库","href":"/xy"},{"label":"请家教","href":"/qjj"},{"label":"价格参考","href":"/zf"}]},{"group":"教员入口","items":[{"label":"教员注册","href":"/register/teacher"},{"label":"教员登录","href":"/login"},{"label":"个人中心","href":"/center"}]},{"group":"帮助与支持","items":[{"label":"帮助中心","href":"/help"},{"label":"用户协议","href":"/agreement/user"},{"label":"隐私政策","href":"/agreement/privacy"},{"label":"免责声明","href":"/agreement/disclaimer"}]},{"group":"关于我们","items":[{"label":"联系我们","href":"/about/contact"},{"label":"服务号","href":"/about/wechat"},{"label":"公司简介","href":"/about/intro"},{"label":"隐私保护","href":"/agreement/privacy"}]}]',
1, 41),
(117, 6, 1, '价格说明列表 JSON',     'sitePriceNotesJson',
'["以上价格为上门一对一辅导参考价格，在线辅导一般可享 8-9折 优惠。","艺术类（钢琴、美术、书法等）价格另议，通常高于文化课 20%-50%。","小语种（日语、法语、德语等）价格参考大学/成人栏目。","首次上课可安排免费试讲30分钟（需与教员协商）。","平台不收取中介费，教员课时费由教员自定，家长与教员直接结算。","以上价格仅供参考，具体价格请与教员沟通确认。"]',
1, 42);

-- ========== 4. 公告种子数据 ==========
-- 浮球公告（category=float）
INSERT IGNORE INTO `sys_announcement` (`id`, `status_id`, `sort`, `category`, `title`, `content`, `is_top`) VALUES
(100001, 1, 0, 'float', '欢迎使用591家教网', '591家教网致力于为学员和家长提供优质的家教服务，我们拥有大量经过认证的优秀教员，覆盖小学、初中、高中各个年级和科目。', 0),
(100002, 1, 1, 'float', '平台服务升级通知', '为了给您提供更好的服务体验，我们将在本周末进行系统升级维护，届时部分功能可能暂时无法使用，预计维护时间为2小时，给您带来的不便敬请谅解。', 0),
(100003, 1, 2, 'float', '教员认证流程优化', '为了提高教员认证效率，我们优化了认证流程，现在只需要上传学生证或教师资格证即可快速完成认证，审核时间缩短至24小时内。', 0);

-- 首页公告 tab1: 网站公告
INSERT IGNORE INTO `sys_announcement` (`id`, `status_id`, `sort`, `category`, `title`, `link_url`, `is_top`) VALUES
(100101, 1, 0, 'notice', '平台服务升级维护通知', '/notice/service-upgrade', 0),
(100102, 1, 1, 'notice', '五一假期客服时间调整', '/notice/holiday-schedule', 0),
(100103, 1, 2, 'notice', '教员认证流程优化公告', '/notice/cert-optimization', 0),
(100104, 1, 3, 'notice', '新增多个城市站点上线', '/notice/new-cities', 0);

-- 首页公告 tab2: 教育资讯
INSERT IGNORE INTO `sys_announcement` (`id`, `status_id`, `sort`, `category`, `title`, `link_url`, `is_top`) VALUES
(100201, 1, 0, 'edu', '2025年上海中考政策解读', '/notice/zhongkao-policy', 0),
(100202, 1, 1, 'edu', '小学数学思维训练方法分享', '/notice/math-training', 0),
(100203, 1, 2, 'edu', '初中英语听力提分技巧', '/notice/english-listening', 0),
(100204, 1, 3, 'edu', '高考志愿填报注意事项', '/notice/gaokao-guide', 0);

-- 首页公告 tab3: 教/学员须知
INSERT IGNORE INTO `sys_announcement` (`id`, `status_id`, `sort`, `category`, `title`, `link_url`, `is_top`) VALUES
(100301, 1, 0, 'notify', '首次试讲免费，满意后再上课', '/notice/free-trial', 0),
(100302, 1, 1, 'notify', '课时费由家长与教员直接结算', '/notice/payment-policy', 0),
(100303, 1, 2, 'notify', '教员需通过实名认证方可接单', '/notice/cert-required', 0),
(100304, 1, 3, 'notify', '如遇问题请拨打客服热线反馈', '/notice/contact-support', 0);

-- ========== 5. FAQ 种子数据 ==========
-- 家长/学员指南
INSERT IGNORE INTO `sys_faq` (`id`, `status_id`, `sort`, `category`, `category_icon`, `category_label`, `question`, `answer`) VALUES
(200001, 1, 0, 'parent', '👨‍👩‍👧', '家长/学员指南', '如何发布家教需求？', '注册并登录账号 → 点击"请家教"进入需求发布页面 → 填写辅导科目、年级、时间、地点等信息 → 提交需求后，平台会为您推荐合适的教员'),
(200002, 1, 1, 'parent', '👨‍👩‍👧', '家长/学员指南', '如何联系教员？', '在教员库中浏览教员信息 → 点击"预约试讲"按钮 → 填写预约信息并提交 → 教员会在24小时内联系您'),
(200003, 1, 2, 'parent', '👨‍👩‍👧', '家长/学员指南', '平台如何保障教学质量？', '严格审核教员资质，要求提供学历证明 → 建立教员评价体系，家长可查看历史评价 → 提供试讲服务，满意后再正式开始 → 7天内不满意可免费更换教员'),
(200004, 1, 3, 'parent', '👨‍👩‍👧', '家长/学员指南', '试讲是否收费？', '首次试讲通常为免费或象征性收费（如半小时30-50元），具体由教员和家长协商。试讲满意后再签订正式辅导协议。');

-- 教员指南
INSERT IGNORE INTO `sys_faq` (`id`, `status_id`, `sort`, `category`, `category_icon`, `category_label`, `question`, `answer`) VALUES
(200101, 1, 0, 'tutor', '👨‍🏫', '教员指南', '如何注册成为教员？', '点击页面右上角"注册"按钮 → 选择"我是教员"身份 → 填写手机号、密码等基本信息 → 完善个人资料和教学信息 → 等待平台审核通过（1-3个工作日）'),
(200102, 1, 1, 'tutor', '👨‍🏫', '教员指南', '如何成为优质教员？', '完善个人资料，上传真实照片和证书 → 及时响应家长预约请求（24小时内） → 认真备课，提供高质量教学服务 → 积累好评，提升平台信誉度 → 定期更新可授课时间和科目'),
(200103, 1, 2, 'tutor', '👨‍🏫', '教员指南', '教员需要提供哪些资料？', '身份证照片（用于实名认证） → 学历证书或学生证 → 教师资格证（在职教师需提供） → 个人简历和教学经历 → 近期生活照'),
(200104, 1, 3, 'tutor', '👨‍🏫', '教员指南', '如何接单和管理订单？', '登录个人中心后，可以在"需求管理"中查看家长发布的需求，主动申请感兴趣的订单。也可以等待平台根据您的资料推荐给家长。');

-- 收费标准
INSERT IGNORE INTO `sys_faq` (`id`, `status_id`, `sort`, `category`, `category_icon`, `category_label`, `question`, `answer`) VALUES
(200201, 1, 0, 'price', '💰', '收费标准', '大学生家教收费是多少？', '大学生家教参考价格为 50-100元/小时。在校大学生学习成绩优秀，有辅导经验，性价比高。'),
(200202, 1, 1, 'price', '💰', '收费标准', '专职教员收费是多少？', '专职教员参考价格为 100-150元/小时。全职家教老师教学经验丰富，有专业教学方法，效果显著。'),
(200203, 1, 2, 'price', '💰', '收费标准', '在职教师收费是多少？', '在职教师参考价格为 150-300元/小时。学校在职教师深谙考试重点，权威教学经验，提分快速。'),
(200204, 1, 3, 'price', '💰', '收费标准', '费用如何支付？', '以上价格仅供参考，实际费用由教员和家长协商确定 → 价格会根据科目难度、年级、上课地点等因素调整 → 课时费一般按课时结算，由家长直接支付给教员');

-- 账号相关
INSERT IGNORE INTO `sys_faq` (`id`, `status_id`, `sort`, `category`, `category_icon`, `category_label`, `question`, `answer`) VALUES
(200301, 1, 0, 'account', '🔒', '账号相关', '忘记密码怎么办？', '点击登录页面的"找回密码" → 输入注册时使用的手机号 → 获取短信验证码并验证 → 设置新密码即可'),
(200302, 1, 1, 'account', '🔒', '账号相关', '如何修改个人信息？', '登录后进入"个人中心" → 点击"个人资料"菜单 → 修改需要变更的信息 → 点击保存即可'),
(200303, 1, 2, 'account', '🔒', '账号相关', '如何注销账号？', '如需注销账号，请联系客服热线 13795420591，客服人员会在确认身份后为您处理。注销后账号数据将无法恢复。');

-- 联系我们
INSERT IGNORE INTO `sys_faq` (`id`, `status_id`, `sort`, `category`, `category_icon`, `category_label`, `question`, `answer`) VALUES
(200401, 1, 0, 'contact', '📞', '联系我们', '平台客服工作时间？', '客服热线工作时间为 周一至周日 9:00-21:00，节假日照常服务。'),
(200402, 1, 1, 'contact', '📞', '联系我们', '如何投诉或建议？', '拨打客服热线：13795420591 → 发送邮件至：service@591jiajiao.com → 通过微信客服进行反馈 → 我们会在24小时内处理您的反馈');
