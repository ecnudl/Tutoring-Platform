<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-brand">
        <SiteLogo :showText="true" />
      </div>
      <div class="register-header">
        <h2>欢迎注册</h2>
        <p>如果您已经注册过，可以直接选择 <NuxtLink to="/login/find-password">找回密码</NuxtLink></p>
      </div>
      <div class="register-cards">
        <NuxtLink to="/register/student" class="reg-card">
          <div class="reg-icon">👨‍👩‍👧</div>
          <h3>家长注册</h3>
          <p>我是学员/家长</p>
          <div class="reg-links">
            <NuxtLink to="/login?type=student">学员/家长登录</NuxtLink>
            <NuxtLink to="/login/find-password">找回密码</NuxtLink>
          </div>
        </NuxtLink>
        <NuxtLink to="/register/teacher" class="reg-card">
          <div class="reg-icon">🎓</div>
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
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-xl);
}

.register-container {
  max-width: 700px;
  width: 100%;
}

.register-brand {
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-2xl);
}

.register-header {
  text-align: center;
  margin-bottom: var(--space-3xl);
}

.register-header h2 {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin: 0 0 var(--space-sm) 0;
}

.register-header p {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
}

.register-header a {
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
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-4xl) var(--space-2xl);
  text-align: center;
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.reg-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-lg);
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
  .register-cards {
    grid-template-columns: 1fr;
    max-width: 400px;
    margin: 0 auto;
  }
}
</style>
