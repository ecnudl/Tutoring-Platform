export default defineNuxtConfig({
  ssr: false,
  devtools: { enabled: false },
  modules: ['@pinia/nuxt'],
  css: ['~/assets/styles/main.scss'],
  runtimeConfig: {
    public: { apiBase: process.env.API_BASE || 'http://localhost:7700' }
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
  }
})
