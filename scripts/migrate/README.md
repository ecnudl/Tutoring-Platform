# 迁移到新服务器清单

目标：新服务器 30 分钟内复活整个家教平台。

## 先决条件

- Ubuntu 22.04+（或 Debian / Rocky，有 docker + nginx）
- docker, docker compose, openjdk-17, node 18+, maven
- 至少 4 GB RAM, 20 GB 磁盘

## 步骤

```bash
# 1) clone 代码
git clone <repo-url> ~/roncoo-education
cd ~/roncoo-education

# 2) 生成生产密钥（或手动复制 .env.example → .env 后编辑）
./scripts/gen-secrets.sh
cp .env.production .env

# 3) 一键 bootstrap（启 docker + 建表 + 种子 + Nacos + MinIO）
./scripts/migrate/bootstrap.sh

# 4) 渲染 nginx 并启用
sudo mkdir -p /etc/nginx/ssl
sudo cp /your/cert/path/*.pem /etc/nginx/ssl/
BASE_DOMAIN_1=myapp.com BASE_DOMAIN_2=myapp.cn \
    scripts/nginx/render-site-conf.sh | sudo tee /etc/nginx/sites-available/myapp
sudo ln -sf /etc/nginx/sites-available/myapp /etc/nginx/sites-enabled/myapp
sudo nginx -t && sudo systemctl reload nginx

# 5) 安装 logrotate
sudo cp scripts/logrotate/roncoo-education.conf /etc/logrotate.d/

# 6) 构建 & 启动后端
mvn -DskipTests package
mkdir -p ~/logs
for svc in system user course; do
    nohup java -jar roncoo-education-gateway/target/gateway.jar > ~/logs/gateway.log 2>&1 &
    nohup java -jar roncoo-education-service/roncoo-education-service-${svc}/target/${svc}.jar > ~/logs/${svc}.log 2>&1 &
done

# 7) 构建 & 启动前端
git clone <web-repo> ~/roncoo-education-web
cd ~/roncoo-education-web
cp ~/roncoo-education/.env .env  # or ln -s
npm ci && npm run build
nohup node .output/server/index.mjs > ~/logs/nuxt-web.log 2>&1 &

# 8) 构建 admin
git clone <admin-repo> ~/roncoo-education-admin
cd ~/roncoo-education-admin
npm ci && npm run build
# dist 已由 nginx alias 自动服务
```

## 验证

```bash
curl -fsS https://${BASE_DOMAIN_1}/healthz  # "ok"
curl -fsS https://${BASE_DOMAIN_1}/user/api/sms/ping  # 后端可达
```

## 回退 / 重放

- sys_config 配置都在 `sql/sys_config_seed.sql`，重放幂等（ON DUPLICATE KEY UPDATE）
- Nacos 配置都在 `scripts/nacos-config-init.sh`
- 迁移后想看差异：`./scripts/gen-sys-config-seed.sh | diff - sql/sys_config_seed.sql`
