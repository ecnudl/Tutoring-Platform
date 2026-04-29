<template>
<div class="bt-page">
  <Head>
    <Title>为什么选择591家教网 · 期待您来做老师</Title>
    <Meta name="description" content="591家教网邀您加入教员队伍 — 透明分发、规则清晰、续单课时费 100% 归教员。" />
  </Head>

  <!-- ============ 面包屑 ============ -->
  <div class="container bt-crumb">
    <NuxtLink to="/">首页</NuxtLink>
    <span class="sep">›</span>
    <span class="cur">做老师</span>
  </div>

  <!-- ============ Hero（受 .container 限宽，与顶栏同宽，与导航留白） ============ -->
  <div class="container bt-hero-shell">
    <article class="bt-hero">
      <div class="bt-hero-decor" aria-hidden="true"></div>

      <div class="bt-hero-content">
        <div class="bt-eyebrow">
          <span class="dot"></span>
          591家教网 · 教员入驻
        </div>

        <h1 class="bt-hero-title">期待您来做老师</h1>

        <div class="bt-hero-en">
          <em>Welcome&nbsp;to&nbsp;be&nbsp;a</em>
          <strong>Tutor.</strong>
        </div>

        <div class="bt-hero-meta">
          <span>诚信</span>
          <span class="bullet"></span>
          <span>专业</span>
          <span class="bullet"></span>
          <span>透明</span>
        </div>
      </div>

      <!-- 装饰印章 -->
      <div class="bt-hero-seal" aria-hidden="true">
        <svg viewBox="0 0 220 220" xmlns="http://www.w3.org/2000/svg">
          <defs>
            <path id="bt-curve" d="M 110 110 m -82 0 a 82 82 0 1 1 164 0 a 82 82 0 1 1 -164 0"/>
          </defs>
          <circle cx="110" cy="110" r="100" fill="none" stroke="#F59E0B" stroke-width="1.5" stroke-dasharray="2 5" opacity="0.85"/>
          <circle cx="110" cy="110" r="78" fill="none" stroke="rgba(255,255,255,.32)" stroke-width="1"/>
          <text fill="rgba(255,255,255,.55)" font-size="9" letter-spacing="3">
            <textPath href="#bt-curve" startOffset="0">591JIAJIAO · TUTOR INVITATION · 自2024 · </textPath>
          </text>
          <g transform="translate(110 110)">
            <text text-anchor="middle" y="6" fill="#FCD34D" font-size="44" font-weight="700" letter-spacing="2">591</text>
            <text text-anchor="middle" y="32" fill="rgba(255,255,255,.7)" font-size="11" letter-spacing="6">JIAJIAO</text>
          </g>
        </svg>
      </div>
    </article>
  </div>

  <!-- ============ 为什么选择 591家教网?（单行 · 艺术化 591家教网） ============ -->
  <section class="container bt-why">
    <header class="bt-why-head">
      <div class="bt-section-rule" aria-hidden="true"></div>

      <h2 class="bt-why-title">
        <span class="prefix">为什么选择</span><span class="brand">
          <span class="brand-num">591</span><span class="brand-zh">家教网</span><svg class="brush" viewBox="0 0 260 18" preserveAspectRatio="none" aria-hidden="true">
            <path d="M3 12 Q 60 3 130 8 T 257 7" stroke="#F59E0B" stroke-width="3.5" stroke-linecap="round" fill="none"/>
          </svg></span><span class="qmark">?</span>
      </h2>

      <p class="bt-why-sub">九条朴素的理由，把您的时间交给一个值得托付的平台。</p>
    </header>

    <ol class="bt-reasons">
      <li v-for="(it, i) in reasons" :key="i" class="bt-reason"
          :style="{ animationDelay: (0.08 + i * 0.04) + 's' }">
        <div class="bt-reason-num">{{ String(i+1).padStart(2,'0') }}</div>
        <div class="bt-reason-body">
          <h3 class="bt-reason-title">{{ it.title }}</h3>
          <p class="bt-reason-desc">{{ it.desc }}</p>
        </div>
      </li>
    </ol>
  </section>

  <!-- ============ CTA ============ -->
  <section class="container bt-cta">
    <div class="bt-cta-card">
      <div class="bt-cta-words">
        <p class="kicker">下一步</p>
        <h3 class="bt-cta-h">填写资料 · 上传证件 · 等待审核 · 开始接单</h3>
        <p class="bt-cta-fine">中介费一次性结清（首次课时费金额）；之后续单课时费由家长直接结算给您（不经平台），长期无负担。</p>
      </div>
      <NuxtLink to="/register/teacher" class="bt-cta-btn">
        <span>现在就注册教员</span>
        <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
          <path d="M5 12h14M13 6l6 6-6 6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </NuxtLink>
    </div>
  </section>
