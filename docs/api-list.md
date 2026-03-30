# 家教在线平台 - API 接口清单

## 1. 概述

接口遵循 roncoo-education 现有的三层路由规范：
- **Public API** (`/user/api/`) - 无需登录，公开访问
- **Auth API** (`/user/auth/`) - 需要登录（JWT Token）
- **Admin API** (`/user/admin/`) - 需要管理员权限

统一返回格式：
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

分页参数：`pageCurrent`（页码）、`pageSize`（每页条数）

---

## 2. Public API（公开接口）

### 2.1 教员相关

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/api/tutor/profile/page` | 分页查询教员列表（已认证） |
| GET | `/user/api/tutor/profile/{id}` | 查看教员详情 |
| GET | `/user/api/tutor/profile/search` | 搜索教员（科目/地区/学历筛选） |

### 2.2 需求相关

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/api/tutor/requirement/page` | 分页查询需求列表（已发布） |
| GET | `/user/api/tutor/requirement/{id}` | 查看需求详情 |

### 2.3 字典相关

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/api/dict/list` | 根据字典编码获取字典值列表 |
| GET | `/user/api/dict/batch` | 批量获取多个字典类型的值 |

### 2.4 地区相关

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/api/region/list` | 获取地区列表（复用现有接口） |

---

## 3. Auth API（需登录接口）

### 3.1 教员资料管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/tutor/profile/me` | 获取当前用户的教员资料 |
| POST | `/user/auth/tutor/profile/save` | 创建教员资料 |
| PUT | `/user/auth/tutor/profile/update` | 修改教员资料 |
| POST | `/user/auth/tutor/profile/submit-verify` | 提交认证审核 |

### 3.2 教员认证材料

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/tutor/certification/list` | 获取我的认证材料列表 |
| POST | `/user/auth/tutor/certification/save` | 上传认证材料 |
| DELETE | `/user/auth/tutor/certification/{id}` | 删除认证材料 |

### 3.3 教员科目与时间

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/tutor/subject/list` | 获取我的科目列表 |
| POST | `/user/auth/tutor/subject/save-batch` | 批量保存科目 |
| GET | `/user/auth/tutor/available-time/list` | 获取我的可授课时间 |
| POST | `/user/auth/tutor/available-time/save-batch` | 批量保存可授课时间 |

### 3.4 学员资料管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/student/profile/list` | 获取我的学员资料列表 |
| POST | `/user/auth/student/profile/save` | 创建学员资料 |
| PUT | `/user/auth/student/profile/update` | 修改学员资料 |
| DELETE | `/user/auth/student/profile/{id}` | 删除学员资料 |

### 3.5 需求管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/tutor/requirement/my-page` | 我发布的需求列表 |
| POST | `/user/auth/tutor/requirement/save` | 创建需求（草稿） |
| PUT | `/user/auth/tutor/requirement/update` | 修改需求 |
| PUT | `/user/auth/tutor/requirement/publish/{id}` | 发布需求 |
| PUT | `/user/auth/tutor/requirement/close/{id}` | 关闭需求 |

### 3.6 教员申请

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/auth/tutor/application/apply` | 教员申请需求 |
| GET | `/user/auth/tutor/application/my-page` | 我的申请列表（教员视角） |
| GET | `/user/auth/tutor/application/received-page` | 收到的申请列表（学员视角） |
| PUT | `/user/auth/tutor/application/view/{id}` | 标记为已查看 |
| PUT | `/user/auth/tutor/application/shortlist/{id}` | 加入备选 |
| PUT | `/user/auth/tutor/application/reject/{id}` | 拒绝申请 |
| PUT | `/user/auth/tutor/application/accept/{id}` | 接受申请 |

### 3.7 备选列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/tutor/shortlist/list` | 某需求的备选列表 |
| PUT | `/user/auth/tutor/shortlist/sort` | 调整备选排序 |
| DELETE | `/user/auth/tutor/shortlist/{id}` | 移出备选 |

