# 家教在线平台改造说明

## 1. 项目概述

本项目基于 [roncoo-education](https://github.com/roncoo/roncoo-education) 开源在线教育平台进行改造，将其转变为**家教在线平台**（家教撮合服务平台），实现教员与学员之间的需求匹配、预约管理等核心功能。

### 核心功能
- 教员注册、资料管理与认证审核
- 学员注册、学员资料管理
- 找老师需求发布与管理
- 教员申请与备选筛选
- 预约管理（创建、确认、完成、取消）
- 收藏、浏览足迹
- 课后反馈评价
- VIP 会员体系
- 后台运营管理

---

## 2. 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.x（与原项目保持一致） |
| 微服务 | Spring Cloud | 与原项目保持一致 |
| 网关 | Spring Cloud Gateway | 与原项目保持一致 |
| ORM | MyBatis-Plus | 3.x |
| 数据库 | MySQL | 8.0 |
| 缓存 | Redis | 6.x |
| 前端门户 | Nuxt 3 + Vue 3 | 3.x |
| 前端管理后台 | Vue 3 + Element Plus | 3.x |
| 构建工具 | Maven | 3.x |

---

## 3. 项目结构

```
roncoo-education/
├── roncoo-education-user/              # 用户服务（家教功能主要开发位置）
│   ├── roncoo-education-user-service/  # 业务实现
│   │   └── src/main/java/.../user/
│   │       ├── controller/
│   │       │   ├── api/                # 公开接口（无需登录）
│   │       │   ├── auth/               # 需登录接口
│   │       │   └── admin/              # 管理后台接口
│   │       ├── service/                # 业务逻辑层
│   │       ├── dao/                    # 数据访问层
│   │       └── common/                 # BO/DTO/VO
│   └── roncoo-education-user-feign/    # Feign 客户端
├── roncoo-education-gateway/           # API 网关
├── roncoo-education-system/            # 系统管理服务
├── roncoo-education-app/               # 门户前端（Nuxt 3）
├── roncoo-education-admin/             # 管理后台前端（Vue 3）
├── docs/                               # 项目文档
│   ├── gap-analysis.md                 # 差距分析
│   ├── domain-model.md                 # 领域模型
│   ├── db-design.md                    # 数据库设计
│   ├── api-list.md                     # 接口清单
│   ├── route-map.md                    # 前端路由映射
│   ├── assumptions.md                  # 前提假设
│   └── manual-test-checklist.md        # 测试清单
└── sql/                                # SQL 脚本
    └── tutor_platform_ddl.sql          # 建表DDL（待生成）
```

---

## 4. 环境准备

### 4.1 必备环境

| 工具 | 版本要求 |
|------|----------|
| JDK | 1.8+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Redis | 6.0+ |
| Node.js | 18+ |
| npm/pnpm | 最新版 |

### 4.2 数据库初始化

```bash
# 1. 先执行 roncoo-education 原有的数据库初始化脚本
# 2. 再执行家教平台新增表的 DDL
mysql -u root -p roncoo_education < sql/tutor_platform_ddl.sql
```

### 4.3 配置修改

在 `application.yml` 中确认以下配置：

```yaml
# 短信 Mock 模式（开发环境）
sms:
  mock:
    enabled: true

# 文件上传配置
upload:
  max-size: 5MB
  allowed-types: jpg,png,pdf
```

---

## 5. 构建与运行

### 5.1 后端

```bash
# 编译整个项目
mvn clean install -DskipTests

# 启动服务（按顺序）
# 1. 启动 Nacos（注册中心/配置中心）
# 2. 启动 Gateway
cd roncoo-education-gateway && mvn spring-boot:run

# 3. 启动 User 服务
cd roncoo-education-user/roncoo-education-user-service && mvn spring-boot:run

# 4. 启动 System 服务
cd roncoo-education-system/roncoo-education-system-service && mvn spring-boot:run
```

### 5.2 前端门户

```bash
cd roncoo-education-app
npm install
npm run dev    # 开发模式
npm run build  # 生产构建
```

### 5.3 管理后台

```bash
cd roncoo-education-admin
npm install
npm run dev    # 开发模式
npm run build  # 生产构建
```

---

## 6. 接口路由说明

所有家教相关接口通过 Gateway 统一路由，遵循三层前缀规范：

| 前缀 | 说明 | 鉴权 |
|------|------|------|
| `/user/api/**` | 公开接口 | 无需 Token |
| `/user/auth/**` | 用户接口 | 需要 JWT Token |
| `/user/admin/**` | 管理接口 | 需要管理员 Token |

详细接口清单见 `docs/api-list.md`。

---

## 7. 改造要点

### 7.1 复用模块
- 用户注册登录体系（Users + SMS）
- 地区管理（Region 三级联动）
- 权限管理（RBAC）
- 文件上传（Upload）
- API 网关（Gateway）

### 7.2 新增功能
- 教员/学员资料与认证
- 需求发布与申请
- 预约管理
- 收藏、足迹、评价
- VIP 会员体系
- 数据字典

### 7.3 改造说明
- Users 表新增 `userType` 枚举值以区分教员/学员
- RBAC 新增教员、学员角色
- 管理后台新增教员审核、VIP管理等菜单

---

## 8. 文档索引

| 文档 | 说明 |
|------|------|
| [差距分析](docs/gap-analysis.md) | 现有平台与目标平台的差距 |
| [领域模型](docs/domain-model.md) | 核心实体与关系 |
| [数据库设计](docs/db-design.md) | 17 张新增表结构 |
| [接口清单](docs/api-list.md) | 56 个 API 端点 |
| [路由映射](docs/route-map.md) | 40 个前端页面与 API 对应关系 |
| [P0移动端设计规范](docs/mobile-p0-design.md) | 响应式门户的页面范围、导航、组件与交互规范 |
| [P0移动端低保真线框](docs/mobile-p0-wireframes.md) | 首页、列表、详情、个人中心、表单与预约流程草图 |
| [前提假设](docs/assumptions.md) | 设计约束与假设 |
| [测试清单](docs/manual-test-checklist.md) | 78 条测试用例 |

---

## 9. 注意事项

1. **本期不接入支付**：VIP 由管理员手动开通
2. **短信开发模式**：验证码统一为 `123456`，不实际发送
3. **不新增微服务**：所有家教功能在 `roncoo-education-user` 模块中实现
4. **前后端分离**：前端通过 Gateway 调用后端 API
5. **数据库同库**：新增表与原有表在同一数据库中
