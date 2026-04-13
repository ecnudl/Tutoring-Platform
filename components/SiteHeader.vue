<template>
  <div class="site-header-outer">
    <div class="site-header-card container">
      <!-- 左：Logo -->
      <div class="nav-logo">
        <SiteLogo />
      </div>

      <!-- 中：城市 + 电话 + 搜索 -->
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

      <!-- 右：导航 + 功能链接 -->
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
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Location, ArrowDown, Phone, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()

const searchKeyword = ref('')

const cities = [
  { name: '上海', pinyin: 'shanghai', id: 1, enabled: true },
  { name: '南京', pinyin: 'nanjing', id: 5, enabled: true },
  { name: '苏州', pinyin: 'suzhou', id: 2, enabled: true },
  { name: '杭州', pinyin: 'hangzhou', id: 4, enabled: true },
  { name: '合肥', pinyin: 'hefei', id: 3, enabled: true },
  { name: '福州', pinyin: 'fuzhou', id: 6, enabled: true },
  { name: '南昌', pinyin: 'nanchang', id: 8, enabled: true },
  { name: '济南', pinyin: 'jinan', id: 7, enabled: true },
  { name: '北京', pinyin: 'beijing', id: 9, enabled: true },
  { name: '天津', pinyin: 'tianjin', id: 10, enabled: true },
  { name: '广州', pinyin: 'guangzhou', id: 11, enabled: true },
  { name: '武汉', pinyin: 'wuhan', id: 12, enabled: true }
]

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通`)
    return
  }
  cityStore.setCity(city.id, city.name)
}

const doSearch = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  router.push(`/jy?keyword=${encodeURIComponent(searchKeyword.value)}`)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.site-header-outer {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 0;
}

.site-header-card {
  display: flex;
  align-items: stretch;
  min-height: 100px;
  background: var(--color-surface);
  border-radius: 12px 12px 0 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid var(--color-border-light);
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

.nav-search { width: 220px; }
.nav-search :deep(.el-input__wrapper) { border-radius: var(--radius-sm); }

/* 城市弹窗 */
.city-popover { padding: var(--space-md); }
.city-popover-title {
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--space-md);
  color: var(--color-text);
}
.city-popover-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-sm);
}
.city-opt {
  padding: var(--space-sm) var(--space-md);
  text-align: center;
  border-radius: var(--radius-md);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
  text-decoration: none;
}
.city-opt:hover {
  background: var(--color-surface-hover);
  color: var(--color-primary);
}
.city-opt.active {
  background: var(--color-primary);
  color: #fff;
}
.city-opt.disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

/* 右：5列等宽网格 */
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
  text-decoration: none;
}

.nav-cell:hover,
.nav-cell.router-link-active {
  color: var(--color-primary);
}

.nav-highlight {
  color: var(--color-primary);
  font-weight: var(--font-weight-bold);
}

/* ============================================
   Responsive
   ============================================ */
@media (max-width: 768px) {
  .site-header-outer { padding: var(--space-md) 0 0; }
  .site-header-card {
    flex-direction: column;
    border-radius: 8px 8px 0 0;
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
}
</style>
