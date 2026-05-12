# 591-education · 591家教网

> 全国 17 城家教撮合服务平台 — 名校教员 / 一对一上门 + 在线辅导 / 严选审核

[![Spring Boot](https://img.shields.io/badge/spring--boot-3.2.4-blue.svg)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/vue-3.x-brightgreen.svg)](https://vuejs.org/)
[![Nuxt 3](https://img.shields.io/badge/nuxt-3.x-00DC82.svg)](https://nuxt.com/)
[![Java](https://img.shields.io/badge/java-17-orange.svg)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/mysql-8.0-blue.svg)](https://www.mysql.com/)

## 项目说明

591-education 是一个为家长 / 教员双向撮合的家教服务平台, 覆盖北京 / 上海 / 广州 / 南京 / 苏州 / 杭州 / 合肥 / 福州 / 南昌 / 济南 / 天津 / 武汉 / 郑州 / 重庆 / 西安 / 成都 / 长沙 共 17 个城市。

- 🏠 **官方站点**: <https://591jiajiao.cn>
- 📧 **客服邮箱**: jiajiao591@126.com

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3 / Spring Cloud Gateway / Java 17 / MyBatis / MySQL 8 / Redis / Nacos / MinIO |
| 学员/教员 web | Nuxt 3 + Vue 3 + Element Plus + Pinia |
| 管理后台 | Vue 3 + Element Plus + Vite |
| 安全 | fail2ban + 应用层智能 captcha + Honeypot + FingerprintJS + Redis 限流 |

## 仓库结构 (单 GitHub repo, 多分支)

| 分支 | 内容 |
|---|---|
| `master` | 学员/教员前端 (Nuxt 3, `591-education-web`) |
| `backend` | Java 微服务后端 (`591-education-service-*`) |
| `admin` | 管理后台前端 (Vue 3, `591-education-admin`) |

## 核心特性

- 17 城子域名同源部署 (`*.591jiajiao.cn`), 统一后端 + cookie/SSO
- 教员严审: 身份证 / 学历 / 教师资格 / 学校 / 自我介绍 多维校验
- 30 + 科目扁平字典, 跨学段统一搜索
- 学员订单结构化采集: 学生情况 / 教员要求 / 交通信息
- 智能反爬: 阶梯 captcha (失败 3 次后触发) + 蜜罐 + IP/账号/指纹多维限流 + fail2ban
- SEO 友好: `spaLoadingTemplate` 注入静态 SEO HTML, robots/sitemap, 17 城子域 sitemap

## 开发文档

- [本地开发环境指南](./README-dev.md)
- [服务器迁移部署指南](./README-deploy.md)
- [家教平台改造说明](./README-家教平台改造说明.md)

## 项目来源

基于 [roncoo-education](https://github.com/roncoo/roncoo-education) 在线教育开源系统二次开发, 已对教学/订单/审核/匹配/防护等核心功能做了大量重写与扩展, 改造为家教撮合场景。

## License

继承上游开源协议。
