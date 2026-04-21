#!/usr/bin/env bash
# 渲染 site.conf.tpl → nginx 站点配置文件
# 用法：
#   BASE_DOMAIN_1=myapp.com BASE_DOMAIN_2=myapp.cn \
#     ADMIN_DIST_PATH=/srv/admin/dist \
#     ./render-site-conf.sh > /etc/nginx/sites-available/myapp
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TPL="$SCRIPT_DIR/site.conf.tpl"

: "${BASE_DOMAIN_1:?请指定 BASE_DOMAIN_1}"
: "${BASE_DOMAIN_2:=}"
: "${SSL_CERT_DIR:=/etc/nginx/ssl}"
: "${ADMIN_DIST_PATH:=/home/ubuntu/roncoo-education-admin/dist}"
: "${GATEWAY_PORT:=7700}"
: "${NUXT_PORT:=3000}"
: "${WECOM_PORT:=18789}"

export BASE_DOMAIN_1 BASE_DOMAIN_2 SSL_CERT_DIR ADMIN_DIST_PATH GATEWAY_PORT NUXT_PORT WECOM_PORT

# 只替换上面列出的变量，保留 $host $remote_addr 等 nginx 内置变量
envsubst '$BASE_DOMAIN_1 $BASE_DOMAIN_2 $SSL_CERT_DIR $ADMIN_DIST_PATH $GATEWAY_PORT $NUXT_PORT $WECOM_PORT' < "$TPL"
