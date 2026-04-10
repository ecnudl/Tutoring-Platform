<template>
  <footer class="site-footer">
    <div class="footer-inner">
      <div class="footer-main">
        <div class="footer-brand">
          <SiteLogo variant="light" />
          <p class="footer-desc">专业家教服务平台，提供上门一对一辅导。严选教员，安心托付。</p>
        </div>
        <div class="footer-links">
          <div class="footer-col">
            <h4>找家教</h4>
            <NuxtLink to="/jy">教员库</NuxtLink>
            <NuxtLink to="/xy">学员库</NuxtLink>
            <NuxtLink to="/qjj">请家教</NuxtLink>
            <NuxtLink to="/zf">价格参考</NuxtLink>
          </div>
          <div class="footer-col">
            <h4>教员入口</h4>
            <NuxtLink to="/register/teacher">教员注册</NuxtLink>
            <NuxtLink to="/login">教员登录</NuxtLink>
            <NuxtLink to="/center">个人中心</NuxtLink>
          </div>
          <div class="footer-col">
            <h4>帮助与支持</h4>
            <NuxtLink to="/help">帮助中心</NuxtLink>
            <NuxtLink to="/agreement/user">用户协议</NuxtLink>
            <NuxtLink to="/agreement/privacy">隐私政策</NuxtLink>
            <NuxtLink to="/agreement/disclaimer" class="disclaimer-link">免责声明</NuxtLink>
          </div>
          <div class="footer-col">
            <h4>关于我们</h4>
            <NuxtLink to="/about/contact">联系我们</NuxtLink>
            <NuxtLink to="/about/wechat">服务号</NuxtLink>
            <NuxtLink to="/about/intro">公司简介</NuxtLink>
            <NuxtLink to="/agreement/privacy">隐私保护</NuxtLink>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>&copy; {{ new Date().getFullYear() }} 591家教网 版权所有</span>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.site-footer {
  background: var(--color-primary-dark, #163B6B);
  color: rgba(255, 255, 255, 0.75);
  margin-top: auto;
}

.footer-inner {
  max-width: var(--content-width, 1200px);
  margin: 0 auto;
  padding: 0 var(--space-xl, 20px);
}

.footer-main {
  display: flex;
  gap: 60px;
  padding: 36px 0 28px;
}

.footer-brand {
  width: 220px;
  flex-shrink: 0;
}

.footer-desc {
  margin-top: 12px;
  font-size: 13px;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.55);
}

.footer-links {
  flex: 1;
  display: flex;
  gap: 40px;
}

.footer-col {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.footer-col h4 {
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 4px;
}

.footer-col a,
.footer-col span {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  transition: color var(--transition-fast, 0.15s ease);
}

.footer-col a:hover {
  color: #fff;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 16px 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  text-align: center;
}

.footer-col a.disclaimer-link {
  font-size: 14px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
}

.footer-col a.disclaimer-link:hover {
  color: #fff;
}

@media (max-width: 768px) {
  .footer-main {
    flex-direction: column;
    gap: 24px;
    padding: 24px 0 20px;
  }

  .footer-brand {
    width: 100%;
  }

  .footer-links {
    flex-wrap: wrap;
    gap: 24px;
  }

  .footer-col {
    min-width: 120px;
  }
}
</style>
