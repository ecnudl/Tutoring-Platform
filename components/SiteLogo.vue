<template>
  <NuxtLink to="/" class="site-logo" :class="variant">
    <div class="logo-icon">
      <span class="logo-number">591</span>
    </div>
    <span v-if="showText" class="logo-text">家教网</span>
  </NuxtLink>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'default',
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
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: var(--color-primary, #1F4E8C);
  border-radius: var(--radius-md, 6px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-number {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  letter-spacing: -0.5px;
}

.logo-text {
  font-size: 22px;
  font-weight: 600;
  color: var(--color-text, #1F2937);
}

/* Light variant for dark backgrounds */
.site-logo.light .logo-icon {
  background: #fff;
}

.site-logo.light .logo-number {
  color: var(--color-primary, #1F4E8C);
}

.site-logo.light .logo-text {
  color: #fff;
}

/* Dark variant */
.site-logo.dark .logo-icon {
  background: var(--color-primary-dark, #163B6B);
}

@media (max-width: 768px) {
  .logo-icon {
    width: 38px;
    height: 38px;
  }

  .logo-number {
    font-size: 16px;
  }

  .logo-text {
    font-size: 17px;
  }
}
</style>
