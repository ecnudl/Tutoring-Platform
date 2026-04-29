<template>
  <div class="cn-page">
    <Head>
      <Title>联系我们 - 591家教网</Title>
      <Meta name="description" content="591 家教网联系方式: 客服热线、客服微信、邮箱、工作时间。" />
    </Head>

    <div class="container cn-crumb">
      <NuxtLink to="/">首页</NuxtLink>
      <span class="sep">›</span>
      <span class="cur">联系我们</span>
    </div>

    <div class="container cn-wrap">
      <header class="cn-head">
        <div class="cn-eyebrow">
          <span class="cn-rule"></span>
          591 家教 · 联系我们
        </div>
        <h1 class="cn-h">联系我们</h1>
        <p class="cn-lead">如您对家教需求有任何疑问, 或希望了解平台规则, 欢迎通过下方方式与我们联系。</p>
      </header>

      <div class="cn-grid">
        <!-- 客服微信 二维码 -->
        <section class="cn-card cn-qr-card">
          <div class="cn-card-eyebrow">客服微信</div>
          <div class="cn-qr-frame">
            <img src="/wechat-qr.png" alt="591家教 客服微信" class="cn-qr-img" />
          </div>
          <div class="cn-qr-hint">扫码添加客服微信, 备注 "请家教 / 做家教"</div>
          <div class="cn-qr-id" v-if="csWechat">
            <span class="lbl">微信号</span>
            <span class="val mono">{{ csWechat }}</span>
          </div>
        </section>

        <!-- 电话 / 邮箱 / 工作时间 -->
        <section class="cn-card cn-info-card">
          <div class="cn-card-eyebrow">联系方式</div>

          <div class="cn-row">
            <div class="cn-row-label">客服热线</div>
            <a :href="`tel:${hotline}`" class="cn-phone">{{ hotline }}</a>
          </div>

          <div class="cn-row" v-if="csEmail">
            <div class="cn-row-label">客服邮箱</div>
            <a :href="`mailto:${csEmail}`" class="cn-mail">{{ csEmail }}</a>
          </div>

          <div class="cn-row">
            <div class="cn-row-label">工作时间</div>
            <span class="cn-time">{{ workTime }}</span>
          </div>

          <div class="cn-tip-block">
            <div class="cn-tip-title">您也可以</div>
            <div class="cn-actions">
              <NuxtLink to="/qjj" class="cn-btn cn-btn-primary">提交家教需求</NuxtLink>
              <NuxtLink to="/jy" class="cn-btn">浏览教员库</NuxtLink>
              <NuxtLink to="/help" class="cn-btn">帮助中心</NuxtLink>
            </div>
          </div>
        </section>
      </div>

      <div class="cn-footnote">
        若需投诉、申诉或其他业务合作, 请通过客服微信留言, 我们会在 1 个工作日内回复。
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSiteConfig } from '~/composables/useSiteConfig'

const { config, load } = useSiteConfig()

const hotline = computed(() => config.value.siteHotline || '13795420591')
const csWechat = computed(() => config.value.siteCsWechat || '')
const csEmail = computed(() => config.value.siteCsEmail || '')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00 — 21:00')

onMounted(() => { load() })
</script>

