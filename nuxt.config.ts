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
      title: '51家教网',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '51家教网 - 找家教、做家教、一站式服务平台' }
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
