import { defineStore } from 'pinia'

export const useCityStore = defineStore('city', {
  state: () => ({
    cityId: 1,
    cityName: '上海',
    cityPinyin: '' // 上海为默认站, pinyin 留空表示裸域名
  }),
  actions: {
    setCity(id: number, name: string, pinyin: string = '') {
      this.cityId = id
      this.cityName = name
      this.cityPinyin = pinyin
      // 保留 localStorage 做 fallback（裸域名访问时使用）
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem('cityId', String(id))
        localStorage.setItem('cityName', name)
        localStorage.setItem('cityPinyin', pinyin)
      }
    },
    loadFromStorage() {
      if (typeof localStorage !== 'undefined') {
        const id = localStorage.getItem('cityId')
        const name = localStorage.getItem('cityName')
        const pinyin = localStorage.getItem('cityPinyin')
        if (id) this.cityId = parseInt(id)
        if (name) this.cityName = name
        if (pinyin !== null) this.cityPinyin = pinyin
      }
    }
  }
})
