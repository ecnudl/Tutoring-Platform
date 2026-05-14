<template>
  <div class="hp-page">
    <Head>
      <Title>{{ article?.title || '帮助中心' }} - 591家教网</Title>
      <Meta name="description" :content="`${article?.title || '帮助中心'} - 591家教网`" />
    </Head>

    <div class="container hp-wrapper">
      <!-- 面包屑 -->
      <div class="hp-crumbs">
        <NuxtLink to="/">首页</NuxtLink>
        <span class="sep">›</span>
        <NuxtLink to="/help">帮助中心</NuxtLink>
        <span class="sep">›</span>
        <span>{{ article?.title || '文章' }}</span>
      </div>

      <!-- 不存在的 slug -->
      <div v-if="!article" class="hp-404">
        <h2>未找到文章</h2>
        <p>抱歉，该帮助文章不存在或已下线。</p>
        <NuxtLink to="/help" class="btn-primary">返回帮助中心</NuxtLink>
      </div>

      <!-- 正文 — Markdown 渲染. admin sys_config 有值优先 admin's, 否则用 helpArticlesMd 内置默认源 -->
      <div v-else class="hp-body">
        <article class="hp-article">
          <div class="hp-content hp-html" v-html="renderedHtml"></div>
        </article>

        <!-- 右侧 -->
        <aside class="hp-side">
          <div class="side-card">
            <div class="side-title">{{ sideCategory === 'student' ? '我是学员' : sideCategory === 'tutor' ? '我是教员' : '更多帮助' }}</div>
            <NuxtLink v-for="l in sideLinks" :key="l.href" :to="l.href" class="side-link" :class="{ current: l.href.endsWith(slug) }">
              {{ l.label }}
            </NuxtLink>
          </div>

          <div class="side-card side-card--blue">
            <div class="side-title light">客服联系</div>
            <div class="side-phone">{{ hotline }}</div>
            <div class="side-time">{{ workTime }}</div>
            <NuxtLink to="/qjj" class="side-btn">提交家教需求 →</NuxtLink>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useSiteConfig } from '~/composables/useSiteConfig'
import { HELP_ARTICLES, HELP_SIDE_LINKS } from '~/composables/helpArticles'
import { HELP_ARTICLES_MD } from '~/composables/helpArticlesMd'
import { renderMarkdown } from '~/composables/useMarkdown'

const route = useRoute()
const slug = computed(() => String(route.params.slug || ''))

const { config, load } = useSiteConfig()

const article = computed(() => HELP_ARTICLES[slug.value] || null)

// Markdown 源: admin sys_config 有值优先 admin's, 否则用内置默认 markdown
const markdownSource = computed(() => {
  if (!article.value) return ''
  const key = article.value.configKey
  const adminMd = ((config.value)[key] || '').trim()
  return adminMd || HELP_ARTICLES_MD[key] || ''
})

const renderedHtml = computed(() => renderMarkdown(markdownSource.value))

const hotline = computed(() => config.value.siteHotline || '13795420591')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00-21:00')
const sideCategory = computed(() => article.value?.category || 'common')
const sideLinks = computed(() => HELP_SIDE_LINKS[sideCategory.value] || HELP_SIDE_LINKS.common)

onMounted(() => { load() })
</script>

