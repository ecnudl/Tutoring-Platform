# 家教在线平台 - 前端路由映射文档

## 1. 概述

前端分为两个独立应用：
- **门户网站（Portal）**：基于 Nuxt 3，面向教员和学员
- **管理后台（Admin）**：基于 Vue 3 + Element Plus，面向运营管理人员

---

## 2. 门户网站路由（Nuxt 3）

### 2.1 公共页面（无需登录）

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/` | 首页 | `GET /user/api/tutor/profile/page` (推荐教员)<br>`GET /user/api/tutor/requirement/page` (最新需求) |
| `/tutor` | 找教员列表 | `GET /user/api/tutor/profile/page`<br>`GET /user/api/dict/list` (筛选项) |
| `/tutor/:id` | 教员详情 | `GET /user/api/tutor/profile/{id}`<br>`POST footprint (自动记录)` |
| `/requirement` | 找老师需求列表 | `GET /user/api/tutor/requirement/page` |
| `/requirement/:id` | 需求详情 | `GET /user/api/tutor/requirement/{id}` |
| `/login` | 登录页 | 复用现有登录接口 |
| `/register` | 注册页 | 复用现有注册接口（扩展角色选择） |
| `/about` | 关于我们 | 静态页面 |

### 2.2 教员中心（需登录 - 教员角色）

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/tutor/center` | 教员中心首页 | `GET /user/auth/tutor/profile/me`<br>`GET /user/auth/vip/my` |
| `/tutor/center/profile` | 我的资料 | `GET /user/auth/tutor/profile/me`<br>`PUT /user/auth/tutor/profile/update` |
| `/tutor/center/profile/edit` | 编辑资料 | `PUT /user/auth/tutor/profile/update`<br>`POST /user/auth/tutor/subject/save-batch`<br>`POST /user/auth/tutor/available-time/save-batch` |
| `/tutor/center/certification` | 认证管理 | `GET /user/auth/tutor/certification/list`<br>`POST /user/auth/tutor/certification/save`<br>`POST /user/auth/tutor/profile/submit-verify` |
| `/tutor/center/application` | 我的申请 | `GET /user/auth/tutor/application/my-page` |
| `/tutor/center/reservation` | 我的预约 | `GET /user/auth/tutor/reservation/my-page` |
| `/tutor/center/reservation/:id` | 预约详情 | `GET /user/auth/tutor/reservation/{id}`<br>`PUT /user/auth/tutor/reservation/confirm/{id}` |
| `/tutor/center/feedback` | 收到的评价 | `GET /user/auth/feedback/received-page` |
| `/tutor/center/favorite` | 收藏的需求 | `GET /user/auth/favorite/page` |

### 2.3 学员中心（需登录 - 学员角色）

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/student/center` | 学员中心首页 | `GET /user/auth/student/profile/list`<br>`GET /user/auth/vip/my` |
| `/student/center/profile` | 学员资料管理 | `GET /user/auth/student/profile/list`<br>`POST /user/auth/student/profile/save` |
| `/student/center/profile/edit/:id?` | 编辑学员资料 | `POST /user/auth/student/profile/save`<br>`PUT /user/auth/student/profile/update` |
| `/student/center/requirement` | 我的需求 | `GET /user/auth/tutor/requirement/my-page` |
| `/student/center/requirement/create` | 发布需求 | `POST /user/auth/tutor/requirement/save`<br>`PUT /user/auth/tutor/requirement/publish/{id}` |
| `/student/center/requirement/:id/applications` | 查看申请 | `GET /user/auth/tutor/application/received-page` |
| `/student/center/requirement/:id/shortlist` | 备选列表 | `GET /user/auth/tutor/shortlist/list` |
| `/student/center/reservation` | 我的预约 | `GET /user/auth/tutor/reservation/my-page` |
| `/student/center/reservation/create` | 创建预约 | `POST /user/auth/tutor/reservation/create` |
| `/student/center/reservation/:id` | 预约详情 | `GET /user/auth/tutor/reservation/{id}` |
| `/student/center/feedback` | 我的评价 | `GET /user/auth/feedback/my-page`<br>`POST /user/auth/feedback/save` |
| `/student/center/favorite` | 收藏的教员 | `GET /user/auth/favorite/page` |
| `/student/center/footprint` | 浏览足迹 | `GET /user/auth/footprint/page` |

---

## 3. 管理后台路由（Vue 3）

### 3.1 教员管理

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/tutor/profile` | 教员列表 | `GET /user/admin/tutor/profile/page` |
| `/tutor/profile/:id` | 教员详情 | `GET /user/admin/tutor/profile/{id}` |
| `/tutor/verify` | 教员认证审核 | `GET /user/admin/tutor/certification/page`<br>`PUT /user/admin/tutor/certification/verify` |

### 3.2 需求管理

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/requirement/list` | 需求列表 | `GET /user/admin/tutor/requirement/page` |
| `/requirement/:id` | 需求详情 | `GET /user/admin/tutor/requirement/{id}` |

### 3.3 预约管理

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/reservation/list` | 预约列表 | `GET /user/admin/tutor/reservation/page` |
| `/reservation/:id` | 预约详情 | `GET /user/admin/tutor/reservation/{id}` |

### 3.4 评价管理

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/feedback/list` | 评价列表 | `GET /user/admin/feedback/page` |

### 3.5 VIP 管理

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/vip/membership` | VIP会员列表 | `GET /user/admin/vip/membership/page`<br>`POST /user/admin/vip/membership/grant` |
| `/vip/level` | VIP等级配置 | `GET /user/admin/vip/level/list`<br>`POST /user/admin/vip/level/save` |
| `/vip/privilege` | VIP权益配置 | `GET /user/admin/vip/privilege/list`<br>`POST /user/admin/vip/privilege/save` |

### 3.6 数据字典

| 路由路径 | 页面名称 | 调用API |
|----------|----------|---------|
| `/dict/type` | 字典类型管理 | `GET /user/admin/dict/type/page`<br>`POST /user/admin/dict/type/save` |
| `/dict/value` | 字典值管理 | `GET /user/admin/dict/value/list`<br>`POST /user/admin/dict/value/save` |

---

## 4. 路由统计

| 应用 | 页面数量 |
|------|----------|
| 门户 - 公共页面 | 7 |
| 门户 - 教员中心 | 9 |
| 门户 - 学员中心 | 13 |
| 管理后台 | 11 |
| **合计** | **40** |

---

## 5. 路由守卫规则

### 门户网站
- `/tutor/center/**` - 需登录且 userType 包含教员角色
- `/student/center/**` - 需登录且 userType 包含学员角色
- 其他路径 - 无限制

### 管理后台
- 所有路径均需管理员角色登录
- 菜单权限通过 RBAC 控制
