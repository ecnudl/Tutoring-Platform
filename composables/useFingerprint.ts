/**
 * 浏览器指纹 (FingerprintJS OSS) — 单例 + 缓存到 sessionStorage,
 * 注册/登录前调一次, 提交时跟随 mobile/password 一起带给后端.
 *
 * 后端用它做"同设备多账号"识别: 一台机器生成 N 个不同手机号注册视为可疑.
 * 注: OSS 版准确率 ~70%, Pro 版 ~99% (要 API key+收费).
 */
import FingerprintJS from '@fingerprintjs/fingerprintjs'

let cachedId: string | null = null
let inflight: Promise<string> | null = null

export const useFingerprint = () => {
  const get = async (): Promise<string> => {
    if (cachedId) return cachedId
    // sessionStorage 缓存 (跨页面但不跨 tab/会话)
    if (typeof window !== 'undefined') {
      const cached = sessionStorage.getItem('fp_visitor_id')
      if (cached) { cachedId = cached; return cached }
    }
    if (inflight) return inflight
    inflight = (async () => {
      try {
        const fp = await FingerprintJS.load()
        const r = await fp.get()
        cachedId = r.visitorId
        if (typeof window !== 'undefined') sessionStorage.setItem('fp_visitor_id', cachedId)
        return cachedId
      } catch (e) {
        console.warn('[fingerprint] load failed', e)
        return ''
      } finally {
        inflight = null
      }
    })()
    return inflight
  }
  return { get }
}
