# 家教在线平台 - 数据库设计文档

## 1. 概述

本文档描述家教在线平台新增的 17 张数据表的设计，所有表均遵循 roncoo-education 现有的建表规范：
- 使用 InnoDB 引擎，UTF8MB4 字符集
- 主键使用雪花算法生成的 BIGINT
- 包含 `gmt_create`、`gmt_modified` 时间戳字段
- 逻辑删除字段 `is_deleted`（0正常 1删除）

> DDL 脚本见 `sql/tutor_platform_ddl.sql`

---

## 2. 表清单

| 序号 | 表名 | 说明 | 预估数据量 |
|------|------|------|-----------|
| 1 | tutor_profile | 教员资料 | 万级 |
| 2 | tutor_certification | 教员认证材料 | 万级 |
| 3 | tutor_subject | 教员科目关联 | 十万级 |
| 4 | tutor_available_time | 教员可授课时间 | 十万级 |
| 5 | student_profile | 学员资料 | 万级 |
| 6 | tutor_requirement | 找老师需求 | 万级 |
| 7 | tutor_application | 教员申请 | 十万级 |
| 8 | tutor_reservation | 预约记录 | 十万级 |
| 9 | tutor_shortlist | 备选列表 | 万级 |
| 10 | favorite | 收藏 | 十万级 |
| 11 | footprint | 浏览足迹 | 百万级 |
| 12 | feedback | 反馈评价 | 万级 |
| 13 | vip_membership | VIP会员记录 | 万级 |
| 14 | vip_level | VIP等级定义 | 十条以内 |
| 15 | vip_privilege | VIP权益配置 | 百条以内 |
| 16 | dict_type | 数据字典类型 | 百条以内 |
| 17 | dict_value | 数据字典值 | 千条以内 |

---

## 3. 表结构详细设计

### 3.1 tutor_profile（教员资料）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| user_id | BIGINT | 是 | - | 关联用户ID |
| real_name | VARCHAR(50) | 是 | - | 真实姓名 |
| gender | TINYINT | 是 | - | 性别：1男 2女 |
| birth_date | DATE | 否 | NULL | 出生日期 |
| education | TINYINT | 是 | - | 学历（字典值） |
| university | VARCHAR(100) | 否 | NULL | 毕业院校 |
| major | VARCHAR(100) | 否 | NULL | 专业 |
| teaching_age | INT | 否 | 0 | 教龄（年） |
| identity_type | TINYINT | 是 | - | 身份类型：1大学生 2在职教师 3专职 4退休 |
| introduction | TEXT | 否 | NULL | 自我介绍 |
| success_cases | TEXT | 否 | NULL | 成功案例 |
| hourly_rate | DECIMAL(10,2) | 否 | 0.00 | 期望时薪 |
| service_radius | INT | 否 | 10 | 服务半径(km) |
| province_id | BIGINT | 否 | NULL | 省 |
| city_id | BIGINT | 否 | NULL | 市 |
| district_id | BIGINT | 否 | NULL | 区 |
| avatar_url | VARCHAR(500) | 否 | NULL | 头像URL |
| verify_status | TINYINT | 是 | 0 | 认证状态：0待审核 1通过 2拒绝 |
| verify_remark | VARCHAR(500) | 否 | NULL | 审核备注 |
| status_id | TINYINT | 是 | 1 | 状态：0禁用 1启用 |
| sort | INT | 否 | 0 | 排序权重 |
| view_count | INT | 否 | 0 | 浏览次数 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- UNIQUE: `uk_user_id (user_id)`
- INDEX: `idx_city_subject (city_id, verify_status, status_id)`
- INDEX: `idx_verify_status (verify_status)`

