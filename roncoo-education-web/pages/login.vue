<template>
  <div style="display:flex;justify-content:center;align-items:center;min-height:80vh">
    <el-card style="width:420px;padding:20px">
      <h2 style="text-align:center;margin-bottom:24px">用户登录</h2>
      <el-form :model="form" label-width="0" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="form.mobile" placeholder="手机号" size="large" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div style="display:flex;justify-content:space-between">
        <NuxtLink to="/register" style="color:#409eff">注册账号</NuxtLink>
        <NuxtLink to="/forgot-password" style="color:#999">忘记密码</NuxtLink>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'

const form = ref({ mobile: '', password: '' })
const loading = ref(false)
const userStore = useUserStore()
const router = useRouter()
const { post } = useApi()

const handleLogin = async () => {
  if (!form.value.mobile) { ElMessage.warning('请输入手机号'); return }
  if (!form.value.password) { ElMessage.warning('请输入密码'); return }
  loading.value = true
  try {
    const res = await post('/user/api/users/login/simple', {
      mobile: form.value.mobile,
      password: form.value.password
    })
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>
