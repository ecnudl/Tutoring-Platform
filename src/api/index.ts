import axios from 'axios'

const api = axios.create({ baseURL: '', timeout: 15000 })

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token')
  if (token) {
    config.headers['token'] = token
  }
  return config
})

// 登录态失效的业务码：99=无token, 301=token过期, 302=token异常, 305=菜单过期
const AUTH_EXPIRED_CODES = new Set([99, 301, 302, 305])

function redirectToLogin() {
  localStorage.removeItem('admin_token')
  if (!location.pathname.endsWith('/login')) {
    location.href = '/admin/login'
  }
}

api.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body && typeof body === 'object' && AUTH_EXPIRED_CODES.has(body.code)) {
      redirectToLogin()
    }
    return body
  },
  (err) => {
    if (err.response?.status === 401) {
      redirectToLogin()
    }
    return Promise.reject(err)
  }
)

export const get = (url: string, params?: any) => api.get(url, { params }) as Promise<any>
export const post = (url: string, data?: any) => api.post(url, data) as Promise<any>
export const put = (url: string, data?: any) => api.put(url, data) as Promise<any>
export const del = (url: string, params?: any) => api.delete(url, { params }) as Promise<any>
export default api
