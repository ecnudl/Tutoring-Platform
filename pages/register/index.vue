<template>
  <div style="min-height:80vh;display:flex;align-items:center;justify-content:center;background:#f5f7fa;padding:20px">
    <div style="max-width:900px;width:100%">
      <div style="text-align:center;margin-bottom:40px">
        <NuxtLink to="/" style="font-size:24px;font-weight:700;color:#409eff;text-decoration:none">★ 51家教网</NuxtLink>
        <h2 style="margin-top:16px;color:#333">欢迎注册</h2>
        <p style="color:#999;margin-top:8px">如果您已经注册过，可以直接选择 <NuxtLink to="/login/find-password" style="color:#409eff">找回密码</NuxtLink></p>
      </div>
      <div style="display:grid;grid-template-columns:repeat(2,1fr);gap:24px;max-width:700px;margin:0 auto">
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
.reg-card { display:block;background:#fff;border-radius:8px;padding:40px 24px;text-align:center;transition:all 0.3s;box-shadow:0 2px 8px rgba(0,0,0,0.1); }
.reg-card:hover { transform:translateY(-4px);box-shadow:0 4px 16px rgba(0,0,0,0.15); }
.reg-icon { font-size:64px;margin-bottom:16px; }
.reg-card h3 { font-size:20px;color:#333;margin-bottom:8px; }
.reg-card p { color:#999;font-size:14px;margin-bottom:20px; }
.reg-links { display:flex;justify-content:center;gap:16px;padding-top:16px;border-top:1px solid #f0f0f0; }
.reg-links a { font-size:13px;color:#409eff; }
.reg-links a:hover { text-decoration:underline; }
@media (max-width:768px) { .reg-card { grid-template-columns:1fr; } }
</style>
