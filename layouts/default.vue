<template>
  <div class="layout-default">
    <header class="site-header">
      <!-- 第一行：Logo + 城市 + 主导航 -->
      <div class="header-top">
        <div class="container top-inner">
          <div class="header-left">
            <NuxtLink to="/" class="logo">
              <span class="logo-icon">&#9733;</span>
              <span class="logo-text">51家教网</span>
            </NuxtLink>
            <el-popover placement="bottom-start" :width="320" trigger="click">
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
            <NuxtLink to="/xy">学员库</NuxtLink>
            <NuxtLink to="/qjj">请家教</NuxtLink>
            <NuxtLink to="/center">个人中心</NuxtLink>
          </nav>

          <div class="header-search desktop-only">
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
        </div>
      </div>

      <!-- 第二行：价格 + 做老师 + 帮助 + 用户信息 -->
      <div class="header-bottom">
        <div class="container bottom-inner">
          <nav class="bottom-nav desktop-only">
            <NuxtLink to="/zf">家教价格</NuxtLink>
            <span class="divider">|</span>
            <NuxtLink to="/register/teacher">做老师</NuxtLink>
            <span class="divider">|</span>
            <NuxtLink to="/help">帮助</NuxtLink>
            <template v-if="userStore.isAdmin">
              <span class="divider">|</span>
              <NuxtLink to="/admin/announcements" style="color:#e6a23c">管理后台</NuxtLink>
            </template>
            <span class="divider">|</span>
            <template v-if="userStore.isLoggedIn">
              <span class="user-greeting">Hi, {{ userStore.mobile }}</span>
              <span class="divider">|</span>
              <NuxtLink to="/center/favorites">备选</NuxtLink>
              <span class="divider">|</span>
              <a href="javascript:;" @click="handleLogout" class="logout-link">退出</a>
            </template>
            <template v-else>
              <NuxtLink to="/login">登录</NuxtLink>
              <span class="divider">|</span>
              <NuxtLink to="/register">注册</NuxtLink>
            </template>
          </nav>
          <span class="hotline-mini desktop-only">
            <el-icon size="14"><Phone /></el-icon>
            400-000-0000
          </span>
        </div>
      </div>

      <!-- 移动端汉堡菜单 -->
      <div class="mobile-menu-btn mobile-only" @click="mobileMenuOpen = !mobileMenuOpen">
        <span></span><span></span><span></span>
      </div>

      <!-- 移动端展开菜单 -->
      <div class="mobile-menu mobile-only" v-show="mobileMenuOpen" @click="mobileMenuOpen = false">
        <div class="mobile-search">
          <el-input v-model="searchKeyword" placeholder="搜索科目/年级" size="default" clearable @keyup.enter="doSearch">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
        </div>
        <NuxtLink to="/">首页</NuxtLink>
        <NuxtLink to="/jy">教员库</NuxtLink>
        <NuxtLink to="/xy">学员库</NuxtLink>
        <NuxtLink to="/qjj">请家教</NuxtLink>
        <NuxtLink to="/zf">家教价格</NuxtLink>
        <template v-if="userStore.isLoggedIn">
          <NuxtLink to="/center">个人中心</NuxtLink>
          <a @click="handleLogout" style="color:#f56c6c;cursor:pointer">退出登录</a>
        </template>
        <template v-else>
          <NuxtLink to="/login">登录</NuxtLink>
          <NuxtLink to="/register">注册</NuxtLink>
        </template>
      </div>
    </header>
    <main class="main-content"><slot /></main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Location, ArrowDown, Search, Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()
const mobileMenuOpen = ref(false)
const searchKeyword = ref('')

// 城市配置：上海主域名，其他子域名
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
.site-header {
  background: #fff;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

/* 第一行 */
.header-top {
  background: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}
.top-inner {
  display: flex;
  align-items: center;
  height: 50px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  gap: 20px;
}

/* 第二行 */
.header-bottom {
  background: #fff;
}
.bottom-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 36px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.bottom-nav {
  display: flex;
  align-items: center;
  gap: 0;
}
.bottom-nav a,
.bottom-nav span {
  color: #666;
  font-size: 13px;
  padding: 0 8px;
  text-decoration: none;
  transition: color 0.2s;
}
.bottom-nav a:hover,
.bottom-nav a.router-link-active {
  color: #ff6600;
}
.divider {
  color: #ccc;
  font-size: 12px;
  margin: 0 2px;
}
.user-greeting {
  color: #ff6600;
  font-weight: 500;
}
.logout-link {
  cursor: pointer;
}
.logout-link:hover {
  color: #ff6600;
}

/* Logo + City */
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.logo {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 20px;
  font-weight: 700;
  color: #409eff;
  white-space: nowrap;
}
.logo-icon {
  font-size: 22px;
  color: #e6a23c;
}
.city-trigger {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  transition: all 0.2s;
  white-space: nowrap;
}
.city-trigger:hover {
  border-color: #409eff;
  color: #409eff;
}

/* City popover */
.city-popover-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}
.city-popover-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}
.city-opt {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 8px;
  border-radius: 6px;
  font-size: 14px;
  color: #333;
  background: #f9fafb;
  transition: all 0.2s;
  text-decoration: none;
}
.city-opt:hover { background: #ecf5ff; color: #409eff; }
.city-opt.active { background: #409eff; color: #fff; }
.city-opt.disabled { opacity: 0.5; cursor: not-allowed; }
.city-opt.disabled:hover { background: #f9fafb; color: #999; }
.city-coming { font-size: 10px; color: #c0c4cc; margin-top: 2px; }
.city-opt.active .city-coming { color: rgba(255,255,255,0.7); }

/* Navigation */
.nav-links {
  display: flex;
  gap: 20px;
  margin-left: 24px;
}
.nav-links a {
  color: #555;
  font-size: 14px;
  white-space: nowrap;
  position: relative;
  padding: 4px 0;
}
.nav-links a:hover,
.nav-links a.router-link-active {
  color: #409eff;
}

/* Right area */
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
  flex-shrink: 0;
}
.header-search {
  width: 180px;
}
.header-search :deep(.el-input__wrapper) {
  border-radius: 20px;
}
.auth-area { display: flex; gap: 6px; }
.user-info { cursor: pointer; }
.user-name { font-size: 13px; color: #333; display: flex; align-items: center; }
.hotline-mini {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.main-content { min-height: calc(100vh - 86px); }

/* 汉堡按钮 */
.mobile-menu-btn { display: flex; flex-direction: column; gap: 5px; cursor: pointer; padding: 8px; margin-left: auto; }
.mobile-menu-btn span { display: block; width: 22px; height: 2px; background: #333; border-radius: 1px; }

/* 移动端展开菜单 */
.mobile-menu { background: #fff; border-top: 1px solid #eee; padding: 12px 20px; }
.mobile-menu a { display: block; padding: 12px 0; color: #333; font-size: 15px; border-bottom: 1px solid #f5f5f5; }
.mobile-menu a:last-child { border-bottom: none; }
.mobile-search { margin-bottom: 8px; }

/* 响应式显隐 */
.mobile-only { display: none; }
@media (max-width: 768px) {
  .desktop-only { display: none !important; }
  .mobile-only { display: block; }
  .mobile-menu-btn { display: flex; }
  .logo { font-size: 18px; }
  .header-inner { gap: 8px; }
}
</style>
