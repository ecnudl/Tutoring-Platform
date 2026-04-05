import { defineStore } from 'pinia'

export const useCityStore = defineStore('city', {
  state: () => ({
    cityId: 1,
    cityName: '上海'
  }),
  actions: {
    setCity(id: number, name: string) {
      this.cityId = id
      this.cityName = name
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem('cityId', String(id))
        localStorage.setItem('cityName', name)
      }
    },
    loadFromStorage() {
      if (typeof localStorage !== 'undefined') {
        const id = localStorage.getItem('cityId')
        const name = localStorage.getItem('cityName')
        if (id) this.cityId = parseInt(id)
        if (name) this.cityName = name
      }
    }
  }
})
