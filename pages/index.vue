<template>
  <div class="home-page">
    <Head>
      <Title>{{ cityStore.cityName }}家教 - 591家教网 - 找家教上门辅导一对一</Title>
      <Meta name="description" :content="`${cityStore.cityName}家教网,提供上门家教一对一辅导服务,大学生家教、专职教员、在职教师,覆盖小学初中高中全科辅导。`" />
    </Head>

    <div class="container">
      <!-- ========== 轮播图 + 功能区（独立卡片，与顶栏拉开距离）========== -->
      <div class="home-card home-card--banner">

        <!-- ▸ 板块二：轮播图 -->
        <div class="card-banner">
          <el-carousel height="380px" :interval="5000" arrow="hover">
            <el-carousel-item v-for="slide in slides" :key="slide.id">
              <div class="slide-item">
                <img v-if="slide.imageUrl" :src="slide.imageUrl" :alt="slide.title" class="slide-img" />
                <div v-else class="slide-placeholder">
                  <div class="slide-placeholder-inner">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" width="60" height="60"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
                    <span>{{ slide.title || '轮播图片' }}</span>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>

        <!-- ▸ 板块三：三栏功能区 -->
        <div class="card-actions">
          <!-- 框1：4 行统一「图标+内容」入口 -->
          <div class="action-panel">
            <a :href="'tel:' + hotline" class="action-entry">
              <div class="entry-icon hotline-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
              </div>
              <span class="hotline-number">{{ hotline }}</span>
            </a>
            <NuxtLink to="/qjj" class="action-entry">
              <div class="entry-icon find-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
              </div>
              <span>我要找家教</span>
            </NuxtLink>
            <NuxtLink to="/about/tutor" class="action-entry">
              <div class="entry-icon teach-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
              </div>
              <span>我要做家教</span>
            </NuxtLink>
            <div class="action-entry action-entry--search">
              <div class="entry-icon search-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="22" height="22"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>
              </div>
              <el-input
                v-model="searchKeyword"
                placeholder="搜索科目"
                size="default"
                clearable
                @keyup.enter="doSearch"
              />
            </div>
          </div>

          <!-- 框2：二维码 -->
          <div class="action-panel contact-panel">
            <div class="qr-header">为便于沟通，欢迎添加微信号</div>
            <div class="contact-qr">
              <div class="qr-frame">
                <div class="qr-frame-inner">
                  <div class="qr-corner qr-corner-tl"></div>
                  <div class="qr-corner qr-corner-tr"></div>
                  <div class="qr-corner qr-corner-bl"></div>
                  <div class="qr-corner qr-corner-br"></div>
                  <div class="qr-placeholder">
                    <img src="/wechat-qr.png" alt="微信二维码" class="qr-image" />
                  </div>
                </div>
              </div>
              <span class="qr-tip">微信扫一扫添加</span>
            </div>
          </div>

          <!-- 框3：公告栏（三个 tab） -->
          <div class="action-panel notice-panel">
            <div class="notice-tabs">
              <span
                v-for="(tab, idx) in noticeTabs"
                :key="idx"
                class="notice-tab"
                :class="{ active: activeNoticeTab === idx }"
                @mouseenter="activeNoticeTab = idx"
              >{{ tab.label }}</span>
            </div>
            <div class="notice-body">
              <NuxtLink v-for="(item, i) in noticeTabs[activeNoticeTab].items" :key="i" :to="item.linkUrl || ('/notice/' + item.id)" class="notice-item">
                <span class="notice-dot"></span>
                <span class="notice-text">{{ item.title }}</span>
                <span v-if="isWithinDays(item.gmtCreate, 3)" class="new-badge">新</span>
              </NuxtLink>
            </div>
          </div>
        </div>
      </div>

      <!-- ========== 家教分类速查 ========== -->
      <div class="category-section">
        <div class="category-title">{{ cityStore.cityName }}家教分类速查</div>
        <div class="category-body">
          <div class="category-row">
            <div class="category-label">家教科目</div>
            <div class="category-links-wrap">
              <div class="category-links" :class="{ expanded: subjectExpanded }">
                <NuxtLink v-for="s in allSubjects" :key="s.name" :to="'/jy?subject=' + encodeURIComponent(s.name)">{{ s.name }}</NuxtLink>
              </div>
              <span class="expand-btn" @click.prevent="subjectExpanded = !subjectExpanded">
                {{ subjectExpanded ? '收起' : '展开' }}
                <el-icon size="12"><component :is="subjectExpanded ? ArrowUp : ArrowDown" /></el-icon>
              </span>
            </div>
          </div>
          <div class="category-row">
            <div class="category-label">家教区域</div>
            <div class="category-links">
              <NuxtLink v-for="d in districts" :key="d.id" :to="'/jy?district=' + d.id">{{ d.name }}</NuxtLink>
            </div>
          </div>
          <div class="category-row">
            <div class="category-label">热门高校</div>
            <div class="category-links-wrap">
              <div class="category-links" :class="{ expanded: universityExpanded }">
                <NuxtLink v-for="u in universities" :key="u" :to="'/jy?university=' + encodeURIComponent(u)">{{ u }}</NuxtLink>
              </div>
              <span class="expand-btn" @click.prevent="universityExpanded = !universityExpanded">
                {{ universityExpanded ? '收起' : '展开' }}
                <el-icon size="12"><component :is="universityExpanded ? ArrowUp : ArrowDown" /></el-icon>
              </span>
            </div>
          </div>
          <div class="category-row">
            <div class="category-label">教员类型</div>
            <div class="category-links">
              <NuxtLink to="/jy?tutorType=1">大学生</NuxtLink>
              <NuxtLink to="/jy?tutorType=2">专职教员</NuxtLink>
              <NuxtLink to="/jy?tutorType=3">在职教师</NuxtLink>
              <NuxtLink to="/jy?tutorType=4">海归外教</NuxtLink>
            </div>
          </div>
        </div>
      </div>

      <!-- ========== 最新教员 ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>最新教员</h2>
          <NuxtLink to="/jy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>
        <div v-if="tutors.length" class="tutor-grid">
          <NuxtLink v-for="t in tutors.slice(0, 18)" :key="t.id" :to="'/jy/t' + (t.displayNo ? t.displayNo.replace(/^T/i, '') : t.id)" class="tutor-card">
            <div class="tutor-avatar-wrap">
              <el-avatar :size="76" :src="tutorAvatarUrl(t)" />
            </div>
            <div class="tutor-name-row">
              <span class="tutor-name">{{ getTutorDisplayName(t) }}</span>
              <TutorIcons :tutor="t" :size="14" />
            </div>
            <div class="tutor-school">{{ t.university || '未填写' }}</div>
          </NuxtLink>
        </div>
        <div v-if="tutors.length" class="tutor-more-wrap">
          <NuxtLink to="/jy" class="tutor-more-btn">显示更多教员 →</NuxtLink>
        </div>
        <el-empty v-else description="暂无教员数据" />
      </div>

      <!-- ========== 家教最新订单 (与 /xy 学员库卡片对齐) ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>{{ cityStore.cityName }}家教最新订单</h2>
          <NuxtLink to="/xy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>

        <div class="req-list" v-if="latestOrders.length">
          <template v-for="r in latestOrders" :key="r.id">
            <NuxtLink
              v-if="!r.isMatched"
              :to="'/xy/s' + (r.displayNo ? r.displayNo.replace(/^S/i, '') : r.id)"
              class="req-row"
            >
              <div class="rc-c1">
                <div class="rc-title">
                  <span v-if="r.isUrgent" class="rc-urgent">急</span>
                  {{ buildCardTitle(r) }}
                </div>
                <div class="rc-no">{{ r.displayNo || ('S' + r.id) }}</div>
              </div>
              <div class="rc-c2">
                <template v-if="Number(r.teachingMethod) === 3">
                  <div class="rc-online">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="10" r="5"/><circle cx="12" cy="10" r="1.6" fill="currentColor" stroke="none"/><path d="M12 15v3M8 19h8"/></svg>
                    网络授课
                  </div>
                </template>
                <template v-else>
                  <div class="rc-region">{{ buildRegion(r) }}</div>
                </template>
              </div>
              <div class="rc-c3">
                <div class="rc-req">{{ r.otherRequirements || '没有额外要求' }}</div>
                <div class="rc-pref">{{ buildTutorPref(r) }}</div>
              </div>
              <div class="rc-c4">
                <div class="rc-budget">{{ buildBudget(r) }}</div>
                <span class="rc-cta">详情</span>
              </div>
            </NuxtLink>

            <div v-else class="req-row is-matched">
              <div class="rc-c1">
                <div class="rc-title">{{ buildCardTitle(r) }}</div>
                <div class="rc-no">{{ r.displayNo || ('S' + r.id) }}</div>
              </div>
              <div class="rc-c2">
                <template v-if="Number(r.teachingMethod) === 3">
                  <div class="rc-online">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="10" r="5"/><circle cx="12" cy="10" r="1.6" fill="currentColor" stroke="none"/><path d="M12 15v3M8 19h8"/></svg>
                    网络授课
                  </div>
                </template>
                <template v-else>
                  <div class="rc-region">{{ buildRegion(r) }}</div>
                </template>
              </div>
              <div class="rc-c3">
                <div class="rc-req">{{ r.otherRequirements || '没有额外要求' }}</div>
                <div class="rc-pref">{{ buildTutorPref(r) }}</div>
              </div>
              <div class="rc-c4">
                <div class="rc-budget">{{ buildBudget(r) }}</div>
                <span class="rc-done">已接单</span>
              </div>
            </div>
          </template>
        </div>

        <div v-else class="order-empty">暂无最新订单 — 可前往 <NuxtLink to="/xy">学员库</NuxtLink> 浏览全部需求</div>
      </div>

      <!-- ========== 家教感言 ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>{{ cityStore.cityName }}家教感言</h2>
          <NuxtLink to="/center/feedback" class="share-link">分享您的感言 →</NuxtLink>
        </div>
        <div v-if="testimonials.length" class="testimonial-grid">
          <div v-for="(t, idx) in testimonials" :key="idx" class="testimonial-card">
            <div class="testimonial-top">
              <el-avatar :size="40">{{ t.name.charAt(0) }}</el-avatar>
              <div class="testimonial-meta">
                <div class="testimonial-name">{{ t.name }}</div>
                <div class="testimonial-role">{{ t.role }}</div>
              </div>
            </div>
            <div class="testimonial-content">"{{ t.content }}"</div>
          </div>
        </div>
        <div v-else class="order-empty">
          暂无家教感言 — 欢迎您 <NuxtLink to="/center/feedback">分享一段</NuxtLink>，经审核通过后将在此展示
        </div>
      </div>

      <!-- ========== 友情链接 & 热门城市 ========== -->
      <div class="section-box links-section">
        <div class="links-row">
          <span class="links-label">友情链接：</span>
          <a v-for="l in friendLinks" :key="l.url" :href="l.url" target="_blank" rel="noopener noreferrer" class="links-item">{{ l.name }}</a>
        </div>
        <div class="links-row">
          <span class="links-label">热门城市：</span>
          <a v-for="c in cities" :key="c.pinyin" :href="buildCityUrl(c, '/')" class="links-item">{{ c.name }}</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Phone, Search, Location, ArrowDown, ArrowUp } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'
