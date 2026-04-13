import { defineStore } from 'pinia'

/**
 * Cookie 工具函数
 * token/userType/mobile 写入 cookie (domain=.591jiajiao.cn / .591jiajiao.com),
 * 所有子域名共享, 实现跨城市保持登录
 */

const COOKIE_DOMAINS = ['.591jiajiao.cn', '.591jiajiao.com']
const COOKIE_MAX_AGE = 7 * 86400 // 7 天

function isProductionHost(): boolean {
  if (typeof window === 'undefined') return false
  const h = window.location.hostname
  return h.endsWith('591jiajiao.cn') || h.endsWith('591jiajiao.com') ||
         h === '591jiajiao.cn' || h === '591jiajiao.com'
}

function setCookie(name: string, value: string) {
  if (typeof document === 'undefined') return
  if (isProductionHost()) {
    // 对两个域都写 cookie
    for (const domain of COOKIE_DOMAINS) {
      document.cookie = `${name}=${encodeURIComponent(value)}; domain=${domain}; path=/; max-age=${COOKIE_MAX_AGE}; SameSite=Lax`
    }
  } else {
    // 开发环境 (localhost): 不带 domain
    document.cookie = `${name}=${encodeURIComponent(value)}; path=/; max-age=${COOKIE_MAX_AGE}; SameSite=Lax`
  }
}

function deleteCookie(name: string) {
  if (typeof document === 'undefined') return
  if (isProductionHost()) {
    for (const domain of COOKIE_DOMAINS) {
      document.cookie = `${name}=; domain=${domain}; path=/; max-age=0; SameSite=Lax`
    }
  } else {
    document.cookie = `${name}=; path=/; max-age=0; SameSite=Lax`
  }
}

function getCookie(name: string): string {
  if (typeof document === 'undefined') return ''
  const match = document.cookie.match(new RegExp(`(?:^|;\\s*)${name}=([^;]*)`))
  return match ? decodeURIComponent(match[1]) : ''
}

/** 初始化时从 cookie 读取, 兼容从旧 localStorage 迁移 */
function initValue(key: string, fallback: string = ''): string {
  // 优先从 cookie 读
  const cookieVal = getCookie(key)
  if (cookieVal) return cookieVal
  // fallback 到 localStorage (兼容旧版)
  if (typeof localStorage !== 'undefined') {
    const lsVal = localStorage.getItem(key)
    if (lsVal) return lsVal
  }
  return fallback
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: initValue('token'),
    userType: parseInt(initValue('userType', '0')) || 0,
    mobile: initValue('mobile'),
    isAdmin: initValue('isAdmin') === 'true',
    nickname: initValue('nickname')
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
    saveLogin(data: { token: string; mobile: string; userType: number; isAdmin?: boolean; nickname?: string }) {
      this.token = data.token
      this.mobile = data.mobile
      this.userType = data.userType
      this.isAdmin = data.isAdmin || false
      this.nickname = data.nickname || ''

      // 写入 cookie (跨子域名共享)
      setCookie('token', data.token)
      setCookie('userType', String(data.userType))
      setCookie('mobile', data.mobile)
      setCookie('isAdmin', String(data.isAdmin || false))
      if (data.nickname) {
        setCookie('nickname', data.nickname)
      }

      // 同时写入 localStorage 做本地 fallback
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem('token', data.token)
        localStorage.setItem('userType', String(data.userType))
        localStorage.setItem('mobile', data.mobile)
        localStorage.setItem('isAdmin', String(data.isAdmin || false))
        if (data.nickname) {
          localStorage.setItem('nickname', data.nickname)
        }
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
            setCookie('nickname', displayName)
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

      // 清除 cookie (两个域都清)
      deleteCookie('token')
      deleteCookie('userType')
      deleteCookie('mobile')
      deleteCookie('isAdmin')
      deleteCookie('nickname')

      // 清除 localStorage
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
