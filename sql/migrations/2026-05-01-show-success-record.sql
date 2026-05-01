-- 教员主页是否展示成功记录开关
ALTER TABLE tutor_profile ADD COLUMN show_success_record TINYINT NOT NULL DEFAULT 1 COMMENT '主页是否展示成功记录(1=展示, 0=隐藏)';
SELECT COUNT(*) AS tutor_profile_rows FROM tutor_profile;