import { CITY_LIST, buildCityUrl, navigateToCity } from '~/composables/useCityMap'
import { useSiteConfig } from '~/composables/useSiteConfig'
import { tutorAvatarUrl } from '~/composables/useTutorAvatar'
import { isWithinDays } from '~/composables/useNewBadge'

const userStore = useUserStore()
const cityStore = useCityStore()
const { districts, universities } = useCityData()
const { post, get } = useApi()
const router = useRouter()
const { config, load: loadSiteConfig, friendLinks } = useSiteConfig()

const hotline = computed(() => config.value.siteHotline || '13795420591')

const searchKeyword = ref('')
const tutors = ref([])

// 显示名：学生(tutor_type=1) → 姓+教员；其他(2在职/3专职/4海归) → 姓+老师
function getTutorDisplayName(t) {
  const name = (t.realName || '').trim()
  if (!name) return '教员'
  const surname = name.charAt(0)
  const suffix = t.tutorType === 1 ? '教员' : '老师'
  return surname + suffix
}
const activeNoticeTab = ref(0)

const latestOrdersData = ref([])
const latestOrders = computed(() => latestOrdersData.value)

const testimonialsData = ref([])
const testimonials = computed(() => testimonialsData.value)
const subjectExpanded = ref(false)
const universityExpanded = ref(false)

