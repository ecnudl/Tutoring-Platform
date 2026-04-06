import axios from 'axios'

const config = useRuntimeConfig()

const api = axios.create({
  baseURL: config.public.apiBase as string,
  timeout: 15000
})

api.interceptors.request.use((config) => {
  if (typeof localStorage !== 'undefined') {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token
    }
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
