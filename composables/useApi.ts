import axios from 'axios'

const config = useRuntimeConfig()

const api = axios.create({
  baseURL: config.public.apiBase as string,
  timeout: 15000
})

/**
 * 读 token: 优先 cookie (跨子域共享, 例如 beijing.591jiajiao.cn → 591jiajiao.cn),
 * 退到 localStorage (兼容老版本 + 同子域内).
 * 跟 middleware/auth.ts 的判定逻辑保持一致.
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

api.interceptors.request.use((config) => {
  const token = readToken()
  if (token) {
    config.headers['token'] = token
  }
  return config
})

api.interceptors.response.use(
  (res) => res.data,
  (err) => {
    if (err.response?.status === 401 && typeof window !== 'undefined') {
      localStorage.removeItem('token')
      window.location.href = '/login'
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
