<template>
<div>
  <h2 class="page-title">VIP会员</h2>
  <el-card class="vip-card" style="text-align:center;padding:40px">
    <div v-if="vip">
      <el-tag size="large" type="warning" style="font-size:18px;padding:12px 24px">VIP会员</el-tag>
      <p style="margin-top:16px;color:#666">到期时间：{{ vip.endTime || '永久' }}</p>
    </div>
    <div v-else>
      <el-tag size="large" type="info" style="font-size:18px;padding:12px 24px">普通用户</el-tag>
      <p style="margin-top:16px;color:#999">VIP会员可享受优先推荐、查看联系方式等特权</p>
    </div>
    <el-alert title="VIP开通请联系管理员或客服，支持线下缴费由老师代收" type="info" :closable="false" style="margin-top:24px;text-align:left" />
  </el-card>
</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const { get } = useApi()
const vip = ref(null)
onMounted(async () => {
  try {
    const res = await get('/user/auth/vip/status')
    if (res.code === 200 && res.data) vip.value = res.data
  } catch (e) { console.error(e) }
})
</script>
<style scoped>
.vip-card { max-width: 500px; }
@media (max-width: 768px) {
  .vip-card { max-width: 100%; padding: 20px !important; }
}
</style>
