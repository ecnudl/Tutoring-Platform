# 591家教网项目

## 服务器信息

- Profile: `591jiajiao`
- 主机: `ubuntu@43.161.234.172`
- 远程项目路径: `/home/ubuntu/roncoo-education-web`
- 本地挂载点: `/Users/dulin/remote/591jiajiao`
- 网站地址: http://591jiajiao.cn

## 技术栈

- Nuxt 3 (SSR 模式) + Vue 3 + Element Plus + Pinia
- 构建产物: `.output/server/index.mjs`，监听端口 3000

## 运行架构

所有后端服务都由 systemd 管理（`Restart=on-failure`、开机自启）：

| 服务 | unit | 端口 | 说明 |
|---|---|---|---|
| Nuxt 前端 | `roncoo-web.service` | 3000 | 本项目 |
| Gateway | `roncoo-gateway.service` | 7700 | Spring Cloud Gateway |
| user-service | `roncoo-user.service` | 7720 | 用户/注册/短信 API |
| system-service | `roncoo-system.service` | 7710 | 系统配置 API |

依赖组件 (Docker 容器，`restart=unless-stopped`)：`tutor-nacos`、`tutor-mysql`、`tutor-redis`、`tutor-minio`。

## 工作流程

每次修改源码后，必须按顺序完成以下三步：**提交 → 构建 → 重启**。

### 1. Git 提交（每次改完代码必须 commit）

```bash
rt -p 591jiajiao exec "cd /home/ubuntu/roncoo-education-web && git add -A && git commit -m '描述本次修改'"
```

- 每次修改完代码都要立即 commit，方便回滚复原
- commit message 用中文简要描述改了什么

### 2. 构建项目

```bash
rt -p 591jiajiao exec --bg --name nuxt-build "cd /home/ubuntu/roncoo-education-web && npm run build 2>&1"
```

等待构建完成（约 20-30 秒），通过日志确认 `Build complete`：

```bash
rt -p 591jiajiao logs rt_591jiajiao_bg_nuxt-build
```

### 3. 重启服务（systemd 管控）

```bash
rt -p 591jiajiao exec "sudo systemctl restart roncoo-web"
rt -p 591jiajiao exec "sudo systemctl status roncoo-web --no-pager"
```

或查看日志确认启动：

```bash
rt -p 591jiajiao exec "tail -20 /home/ubuntu/logs/nuxt-web.log"
```

看到 `Listening on http://[::]:3000` 即表示成功，刷新网页查看效果。

## 后端服务常用运维

```bash
# 查看全部 roncoo-* 单元状态
rt -p 591jiajiao exec "sudo systemctl list-units 'roncoo-*' --no-pager"
# 重启单个或全部
rt -p 591jiajiao exec "sudo systemctl restart roncoo-user"
rt -p 591jiajiao exec "sudo systemctl restart 'roncoo-*'"
# 跟随日志
rt -p 591jiajiao exec "tail -f /home/ubuntu/logs/user-service.log"
```

systemd unit 文件位于 `/etc/systemd/system/roncoo-{web,gateway,user,system}.service`。

## 项目结构（关键目录）

- `pages/` — 页面组件（index.vue 为首页）
- `components/` — 公共组件（SiteHeader, SiteFooter, SiteLogo 等）
- `stores/` — Pinia 状态管理（city.ts, user.ts 等）
- `composables/` — 组合式函数（useCityData 等）
- `layouts/` — 布局组件
- `public/` — 静态资源