<style scoped>
.cn-page { background: var(--color-bg, #f5f6f8); min-height: 80vh; padding-bottom: 80px; }

/* breadcrumb */
.cn-crumb { padding-top: 18px; font-size: 13px; color: #64748b; }
.cn-crumb a { color: #64748b; text-decoration: none; transition: color .15s; }
.cn-crumb a:hover { color: var(--color-primary, #163B6B); }
.cn-crumb .sep { margin: 0 6px; color: #cbd5e1; }
.cn-crumb .cur { color: var(--color-primary, #163B6B); font-weight: 600; }

.cn-wrap { max-width: var(--content-width, 1200px); margin: 20px auto 0; padding: 0 20px; }

/* head */
.cn-head {
  background: #fff;
  border-radius: 12px;
  padding: 30px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
}
.cn-head::before {
  content: '';
  position: absolute; top: 0; left: 0; right: 0; height: 3px;
  background: linear-gradient(to right, var(--color-primary, #163B6B), #264e82, #d97706);
}
.cn-eyebrow {
  display: flex; align-items: center; gap: 12px;
  font-size: 12px; letter-spacing: 4px; font-weight: 600;
  color: var(--color-primary, #163B6B);
  margin-bottom: 12px;
}
.cn-rule { width: 30px; height: 2px; background: var(--color-primary, #163B6B); }
.cn-h {
  font-size: 26px; font-weight: 700; letter-spacing: 4px;
  color: var(--color-primary, #163B6B);
  margin: 0 0 12px;
}
.cn-lead {
  font-size: 14px; color: #475569; line-height: 1.85; margin: 0;
  max-width: 720px;
}

/* grid */
.cn-grid {
  display: grid;
  grid-template-columns: 360px 1fr;
  gap: 20px;
  align-items: stretch;
}

/* card base */
.cn-card {
  background: #fff;
  border-radius: 12px;
  padding: 28px 32px 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}
.cn-card-eyebrow {
  font-size: 12px; letter-spacing: 4px; font-weight: 700;
  color: var(--color-primary, #163B6B);
  margin-bottom: 18px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #e5e7eb;
}

/* QR card */
.cn-qr-card {
  align-items: center;
  text-align: center;
}
.cn-qr-card .cn-card-eyebrow { width: 100%; }
.cn-qr-frame {
  width: 220px; height: 220px;
  background: linear-gradient(180deg, #f8fafc 0%, #fff 100%);
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 12px;
  display: flex; align-items: center; justify-content: center;
  margin: 6px 0 18px;
  box-shadow: 0 6px 20px -10px rgba(31, 78, 140, 0.18);
}
.cn-qr-img {
  width: 100%; height: 100%;
  object-fit: contain;
}
.cn-qr-hint {
  font-size: 13px; color: #475569;
  letter-spacing: 0.5px;
  margin-bottom: 12px;
}
.cn-qr-id {
  display: flex; align-items: center; gap: 10px;
  background: #f8fafc;
  padding: 8px 14px;
  border-radius: 6px;
  font-size: 13px;
}
.cn-qr-id .lbl { color: #94a3b8; letter-spacing: 1px; font-size: 12px; }
.cn-qr-id .val { color: #111827; font-weight: 600; }
.mono {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  letter-spacing: 0.5px;
}

/* info card */
.cn-info-card { gap: 0; }
.cn-row {
  display: grid;
  grid-template-columns: 90px 1fr;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px dashed #f1f5f9;
}
.cn-row:last-of-type { border-bottom: none; }
.cn-row-label {
  font-size: 13px; color: #64748b;
  letter-spacing: 2px;
  font-weight: 600;
}
.cn-phone {
  font-size: 22px; font-weight: 700;
  color: #d97706;
  font-family: Georgia, serif;
  letter-spacing: 1px;
  text-decoration: none;
  transition: color 0.15s;
}
.cn-phone:hover { color: #b45309; }
.cn-mail {
  font-size: 14px;
  color: var(--color-primary, #163B6B);
  text-decoration: none;
}
.cn-mail:hover { text-decoration: underline; }
.cn-time {
  font-size: 14px; color: #475569;
  letter-spacing: 0.3px;
}

.cn-tip-block {
  margin-top: 22px;
  padding-top: 22px;
  border-top: 1px dashed #e5e7eb;
}
.cn-tip-title {
  font-size: 13px; color: var(--color-primary, #163B6B);
  font-weight: 700; letter-spacing: 4px;
  margin-bottom: 12px;
}
.cn-actions {
  display: flex; flex-wrap: wrap; gap: 10px;
}
.cn-btn {
  display: inline-flex; align-items: center;
  padding: 10px 22px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #475569;
  text-decoration: none;
  transition: all 0.15s;
  background: #fff;
}
.cn-btn:hover {
  border-color: var(--color-primary, #163B6B);
  color: var(--color-primary, #163B6B);
}
.cn-btn-primary {
  background: var(--color-primary, #163B6B);
  color: #fff;
  border-color: var(--color-primary, #163B6B);
}
.cn-btn-primary:hover {
  background: #264e82;
  color: #fff;
}

.cn-footnote {
  margin-top: 20px;
  text-align: center;
  font-size: 13px;
  color: var(--color-text-muted, #94a3b8);
  letter-spacing: 0.3px;
}

/* responsive */
@media (max-width: 900px) {
  .cn-grid { grid-template-columns: 1fr; }
}
@media (max-width: 768px) {
  .cn-head { padding: 22px 18px; }
  .cn-h { font-size: 22px; letter-spacing: 2px; }
  .cn-card { padding: 22px 18px; }
  .cn-row { grid-template-columns: 80px 1fr; }
  .cn-phone { font-size: 18px; }
  .cn-qr-frame { width: 180px; height: 180px; }
}
</style>
