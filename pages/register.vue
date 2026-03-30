<template>
  <div style="display:flex;justify-content:center;align-items:center;min-height:80vh;padding:16px">
    <el-card style="width:480px;max-width:100%;padding:20px">
      <h2 style="text-align:center;margin-bottom:24px">用户注册</h2>
      <el-form :model="form" label-width="80px" class="reg-form">
        <el-form-item label="我是">
          <el-radio-group v-model="form.userType">
            <el-radio :label="1">教员（做家教）</el-radio>
            <el-radio :label="2">学员（找家教）</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.mobile" placeholder="请输入手机号" size="large" clearable />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="设置密码（6位以上）" size="large" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleRegister">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center">
        <NuxtLink to="/login" style="color:#409eff">已有账号？去登录</NuxtLink>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'

const form = ref({ userType: 2, mobile: '', password: '', confirmPassword: '' })
const loading = ref(false)
const userStore = useUserStore()
const router = useRouter()
const { post } = useApi()

const handleRegister = async () => {
  if (!form.value.mobile) { ElMessage.warning('请输入手机号'); return }
  if (!/^1[3-9]\d{9}$/.test(form.value.mobile)) { ElMessage.warning('手机号格式不正确'); return }
  if (!form.value.password || form.value.password.length < 6) { ElMessage.warning('密码不能少于6位'); return }
  if (form.value.password !== form.value.confirmPassword) { ElMessage.warning('两次密码不一致'); return }

  loading.value = true
  try {
    const res = await post('/user/api/users/register/simple', {
      mobile: form.value.mobile,
      password: form.value.password,
      userType: form.value.userType
    })
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      ElMessage.success('注册成功')
      router.push('/')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
@media (max-width: 768px) {
  .reg-form { label-width: 70px; }
  .reg-form :deep(.el-form-item__label) { font-size: 13px; }
}
</style>
