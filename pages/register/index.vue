<template>
  <div class="register-page-wrapper">
    <div class="container register-page">
      <!-- 登录/注册 切换 -->
      <div class="auth-tabs">
        <NuxtLink to="/login" class="auth-tab">登录</NuxtLink>
        <NuxtLink to="/register" class="auth-tab active">注册</NuxtLink>
      </div>

      <div class="register-body">
        <h2 class="register-title">欢迎注册</h2>
        <p class="register-subtitle">如果您已经注册过，可以直接 <NuxtLink to="/login/find-password">找回密码</NuxtLink></p>
        <div class="register-cards">
          <NuxtLink to="/register/student" class="reg-card">
            <div class="reg-icon">&#x1F468;&#x200D;&#x1F469;&#x200D;&#x1F467;</div>
            <h3>家长注册</h3>
            <p>我是学员/家长</p>
            <div class="reg-links">
              <NuxtLink to="/login?type=student">学员/家长登录</NuxtLink>
              <NuxtLink to="/login/find-password">找回密码</NuxtLink>
            </div>
          </NuxtLink>
          <NuxtLink to="/register/teacher" class="reg-card">
            <div class="reg-icon">&#x1F393;</div>
            <h3>教员注册</h3>
            <p>我是老师</p>
            <div class="reg-links">
              <NuxtLink to="/login?type=teacher">教员登录</NuxtLink>
              <NuxtLink to="/login/find-password">找回密码</NuxtLink>
            </div>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
  min-height: calc(100vh - 200px);
}

.register-page {
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-2xl) var(--space-xl) var(--space-4xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 登录/注册 切换标签 */
.auth-tabs {
  display: flex;
  gap: var(--space-xl);
  margin-bottom: var(--space-3xl);
  border-bottom: 2px solid var(--color-border-light);
  padding-bottom: 0;
}

.auth-tab {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-secondary);
  padding: var(--space-md) var(--space-xl);
  text-decoration: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  transition: all var(--transition-fast);
}

.auth-tab:hover {
  color: var(--color-primary);
}

.auth-tab.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.register-body {
  text-align: center;
  max-width: 700px;
  width: 100%;
}

.register-title {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin: 0 0 var(--space-sm) 0;
}

.register-subtitle {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  margin-bottom: var(--space-3xl);
}

.register-subtitle a {
  color: var(--color-primary);
}

.register-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-2xl);
}

.reg-card {
  display: block;
  background: var(--color-surface);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-xl);
  padding: var(--space-3xl) var(--space-2xl);
  text-align: center;
  transition: all var(--transition-normal);
}

.reg-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.reg-icon {
  font-size: 48px;
  margin-bottom: var(--space-lg);
}

.reg-card h3 {
  font-size: var(--font-size-xl);
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.reg-card p {
  color: var(--color-text-muted);
  font-size: var(--font-size-base);
  margin-bottom: var(--space-xl);
}

.reg-links {
  display: flex;
  justify-content: center;
  gap: var(--space-lg);
  padding-top: var(--space-lg);
  border-top: 1px solid var(--color-border-light);
}

.reg-links a {
  font-size: var(--font-size-sm);
  color: var(--color-primary);
}

.reg-links a:hover {
  color: var(--color-primary-light);
}

@media (max-width: 768px) {
  .register-page { padding: var(--space-lg); }
  .register-cards {
    grid-template-columns: 1fr;
    max-width: 400px;
    margin: 0 auto;
  }
  .reg-card { padding: var(--space-2xl) var(--space-xl); }
}
</style>
