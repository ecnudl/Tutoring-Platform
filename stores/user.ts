import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: (typeof localStorage !== 'undefined' ? localStorage.getItem('token') : '') || '',
    userType: parseInt((typeof localStorage !== 'undefined' ? localStorage.getItem('userType') : '0') || '0'),
    mobile: (typeof localStorage !== 'undefined' ? localStorage.getItem('mobile') : '') || '',
    isAdmin: (typeof localStorage !== 'undefined' ? localStorage.getItem('isAdmin') === 'true' : false)
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
    isTutor: (s) => s.userType === 1,
    isStudent: (s) => s.userType === 2
  },
  actions: {
    saveLogin(data: { token: string; mobile: string; userType: number; isAdmin?: boolean }) {
      this.token = data.token
      this.mobile = data.mobile
      this.userType = data.userType
      this.isAdmin = data.isAdmin || false
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem('token', data.token)
        localStorage.setItem('userType', String(data.userType))
        localStorage.setItem('mobile', data.mobile)
        localStorage.setItem('isAdmin', String(data.isAdmin || false))
      }
    },
    logout() {
      this.token = ''
      this.mobile = ''
      this.userType = 0
      this.isAdmin = false
      if (typeof localStorage !== 'undefined') {
        localStorage.removeItem('token')
        localStorage.removeItem('userType')
        localStorage.removeItem('mobile')
        localStorage.removeItem('isAdmin')
      }
    }
  }
})
