<template>
  <div class="layout-default">
    <header class="site-header">
      <div class="header-main">
        <div class="container header-inner">
          <div class="header-left">
            <SiteLogo />
            <el-popover placement="bottom-start" :width="300" trigger="click">
              <template #reference>
                <span class="city-trigger">
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
                    :href="c.enabled ? c.url : 'javascript:;'"
                    class="city-opt"
                    :class="{ active: cityStore.cityName === c.name, disabled: !c.enabled }"
                    @click.prevent="handleCityClick(c)"
                  >
                    {{ c.name }}
                    <span v-if="!c.enabled" class="city-coming">即将开通</span>
                  </a>
                </div>
              </div>
            </el-popover>
          </div>

          <nav class="nav-links desktop-only">
            <NuxtLink to="/">首页</NuxtLink>
            <NuxtLink to="/jy">教员库</NuxtLink>
            <NuxtLink to="/xy">学员需求</NuxtLink>
            <NuxtLink to="/qjj">请家教</NuxtLink>
            <NuxtLink to="/zf">价格参考</NuxtLink>
            <NuxtLink to="/help">帮助</NuxtLink>
            <a href="javascript:;" class="nav-announce" @click="announcementVisible = true">
              公告
              <span v-if="hasNewAnnouncement" class="nav-new-badge">new</span>
            </a>
          </nav>

          <div class="header-right desktop-only">
            <div class="header-search">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索科目/年级"
                size="default"
                clearable
                @keyup.enter="doSearch"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </div>

            <template v-if="userStore.isLoggedIn">
              <NuxtLink to="/center" class="user-link">
                <el-icon size="14"><User /></el-icon>
                {{ userStore.mobile }}
              </NuxtLink>
              <NuxtLink to="/center/favorites" class="header-link">备选</NuxtLink>
              <template v-if="userStore.isAdmin">
                <NuxtLink to="/admin/announcements" class="header-link admin-link">管理</NuxtLink>
              </template>
              <a href="javascript:;" @click="handleLogout" class="header-link">退出</a>
            </template>
            <template v-else>
              <NuxtLink to="/login" class="header-link">登录</NuxtLink>
              <NuxtLink to="/register" class="btn-register">注册</NuxtLink>
            </template>

            <span class="hotline">
              <el-icon size="13"><Phone /></el-icon>
              400-000-0000
            </span>
          </div>
        </div>
      </div>

      <!-- Mobile hamburger -->
      <div class="mobile-menu-btn mobile-only" @click="mobileMenuOpen = !mobileMenuOpen">
        <span></span><span></span><span></span>
      </div>

      <!-- Mobile menu -->
      <div class="mobile-menu mobile-only" v-show="mobileMenuOpen" @click="mobileMenuOpen = false">
        <div class="mobile-search">
          <el-input v-model="searchKeyword" placeholder="搜索科目/年级" size="default" clearable @keyup.enter="doSearch">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
        </div>
        <NuxtLink to="/">首页</NuxtLink>
        <NuxtLink to="/jy">教员库</NuxtLink>
        <NuxtLink to="/xy">学员需求</NuxtLink>
        <NuxtLink to="/qjj">请家教</NuxtLink>
        <NuxtLink to="/zf">价格参考</NuxtLink>
        <NuxtLink to="/help">帮助</NuxtLink>
        <a href="javascript:;" @click="announcementVisible = true; mobileMenuOpen = false">公告</a>
        <template v-if="userStore.isLoggedIn">
          <NuxtLink to="/center">个人中心</NuxtLink>
          <a @click="handleLogout" style="color:var(--color-error);cursor:pointer">退出登录</a>
        </template>
        <template v-else>
          <NuxtLink to="/login">登录</NuxtLink>
          <NuxtLink to="/register">注册</NuxtLink>
        </template>
      </div>
    </header>

    <main class="page-content"><slot /></main>

    <!-- 公告弹窗 -->
    <el-dialog v-model="announcementVisible" title="网站公告" width="600px" :close-on-click-modal="false">
      <div class="announcement-list">
        <div
          v-for="item in announcements"
          :key="item.id"
          class="announcement-item"
          @click="showAnnouncementDetail(item)"
        >
          <div class="ann-header">
            <h3 class="ann-title">
              {{ item.title }}
              <span v-if="isNewAnnouncement(item)" class="ann-new-tag">NEW</span>
            </h3>
            <span class="ann-time">{{ formatDate(item.gmtCreate) }}</span>
          </div>
          <div class="ann-preview">{{ item.content.substring(0, 50) }}...</div>
        </div>
        <el-empty v-if="!announcements.length" description="暂无公告" />
      </div>
    </el-dialog>

    <!-- 公告详情弹窗 -->
    <el-dialog v-model="announcementDetailVisible" :title="currentAnnouncement?.title" width="600px">
      <div class="announcement-detail">
        <div class="detail-time">发布时间：{{ formatDate(currentAnnouncement?.gmtCreate) }}</div>
        <div class="detail-content" v-html="currentAnnouncement?.content"></div>
      </div>
    </el-dialog>

    <SiteFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Location, ArrowDown, Search, Phone, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()
