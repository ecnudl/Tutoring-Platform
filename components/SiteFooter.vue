<template>
  <footer class="site-footer">
    <div class="footer-inner">
      <div class="footer-main">
        <div class="footer-brand">
          <SiteLogo variant="light" />
          <p class="footer-desc">{{ brandDesc }}</p>
        </div>
        <div class="footer-links">
          <div v-for="group in groups" :key="group.group" class="footer-col">
            <h4>{{ group.group }}</h4>
            <NuxtLink
              v-for="item in group.items"
              :key="item.href"
              :to="item.href"
              :class="{ 'disclaimer-link': item.href === '/agreement/disclaimer' }"
            >{{ item.label }}</NuxtLink>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>{{ copyright }}</span>
        <template v-if="icpNo">
          <span class="sep">|</span>
          <a href="https://beian.miit.gov.cn" target="_blank" rel="noopener">{{ icpNo }}</a>
        </template>
        <template v-if="securityNo">
          <span class="sep">|</span>
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
  { group: '找家教', items: [
    { label: '教员库', href: '/jy' }, { label: '学员库', href: '/xy' },
    { label: '请家教', href: '/qjj' }, { label: '价格参考', href: '/zf' }
  ]},
  { group: '教员入口', items: [
    { label: '教员注册', href: '/register/teacher' },
    { label: '教员登录', href: '/login' }, { label: '个人中心', href: '/center' }
  ]},
  { group: '帮助与支持', items: [
    { label: '帮助中心', href: '/help' }, { label: '用户协议', href: '/agreement/user' },
    { label: '隐私政策', href: '/agreement/privacy' }, { label: '免责声明', href: '/agreement/disclaimer' }
  ]},
  { group: '关于我们', items: [
    { label: '联系我们', href: '/about/contact' }, { label: '服务号', href: '/about/wechat' },
    { label: '公司简介', href: '/about/intro' }, { label: '隐私保护', href: '/agreement/privacy' }
  ]}
]

const brandDesc = computed(() =>
  config.value.siteFooterDesc || '专业家教服务平台，提供上门一对一辅导。严选教员，安心托付。'
)
const copyright = computed(() => config.value.siteCopyright
  || `© ${new Date().getFullYear()} 591家教网 版权所有`)
const icpNo = computed(() => config.value.siteIcpNo || '')
const securityNo = computed(() => config.value.sitePublicSecurityNo || '')
const groups = computed(() => footerMenus.value.length ? footerMenus.value : DEFAULT_GROUPS)

onMounted(() => { load() })
</script>

<style scoped>
.site-footer {
  background: var(--color-primary-dark, #163B6B);
  color: rgba(255, 255, 255, 0.75);
  margin-top: auto;
}

.footer-inner { max-width: var(--content-width, 1200px); margin: 0 auto; padding: 0 var(--space-xl, 20px); }
.footer-main { display: flex; gap: 60px; padding: 36px 0 28px; }
.footer-brand { width: 220px; flex-shrink: 0; }
.footer-desc { margin-top: 12px; font-size: 13px; line-height: 1.6; color: rgba(255, 255, 255, 0.55); }
.footer-links { flex: 1; display: flex; gap: 40px; }
.footer-col { display: flex; flex-direction: column; gap: 8px; }
.footer-col h4 { font-size: 14px; font-weight: 600; color: #fff; margin-bottom: 4px; }
.footer-col a, .footer-col span { font-size: 13px; color: rgba(255, 255, 255, 0.6); transition: color 0.15s ease; }
.footer-col a:hover { color: #fff; }

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 16px 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  text-align: center;
  display: flex;
  justify-content: center;
  gap: 10px;
}
.footer-bottom .sep { color: rgba(255,255,255,0.2); }
.footer-bottom a { color: rgba(255,255,255,0.5); }
.footer-bottom a:hover { color: #fff; }

.footer-col a.disclaimer-link { font-size: 14px; font-weight: 600; color: rgba(255, 255, 255, 0.85); }
.footer-col a.disclaimer-link:hover { color: #fff; }

@media (max-width: 768px) {
  .footer-main { flex-direction: column; gap: 24px; padding: 24px 0 20px; }
  .footer-brand { width: 100%; }
  .footer-links { flex-wrap: wrap; gap: 24px; }
  .footer-col { min-width: 120px; }
}
</style>
