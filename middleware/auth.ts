/**
 * /center/* 等需要登录的页面用的鉴权中间件.
 * 登录态来源 (与 stores/user.ts 一致):
 *   1) cookie 'token' (跨子域共享, 主要)
 *   2) localStorage 'token' (兼容旧版, 仅同子域)
 * 任一存在即视为登录, 不存在跳 /login.
 */
function readCookie(name: string): string {
  if (typeof document === 'undefined') return ''
  const m = document.cookie.match(new RegExp('(^|; )' + name + '=([^;]*)'))
  return m ? decodeURIComponent(m[2]) : ''
}

export default defineNuxtRouteMiddleware((to) => {
  // SSR 时无法读 cookie/localStorage, 跳过 (页面会在 client mount 后再判定)
  if (typeof window === 'undefined') return
  const fromCookie = readCookie('token')
  const fromLocal = (typeof localStorage !== 'undefined' && localStorage.getItem('token')) || ''
  if (!fromCookie && !fromLocal) {
    return navigateTo('/login?redirect=' + encodeURIComponent(to.fullPath))
  }
})
