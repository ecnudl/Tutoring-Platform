<template>
  <div class="home-page">
    <Head>
      <Title>51家教网 - 找家教上门辅导一对一</Title>
      <Meta name="description" content="51家教网,提供上门家教一对一辅导服务,大学生家教、专职教员、在职教师,覆盖小学初中高中全科辅导。" />
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
                400-000-0000
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
          <el-carousel height="380px" :interval="5000" arrow="hover" indicator-position="outside">
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

        <!-- ▸ 板块三：三个入口方块 -->
        <div class="card-actions">
          <NuxtLink to="/qjj" class="action-box">
            <div class="action-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="28" height="28"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </div>
            <div class="action-text">
              <div class="action-title">我是家长</div>
              <div class="action-sub">找最家教</div>
            </div>
          </NuxtLink>

          <NuxtLink to="/register/teacher" class="action-box">
            <div class="action-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="28" height="28"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>
            </div>
            <div class="action-text">
              <div class="action-title">我是教员</div>
              <div class="action-sub">找最教员</div>
            </div>
          </NuxtLink>

          <div class="action-box action-hotline">
            <div class="action-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="28" height="28"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
            </div>
            <div class="action-text">
              <div class="action-title">家教热线</div>
              <div class="action-phone">400-000-0000</div>
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
            <div class="category-links">
              <NuxtLink v-for="s in hotSubjects" :key="s.id" :to="'/jy?subject=' + s.id">{{ s.name }}</NuxtLink>
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
            <div class="category-links">
              <NuxtLink v-for="u in universities" :key="u" :to="'/jy?university=' + encodeURIComponent(u)">{{ u }}</NuxtLink>
              <NuxtLink to="/university" class="more-link">更多 &rarr;</NuxtLink>
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

      <!-- ========== 热门科目 ========== -->
      <div class="hot-subjects">
        <div class="hot-title">热门科目</div>
        <div class="hot-grid">
          <NuxtLink v-for="item in hotCombos" :key="item.label" :to="item.link" class="hot-tag">{{ item.label }}</NuxtLink>
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

      <!-- ========== 最新需求 ========== -->
      <div class="section-box">
        <div class="section-header">
          <h2>最新学员需求</h2>
          <NuxtLink to="/xy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>
        <div v-if="requirements.length" class="req-list">
          <NuxtLink v-for="r in requirements" :key="r.id" :to="'/xy/a' + r.id" class="req-item">
            <div class="req-title">{{ r.title || '家教需求' }}</div>
            <div class="req-budget" v-if="r.budgetMin">预算: {{ r.budgetMin }}-{{ r.budgetMax }}元/小时</div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无需求数据" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Phone, Search, Location, ArrowDown } from '@element-plus/icons-vue'
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
const requirements = ref([])

// 轮播图数据
const slides = ref([
  { id: 1, title: '专业家教上门辅导', imageUrl: '', link: '/qjj' },
  { id: 2, title: '优质教员等你来', imageUrl: '', link: '/jy' },
  { id: 3, title: '新学期辅导计划', imageUrl: '', link: '/qjj' }
])

const cities = [
  { name: '上海', pinyin: 'shanghai', id: 1, enabled: true },
  { name: '苏州', pinyin: 'suzhou', id: 2, enabled: true },
  { name: '南京', pinyin: 'nanjing', id: 5, enabled: true },
  { name: '合肥', pinyin: 'hefei', id: 3, enabled: true },
  { name: '杭州', pinyin: 'hangzhou', id: 4, enabled: true }
]

const hotSubjects = [
  { id: 1, name: '数学' }, { id: 2, name: '英语' }, { id: 3, name: '语文' }, { id: 4, name: '物理' },
  { id: 5, name: '化学' }, { id: 6, name: '钢琴' }, { id: 7, name: '美术' }, { id: 8, name: '编程' },
  { id: 9, name: '日语' }, { id: 10, name: '法语' }, { id: 11, name: '小提琴' }, { id: 12, name: '书法' }
]

