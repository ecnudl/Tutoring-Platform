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
      title: '591家教网',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '591家教网 - 找家教、做家教、一站式服务平台' }
      ],
      link: dnsPrefetch
    }
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
