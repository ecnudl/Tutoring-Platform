<template>
<div>
  <h2 class="page-title">我的订单</h2>
  <el-alert title="在线支付暂未接入，请联系客服或走后台人工开通流程" type="warning" :closable="false" style="margin-bottom:20px" />
  <el-card class="orders-card">
    <h4>联系客服</h4>
    <p style="color:#666;margin-top:8px">如需开通VIP或其他付费服务，请通过以下方式联系：</p>
    <p style="margin-top:8px">客服电话：400-000-0000</p>
    <p>工作时间：周一至周五 9:00-18:00</p>
  </el-card>
</div>
</template>
<script setup>
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
</script>
<style scoped>
.orders-card { max-width: 500px; }
@media (max-width: 768px) { .orders-card { max-width: 100%; } }
</style>
