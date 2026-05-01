-- 教员申请家长需求时填写的联系电话 (admin 撮合时直接联系教员)
ALTER TABLE tutor_application ADD COLUMN mobile VARCHAR(20) NULL COMMENT '申请人联系电话' AFTER apply_message;
SELECT COUNT(*) AS application_rows FROM tutor_application;
