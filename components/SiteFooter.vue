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

// 学员/教员 板块所有条目都指向 /help 帮助中心 (统一入口, 减少分散子页);
// /zf 收费标准是独立功能页, 保留入口. 关于我们继续指向独立页面 (内容性质不同).
const DEFAULT_GROUPS = [
  { group: '我是学员', items: [
    { label: '请家教流程', href: '/help' },
    { label: '请家教收费标准', href: '/zf' },
    { label: '请家教常见问题', href: '/help' },
    { label: '怎样快速找到老师', href: '/help' }
  ]},
  { group: '我是教员', items: [
    { label: '成为家教老师', href: '/help' },
    { label: '家教课时费参考标准', href: '/zf' },
    { label: '做家教收费标准', href: '/help' },
    { label: '做家教常见问题', href: '/help' }
  ]},
  { group: '关于我们', items: [
    { label: '联系我们', href: '/about/contact' },
    { label: '公司简介', href: '/about/intro' },
    { label: '免责声明', href: '/agreement/disclaimer' },
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
/* 深蓝底 + 浅蓝卡片浮在上面：卡片像一块"漂浮"在深蓝海面上的光板 */
.site-footer {
  background: var(--color-primary-dark, #163B6B);
  margin-top: auto;
  padding: 56px 0 24px;
}

.footer-inner {
  max-width: var(--content-width, 1200px);
  margin: 0 auto;
  padding: 0 var(--space-xl, 20px);
}

.footer-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.footer-card {
  /* 浅蓝——在深蓝底色上是"更浅的蓝"，仍然含蓝色调 */
  background: #eaf1fa;
  border-radius: 10px;
  padding: 34px 38px;
  min-height: 260px;
  transition: transform 0.25s ease, box-shadow 0.25s ease, background 0.25s ease;
  box-shadow: 0 2px 10px rgba(8, 22, 44, 0.18);
}

.footer-card:hover {
  background: #f2f7fd;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(8, 22, 44, 0.28);
}

.card-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-primary-dark, #163B6B);
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
  color: #4a5a72;
  text-decoration: none;
  transition: color 0.18s ease;
  width: fit-content;
}

.card-link:hover {
  color: var(--color-primary);
}

/* 底部版权条：深蓝底色、白色淡文字 */
.footer-bottom {
  margin-top: 36px;
  padding-top: 18px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.footer-bottom .sep { color: rgba(255, 255, 255, 0.2); }
.footer-bottom a { color: rgba(255, 255, 255, 0.5); text-decoration: none; }
.footer-bottom a:hover { color: #fff; }

@media (max-width: 768px) {
  .site-footer { padding: 36px 0 20px; }
  .footer-cards { grid-template-columns: 1fr; gap: 16px; }
  .footer-card { padding: 24px 28px; min-height: auto; }
  .card-title { font-size: 18px; margin-bottom: 18px; }
  .card-links { gap: 12px; }
}
</style>
