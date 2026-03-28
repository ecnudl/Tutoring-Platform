<template>
  <div style="display:flex;justify-content:center;align-items:center;height:100vh;background:linear-gradient(135deg,#667eea,#764ba2)">
    <el-card style="width:400px;padding:20px">
      <h2 style="text-align:center;margin-bottom:24px">家教平台管理后台</h2>
      <el-form :model="form" @keyup.enter="handleLogin">
        <el-form-item><el-input v-model="form.mobile" placeholder="管理员手机号" size="large" clearable /></el-form-item>
        <el-form-item><el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password /></el-form-item>
        <el-form-item><el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleLogin">登录</el-button></el-form-item>
      </el-form>
      <p style="text-align:center;color:#999;font-size:12px">默认账号: 18302045627 / admin123</p>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { post } from '@/api/index'
import { ElMessage } from 'element-plus'

const form = ref({ mobile: '', password: '' })
const loading = ref(false)
const router = useRouter()

const handleLogin = async () => {
  if (!form.value.mobile || !form.value.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const res = await post('/system/admin/login/simple', {
      mobile: form.value.mobile,
      password: form.value.password
    })
    if (res.code === 200) {
      localStorage.setItem('admin_token', res.data.token)
      localStorage.setItem('admin_name', res.data.realName || res.data.mobile)
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}
</script>
