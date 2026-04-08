<template>
  <div class="layout-default">
    <!-- Header only for non-homepage -->
    <header v-if="route.path !== '/'" class="site-header">
      <div class="container header-inner">
        <div class="header-left">
          <SiteLogo />
          <span class="header-city">
            {{ cityStore.cityName }}，
          </span>
          <span class="header-phone">
            <el-icon size="13"><Phone /></el-icon>
            400-000-0000
          </span>
        </div>
        <nav class="header-nav desktop-only">
          <NuxtLink to="/">{{ cityStore.cityName }}家教</NuxtLink>
          <NuxtLink to="/jy">教员库</NuxtLink>
          <NuxtLink to="/xy">学员需求</NuxtLink>
          <NuxtLink to="/qjj">请家教</NuxtLink>
          <template v-if="userStore.isLoggedIn">
            <NuxtLink to="/center">个人中心</NuxtLink>
            <a href="javascript:;" @click="handleLogout">退出</a>
          </template>
          <template v-else>
            <NuxtLink to="/login">登录</NuxtLink>
            <NuxtLink to="/register">注册</NuxtLink>
          </template>
        </nav>
        <div class="mobile-menu-btn mobile-only" @click="mobileMenuOpen = !mobileMenuOpen">
          <span></span><span></span><span></span>
        </div>
      </div>
      <div class="mobile-menu mobile-only" v-show="mobileMenuOpen" @click="mobileMenuOpen = false">
        <NuxtLink to="/">首页</NuxtLink>
        <NuxtLink to="/jy">教员库</NuxtLink>
        <NuxtLink to="/xy">学员需求</NuxtLink>
        <NuxtLink to="/qjj">请家教</NuxtLink>
        <template v-if="userStore.isLoggedIn">
          <NuxtLink to="/center">个人中心</NuxtLink>
          <a @click="handleLogout" style="cursor:pointer">退出</a>
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
import { ref, onMounted } from 'vue'
import { Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const router = useRouter()
const route = useRoute()
const mobileMenuOpen = ref(false)

const handleLogout = () => { userStore.logout(); router.push('/') }

onMounted(() => {
  cityStore.loadFromStorage()
})
</script>

<style scoped>
.layout-default {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.site-header {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.header-inner {
  display: flex;
  align-items: center;
  height: 56px;
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  flex-shrink: 0;
}

.header-city {
  font-size: var(--font-size-lg);
  color: var(--color-text);
  font-weight: var(--font-weight-medium);
}

.header-phone {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
}

.header-nav {
  display: flex;
  align-items: center;
  gap: var(--space-2xl);
  margin-left: auto;
}

.header-nav a {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  white-space: nowrap;
  transition: color var(--transition-fast);
}
.header-nav a:hover,
.header-nav a.router-link-active {
  color: var(--color-primary);
}

.page-content { flex: 1; }

.mobile-menu-btn {
  display: none;
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

.mobile-only { display: none; }

@media (max-width: 768px) {
  .desktop-only { display: none !important; }
  .mobile-only { display: block; }
  .mobile-menu-btn { display: flex; }
  .header-inner { height: 48px; padding: 0 var(--space-md); }
  .header-phone { display: none; }
}
</style>