</div>
</template>

<script setup>
const reasons = [
  { title: '正规平台 · 安全有保障', desc: '工商注册登记，正规合法可查询，受平台规则与法律双重约束。' },
  { title: '上海本地深耕',         desc: '专注沪上一对一上门 / 在线家教，需求集中，匹配效率更高。' },
  { title: '一次注册 · 全城匹配',  desc: '注册一次保留个人信息，跟随平台扩张自由切换城市。' },
  { title: '自研平台 · 持续优化',  desc: '所有功能由团队自研，反馈直达开发，一周内可上线改进。' },
  { title: '订单透明公开',         desc: '已发布的家教需求所有合规教员都能完整看到，机会均等。' },
  { title: '信息真实可靠',         desc: '每一位学员/家长信息逐条核实，确保您接到真实有效的需求。' },
  { title: '诚信服务 · 反馈通道',  desc: '提供诚信优质服务，纠纷与建议有专人响应，可投诉可申诉。' },
  { title: '透明中介费 · 一次性结清', desc: '中介费一次性收取(等同首次课时费金额), 上 1 次课减半, 2 次起不退; 续单课时费 100% 归您, 平台不再收取。' },
  { title: '隐私保护 · 严格脱敏',  desc: '注册资料采取保密措施，对外展示均经脱敏处理，绝不外泄。' }
]
</script>

<style scoped>
/* ============================================
   面包屑
   ============================================ */
.bt-page { padding-bottom: 32px; }

.bt-crumb {
  margin-top: 18px;
  font-size: 13px;
  color: var(--color-text-secondary);
}
.bt-crumb a {
  color: var(--color-text-secondary);
  transition: color var(--transition-fast);
}
.bt-crumb a:hover { color: var(--color-primary); }
.bt-crumb .sep { margin: 0 8px; color: var(--color-text-muted); }
.bt-crumb .cur { color: var(--color-text); font-weight: 500; }

/* ============================================
   Hero - 受 .container 限宽，与顶栏同宽
   ============================================ */
.bt-hero-shell { margin-top: 12px; }

.bt-hero {
  position: relative;
  background: linear-gradient(135deg, #1F4E8C 0%, #2A6AB5 50%, #163B6B 100%);
  border-radius: 14px;
  padding: 36px 56px;
  display: grid;
  grid-template-columns: 1fr auto;
  align-items: center;
  gap: 32px;
  overflow: hidden;
  box-shadow: 0 16px 50px -22px rgba(31, 78, 140, 0.55);
  animation: bt-rise 0.6s cubic-bezier(0.22, 1, 0.36, 1) both;
}

/* 蓝图风格的浅色斜纹底纹 */
.bt-hero-decor {
  position: absolute;
  inset: 0;
  background:
    repeating-linear-gradient(
      135deg,
      transparent 0 14px,
      rgba(255, 255, 255, 0.04) 14px 15px
    ),
    radial-gradient(circle at 18% 30%, rgba(252, 211, 77, 0.18), transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(252, 211, 77, 0.10), transparent 50%);
  pointer-events: none;
}

.bt-hero-content {
  position: relative;
  z-index: 2;
  color: #fff;
}

.bt-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 5px 12px;
  border-radius: 999px;
  background: rgba(252, 211, 77, 0.14);
  border: 1px solid rgba(252, 211, 77, 0.32);
  font-size: 12px;
  letter-spacing: 1px;
  color: #FCD34D;
  margin-bottom: 14px;
}
.bt-eyebrow .dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: #F59E0B;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.22);
}

