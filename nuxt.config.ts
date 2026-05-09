import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// 站点域名 & 城市：来自 .env，迁移时只需改环境变量
// BASE_DOMAINS 可多个，用半角逗号分隔（默认保持 591jiajiao.cn/com）
const BASE_DOMAINS = (process.env.BASE_DOMAINS || '591jiajiao.cn,591jiajiao.com')
  .split(',').map(s => s.trim()).filter(Boolean)

// 启用城市拼音列表（不含上海 = 默认站）；迁移时可改，失败时不影响首屏
const CITY_PINYINS = (process.env.CITY_PINYINS ||
  'beijing,guangzhou,nanjing,suzhou,hangzhou,hefei,fuzhou,nanchang,jinan,' +
  'tianjin,wuhan,zhengzhou,chongqing,xian,chengdu,changsha')
  .split(',').map(s => s.trim()).filter(Boolean)

// DNS prefetch：base × pinyin 笛卡儿积，浏览器提前解析子域名 DNS
const dnsPrefetch = BASE_DOMAINS.flatMap(base =>
  CITY_PINYINS.map(py => ({ rel: 'dns-prefetch', href: `//${py}.${base}` }))
)

// Cookie 域：给 stores/user.ts 用，登录态跨子域共享
const COOKIE_DOMAINS = BASE_DOMAINS.map(d => `.${d}`)

// 站点品牌域名（顶栏右侧展示），sys_config.websiteDomain 覆盖此默认值
const BRAND_DOMAINS = BASE_DOMAINS.map(d => `www.${d}`)

export default defineNuxtConfig({
  ssr: false,
  devtools: { enabled: false },
  modules: ['@pinia/nuxt'],
  css: ['~/assets/styles/main.scss'],
  runtimeConfig: {
    public: {
      apiBase: process.env.API_BASE || '',
      baseDomains: BASE_DOMAINS,
      cookieDomains: COOKIE_DOMAINS,
      brandDomains: BRAND_DOMAINS
    }
  },
  app: {
    head: {
      title: '591家教网 - 名校名师优质家教平台 / 全国 17 城上门家教 / 在线辅导',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '591家教网 — 全国 17 城 (北京/上海/广州/南京/苏州/杭州等) 优质家教平台, 名校教员严选审核, 一对一上门或在线辅导, 涵盖语文/数学/英语/物理/化学/钢琴/美术/雅思/托福/GRE 等 30+ 科目。' },
        { name: 'keywords', content: '家教,家教网,591家教,上门家教,在线家教,大学生家教,北京家教,上海家教,广州家教,南京家教,杭州家教,苏州家教,武汉家教,西安家教,成都家教,数学家教,英语家教,物理家教,化学家教,钢琴家教,雅思,托福,GRE' },
        { name: 'author', content: '591家教网' },
        { name: 'robots', content: 'index,follow' },
        { property: 'og:title', content: '591家教网 - 名校名师优质家教平台' },
        { property: 'og:description', content: '全国 17 城优质家教, 名校教员严选, 一对一上门或在线辅导。' },
        { property: 'og:type', content: 'website' },
        { property: 'og:locale', content: 'zh_CN' }
      ],
      link: dnsPrefetch
    },
    // SPA 壳子里塞 SEO 静态 HTML — 蜘蛛 (不执行 JS) 看到这段, JS 加载后 Vue 替换
    spaLoadingTemplate: 'app/spa-loading-template.html'
  },
  vite: {
    plugins: [
      AutoImport({
        resolvers: [ElementPlusResolver({ importStyle: 'css' })],
      }),
      Components({
        resolvers: [ElementPlusResolver({ importStyle: 'css' })],
      }),
    ],
    build: {
      rollupOptions: {
        output: {
          manualChunks: {
            'element-plus': ['element-plus'],
            'vue-vendor': ['vue', 'vue-router'],
          }
        }
      }
    }
  },
  nitro: {
    compressPublicAssets: { gzip: true, brotli: true },
  }
})