const mobileMenuOpen = ref(false)
const searchKeyword = ref('')

// 公告相关
const announcementVisible = ref(false)
const announcementDetailVisible = ref(false)
const currentAnnouncement = ref(null)
const announcements = ref([])
const readIds = ref([])

const mockAnnouncements = [
  { id: 1, title: '欢迎使用51家教网', content: '51家教网致力于为学员和家长提供优质的家教服务，我们拥有大量经过认证的优秀教员，覆盖小学、初中、高中各个年级和科目。', gmtCreate: new Date().toISOString(), status: 1 },
  { id: 2, title: '平台服务升级通知', content: '为了给您提供更好的服务体验，我们将在本周末进行系统升级维护，届时部分功能可能暂时无法使用，预计维护时间为2小时，给您带来的不便敬请谅解。', gmtCreate: new Date(Date.now() - 86400000).toISOString(), status: 1 },
  { id: 3, title: '教员认证流程优化', content: '为了提高教员认证效率，我们优化了认证流程，现在只需要上传学生证或教师资格证即可快速完成认证，审核时间缩短至24小时内。', gmtCreate: new Date(Date.now() - 172800000).toISOString(), status: 1 }
]

const hasNewAnnouncement = computed(() => {
  return announcements.value.some(a => !readIds.value.includes(a.id) && isNewAnnouncement(a))
})

