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
        <el-form :model="passwordForm" @keyup.enter="handlePasswordLogin" class="login-form">
          <el-form-item>
            <el-input v-model="passwordForm.mobile" placeholder="请输入用户名或手机号" size="large" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="passwordForm.password" type="password" placeholder="请输入密码" size="large" show-password />
          </el-form-item>
          <el-form-item v-if="captchaRequired">
            <div style="display:flex;gap:8px;width:100%">
              <el-input v-model="passwordForm.verCode" placeholder="请输入右图字符" size="large" style="flex:1" />
              <img v-if="captchaImg" :src="captchaImg" alt="captcha" @click="loadCaptcha" style="height:40px;cursor:pointer;border-radius:6px" title="点击刷新" />
              <el-button v-else size="large" @click="loadCaptcha">获取验证码</el-button>
            </div>
          </el-form-item>
          <label aria-hidden="true" style="position:absolute;left:-9999px;width:1px;height:1px;opacity:0;pointer-events:none">
            如果您是真人请勿填写
            <input v-model="passwordForm.honeypot" type="text" name="hp_zxq3" autocomplete="off" tabindex="-1" />
          </label>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handlePasswordLogin">登录</el-button>
        </el-form>
        <div class="login-footer">
          <NuxtLink to="/forgot-password">找回密码</NuxtLink>
          <NuxtLink :to="selectedRole === 'student' ? '/register/student' : '/register/teacher'">我要注册</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'

const selectedRole = ref(null)
const passwordForm = ref({ mobile: '', password: '', verToken: '', verCode: '', honeypot: '' })
const loading = ref(false)
const captchaImg = ref('')
const captchaRequired = ref(false)
const userStore = useUserStore()
const router = useRouter()
const { post, get } = useApi()
const route = useRoute()

if (route.query.type === 'student' || route.query.type === 'teacher') {
  selectedRole.value = route.query.type
}

const loadCaptcha = async () => {
  try {
    const r = await get('/system/api/common/code')
    if (r?.code === 200 && r.data) {
      passwordForm.value.verToken = r.data.verToken
      captchaImg.value = r.data.img
      passwordForm.value.verCode = ''
    }
  } catch (e) { ElMessage.error('图形码获取失败') }
}

const handlePasswordLogin = async () => {
  if (!passwordForm.value.mobile) { ElMessage.warning('请输入用户名或手机号'); return }
  if (!passwordForm.value.password) { ElMessage.warning('请输入密码'); return }
  if (captchaRequired.value && !passwordForm.value.verCode) {
    ElMessage.warning('请输入图形验证码')
    return
  }

  loading.value = true
  try {
    const expectedUserType = selectedRole.value === 'teacher' ? 1 : 2
    const loginData = {
      mobile: passwordForm.value.mobile,
      password: passwordForm.value.password,
      userType: expectedUserType,
      verToken: passwordForm.value.verToken,
      verCode: passwordForm.value.verCode,
      honeypot: passwordForm.value.honeypot
    }
    const res = await post('/user/api/users/login/simple', loginData)
    if (res.code === 200) {
      userStore.saveLogin(res.data)
      userStore.fetchNickname()
      ElMessage.success('登录成功')
      router.push(route.query.redirect || '/')
    } else if (res.msg === 'CAPTCHA_REQUIRED') {
      captchaRequired.value = true
      await loadCaptcha()
      ElMessage.warning('多次失败, 请完成图形验证码')
    } else {
      ElMessage.error(res.msg || '登录失败')
      if (captchaRequired.value) await loadCaptcha()
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
