<template>
  <div class="layout-center">
    <header class="site-header">
      <div class="container header-inner">
        <NuxtLink to="/" class="logo">家教在线</NuxtLink>
        <nav class="nav-links">
          <NuxtLink to="/">首页</NuxtLink>
          <NuxtLink to="/tutor">教员库</NuxtLink>
          <NuxtLink to="/requirement">找老师需求</NuxtLink>
        </nav>
        <div class="user-info" v-if="userStore.isLoggedIn">
          <span style="font-size:14px;color:#333">{{ userStore.mobile }}</span>
          <el-tag size="small" :type="userStore.isTutor ? 'warning' : 'success'" style="margin:0 8px">
            {{ userStore.isTutor ? '教员' : '学员' }}
          </el-tag>
          <el-button size="small" @click="handleLogout">退出</el-button>
        </div>
      </div>
    </header>
    <div class="container center-layout">
      <aside class="sidebar">
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
</style>
