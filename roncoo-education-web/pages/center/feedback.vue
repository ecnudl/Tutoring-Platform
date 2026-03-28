<template>
<div>
  <h2 class="page-title">意见反馈</h2>
  <el-form label-width='80px' style='max-width:500px'><el-form-item label='反馈内容'><el-input type='textarea' :rows='4' placeholder='请输入您的意见或建议' /></el-form-item><el-form-item label='联系方式'><el-input placeholder='手机号或邮箱（选填）' /></el-form-item><el-form-item><el-button type='primary'>提交反馈</el-button></el-form-item></el-form>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
