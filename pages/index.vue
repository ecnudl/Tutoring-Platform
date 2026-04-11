<template>
  <div class="home-page">
    <Head>
      <Title>591家教网 - 找家教上门辅导一对一</Title>
      <Meta name="description" content="591家教网,提供上门家教一对一辅导服务,大学生家教、专职教员、在职教师,覆盖小学初中高中全科辅导。" />
    </Head>

    <div class="container">
      <!-- ========== 主卡片 ========== -->
      <div class="home-card">

        <!-- ▸ 板块一：顶部导航栏 -->
        <div class="card-nav">
          <!-- 左：Logo -->
          <div class="nav-logo">
            <SiteLogo />
          </div>

          <!-- 中：城市 + 电话 + 搜索（居中偏右） -->
          <div class="nav-center">
            <div class="nav-center-top">
              <el-popover placement="bottom-start" :width="300" trigger="click">
                <template #reference>
                  <span class="city-btn">
                    <el-icon size="14"><Location /></el-icon>
                    {{ cityStore.cityName }}
                    <el-icon size="10"><ArrowDown /></el-icon>
                  </span>
                </template>
                <div class="city-popover">
                  <div class="city-popover-title">选择城市</div>
                  <div class="city-popover-grid">
                    <a
                      v-for="c in cities"
                      :key="c.pinyin"
                      href="javascript:;"
                      class="city-opt"
                      :class="{ active: cityStore.cityName === c.name, disabled: !c.enabled }"
                      @click.prevent="handleCityClick(c)"
                    >{{ c.name }}</a>
                  </div>
                </div>
              </el-popover>
              <span class="nav-phone">
                <el-icon size="13"><Phone /></el-icon>
                13795420591
              </span>
            </div>
            <div class="nav-center-bottom">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索科目"
                size="small"
                clearable
                class="nav-search"
                @keyup.enter="doSearch"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <!-- 中右：品牌名 + 域名 -->
          <div class="nav-brand">
            <div class="nav-brand-name"><span class="brand-main">591家教网</span><span class="brand-sub">名校名师优质家教平台</span></div>
            <div class="nav-brand-domains">
              <span>www.591jiajiao.com</span>
              <span>www.591jiajiao.cn</span>
            </div>
          </div>

          <!-- 右：导航 + 功能链接（靠最右） -->
          <div class="nav-right">
            <div class="nav-right-row">
              <NuxtLink to="/" class="nav-cell">{{ cityStore.cityName }}家教</NuxtLink>
              <NuxtLink to="/jy" class="nav-cell">教员库</NuxtLink>
              <NuxtLink to="/xy" class="nav-cell">学员库</NuxtLink>
              <NuxtLink to="/zf" class="nav-cell">家教价格</NuxtLink>
              <NuxtLink to="/center" class="nav-cell">个人中心</NuxtLink>
            </div>
            <div class="nav-right-row">
              <NuxtLink to="/qjj" class="nav-cell">请家教</NuxtLink>
              <NuxtLink to="/register/teacher" class="nav-cell">做老师</NuxtLink>
              <NuxtLink to="/help" class="nav-cell">帮助</NuxtLink>
              <template v-if="userStore.isLoggedIn">
                <NuxtLink to="/center" class="nav-cell nav-highlight">{{ userStore.mobile || '个人中心' }}</NuxtLink>
                <a href="javascript:;" @click="handleLogout" class="nav-cell">退出</a>
              </template>
              <template v-else>
                <NuxtLink to="/login" class="nav-cell">登录</NuxtLink>
                <NuxtLink to="/register" class="nav-cell nav-highlight">注册</NuxtLink>
              </template>
            </div>
          </div>
        </div>

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
          <!-- 框1：热线 + 两个入口按钮 -->
          <div class="action-panel">
            <div class="panel-hotline">
              <span class="hotline-label">家教热线</span>
              <span class="hotline-number">13795420591</span>
            </div>
            <NuxtLink to="/qjj" class="action-entry">
              <div class="entry-icon find-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
              </div>
              <span>我要找家教</span>
            </NuxtLink>
            <NuxtLink to="/register/teacher" class="action-entry">
              <div class="entry-icon teach-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="24" height="24"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
              </div>
              <span>我要做家教</span>
            </NuxtLink>
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
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="120" height="120"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="3" height="3"/><line x1="21" y1="14" x2="21" y2="21"/><line x1="14" y1="21" x2="21" y2="21"/></svg>
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
              <div v-for="(item, i) in noticeTabs[activeNoticeTab].items" :key="i" class="notice-item">
                <span class="notice-dot"></span>
                <span class="notice-text">{{ item }}</span>
              </div>
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
          <NuxtLink v-for="t in tutors" :key="t.id" :to="'/jy/t' + (t.displayNo || t.id)" class="tutor-card">
            <el-avatar :size="56" :src="t.avatar" />
            <div class="tutor-info">
              <div class="tutor-name">{{ t.realName || '教员' }}</div>
              <div class="tutor-school">{{ t.university || '未填写' }}</div>
              <div class="tutor-price" v-if="t.priceMin">{{ t.priceMin }}-{{ t.priceMax }}元/时</div>
            </div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无教员数据" />
      </div>

      <!-- ========== 家教最新订单 ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>{{ cityStore.cityName }}家教最新订单</h2>
          <NuxtLink to="/xy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>
        <div class="order-list">
          <div v-for="(order, idx) in latestOrders" :key="idx" class="order-item">
            <div class="order-left">
              <span class="order-tag" :class="order.tagClass">{{ order.tag }}</span>
              <span class="order-title">{{ order.title }}</span>
            </div>
            <div class="order-right">
              <span class="order-area">{{ order.area }}</span>
              <span class="order-time">{{ order.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- ========== 家教感言 ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>{{ cityStore.cityName }}家教感言</h2>
        </div>
        <div class="testimonial-grid">
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
      </div>

      <!-- ========== 友情链接 & 热门城市 ========== -->
      <div class="section-box links-section">
        <div class="links-row">
          <span class="links-label">友情链接：</span>
          <a href="https://www.ttgood.com" target="_blank" rel="noopener noreferrer" class="links-item">天天家教网</a>
        </div>
        <div class="links-row">
          <span class="links-label">热门城市：</span>
          <NuxtLink v-for="c in cities" :key="c.pinyin" :to="'/city?name=' + c.name" class="links-item">{{ c.name }}</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Phone, Search, Location, ArrowDown, ArrowUp } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const userStore = useUserStore()
const cityStore = useCityStore()
const { districts, universities } = useCityData()
const { post } = useApi()
const router = useRouter()

const searchKeyword = ref('')
const tutors = ref([])
const activeNoticeTab = ref(0)

const latestOrders = [
  { tag: '急', tagClass: 'tag-urgent', title: '高二数学，每周两次上门辅导', area: '浦东新区', time: '1小时前' },
  { tag: '新', tagClass: 'tag-new', title: '小学三年级英语，启蒙阶段', area: '徐汇区', time: '2小时前' },
  { tag: '新', tagClass: 'tag-new', title: '初三物理化学，冲刺中考', area: '闵行区', time: '3小时前' },
  { tag: '急', tagClass: 'tag-urgent', title: '钢琴陪练，5岁女孩入门', area: '长宁区', time: '4小时前' },
  { tag: '新', tagClass: 'tag-new', title: '高一语文作文专项提升', area: '杨浦区', time: '5小时前' },
  { tag: '新', tagClass: 'tag-new', title: '雅思口语7分冲刺辅导', area: '静安区', time: '6小时前' },
]

const testimonials = [
  { name: '张女士', role: '学生家长', content: '老师非常耐心，孩子数学从70分提到了95分，非常感谢平台推荐的教员！' },
  { name: '李同学', role: '大学生教员', content: '平台派单很快，家长也很好沟通，一个月接了5个学生，课时费准时结算。' },
  { name: '王先生', role: '学生家长', content: '给孩子找了一位复旦的英语家教，口语提升很明显，试讲免费这个政策很好。' },
  { name: '陈老师', role: '专职教员', content: '在平台上教了两年了，学生资源稳定，平台不收中介费，比之前的机构好很多。' },
]
const subjectExpanded = ref(false)
const universityExpanded = ref(false)

const allSubjects = [
  { name: '幼儿学前' }, { name: '小学全科' }, { name: '初中理科' }, { name: '初中文科' },
  { name: '高中理科' }, { name: '高中文科' }, { name: '语文' }, { name: '英语' },
  { name: '数学' }, { name: '奥数' }, { name: '物理' }, { name: '化学' },
  { name: '生物' }, { name: '历史' }, { name: '地理' }, { name: '政治' },
  { name: '钢琴' }, { name: '小提琴' }, { name: '古筝' }, { name: '跳绳' },
  { name: '篮球' }, { name: '游泳' }, { name: '围棋' }, { name: '书法' },
  { name: '美术' }, { name: '英语口语' }, { name: '四级' }, { name: '托福' },
  { name: '雅思' }, { name: 'SAT' }, { name: 'AP' }, { name: 'A-level' },
  { name: 'IB' }, { name: 'IGCSE' }, { name: '日语' }, { name: '德语' },
  { name: '高数' }, { name: '计算机' }, { name: '初中' }, { name: '高中' },
  { name: '大学考研' }, { name: '高考志愿填报' }, { name: '生涯规划' }, { name: '心理辅导' }
]

const noticeTabs = [
  {
    label: '网站公告',
    items: [
      '平台服务升级维护通知',
      '五一假期客服时间调整',
      '教员认证流程优化公告',
      '新增苏州、南京站点上线',
    ]
  },
  {
    label: '教育资讯',
    items: [
      '2025年上海中考政策解读',
      '小学数学思维训练方法分享',
      '初中英语听力提分技巧',
      '高考志愿填报注意事项',
    ]
  },
  {
    label: '教/学员须知',
    items: [
      '首次试讲免费，满意后再上课',
      '课时费由家长与教员直接结算',
      '教员需通过实名认证方可接单',
      '如遇问题请拨打客服热线反馈',
    ]
  }
]

// 轮播图数据
const slides = ref([
  { id: 1, title: '专业家教上门辅导', imageUrl: '', link: '/qjj' },
  { id: 2, title: '优质教员等你来', imageUrl: '', link: '/jy' },
  { id: 3, title: '新学期辅导计划', imageUrl: '', link: '/qjj' }
])

const cities = [
  { name: '上海', pinyin: 'shanghai', id: 1, enabled: true },
  { name: '南京', pinyin: 'nanjing', id: 5, enabled: true },
  { name: '苏州', pinyin: 'suzhou', id: 2, enabled: true },
  { name: '杭州', pinyin: 'hangzhou', id: 4, enabled: true },
  { name: '合肥', pinyin: 'hefei', id: 3, enabled: true },
  { name: '福州', pinyin: 'fuzhou', id: 6, enabled: true },
  { name: '南昌', pinyin: 'nanchang', id: 8, enabled: true },
  { name: '济南', pinyin: 'jinan', id: 7, enabled: true }
]

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通`)
    return
  }
  cityStore.setCity(city.id, city.name)
  ElMessage.success(`已切换到${city.name}`)
}

const doSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/jy', query: { keyword: searchKeyword.value.trim() } })
  } else {
    router.push('/jy')
  }
}

const handleLogout = () => { userStore.logout(); router.push('/') }

onMounted(async () => {
  cityStore.loadFromStorage()

  // 加载轮播图
  const stored = localStorage.getItem('carousel_slides')
  if (stored) {
    try {
      const parsed = JSON.parse(stored)
      if (parsed.length) slides.value = parsed
    } catch (e) { /* use defaults */ }
  }

  try {
    const tRes = await post('/user/api/tutor/search', { pageCurrent: 1, pageSize: 8 })
    if (tRes.code === 200 && tRes.data) tutors.value = tRes.data.list || []
  } catch (e) {
    console.error(e)
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

/* ============================================
   板块一：顶部导航栏
   ============================================ */
.card-nav {
  display: flex;
  align-items: stretch;
  border-bottom: 1px solid var(--color-border-light);
  min-height: 100px;
}

/* 左：Logo */
.nav-logo {
  display: flex;
  align-items: center;
  padding: 0 var(--space-xl);
  flex-shrink: 0;
  border-right: 1px solid var(--color-border-light);
}

/* 中：城市+电话 / 搜索 */
.nav-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-sm) var(--space-xl);
  gap: 6px;
  flex-shrink: 0;
}

.nav-center-top {
  display: flex;
  align-items: center;
  gap: var(--space-xl);
}

/* 城市按钮样式 */
.city-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  cursor: pointer;
  padding: 4px 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  white-space: nowrap;
}
.city-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
  background: var(--color-primary-lighter);
}

.nav-phone {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-md);
  color: var(--color-text);
  font-weight: var(--font-weight-semibold);
}

.nav-search {
  width: 220px;
}
.nav-search :deep(.el-input__wrapper) {
  border-radius: var(--radius-sm);
}

/* 中右：品牌名 + 域名 */
.nav-brand {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: center;
  padding: var(--space-sm) var(--space-lg);
  border-right: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.nav-brand-name {
  display: flex;
  align-items: baseline;
  gap: 6px;
  white-space: nowrap;
}

.brand-main {
  font-family: "SimHei", "STHeiti", "Heiti SC", sans-serif;
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary);
  letter-spacing: 2px;
}

.brand-sub {
  font-size: 15px;
  font-weight: 400;
  color: var(--color-text-secondary);
  letter-spacing: 1px;
}

.nav-brand-domains {
  display: flex;
  justify-content: center;
  gap: 14px;
  width: 100%;
  margin-top: 4px;
}

.nav-brand-domains span {
  font-family: "Times New Roman", Times, serif;
  font-size: 14px;
  font-weight: 600;
  color: #000;
  letter-spacing: 0.5px;
}

/* 右：5列等宽网格，上下两排完全对齐 */
.nav-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-md) var(--space-lg);
  gap: 8px;
  margin-left: auto;
  flex-shrink: 0;
}

.nav-right-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0;
}

.nav-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px 8px;
  font-size: 15px;
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  white-space: nowrap;
  transition: color var(--transition-fast);
  text-align: center;
}
.nav-cell:hover {
  color: var(--color-primary);
}

.nav-highlight {
  color: var(--color-primary);
}

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

/* --- 框1：热线 + 两个入口按钮 --- */
.panel-hotline {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding: 0 20px var(--space-md);
  margin-bottom: var(--space-sm);
  border-bottom: 1px solid var(--color-border-light);
}

.hotline-label {
  font-size: var(--font-size-md);
  color: var(--color-text);
  font-weight: var(--font-weight-medium);
}

.hotline-number {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-primary);
  letter-spacing: 0.5px;
}

.action-entry {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  padding: 14px 20px;
  border-radius: var(--radius-lg);
  transition: background var(--transition-fast);
  cursor: pointer;
}
.action-entry:hover {
  background: var(--color-primary-lighter);
}
.action-entry + .action-entry {
  margin-top: var(--space-md);
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
.find-icon {
  background: var(--color-primary);
}
.teach-icon {
  background: var(--color-accent);
}

.action-entry span {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
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

.tutor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-lg);
}

.tutor-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color var(--transition-fast);
}
.tutor-card:hover { border-color: var(--color-primary); }

.tutor-info { text-align: center; margin-top: var(--space-md); width: 100%; }
.tutor-name { font-weight: var(--font-weight-semibold); margin-bottom: 2px; }
.tutor-school { font-size: var(--font-size-sm); color: var(--color-text-muted); margin-bottom: 4px; }
.tutor-price { font-weight: var(--font-weight-semibold); color: var(--color-accent-dark); }

/* ============================================
   最新订单
   ============================================ */
.order-list {
  display: flex;
  flex-direction: column;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid var(--color-border-light);
}
.order-item:last-child { border-bottom: none; }

.order-left {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  min-width: 0;
}

.order-tag {
  display: inline-block;
  padding: 1px 6px;
  font-size: 11px;
  font-weight: var(--font-weight-bold);
  border-radius: 3px;
  flex-shrink: 0;
}
.tag-urgent {
  background: #FEE2E2;
  color: #DC2626;
}
.tag-new {
  background: var(--color-primary-lighter);
  color: var(--color-primary);
}

.order-title {
  font-size: var(--font-size-base);
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-right {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  flex-shrink: 0;
  margin-left: var(--space-lg);
}

.order-area {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  white-space: nowrap;
}

.order-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  white-space: nowrap;
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
  align-items: center;
  gap: var(--space-sm);
  padding: 4px 0;
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

  /* 导航栏：竖排 */
  .card-nav {
    flex-direction: column;
  }
  .nav-logo {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    justify-content: center;
    padding: var(--space-md);
  }
  .nav-center {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    min-width: auto;
    padding: var(--space-md) var(--space-lg);
    align-items: center;
  }
  .nav-brand {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    padding: var(--space-md);
  }
  .nav-right {
    padding: var(--space-md) var(--space-lg);
    width: 100%;
  }
  .nav-right-row {
    grid-template-columns: repeat(5, 1fr);
    justify-items: center;
    gap: 0;
  }
  .nav-cell {
    font-size: var(--font-size-sm);
    padding: 4px 6px;
  }

  /* 轮播图 */
  .slide-item { height: 200px; }
  .card-banner :deep(.el-carousel) { height: 200px !important; }

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
  .category-row { flex-direction: column; }
  .category-label { margin-bottom: var(--space-xs); }
  .category-links-wrap { flex-direction: column; align-items: flex-start; }

  /* 下方模块 */
  .section-box { margin: 0 var(--space-sm) var(--space-xl); border-radius: 8px; }
  .tutor-grid { grid-template-columns: repeat(2, 1fr); }
  .testimonial-grid { grid-template-columns: 1fr; }
  .order-right { gap: var(--space-sm); }
}
</style>
