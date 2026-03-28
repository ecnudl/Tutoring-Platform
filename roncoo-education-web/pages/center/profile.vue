<template>
<div>
  <h2 class="page-title">个人资料</h2>
  <el-form label-width='100px' style='max-width:500px'><el-form-item label='昵称'><el-input placeholder='请输入昵称' /></el-form-item><el-form-item label='性别'><el-radio-group><el-radio :label='1'>男</el-radio><el-radio :label='2'>女</el-radio></el-radio-group></el-form-item><el-form-item label='头像'><el-upload action='#' :auto-upload='false'><el-button>上传头像</el-button></el-upload></el-form-item><el-form-item><el-button type='primary'>保存</el-button></el-form-item></el-form>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