.bt-hero-title {
  font-size: 40px;
  font-weight: 700;
  letter-spacing: 5px;
  line-height: 1.15;
  color: #fff;
  margin: 0 0 10px;
  text-shadow: 0 1px 0 rgba(0, 0, 0, 0.12);
}

.bt-hero-en {
  display: flex;
  align-items: baseline;
  gap: 8px;
  font-family: 'EB Garamond', 'Times New Roman', serif;
  font-size: 19px;
  letter-spacing: 1px;
  color: #FCD34D;
  margin-bottom: 16px;
}
.bt-hero-en em {
  font-style: italic;
  color: rgba(255, 255, 255, 0.78);
  font-weight: 400;
}
.bt-hero-en strong {
  font-weight: 600;
  letter-spacing: 2px;
}

.bt-hero-meta {
  display: inline-flex;
  align-items: center;
  gap: 14px;
  padding: 9px 18px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 4px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 4px;
}
.bt-hero-meta .bullet {
  width: 3px; height: 3px; border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
}

.bt-hero-seal {
  position: relative;
  z-index: 2;
  width: 156px;
  height: 156px;
  flex-shrink: 0;
  opacity: 0.95;
  animation: bt-spin 60s linear infinite;
}
@keyframes bt-spin {
  from { transform: rotate(0); }
  to   { transform: rotate(360deg); }
}