### 3.2 tutor_certification（教员认证材料）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| tutor_profile_id | BIGINT | 是 | - | 教员资料ID |
| cert_type | TINYINT | 是 | - | 证件类型：1身份证 2学历证 3教师资格证 4其他 |
| cert_name | VARCHAR(100) | 否 | NULL | 证件名称 |
| cert_no | VARCHAR(100) | 否 | NULL | 证件编号 |
| image_url | VARCHAR(500) | 是 | - | 证件照片URL |
| verify_status | TINYINT | 是 | 0 | 审核状态 |
| verify_remark | VARCHAR(500) | 否 | NULL | 审核备注 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.3 tutor_subject（教员科目关联）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| tutor_profile_id | BIGINT | 是 | - | 教员资料ID |
| subject_code | VARCHAR(50) | 是 | - | 科目编码（字典） |
| grade_codes | VARCHAR(500) | 否 | NULL | 可教年级编码（逗号分隔） |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.4 tutor_available_time（教员可授课时间）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| tutor_profile_id | BIGINT | 是 | - | 教员资料ID |
| day_of_week | TINYINT | 是 | - | 星期几：1-7 |
| start_time | VARCHAR(10) | 是 | - | 开始时间（HH:mm） |
| end_time | VARCHAR(10) | 是 | - | 结束时间（HH:mm） |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.5 student_profile（学员资料）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| user_id | BIGINT | 是 | - | 关联用户ID |
| real_name | VARCHAR(50) | 是 | - | 姓名 |
| gender | TINYINT | 否 | NULL | 性别 |
| grade | VARCHAR(50) | 否 | NULL | 年级（字典编码） |
| school_name | VARCHAR(200) | 否 | NULL | 所在学校 |
| province_id | BIGINT | 否 | NULL | 省 |
| city_id | BIGINT | 否 | NULL | 市 |
| district_id | BIGINT | 否 | NULL | 区 |
| address | VARCHAR(500) | 否 | NULL | 详细地址 |
| contact_phone | VARCHAR(20) | 否 | NULL | 联系电话 |
| remark | VARCHAR(1000) | 否 | NULL | 备注 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_user_id (user_id)`

### 3.6 tutor_requirement（找老师需求）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| student_profile_id | BIGINT | 是 | - | 学员资料ID |
| user_id | BIGINT | 是 | - | 发布者用户ID |
| title | VARCHAR(200) | 是 | - | 需求标题 |
| subject_code | VARCHAR(50) | 是 | - | 科目编码 |
| grade_code | VARCHAR(50) | 否 | NULL | 年级编码 |
| gender_prefer | TINYINT | 否 | 0 | 性别偏好：0不限 1男 2女 |
| education_prefer | TINYINT | 否 | 0 | 学历偏好 |
| identity_prefer | TINYINT | 否 | 0 | 身份偏好 |
| budget_min | DECIMAL(10,2) | 否 | NULL | 预算下限 |
| budget_max | DECIMAL(10,2) | 否 | NULL | 预算上限 |
| frequency | VARCHAR(100) | 否 | NULL | 上课频率 |
| available_time | VARCHAR(500) | 否 | NULL | 可上课时段 |
| province_id | BIGINT | 否 | NULL | 上课地点-省 |
| city_id | BIGINT | 否 | NULL | 上课地点-市 |
| district_id | BIGINT | 否 | NULL | 上课地点-区 |
| description | TEXT | 否 | NULL | 详细描述 |
| status | TINYINT | 是 | 0 | 状态：0草稿 1发布 2关闭 3完成 |
| application_count | INT | 否 | 0 | 申请人数 |
| view_count | INT | 否 | 0 | 浏览次数 |
| is_top | TINYINT | 否 | 0 | 是否置顶 |
| expire_time | DATETIME | 否 | NULL | 过期时间 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_user_id (user_id)`
- INDEX: `idx_city_subject (city_id, subject_code, status)`

### 3.7 tutor_application（教员申请）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| requirement_id | BIGINT | 是 | - | 需求ID |
| tutor_profile_id | BIGINT | 是 | - | 教员资料ID |
| user_id | BIGINT | 是 | - | 申请人用户ID |
| message | VARCHAR(1000) | 否 | NULL | 申请留言 |
| quoted_rate | DECIMAL(10,2) | 否 | NULL | 报价 |
| status | TINYINT | 是 | 0 | 状态：0待处理 1已查看 2备选 3拒绝 4接受 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- UNIQUE: `uk_req_tutor (requirement_id, tutor_profile_id)`
- INDEX: `idx_tutor_profile_id (tutor_profile_id)`

