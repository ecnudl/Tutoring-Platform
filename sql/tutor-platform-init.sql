-- ============================================================
-- 家教平台 DDL 初始化脚本
-- tutor-platform-init.sql
-- 共24张新表 + ALTER语句
-- 对齐 ttgood.com 信息架构：城市/区域/高校/科目层级/教员多区域多科目
-- ============================================================

USE `roncoo_education`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ===========================================================
-- 一、地理字典表（城市 → 区域 → 子区域）
-- ===========================================================

-- 1. dict_city 城市表
DROP TABLE IF EXISTS `dict_city`;
CREATE TABLE `dict_city` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_name`     VARCHAR(50)   NOT NULL                COMMENT '城市名称',
  `city_pinyin`   VARCHAR(50)   NOT NULL                COMMENT '拼音slug(shanghai)',
  `province_name` VARCHAR(50)   DEFAULT NULL            COMMENT '省份名称',
  `is_hot`        tinyint       NOT NULL DEFAULT 0      COMMENT '是否热门城市',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_city_pinyin` (`city_pinyin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='城市表';

-- 2. dict_district 区域表
DROP TABLE IF EXISTS `dict_district`;
CREATE TABLE `dict_district` (
  `id`               BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`        tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_id`          BIGINT        NOT NULL                COMMENT '所属城市ID',
  `district_name`    VARCHAR(50)   NOT NULL                COMMENT '区域名称',
  `district_pinyin`  VARCHAR(50)   DEFAULT NULL            COMMENT '拼音slug',
  `sort`             INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_city_id` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域表';

-- 3. dict_sub_district 子区域/街道/镇
DROP TABLE IF EXISTS `dict_sub_district`;
CREATE TABLE `dict_sub_district` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `district_id`   BIGINT        NOT NULL                COMMENT '所属区域ID',
  `name`          VARCHAR(50)   NOT NULL                COMMENT '街道/镇名称',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_district_id` (`district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子区域/街道/镇';

-- 4. dict_university 高校表
DROP TABLE IF EXISTS `dict_university`;
CREATE TABLE `dict_university` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_id`       BIGINT        NOT NULL                COMMENT '所在城市ID',
  `uni_name`      VARCHAR(100)  NOT NULL                COMMENT '高校名称',
  `uni_short`     VARCHAR(50)   DEFAULT NULL            COMMENT '简称',
  `is_hot`        tinyint       NOT NULL DEFAULT 0      COMMENT '是否热门',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_city_id` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='高校表';

-- ===========================================================
-- 二、科目层级字典
-- ===========================================================

-- 5. dict_subject_category 科目分类(树形)
DROP TABLE IF EXISTS `dict_subject_category`;
CREATE TABLE `dict_subject_category` (
  `id`              BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`       tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `parent_id`       BIGINT        NOT NULL DEFAULT 0      COMMENT '父级ID 0为顶级',
  `category_name`   VARCHAR(50)   NOT NULL                COMMENT '分类名称',
  `sort`            INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目分类(树形)';

-- ===========================================================
-- 三、科目字典（增加 category_id, is_hot 字段）
-- ===========================================================

-- 6. dict_subject 科目字典
DROP TABLE IF EXISTS `dict_subject`;
CREATE TABLE `dict_subject` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `category_id`   BIGINT        DEFAULT NULL            COMMENT '所属分类ID',
  `subject_name`  VARCHAR(50)   NOT NULL                COMMENT '科目名称',
  `is_hot`        tinyint       NOT NULL DEFAULT 0      COMMENT '是否热门',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目字典';

-- 7. dict_grade 年级字典
DROP TABLE IF EXISTS `dict_grade`;
CREATE TABLE `dict_grade` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `grade_name`    VARCHAR(50)   NOT NULL                COMMENT '年级名称',
  `grade_level`   TINYINT       DEFAULT NULL            COMMENT '1小学2初中3高中4大学',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级字典';

-- 8. dict_tutor_tag 教员标签字典
DROP TABLE IF EXISTS `dict_tutor_tag`;
CREATE TABLE `dict_tutor_tag` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tag_name`      VARCHAR(50)   NOT NULL                COMMENT '标签名称',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员标签字典';

-- ===========================================================
-- 四、教员相关表
-- ===========================================================

-- 9. tutor_profile 教员资料表（扩展字段对齐ttgood）
DROP TABLE IF EXISTS `tutor_profile`;
CREATE TABLE `tutor_profile` (
  `id`                  BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`          datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`           tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`             BIGINT        NOT NULL                COMMENT '关联users',
  `display_no`          VARCHAR(20)   DEFAULT NULL            COMMENT '展示编号 如T470001',
  `real_name`           VARCHAR(50)   DEFAULT NULL            COMMENT '真实姓名',
  `gender`              TINYINT       NOT NULL DEFAULT 0      COMMENT '性别 0未设置 1男 2女',
  `birth_date`          DATE          DEFAULT NULL            COMMENT '出生日期',
  `avatar`              VARCHAR(500)  DEFAULT NULL            COMMENT '头像(object key)',
  `mobile`              VARCHAR(20)   DEFAULT NULL            COMMENT '联系电话',
  `id_card`             VARCHAR(18)   DEFAULT NULL            COMMENT '身份证号',
  `tutor_type`          TINYINT       NOT NULL DEFAULT 1      COMMENT '1大学生 2专职 3在职教师 4海归外教 5其他',
  `degree`              TINYINT       NOT NULL DEFAULT 0      COMMENT '0未设置 1高中 2大专 3本科 4硕士 5博士',
  `university`          VARCHAR(100)  DEFAULT NULL            COMMENT '毕业/在读院校名称',
  `university_id`       BIGINT        DEFAULT NULL            COMMENT '关联dict_university',
  `major`               VARCHAR(100)  DEFAULT NULL            COMMENT '专业',
  `grade_year`          VARCHAR(20)   DEFAULT NULL            COMMENT '年级 如大一大二',
  `high_school`         VARCHAR(100)  DEFAULT NULL            COMMENT '高中',
  `hometown_province`   VARCHAR(50)   DEFAULT NULL            COMMENT '籍贯省份',
  `teaching_experience` TEXT                                  COMMENT '教学经验/经历',
  `self_introduction`   TEXT                                  COMMENT '自我介绍/描述',
  `certificates_desc`   TEXT                                  COMMENT '所获证书文字描述',
  `tags`                VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON数组 标签ID',
  `city_id`             BIGINT        DEFAULT NULL            COMMENT '所在城市ID',
  `district_id`         BIGINT        DEFAULT NULL            COMMENT '所在区县ID',
  `address`             VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `latitude`            DECIMAL(10,7) DEFAULT NULL            COMMENT '纬度',
  `longitude`           DECIMAL(10,7) DEFAULT NULL            COMMENT '经度',
  `teaching_method`     TINYINT       NOT NULL DEFAULT 0      COMMENT '授课方式 0不限 1教员上门 2学员上门 3网课 4协商',
  `price_min`           DECIMAL(10,2) NOT NULL DEFAULT 0.00   COMMENT '最低课时费(元/小时)',
  `price_max`           DECIMAL(10,2) NOT NULL DEFAULT 0.00   COMMENT '最高课时费(元/小时)',
  `salary_remark`       VARCHAR(200)  DEFAULT NULL            COMMENT '薪资要求备注',
  `free_trial`          TINYINT       NOT NULL DEFAULT 0      COMMENT '是否可试讲 0否 1是',
  `audit_status`        TINYINT       NOT NULL DEFAULT 0      COMMENT '0草稿 1待审核 2已通过 3已拒绝 4已发布',
  `audit_remark`        VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  `is_star`             TINYINT       NOT NULL DEFAULT 0      COMMENT '是否明星教员',
  `is_verified`         TINYINT       NOT NULL DEFAULT 0      COMMENT '是否实名认证',
  `sort`                INT           NOT NULL DEFAULT 0      COMMENT '排序权重',
  `view_count`          INT           NOT NULL DEFAULT 0      COMMENT '浏览次数',
  `success_count`       INT           NOT NULL DEFAULT 0      COMMENT '成功次数',
  `login_count`         INT           NOT NULL DEFAULT 0      COMMENT '登录次数',
  `last_login_time`     DATETIME      DEFAULT NULL            COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_display_no` (`display_no`),
  KEY `idx_city_district` (`city_id`, `district_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_tutor_type` (`tutor_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员资料表';

-- 10. tutor_subject 教员科目关联(多对多)
DROP TABLE IF EXISTS `tutor_subject`;
CREATE TABLE `tutor_subject` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID(tutor_profile.id)',
  `subject_id`    BIGINT        NOT NULL                COMMENT '科目ID(dict_subject.id)',
  `category_id`   BIGINT        DEFAULT NULL            COMMENT '所属分类ID(dict_subject_category.id)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutor_subject` (`tutor_id`, `subject_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员科目关联';

-- 11. tutor_teaching_area 教员授课区域(多对多)
DROP TABLE IF EXISTS `tutor_teaching_area`;
CREATE TABLE `tutor_teaching_area` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID(tutor_profile.id)',
  `city_id`       BIGINT        NOT NULL                COMMENT '城市ID',
  `district_id`   BIGINT        DEFAULT NULL            COMMENT '区域ID(NULL表示全市)',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_id` (`tutor_id`),
  KEY `idx_city_district` (`city_id`, `district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员授课区域';

-- 12. tutor_certification 教员资质证书
DROP TABLE IF EXISTS `tutor_certification`;
CREATE TABLE `tutor_certification` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID',
  `cert_type`     TINYINT       DEFAULT NULL            COMMENT '1身份证 2学生证 3教师资格证 4学历证 5其他',
  `cert_name`     VARCHAR(100)  DEFAULT NULL            COMMENT '证书名称',
  `cert_url`      VARCHAR(500)  DEFAULT NULL            COMMENT '证书图片(object key)',
  `cert_no`       VARCHAR(100)  DEFAULT NULL            COMMENT '证书编号',
  `audit_status`  TINYINT       NOT NULL DEFAULT 0      COMMENT '0待审核 1已通过 2已拒绝',
  `audit_remark`  VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员资质证书';

-- 13. tutor_audit_record 教员审核记录
DROP TABLE IF EXISTS `tutor_audit_record`;
CREATE TABLE `tutor_audit_record` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID',
  `auditor_id`    BIGINT        DEFAULT NULL            COMMENT '审核人ID',
  `audit_action`  TINYINT       DEFAULT NULL            COMMENT '1通过 2拒绝',
  `audit_remark`  VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员审核记录';

-- ===========================================================
-- 五、学员/需求相关表
-- ===========================================================

-- 14. student_profile 学员资料
DROP TABLE IF EXISTS `student_profile`;
CREATE TABLE `student_profile` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '关联users',
  `real_name`     VARCHAR(50)   DEFAULT NULL            COMMENT '真实姓名',
  `gender`        TINYINT       NOT NULL DEFAULT 0      COMMENT '性别 0未设置 1男 2女',
  `grade`         VARCHAR(50)   DEFAULT NULL            COMMENT '当前年级',
  `school`        VARCHAR(100)  DEFAULT NULL            COMMENT '学校',
  `city_id`       BIGINT        DEFAULT NULL            COMMENT '城市ID',
  `district_id`   BIGINT        DEFAULT NULL            COMMENT '区县ID',
  `address`       VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `parent_name`   VARCHAR(50)   DEFAULT NULL            COMMENT '家长姓名',
  `parent_mobile` VARCHAR(20)   DEFAULT NULL            COMMENT '家长电话',
  `remark`        TEXT                                  COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员资料';

-- 15. tutor_requirement 学员找老师需求（扩展字段对齐ttgood）
DROP TABLE IF EXISTS `tutor_requirement`;
CREATE TABLE `tutor_requirement` (
  `id`                  BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`          datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`           tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `display_no`          VARCHAR(20)   DEFAULT NULL            COMMENT '展示编号 如A110001',
  `user_id`             BIGINT        NOT NULL                COMMENT '发布人user_id(0=游客)',
  `title`               VARCHAR(200)  DEFAULT NULL            COMMENT '需求标题',
  `subject_ids`         VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON 科目ID数组',
  `grade_id`            BIGINT        DEFAULT NULL            COMMENT '年级ID',
  `city_id`             BIGINT        DEFAULT NULL            COMMENT '城市ID',
  `district_id`         BIGINT        DEFAULT NULL            COMMENT '区县ID',
  `address`             VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `student_gender`      TINYINT       NOT NULL DEFAULT 0      COMMENT '学员性别 0未设置 1男 2女',
  `tutor_gender`        TINYINT       NOT NULL DEFAULT 0      COMMENT '期望教员性别 0不限 1男 2女',
  `tutor_type_pref`     VARCHAR(100)  DEFAULT NULL            COMMENT 'JSON偏好教员类型',
  `teaching_method`     TINYINT       NOT NULL DEFAULT 0      COMMENT '授课方式 0不限 1教员上门 2学员上门 3网课',
  `schedule`            TEXT                                  COMMENT '时间安排',
  `budget_min`          DECIMAL(10,2) DEFAULT NULL            COMMENT '预算下限(元/小时)',
  `budget_max`          DECIMAL(10,2) DEFAULT NULL            COMMENT '预算上限(元/小时)',
  `has_discount`        TINYINT       NOT NULL DEFAULT 0      COMMENT '是否有优惠',
  `requirement_detail`  TEXT                                  COMMENT '需求详情 (学员情况)',
  `transport`           VARCHAR(200)  DEFAULT NULL            COMMENT '交通线路 (自由文本)',
  `frequency`           VARCHAR(50)   DEFAULT NULL            COMMENT '频次 (如 5次/周)',
  `requirement_type`    TINYINT       DEFAULT 0               COMMENT '求教性质 1提高型 2同步辅导 3竞赛 4冲刺 5陪学 6其他',
  `other_requirements`  VARCHAR(500)  DEFAULT NULL            COMMENT '其它要求 (对教员的额外要求)',
  `transport_subsidy`   VARCHAR(100)  DEFAULT NULL            COMMENT '有无车贴 (自由文本)',
  `contact_name`        VARCHAR(50)   DEFAULT NULL            COMMENT '联系人姓名',
  `contact_mobile`      VARCHAR(20)   DEFAULT NULL            COMMENT '联系人电话',
  `contact_wechat`      VARCHAR(50)   DEFAULT NULL            COMMENT '联系人微信',
  `req_status`          TINYINT       NOT NULL DEFAULT 0      COMMENT '0草稿 1待审核 2已发布 3匹配中 4已预约 5已完成 6已关闭',
  `audit_remark`        VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  `view_count`          INT           NOT NULL DEFAULT 0      COMMENT '浏览次数',
  `application_count`   INT           NOT NULL DEFAULT 0      COMMENT '申请次数',
  `matched_at`          DATETIME      DEFAULT NULL            COMMENT '匹配确认时间(admin标已接单)',
  `matched_tutor_remark` VARCHAR(200) DEFAULT NULL            COMMENT '撮合后admin记录的接单教员信息(线下)',
  `target_tutor_user_id` BIGINT       DEFAULT NULL            COMMENT 'Flow1预约时锁定的目标教员user_id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_display_no` (`display_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_city_district` (`city_id`, `district_id`),
  KEY `idx_req_status` (`req_status`),
  KEY `idx_target_tutor` (`target_tutor_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员找老师需求';

-- 16. tutor_requirement_audit 需求审核记录
DROP TABLE IF EXISTS `tutor_requirement_audit`;
CREATE TABLE `tutor_requirement_audit` (
  `id`              BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`       tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `requirement_id`  BIGINT        NOT NULL                COMMENT '需求ID',
  `auditor_id`      BIGINT        DEFAULT NULL            COMMENT '审核人',
  `audit_action`    TINYINT       DEFAULT NULL            COMMENT '1通过 2拒绝',
  `audit_remark`    VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_requirement_id` (`requirement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求审核记录';

-- ===========================================================
-- 六、撮合与预约
-- ===========================================================

-- 17. tutor_application 教员申请需求单
DROP TABLE IF EXISTS `tutor_application`;
CREATE TABLE `tutor_application` (
  `id`              BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`       tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `requirement_id`  BIGINT        NOT NULL                COMMENT '需求ID',
  `tutor_id`        BIGINT        NOT NULL                COMMENT '教员ID tutor_profile.id',
  `user_id`         BIGINT        NOT NULL                COMMENT '教员的user_id',
  `apply_message`   TEXT                                  COMMENT '申请留言',
  `app_status`      TINYINT       NOT NULL DEFAULT 0      COMMENT '0已申请 1入围 2录用 3拒绝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_req_tutor` (`requirement_id`, `tutor_id`),
  KEY `idx_requirement_id` (`requirement_id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员申请需求单';

-- 18. tutor_reservation 学员预约教员
DROP TABLE IF EXISTS `tutor_reservation`;
CREATE TABLE `tutor_reservation` (
  `id`                BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`         tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `student_user_id`   BIGINT        NOT NULL                COMMENT '学员user_id',
  `tutor_user_id`     BIGINT        NOT NULL                COMMENT '教员user_id',
  `tutor_id`          BIGINT        DEFAULT NULL            COMMENT 'tutor_profile.id',
  `subject_id`        BIGINT        DEFAULT NULL            COMMENT '科目ID',
  `contact_name`      VARCHAR(50)   DEFAULT NULL            COMMENT '称谓 (如 张女士)',
  `contact_mobile`    VARCHAR(20)   DEFAULT NULL            COMMENT '联系手机号',
  `contact_wechat`    VARCHAR(50)   DEFAULT NULL            COMMENT '联系微信',
  `schedule_time`     VARCHAR(200)  DEFAULT NULL            COMMENT '预约时间',
  `address`           VARCHAR(200)  DEFAULT NULL            COMMENT '上课地址',
  `remark`            TEXT                                  COMMENT '备注',
  `res_status`        TINYINT       NOT NULL DEFAULT 0      COMMENT '0待处理 1已匹配 2已完成 3已驳回/取消',
  `cancel_reason`     VARCHAR(500)  DEFAULT NULL            COMMENT '取消原因',
  `matched_at`        DATETIME      DEFAULT NULL            COMMENT '匹配确认时间',
  `requirement_id`    BIGINT        DEFAULT NULL            COMMENT '关联的tutor_requirement.id (双写)',
  PRIMARY KEY (`id`),
  KEY `idx_student` (`student_user_id`),
  KEY `idx_tutor` (`tutor_user_id`),
  KEY `idx_requirement` (`requirement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员预约教员';

-- 19. tutor_shortlist 学员备选老师
DROP TABLE IF EXISTS `tutor_shortlist`;
CREATE TABLE `tutor_shortlist` (
  `id`              BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`       tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`         BIGINT        NOT NULL                COMMENT '学员user_id',
  `tutor_id`        BIGINT        NOT NULL                COMMENT '教员ID',
  `requirement_id`  BIGINT        DEFAULT NULL            COMMENT '关联需求ID',
  `remark`          VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tutor` (`user_id`, `tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员备选老师';

-- ===========================================================
-- 七、通用功能表
-- ===========================================================

-- 20. favorite 收藏
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '用户ID',
  `target_type`   TINYINT       DEFAULT NULL            COMMENT '1教员 2需求',
  `target_id`     BIGINT        NOT NULL                COMMENT '目标ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏';

-- 21. footprint 浏览足迹
DROP TABLE IF EXISTS `footprint`;
CREATE TABLE `footprint` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '用户ID',
  `target_type`   TINYINT       DEFAULT NULL            COMMENT '目标类型',
  `target_id`     BIGINT        NOT NULL                COMMENT '目标ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览足迹';

-- 22. feedback 意见反馈
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '用户ID',
  `content`       TEXT                                  COMMENT '反馈内容',
  `contact`       VARCHAR(100)  DEFAULT NULL            COMMENT '联系方式',
  `images`        VARCHAR(1000) DEFAULT NULL            COMMENT 'JSON图片URL数组',
  `fb_status`     TINYINT       NOT NULL DEFAULT 0      COMMENT '0待处理 1已回复 2已关闭',
  `reply`         TEXT                                  COMMENT '回复内容',
  `reply_time`    DATETIME      DEFAULT NULL            COMMENT '回复时间',
  `replier_id`    BIGINT        DEFAULT NULL            COMMENT '回复人ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈';

-- 23. sms_log 短信记录
DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `mobile`        VARCHAR(20)   NOT NULL                COMMENT '手机号',
  `sms_type`      TINYINT       DEFAULT NULL            COMMENT '1验证码 2通知',
  `content`       VARCHAR(500)  DEFAULT NULL            COMMENT '短信内容',
  `send_status`   TINYINT       NOT NULL DEFAULT 0      COMMENT '0成功 1失败',
  `platform`      TINYINT       DEFAULT NULL            COMMENT '平台',
  `ip`            VARCHAR(50)   DEFAULT NULL            COMMENT 'IP地址',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信记录';


-- ===========================================================
-- 八、内容管理
-- ===========================================================

-- 26. website_banner 轮播图/广告位
DROP TABLE IF EXISTS `website_banner`;
CREATE TABLE `website_banner` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_id`       BIGINT        DEFAULT NULL            COMMENT '城市ID(NULL=全局)',
  `banner_title`  VARCHAR(100)  DEFAULT NULL            COMMENT '标题',
  `banner_img`    VARCHAR(500)  NOT NULL DEFAULT ''     COMMENT '图片(object key)',
  `banner_link`   VARCHAR(500)  DEFAULT NULL            COMMENT '跳转链接',
  `position`      VARCHAR(50)   NOT NULL DEFAULT 'home' COMMENT '展示位置: home/jy/xy',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_city_position` (`city_id`, `position`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图/广告位';

-- 27. homepage_config 首页配置
DROP TABLE IF EXISTS `homepage_config`;
CREATE TABLE `homepage_config` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_id`       BIGINT        NOT NULL                COMMENT '城市ID',
  `config_key`    VARCHAR(50)   NOT NULL                COMMENT '配置键: hotline/intro/seo_title/seo_desc/seo_keywords',
  `config_value`  TEXT                                  COMMENT '配置值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_city_key` (`city_id`, `config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页配置(按城市)';

-- 28. price_reference 价格参考表
DROP TABLE IF EXISTS `price_reference`;
CREATE TABLE `price_reference` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '1启用 0禁用',
  `city_id`       BIGINT        NOT NULL                COMMENT '城市ID',
  `tutor_type`    TINYINT       NOT NULL                COMMENT '教员类型',
  `grade_level`   TINYINT       NOT NULL                COMMENT '1小学 2初中 3高中 4大学',
  `price_min`     DECIMAL(10,2) NOT NULL DEFAULT 0      COMMENT '参考最低价',
  `price_max`     DECIMAL(10,2) NOT NULL DEFAULT 0      COMMENT '参考最高价',
  `remark`        VARCHAR(200)  DEFAULT NULL            COMMENT '备注',
  `sort`          INT           NOT NULL DEFAULT 0      COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_city_id` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='价格参考';

-- ===========================================================
-- 九、ALTER: 给 users 表增加 user_type 字段（幂等）
-- ===========================================================
-- 注意: 如果字段已存在则跳过
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='roncoo_education' AND TABLE_NAME='users' AND COLUMN_NAME='user_type');
SET @sql = IF(@col_exists = 0, 'ALTER TABLE `users` ADD COLUMN `user_type` tinyint NOT NULL DEFAULT 0 COMMENT ''用户类型 0未设置 1教员 2学员'' AFTER `register_source`', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;