const hotCombos = [
  { label: '小学数学', link: '/jy?subject=1&grade=1' },
  { label: '小学英语', link: '/jy?subject=2&grade=1' },
  { label: '小学语文', link: '/jy?subject=3&grade=1' },
  { label: '初中数学', link: '/jy?subject=1&grade=2' },
  { label: '初中英语', link: '/jy?subject=2&grade=2' },
  { label: '初中物理', link: '/jy?subject=4&grade=2' },
  { label: '初中化学', link: '/jy?subject=5&grade=2' },
  { label: '高中数学', link: '/jy?subject=1&grade=3' },
  { label: '高中英语', link: '/jy?subject=2&grade=3' },
  { label: '高中物理', link: '/jy?subject=4&grade=3' },
  { label: '高中化学', link: '/jy?subject=5&grade=3' },
  { label: '钢琴陪练', link: '/jy?subject=6' },
  { label: '美术绘画', link: '/jy?subject=7' },
  { label: '书法练字', link: '/jy?subject=12' },
  { label: '日语辅导', link: '/jy?subject=9' },
  { label: '编程入门', link: '/jy?subject=8' },
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
    const [tRes, rRes] = await Promise.all([
      post('/user/api/tutor/search', { pageCurrent: 1, pageSize: 8 }),
      post('/user/api/requirement/search', { pageCurrent: 1, pageSize: 6 })
    ])
    if (tRes.code === 200 && tRes.data) tutors.value = tRes.data.list || []
    if (rRes.code === 200 && rRes.data) requirements.value = rRes.data.list || []
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
  padding: 0 var(--space-2xl);
  flex-shrink: 0;
  border-right: 1px solid var(--color-border-light);
}

/* 中：城市+电话 / 搜索 */
.nav-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-md) var(--space-3xl);
  gap: 8px;
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

/* 右：5列等宽网格，上下两排完全对齐 */
.nav-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-md) var(--space-2xl);
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
  padding: 4px 12px;
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

.card-banner :deep(.el-carousel__indicators--outside) {
  margin-top: -30px;
  position: relative;
  z-index: 2;
}

.card-banner :deep(.el-carousel__indicator .el-carousel__button) {
  background: var(--color-primary);
  opacity: 0.3;
}
.card-banner :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  opacity: 1;
}

/* ============================================
   板块三：三个入口方块
   ============================================ */
.card-actions {
  display: flex;
  border-top: 1px solid var(--color-border-light);
}

.action-box {
  flex: 1;
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  padding: var(--space-xl) var(--space-2xl);
  border-right: 1px solid var(--color-border-light);
  transition: background var(--transition-fast);
  cursor: pointer;
}
.action-box:last-child {
  border-right: none;
}
.action-box:hover {
  background: var(--color-primary-lighter);
}

.action-icon {
  width: 52px;
  height: 52px;
  background: var(--color-primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.action-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  line-height: 1.4;
}

.action-sub {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}

.action-phone {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-primary);
  letter-spacing: 0.5px;
}

.action-hotline {
  cursor: default;
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
   热门科目
   ============================================ */
.hot-subjects {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--space-2xl);
  overflow: hidden;
}

.hot-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  padding: var(--space-md) var(--space-xl);
  background: var(--color-bg);
  border-bottom: 2px solid var(--color-primary);
}

.hot-grid {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-md);
  padding: var(--space-lg) var(--space-xl);
}

.hot-tag {
  padding: 6px 16px;
  background: var(--color-primary-lighter);
  color: var(--color-primary);
  font-size: var(--font-size-sm);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}
.hot-tag:hover {
  background: var(--color-primary);
  color: #fff;
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

.req-list { display: flex; flex-direction: column; gap: var(--space-md); }
.req-item {
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color var(--transition-fast);
}
.req-item:hover { border-color: var(--color-primary); }
.req-title { font-weight: var(--font-weight-medium); margin-bottom: var(--space-sm); }
.req-budget { font-weight: var(--font-weight-semibold); color: var(--color-accent-dark); }

/* ============================================
   Responsive
   ============================================ */
@media (max-width: 768px) {
  .home-page { padding: var(--space-md) 0 var(--space-2xl); }
  .home-card { border-radius: 8px; }

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
  }
  .nav-right {
    padding: var(--space-md) var(--space-lg);
  }
  .nav-right-top {
    flex-wrap: wrap;
    gap: var(--space-md);
  }

  .slide-item { height: 200px; }
  .card-banner :deep(.el-carousel) { height: 200px !important; }

  .card-actions {
    flex-direction: column;
  }
  .action-box {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
  }
  .action-box:last-child { border-bottom: none; }

  .category-row { flex-direction: column; }
  .category-label { margin-bottom: var(--space-xs); }

  .tutor-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
