import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: (typeof localStorage !== 'undefined' ? localStorage.getItem('token') : '') || '',
    userType: parseInt((typeof localStorage !== 'undefined' ? localStorage.getItem('userType') : '0') || '0'),
    mobile: (typeof localStorage !== 'undefined' ? localStorage.getItem('mobile') : '') || '',
    isAdmin: (typeof localStorage !== 'undefined' ? localStorage.getItem('isAdmin') === 'true' : false),
    nickname: (typeof localStorage !== 'undefined' ? localStorage.getItem('nickname') : '') || ''
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
    isTutor: (s) => s.userType === 1,
    isStudent: (s) => s.userType === 2,
    displayName: (s) => {
      if (s.isAdmin) return '管理员'
      if (s.nickname) return s.nickname
      if (s.userType === 1) return '教员'
      if (s.userType === 2) return '家长'
      if (s.mobile) return s.mobile.slice(0, 3) + '**' + s.mobile.slice(-2)
      return '个人中心'
    }
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
    async fetchNickname() {
      if (typeof window === 'undefined' || !this.token) return
      if (this.nickname) return
      if (this.userType !== 1 && this.userType !== 2) return

      try {
        const endpoint = this.userType === 1
          ? '/user/auth/tutor-profile/view'
          : '/user/auth/student-profile/view'
        const res = await fetch(endpoint, {
          headers: { 'token': this.token }
        })
        const json = await res.json()
        if (json.code === 200 && json.data) {
          const realName = json.data.realName
          if (realName && realName.trim()) {
            const surname = realName.trim().charAt(0)
            let displayName: string
            if (this.userType === 1) {
              displayName = surname + '教员'
            } else {
              const gender = json.data.gender
              displayName = surname + (gender === 2 ? '女士' : '先生')
            }
            this.nickname = displayName
            if (typeof localStorage !== 'undefined') {
              localStorage.setItem('nickname', displayName)
            }
          }
        }
      } catch (e) {
        // silently fail - displayName getter will use fallback
      }
    },
    logout() {
      this.token = ''
      this.mobile = ''
      this.userType = 0
      this.isAdmin = false
      this.nickname = ''
      if (typeof localStorage !== 'undefined') {
        localStorage.removeItem('token')
        localStorage.removeItem('userType')
        localStorage.removeItem('mobile')
        localStorage.removeItem('isAdmin')
        localStorage.removeItem('nickname')
      }
    }
  }
})
