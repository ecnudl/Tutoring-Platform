<template>
  <div class="layout-center">
    <header class="site-header">
      <div class="container header-inner">
        <NuxtLink to="/" class="logo">家教在线</NuxtLink>
        <nav class="nav-links desktop-nav">
          <NuxtLink to="/">首页</NuxtLink>
          <NuxtLink to="/tutor">教员库</NuxtLink>
          <NuxtLink to="/requirement">找老师需求</NuxtLink>
        </nav>
        <div class="user-info" v-if="userStore.isLoggedIn">
          <span class="desktop-nav" style="font-size:14px;color:#333">{{ userStore.mobile }}</span>
          <el-tag size="small" :type="userStore.isTutor ? 'warning' : 'success'" style="margin:0 8px" class="desktop-nav">
            {{ userStore.isTutor ? '教员' : '学员' }}
          </el-tag>
          <el-button size="small" @click="handleLogout" class="desktop-nav">退出</el-button>
        </div>
        <!-- 移动端返回首页按钮 -->
        <NuxtLink to="/" class="mobile-back">← 返回</NuxtLink>
      </div>
    </header>

    <!-- 移动端：顶部横向滚动菜单 -->
    <div class="mobile-tabs">
      <div class="mobile-tabs-inner">
        <NuxtLink to="/center" :class="{ active: $route.path === '/center' }">首页</NuxtLink>
        <NuxtLink to="/center/profile" :class="{ active: $route.path === '/center/profile' }">资料</NuxtLink>
        <template v-if="userStore.isTutor">
          <NuxtLink to="/center/tutor-profile" :class="{ active: $route.path === '/center/tutor-profile' }">教员资料</NuxtLink>
          <NuxtLink to="/center/certifications" :class="{ active: $route.path === '/center/certifications' }">证书</NuxtLink>
          <NuxtLink to="/center/applications" :class="{ active: $route.path === '/center/applications' }">申请</NuxtLink>
        </template>
        <template v-if="userStore.isStudent">
          <NuxtLink to="/center/requirements" :class="{ active: $route.path === '/center/requirements' }">需求</NuxtLink>
          <NuxtLink to="/center/shortlist" :class="{ active: $route.path === '/center/shortlist' }">备选</NuxtLink>
          <NuxtLink to="/center/received-applications" :class="{ active: $route.path === '/center/received-applications' }">收到申请</NuxtLink>
        </template>
        <NuxtLink to="/center/reservations" :class="{ active: $route.path === '/center/reservations' }">预约</NuxtLink>
        <NuxtLink to="/center/password" :class="{ active: $route.path === '/center/password' }">密码</NuxtLink>
      </div>
    </div>

    <div class="container center-layout">
      <!-- PC侧边栏 -->
      <aside class="sidebar desktop-sidebar">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/center">个人中心</el-menu-item>
          <el-menu-item index="/center/profile">个人资料</el-menu-item>
          <template v-if="userStore.isTutor">
            <el-menu-item index="/center/tutor-profile">教员资料</el-menu-item>
            <el-menu-item index="/center/certifications">资质证书</el-menu-item>
            <el-menu-item index="/center/applications">我的申请</el-menu-item>
          </template>
          <template v-if="userStore.isStudent">
            <el-menu-item index="/center/requirements">我的需求</el-menu-item>
            <el-menu-item index="/center/requirement-new">发布需求</el-menu-item>
            <el-menu-item index="/center/shortlist">备选老师</el-menu-item>
            <el-menu-item index="/center/received-applications">收到的申请</el-menu-item>
          </template>
          <el-menu-item index="/center/reservations">我的预约</el-menu-item>
          <el-menu-item index="/center/password">修改密码</el-menu-item>
        </el-menu>
      </aside>
      <main class="center-content"><slot /></main>
    </div>
  </div>
</template>
<script setup>
import { useUserStore } from '~/stores/user'
const userStore = useUserStore()
const router = useRouter()
const handleLogout = () => { userStore.logout(); router.push('/') }
</script>
<style scoped>
.site-header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 0 20px; }
.header-inner { display: flex; align-items: center; height: 60px; max-width: 1200px; margin: 0 auto; }
.logo { font-size: 22px; font-weight: 700; color: #409eff; margin-right: 40px; }
.nav-links { display: flex; gap: 24px; flex: 1; }
.nav-links a { color: #333; font-size: 15px; }
.user-info { display: flex; align-items: center; }
.center-layout { display: flex; gap: 20px; padding: 20px 0; min-height: calc(100vh - 80px); }
.sidebar { width: 220px; flex-shrink: 0; }
.center-content { flex: 1; background: #fff; border-radius: 8px; padding: 24px; }

/* 移动端顶部Tab */
.mobile-tabs { display: none; background: #fff; border-bottom: 1px solid #eee; overflow-x: auto; -webkit-overflow-scrolling: touch; }
.mobile-tabs-inner { display: flex; white-space: nowrap; padding: 0 12px; }
.mobile-tabs a { display: inline-block; padding: 12px 14px; font-size: 14px; color: #666; border-bottom: 2px solid transparent; }
.mobile-tabs a.active { color: #409eff; border-bottom-color: #409eff; font-weight: 600; }

/* 移动端返回按钮 */
.mobile-back { display: none; margin-left: auto; font-size: 14px; color: #409eff; }

@media (max-width: 768px) {
  .desktop-nav { display: none !important; }
  .desktop-sidebar { display: none; }
  .mobile-tabs { display: block; }
  .mobile-back { display: block; }
  .center-layout { padding: 12px 0; }
  .center-content { padding: 16px; border-radius: 0; }
  .logo { margin-right: auto; font-size: 20px; }
}
</style>