import { SUBJECT_OBJECTS as allSubjects } from '~/composables/subjectList'

// 公告栏 tab 配置（与后端 category 对应）
const NOTICE_CATEGORIES = [
  { key: 'notice', label: '网站公告' },
  { key: 'edu',    label: '教育资讯' },
  { key: 'notify', label: '教/学员须知' }
]

const noticeTabs = ref(NOTICE_CATEGORIES.map(c => ({ ...c, items: [] })))

const loadAnnouncements = async () => {
  await Promise.all(NOTICE_CATEGORIES.map(async (c, idx) => {
    try {
      const res = await get('/system/api/announcement/list', { category: c.key, limit: 8 })
      if (res?.code === 200 && Array.isArray(res.data)) {
        noticeTabs.value[idx].items = res.data.map(a => ({
          id: a.linkUrl ? a.linkUrl.replace(/^\/notice\//, '') : a.id,
          title: a.title,
          linkUrl: a.linkUrl || '',
          gmtCreate: a.gmtCreate || a.gmt_create || null
        }))
      }
    } catch (e) { console.warn('[announcement]', c.key, e) }
  }))
}

// 轮播图数据
const slides = ref([
  { id: 1, title: '专业家教上门辅导', imageUrl: '/banners/banner-1.svg', link: '/qjj' },
  { id: 2, title: '优质教员等你来', imageUrl: '/banners/banner-2.svg', link: '/jy' },
  { id: 3, title: '新学期辅导计划', imageUrl: '/banners/banner-3.svg', link: '/qjj' }
])

const cities = CITY_LIST

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通`)
    return
  }
  navigateToCity(city, '/')
}

const doSearch = () => {
  const kw = searchKeyword.value.trim()
  if (!kw) { router.push('/jy'); return }
  // 命中已知科目（与"分类速查"一致）→ 走 subject 参数，让科目筛选标签高亮
  // 否则 → 走 keyword 走姓名/内容模糊查
  const isKnownSubject = allSubjects.some(s => s.name === kw)
  router.push({ path: '/jy', query: isKnownSubject ? { subject: kw } : { keyword: kw } })
}

const handleLogout = () => { userStore.logout(); router.push('/') }

// 卡片渲染助手 (与 /xy 学员库卡片完全一致)
const csvFirst = (s) => {
  if (!s) return ''
  return String(s).split(',').map(x => x.trim()).filter(Boolean)[0] || ''
}
const csvJoin = (s) => {
  if (!s) return ''
  return String(s).split(',').map(x => x.trim()).filter(Boolean).join('、')
}
const buildCardTitle = (r) => {
  if (r && r.title && String(r.title).trim()) return r.title
  const subjects = csvJoin(r.subjectIds)
  return subjects || '暂无科目要求'
}
const buildRegion = (r) => {
  const dn = csvFirst(r.districtNames)
  return dn || '区域待确认'
}
const buildTutorPref = (r) => {
  // 注: latest API 字段是 studentGender, xy 是 tutorGender — 同一含义, 这里兼容两个名字
  const g = r.tutorGender !== undefined ? r.tutorGender : r.studentGender
  const gPart = g === 1 ? '[男]' : g === 2 ? '[女]' : '[无限制]'
  const types = csvJoin(r.tutorTypePref)
  return types ? `${gPart}需要教员身份${types}` : `${gPart}不限教员类型`
}
const buildBudget = (r) => {
  if (r.budgetMin && r.budgetMax && Number(r.budgetMin) !== Number(r.budgetMax)) {
    return `${r.budgetMin}-${r.budgetMax} 元/小时`
  }
  if (r.budgetMin) return `${r.budgetMin} 元/小时`
  if (r.budgetMax) return `${r.budgetMax} 元/小时`
  return '面议'
}

const loadLatestOrders = async () => {
  try {
    const res = await get('/user/api/requirement/latest', { cityId: cityStore.cityId, limit: 6 })
    if (res?.code === 200 && Array.isArray(res.data)) {
      // 直接保留原 r 字段供 buildXxx 渲染, 仅追加便于 v-if 判断的 isMatched / isUrgent (boolean)
      latestOrdersData.value = res.data.map((r) => ({
        ...r,
        isUrgent: r.isUrgent === 1,
        isMatched: r.reqStatus === 3
      }))
    }
  } catch (e) { /* leave empty */ }
}

const loadTestimonials = async () => {
  try {
    const res = await get('/user/api/feedback/latest', { limit: 4 })
    if (res?.code === 200 && Array.isArray(res.data) && res.data.length) {
      testimonialsData.value = res.data.map(f => ({
        name: f.author || '热心用户',
        role: f.role || '热心用户',
        content: f.content || ''
      }))
    }
  } catch (e) { /* keep default */ }
}

const timeAgo = (iso) => {
  try {
    const diff = Date.now() - new Date(iso).getTime()
    const m = Math.floor(diff / 60000)
    if (m < 60) return `${Math.max(1, m)}分钟前`
    const h = Math.floor(m / 60)
    if (h < 24) return `${h}小时前`
    const d = Math.floor(h / 24)
    return `${d}天前`
  } catch { return '' }
}

onMounted(async () => {
  if (userStore.isLoggedIn) {
    userStore.fetchNickname()
  }

  // 加载轮播图
  const stored = localStorage.getItem('carousel_slides')
  if (stored) {
    try {
      const parsed = JSON.parse(stored)
      if (parsed.length) slides.value = parsed
    } catch (e) { /* use defaults */ }
  }

  // 并行加载 site config / 公告 / 最新订单 / 感言 / 推荐教员
  loadSiteConfig()
  loadAnnouncements()
  loadLatestOrders()
  loadTestimonials()

  try {
    // 严格按当前城市筛选：其他城市的教员不在本站显示
    const tRes = await post('/user/api/tutor/search', {
      pageCurrent: 1,
      pageSize: 18,
      cityId: cityStore.cityId
    })
    if (tRes?.code === 200 && tRes.data) tutors.value = tRes.data.list || []
  } catch (e) {
    console.error('[home] load tutors failed', e)
  }
})
</script>

<style scoped>
.home-page {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 var(--space-4xl);
}

/* ============================================
   主卡片 — 圆角白色容器
   ============================================ */
.home-card {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  margin-bottom: var(--space-2xl);
}
/* 方案 A：banner 卡片拉开距离 */
.home-card--banner {
  margin-top: var(--space-xl);
}

/* ============================================
   板块一：顶部导航栏 — 已删除, 改用全局 components/SiteHeader.vue
   下面这段曾经的 navbar CSS (.card-nav .nav-brand .nav-center .nav-right
   .nav-cell .brand-* .city-btn .nav-phone .phone-num 等) 全部删除,
   防止与 SiteHeader 的 scoped style 干扰其他页面.
   ============================================ */
.placeholder-nav-removed { display: none; }
/* (原来 130 行 navbar CSS 已删除) */

/* ============================================
   板块二：轮播图
   ============================================ */
.card-banner {
  position: relative;
}

.slide-item {
  width: 100%;
  height: 380px;
}

.slide-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.slide-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #e8eff8 0%, #f0f4f9 50%, #eaeff6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.slide-placeholder-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-md);
  color: var(--color-primary-light);
  opacity: 0.5;
  font-size: var(--font-size-lg);
}

.card-banner :deep(.el-carousel__indicators) {
  bottom: 12px;
}

.card-banner :deep(.el-carousel__indicator .el-carousel__button) {
  background: #fff;
  opacity: 0.5;
  width: 30px;
  height: 3px;
  border-radius: 2px;
}
.card-banner :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  opacity: 1;
  background: var(--color-primary);
}

/* ============================================
   板块三：三栏功能区
   ============================================ */
.card-actions {
  display: flex;
  gap: var(--space-lg);
  padding: var(--space-xl) var(--space-xl) var(--space-xl);
  margin-top: var(--space-lg);
  border-top: none;
  min-height: 180px;
}

.action-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-xl) var(--space-2xl);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  background: var(--color-surface);
  transition: box-shadow var(--transition-fast);
}
.action-panel:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

/* --- 框1：4 行统一 图标+内容 布局 --- */
.action-entry {
  display: flex;
  align-items: center;
  gap: 22px;
  padding: 10px 12px;
  border-radius: var(--radius-lg);
  transition: background var(--transition-fast);
  cursor: pointer;
  max-width: 260px;
  width: 100%;
  margin-left: auto;
  margin-right: auto;
  text-decoration: none;
}
.action-entry:hover {
  background: var(--color-primary-lighter);
}
.action-entry + .action-entry {
  margin-top: var(--space-xs);
}

.entry-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.hotline-icon {
  background: var(--color-primary);
}
.find-icon {
  background: var(--color-primary);
}
.teach-icon {
  background: var(--color-accent);
}
.search-icon {
  background: var(--color-primary-light);
}

.action-entry span:not(.hotline-number) {
  font-size: 19px;
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
  letter-spacing: 1px;
}
.action-entry .hotline-number {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-primary);
  letter-spacing: 0.5px;
}

/* 搜索行：不需要 hover 高亮，输入框填满剩余宽度 */
.action-entry--search {
  cursor: default;
}
.action-entry--search:hover {
  background: transparent;
}
.action-entry--search :deep(.el-input) {
  flex: 1;
  min-width: 0;
}
.action-entry--search :deep(.el-input__wrapper) {
  height: 34px;
  border-radius: 8px;
  padding: 0 10px;
  box-shadow: inset 0 0 0 1px var(--color-border);
  transition: box-shadow var(--transition-fast);
}
.action-entry--search :deep(.el-input__wrapper.is-focus) {
  box-shadow: inset 0 0 0 1px var(--color-primary);
}
.action-entry--search :deep(.el-input__inner) {
  font-size: 13px;
}

/* --- 框2：二维码 --- */
.contact-panel {
  align-items: center;
  justify-content: center;
}

.qr-header {
  font-size: var(--font-size-md);
  color: var(--color-text);
  font-weight: var(--font-weight-semibold);
  text-align: center;
  margin-bottom: var(--space-lg);
}

.contact-qr {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-md);
}

.qr-frame {
  padding: 3px;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light), var(--color-accent));
  box-shadow: 0 4px 16px rgba(31, 78, 140, 0.15);
}

.qr-frame-inner {
  position: relative;
  padding: 16px;
  border-radius: 13px;
  background: #fff;
}

.qr-corner {
  position: absolute;
  width: 18px;
  height: 18px;
  border-color: var(--color-primary);
  border-style: solid;
  border-width: 0;
}
.qr-corner-tl { top: 8px; left: 8px; border-top-width: 2.5px; border-left-width: 2.5px; border-radius: 4px 0 0 0; }
.qr-corner-tr { top: 8px; right: 8px; border-top-width: 2.5px; border-right-width: 2.5px; border-radius: 0 4px 0 0; }
.qr-corner-bl { bottom: 8px; left: 8px; border-bottom-width: 2.5px; border-left-width: 2.5px; border-radius: 0 0 0 4px; }
.qr-corner-br { bottom: 8px; right: 8px; border-bottom-width: 2.5px; border-right-width: 2.5px; border-radius: 0 0 4px 0; }

.qr-placeholder {
  width: 190px;
  height: 190px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-primary-light);
  background: var(--color-primary-lighter);
  overflow: hidden;
}

.qr-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}

.qr-tip {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  letter-spacing: 0.5px;
}

/* --- 框3：公告栏 --- */
.notice-panel {
  padding: var(--space-lg) var(--space-xl);
  gap: 0;
}

.notice-tabs {
  display: flex;
  border-bottom: 2px solid var(--color-border-light);
  margin-bottom: var(--space-md);
}

.notice-tab {
  flex: 1;
  text-align: center;
  padding: 8px 0 10px;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
  cursor: pointer;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  transition: all var(--transition-fast);
}
.notice-tab:hover {
  color: var(--color-text);
}
.notice-tab.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

.notice-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notice-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  font-size: var(--font-size-base);
  color: var(--color-text);
  line-height: 1.6;
  cursor: pointer;
  transition: color var(--transition-fast);
}
.notice-item:hover {
  color: var(--color-primary);
}

.notice-dot {
  width: 5px;
  height: 5px;
  background: var(--color-primary);
  border-radius: 50%;
  flex-shrink: 0;
}

.notice-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.new-badge {
  display: inline-block;
  background: #ef4444;
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 8px;
  margin-left: 6px;
  vertical-align: middle;
  letter-spacing: 0;
  line-height: 1.4;
  flex-shrink: 0;
}

/* ============================================
   城市弹窗
   ============================================ */
.city-popover-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--space-md);
}
.city-popover-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-sm);
}
.city-opt {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  color: var(--color-text);
  background: var(--color-bg);
  transition: all var(--transition-fast);
}
.city-opt:hover { background: var(--color-primary-lighter); color: var(--color-primary); }
.city-opt.active { background: var(--color-primary); color: #fff; }
.city-opt.disabled { opacity: 0.5; cursor: not-allowed; }

/* ============================================
   家教分类速查
   ============================================ */
.category-section {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--space-2xl);
  overflow: hidden;
}

.category-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  padding: var(--space-md) var(--space-xl);
  background: var(--color-bg);
  border-bottom: 2px solid var(--color-primary);
}

.category-body {
  padding: 0 var(--space-xl);
}

.category-row {
  display: flex;
  align-items: baseline;
  padding: var(--space-md) 0;
  border-bottom: 1px solid var(--color-border-light);
}
.category-row:last-child { border-bottom: none; }

.category-label {
  width: 80px;
  font-weight: var(--font-weight-semibold);
  color: var(--color-primary);
  flex-shrink: 0;
  font-size: var(--font-size-base);
  line-height: 28px;
}

.category-links {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-md) var(--space-xl);
}
.category-links a {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  line-height: 28px;
  transition: color var(--transition-fast);
}
.category-links a:hover { color: var(--color-primary); }

/* ============================================
   可展开科目/高校行
   ============================================ */
.category-links-wrap {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: flex-start;
  gap: var(--space-md);
}

.category-links-wrap .category-links {
  flex: 1;
  min-width: 0;
  max-height: 28px;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.category-links-wrap .category-links.expanded {
  max-height: 500px;
}

.expand-btn {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: var(--font-size-base);
  color: var(--color-primary);
  cursor: pointer;
  transition: color var(--transition-fast);
  user-select: none;
  white-space: nowrap;
  line-height: 28px;
  flex-shrink: 0;
}
.expand-btn:hover {
  color: var(--color-primary-light);
}

/* ============================================
   Section / More link
   ============================================ */
.more-link {
  color: var(--color-primary);
  font-size: var(--font-size-sm);
}

.section-box {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-2xl);
  margin-bottom: var(--space-2xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-xl);
  padding-bottom: var(--space-md);
  border-bottom: 2px solid var(--color-primary);
}

.section-header h2 {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
}

/* "分享您的感言" CTA — 琥珀色描边, hover 填充, 跟首页强调色一致 */
.share-link {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-accent-dark);
  letter-spacing: 1px;
  padding: 6px 14px;
  border: 1px solid var(--color-accent);
  border-radius: var(--radius-md);
  text-decoration: none;
  white-space: nowrap;
  transition: background-color var(--transition-fast), color var(--transition-fast);
}
.share-link:hover {
  background: var(--color-accent);
  color: #fff;
}

.tutor-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 14px;
}

.tutor-more-wrap {
  display: flex;
  justify-content: center;
  margin-top: 18px;
}
.tutor-more-btn {
  display: inline-block;
  padding: 10px 28px;
  background: #fff;
  color: var(--color-primary);
  border: 1px solid var(--color-primary-light);
  border-radius: 999px;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all var(--transition-fast);
}
.tutor-more-btn:hover {
  background: var(--color-primary);
  color: #fff;
  box-shadow: 0 4px 14px rgba(31, 78, 140, 0.18);
}

.tutor-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 10px 14px;
  background: #f6f7f9;
  border: 1px solid transparent;
  border-radius: 10px;
  transition: all var(--transition-fast);
  text-decoration: none;
}
.tutor-card:hover {
  background: #fff;
  border-color: var(--color-primary-light);
  box-shadow: 0 4px 14px rgba(31, 78, 140, 0.08);
}

.tutor-avatar-wrap {
  line-height: 0;
  margin-bottom: 10px;
}

.tutor-name-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 3px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 3px;
  width: 100%;
  white-space: nowrap;
}
.tutor-name { color: var(--color-text); }

.tutor-school {
  font-size: 12px;
  color: var(--color-text-secondary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  text-align: center;
}

/* ============================================
   最新订单 — 与 /xy 学员库卡片完全对齐
   ============================================ */
.req-list {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
}

.req-row {
  display: grid;
  grid-template-columns: 200px 160px 1fr 150px;
  gap: 20px;
  align-items: center;
  padding: 18px 18px;
  background: #fff;
  border-bottom: 1px dashed #e5e7eb;
  text-decoration: none;
  position: relative;
  transition: background 0.15s, transform 0.15s;
}
.req-row:hover { background: #f8fafc; }
.req-row:hover .rc-cta {
  background: #b45309;
  transform: translateX(2px);
  box-shadow: 0 6px 20px -8px rgba(180, 83, 9, 0.45);
}
.req-row:last-child { border-bottom: none; }
.req-row.is-matched {
  opacity: 0.65;
  cursor: default;
  background: #fafbfc;
}
.req-row.is-matched:hover { background: #fafbfc; }

/* col 1: 标题 + 编号 */
.rc-c1 { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.rc-title {
  font-size: 16px; font-weight: 700;
  color: #111827;
  letter-spacing: 0.4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 6px;
}
.rc-urgent {
  display: inline-block;
  background: #FEE2E2;
  color: #DC2626;
  font-size: 11px;
  font-weight: 700;
  padding: 1px 6px;
  border-radius: 3px;
  letter-spacing: 0;
  flex-shrink: 0;
}
.rc-no {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12px;
  color: #94a3b8;
  letter-spacing: 0.5px;
}

/* col 2: 区域 / 网络授课 */
.rc-c2 { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.rc-region {
  font-size: 14px;
  color: #111827;
  font-weight: 500;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.rc-online {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #d97706;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 1px;
}
.rc-online svg { flex-shrink: 0; }

/* col 3: 备注 + 教员偏好 */
.rc-c3 { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.rc-req {
  font-size: 13px;
  color: #111827;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
  letter-spacing: 0.3px;
}
.rc-pref {
  font-size: 12px;
  color: #64748b;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}

/* col 4: 预算 + CTA */
.rc-c4 {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 14px;
  min-width: 0;
}
.rc-budget {
  font-size: 15px;
  font-weight: 700;
  color: #d97706;
  font-family: Georgia, serif;
  letter-spacing: 0.4px;
  white-space: nowrap;
}
.rc-cta {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  background: #d97706;
  color: #fff;
  padding: 7px 16px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 4px;
  transition: background 0.15s, transform 0.15s, box-shadow 0.15s;
}
.rc-done {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  color: #94a3b8;
  padding: 7px 16px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 4px;
  border: 1px dashed #cbd5e1;
  border-radius: 4px;
  cursor: default;
  user-select: none;
  background: transparent;
}

/* 折叠 */
@media (max-width: 1100px) {
  .req-row {
    grid-template-columns: 1fr 1fr;
    gap: 12px 18px;
    padding: 16px 16px;
  }
  .rc-c4 {
    grid-column: 1 / -1;
    justify-content: space-between;
    border-top: 1px dashed #f1f5f9;
    padding-top: 12px;
  }
}
@media (max-width: 768px) {
  .req-row {
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 14px 14px;
  }
  .rc-c4 {
    flex-direction: row-reverse;
    border-top: 1px dashed #f1f5f9;
    padding-top: 10px;
    margin-top: 4px;
  }
  .rc-cta { padding: 7px 14px; }
}

.order-empty {
  text-align: center;
  padding: 32px 16px;
  font-size: 13px;
  color: var(--color-text-muted);
}
.order-empty a {
  color: var(--color-primary);
  text-decoration: underline;
  margin: 0 4px;
}

/* ============================================
   家教感言
   ============================================ */
.testimonial-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-lg);
}

.testimonial-card {
  padding: var(--space-xl);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color var(--transition-fast);
}
.testimonial-card:hover {
  border-color: var(--color-primary);
}

.testimonial-top {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-bottom: var(--space-md);
}

.testimonial-name {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.testimonial-role {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

.testimonial-content {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  line-height: var(--line-height-relaxed);
}

/* ============================================
   友情链接 & 热门城市
   ============================================ */
.links-section {
  padding: var(--space-lg) var(--space-xl) !important;
}

.links-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: var(--space-xs) var(--space-md);
  padding: 6px 0;
}

.links-label {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
  white-space: nowrap;
}

.links-item {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  transition: color var(--transition-fast);
  white-space: nowrap;
}

.links-item:hover {
  color: var(--color-primary);
}

/* ============================================
   Responsive
   ============================================ */
@media (max-width: 768px) {
  .home-page { padding: var(--space-md) 0 var(--space-2xl); }
  .home-card { border-radius: 8px; margin: 0 var(--space-sm); }

  /* (导航栏 navbar mobile 样式已删除, 改用全局 SiteHeader 的 mobile media query) */

  /* 轮播图：匹配 banner 3.2:1 纵横比，避免裁剪 */
  .slide-item { height: 140px; background: #eaeff6; }
  .slide-img { object-fit: contain; }
  .card-banner :deep(.el-carousel) { height: 140px !important; }
  .card-banner :deep(.el-carousel__container) { height: 140px !important; }
  .card-banner :deep(.el-carousel__indicators) { bottom: 4px; }
  .card-banner :deep(.el-carousel__indicator .el-carousel__button) {
    width: 18px;
    height: 2px;
  }

  /* 三栏功能区 */
  .card-actions {
    flex-direction: column;
    padding: var(--space-md);
  }
  .action-panel {
    border-radius: 10px;
  }
  .qr-placeholder {
    width: 150px;
    height: 150px;
  }
  .qr-frame-inner {
    padding: 12px;
  }

  /* 分类速查 */
  .category-section { margin: 0 var(--space-sm) var(--space-xl); border-radius: 8px; }
  .category-body { padding: 0 var(--space-md); }
  .category-row { flex-direction: column; padding: var(--space-md) 0; }
  .category-label { margin-bottom: var(--space-sm); width: auto; line-height: 1.4; }
  .category-links-wrap { flex-direction: column; align-items: stretch; width: 100%; gap: var(--space-sm); }
  .category-links {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: var(--space-sm);
    width: 100%;
    min-width: 0;
  }
  .category-links a {
    font-size: 13px;
    line-height: 1.2;
    padding: 7px 4px;
    text-align: center;
    background: var(--color-bg);
    border-radius: 6px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    min-width: 0;
  }
  .category-links-wrap .category-links {
    max-height: 72px;
  }
  .category-links-wrap .category-links.expanded {
    max-height: 1000px;
  }
  .expand-btn {
    align-self: flex-end;
    font-size: var(--font-size-sm);
  }

  /* 下方模块 */
  .section-box { margin: 0 var(--space-sm) var(--space-xl); border-radius: 8px; }
  .tutor-grid { grid-template-columns: repeat(3, 1fr); gap: 10px; }
  .tutor-card { padding: 12px 6px 10px; }
  .testimonial-grid { grid-template-columns: 1fr; }
  .order-right { gap: var(--space-sm); }

  /* 友情链接 & 热门城市 */
  .links-section { padding: var(--space-md) !important; }
  .links-row {
    gap: 6px 10px;
    padding: 6px 0;
  }
  .links-label {
    width: 100%;
    margin-bottom: 2px;
    color: var(--color-text);
    font-weight: var(--font-weight-semibold);
  }
  .links-item {
    font-size: 13px;
    padding: 3px 0;
  }
}
</style>
