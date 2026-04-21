#!/usr/bin/env bash
# 新服务器一键 bootstrap 脚本
# 使用前：clone 仓库，cp .env.example .env（或运行 gen-secrets.sh 生成），按实际情况编辑。
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$REPO_ROOT"

echo "==> 1. 校验环境变量"
[[ -f .env ]] || { echo "缺少 .env，请先 cp .env.example .env 并填写"; exit 1; }
set -a; source .env; set +a

echo "==> 2. 启动基础设施（MySQL / Redis / Nacos / MinIO）"
docker compose -f docker-compose.dev.yml up -d

echo "==> 3. 等待 MySQL 启动 "
until docker exec tutor-mysql mysqladmin ping -uroot -p"$MYSQL_PASSWORD" --silent 2>/dev/null; do
    echo -n "."; sleep 2
done
echo

echo "==> 4. 初始化数据库 schema（如果 sql/ 下有 init SQL）"
for f in sql/*.sql; do
    [[ -f "$f" ]] || continue
    # 跳过 seed 文件（单独 step）
    case "$f" in *seed*) continue ;; esac
    echo "  应用 $f"
    docker exec -i tutor-mysql mysql --default-character-set=utf8mb4 -uroot -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < "$f" || true
done

echo "==> 5. 写入 sys_config 种子"
if [[ -f sql/sys_config_seed.sql ]]; then
    docker exec -i tutor-mysql mysql --default-character-set=utf8mb4 -uroot -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < sql/sys_config_seed.sql
fi

echo "==> 6. 写入 Nacos 配置"
if [[ -x scripts/nacos-config-init.sh ]]; then
    scripts/nacos-config-init.sh
fi

echo "==> 7. 初始化 MinIO bucket"
if [[ -x scripts/minio-init.sh ]]; then
    scripts/minio-init.sh
fi

echo "==> 8. 渲染 nginx 配置（提示）"
cat <<EOF
  下一步手动执行：
  1) 把 SSL 证书放到 /etc/nginx/ssl/
  2) BASE_DOMAIN_1=<主域名> BASE_DOMAIN_2=<次域名> \
       scripts/nginx/render-site-conf.sh > /etc/nginx/sites-available/myapp
  3) sudo ln -sf /etc/nginx/sites-available/myapp /etc/nginx/sites-enabled/myapp
  4) sudo nginx -t && sudo systemctl reload nginx
  5) sudo cp scripts/logrotate/roncoo-education.conf /etc/logrotate.d/

  最后：mvn package 构建后端，npm run build 构建前端，启动对应 jar / node
EOF

echo "==> bootstrap 完成"
