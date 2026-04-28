<template>
<div class="bt-page">
  <Head>
    <Title>为什么选择591家教网 - 期待您来做老师</Title>
    <Meta name="description" content="591家教网 — 期待您来做老师。了解我们的优势，注册教员，开始接单。" />
  </Head>

  <!-- ========== Hero ========== -->
  <div class="bt-hero">
    <div class="bt-hero-inner">
      <div class="bt-hero-text">
        <h1>期待您来做老师</h1>
        <div class="bt-hero-en">Welcome to be a tutor</div>
      </div>
      <div class="bt-hero-icon">
        <svg viewBox="0 0 64 64" width="100" height="100">
          <circle cx="32" cy="20" r="11" fill="#fde9c0" />
          <path d="M10 56c0-12 10-20 22-20s22 8 22 20" fill="#fde9c0" />
          <path d="M22 18l8 8 12-14" stroke="#f59e0b" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
    </div>
  </div>

  <!-- ========== 面包屑 ========== -->
  <div class="bt-crumb">
    <NuxtLink to="/">首页</NuxtLink> / <span>做老师</span>
  </div>

  <!-- ========== 主内容 ========== -->
  <section class="bt-main">
    <h2 class="bt-title">
      为什么<br />选择<br /><span class="brand">591家教网?</span>
    </h2>

    <ul class="bt-list">
      <li v-for="(it, i) in reasons" :key="i">
        <span class="dot"></span>
        <strong>{{ it.title }}</strong>
        <span class="desc">{{ it.desc }}</span>
      </li>
    </ul>

    <div class="bt-cta">
      <NuxtLink to="/register/teacher" class="cta-btn">现在就注册教员吧！</NuxtLink>
    </div>
  </section>
</div>
</template>

<script setup>
const reasons = [
  { title: '正规平台安全有保障', desc: '工商注册登记，正规合法可查询。' },
  { title: '上海本地深耕', desc: '专注沪上家教市场，主攻一对一上门 / 线上家教。' },
  { title: '一次注册全城匹配', desc: '注册一次保留个人信息，跟随站点扩张自由切换。' },
  { title: '自研平台', desc: '自主研发系统，持续优化信息服务体验。' },
  { title: '订单透明公开', desc: '已发布的需求信息，所有教员都能完整看到。' },
  { title: '信息真实可靠', desc: '学员/家长信息逐条核实，确保真实有效。' },
  { title: '诚信服务', desc: '提供诚信优质服务，欢迎反馈和投诉。' },
  { title: '不收教员中介费', desc: '教员在平台上接单不抽成，课时费由学员直接结算。' },
  { title: '隐私保护', desc: '对教员注册的相关信息采取保密措施，绝不外泄。' }
]
</script>

<style scoped>
.bt-page { min-height: 100vh; background: #fff; }

.bt-hero {
  background: linear-gradient(135deg, #fff7ed 0%, #fef3c7 100%);
  padding: 36px 0 24px;
  margin-bottom: 28px;
}
.bt-hero-inner {
  max-width: 1100px; margin: 0 auto; padding: 0 40px;
  display: flex; justify-content: space-between; align-items: center;
}
.bt-hero-text h1 {
  font-size: 40px; font-weight: 400; font-family: 黑体, "PingFang SC", sans-serif;
  margin: 0; color: #1f2937;
}
.bt-hero-en {
  font-size: 16px; color: #555; letter-spacing: 8px;
  font-family: 仿宋, 宋体, sans-serif; margin-top: 6px;
}
.bt-hero-icon { padding-right: 80px; }

.bt-crumb {
  max-width: 1100px; margin: 0 auto 16px; padding: 0 40px;
  font-size: 13px; color: #64748b;
}
.bt-crumb a { color: #f59e0b; }
.bt-crumb a:hover { color: #d97706; }

.bt-main { max-width: 900px; margin: 0 auto 60px; padding: 0 40px; }

.bt-title {
  font-size: 26px; line-height: 42px; font-weight: 600; color: #111;
  margin: 0 0 24px 30px;
}
.bt-title .brand { color: #f59e0b; }

.bt-list { list-style: none; padding: 0; margin: 0 0 40px; }
.bt-list li {
  position: relative;
  padding: 16px 16px 16px 32px;
  font-size: 15px;
  color: #334155;
  line-height: 1.8;
  border-bottom: 1px solid #f5f5f5;
}
.bt-list li:last-child { border-bottom: 0; }
.bt-list li .dot {
  position: absolute; left: 8px; top: 24px;
  width: 7px; height: 7px; border-radius: 50%; background: #f59e0b;
}
.bt-list li strong {
  font-weight: 600; color: #111; margin-right: 8px;
}
.bt-list li .desc { color: #475569; }

.bt-cta { text-align: center; margin: 30px 0 60px; }
.cta-btn {
  display: inline-block;
  background: #f59e0b; color: #fff;
  font-size: 18px; font-weight: 600;
  padding: 16px 80px; border-radius: 6px;
  letter-spacing: 4px;
  transition: background .15s;
}
.cta-btn:hover { background: #d97706; color: #fff; }

@media (max-width: 768px) {
  .bt-hero-inner { padding: 0 20px; }
  .bt-hero-text h1 { font-size: 28px; }
  .bt-hero-icon { display: none; }
  .bt-main { padding: 0 20px; }
  .bt-title { margin-left: 0; font-size: 22px; line-height: 36px; }
  .cta-btn { padding: 14px 40px; font-size: 16px; letter-spacing: 2px; }
}
</style>
