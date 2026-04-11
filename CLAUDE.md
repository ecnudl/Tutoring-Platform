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

### 3. 重启服务

找到旧进程并重启：

```bash
rt -p 591jiajiao exec "ps aux | grep 'index.mjs' | grep -v grep"
rt -p 591jiajiao exec "kill <旧PID> && sleep 1 && cd /home/ubuntu/roncoo-education-web && nohup node .output/server/index.mjs > /home/ubuntu/logs/nuxt-web.log 2>&1 & echo 'started'"
```

确认启动成功：

```bash
rt -p 591jiajiao exec "cat /home/ubuntu/logs/nuxt-web.log"
```

看到 `Listening on http://[::]:3000` 即表示成功，刷新网页查看效果。

## 项目结构（关键目录）

- `pages/` — 页面组件（index.vue 为首页）
- `components/` — 公共组件（SiteHeader, SiteFooter, SiteLogo 等）
- `stores/` — Pinia 状态管理（city.ts, user.ts 等）
- `composables/` — 组合式函数（useCityData 等）
- `layouts/` — 布局组件
- `public/` — 静态资源
