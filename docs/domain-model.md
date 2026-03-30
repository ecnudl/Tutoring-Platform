# 家教在线平台 - 领域模型文档

## 1. 概述

本文档描述家教在线平台的核心领域模型、实体关系及业务规则。

---

## 2. 核心实体

### 2.1 TutorProfile（教员资料）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| userId | Long | 关联 Users 表 |
| realName | String | 真实姓名 |
| gender | Integer | 性别（1男 2女） |
| birthDate | Date | 出生日期 |
| education | Integer | 学历（字典） |
| university | String | 毕业院校 |
| major | String | 专业 |
| teachingAge | Integer | 教龄（年） |
| identityType | Integer | 身份类型（1大学生 2在职教师 3专职教师 4退休教师） |
| introduction | String | 自我介绍 |
| successCases | String | 成功案例 |
| hourlyRate | Decimal | 期望时薪（元） |
| serviceRadius | Integer | 服务半径（公里） |
| avatarUrl | String | 头像 |
| verifyStatus | Integer | 认证状态（0待审核 1通过 2拒绝） |
| status | Integer | 状态（0禁用 1启用） |
| sort | Integer | 排序权重 |

**业务规则：**
- 一个用户只能创建一个教员资料
- 认证通过后方可在前台展示
- 修改关键信息需重新审核

### 2.2 StudentProfile（学员资料）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| userId | Long | 关联 Users 表 |
| realName | String | 学员姓名（或家长姓名） |
| gender | Integer | 学员性别 |
| grade | Integer | 年级（字典） |
| schoolName | String | 所在学校 |
| provinceId | Long | 省 |
| cityId | Long | 市 |
| districtId | Long | 区 |
| address | String | 详细地址 |
| contactPhone | String | 联系电话 |
| remark | String | 备注 |

**业务规则：**
- 一个用户可创建多个学员资料（家长可管理多个孩子）

### 2.3 TutorRequirement（找老师需求）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| studentProfileId | Long | 关联学员资料 |
| userId | Long | 发布者 |
| title | String | 需求标题 |
| subjectId | Long | 科目（字典） |
| gradeId | Integer | 年级 |
| genderPrefer | Integer | 教员性别偏好（0不限 1男 2女） |
| educationPrefer | Integer | 教员学历偏好 |
| identityPrefer | Integer | 教员身份偏好 |
| budgetMin | Decimal | 预算下限（元/小时） |
| budgetMax | Decimal | 预算上限 |
| frequency | String | 上课频率（如"每周2次"） |
| availableTime | String | 可上课时段 |
| provinceId | Long | 上课地点-省 |
| cityId | Long | 上课地点-市 |
| districtId | Long | 上课地点-区 |
| description | String | 详细描述 |
| status | Integer | 状态（0草稿 1发布 2已关闭 3已完成） |
| expireTime | DateTime | 过期时间 |

**业务规则：**
- 发布后30天自动过期
- VIP 用户可置顶需求
- 已关闭/已完成的需求不再接受申请

### 2.4 TutorApplication（教员申请）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| requirementId | Long | 关联需求 |
| tutorProfileId | Long | 关联教员资料 |
| userId | Long | 申请人 |
| message | String | 申请留言 |
| quotedRate | Decimal | 报价（元/小时） |
| status | Integer | 状态（0待处理 1已查看 2加入备选 3已拒绝 4已接受） |

**业务规则：**
- 同一教员对同一需求只能申请一次
- 非 VIP 教员每天申请次数有限制
- 学员可将申请加入备选列表

### 2.5 TutorReservation（预约）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| tutorProfileId | Long | 教员 |
| studentProfileId | Long | 学员 |
| userId | Long | 预约发起人 |
| subjectId | Long | 科目 |
| scheduledDate | Date | 预约日期 |
| scheduledTime | String | 预约时段 |
| duration | Integer | 时长（分钟） |
| address | String | 上课地点 |
| agreedRate | Decimal | 约定时薪 |
| status | Integer | 状态（0待确认 1已确认 2已完成 3已取消） |
| cancelReason | String | 取消原因 |

