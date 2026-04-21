#!/usr/bin/env bash
# 从 sys_config 表生成 ON DUPLICATE KEY UPDATE 形式的种子 SQL
# 用法（在能访问 docker 的主机上）：
#   ./gen-sys-config-seed.sh > sql/sys_config_seed.sql
set -euo pipefail

CONTAINER="${MYSQL_CONTAINER:-tutor-mysql}"
DB="${MYSQL_DATABASE:-roncoo_education}"
USER="${MYSQL_USER:-root}"
PASSWORD="${MYSQL_PASSWORD:-RonCoo.123}"

cat <<'EOF'
-- 站点运行时配置种子数据（sys_config）
-- 本文件由 scripts/gen-sys-config-seed.sh 导出。
-- 重放策略：ON DUPLICATE KEY UPDATE — 新服务器上可直接 mysql < 导入，已有 key 会更新 value。
-- ⚠ 字符集：导入前务必 SET NAMES utf8mb4（mysql 命令行传 --default-character-set=utf8mb4）
SET NAMES utf8mb4;

EOF

SQL_STMT="SELECT CONCAT('INSERT INTO sys_config (id, config_type, content_type, config_name, config_key, config_value, config_show, sort) VALUES (', id, ', ', config_type, ', ', content_type, ', ', QUOTE(config_name), ', ', QUOTE(config_key), ', ', QUOTE(config_value), ', ', IF(config_show, 1, 0), ', ', sort, ') ON DUPLICATE KEY UPDATE config_type=VALUES(config_type), content_type=VALUES(content_type), config_name=VALUES(config_name), config_value=VALUES(config_value), config_show=VALUES(config_show), sort=VALUES(sort);') FROM sys_config ORDER BY id"

docker exec "$CONTAINER" mysql --default-character-set=utf8mb4 -u"$USER" -p"$PASSWORD" -N -B "$DB" -e "$SQL_STMT" 2>/dev/null
