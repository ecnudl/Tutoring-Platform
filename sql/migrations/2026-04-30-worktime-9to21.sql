-- 修正 sys_config 里 9:00-20:00 / 9:00-19:00 / 9:00-18:00 → 9:00-21:00
UPDATE sys_config SET config_value = REPLACE(config_value, '9:00-20:00', '9:00-21:00')
  WHERE config_value LIKE '%9:00-20:00%';
UPDATE sys_config SET config_value = REPLACE(config_value, '9:00-19:00', '9:00-21:00')
  WHERE config_value LIKE '%9:00-19:00%';
UPDATE sys_config SET config_value = REPLACE(config_value, '9:00-18:00', '9:00-21:00')
  WHERE config_value LIKE '%9:00-18:00%';
UPDATE sys_config SET config_value = REPLACE(config_value, '9:00 - 20:00', '9:00 - 21:00')
  WHERE config_value LIKE '%9:00 - 20:00%';
UPDATE sys_config SET config_value = REPLACE(config_value, '9:00 - 19:00', '9:00 - 21:00')
  WHERE config_value LIKE '%9:00 - 19:00%';
-- 检查
SELECT config_key, config_value FROM sys_config
WHERE config_value LIKE '%9:00%' OR config_key LIKE '%hours%' OR config_key LIKE '%Time%';
