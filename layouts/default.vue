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

    <SiteFooter />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Location, ArrowDown, Search, Phone, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()
const mobileMenuOpen = ref(false)
const searchKeyword = ref('')

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
