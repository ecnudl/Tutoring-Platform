<template>
<div>
  <h2 class="page-title">VIP会员</h2>
  <el-card style='text-align:center;padding:40px'><el-tag size='large' type='info'>普通用户</el-tag><p style='margin-top:16px;color:#999'>VIP会员可享受优先推荐、无限浏览等特权</p><el-alert title='VIP开通请联系管理员，暂不支持在线支付' type='info' :closable='false' style='margin-top:16px' /></el-card>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