### 3.8 预约管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/auth/tutor/reservation/create` | 创建预约 |
| GET | `/user/auth/tutor/reservation/my-page` | 我的预约列表 |
| GET | `/user/auth/tutor/reservation/{id}` | 预约详情 |
| PUT | `/user/auth/tutor/reservation/confirm/{id}` | 确认预约（教员） |
| PUT | `/user/auth/tutor/reservation/cancel/{id}` | 取消预约 |
| PUT | `/user/auth/tutor/reservation/complete/{id}` | 标记完成 |

### 3.9 收藏

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/auth/favorite/toggle` | 收藏/取消收藏 |
| GET | `/user/auth/favorite/page` | 我的收藏列表 |
| GET | `/user/auth/favorite/check` | 检查是否已收藏 |

### 3.10 浏览足迹

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/footprint/page` | 我的足迹列表 |
| DELETE | `/user/auth/footprint/clear` | 清空足迹 |

### 3.11 反馈评价

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/auth/feedback/save` | 提交评价 |
| GET | `/user/auth/feedback/my-page` | 我的评价列表 |
| GET | `/user/auth/feedback/received-page` | 收到的评价列表 |

### 3.12 VIP

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/auth/vip/my` | 获取我的VIP信息 |
| GET | `/user/auth/vip/privileges` | 获取VIP权益列表 |

---

## 4. Admin API（管理后台接口）

### 4.1 教员管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/tutor/profile/page` | 分页查询教员列表 |
| GET | `/user/admin/tutor/profile/{id}` | 查看教员详情 |
| PUT | `/user/admin/tutor/profile/verify` | 审核教员认证 |
| PUT | `/user/admin/tutor/profile/status` | 启用/禁用教员 |
| PUT | `/user/admin/tutor/profile/sort` | 设置排序权重 |

### 4.2 教员认证审核

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/tutor/certification/page` | 待审核认证列表 |
| PUT | `/user/admin/tutor/certification/verify` | 审核认证材料 |

### 4.3 需求管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/tutor/requirement/page` | 分页查询需求列表 |
| GET | `/user/admin/tutor/requirement/{id}` | 查看需求详情 |
| PUT | `/user/admin/tutor/requirement/close/{id}` | 关闭需求 |

### 4.4 预约管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/tutor/reservation/page` | 分页查询预约列表 |
| GET | `/user/admin/tutor/reservation/{id}` | 查看预约详情 |

### 4.5 评价管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/feedback/page` | 分页查询评价列表 |
| DELETE | `/user/admin/feedback/{id}` | 删除不当评价 |

### 4.6 VIP 管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/vip/membership/page` | VIP会员列表 |
| POST | `/user/admin/vip/membership/grant` | 手动授予VIP |
| PUT | `/user/admin/vip/membership/revoke/{id}` | 撤销VIP |
| GET | `/user/admin/vip/level/list` | VIP等级列表 |
| POST | `/user/admin/vip/level/save` | 新增/修改等级 |
| GET | `/user/admin/vip/privilege/list` | VIP权益列表 |
| POST | `/user/admin/vip/privilege/save` | 新增/修改权益 |

### 4.7 数据字典管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/admin/dict/type/page` | 字典类型列表 |
| POST | `/user/admin/dict/type/save` | 新增/修改字典类型 |
| DELETE | `/user/admin/dict/type/{id}` | 删除字典类型 |
| GET | `/user/admin/dict/value/list` | 字典值列表 |
| POST | `/user/admin/dict/value/save` | 新增/修改字典值 |
| DELETE | `/user/admin/dict/value/{id}` | 删除字典值 |

---

## 5. 接口统计

| 分类 | 数量 |
|------|------|
| Public API | 7 个 |
| Auth API | 30 个 |
| Admin API | 19 个 |
| **合计** | **56 个** |
