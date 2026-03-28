# 家教在线平台 (Tutoring Platform)

基于 roncoo-education 改造的家教在线平台。

## 项目结构

```
├── roncoo-education/          # 后端 (Spring Cloud Alibaba 微服务)
│   ├── roncoo-education-gateway/      # API 网关 :7700
│   ├── roncoo-education-service/
│   │   ├── service-system/            # 系统服务 :7710
│   │   ├── service-user/              # 用户服务 :7720 (家教业务主服务)
│   │   └── service-course/            # 课程服务 :7730
│   ├── sql/                           # SQL 脚本
│   ├── scripts/                       # 初始化脚本
│   └── docker-compose.dev.yml         # 开发环境 Docker
├── roncoo-education-web/      # 门户前端 (Nuxt 3 + Vue 3 + Element Plus)
└── roncoo-education-admin/    # 管理后台 (Vue 3 + Vite + Element Plus)
```

## 快速启动

详见 `roncoo-education/README-dev.md`

## 技术栈

- 后端: Java 17, Spring Boot 3.2, Spring Cloud Alibaba, MyBatis, MySQL 8, Redis, Nacos
- 门户前端: Nuxt 3, Vue 3, Element Plus, Pinia
- 管理后台: Vue 3, Vite, Element Plus, Vue Router, Pinia
