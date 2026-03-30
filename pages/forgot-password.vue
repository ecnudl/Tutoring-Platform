<template>
  <div style="display:flex;justify-content:center;align-items:center;min-height:80vh;padding:16px">
    <el-card style="width:420px;max-width:100%;padding:20px">
      <h2 style="text-align:center;margin-bottom:24px">重置密码</h2>
      <el-form label-width="0">
        <el-form-item><el-input placeholder="手机号" /></el-form-item>
        <el-form-item><el-input placeholder="验证码"><template #append><el-button>获取验证码</el-button></template></el-input></el-form-item>
        <el-form-item><el-input type="password" placeholder="新密码" show-password /></el-form-item>
        <el-form-item><el-button type="primary" style="width:100%">重置密码</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
