#!/usr/bin/env bash
# 生成生产环境强随机密钥，写入 .env.production
# 用法：./gen-secrets.sh [输出文件名，默认 .env.production]
set -euo pipefail

OUT="${1:-.env.production}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
OUT_PATH="$REPO_ROOT/$OUT"

if [[ -f "$OUT_PATH" ]]; then
    echo "⚠ $OUT_PATH 已存在，不覆盖。请先备份/删除后再运行。" >&2
    exit 1
fi

# 随机字符串生成器（32 字节，base64 编码，去掉特殊字符）
gen() {
  openssl rand -base64 32 | tr -d '\n+/=' | cut -c1-40
}

MYSQL_PW=$(gen)
REDIS_PW=$(gen)
NACOS_PW=$(gen)
JWT_SECRET=$(gen)
JASYPT_PW=$(gen)
MINIO_AK="mc-$(gen | cut -c1-12)"
MINIO_SK=$(gen)

cat > "$OUT_PATH" <<EOF
# 生产环境密钥（由 scripts/gen-secrets.sh 生成于 $(date -u +%FT%TZ)）
# ⚠ 本文件不要提交到 git，加入 .gitignore

# ---- MySQL ----
MYSQL_HOST=127.0.0.1
MYSQL_PORT=3306
MYSQL_DATABASE=roncoo_education
MYSQL_USER=root
MYSQL_PASSWORD=${MYSQL_PW}

# ---- Redis ----
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=${REDIS_PW}

# ---- Nacos ----
NACOS_HOST=127.0.0.1
NACOS_PORT=8848
NACOS_USERNAME=nacos
NACOS_PASSWORD=${NACOS_PW}
NACOS_NAMESPACE=prod

# ---- MinIO ----
MINIO_ENDPOINT=http://127.0.0.1:9000
MINIO_ACCESS_KEY=${MINIO_AK}
MINIO_SECRET_KEY=${MINIO_SK}
MINIO_BUCKET=education
MINIO_DOMAIN=https://static.example.com/education/

# ---- 应用密钥 ----
JWT_TOKEN_SECRET=${JWT_SECRET}
JASYPT_PASSWORD=${JASYPT_PW}

# ---- 域名（按实际情况修改） ----
BASE_DOMAINS=example.com
CITY_PINYINS=beijing,guangzhou,nanjing,suzhou,hangzhou,hefei,fuzhou,nanchang,jinan,tianjin,wuhan,zhengzhou,chongqing,xian,chengdu,changsha

# ---- XXL-JOB ----
XXL_JOB_ADMIN_ADDRESSES=http://127.0.0.1:8088/xxl-job-admin
XXL_JOB_ACCESS_TOKEN=$(gen)
XXL_JOB_EXECUTOR_PORT=9901

# ---- 前端 ----
WEB_PORT=3000
WEB_API_BASE=/
ADMIN_PORT=3100
ADMIN_API_BASE=/

# ---- 后端服务端口 ----
GATEWAY_PORT=7700
SERVICE_SYSTEM_PORT=7710
SERVICE_USER_PORT=7720
SERVICE_COURSE_PORT=7730

# ---- 平台 ----
PLATFORM_NAME=请按实际填写
PLATFORM_HOTLINE=400-000-0000
PLATFORM_DEFAULT_CITY=shanghai
WEB_DOMAIN=https://example.com
ADMIN_DOMAIN=https://example.com/admin
STORAGE_PUBLIC_URL_PREFIX=https://static.example.com/education
EOF

chmod 600 "$OUT_PATH"
echo "✓ 生产密钥已写入 $OUT_PATH（权限 600）"
echo "  下一步："
echo "  1. 检查 BASE_DOMAINS / WEB_DOMAIN / MINIO_DOMAIN 等域名类配置"
echo "  2. cp $OUT_PATH .env（在服务器上部署时）"
echo "  3. 把同一份 Nacos 密码/DB 密码/MinIO 凭证配置到对应基础设施"
