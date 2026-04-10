<template>
  <div class="site-header-wrapper">
    <div class="site-header-container">
      <!-- 左：Logo -->
      <div class="header-logo">
        <SiteLogo />
      </div>

      <!-- 中：城市 + 电话 + 搜索 -->
      <div class="header-center">
        <div class="header-center-top">
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
          <span class="header-phone">
            <el-icon size="13"><Phone /></el-icon>
            13795420591
          </span>
        </div>
        <div class="header-center-bottom">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索科目"
            size="small"
            clearable
            class="header-search"
            @keyup.enter="doSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <!-- 右：导航 + 功能链接 -->
      <div class="header-right">
        <div class="header-right-row">
          <NuxtLink to="/" class="header-cell">{{ cityStore.cityName }}家教</NuxtLink>
          <NuxtLink to="/jy" class="header-cell">教员库</NuxtLink>
          <NuxtLink to="/xy" class="header-cell">学员库</NuxtLink>
          <NuxtLink to="/zf" class="header-cell">家教价格</NuxtLink>
          <NuxtLink to="/center" class="header-cell">个人中心</NuxtLink>
        </div>
        <div class="header-right-row">
          <NuxtLink to="/qjj" class="header-cell">请家教</NuxtLink>
          <NuxtLink to="/register/teacher" class="header-cell">做老师</NuxtLink>
          <NuxtLink to="/help" class="header-cell">帮助</NuxtLink>
          <template v-if="userStore.isLoggedIn">
            <NuxtLink to="/center" class="header-cell header-highlight">{{ userStore.mobile || '个人中心' }}</NuxtLink>
            <a href="javascript:;" @click="handleLogout" class="header-cell">退出</a>
          </template>
          <template v-else>
            <NuxtLink to="/login" class="header-cell">登录</NuxtLink>
            <NuxtLink to="/register" class="header-cell header-highlight">注册</NuxtLink>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Location, ArrowDown, Phone, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()

const searchKeyword = ref('')
const cities = ref([
  { name: '上海', pinyin: 'shanghai', enabled: true },
  { name: '苏州', pinyin: 'suzhou', enabled: false },
  { name: '合肥', pinyin: 'hefei', enabled: false },
  { name: '杭州', pinyin: 'hangzhou', enabled: false },
  { name: '南京', pinyin: 'nanjing', enabled: false }
])

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info('该城市暂未开放')
    return
  }
  cityStore.setCity(city.name, city.pinyin)
}

const doSearch = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  router.push(`/jy?search=${encodeURIComponent(searchKeyword.value)}`)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}

onMounted(() => {
  cityStore.loadFromStorage()
})
</script>

<style scoped>
.site-header-wrapper {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  padding: var(--space-md) 0;
}

.site-header-container {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
  display: grid;
  grid-template-columns: auto 1fr auto;
  gap: var(--space-xl);
  align-items: center;
  min-height: 100px;
}

/* 左侧 Logo */
.header-logo {
  display: flex;
  align-items: center;
  border-right: 1px solid var(--color-border-light);
  padding-right: var(--space-xl);
}

/* 中间 城市+电话+搜索 */
.header-center {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  padding: 0 var(--space-xl);
  border-right: 1px solid var(--color-border-light);
}

.header-center-top {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
}

.city-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  color: var(--color-text);
  font-size: var(--font-size-base);
  transition: color var(--transition-fast);
}

.city-btn:hover {
  color: var(--color-primary);
}

.city-popover {
  padding: var(--space-md);
}

.city-popover-title {
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--space-md);
  color: var(--color-text);
}

.city-popover-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-md);
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

.header-phone {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  white-space: nowrap;
}

.header-center-bottom {
  width: 100%;
}

.header-search {
  width: 100%;
}

/* 右侧 导航 */
.header-right {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  padding-left: var(--space-xl);
}

.header-right-row {
  display: grid;
  grid-template-columns: repeat(5, auto);
  gap: var(--space-lg);
  justify-content: flex-end;
}

.header-cell {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  text-decoration: none;
  transition: color var(--transition-fast);
  white-space: nowrap;
}

.header-cell:hover,
.header-cell.router-link-active {
  color: var(--color-primary);
}

.header-highlight {
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

/* 响应式 */
@media (max-width: 1200px) {
  .site-header-container {
    grid-template-columns: 1fr;
    gap: var(--space-md);
    min-height: auto;
    padding: var(--space-md) var(--space-lg);
  }

  .header-logo {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    justify-content: center;
    padding: var(--space-md) 0;
  }

  .header-center {
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    padding: var(--space-md) 0;
  }

  .header-right {
    padding-left: 0;
  }

  .header-right-row {
    grid-template-columns: repeat(3, auto);
    justify-content: center;
    gap: var(--space-md);
  }

  .header-cell {
    font-size: var(--font-size-sm);
  }
}

@media (max-width: 768px) {
  .site-header-wrapper {
    padding: var(--space-sm) 0;
  }

  .site-header-container {
    padding: 0 var(--space-md);
  }

  .header-center-top {
    gap: var(--space-md);
  }

  .header-right-row {
    grid-template-columns: repeat(2, auto);
    gap: var(--space-sm);
  }

  .header-cell {
    font-size: var(--font-size-xs);
    padding: 4px 2px;
  }
}
</style>