@keyframes bt-rise {
  from { opacity: 0; transform: translateY(18px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ============================================
   为什么选择 591家教网?
   ============================================ */
.bt-why {
  margin-top: 48px;
}

.bt-why-head {
  position: relative;
  margin-bottom: 24px;
}

.bt-section-rule {
  width: 36px;
  height: 3px;
  background: var(--color-accent);
  border-radius: 2px;
  margin-bottom: 14px;
}

.bt-why-title {
  font-size: 30px;
  font-weight: 700;
  letter-spacing: 3px;
  color: var(--color-text);
  margin: 0;
  display: flex;
  align-items: baseline;
  gap: 4px;
  flex-wrap: nowrap;
  white-space: nowrap;
  line-height: 1.3;
}
.bt-why-title .prefix {
  color: var(--color-text);
  margin-right: 18px;
}
.bt-why-title .brand {
  position: relative;
  display: inline-flex;
  align-items: baseline;
  color: var(--color-primary);
}
.bt-why-title .brand-num {
  font-size: 46px;
  font-weight: 800;
  letter-spacing: 0;
  line-height: 1;
  position: relative;
  top: 4px;
  margin-right: 4px;
  background: linear-gradient(135deg, #1F4E8C 30%, #2A6AB5 70%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  font-feature-settings: "tnum" 1;
}
.bt-why-title .brand-zh {
  font-weight: 700;
  letter-spacing: 4px;
}
.bt-why-title .brush {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 14px;
  pointer-events: none;
  filter: drop-shadow(0 1px 0 rgba(245, 158, 11, 0.18));
}
.bt-why-title .qmark {
  margin-left: 10px;
  color: var(--color-accent);
  font-weight: 700;
  font-size: 32px;
  position: relative;
  top: 2px;
}

.bt-why-sub {
  font-size: 15px;
  color: var(--color-text-secondary);
  letter-spacing: 1px;
  margin: 16px 0 0;
}

/* ============================================
   九条理由 — 杂志目录式
   ============================================ */
.bt-reasons {
  list-style: none;
  padding: 0;
  margin: 28px 0 0;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-top: 1px solid var(--color-border-light);
  border-left: 1px solid var(--color-border-light);
}

.bt-reason {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 16px;
  padding: 20px 22px;
  background: var(--color-surface);
  border-right: 1px solid var(--color-border-light);
  border-bottom: 1px solid var(--color-border-light);
  transition: background 0.2s ease, transform 0.2s ease;
  opacity: 0;
  animation: bt-fade-up 0.5s cubic-bezier(0.22, 1, 0.36, 1) forwards;
}

.bt-reason:hover {
  background: var(--color-primary-lighter);
  transform: translateY(-2px);
}
.bt-reason:hover .bt-reason-num {
  color: var(--color-accent);
}
.bt-reason:hover .bt-reason-title {
  color: var(--color-primary-dark);
}

.bt-reason-num {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary);
  letter-spacing: 1px;
  font-feature-settings: "tnum" 1;
  font-family: 'EB Garamond', Georgia, serif;
  font-style: italic;
  line-height: 1.1;
  padding-top: 2px;
  transition: color 0.2s ease;
}

.bt-reason-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  margin: 0 0 6px;
  letter-spacing: 0.5px;
  transition: color 0.2s ease;
}

.bt-reason-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.7;
}

@keyframes bt-fade-up {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ============================================
   CTA
   ============================================ */
.bt-cta { margin-top: 40px; }

.bt-cta-card {
  position: relative;
  background:
    linear-gradient(135deg, rgba(31, 78, 140, 0.04) 0%, rgba(245, 158, 11, 0.06) 100%),
    var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 24px 36px;
  display: grid;
  grid-template-columns: 1fr auto;
  align-items: center;
  gap: 28px;
}
.bt-cta-card::before {
  content: "";
  position: absolute;
  left: 0; top: 28px; bottom: 28px;
  width: 4px;
  background: var(--color-accent);
  border-radius: 0 4px 4px 0;
}

.bt-cta-words .kicker {
  font-size: 11px;
  letter-spacing: 4px;
  color: var(--color-accent-dark);
  font-weight: 600;
  margin: 0 0 6px;
  text-transform: uppercase;
}
.bt-cta-h {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--color-text);
  margin: 0 0 4px;
}
.bt-cta-fine {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin: 0;
}

.bt-cta-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: var(--color-primary);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 2px;
  padding: 13px 28px;
  border-radius: 8px;
  white-space: nowrap;
  transition: all 0.2s ease;
  box-shadow: 0 6px 20px -10px rgba(31, 78, 140, 0.6);
  position: relative;
  overflow: hidden;
}
.bt-cta-btn::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, transparent 30%, rgba(252, 211, 77, 0.2) 50%, transparent 70%);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}
.bt-cta-btn:hover {
  background: var(--color-primary-dark);
  color: #fff;
  transform: translateY(-1px);
  box-shadow: 0 12px 30px -12px rgba(31, 78, 140, 0.65);
}
.bt-cta-btn:hover::after { transform: translateX(100%); }
.bt-cta-btn svg { transition: transform 0.2s ease; }
.bt-cta-btn:hover svg { transform: translateX(3px); }

/* ============================================
   响应式
   ============================================ */
@media (max-width: 960px) {
  .bt-reasons { grid-template-columns: repeat(2, 1fr); }
  .bt-hero { padding: 40px 32px; grid-template-columns: 1fr; }
  .bt-hero-seal { display: none; }
  .bt-hero-title { font-size: 36px; letter-spacing: 4px; }
  .bt-why-title { font-size: 26px; flex-wrap: wrap; white-space: normal; }
  .bt-why-title .brand-num { font-size: 38px; }
  .bt-cta-card { grid-template-columns: 1fr; padding: 28px 24px; }
  .bt-cta-h { font-size: 18px; letter-spacing: 1px; }
}

@media (max-width: 560px) {
  .bt-reasons { grid-template-columns: 1fr; }
  .bt-hero { padding: 32px 22px; }
  .bt-hero-title { font-size: 28px; letter-spacing: 2px; }
  .bt-why-title .brand-num { font-size: 30px; top: 2px; }
  .bt-cta-btn { padding: 14px 22px; font-size: 14px; letter-spacing: 1px; }
}
</style>
