-- 教员审核分流: 加 last_published_at 区分"首次注册审核"vs"修改后重审"
ALTER TABLE tutor_profile ADD COLUMN last_published_at DATETIME NULL COMMENT '首次审核通过时间, 区分初次审核 vs 修改重审' AFTER last_login_time;
-- 已 PUBLISHED 的存量数据: last_published_at = gmt_modified (近似)
UPDATE tutor_profile SET last_published_at = gmt_modified WHERE audit_status IN (2, 4) AND last_published_at IS NULL;
SELECT id, display_no, audit_status, last_published_at FROM tutor_profile;
