<template>
  <div class="layout-center">
    <div class="container">
      <div class="center-wrapper">
        <aside class="sidebar">
          <div class="user-card">
            <el-avatar :size="56" :src="userStore.avatar" />
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
  background: var(--color-bg);
  min-height: calc(100vh - 56px);
  padding: var(--space-xl) 0;
}

.container {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
}

.center-wrapper {
  display: flex;
  gap: var(--space-xl);
}

.sidebar {
  width: var(--sidebar-width);
  flex-shrink: 0;
}

.user-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  text-align: center;
  margin-bottom: var(--space-lg);
}

.user-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-top: var(--space-md);
}

.user-type {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-top: 4px;
}

.menu-box {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-sm) 0;
}

.menu-section {
  margin-bottom: var(--space-sm);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--color-border-light);
}
.menu-section:last-child { margin-bottom: 0; padding-bottom: 0; border-bottom: none; }

.menu-title {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  padding: var(--space-sm) var(--space-lg);
  font-weight: var(--font-weight-semibold);
}

.menu-link {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  padding: 10px var(--space-lg);
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  transition: all var(--transition-fast);
  cursor: pointer;
}
.menu-link:hover { background: var(--color-bg); color: var(--color-primary); }
.menu-link.router-link-active { background: var(--color-primary-lighter); color: var(--color-primary); font-weight: var(--font-weight-medium); }

.icon { font-size: var(--font-size-lg); width: 20px; text-align: center; }

.main-content {
  flex: 1;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  min-height: 500px;
}

@media (max-width: 768px) {
  .center-wrapper { flex-direction: column; }
  .sidebar { width: 100%; }
  .menu-box { display: flex; overflow-x: auto; padding: var(--space-sm); gap: var(--space-sm); }
  .menu-section { display: flex; gap: 4px; margin-bottom: 0; padding-bottom: 0; border-bottom: none; flex-shrink: 0; }
  .menu-title { display: none; }
  .menu-link { white-space: nowrap; padding: var(--space-sm) var(--space-md); border-radius: var(--radius-sm); font-size: var(--font-size-sm); }
  .main-content { padding: var(--space-lg); }
}
</style>
