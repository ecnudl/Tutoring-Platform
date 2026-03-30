# 家教在线平台 - 本地开发环境指南

## 一、架构概览

```
                    ┌──────────┐
                    │  Nacos   │  :8848  配置中心 + 服务注册
                    └────┬─────┘
                         │
     ┌───────────────────┼───────────────────┐
     │                   │                   │
┌────┴─────┐     ┌───────┴──────┐    ┌───────┴──────┐
│ Gateway  │     │service-system│    │ service-user │
│  :7700   │     │    :7710     │    │    :7720     │
└────┬─────┘     └──────────────┘    └──────────────┘
     │                   │                   │
     │              ┌────┴────┐         ┌────┴────┐
     │              │  MySQL  │         │  Redis  │
     │              │  :3306  │         │  :6379  │
     │              └─────────┘         └─────────┘
     │
┌────┴──────────────────────────────────┐
│  前端                                  │
│  门户 (Nuxt3)  :3000                   │
│  管理后台 (Vue3+Vite)  :3100           │
└───────────────────────────────────────┘
```

## 二、前置依赖

| 软件 | 版本要求 | 用途 |
|------|----------|------|
| Docker + Docker Compose | 20+ / V2 | 运行 MySQL、Redis、Nacos、MinIO |
| JDK | 17+ | 编译运行后端 |
| Maven | 3.8+ | 构建后端 |
| Node.js | 18+ | 构建前端 |
| curl | * | Nacos 配置初始化 |

## 三、启动顺序

### 第 1 步：准备环境变量

```bash
cd /home/ubuntu/roncoo-education
cp .env.example .env
# 按需编辑 .env，默认值可直接使用
```

### 第 2 步：启动基础设施（Docker）

```bash
# 加载环境变量
set -a; source .env; set +a

# 启动 MySQL + Redis + Nacos + MinIO
docker compose -f docker-compose.dev.yml up -d

# 查看状态（等待全部 healthy）
docker compose -f docker-compose.dev.yml ps
```

等待所有服务 healthy（约 30-60 秒），尤其是 Nacos 启动较慢。

### 第 3 步：初始化数据库

docker-compose 会自动执行 `sql/` 目录下的 SQL 文件（按字母序）。手动执行方式：

```bash
# 如需手动执行
docker exec -i tutor-mysql mysql -uroot -p"${MYSQL_PASSWORD}" < sql/roncoo-education-base.sql
docker exec -i tutor-mysql mysql -uroot -p"${MYSQL_PASSWORD}" roncoo_education < sql/tutor-platform-init.sql
docker exec -i tutor-mysql mysql -uroot -p"${MYSQL_PASSWORD}" roncoo_education < sql/tutor-platform-seed.sql
```

### 第 4 步：初始化 Nacos 配置

```bash
set -a; source .env; set +a
./scripts/nacos-config-init.sh
```

验证: 打开 http://localhost:8848/nacos/ (nacos/nacos)，切换到 `dev` 命名空间，应看到 5 个配置文件。

### 第 5 步：初始化 MinIO

```bash
./scripts/minio-init.sh
```

或手动登录 http://localhost:9001 (minioadmin/minioadmin)，创建名为 `education` 的 bucket。

### 第 6 步：编译后端

```bash
cd /home/ubuntu/roncoo-education
mvn clean package -DskipTests
```

### 第 7 步：启动后端服务

**按顺序启动**（Nacos 必须先就绪）：

```bash
# 终端1: Gateway
java -jar roncoo-education-gateway/target/gateway.jar

# 终端2: service-system
java -jar roncoo-education-service/roncoo-education-service-system/target/system.jar

# 终端3: service-user
java -jar roncoo-education-service/roncoo-education-service-user/target/user.jar
```

或使用快捷脚本：
```bash
# 后台启动全部
nohup java -jar roncoo-education-gateway/target/gateway.jar > logs/gateway.log 2>&1 &
nohup java -jar roncoo-education-service/roncoo-education-service-system/target/system.jar > logs/system.log 2>&1 &
nohup java -jar roncoo-education-service/roncoo-education-service-user/target/user.jar > logs/user.log 2>&1 &
```

### 第 8 步：启动前端

