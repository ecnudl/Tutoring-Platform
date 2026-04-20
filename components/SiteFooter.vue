<template>
  <footer class="site-footer">
    <div class="footer-inner">
      <div class="footer-cards">
        <div v-for="group in groups" :key="group.group" class="footer-card">
          <h4 class="card-title">{{ group.group }}</h4>
          <div class="card-links">
            <NuxtLink
              v-for="item in group.items"
              :key="item.href"
              :to="item.href"
              class="card-link"
            >{{ item.label }}</NuxtLink>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>{{ copyright }}</span>
        <template v-if="icpNo">
          <span class="sep">·</span>
          <a href="https://beian.miit.gov.cn" target="_blank" rel="noopener">{{ icpNo }}</a>
        </template>
        <template v-if="securityNo">
          <span class="sep">·</span>
          <span>{{ securityNo }}</span>
        </template>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSiteConfig } from '~/composables/useSiteConfig'

const { config, load, footerMenus } = useSiteConfig()

const DEFAULT_GROUPS = [
  { group: '我是学员', items: [
    { label: '请家教流程', href: '/help' },
    { label: '请家教收费标准', href: '/zf' },
    { label: '请家教常见问题', href: '/help' },
    { label: '怎样快速找到老师', href: '/help' }
  ]},
  { group: '我是教员', items: [
    { label: '成为家教老师', href: '/register/teacher' },
    { label: '家教课时费参考标准', href: '/zf' },
    { label: '做家教收费标准', href: '/zf' },
    { label: '做家教常见问题', href: '/help' }
  ]},
  { group: '关于我们', items: [
    { label: '服务号', href: '/about/wechat' },
    { label: '联系我们', href: '/about/contact' },
    { label: '公司简介', href: '/about/intro' },
    { label: '隐私保护', href: '/agreement/privacy' }
  ]}
]

const copyright = computed(() => config.value.siteCopyright
  || `© ${new Date().getFullYear()} 591家教网 版权所有`)
const icpNo = computed(() => config.value.siteIcpNo || '')
const securityNo = computed(() => config.value.sitePublicSecurityNo || '')
const groups = computed(() => footerMenus.value.length ? footerMenus.value : DEFAULT_GROUPS)

onMounted(() => { load() })
</script>

<style scoped>
.site-footer {
  background: var(--color-bg, #f5f6f8);
  margin-top: auto;
  padding: 60px 0 28px;
}

.footer-inner {
  max-width: var(--content-width, 1200px);
  margin: 0 auto;
  padding: 0 var(--space-xl, 20px);
}

.footer-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 28px;
}

.footer-card {
  background: #f3f4f6;
  border-radius: 10px;
  padding: 36px 40px;
  min-height: 260px;
  transition: box-shadow 0.25s ease;
}

.footer-card:hover {
  box-shadow: 0 6px 24px rgba(22, 59, 107, 0.08);
}

.card-title {
  font-size: 20px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 24px;
  letter-spacing: 1px;
}

.card-links {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.card-link {
  font-size: 15px;
  color: #525b68;
  text-decoration: none;
  transition: color 0.18s ease;
  width: fit-content;
}

.card-link:hover {
  color: var(--color-primary);
}

.footer-bottom {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
  font-size: 13px;
  color: #94a3b8;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.footer-bottom .sep { color: #cbd5e1; }
.footer-bottom a { color: #94a3b8; text-decoration: none; }
.footer-bottom a:hover { color: var(--color-primary); }

@media (max-width: 768px) {
  .site-footer { padding: 36px 0 20px; }
  .footer-cards { grid-template-columns: 1fr; gap: 16px; }
  .footer-card { padding: 24px 28px; min-height: auto; }
  .card-title { font-size: 18px; margin-bottom: 18px; }
  .card-links { gap: 12px; }
}
</style>
