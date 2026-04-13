/**
 * 城市数据单一来源 + 子域名解析工具
 *
 * 域名规则:
 *   裸域名 / www = 上海（默认站，无子域名）
 *   {pinyin}.591jiajiao.cn = 对应城市站
 *   {pinyin}.591jiajiao.com = 对应城市站
 *   两个域名行为完全一致
 */

export interface CityEntry {
  id: number
  name: string
  pinyin: string
  enabled: boolean
}

/** 12 个城市，上海排第一 */
export const CITY_LIST: CityEntry[] = [
  { id: 1,  name: '上海', pinyin: 'shanghai',  enabled: true },
  { id: 5,  name: '南京', pinyin: 'nanjing',   enabled: true },
  { id: 2,  name: '苏州', pinyin: 'suzhou',    enabled: true },
  { id: 4,  name: '杭州', pinyin: 'hangzhou',  enabled: true },
  { id: 3,  name: '合肥', pinyin: 'hefei',     enabled: true },
  { id: 6,  name: '福州', pinyin: 'fuzhou',    enabled: true },
  { id: 8,  name: '南昌', pinyin: 'nanchang',  enabled: true },
  { id: 7,  name: '济南', pinyin: 'jinan',     enabled: true },
  { id: 9,  name: '北京', pinyin: 'beijing',   enabled: true },
  { id: 10, name: '天津', pinyin: 'tianjin',   enabled: true },
  { id: 11, name: '广州', pinyin: 'guangzhou',  enabled: true },
  { id: 12, name: '武汉', pinyin: 'wuhan',     enabled: true },
]

/** pinyin → CityEntry 的 O(1) 查找 Map */
export const PINYIN_TO_CITY: Map<string, CityEntry> = new Map(
  CITY_LIST.map(c => [c.pinyin, c])
)

/** 上海的默认条目 */
export const DEFAULT_CITY: CityEntry = CITY_LIST[0] // 上海

/** 支持的主域名后缀 */
const BASE_DOMAINS = ['591jiajiao.cn', '591jiajiao.com']

/**
 * 从 hostname 提取子域名拼音
 * 例如 'jinan.591jiajiao.cn' → 'jinan'
 *      '591jiajiao.cn'       → null
 *      'www.591jiajiao.cn'   → null (www 不算子域名)
 *      'localhost'           → null
 */
export function extractSubdomain(hostname: string): string | null {
  const h = hostname.toLowerCase()

  for (const base of BASE_DOMAINS) {
    if (h === base || h === `www.${base}`) {
      return null // 裸域名 / www → 上海
    }
    if (h.endsWith(`.${base}`)) {
      const sub = h.slice(0, h.length - base.length - 1) // 去掉 '.591jiajiao.cn'
      if (sub && sub !== 'www') {
        return sub
      }
      return null
    }
  }

  // localhost / IP / 其他域名 → null
  return null
}

/**
 * hostname → CityEntry
 * 无效子域名或裸域名均降级为上海
 */
export function resolveCityFromHostname(hostname: string): CityEntry {
  const sub = extractSubdomain(hostname)
  if (!sub) return DEFAULT_CITY
  return PINYIN_TO_CITY.get(sub) || DEFAULT_CITY
}

/**
 * 获取当前访问的主域名后缀（.cn 或 .com）
 * 用于 buildCityUrl 保持域名后缀不变
 */
function detectBaseDomain(hostname: string): string {
  const h = hostname.toLowerCase()
  for (const base of BASE_DOMAINS) {
    if (h === base || h === `www.${base}` || h.endsWith(`.${base}`)) {
      return base
    }
  }
  // 开发环境 fallback
  return BASE_DOMAINS[0] // 591jiajiao.cn
}

/**
 * 构建目标城市的完整 URL
 *
 * 规则:
 *   上海 → https://591jiajiao.cn{path}    （裸域名，无子域名）
 *   济南 → https://jinan.591jiajiao.cn{path}
 *
 * 保持当前用户访问的域名后缀（.cn 或 .com）
 */
export function buildCityUrl(city: CityEntry, path: string = '/'): string {
  // 开发环境 (localhost) 不跳转子域名，仅更新 path
  if (typeof window !== 'undefined') {
    const h = window.location.hostname
    if (h === 'localhost' || h === '127.0.0.1' || h.startsWith('192.168.')) {
      return path
    }
  }

  const hostname = typeof window !== 'undefined' ? window.location.hostname : ''
  const base = detectBaseDomain(hostname)
  const protocol = typeof window !== 'undefined' ? window.location.protocol : 'https:'

  if (city.pinyin === 'shanghai') {
    // 上海用裸域名
    return `${protocol}//${base}${path}`
  }
  return `${protocol}//${city.pinyin}.${base}${path}`
}

/**
 * 判断当前浏览器是否已经在目标城市的子域名上
 */
export function isCurrentCity(city: CityEntry): boolean {
  if (typeof window === 'undefined') return false
  const current = resolveCityFromHostname(window.location.hostname)
  return current.id === city.id
}

/**
 * 城市切换导航：带 loading 遮罩 + 同城市跳过
 * 所有城市切换入口统一调用此函数
 */
export function navigateToCity(city: CityEntry, path: string = '/') {
  if (typeof window === 'undefined') return

  // 已在目标城市，不重载
  if (isCurrentCity(city)) return

  // 立即显示全屏 loading 遮罩，消除"卡住"的感觉
  showNavigationOverlay(city.name)

  // 跳转
  window.location.href = buildCityUrl(city, path)
}

/**
 * 显示城市切换的 loading 遮罩
 */
function showNavigationOverlay(cityName: string) {
  // 避免重复创建
  if (document.getElementById('city-nav-overlay')) return

  const overlay = document.createElement('div')
  overlay.id = 'city-nav-overlay'
  overlay.innerHTML = `
    <div style="display:flex;flex-direction:column;align-items:center;gap:16px;">
      <div style="width:40px;height:40px;border:3px solid rgba(255,255,255,0.3);border-top-color:#fff;border-radius:50%;animation:city-spin .8s linear infinite;"></div>
      <div style="font-size:16px;color:#fff;font-weight:500;">正在切换到${cityName}站...</div>
    </div>
  `
  overlay.style.cssText = `
    position:fixed;top:0;left:0;right:0;bottom:0;z-index:99999;
    background:rgba(0,0,0,0.45);backdrop-filter:blur(4px);
    display:flex;align-items:center;justify-content:center;
  `
  // 添加旋转动画
  const style = document.createElement('style')
  style.textContent = '@keyframes city-spin{to{transform:rotate(360deg)}}'
  overlay.appendChild(style)

  document.body.appendChild(overlay)
}
