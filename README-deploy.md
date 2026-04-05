# 家教平台 - 服务器迁移部署指南

## 一、迁移概述

本文档说明如何将项目从开发服务器迁移到正式使用服务器。
迁移核心原则：**只改配置，不改代码**。

## 二、迁移前检查清单

- [ ] 目标服务器安装 Docker + Docker Compose
- [ ] 目标服务器安装 JDK 17+
- [ ] 目标服务器安装 Node.js 18+
- [ ] 目标服务器安装 Maven 3.8+（或从开发机复制构建产物）
- [ ] 准备正式域名和 SSL 证书
- [ ] 准备正式数据库密码
- [ ] 准备正式 JWT 密钥
- [ ] 准备短信服务账号（如不需要，保持 MockSms）
- [ ] 准备对象存储账号（如不需要，保持 MinIO）

## 三、迁移步骤

### 第 1 步：复制仓库到目标服务器

```bash
# 方式1: git clone
git clone <repo-url> /opt/tutor-platform
cd /opt/tutor-platform/roncoo-education

# 方式2: rsync/scp
rsync -avz /home/ubuntu/ user@target:/opt/tutor-platform/
```

### 第 2 步：修改 .env

```bash
cp .env.example .env
vi .env
```

需要修改的变量：

| 变量 | 开发环境 | 正式环境(示例) | 说明 |
|------|----------|---------------|------|
| MYSQL_HOST | 127.0.0.1 | db.internal | MySQL 内网地址 |
| MYSQL_PASSWORD | RonCoo.123 | **强密码** | 必须修改 |
| REDIS_HOST | 127.0.0.1 | redis.internal | Redis 内网地址 |
| REDIS_PASSWORD | (空) | **设置密码** | 生产必须有密码 |
| NACOS_HOST | 127.0.0.1 | nacos.internal | Nacos 地址 |
| NACOS_NAMESPACE | dev | prod | 切换命名空间 |
| MINIO_ENDPOINT | http://127.0.0.1:9000 | https://oss.example.com | 对象存储地址 |
| MINIO_ACCESS_KEY | minioadmin | **正式AK** | 必须修改 |
| MINIO_SECRET_KEY | minioadmin | **正式SK** | 必须修改 |
| MINIO_DOMAIN | http://127.0.0.1:9000/education/ | https://cdn.example.com/education/ | 公网访问域名 |
| JWT_TOKEN_SECRET | tutor-platform-dev-secret-key | **随机长字符串** | 必须修改 |
| JASYPT_PASSWORD | roncoo_education | **随机密钥** | 必须修改 |
| WEB_API_BASE | http://127.0.0.1:7700 | https://api.example.com | 正式API域名 |
| ADMIN_API_BASE | http://127.0.0.1:7700 | https://api.example.com | 正式API域名 |
| WEB_DOMAIN | http://localhost:3000 | https://www.example.com | 正式前台域名 |
| ADMIN_DOMAIN | http://localhost:3100 | https://admin.example.com | 正式后台域名 |
| STORAGE_PUBLIC_URL_PREFIX | http://localhost:9000/education | https://cdn.example.com/education | 静态资源前缀 |
| PLATFORM_NAME | 好家教在线 | (保持或修改) | 平台名称 |
| PLATFORM_HOTLINE | 400-000-0000 | (正式号码) | 服务热线 |

### 第 3 步：启动基础设施

方式A: 使用 Docker Compose（推荐小规模部署）

```bash
set -a; source .env; set +a
docker compose -f docker-compose.dev.yml up -d
```

方式B: 使用已有中间件（推荐生产环境）

如果已有独立的 MySQL、Redis、Nacos 实例，只需在 .env 中指向它们的地址，不需要运行 docker-compose。

### 第 4 步：初始化数据库

```bash
set -a; source .env; set +a

# 连接到正式数据库执行
mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${MYSQL_USER} -p"${MYSQL_PASSWORD}" < sql/roncoo-education-base.sql
mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${MYSQL_USER} -p"${MYSQL_PASSWORD}" ${MYSQL_DATABASE} < sql/tutor-platform-init.sql
mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${MYSQL_USER} -p"${MYSQL_PASSWORD}" ${MYSQL_DATABASE} < sql/tutor-platform-seed.sql
mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${MYSQL_USER} -p"${MYSQL_PASSWORD}" ${MYSQL_DATABASE} < sql/tutor-platform-seed-shanghai.sql
```

