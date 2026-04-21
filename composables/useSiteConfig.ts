/**
 * 站点配置（品牌、客服、备案、协议、友链 JSON、底部菜单 JSON、价格说明 JSON 等）。
 * 缓存于全局 state，多处组件共用一次请求。
 */

export interface SiteConfig {
  siteBrandName?: string
  siteBrandSlogan?: string
  siteHotline?: string
  siteCsEmail?: string
  siteCsWechat?: string
  siteWorkTime?: string
  siteIcpNo?: string
  sitePublicSecurityNo?: string
  siteFooterDesc?: string
  siteCopyright?: string
  agreementUserHtml?: string
  agreementDisclaimerHtml?: string
  agreementPrivacyHtml?: string
  siteAboutIntroHtml?: string
  helpRequestProcessHtml?: string
  helpFindTutorHtml?: string
  helpRequestFaqHtml?: string
  helpBecomeTutorHtml?: string
  helpTutorFaqHtml?: string
  helpTutorPricingHtml?: string
  helpPaymentMethodHtml?: string
  qjjSubtitle?: string
  qjjSuccessTip?: string
  siteFriendLinksJson?: string
  siteFooterMenusJson?: string
  sitePriceNotesJson?: string
  sitePricingData?: string

  // 网站元数据
  websiteTitle?: string
  websiteDesc?: string
  websiteKeyword?: string
  websiteCopyright?: string
  websiteDomain?: string
}

export interface FriendLink { name: string; url: string }
export interface FooterMenuGroup { group: string; items: { label: string; href: string }[] }

export const useSiteConfig = () => {
  // 使用 useState 在 Nuxt 会话里复用一次请求结果
  const cfg = useState<SiteConfig>('site-config', () => ({}))
  const loaded = useState<boolean>('site-config-loaded', () => false)

  const load = async (force = false) => {
    if (loaded.value && !force) return cfg.value
    try {
      const { get } = useApi()
      const res = await get('/system/api/site/config')
      if (res?.code === 200 && res.data) {
        cfg.value = res.data
        loaded.value = true
      }
    } catch (e) {
      console.warn('[site-config] load failed', e)
    }
    return cfg.value
  }

  const parseJSON = <T>(s?: string, fallback: T | null = null): T | null => {
    if (!s) return fallback
    try { return JSON.parse(s) as T } catch { return fallback }
  }

  const friendLinks = computed<FriendLink[]>(() => parseJSON<FriendLink[]>(cfg.value.siteFriendLinksJson, []) || [])
  const footerMenus = computed<FooterMenuGroup[]>(() => parseJSON<FooterMenuGroup[]>(cfg.value.siteFooterMenusJson, []) || [])
  const priceNotes = computed<string[]>(() => parseJSON<string[]>(cfg.value.sitePriceNotesJson, []) || [])

  return { config: cfg, load, friendLinks, footerMenus, priceNotes }
}
