-- ============================================================
-- 家教平台 DDL 初始化脚本
-- tutor-platform-init.sql
-- 共17张新表 + 1条ALTER语句
-- ============================================================

USE `roncoo_education`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 1. tutor_profile  教员资料表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `tutor_profile`;
CREATE TABLE `tutor_profile` (
  `id`                  BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`          datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`           tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`             BIGINT        NOT NULL                COMMENT '关联users',
  `real_name`           VARCHAR(50)   DEFAULT NULL            COMMENT '真实姓名',
  `gender`              TINYINT       DEFAULT 0               COMMENT '性别 0未设置 1男 2女',
  `birth_date`          DATE          DEFAULT NULL            COMMENT '出生日期',
  `avatar`              VARCHAR(500)  DEFAULT NULL            COMMENT '头像URL',
  `mobile`              VARCHAR(20)   DEFAULT NULL            COMMENT '联系电话',
  `id_card`             VARCHAR(18)   DEFAULT NULL            COMMENT '身份证号',
  `tutor_type`          TINYINT       DEFAULT 1               COMMENT '1大学生2专职3在职教师4退休教师5其他',
  `degree`              TINYINT       DEFAULT 0               COMMENT '0未设置1高中2大专3本科4硕士5博士',
  `university`          VARCHAR(100)  DEFAULT NULL            COMMENT '毕业/在读院校',
  `major`               VARCHAR(100)  DEFAULT NULL            COMMENT '专业',
  `grade_year`          VARCHAR(20)   DEFAULT NULL            COMMENT '年级如大一大二',
  `teaching_experience` TEXT                                  COMMENT '教学经验',
  `self_introduction`   TEXT                                  COMMENT '自我介绍',
  `subjects`            VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON数组 科目ID',
  `grades`              VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON数组 年级ID',
  `tags`                VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON数组 标签ID',
  `province_id`         BIGINT        DEFAULT NULL            COMMENT '省份ID',
  `city_id`             BIGINT        DEFAULT NULL            COMMENT '城市ID',
  `district_id`         BIGINT        DEFAULT NULL            COMMENT '区县ID',
  `address`             VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `latitude`            DECIMAL(10,7) DEFAULT NULL            COMMENT '纬度',
  `longitude`           DECIMAL(10,7) DEFAULT NULL            COMMENT '经度',
  `price_min`           DECIMAL(10,2) DEFAULT 0.00            COMMENT '最低课时费',
  `price_max`           DECIMAL(10,2) DEFAULT 0.00            COMMENT '最高课时费',
  `free_trial`          TINYINT       DEFAULT 0               COMMENT '是否可试讲0否1是',
  `audit_status`        TINYINT       DEFAULT 0               COMMENT '0草稿1待审核2已通过3已拒绝4已发布',
  `audit_remark`        VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  `sort`                INT           DEFAULT 0               COMMENT '排序',
  `view_count`          INT           DEFAULT 0               COMMENT '浏览次数',
  `success_count`       INT           DEFAULT 0               COMMENT '成功次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员资料表';

-- -----------------------------------------------------------
-- 2. tutor_certification  教员资质证书
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `tutor_certification`;
CREATE TABLE `tutor_certification` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID',
  `cert_type`     TINYINT       DEFAULT NULL            COMMENT '1身份证2学生证3教师资格证4学历证5其他',
  `cert_name`     VARCHAR(100)  DEFAULT NULL            COMMENT '证书名称',
  `cert_url`      VARCHAR(500)  DEFAULT NULL            COMMENT '证书图片URL',
  `cert_no`       VARCHAR(100)  DEFAULT NULL            COMMENT '证书编号',
  `audit_status`  TINYINT       DEFAULT 0               COMMENT '0待审核1已通过2已拒绝',
  `audit_remark`  VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员资质证书';

-- -----------------------------------------------------------
-- 3. tutor_audit_record  教员审核记录
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `tutor_audit_record`;
CREATE TABLE `tutor_audit_record` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tutor_id`      BIGINT        NOT NULL                COMMENT '教员ID',
  `auditor_id`    BIGINT        DEFAULT NULL            COMMENT '审核人',
  `audit_action`  TINYINT       DEFAULT NULL            COMMENT '1通过2拒绝',
  `audit_remark`  VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员审核记录';

-- -----------------------------------------------------------
-- 4. student_profile  学员资料
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `student_profile`;
CREATE TABLE `student_profile` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '关联users',
  `real_name`     VARCHAR(50)   DEFAULT NULL            COMMENT '真实姓名',
  `gender`        TINYINT       DEFAULT 0               COMMENT '性别 0未设置 1男 2女',
  `grade`         VARCHAR(50)   DEFAULT NULL            COMMENT '当前年级',
  `school`        VARCHAR(100)  DEFAULT NULL            COMMENT '学校',
  `province_id`   BIGINT        DEFAULT NULL            COMMENT '省份ID',
  `city_id`       BIGINT        DEFAULT NULL            COMMENT '城市ID',
  `district_id`   BIGINT        DEFAULT NULL            COMMENT '区县ID',
  `address`       VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `parent_name`   VARCHAR(50)   DEFAULT NULL            COMMENT '家长姓名',
  `parent_mobile` VARCHAR(20)   DEFAULT NULL            COMMENT '家长电话',
  `remark`        TEXT                                  COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员资料';

-- -----------------------------------------------------------
-- 5. tutor_requirement  学员找老师需求
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `tutor_requirement`;
CREATE TABLE `tutor_requirement` (
  `id`                  BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`          datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`           tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`             BIGINT        NOT NULL                COMMENT '发布人user_id',
  `title`               VARCHAR(200)  DEFAULT NULL            COMMENT '需求标题',
  `subject_ids`         VARCHAR(500)  DEFAULT NULL            COMMENT 'JSON 科目ID数组',
  `grade_id`            BIGINT        DEFAULT NULL            COMMENT '年级ID',
  `province_id`         BIGINT        DEFAULT NULL            COMMENT '省份ID',
  `city_id`             BIGINT        DEFAULT NULL            COMMENT '城市ID',
  `district_id`         BIGINT        DEFAULT NULL            COMMENT '区县ID',
  `address`             VARCHAR(200)  DEFAULT NULL            COMMENT '详细地址',
  `tutor_gender`        TINYINT       DEFAULT 0               COMMENT '0不限1男2女',
  `tutor_type_pref`     VARCHAR(100)  DEFAULT NULL            COMMENT 'JSON偏好类型',
  `schedule`            TEXT                                  COMMENT '时间安排',
  `budget_min`          DECIMAL(10,2) DEFAULT NULL            COMMENT '预算下限',
  `budget_max`          DECIMAL(10,2) DEFAULT NULL            COMMENT '预算上限',
  `requirement_detail`  TEXT                                  COMMENT '需求详情',
  `contact_name`        VARCHAR(50)   DEFAULT NULL            COMMENT '联系人姓名',
  `contact_mobile`      VARCHAR(20)   DEFAULT NULL            COMMENT '联系人电话',
  `req_status`          TINYINT       DEFAULT 0               COMMENT '0草稿1待审核2已发布3已匹配4已完成5已关闭',
  `audit_remark`        VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  `view_count`          INT           DEFAULT 0               COMMENT '浏览次数',
  `application_count`   INT           DEFAULT 0               COMMENT '申请次数',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`req_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员找老师需求';

-- -----------------------------------------------------------
-- 6. tutor_requirement_audit  需求审核记录
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `tutor_requirement_audit`;
CREATE TABLE `tutor_requirement_audit` (
  `id`              BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`       tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `requirement_id`  BIGINT        NOT NULL                COMMENT '需求ID',
  `auditor_id`      BIGINT        DEFAULT NULL            COMMENT '审核人',
  `audit_action`    TINYINT       DEFAULT NULL            COMMENT '1通过2拒绝',
  `audit_remark`    VARCHAR(500)  DEFAULT NULL            COMMENT '审核备注',
  PRIMARY KEY (`id`),
  KEY `idx_requirement_id` (`requirement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求审核记录';

-- -----------------------------------------------------------
-- 7. tutor_application  教员申请需求单
-- -----------------------------------------------------------
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
  `app_status`      TINYINT       DEFAULT 0               COMMENT '0已申请1入围2录用3拒绝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_req_tutor` (`requirement_id`, `tutor_id`),
  KEY `idx_requirement_id` (`requirement_id`),
  KEY `idx_tutor_id` (`tutor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员申请需求单';

-- -----------------------------------------------------------
-- 8. tutor_reservation  学员预约教员
-- -----------------------------------------------------------
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
  `schedule_time`     VARCHAR(200)  DEFAULT NULL            COMMENT '预约时间',
  `address`           VARCHAR(200)  DEFAULT NULL            COMMENT '上课地址',
  `remark`            TEXT                                  COMMENT '备注',
  `res_status`        TINYINT       DEFAULT 0               COMMENT '0待确认1已确认2已完成3已取消',
  `cancel_reason`     VARCHAR(500)  DEFAULT NULL            COMMENT '取消原因',
  PRIMARY KEY (`id`),
  KEY `idx_student` (`student_user_id`),
  KEY `idx_tutor` (`tutor_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员预约教员';

-- -----------------------------------------------------------
-- 9. tutor_shortlist  学员备选老师
-- -----------------------------------------------------------
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

-- -----------------------------------------------------------
-- 10. favorite  收藏
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '用户ID',
  `target_type`   TINYINT       DEFAULT NULL            COMMENT '1教员2需求',
  `target_id`     BIGINT        NOT NULL                COMMENT '目标ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏';

-- -----------------------------------------------------------
-- 11. footprint  浏览足迹
-- -----------------------------------------------------------
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

-- -----------------------------------------------------------
-- 12. feedback  意见反馈
-- -----------------------------------------------------------
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
  `fb_status`     TINYINT       DEFAULT 0               COMMENT '0待处理1已回复2已关闭',
  `reply`         TEXT                                  COMMENT '回复内容',
  `reply_time`    DATETIME      DEFAULT NULL            COMMENT '回复时间',
  `replier_id`    BIGINT        DEFAULT NULL            COMMENT '回复人ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈';

-- -----------------------------------------------------------
-- 13. sms_log  短信记录
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `mobile`        VARCHAR(20)   NOT NULL                COMMENT '手机号',
  `sms_type`      TINYINT       DEFAULT NULL            COMMENT '1验证码2通知',
  `content`       VARCHAR(500)  DEFAULT NULL            COMMENT '短信内容',
  `send_status`   TINYINT       DEFAULT 0               COMMENT '0成功1失败',
  `platform`      TINYINT       DEFAULT NULL            COMMENT '平台',
  `ip`            VARCHAR(50)   DEFAULT NULL            COMMENT 'IP地址',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信记录';

-- -----------------------------------------------------------
-- 14. vip_membership  VIP会员
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `vip_membership`;
CREATE TABLE `vip_membership` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `user_id`       BIGINT        NOT NULL                COMMENT '用户ID',
  `vip_level`     TINYINT       DEFAULT 1               COMMENT 'VIP等级',
  `start_time`    DATETIME      DEFAULT NULL            COMMENT '开始时间',
  `end_time`      DATETIME      DEFAULT NULL            COMMENT '到期时间',
  `operator_id`   BIGINT        DEFAULT NULL            COMMENT '操作人',
  `remark`        VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP会员';

-- -----------------------------------------------------------
-- 15. dict_subject  科目字典
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `dict_subject`;
CREATE TABLE `dict_subject` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `subject_name`  VARCHAR(50)   NOT NULL                COMMENT '科目名称',
  `sort`          INT           DEFAULT 0               COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目字典';

-- -----------------------------------------------------------
-- 16. dict_grade  年级字典
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `dict_grade`;
CREATE TABLE `dict_grade` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `grade_name`    VARCHAR(50)   NOT NULL                COMMENT '年级名称',
  `grade_level`   TINYINT       DEFAULT NULL            COMMENT '1小学2初中3高中4大学',
  `sort`          INT           DEFAULT 0               COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级字典';

-- -----------------------------------------------------------
-- 17. dict_tutor_tag  教员标签字典
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `dict_tutor_tag`;
CREATE TABLE `dict_tutor_tag` (
  `id`            BIGINT        NOT NULL                COMMENT '主键',
  `gmt_create`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_id`     tinyint       NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
  `tag_name`      VARCHAR(50)   NOT NULL                COMMENT '标签名称',
  `sort`          INT           DEFAULT 0               COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员标签字典';

-- -----------------------------------------------------------
-- ALTER: 给 users 表增加 user_type 字段
-- -----------------------------------------------------------
ALTER TABLE `users` ADD COLUMN `user_type` tinyint NOT NULL DEFAULT 0 COMMENT '用户类型 0未设置 1教员 2学员' AFTER `register_source`;

SET FOREIGN_KEY_CHECKS = 1;
