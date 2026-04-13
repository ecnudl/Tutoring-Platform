/**
 * Client plugin: 从子域名识别城市
 *
 * 在所有组件 mount 之前运行,
 * 读取 window.location.hostname, 解析出城市, 设置 cityStore
 * 这样首次渲染就是正确城市, 不会闪烁
 */
import { resolveCityFromHostname } from '~/composables/useCityMap'
import { useCityStore } from '~/stores/city'

export default defineNuxtPlugin(() => {
  const cityStore = useCityStore()
  const hostname = window.location.hostname
  const city = resolveCityFromHostname(hostname)
  // pinyin 为 'shanghai' 时留空（裸域名）
  const pinyin = city.pinyin === 'shanghai' ? '' : city.pinyin
  cityStore.setCity(city.id, city.name, pinyin)
})
