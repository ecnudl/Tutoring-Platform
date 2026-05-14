<template>
<div class="about-page">
<Head>
  <Title>{{ siteBrand }} - 公司简介</Title>
  <Meta name="description" :content="'关于 ' + siteBrand + ' - 公司简介'" />
</Head>

<div class="container about-wrapper">
  <div class="crumbs">
    <NuxtLink to="/">首页</NuxtLink>
    <span class="sep">›</span>
    <span>公司简介</span>
  </div>

  <article class="about-card">
    <!-- Markdown 渲染: admin sys_config 有值优先 admin's, 否则用内置默认 markdown -->
    <div class="about-body about-html" v-html="renderedHtml"></div>

    <!-- 联系方式 — 始终用 sys_config 动态值, 不进 markdown (单一来源) -->
    <h2 class="contact-title">联系我们</h2>
    <div class="contact">
      <div><strong>客服热线：</strong>{{ hotline }}</div>
      <div v-if="csEmail"><strong>客服邮箱：</strong>{{ csEmail }}</div>
      <div v-if="csWechat"><strong>客服微信：</strong>{{ csWechat }}</div>
      <div v-if="workTime"><strong>工作时间：</strong>{{ workTime }}</div>
    </div>
  </article>
</div>
</div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSiteConfig } from '~/composables/useSiteConfig'
import { HELP_ARTICLES_MD } from '~/composables/helpArticlesMd'
import { renderMarkdown } from '~/composables/useMarkdown'
const { config, load } = useSiteConfig()

const markdownSource = computed(() => {
  const adminMd = (config.value.siteAboutIntroHtml || '').trim()
  return adminMd || HELP_ARTICLES_MD['siteAboutIntroHtml'] || ''
})
const renderedHtml = computed(() => renderMarkdown(markdownSource.value))
const siteBrand = computed(() => config.value.siteBrandName || '591家教网')
const hotline = computed(() => config.value.siteHotline || '客服热线：待配置')
const csEmail = computed(() => config.value.siteCsEmail || '')
const csWechat = computed(() => config.value.siteCsWechat || '')
const workTime = computed(() => config.value.siteWorkTime || '')

onMounted(() => { load() })
</script>

<style scoped>
.about-page { min-height: 100vh; background: #f5f7fa; padding: 32px 0 60px; }
.about-wrapper { max-width: 820px; margin: 0 auto; padding: 0 20px; }
.crumbs { font-size: 13px; color: #64748b; margin-bottom: 16px; }
.crumbs a { color: #64748b; text-decoration: none; }
.crumbs a:hover { color: var(--color-primary); }
.crumbs .sep { margin: 0 6px; color: #cbd5e1; }

.about-card {
  background: #fff;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  padding: 40px 48px 48px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.03);
}
.about-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 20px;
  text-align: center;
  letter-spacing: 2px;
}
.about-body h2 {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-primary, #163B6B);
  margin: 28px 0 12px;
  padding-left: 10px;
  border-left: 4px solid var(--color-primary, #163B6B);
}
.about-body p, .about-body li { color: #334155; line-height: 1.9; font-size: 15px; }
.about-lead {
  background: #f8fafc;
  padding: 18px 22px;
  border-radius: 8px;
  font-size: 15px !important;
}
.about-lead strong { color: #f97316; }
.about-body ul { padding-left: 20px; margin: 10px 0; }
.about-body li { margin-bottom: 8px; }
.contact {
  background: #f8fafc;
  padding: 16px 22px;
  border-radius: 8px;
  line-height: 2;
}
.about-footnote {
  margin-top: 40px;
  font-size: 12px;
  color: #94a3b8;
  border-top: 1px dashed #e2e8f0;
  padding-top: 12px;
}
.about-footnote code {
  background: #f1f5f9;
  padding: 1px 6px;
  border-radius: 3px;
  font-family: ui-monospace, monospace;
}
.about-html :deep(h1), .about-html :deep(h2), .about-html :deep(h3) { color: var(--color-primary, #163B6B); }
.about-html :deep(p) { line-height: 1.9; }

@media (max-width: 640px) {
  .about-card { padding: 24px 16px; }
  .about-title { font-size: 22px; }
}
</style>
