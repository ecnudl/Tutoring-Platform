<template>
  <NuxtLink to="/" class="site-logo" :class="variant">
    <div class="logo-icon">
      <span class="logo-number">51</span>
    </div>
    <span v-if="showText" class="logo-text">家教网</span>
  </NuxtLink>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'default', // default, light, dark
    validator: (v) => ['default', 'light', 'dark'].includes(v)
  },
  showText: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.site-logo {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  transition: all 0.3s;
}

.site-logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.logo-icon::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transform: rotate(45deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(45deg); }
}

.logo-number {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  position: relative;
  z-index: 1;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

/* Light variant - 用于深色背景 */
.site-logo.light .logo-icon {
  background: linear-gradient(135deg, #fff 0%, #f0f0f0 100%);
  box-shadow: 0 2px 8px rgba(255, 255, 255, 0.3);
}

.site-logo.light .logo-number {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.site-logo.light .logo-text {
  color: #fff;
}

/* Dark variant - 用于浅色背景 */
.site-logo.dark .logo-icon {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

/* 响应式 */
@media (max-width: 768px) {
  .logo-icon {
    width: 36px;
    height: 36px;
  }

  .logo-number {
    font-size: 16px;
  }

  .logo-text {
    font-size: 14px;
  }
}
</style>