<style scoped>
/* 布局骨架（与 request-process 风格保持一致，以便后续统一主题） */
.hp-page { background: var(--color-bg, #f5f6f8); min-height: 100vh; padding: 30px 0 60px; }
.hp-wrapper { max-width: var(--content-width, 1200px); margin: 0 auto; padding: 0 20px; }

.hp-crumbs { font-size: 13px; color: #64748b; margin-bottom: 18px; }
.hp-crumbs a { color: #64748b; text-decoration: none; }
.hp-crumbs a:hover { color: var(--color-primary); }
.hp-crumbs .sep { margin: 0 6px; color: #cbd5e1; }

.hp-404 {
  background: #fff; padding: 80px 40px; border-radius: 12px; text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.hp-404 h2 { font-size: 22px; margin: 0 0 12px; }
.hp-404 p { color: #64748b; margin: 0 0 24px; }

.hp-body { display: grid; grid-template-columns: 1fr 280px; gap: 24px; align-items: start; }

.hp-article {
  background: #fff; border-radius: 12px; padding: 48px 56px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}
.hp-title {
  font-size: 30px; font-weight: 700; color: #111827; margin: 0 0 16px;
  padding-bottom: 20px; border-bottom: 2px solid var(--color-primary, #163B6B);
  width: fit-content; letter-spacing: 1px;
}
.hp-lead { font-size: 16px; line-height: 1.8; color: #374151; margin: 24px 0 36px; }
.hp-lead strong { color: var(--color-primary, #163B6B); }

.steps { display: flex; flex-direction: column; gap: 20px; margin-bottom: 40px; }
.step {
  display: flex; gap: 20px; background: #f8fafc;
  border-left: 3px solid var(--color-primary, #163B6B);
  border-radius: 6px; padding: 20px 24px; transition: box-shadow 0.2s;
}
.step:hover { box-shadow: 0 3px 14px rgba(22, 59, 107, 0.08); }
.step-num {
  flex-shrink: 0; width: 40px; height: 40px; border-radius: 50%;
  background: var(--color-primary, #163B6B); color: #fff;
  font-size: 18px; font-weight: 700; font-family: Georgia, serif;
  display: flex; align-items: center; justify-content: center;
}
.step-body h3 { font-size: 17px; font-weight: 600; color: #111827; margin: 0 0 8px; }
.step-body p { font-size: 14px; line-height: 1.75; color: #475569; margin: 0; }

.hp-h2 {
  font-size: 22px; font-weight: 700; color: #111827;
  margin: 36px 0 18px; padding-left: 12px;
  border-left: 4px solid var(--color-primary, #163B6B); line-height: 1;
}
.sec-intro { font-size: 14px; line-height: 1.8; color: #475569; margin: 0 0 14px; }

.hp-list { list-style: none; padding: 0; margin: 16px 0 28px; }
.hp-list li {
  position: relative; padding: 8px 0 8px 22px;
  color: #475569; font-size: 14px; line-height: 1.75;
}
.hp-list li::before {
  content: '✓'; position: absolute; left: 0;
  color: var(--color-primary, #163B6B); font-weight: 700;
}
.hp-list strong { color: #111827; }

.hp-qa { margin: 16px 0 28px; }
.hp-qa dt { font-size: 15px; font-weight: 600; color: #111827; margin-top: 18px; }
.hp-qa dt:first-child { margin-top: 0; }
.hp-qa dd {
  font-size: 14px; color: #475569; line-height: 1.75; margin: 6px 0 0 0;
  padding-left: 22px; border-left: 2px solid #e5e7eb;
}

.hp-pricetable { margin: 20px 0 28px; overflow-x: auto; }
.hp-pricetable table { width: 100%; border-collapse: collapse; font-size: 14px; }
.hp-pricetable th, .hp-pricetable td {
  padding: 10px 12px; border: 1px solid #e5e7eb; text-align: center;
}
.hp-pricetable th {
  background: var(--color-primary, #163B6B); color: #fff; font-weight: 600;
}
.hp-pricetable td:first-child { background: #f8fafc; font-weight: 600; color: #111827; }

.hp-cta {
  background: linear-gradient(135deg, #f0f6fd, #e8f0fa);
  border-radius: 8px; padding: 20px 24px; margin: 30px 0;
  color: #334155; font-size: 14px; line-height: 1.9; text-align: center;
}
.hp-cta :deep(strong) { color: var(--color-primary, #163B6B); font-family: Georgia, serif; font-size: 17px; }

.hp-actions {
  display: flex; gap: 16px; margin-top: 32px;
  padding-top: 24px; border-top: 1px dashed #e5e7eb;
}
.btn-primary, .btn-secondary {
  flex: 1; text-align: center; padding: 12px 20px; border-radius: 6px;
  font-size: 15px; font-weight: 600; text-decoration: none; transition: all 0.2s;
}
.btn-primary { background: var(--color-primary, #163B6B); color: #fff; }
.btn-primary:hover { background: #0f2a4d; transform: translateY(-1px); }
.btn-secondary {
  background: transparent; color: var(--color-primary, #163B6B);
  border: 1px solid var(--color-primary, #163B6B);
}
.btn-secondary:hover { background: var(--color-primary-lighter); }

.hp-side { position: sticky; top: 20px; display: flex; flex-direction: column; gap: 16px; }
.side-card { background: #fff; border-radius: 10px; padding: 20px 22px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04); }
.side-title { font-size: 15px; font-weight: 700; color: #111827; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.side-title.light { color: #fff; border-bottom-color: rgba(255,255,255,0.15); }
.side-link { display: block; font-size: 14px; color: #475569; padding: 8px 0; text-decoration: none; transition: color 0.2s; }
.side-link:hover { color: var(--color-primary); }
.side-link.current { color: var(--color-primary); font-weight: 600; }

.side-card--blue { background: var(--color-primary, #163B6B); color: #fff; text-align: center; }
.side-phone { font-family: Georgia, serif; font-size: 22px; font-weight: 700; letter-spacing: 1px; margin: 10px 0 4px; }
.side-time { font-size: 12px; color: rgba(255, 255, 255, 0.65); margin-bottom: 14px; }
.side-btn {
  display: inline-block; padding: 8px 22px;
  background: rgba(255, 255, 255, 0.15); color: #fff;
  border-radius: 20px; font-size: 13px; text-decoration: none; transition: background 0.2s;
}
.side-btn:hover { background: rgba(255, 255, 255, 0.25); }

/* admin 富文本样式 */
.hp-html :deep(h1) { font-size: 28px; font-weight: 700; color: #111827; margin: 0 0 20px; }
.hp-html :deep(h2) { font-size: 20px; font-weight: 700; color: #111827; margin: 28px 0 12px; padding-left: 12px; border-left: 4px solid var(--color-primary); }
.hp-html :deep(h3) { font-size: 16px; font-weight: 600; color: #111827; margin: 18px 0 8px; }
.hp-html :deep(p) { font-size: 14px; line-height: 1.8; color: #475569; margin: 8px 0; }
.hp-html :deep(ul), .hp-html :deep(ol) { padding-left: 24px; }
.hp-html :deep(li) { font-size: 14px; line-height: 1.8; color: #475569; margin: 4px 0; }
.hp-html :deep(strong) { color: #111827; }
.hp-html :deep(a) { color: var(--color-primary, #163B6B); text-decoration: underline; }
.hp-html :deep(blockquote) { border-left: 3px solid var(--color-primary, #163B6B); padding: 8px 16px; margin: 14px 0; background: #f8fafc; color: #475569; }
.hp-html :deep(hr) { border: none; border-top: 1px solid #e2e8f0; margin: 24px 0; }
.hp-html :deep(code) { background: #f1f5f9; padding: 1px 6px; border-radius: 3px; font-family: ui-monospace, "SF Mono", Consolas, monospace; font-size: 13px; color: #163B6B; }
.hp-html :deep(table) { border-collapse: collapse; width: 100%; margin: 16px 0; font-size: 13px; }
.hp-html :deep(th), .hp-html :deep(td) { border: 1px solid #e2e8f0; padding: 8px 10px; text-align: center; }
.hp-html :deep(th) { background: #f8fafc; font-weight: 600; color: #1F2937; }

@media (max-width: 768px) {
  .hp-body { grid-template-columns: 1fr; }
  .hp-article { padding: 28px 20px; }
  .hp-title { font-size: 24px; }
  .step { flex-direction: column; gap: 10px; }
  .hp-actions { flex-direction: column; }
  .hp-side { position: static; }
}
</style>