const isNewAnnouncement = (item) => {
  const threeDaysAgo = Date.now() - 3 * 24 * 60 * 60 * 1000
  return new Date(item.gmtCreate).getTime() > threeDaysAgo
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const showAnnouncementDetail = (item) => {
  currentAnnouncement.value = item
  announcementDetailVisible.value = true
  if (!readIds.value.includes(item.id)) {
    readIds.value.push(item.id)
    localStorage.setItem('announcement_read_ids', JSON.stringify(readIds.value))
  }
}

const cities = [
  { name: '上海', pinyin: 'shanghai', id: 1, enabled: true, url: '/' },
  { name: '苏州', pinyin: 'suzhou', id: 2, enabled: true, url: '/' },
  { name: '南京', pinyin: 'nanjing', id: 5, enabled: true, url: '/' },
  { name: '合肥', pinyin: 'hefei', id: 3, enabled: true, url: '/' },
  { name: '杭州', pinyin: 'hangzhou', id: 4, enabled: true, url: '/' }
]

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通，敬请期待`)
    return
  }
  cityStore.setCity(city.id, city.name)
  ElMessage.success(`已切换到${city.name}`)
  router.push('/')
}

const doSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/jy', query: { keyword: searchKeyword.value.trim() } })
  } else {
    router.push('/jy')
  }
}

const handleLogout = () => { userStore.logout(); router.push('/') }

onMounted(() => {
  const stored = localStorage.getItem('announcements')
  if (stored) {
    try { announcements.value = JSON.parse(stored) } catch (e) { announcements.value = mockAnnouncements }
  } else {
    announcements.value = mockAnnouncements
    localStorage.setItem('announcements', JSON.stringify(mockAnnouncements))
  }
  const readStored = localStorage.getItem('announcement_read_ids')
  if (readStored) {
    try { readIds.value = JSON.parse(readStored) } catch (e) { readIds.value = [] }
  }
  window.addEventListener('storage', (e) => {
    if (e.key === 'announcements' && e.newValue) {
      try { announcements.value = JSON.parse(e.newValue) } catch (err) { /* ignore */ }
    }
  })
})
</script>

<style scoped>
.layout-default {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* Header */
.site-header {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-main {
  background: var(--color-surface);
}

.header-inner {
  display: flex;
  align-items: center;
  height: 56px;
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
  gap: var(--space-lg);
}

/* Left: Logo + City */
.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  flex-shrink: 0;
}

.city-trigger {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-border);
  transition: all var(--transition-fast);
  white-space: nowrap;
}
.city-trigger:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
}

/* City popover */
.city-popover-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: var(--space-md);
}
.city-popover-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-sm);
}
.city-opt {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 8px;
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  color: var(--color-text);
  background: var(--color-bg);
  transition: all var(--transition-fast);
  text-decoration: none;
}
.city-opt:hover { background: var(--color-primary-lighter); color: var(--color-primary); }
.city-opt.active { background: var(--color-primary); color: #fff; }
.city-opt.disabled { opacity: 0.5; cursor: not-allowed; }
.city-opt.disabled:hover { background: var(--color-bg); color: var(--color-text-muted); }
.city-coming { font-size: 10px; color: var(--color-text-muted); margin-top: 2px; }
.city-opt.active .city-coming { color: rgba(255,255,255,0.7); }

/* Navigation */
.nav-links {
  display: flex;
  gap: var(--space-xl);
  margin-left: var(--space-2xl);
}
.nav-links a {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  white-space: nowrap;
  padding: 4px 0;
  transition: color var(--transition-fast);
}
.nav-links a:hover,
.nav-links a.router-link-active {
  color: var(--color-primary);
}

/* Nav announce button */
.nav-announce {
  position: relative;
  display: inline-flex;
  align-items: center;
}

.nav-new-badge {
  position: absolute;
  top: -8px;
  right: -18px;
  background: var(--color-error);
  color: #fff;
  font-size: 10px;
  font-weight: var(--font-weight-bold);
  line-height: 1;
  padding: 2px 4px;
  border-radius: var(--radius-sm);
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

/* Announcement dialog */
.announcement-list {
  max-height: 500px;
  overflow-y: auto;
}

.announcement-item {
  padding: var(--space-lg);
  border-bottom: 1px solid var(--color-border-light);
  cursor: pointer;
  transition: background var(--transition-fast);
}
.announcement-item:hover { background: var(--color-bg); }
.announcement-item:last-child { border-bottom: none; }

.ann-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-sm);
}

.ann-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  display: flex;
  align-items: center;
  gap: var(--space-sm);
}

.ann-new-tag {
  display: inline-block;
  background: var(--color-error);
  color: #fff;
  font-size: 10px;
  font-weight: var(--font-weight-bold);
  padding: 1px 5px;
  border-radius: var(--radius-sm);
  line-height: 1.4;
}

.ann-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  white-space: nowrap;
}

.ann-preview {
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  line-height: var(--line-height-normal);
}

.announcement-detail { padding: var(--space-lg) 0; }
.detail-time {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-md);
  border-bottom: 1px solid var(--color-border-light);
}
.detail-content {
  font-size: var(--font-size-base);
  color: var(--color-text);
  line-height: var(--line-height-relaxed);
  white-space: pre-wrap;
}

/* Right area */
.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  margin-left: auto;
  flex-shrink: 0;
}

.header-search {
  width: 170px;
}
.header-search :deep(.el-input__wrapper) {
  border-radius: var(--radius-lg);
}

.user-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.header-link {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  transition: color var(--transition-fast);
  cursor: pointer;
}
.header-link:hover {
  color: var(--color-primary);
}

.admin-link {
  color: var(--color-accent);
}

.btn-register {
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  border: 1px solid var(--color-primary);
  padding: 4px 12px;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}
.btn-register:hover {
  background: var(--color-primary);
  color: #fff;
}

.hotline {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  white-space: nowrap;
  padding-left: var(--space-md);
  border-left: 1px solid var(--color-border);
}

.page-content { flex: 1; }

/* Mobile hamburger */
.mobile-menu-btn {
  display: flex;
  flex-direction: column;
  gap: 5px;
  cursor: pointer;
  padding: 8px;
  margin-left: auto;
}
.mobile-menu-btn span {
  display: block;
  width: 22px;
  height: 2px;
  background: var(--color-text);
  border-radius: 1px;
}

/* Mobile menu */
.mobile-menu {
  background: var(--color-surface);
  border-top: 1px solid var(--color-border-light);
  padding: var(--space-md) var(--space-xl);
}
.mobile-menu a {
  display: block;
  padding: 12px 0;
  color: var(--color-text);
  font-size: var(--font-size-md);
  border-bottom: 1px solid var(--color-border-light);
}
.mobile-menu a:last-child { border-bottom: none; }
.mobile-search { margin-bottom: var(--space-sm); }

/* Responsive */
.mobile-only { display: none; }
@media (max-width: 768px) {
  .desktop-only { display: none !important; }
  .mobile-only { display: block; }
  .mobile-menu-btn { display: flex; }
  .header-inner { gap: var(--space-sm); }
}
</style>
