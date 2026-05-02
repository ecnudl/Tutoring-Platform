import axios from 'axios'

const config = useRuntimeConfig()

const api = axios.create({
  baseURL: config.public.apiBase as string,
  timeout: 15000
})

/**
 * 读 token: 优先 cookie (跨子域共享, 例如 beijing.591jiajiao.cn → 591jiajiao.cn),
 * 退到 localStorage (兼容老版本 + 同子域内).
 */
function readToken(): string {
  if (typeof document !== 'undefined') {
    const m = document.cookie.match(/(^|; )token=([^;]*)/)
    if (m) return decodeURIComponent(m[2])
  }
  if (typeof localStorage !== 'undefined') {
    return localStorage.getItem('token') || ''
  }
  return ''
}

/** token 失效相关业务码 — 收到这些 code 自动跳登录 */
const AUTH_EXPIRED_CODES = new Set([99, 301, 302, 305])

function clearAllAuthAndRedirect() {
  if (typeof window === 'undefined') return
  try {
    localStorage.removeItem('token')
    localStorage.removeItem('userType')
    localStorage.removeItem('mobile')
    localStorage.removeItem('isAdmin')
    localStorage.removeItem('nickname')
    // 删 cookie (跨子域 + 当前域)
    for (const domain of ['.591jiajiao.cn', '.591jiajiao.com']) {
      document.cookie = `token=; domain=${domain}; path=/; max-age=0`
      document.cookie = `userType=; domain=${domain}; path=/; max-age=0`
      document.cookie = `mobile=; domain=${domain}; path=/; max-age=0`
      document.cookie = `isAdmin=; domain=${domain}; path=/; max-age=0`
      document.cookie = `nickname=; domain=${domain}; path=/; max-age=0`
    }
    document.cookie = 'token=; path=/; max-age=0'
  } catch {}
  const cur = window.location.pathname + window.location.search
  if (!cur.startsWith('/login')) {
    window.location.href = '/login?redirect=' + encodeURIComponent(cur)
  }
}

api.interceptors.request.use((config) => {
  const token = readToken()
  if (token) {
    config.headers['token'] = token
  }
  return config
})

api.interceptors.response.use(
  (res) => {
    const body = res.data
    // 业务码: token 失效 → 清登录态 + 跳登录
    if (body && typeof body === 'object' && AUTH_EXPIRED_CODES.has(body.code)) {
      clearAllAuthAndRedirect()
    }
    return body
  },
  (err) => {
    // HTTP 401 — 也跳登录
    if (err.response?.status === 401) {
      clearAllAuthAndRedirect()
    }
    return Promise.reject(err)
  }
)

export const useApi = () => ({
  get: (url: string, params?: any) => api.get(url, { params }) as Promise<any>,
  post: (url: string, data?: any) => api.post(url, data) as Promise<any>,
  put: (url: string, data?: any) => api.put(url, data) as Promise<any>,
  del: (url: string, params?: any) => api.delete(url, { params }) as Promise<any>
})