### 第 5 步：推送 Nacos 配置

```bash
set -a; source .env; set +a
./scripts/nacos-config-init.sh
```

脚本会读取 .env 中的正式配置并推送到 Nacos。

### 第 6 步：构建并启动后端

```bash
cd /opt/tutor-platform/roncoo-education
mvn clean package -DskipTests

# 启动
nohup java -jar roncoo-education-gateway/target/gateway.jar > logs/gateway.log 2>&1 &
nohup java -jar roncoo-education-service/roncoo-education-service-system/target/system.jar > logs/system.log 2>&1 &
nohup java -jar roncoo-education-service/roncoo-education-service-user/target/user.jar > logs/user.log 2>&1 &
```

### 第 7 步：构建前端

```bash
# 门户
cd /opt/tutor-platform/roncoo-education-web
echo "API_BASE=https://api.example.com" > .env
npm install && npm run build

# 后台
cd /opt/tutor-platform/roncoo-education-admin
echo "VITE_API_BASE=https://api.example.com" > .env
npm install && npm run build
```

### 第 8 步：配置 Nginx 反向代理

```nginx
# API 网关
server {
    listen 443 ssl;
    server_name api.example.com;
    ssl_certificate     /etc/nginx/ssl/api.crt;
    ssl_certificate_key /etc/nginx/ssl/api.key;

    location / {
        proxy_pass http://127.0.0.1:7700;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

# 前台门户
server {
    listen 443 ssl;
    server_name www.example.com;
    ssl_certificate     /etc/nginx/ssl/www.crt;
    ssl_certificate_key /etc/nginx/ssl/www.key;

    location / {
        proxy_pass http://127.0.0.1:3000;
        # 或者 SSG 模式下直接指向 dist 目录
        # root /opt/tutor-platform/roncoo-education-web/.output/public;
        # try_files $uri $uri/ /index.html;
    }
}

# 管理后台
server {
    listen 443 ssl;
    server_name admin.example.com;
    ssl_certificate     /etc/nginx/ssl/admin.crt;
    ssl_certificate_key /etc/nginx/ssl/admin.key;

    root /opt/tutor-platform/roncoo-education-admin/dist;

    location /admin/ {
        try_files $uri $uri/ /admin/index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:7700/;
    }
}
```

## 四、切换第三方服务

### 短信服务
1. 修改 `sys_config` 表中 `smsPlatform` 值
2. 在 Nacos 配置中添加短信 AK/SK
3. 无需改代码

### 对象存储（切换到阿里云 OSS）
1. 修改 `sys_config` 表中 `uploadPlatform` 值
2. 在 .env 中配置 OSS 的 endpoint/AK/SK/bucket
3. 无需改代码

### 邮件服务
1. 实现 `EmailFace` 接口（如需要）
2. 在 Nacos 配置中添加 SMTP 配置

## 五、扩展到新城市

无需改代码：

1. 后台管理 → 城市管理 → 新增城市
2. 后台管理 → 区域管理 → 新增该城市的区域
3. 后台管理 → 高校管理 → 新增该城市的高校
4. 后台管理 → 首页配置 → 为该城市配置 SEO/热线等
5. 后台管理 → 价格参考 → 为该城市配置参考价格
6. 前端城市选择器会自动出现新城市

或者使用 SQL 批量导入：
```bash
mysql ... < sql/tutor-platform-seed-suzhou.sql
```

## 六、安全加固清单

- [ ] 修改所有默认密码
- [ ] 启用 Nacos 鉴权（NACOS_AUTH_ENABLE=true）
- [ ] Redis 设置密码
- [ ] MinIO 修改默认凭证
- [ ] JWT 密钥使用 32+ 字符随机字符串
- [ ] 前端构建时移除 source map
- [ ] Nginx 配置 HTTPS
- [ ] 数据库禁止外网直连
- [ ] 定期备份数据库
