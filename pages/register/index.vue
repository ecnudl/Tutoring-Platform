<template>
  <div class="register-page">
    <div class="register-container">
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
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  overflow: hidden;
}

.register-container {
  max-width: 900px;
  width: 100%;
}

.register-header {
  text-align: center;
  margin-bottom: 40px;
}

.register-header h2 {
  color: #fff;
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 12px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.register-header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  margin: 0;
}

.register-header a {
  color: #fff;
  text-decoration: underline;
}

.register-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  max-width: 700px;
  margin: 0 auto;
}

.reg-card {
  display: block;
  background: #fff;
  border-radius: 12px;
  padding: 40px 24px;
  text-align: center;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  text-decoration: none;
}

.reg-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.reg-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.reg-card h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 8px;
}

.reg-card p {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
}

.reg-links {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.reg-links a {
  font-size: 13px;
  color: #409eff;
  text-decoration: none;
}

.reg-links a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-cards {
    grid-template-columns: 1fr;
    max-width: 400px;
  }
}
</style>
