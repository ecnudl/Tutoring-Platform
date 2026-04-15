import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineNuxtConfig({
  ssr: false,
  devtools: { enabled: false },
  modules: ['@pinia/nuxt'],
  css: ['~/assets/styles/main.scss'],
  runtimeConfig: {
    public: { apiBase: process.env.API_BASE || '' }
  },
  app: {
    head: {
      title: '591家教网',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '591家教网 - 找家教、做家教、一站式服务平台' }
      ],
      link: [
        // DNS prefetch + preconnect: 浏览器提前解析所有城市子域名，切换时省去 DNS 耗时
        { rel: 'dns-prefetch', href: '//nanjing.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//suzhou.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//hangzhou.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//hefei.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//fuzhou.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//nanchang.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//jinan.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//beijing.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//tianjin.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//guangzhou.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//wuhan.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//nanjing.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//suzhou.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//hangzhou.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//hefei.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//fuzhou.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//nanchang.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//jinan.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//beijing.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//tianjin.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//guangzhou.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//wuhan.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//zhengzhou.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//chongqing.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//xian.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//chengdu.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//changsha.591jiajiao.cn' },
        { rel: 'dns-prefetch', href: '//zhengzhou.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//chongqing.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//xian.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//chengdu.591jiajiao.com' },
        { rel: 'dns-prefetch', href: '//changsha.591jiajiao.com' },
      ]
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
