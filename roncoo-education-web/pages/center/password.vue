<template>
<div>
  <h2 class="page-title">修改密码</h2>
  <el-form label-width='100px' style='max-width:500px'><el-form-item label='手机号'><el-input /></el-form-item><el-form-item label='验证码'><el-input><template #append><el-button>获取验证码</el-button></template></el-input></el-form-item><el-form-item label='新密码'><el-input type='password' show-password /></el-form-item><el-form-item><el-button type='primary'>确认修改</el-button></el-form-item></el-form>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
