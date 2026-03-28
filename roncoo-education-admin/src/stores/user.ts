import { defineStore } from 'pinia'
export const useAdminStore = defineStore('admin', {
  state: () => ({ token: localStorage.getItem('admin_token') || '', adminInfo: null as any }),
  getters: { isLoggedIn: (s) => !!s.token },
  actions: {
    setToken(t: string) { this.token = t; localStorage.setItem('admin_token', t) },
    logout() { this.token = ''; localStorage.removeItem('admin_token') }
  }
})
