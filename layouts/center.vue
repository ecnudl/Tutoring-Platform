<template>
  <div class="layout-center">
    <div class="container">
      <div class="center-wrapper">
        <!-- 左侧菜单 -->
        <aside class="sidebar">
          <div class="user-card">
            <el-avatar :size="64" :src="userStore.avatar" />
            <div class="user-name">{{ userStore.mobile || '用户' }}</div>
            <div class="user-type">{{ userStore.userType === 1 ? '教员' : '学员/家长' }}</div>
          </div>

          <div class="menu-box">
            <div class="menu-section">
              <div class="menu-title">个人中心</div>
              <NuxtLink to="/center" class="menu-link">
                <span class="icon">🏠</span>
                <span>首页</span>
              </NuxtLink>
              <NuxtLink to="/center/profile" class="menu-link">
                <span class="icon">👤</span>
                <span>个人资料</span>
              </NuxtLink>
              <NuxtLink to="/center/favorites" class="menu-link">
                <span class="icon">⭐</span>
                <span>备选教员</span>
              </NuxtLink>
            </div>

            <div class="menu-section" v-if="userStore.userType === 1">
              <div class="menu-title">教员中心</div>
              <NuxtLink to="/center/tutor-profile" class="menu-link">
                <span class="icon">📝</span>
                <span>教员资料</span>
              </NuxtLink>
              <NuxtLink to="/center/orders" class="menu-link">
                <span class="icon">📋</span>
                <span>我的订单</span>
              </NuxtLink>
            </div>

            <div class="menu-section">
              <div class="menu-title">需求管理</div>
              <NuxtLink to="/center/requirements" class="menu-link">
                <span class="icon">📄</span>
                <span>我的需求</span>
              </NuxtLink>
              <NuxtLink to="/qjj" class="menu-link">
                <span class="icon">➕</span>
                <span>发布需求</span>
              </NuxtLink>
            </div>

            <div class="menu-section">
              <div class="menu-title">账户设置</div>
              <NuxtLink to="/center/password" class="menu-link">
                <span class="icon">🔒</span>
                <span>安全设置</span>
              </NuxtLink>
              <a href="javascript:;" @click="handleLogout" class="menu-link">
                <span class="icon">🚪</span>
                <span>退出登录</span>
              </a>
            </div>
          </div>
        </aside>

        <!-- 右侧内容 -->
        <main class="main-content">
          <slot />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '~/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.layout-center {
  background: #f5f7fa;
  min-height: calc(100vh - 86px);
  padding: 20px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.center-wrapper {
  display: flex;
  gap: 20px;
}

/* 左侧边栏 */
.sidebar {
  width: 220px;
  flex-shrink: 0;
}

.user-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-top: 12px;
}

.user-type {
  font-size: 13px;
  color: #999;
  margin-top: 4px;
}

.menu-box {
  background: #fff;
  border-radius: 8px;
  padding: 8px 0;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

.menu-section {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.menu-section:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.menu-title {
  font-size: 12px;
  color: #999;
  padding: 8px 16px;
  font-weight: 600;
}

.menu-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  color: #666;
  font-size: 14px;
  transition: all 0.2s;
  cursor: pointer;
}

.menu-link:hover {
  background: #f5f7fa;
  color: #409eff;
}

.menu-link.router-link-active {
  background: #ecf5ff;
  color: #409eff;
  font-weight: 500;
}

.icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

/* 右侧内容 */
.main-content {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  min-height: 500px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

@media (max-width: 768px) {
  .center-wrapper {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }

  .menu-box {
    display: flex;
    overflow-x: auto;
    padding: 8px;
    gap: 8px;
  }

  .menu-section {
    display: flex;
    gap: 4px;
    margin-bottom: 0;
    padding-bottom: 0;
    border-bottom: none;
    flex-shrink: 0;
  }

  .menu-title {
    display: none;
  }

  .menu-link {
    white-space: nowrap;
    padding: 8px 12px;
    border-radius: 4px;
    font-size: 13px;
  }

  .main-content {
    padding: 16px;
  }
}
</style>
