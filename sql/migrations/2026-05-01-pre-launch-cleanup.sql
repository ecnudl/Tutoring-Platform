-- 591 上线前数据清理
-- 保留: admin 账号 (mobile=13795420591, userId=2049282375569047553) + 该账号的 student_profile / users_account
-- 不动: 字典 (dict_*) / 配置 (sys_config) / 公告 / banner / article / faq / homepage_config / price_reference / region / lecturer 等种子&CMS
--
-- 执行前必读:
-- 1. 先 mysqldump 备份: mysqldump -uroot -p<pw> roncoo_education > ~/backups/pre-cleanup-$(date +%Y%m%d-%H%M).sql
-- 2. 在事务里跑, 异常即回滚
-- 3. 跑完用最后的 SELECT 列表验证只剩 1 个 users + 0 业务行

USE roncoo_education;

START TRANSACTION;

SET @ADMIN_USER_ID = 2049282375569047553;

-- ========== 1. 业务表 (无须保留任何记录) ==========
DELETE FROM tutor_application;
DELETE FROM tutor_reservation;
DELETE FROM tutor_requirement;
DELETE FROM tutor_requirement_audit;
DELETE FROM tutor_shortlist;
DELETE FROM tutor_audit_record;
DELETE FROM tutor_certification;
DELETE FROM tutor_subject;
DELETE FROM tutor_teaching_area;
DELETE FROM feedback;
DELETE FROM msg_user;
DELETE FROM msg;

-- ========== 2. 用户行为 (保留 admin 的) ==========
DELETE FROM favorite       WHERE user_id != @ADMIN_USER_ID;
DELETE FROM footprint      WHERE user_id != @ADMIN_USER_ID;
DELETE FROM users_log      WHERE user_id != @ADMIN_USER_ID;

-- ========== 3. 教员资料 (admin 不是教员, 全清) ==========
DELETE FROM tutor_profile;

-- ========== 4. 学员资料 (保留 admin 自己的 1 条) ==========
DELETE FROM student_profile WHERE user_id != @ADMIN_USER_ID;

-- ========== 5. 订单 (清空, 防 schema drift 残留) ==========
DELETE FROM order_pay;
DELETE FROM order_info;

-- ========== 6. SMS 日志 ==========
DELETE FROM sms_log;

-- ========== 7. 账户 / 余额 (保留 admin 的) ==========
DELETE FROM users_account_consume WHERE user_id != @ADMIN_USER_ID;
DELETE FROM users_account         WHERE user_id != @ADMIN_USER_ID;

-- ========== 8. 用户主表 (最后, 保留 admin) ==========
DELETE FROM users WHERE id != @ADMIN_USER_ID;

-- ========== 验证 ==========
SELECT 'users (应=1)'             tbl, COUNT(*) cnt FROM users
UNION ALL SELECT 'users_account (应=1)',    COUNT(*) FROM users_account
UNION ALL SELECT 'users_log (应=admin历史)', COUNT(*) FROM users_log
UNION ALL SELECT 'student_profile (应=1)',  COUNT(*) FROM student_profile
UNION ALL SELECT 'tutor_profile (应=0)',    COUNT(*) FROM tutor_profile
UNION ALL SELECT 'tutor_certification (=0)',COUNT(*) FROM tutor_certification
UNION ALL SELECT 'tutor_subject (=0)',      COUNT(*) FROM tutor_subject
UNION ALL SELECT 'tutor_teaching_area (=0)',COUNT(*) FROM tutor_teaching_area
UNION ALL SELECT 'tutor_audit_record (=0)', COUNT(*) FROM tutor_audit_record
UNION ALL SELECT 'tutor_requirement (=0)',  COUNT(*) FROM tutor_requirement
UNION ALL SELECT 'tutor_requirement_audit (=0)', COUNT(*) FROM tutor_requirement_audit
UNION ALL SELECT 'tutor_reservation (=0)',  COUNT(*) FROM tutor_reservation
UNION ALL SELECT 'tutor_application (=0)',  COUNT(*) FROM tutor_application
UNION ALL SELECT 'tutor_shortlist (=0)',    COUNT(*) FROM tutor_shortlist
UNION ALL SELECT 'feedback (=0)',           COUNT(*) FROM feedback
UNION ALL SELECT 'msg (=0)',                COUNT(*) FROM msg
UNION ALL SELECT 'msg_user (=0)',           COUNT(*) FROM msg_user
UNION ALL SELECT 'favorite (admin=ok)',     COUNT(*) FROM favorite
UNION ALL SELECT 'footprint (admin=ok)',    COUNT(*) FROM footprint
UNION ALL SELECT 'order_info (=0)',         COUNT(*) FROM order_info
UNION ALL SELECT 'order_pay (=0)',          COUNT(*) FROM order_pay
UNION ALL SELECT 'sms_log (=0)',            COUNT(*) FROM sms_log;

-- 提交事务 (mysql 默认 abort-on-error: 中途任一 DELETE 失败 → 连接关闭时自动 ROLLBACK)
COMMIT;
