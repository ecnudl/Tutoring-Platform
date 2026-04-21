-- 通用 SMS 配置 key（厂商中立），接入新短信服务商时只需在这些 key 下填写凭证
-- TencentSmsImpl 已支持：读取通用 key，缺失时回退到 tencentSms* 旧 key
SET NAMES utf8mb4;

INSERT INTO sys_config (id, config_type, content_type, config_name, config_key, config_value, config_show, sort) VALUES
(200, 5, 1, '短信签名（通用）', 'smsSignName', '', 1, 1),
(201, 5, 1, '短信 SecretId（通用）', 'smsSecretId', '', 1, 2),
(202, 5, 1, '短信 SecretKey（通用）', 'smsSecretKey', '', 1, 3),
(203, 5, 1, '短信 SdkAppId/AppKey（通用）', 'smsSdkAppId', '', 1, 4),
(204, 5, 1, '短信验证码模板 ID（通用）', 'smsAuthCode', '', 1, 5),
(205, 5, 1, '短信通知模板 ID（通用）', 'smsPurchaseCode', '', 1, 6),
(206, 5, 1, '短信区域（通用）', 'smsRegion', '', 1, 7)
ON DUPLICATE KEY UPDATE id=VALUES(id);