```bash
# 终端4: 门户前端
cd /home/ubuntu/roncoo-education-web
npm install  # 首次
npm run dev

# 终端5: 管理后台
cd /home/ubuntu/roncoo-education-admin
npm install  # 首次
npm run dev
```

## 四、端口清单

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| Nacos | 8848 | 配置中心/服务注册 |
| Nacos gRPC | 9848, 9849 | Nacos 内部通信 |
| MinIO API | 9000 | 对象存储 |
| MinIO Console | 9001 | MinIO 管理界面 |
| Gateway | 7700 | API 网关（所有 API 入口） |
| service-system | 7710 | 系统服务 |
| service-user | 7720 | 用户服务 |
| service-course | 7730 | 课程服务（家教平台暂不需要） |
| 门户前端 | 3000 | Nuxt3 SSR |
| 管理后台 | 3100 | Vue3 + Vite |

## 五、验证方式

```bash
# 1. Nacos 控制台
curl http://localhost:8848/nacos/

# 2. Gateway 健康检查
curl http://localhost:7700/

# 3. 字典接口（公开API，不需要登录）
curl http://localhost:7700/user/api/dict/subject/list

# 4. Knife4j API 文档
# http://localhost:7700/doc.html
```

## 六、常见报错和排查

### 1. `Connection refused: 127.0.0.1:8848`
**原因:** Nacos 未启动或未就绪。
**解决:**
```bash
docker compose -f docker-compose.dev.yml ps
docker compose -f docker-compose.dev.yml logs nacos
```

### 2. `No DataId: application.properties`
**原因:** Nacos 中没有配置。
**解决:** 重新执行 `./scripts/nacos-config-init.sh`

### 3. `Access denied for user 'root'@'...'`
**原因:** MySQL 密码不匹配。
**解决:** 检查 `.env` 中的 `MYSQL_PASSWORD`，确认与 Nacos 中 `application.properties` 的数据源密码一致。

### 4. `Cannot resolve host: service-user`
**原因:** 微服务未注册到 Nacos。
**解决:** 确认 service-user 已启动，检查 Nacos 控制台 → 服务列表。

### 5. `Table 'xxx' doesn't exist`
**原因:** 数据库表未创建。
**解决:**
```bash
docker exec -i tutor-mysql mysql -uroot -pRonCoo.123 roncoo_education < sql/roncoo-education-base.sql
docker exec -i tutor-mysql mysql -uroot -pRonCoo.123 roncoo_education < sql/tutor-platform-init.sql
```

### 6. `XxlJobSpringExecutor start fail`
**原因:** XXL-JOB admin 未部署，可忽略。
**说明:** XXL-JOB 为可选组件，报错不影响主流程，仅定时任务不可用。

### 7. 前端页面显示但无数据
**原因:** 后端 Gateway 未启动，或前端 API 地址配置错误。
**解决:**
```bash
# 确认 Gateway 可达
curl http://localhost:7700/user/api/dict/subject/list
# 检查前端 .env 中 API_BASE 配置
```

### 8. CORS 跨域报错
**原因:** Gateway 配置缺少 CORS。
**解决:** 检查 Nacos 中 `gateway.properties` 的 CORS 配置。

## 七、清理环境

```bash
# 停止所有 Docker 容器
docker compose -f docker-compose.dev.yml down

# 停止并删除数据卷（会清空数据库！）
docker compose -f docker-compose.dev.yml down -v
```

## 八、环境变量说明

所有连接信息从 `.env` 文件读取（不要直接修改 bootstrap.properties）。

| 变量 | 默认值 | 说明 |
|------|--------|------|
| MYSQL_HOST | 127.0.0.1 | MySQL 地址 |
| MYSQL_PORT | 3306 | MySQL 端口 |
| MYSQL_PASSWORD | RonCoo.123 | MySQL root 密码 |
| REDIS_HOST | 127.0.0.1 | Redis 地址 |
| NACOS_HOST | 127.0.0.1 | Nacos 地址 |
| NACOS_NAMESPACE | dev | Nacos 命名空间 |
| MINIO_ENDPOINT | http://127.0.0.1:9000 | MinIO 地址 |
| JWT_TOKEN_SECRET | tutor-platform-dev-secret-key | JWT 密钥 |

完整变量见 `.env.example`。
