<template>
  <div class="layout-default">
    <!-- 统一头部 - 所有页面都显示 -->
    <SiteHeader v-if="route.path !== '/'" />

    <main class="page-content"><slot /></main>
    <SiteFooter />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '~/stores/user'
import { useCityStore } from '~/stores/city'

const userStore = useUserStore()
const cityStore = useCityStore()
const route = useRoute()

onMounted(() => {
  cityStore.loadFromStorage()
})
</script>

<style scoped>
.layout-default {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.page-content {
  flex: 1;
}
</style>
