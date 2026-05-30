<template>
<div>
  <h2 class="page-title">我的订单</h2>

  <div v-if="loading" style="text-align:center;padding:40px;color:#999">
    <el-icon class="is-loading" :size="22"><Loading /></el-icon>
  </div>

  <div v-else-if="orders.length === 0" class="empty-state">
    <div class="empty-icon">📋</div>
    <div class="empty-title">暂无订单</div>
    <div class="empty-hint" v-if="!userStore.isTutor">您可前往请家教提交需求, 客服会尽快撮合</div>
    <div class="empty-hint" v-else>客服撮合后, 您的订单会显示在这里</div>
    <NuxtLink v-if="!userStore.isTutor" to="/qjj">
      <el-button type="primary" style="margin-top:14px">请家教</el-button>
    </NuxtLink>
  </div>

  <div v-else class="orders-list">
    <div v-for="o in orders" :key="o.id" class="order-card">
      <div v-if="o.displayNo" class="order-no">订单号 <span class="order-no-val">{{ o.displayNo }}</span></div>
      <div class="order-row">
        <div class="order-title">
          <span v-if="o.grade">{{ o.grade }}</span>
          <span v-if="o.subjects">{{ formatSubjects(o.subjects) }}</span>
          <span v-if="o.location" class="order-location">{{ o.location }}</span>
        </div>
        <div class="order-meta">
          <el-tag :type="statusType(o.resStatus)" size="small" effect="light">{{ o.statusText }}</el-tag>
          <span class="order-date">{{ o.date }}</span>
        </div>
      </div>
      <div v-if="o.detail" class="order-detail">{{ o.detail }}</div>
      <div v-if="o.cancelReason" class="order-cancel">取消原因: {{ o.cancelReason }}</div>
    </div>
  </div>

  <el-card class="orders-card" style="margin-top:24px">
    <h4 style="margin:0 0 8px">订单有疑问? 联系客服</h4>
    <p style="color:#64748b;margin:0">客服电话: <strong>13795420591</strong></p>
    <p style="color:#64748b;margin:6px 0 0">工作时间: 周一至周日 9:00-21:00</p>
  </el-card>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import { useUserStore } from '~/stores/user'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const userStore = useUserStore()
const { post } = useApi()
const orders = ref([])
const loading = ref(true)

const statusType = (code) => {
  if (code === 0) return 'warning'
  if (code === 1) return 'primary'
  if (code === 2) return 'success'
  if (code === 3) return 'info'
  return ''
}

const formatSubjects = (csv) => {
  if (!csv) return ''
  return String(csv).split(',').map(s => s.trim()).filter(Boolean).join(' ')
}

onMounted(async () => {
  try {
    const res = await post('/user/auth/reservation/page', { pageSize: 50 })
    if (res?.code === 200 && res.data?.list) {
      orders.value = res.data.list
    }
  } catch (e) {
    console.warn('[orders] load failed', e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 8px;
}
.order-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: 16px 20px;
  transition: border-color var(--transition-fast);
}
.order-card:hover {
  border-color: var(--color-primary-light);
}
.order-no {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
}
.order-no-val {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  color: var(--color-text);
  font-weight: 600;
  letter-spacing: 0.3px;
}
.order-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}
.order-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text);
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: baseline;
}
.order-location {
  font-weight: 400;
  color: var(--color-text-secondary);
  font-size: 14px;
}
.order-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
  flex-shrink: 0;
}
.order-date {
  font-size: 12px;
  color: var(--color-text-muted);
}
.order-detail {
  margin-top: 8px;
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.6;
}
.order-cancel {
  margin-top: 6px;
  font-size: 12px;
  color: var(--color-text-muted);
}
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: var(--color-surface);
  border: 1px dashed var(--color-border);
  border-radius: var(--radius-md);
}
.empty-icon { font-size: 48px; }
.empty-title {
  margin-top: 12px;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
}
.empty-hint {
  margin-top: 6px;
  font-size: 13px;
  color: var(--color-text-secondary);
}
.orders-card { max-width: 500px; }
@media (max-width: 768px) {
  .orders-card { max-width: 100%; }
}
</style>
