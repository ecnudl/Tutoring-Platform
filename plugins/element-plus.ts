import { ID_INJECTION_KEY } from 'element-plus'
import 'element-plus/es/components/message/style/css'
import 'element-plus/es/components/message-box/style/css'
import 'element-plus/es/components/notification/style/css'
import 'element-plus/es/components/loading/style/css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { ElLoading } from 'element-plus'

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.provide(ID_INJECTION_KEY, { prefix: 1024, current: 0 })
  nuxtApp.vueApp.use(ElLoading)
  // locale is set per-component via ConfigProvider or defaults
  nuxtApp.provide('elementPlusLocale', zhCn)
})
