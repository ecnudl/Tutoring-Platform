<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <h2>会员登录</h2>
      </div>
      <el-card>
        <el-tabs v-model="activeTab" stretch>
          <el-tab-pane label="账号登录" name="password">
            <el-form :model="passwordForm" @keyup.enter="handlePasswordLogin">
              <el-form-item>
                <el-input v-model="passwordForm.mobile" placeholder="请输入用户名或手机号" size="large" />
              </el-form-item>
              <el-form-item>
                <el-input v-model="passwordForm.password" type="password" placeholder="请输入密码" size="large" show-password />
              </el-form-item>
              <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handlePasswordLogin">登录</el-button>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="短信登录" name="sms">
            <el-form :model="smsForm">
              <el-form-item>
                <el-input v-model="smsForm.mobile" placeholder="请输入手机号" size="large" />
              </el-form-item>
              <el-form-item>
                <div style="display:flex;gap:8px">
                  <el-input v-model="smsForm.code" placeholder="请输入短信验证码" size="large" style="flex:1" />
                  <el-button size="large" :disabled="countdown > 0" @click="sendSmsCode">{{ countdown > 0 ? `${countdown}秒` : '发送验证码' }}</el-button>
                </div>
              </el-form-item>
              <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleSmsLogin">登录</el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div class="login-footer">
          <NuxtLink to="/login/find-password">找回密码</NuxtLink>
          <NuxtLink to="/register">我要注册</NuxtLink>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'

const activeTab = ref('password')
const passwordForm = ref({ mobile: '', password: '' })
const smsForm = ref({ mobile: '', code: '' })
const loading = ref(false)
const countdown = ref(0)
const userStore = useUserStore()
const router = useRouter()
const { post } = useApi()
const route = useRoute()

const sendSmsCode = async () => {
  if (!smsForm.value.mobile || !/^1[3-9]\d{9}$/.test(smsForm.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  try {
    const res = await post('/user/api/sms/send-login', { mobile: smsForm.value.mobile })
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

const handlePasswordLogin = async () => {
  if (!passwordForm.value.mobile) { ElMessage.warning('请输入用户名或手机号'); return }
  if (!passwordForm.value.password) { ElMessage.warning('请输入密码'); return }

  // 临时管理员账号（开发测试用）
  if (passwordForm.value.mobile === 'admin' && passwordForm.value.password === 'admin123') {
    userStore.saveLogin({
      token: 'admin-token-' + Date.now(),
      mobile: 'admin',
      userType: 0,
      isAdmin: true
    })
    ElMessage.success('管理员登录成功')
    router.push(route.query.redirect || '/')
    return
  }

  loading.value = true
  try {
    const res = await post('/user/api/login', passwordForm.value)
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      ElMessage.success('登录成功')
      router.push(route.query.redirect || '/')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

const handleSmsLogin = async () => {
  if (!smsForm.value.mobile || !/^1[3-9]\d{9}$/.test(smsForm.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  if (!smsForm.value.code) { ElMessage.warning('请输入验证码'); return }
  loading.value = true
  try {
    const res = await post('/user/api/login/sms', smsForm.value)
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      ElMessage.success('登录成功')
      router.push(route.query.redirect || '/')
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

<style scoped>
.login-page {
  min-height: 100vh;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  overflow: hidden;
}

.login-container {
  max-width: 420px;
  width: 100%;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-header h2 {
  color: #fff;
  font-size: 28px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
  font-size: 13px;
}

.login-footer a {
  color: #409eff;
  text-decoration: none;
  transition: color 0.2s;
}

.login-footer a:hover {
  color: #66b1ff;
}
</style>