**业务规则：**
- 教员需在24小时内确认预约，否则自动取消
- 已确认的预约在上课时间前2小时内不可取消
- 完成后可发起评价

### 2.6 TutorShortlist（备选列表）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| requirementId | Long | 关联需求 |
| applicationId | Long | 关联申请 |
| tutorProfileId | Long | 教员 |
| userId | Long | 学员用户 |
| sortNum | Integer | 排序 |
| remark | String | 备注 |

### 2.7 Favorite（收藏）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| userId | Long | 收藏者 |
| targetType | Integer | 目标类型（1教员 2需求） |
| targetId | Long | 目标ID |

**业务规则：**
- 学员可收藏教员，教员可收藏需求
- 不可重复收藏

### 2.8 Footprint（浏览足迹）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| userId | Long | 用户 |
| targetType | Integer | 浏览目标类型 |
| targetId | Long | 目标ID |
| viewTime | DateTime | 浏览时间 |

**业务规则：**
- 同一用户同一目标当天只记录一次
- 超过90天自动清理

### 2.9 Feedback（反馈评价）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| reservationId | Long | 关联预约 |
| fromUserId | Long | 评价人 |
| toUserId | Long | 被评价人 |
| rating | Integer | 评分（1-5） |
| content | String | 评价内容 |
| tags | String | 评价标签（JSON数组） |
| isAnonymous | Integer | 是否匿名 |

**业务规则：**
- 仅已完成的预约可评价
- 双方均可评价对方
- 评价后不可修改

### 2.10 VipMembership（VIP会员）

| 属性 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| userId | Long | 用户 |
| levelId | Long | VIP等级 |
| startTime | DateTime | 开始时间 |
| expireTime | DateTime | 过期时间 |
| status | Integer | 状态（0过期 1生效） |
| source | Integer | 来源（1管理员手动 2系统赠送） |

---

## 3. 实体关系图

```
Users(1) ──── (0..1) TutorProfile
  │                      │
  │                      ├── (N) TutorCertification
  │                      ├── (N) TutorSubject
  │                      ├── (N) TutorAvailableTime
  │                      ├── (N) TutorApplication ──── (1) TutorRequirement
  │                      └── (N) TutorReservation ──┐
  │                                                  │
  ├── (N) StudentProfile                             │
  │        │                                         │
  │        ├── (N) TutorRequirement                  │
  │        │        │                                │
  │        │        └── (N) TutorShortlist           │
  │        │                                         │
  │        └── (N) TutorReservation ─────────────────┘
  │                                        │
  ├── (N) Favorite                         └── (0..1) Feedback
  ├── (N) Footprint
  └── (0..1) VipMembership ──── (1) VipLevel
                                     │
                                     └── (N) VipPrivilege
```

---

## 4. 字典表设计

### DictType（字典类型）

| dictCode | dictName |
|----------|----------|
| subject | 科目 |
| grade | 年级 |
| education | 学历 |
| tutor_identity | 教员身份类型 |
| teach_method | 授课方式 |
| feedback_tag | 评价标签 |
| vip_privilege_type | VIP权益类型 |

### DictValue 示例

**科目（subject）：**
语文、数学、英语、物理、化学、生物、历史、地理、政治、音乐、美术、体育、编程、其他

**年级（grade）：**
小学一年级 ~ 小学六年级、初一 ~ 初三、高一 ~ 高三、大学、成人

**学历（education）：**
高中、大专、本科、硕士、博士

**教员身份（tutor_identity）：**
在校大学生、在职教师、专职教师、退休教师

---

## 5. 状态机

### 需求状态流转
```
草稿(0) → 发布(1) → 已关闭(2)
                   → 已完成(3)
                   → 过期自动关闭(2)
```

### 申请状态流转
```
待处理(0) → 已查看(1) → 加入备选(2) → 已接受(4)
                      → 已拒绝(3)
```

### 预约状态流转
```
待确认(0) → 已确认(1) → 已完成(2)
         → 已取消(3)    → 已取消(3)
```

### 教员认证状态流转
```
待审核(0) → 通过(1)
         → 拒绝(2) → 重新提交 → 待审核(0)
```
