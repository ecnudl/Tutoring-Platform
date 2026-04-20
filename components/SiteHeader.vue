<template>
  <div class="site-header-outer">
    <div class="site-header-card container">
      <!-- 左：品牌名 + 域名 -->
      <div class="nav-brand">
        <div class="nav-brand-name"><span class="brand-main"><span class="brand-num">591</span><span class="brand-cn">家教网</span></span><span class="brand-sub">名校名师优质家教平台</span></div>
        <div class="nav-brand-domains">
          <span>www.591jiajiao.com</span>
          <span>www.591jiajiao.cn</span>
        </div>
      </div>

      <!-- 中：切换城市 / 家教热线（两行竖排） -->
      <div class="nav-center">
        <div class="nav-center-row">
          <span class="nav-center-label">切换城市</span>
          <el-popover placement="bottom-start" :width="300" trigger="click">
            <template #reference>
              <span class="city-btn">
                <el-icon size="13"><Location /></el-icon>
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
        </div>
        <div class="nav-center-row">
          <span class="nav-center-label">家教热线</span>
          <span class="nav-phone">
            <el-icon size="13"><Phone /></el-icon>
            <span class="phone-num">13795420591</span>
          </span>
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
            <NuxtLink to="/center" class="nav-cell nav-highlight">{{ userStore.displayName }}</NuxtLink>
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
import { onMounted } from 'vue'
import { Location, ArrowDown, Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'
import { CITY_LIST, navigateToCity } from '~/composables/useCityMap'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()

onMounted(() => {
  if (userStore.isLoggedIn) {
    userStore.fetchNickname()
  }
})

const cities = CITY_LIST

const handleCityClick = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通`)
    return
  }
  navigateToCity(city, '/')
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

/* 中：切换城市 / 家教热线（两行竖排） */
.nav-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: var(--space-sm) var(--space-xl);
  gap: 10px;
  flex-shrink: 0;
}

.nav-center-row {
  display: flex;
  align-items: center;
  gap: 14px;
}

.nav-center-label {
  font-size: 14px;
  font-weight: 500;
  color: #475569;
  letter-spacing: 2px;
  min-width: 62px;
}

.city-btn,
.nav-phone {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 30px;
  padding: 0 14px;
  font-size: 14px;
  font-weight: 600;
  color: var(--color-primary);
  background: var(--color-primary-lighter);
  border: 1px solid transparent;
  border-radius: 999px;
  white-space: nowrap;
  transition: background var(--transition-fast), color var(--transition-fast), border-color var(--transition-fast);
  cursor: pointer;
}

.city-btn:hover,
.nav-phone:hover {
  background: var(--color-primary);
  color: #fff;
}

.nav-phone { cursor: default; }

.phone-num {
  font-family: Georgia, "Times New Roman", serif;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

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

/* 左：品牌名 + 域名 */
.nav-brand {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: center;
  padding: var(--space-sm) var(--space-xl);
  border-right: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.nav-brand-name {
  display: flex;
  align-items: baseline;
  gap: 8px;
  white-space: nowrap;
}

.brand-main {
  display: inline-flex;
  align-items: baseline;
  font-size: 22px;
  line-height: 1;
}

.brand-num {
  font-family: Georgia, "Playfair Display", "Times New Roman", serif;
  font-weight: 900;
  font-style: italic;
  font-size: 24px;
  color: #1a1a1a;
  letter-spacing: -0.5px;
  margin-right: 4px;
  /* 斜体挂钩与中文对齐的微调 */
  transform: translateY(1px);
}

.brand-cn {
  font-family: "Songti SC", "STSong", "SimSun", "Noto Serif SC", "Source Han Serif SC", serif;
  font-weight: 700;
  color: #1a1a1a;
  letter-spacing: 3px;
}

.brand-sub {
  font-family: "Songti SC", "STSong", "Noto Serif SC", serif;
  font-size: 15px;
  font-weight: 400;
  color: var(--color-primary);
  letter-spacing: 2px;
}

.nav-brand-domains {
  display: flex;
  justify-content: center;
  gap: 14px;
  width: 100%;
  margin-top: 6px;
}

.nav-brand-domains span {
  font-family: Georgia, "Times New Roman", Times, serif;
  font-size: 13px;
  font-weight: 500;
  font-style: italic;
  color: #64748b;
  letter-spacing: 0.6px;
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
  min-width: 0;
  overflow: hidden;
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
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color var(--transition-fast);
  text-align: center;
  text-decoration: none;
  min-width: 0;
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