### 3.8 tutor_reservation（预约记录）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| tutor_profile_id | BIGINT | 是 | - | 教员资料ID |
| student_profile_id | BIGINT | 是 | - | 学员资料ID |
| user_id | BIGINT | 是 | - | 预约发起人 |
| subject_code | VARCHAR(50) | 是 | - | 科目 |
| scheduled_date | DATE | 是 | - | 预约日期 |
| scheduled_time | VARCHAR(50) | 是 | - | 预约时段 |
| duration | INT | 否 | 120 | 时长（分钟） |
| address | VARCHAR(500) | 否 | NULL | 上课地点 |
| agreed_rate | DECIMAL(10,2) | 否 | NULL | 约定时薪 |
| status | TINYINT | 是 | 0 | 状态：0待确认 1已确认 2完成 3取消 |
| cancel_reason | VARCHAR(500) | 否 | NULL | 取消原因 |
| confirmed_time | DATETIME | 否 | NULL | 确认时间 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_tutor (tutor_profile_id, status)`
- INDEX: `idx_student (student_profile_id, status)`
- INDEX: `idx_date (scheduled_date)`

### 3.9 tutor_shortlist（备选列表）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| requirement_id | BIGINT | 是 | - | 需求ID |
| application_id | BIGINT | 是 | - | 申请ID |
| tutor_profile_id | BIGINT | 是 | - | 教员ID |
| user_id | BIGINT | 是 | - | 学员用户ID |
| sort_num | INT | 否 | 0 | 排序 |
| remark | VARCHAR(500) | 否 | NULL | 备注 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.10 favorite（收藏）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| user_id | BIGINT | 是 | - | 收藏者 |
| target_type | TINYINT | 是 | - | 目标类型：1教员 2需求 |
| target_id | BIGINT | 是 | - | 目标ID |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- UNIQUE: `uk_user_target (user_id, target_type, target_id)`
- INDEX: `idx_target (target_type, target_id)`

### 3.11 footprint（浏览足迹）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| user_id | BIGINT | 是 | - | 用户ID |
| target_type | TINYINT | 是 | - | 目标类型 |
| target_id | BIGINT | 是 | - | 目标ID |
| view_time | DATETIME | 是 | CURRENT_TIMESTAMP | 浏览时间 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_user_time (user_id, view_time DESC)`
- INDEX: `idx_user_target_date (user_id, target_type, target_id, view_time)`

### 3.12 feedback（反馈评价）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| reservation_id | BIGINT | 是 | - | 预约ID |
| from_user_id | BIGINT | 是 | - | 评价人 |
| to_user_id | BIGINT | 是 | - | 被评价人 |
| rating | TINYINT | 是 | - | 评分：1-5 |
| content | VARCHAR(2000) | 否 | NULL | 评价内容 |
| tags | VARCHAR(500) | 否 | NULL | 标签（JSON数组） |
| is_anonymous | TINYINT | 否 | 0 | 是否匿名 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- UNIQUE: `uk_reservation_from (reservation_id, from_user_id)`
- INDEX: `idx_to_user (to_user_id)`

### 3.13 vip_membership（VIP会员记录）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| user_id | BIGINT | 是 | - | 用户ID |
| level_id | BIGINT | 是 | - | VIP等级ID |
| start_time | DATETIME | 是 | - | 开始时间 |
| expire_time | DATETIME | 是 | - | 过期时间 |
| status | TINYINT | 是 | 1 | 状态：0过期 1生效 |
| source | TINYINT | 是 | 1 | 来源：1手动开通 2系统赠送 |
| operator_id | BIGINT | 否 | NULL | 操作人ID |
| remark | VARCHAR(500) | 否 | NULL | 备注 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_user_id (user_id, status)`

### 3.14 vip_level（VIP等级定义）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| level_name | VARCHAR(50) | 是 | - | 等级名称 |
| level_code | VARCHAR(20) | 是 | - | 等级编码 |
| sort | INT | 否 | 0 | 排序 |
| description | VARCHAR(500) | 否 | NULL | 描述 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.15 vip_privilege（VIP权益配置）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| level_id | BIGINT | 是 | - | VIP等级ID |
| privilege_type | VARCHAR(50) | 是 | - | 权益类型编码 |
| privilege_value | VARCHAR(100) | 是 | - | 权益值 |
| description | VARCHAR(500) | 否 | NULL | 描述 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

### 3.16 dict_type（数据字典类型）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| dict_code | VARCHAR(50) | 是 | - | 字典编码 |
| dict_name | VARCHAR(100) | 是 | - | 字典名称 |
| remark | VARCHAR(500) | 否 | NULL | 备注 |
| sort | INT | 否 | 0 | 排序 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- UNIQUE: `uk_dict_code (dict_code)`

### 3.17 dict_value（数据字典值）

| 字段名 | 类型 | 是否必填 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | BIGINT | 是 | - | 主键 |
| dict_code | VARCHAR(50) | 是 | - | 字典编码 |
| value_code | VARCHAR(50) | 是 | - | 值编码 |
| value_name | VARCHAR(100) | 是 | - | 值名称 |
| sort | INT | 否 | 0 | 排序 |
| remark | VARCHAR(500) | 否 | NULL | 备注 |
| is_deleted | TINYINT | 是 | 0 | 逻辑删除 |
| gmt_create | DATETIME | 是 | CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | 是 | CURRENT_TIMESTAMP | 修改时间 |

**索引：**
- INDEX: `idx_dict_code (dict_code, sort)`
- UNIQUE: `uk_dict_value (dict_code, value_code)`
