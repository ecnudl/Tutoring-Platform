<template>
  <div class="find-pwd-page">
    <div class="find-pwd-container">
      <div style="text-align:center;margin-bottom:24px">
        <h2 style="margin-top:0;color:#333">找回密码</h2>
      </div>
      <el-card>
        <el-form :model="form">
          <el-form-item label="手机号">
            <el-input v-model="form.mobile" placeholder="请输入注册手机号" size="large" />
          </el-form-item>
          <el-form-item label="短信验证码">
            <div style="display:flex;gap:8px">
              <el-input v-model="form.code" placeholder="请输入短信验证码" size="large" style="flex:1" />
              <el-button size="large" :disabled="countdown > 0" @click="sendCode">{{ countdown > 0 ? `${countdown}秒` : '发送验证码' }}</el-button>
            </div>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="form.password" type="password" placeholder="请输入新密码（6位以上）" size="large" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入新密码" size="large" show-password />
          </el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleReset">重置密码</el-button>
        </el-form>
        <div style="text-align:center;margin-top:16px;font-size:13px">
          <NuxtLink to="/login" style="color:#409eff">返回登录</NuxtLink>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const { post } = useApi()
const router = useRouter()
const form = ref({ mobile: '', code: '', password: '', confirmPassword: '' })
const loading = ref(false)
const countdown = ref(0)

const sendCode = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  try {
    const res = await post('/user/api/sms/send-reset', { mobile: form.value.mobile })
    if (res.code === 200) {
      ElMessage.success('验证码已发送')
      countdown.value = 60
      const timer = setInterval(() => { if (--countdown.value <= 0) clearInterval(timer) }, 1000)
    } else {
      ElMessage.error(res.msg || '发送失败')
    }
  } catch (e) {
    ElMessage.error('发送失败')
  }
}

const handleReset = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  if (!form.value.code) { ElMessage.warning('请输入验证码'); return }
  if (!form.value.password || form.value.password.length < 6) {
    ElMessage.warning('密码不能少于6位')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  loading.value = true
  try {
    const res = await post('/user/api/password/reset', {
      mobile: form.value.mobile,
      code: form.value.code,
      password: form.value.password
    })
    if (res.code === 200) {
      ElMessage.success('密码重置成功')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '重置失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}
</script>
