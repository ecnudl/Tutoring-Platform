<template>
  <div class="layout-default">
    <header class="site-header">
      <div class="container header-inner">
        <NuxtLink to="/" class="logo">家教在线</NuxtLink>
        <nav class="nav-links">
          <NuxtLink to="/">首页</NuxtLink>
          <NuxtLink to="/tutor">教员库</NuxtLink>
          <NuxtLink to="/requirement">找老师需求</NuxtLink>
        </nav>
        <div class="auth-links" v-if="!userStore.isLoggedIn">
          <NuxtLink to="/login"><el-button type="primary" size="small">登录</el-button></NuxtLink>
          <NuxtLink to="/register"><el-button size="small">注册</el-button></NuxtLink>
        </div>
        <div class="user-info" v-else>
          <el-dropdown>
            <span class="user-name">
              {{ userStore.mobile }}
              <el-tag size="small" :type="userStore.isTutor ? 'warning' : 'success'" style="margin-left:6px">
                {{ userStore.isTutor ? '教员' : '学员' }}
              </el-tag>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/center')">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
    <main class="main-content"><slot /></main>
    <footer class="site-footer">
      <div class="container"><p>© 2024 家教在线平台 版权所有</p></div>
    </footer>
  </div>
</template>
<script setup>
import { useUserStore } from '~/stores/user'
const userStore = useUserStore()
const router = useRouter()
const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>
<style scoped>
.site-header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 0 20px; position: sticky; top: 0; z-index: 100; }
.header-inner { display: flex; align-items: center; height: 60px; max-width: 1200px; margin: 0 auto; }
.logo { font-size: 22px; font-weight: 700; color: #409eff; margin-right: 40px; }
.nav-links { display: flex; gap: 24px; flex: 1; }
.nav-links a { color: #333; font-size: 15px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #409eff; }
.auth-links { display: flex; gap: 8px; }
.user-info { cursor: pointer; }
.user-name { font-size: 14px; color: #333; display: flex; align-items: center; }
.main-content { min-height: calc(100vh - 120px); }
.site-footer { background: #2c3e50; color: #fff; text-align: center; padding: 20px; margin-top: 40px; }
</style>
