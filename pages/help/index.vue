<template>
  <div class="help-wrapper">
    <div class="container">
      <!-- Hero + 分类导航 -->
      <div class="help-hero">
        <div class="hero-title">帮助中心</div>
        <div class="hero-sub">找答案 / 解惑 / 联系我们</div>
      </div>

      <div class="help-articles">
        <div class="art-col">
          <div class="art-title"><span class="dot dot-student"></span>我是学员</div>
          <NuxtLink v-for="a in studentArticles" :key="a.href" :to="a.href" class="art-link">
            <span class="art-arrow">›</span>{{ a.label }}
          </NuxtLink>
        </div>
        <div class="art-col">
          <div class="art-title"><span class="dot dot-tutor"></span>我是教员</div>
          <NuxtLink v-for="a in tutorArticles" :key="a.href" :to="a.href" class="art-link">
            <span class="art-arrow">›</span>{{ a.label }}
          </NuxtLink>
        </div>
        <div class="art-col">
          <div class="art-title"><span class="dot dot-common"></span>通用</div>
          <NuxtLink v-for="a in commonArticles" :key="a.href" :to="a.href" class="art-link">
            <span class="art-arrow">›</span>{{ a.label }}
          </NuxtLink>
        </div>
      </div>
    </div>

    <div class="container help-body">
      <aside class="help-sidebar">
        <div class="sidebar-title">帮助中心</div>
        <div class="sidebar-menu">
          <div
            v-for="(cat, idx) in categories"
            :key="idx"
            class="menu-item"
            :class="{ active: activeCategory === idx }"
            @click="activeCategory = idx"
          >
            <span class="menu-icon">{{ cat.icon }}</span>
            <span>{{ cat.name }}</span>
          </div>
        </div>
      </aside>

      <main class="help-main">
        <div class="main-header">
          <h2>{{ categories[activeCategory].name }}</h2>
        </div>

        <div class="faq-list">
          <div
            v-for="(item, idx) in categories[activeCategory].items"
            :key="idx"
            class="faq-card"
            :class="{ open: item.open }"
          >
            <div class="faq-q" @click="item.open = !item.open">
              <span class="q-badge">Q</span>
              <span class="q-text">{{ item.q }}</span>
              <span class="q-arrow" :class="{ rotated: item.open }">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
              </span>
            </div>
            <transition name="slide">
              <div class="faq-a" v-show="item.open">
                <div class="a-text" v-html="item.a"></div>
              </div>
            </transition>
          </div>
        </div>

        <div v-if="activeCategory === categories.length - 1 && categories.length > 0" class="contact-cards">
          <div class="contact-card">
            <div class="c-title">客服热线</div>
            <div class="c-value">{{ hotline }}</div>
            <div class="c-desc">{{ workTime }}</div>
          </div>
          <div class="contact-card">
            <div class="c-title">客服邮箱</div>
            <div class="c-value">{{ csEmail }}</div>
            <div class="c-desc">24小时内回复</div>
          </div>
          <div class="contact-card">
            <div class="c-title">在线客服</div>
            <div class="c-value">微信客服</div>
            <div class="c-desc">{{ csWechat ? `微信号 ${csWechat}` : '扫码添加客服微信' }}</div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { HELP_SIDE_LINKS } from '~/composables/helpArticles'

const studentArticles = HELP_SIDE_LINKS.student
const tutorArticles = HELP_SIDE_LINKS.tutor
const commonArticles = [
  { label: '收费方式', href: '/help/payment-method' },
  { label: '联系我们', href: '/about/contact' },
  { label: '服务号', href: '/about/wechat' },
  { label: '公司简介', href: '/about/intro' },
  { label: '免责声明', href: '/agreement/disclaimer' },
  { label: '隐私保护', href: '/agreement/privacy' }
]
import { useSiteConfig } from '~/composables/useSiteConfig'

const activeCategory = ref(0)
const categories = reactive([])

const { config, load } = useSiteConfig()
const hotline = computed(() => config.value.siteHotline || '13795420591')
const csEmail = computed(() => config.value.siteCsEmail || 'jiajiao591@126.com')
const csWechat = computed(() => config.value.siteCsWechat || '')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00-21:00')

// 答案文本 → HTML：包含"→"分段成列表，否则包 <p>
const toHtml = (text) => {
  if (!text) return ''
  if (text.includes('→')) {
    const items = text.split('→').map(s => s.trim()).filter(Boolean)
    return '<ul>' + items.map(i => `<li>${i}</li>`).join('') + '</ul>'
  }
  return `<p>${text.replace(/\n/g, '<br>')}</p>`
}

onMounted(async () => {
  load()
  try {
    const { get } = useApi()
    const res = await get('/system/api/faq/list')
    if (res?.code === 200 && Array.isArray(res.data) && res.data.length > 0) {
      const map = new Map()
      for (const it of res.data) {
        const key = it.category
        if (!map.has(key)) {
          map.set(key, { name: it.categoryLabel || key, icon: it.categoryIcon || '', items: [] })
        }
        map.get(key).items.push({ q: it.question, a: toHtml(it.answer), open: false })
      }
      categories.splice(0, categories.length, ...Array.from(map.values()))
    }
  } catch (e) { console.warn('[faq] load failed', e) }
})
</script>

