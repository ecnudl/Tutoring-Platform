# 项目工程约束 (Claude Code 全局规则)

本文件定义所有代码修改必须遵守的工程约束。每次编码前先读此文件，完成后对照检查。

---

## 一、总体原则

- **云厂商无关**: 业务逻辑不绑定腾讯云/华为云/阿里云专有能力，未来可迁移。
- **最小改动**: 复用现有代码和目录风格，不做无关重构。
- **单任务聚焦**: 每次只完成当前任务范围，不顺手扩展到其他模块。

## 二、分层约束

### 禁止直接依赖云 SDK 的层

- controller
- biz / service（业务层）
- dao（领域层）

### 云 SDK 只允许出现的层

- `infrastructure/` / `adapter/` / `provider/` / 现有项目中的 `impl/` 层
- 类名示例: `TencentSmsProvider`, `CosStorageProvider`, `MinIOUploadImpl`

### 外部能力抽象接口

新增外部能力时必须先定义接口，再实现 provider:

| 能力 | 接口 | 现有实现 |
|------|------|---------|
| 短信 | `SmsFace` (已有) | `MockSmsImpl`, `AliyunSmsImpl`, `LkyunSmsImpl` |
| 存储 | `UploadFace` (已有) | `MinIOUploadImpl` |
| 支付 | `PaymentProvider` (待建) | `ManualPaymentProvider` (待建) |

业务代码只能 import 接口，不能 import 具体实现类。

## 三、配置约束

### 禁止硬编码的内容

以下内容 **必须** 来自环境变量或配置文件，禁止写死在代码中:

- 域名、回调地址
- endpoint、region、bucket
- AccessKey / SecretKey
- 数据库地址、Redis 地址
- 内网 IP、固定端口
- private DNS、安全组规则

### 配置方式

- 后端: 通过 `bootstrap.properties` 中的 `${ENV_VAR:default}` 读取环境变量，运行时配置存放在 Nacos 配置中心
- 前端: 通过 `.env` / `runtimeConfig` 读取
- 模板: 提供 `.env.example`
- 环境切换: 通过 `spring.profiles.active` 或环境变量

### 现有配置流

```
.env → bootstrap.properties (${ENV_VAR:default})
                ↓
         Nacos (dev namespace)
                ↓
    application.properties (数据源/Redis/JWT/日志)
    gateway.properties (路由/CORS)
    service-*.properties (xxl-job等)
```

## 四、存储与文件约束

- 文件上传必须走 `UploadFace` 接口
- 数据库 **禁止** 保存厂商绝对 URL (如固定 COS/OBS 域名)
- 数据库只保存文件元数据: bucket, objectKey, originalName, contentType, size, hash
- 对外 URL 在运行时通过 `UploadFace` 或 UrlBuilder 生成
- 开发环境优先使用 `MinIOUploadImpl`，云厂商实现可插拔替换
- 现有 `Upload.java` 中的配置字段 (minioEndpoint, minioBucket 等) 从 `sys_config` 表读取，**不硬编码**

## 五、短信与支付约束

### 短信

- 必须走 `SmsFace` 接口
- 开发环境使用 `MockSmsImpl` (bean name: `mockSms`)
- 切换短信平台: 修改 `sys_config` 表中 `smsPlatform` 值即可，对应 `SmsPlatformEnum`
- 验证码存 Redis (`Constants.RedisPre.CODE + mobile`)，不存数据库

### 支付

- 必须走 `PaymentProvider` 抽象
- 正式支付未接入前使用 `ManualPaymentProvider`
- "支付成功" 必须通过明确状态流转确认
- 禁止用户上传截图后自动变更为已支付
- 前端所有支付入口显示占位提示: "在线支付暂未接入，请联系客服"

## 六、数据库与缓存约束

- 使用标准 MySQL 8 + Redis 通用能力，不依赖厂商专有特性
- 新增表遵循现有约定: `id` BIGINT (雪花), `gmt_create`, `gmt_modified`, `status_id`
- ID 生成: `IdWorker.getId()` (雪花算法)
- 状态字段使用枚举类，禁止魔法值。现有枚举路径: `com.roncoo.education.common.core.enums.*`
- 验证码等短期数据放 Redis，不放数据库
- SQL 全部参数化查询 (MyBatis Example 模式已保证)

## 七、开发与部署约束

- 本地开发使用 `docker-compose.dev.yml` (MySQL + Redis + Nacos + MinIO)
- 不依赖固定目录、固定挂载点或本地绝对路径
- 所有脚本可在新服务器复用
- 外部服务切换目标: "只改配置或切换 provider"，不重写业务代码

## 八、编码前后检查清单

### 编码前必须输出

1. 实施方案
2. 受影响文件列表
3. 风险点

### 编码后必须输出

1. 实际改动文件列表
2. 如何运行/验证
3. 是否引入云厂商锁定点；如有，写出位置、原因、替代方案

## 九、项目结构速查

```
roncoo-education/
├── roncoo-education-common/
│   ├── roncoo-education-common-core/     # 枚举、常量、工具
│   │   └── enums/                        # 所有枚举
│   └── roncoo-education-common-service/  # 通用服务实现
│       ├── sms/                          # SmsFace + 实现
│       ├── upload/                       # UploadFace + 实现
│       └── cache/CacheRedis.java         # Redis 缓存
├── roncoo-education-gateway/             # API 网关 :7700
├── roncoo-education-service/
│   ├── service-system/                   # 系统服务 :7710
│   ├── service-user/                     # 用户服务 :7720 (家教业务主服务)
│   │   ├── dao/                          # DAO 接口
│   │   ├── dao/impl/                     # DAO 实现 + Mapper
│   │   ├── dao/impl/mapper/entity/       # Entity + Example
│   │   ├── service/api/                  # 公开 API (无需登录)
│   │   ├── service/auth/                 # 认证 API (需 JWT)
│   │   └── service/admin/               # 管理 API (需 RBAC)
│   └── service-course/                   # 课程服务 :7730
├── roncoo-education-feign/               # Feign 接口
├── sql/                                  # SQL 脚本
├── scripts/                              # 初始化脚本
├── docker-compose.dev.yml
└── .env.example
```

## 十、API 路径约定

| 路径前缀 | 认证要求 | 说明 |
|----------|---------|------|
| `/user/api/*` | 无 | 公开接口 |
| `/user/auth/*` | JWT Token | 登录用户接口 |
| `/user/admin/*` | JWT + RBAC | 管理后台接口 |
| `/system/admin/*` | JWT + RBAC | 系统管理接口 |

## 十一、关键技术栈

- 后端: Java 17, Spring Boot 3.2.4, Spring Cloud Alibaba, MyBatis, Druid, Jasypt
- 前端门户: Nuxt 3 + Vue 3 + Element Plus + Pinia
- 前端管理: Vue 3 + Vite + Element Plus + Vue Router + Pinia
- 基础设施: MySQL 8, Redis 7, Nacos 2.3, MinIO
