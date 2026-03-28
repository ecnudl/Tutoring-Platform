import axios from 'axios'

const api = axios.create({ baseURL: '', timeout: 15000 })

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token')
  if (token) {
    config.headers['token'] = token
  }
  return config
})

api.interceptors.response.use(
  (res) => res.data,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('admin_token')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export const get = (url: string, params?: any) => api.get(url, { params }) as Promise<any>
export const post = (url: string, data?: any) => api.post(url, data) as Promise<any>
export const put = (url: string, data?: any) => api.put(url, data) as Promise<any>
export const del = (url: string, params?: any) => api.delete(url, { params }) as Promise<any>
export default api