<style scoped>
/* === Hero / 分类导航（新）=== */
.help-hero {
  background: linear-gradient(135deg, var(--color-primary, #163B6B), #264e82);
  color: #fff;
  padding: 40px 40px 44px;
  border-radius: 12px 12px 0 0;
  margin-top: var(--space-xl, 20px);
  text-align: center;
}
.hero-title { font-size: 28px; font-weight: 700; letter-spacing: 4px; }
.hero-sub { margin-top: 6px; font-size: 14px; opacity: 0.8; letter-spacing: 1px; }

.help-articles {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  background: #fff;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: 28px 0;
  margin-bottom: var(--space-2xl, 24px);
}
.art-col {
  padding: 0 36px;
  border-right: 1px solid #f1f5f9;
}
.art-col:last-child { border-right: none; }
.art-title {
  font-size: 16px; font-weight: 700; color: #111827;
  margin-bottom: 14px; display: flex; align-items: center; gap: 8px;
}
.dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; }
.dot-student { background: var(--color-primary, #163B6B); }
.dot-tutor   { background: #f59e0b; }
.dot-common  { background: #64748b; }
.art-link {
  display: flex; align-items: center; gap: 4px;
  padding: 7px 0;
  font-size: 14px; color: #475569;
  text-decoration: none;
  transition: color 0.2s;
}
.art-link:hover { color: var(--color-primary); }
.art-arrow { color: #cbd5e1; transition: color 0.2s; }
.art-link:hover .art-arrow { color: var(--color-primary); }

@media (max-width: 768px) {
  .help-hero { padding: 28px 20px; }
  .hero-title { font-size: 22px; }
  .help-articles { grid-template-columns: 1fr; padding: 18px 0; }
  .art-col { padding: 16px 24px; border-right: none; border-bottom: 1px solid #f1f5f9; }
  .art-col:last-child { border-bottom: none; }
}

/* === 原 FAQ 样式 === */
.help-wrapper {
  min-height: 100vh;
  background: var(--color-bg);
}

.help-body {
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-xl);
  max-width: var(--content-width);
  margin: 0 auto;
  padding: var(--space-2xl) var(--space-xl);
  display: flex;
  gap: var(--space-2xl);
  align-items: flex-start;
}

.help-sidebar {
  width: var(--sidebar-width);
  flex-shrink: 0;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  position: sticky;
  top: 72px;
}

.sidebar-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  padding: 18px var(--space-xl);
  border-bottom: 1px solid var(--color-border-light);
}

.sidebar-menu { padding: var(--space-sm) 0; }

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: var(--space-md) var(--space-xl);
  cursor: pointer;
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
  border-left: 3px solid transparent;
}
.menu-item:hover { background: var(--color-bg); color: var(--color-primary); }
.menu-item.active { background: var(--color-primary-lighter); color: var(--color-primary); font-weight: var(--font-weight-medium); border-left-color: var(--color-primary); }
.menu-icon { font-size: var(--font-size-xl); }

.help-main { flex: 1; min-width: 0; }

.main-header { margin-bottom: var(--space-xl); }
.main-header h2 {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin: 0;
  padding-bottom: var(--space-md);
  border-bottom: 2px solid var(--color-primary);
  display: inline-block;
}

.faq-list { display: flex; flex-direction: column; gap: var(--space-md); }

.faq-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: border-color var(--transition-fast);
}
.faq-card:hover { border-color: var(--color-primary); }

.faq-q {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-lg) var(--space-xl);
  cursor: pointer;
  transition: background var(--transition-fast);
}
.faq-q:hover { background: var(--color-bg); }

.q-badge {
  width: 24px;
  height: 24px;
  background: var(--color-primary);
  color: white;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
  flex-shrink: 0;
}
.q-text { flex: 1; font-size: var(--font-size-md); font-weight: var(--font-weight-medium); color: var(--color-text); }
.q-arrow { color: var(--color-text-muted); transition: transform 0.2s; display: flex; align-items: center; }
.q-arrow.rotated { transform: rotate(180deg); }

.faq-a { padding: 0 var(--space-xl) var(--space-lg) 56px; color: var(--color-text-secondary); font-size: var(--font-size-base); line-height: var(--line-height-relaxed); }

.a-text :deep(ul) { margin: 0; padding: 0; list-style: none; }
.a-text :deep(li) { position: relative; padding-left: var(--space-lg); margin: 6px 0; }
.a-text :deep(li):before { content: ''; position: absolute; left: 0; top: 10px; width: 6px; height: 6px; background: var(--color-primary); border-radius: 50%; }
.a-text :deep(p) { margin: 0; }
.a-text :deep(strong) { color: var(--color-primary); }

.slide-enter-active, .slide-leave-active { transition: all 0.2s ease; overflow: hidden; }
.slide-enter-from, .slide-leave-to { opacity: 0; max-height: 0; padding-top: 0; padding-bottom: 0; }

.contact-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-lg); margin-top: var(--space-2xl); }
.contact-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  text-align: center;
  transition: border-color var(--transition-fast);
}
.contact-card:hover { border-color: var(--color-primary); }
.c-title { font-size: var(--font-size-md); font-weight: var(--font-weight-semibold); color: var(--color-text); margin-bottom: var(--space-sm); }
.c-value { font-size: var(--font-size-lg); font-weight: var(--font-weight-semibold); color: var(--color-primary); margin-bottom: 4px; }
.c-desc { font-size: var(--font-size-xs); color: var(--color-text-muted); }

@media (max-width: 768px) {
  .help-body {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-xl); flex-direction: column; padding: var(--space-lg) var(--space-md); }
  .help-sidebar { width: 100%; position: static; }
  .sidebar-menu { display: flex; overflow-x: auto; padding: var(--space-sm) var(--space-md); gap: 4px; }
  .menu-item { white-space: nowrap; padding: var(--space-sm) var(--space-md); border-radius: var(--radius-lg); border-left: none; font-size: var(--font-size-sm); }
  .menu-item.active { background: var(--color-primary); color: white; border-left: none; }
  .sidebar-title { display: none; }
  .contact-cards { grid-template-columns: 1fr; }
  .faq-a { padding-left: var(--space-xl); }
}
</style>
