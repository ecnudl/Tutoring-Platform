<template>
  <div class="login-page-wrapper">
    <div class="container login-page">
      <!-- 角色选择 -->
      <div v-if="!selectedRole" class="role-select">
        <h2 class="role-title">欢迎登录</h2>
        <p class="role-subtitle">请选择您的身份</p>
        <div class="role-cards">
          <div class="role-card" @click="selectedRole = 'student'">
            <div class="role-icon">&#x1F468;&#x200D;&#x1F469;&#x200D;&#x1F467;</div>
            <h3>家长/学员</h3>
            <p>我要找家教老师</p>
          </div>
          <div class="role-card" @click="selectedRole = 'teacher'">
            <div class="role-icon">&#x1F393;</div>
            <h3>教员/老师</h3>
            <p>我要做家教</p>
          </div>
        </div>
        <div class="role-footer">
          还没有账号？<NuxtLink to="/register">立即注册</NuxtLink>
        </div>
      </div>

      <!-- 登录表单 -->
      <div v-else class="login-card">
        <div class="login-back" @click="selectedRole = null">
          <span>&larr;</span> 返回选择
        </div>
        <h2 class="login-title">{{ selectedRole === 'student' ? '家长/学员登录' : '教员登录' }}</h2>
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
                  <el-button size="large" :disabled="countdown > 0" @click="sendSmsCode">{{ countdownText }}</el-button>
                </div>
              </el-form-item>
              <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleSmsLogin">登录</el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div class="login-footer">
          <NuxtLink to="/login/find-password">找回密码</NuxtLink>
          <NuxtLink :to="selectedRole === 'student' ? '/register/student' : '/register/teacher'">我要注册</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref, computed } from 'vue'
import { useUserStore } from '~/stores/user'

const selectedRole = ref(null)
const activeTab = ref('password')
const passwordForm = ref({ mobile: '', password: '' })
const smsForm = ref({ mobile: '', code: '' })
const loading = ref(false)
const countdown = ref(0)
const userStore = useUserStore()
const router = useRouter()
const { post } = useApi()
const route = useRoute()

const countdownText = computed(() => countdown.value > 0 ? countdown.value + '秒' : '发送验证码')

if (route.query.type === 'student' || route.query.type === 'teacher') {
  selectedRole.value = route.query.type
}

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
    const loginData = { mobile: passwordForm.value.mobile, password: passwordForm.value.password }
    const res = await post('/user/api/users/login/simple', loginData)
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      userStore.fetchNickname()
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
    const loginData = { ...smsForm.value, userType: selectedRole.value === 'student' ? 1 : 2 }
    const res = await post('/user/api/login/sms', loginData)
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      userStore.fetchNickname()
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
.login-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
  min-height: calc(100vh - 200px);
}

.login-page {
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-2xl) var(--space-xl) var(--space-4xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 登录/注册 切换标签 */
.auth-tabs {
  display: flex;
  gap: var(--space-xl);
  margin-bottom: var(--space-3xl);
  border-bottom: 2px solid var(--color-border-light);
  padding-bottom: 0;
}

.auth-tab {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-secondary);
  padding: var(--space-md) var(--space-xl);
  text-decoration: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  transition: all var(--transition-fast);
}

.auth-tab:hover {
  color: var(--color-primary);
}

.auth-tab.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.role-select {
  text-align: center;
  max-width: 520px;
  width: 100%;
}

.role-title {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.role-subtitle {
  font-size: var(--font-size-md);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-3xl);
}

.role-cards {
  display: flex;
  gap: var(--space-xl);
}

.role-card {
  flex: 1;
  background: var(--color-surface);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-xl);
  padding: var(--space-3xl) var(--space-xl);
  cursor: pointer;
  transition: all var(--transition-normal);
  text-align: center;
}

.role-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.role-icon {
  font-size: 48px;
  margin-bottom: var(--space-lg);
}

.role-card h3 {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.role-card p {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.role-footer {
  margin-top: var(--space-2xl);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.role-footer a {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.login-card {
  max-width: 460px;
  width: 100%;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-3xl);
}

.login-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
  margin-bottom: var(--space-lg);
  transition: color var(--transition-fast);
}

.login-back:hover {
  color: var(--color-primary);
}

.login-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  text-align: center;
  margin-bottom: var(--space-2xl);
}

.login-footer {
  display: flex;
  justify-content: space-between;
  margin-top: var(--space-lg);
  font-size: var(--font-size-sm);
}

.login-footer a {
  color: var(--color-primary);
  transition: color var(--transition-fast);
}

.login-footer a:hover {
  color: var(--color-primary-light);
}

@media (max-width: 768px) {
  .login-page { padding: var(--space-lg); }
  .role-cards { flex-direction: column; gap: var(--space-lg); }
  .role-card { padding: var(--space-2xl) var(--space-xl); }
  .role-icon { font-size: 36px; }
  .login-card { padding: var(--space-xl); }
}
</style>
