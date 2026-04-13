<template>
  <div class="student-register-page">
    <Head>
      <Title>家长/学员注册 - 591家教网</Title>
    </Head>
    <div class="student-register-container">
      <div class="register-brand">
        <SiteLogo :showText="true" />
      </div>
      <div class="register-card">
        <h2 class="register-title">家长注册</h2>
        <div class="city-info">目前所在城市：<span>{{ cityStore.cityName }}</span></div>
        <el-form :model="form" ref="formRef" label-position="top">
          <el-form-item label="手机号">
            <el-input v-model="form.mobile" placeholder="请输入手机号" size="large" />
          </el-form-item>
          <el-form-item label="您的尊称">
            <el-input v-model="form.realName" placeholder="请输入您的尊称：例如 张女士" size="large" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" size="large" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请重复密码" size="large" show-password />
          </el-form-item>
          <el-form-item>
            <div class="agreement-check" :class="{ 'agreement-check--error': showAgreementError && !form.agreed }">
              <el-checkbox v-model="form.agreed" @change="showAgreementError = false" />
              <span class="agreement-text">
                我已仔细阅读并同意
                <a href="/agreement/user" target="_blank">《用户服务协议》</a>
                <a href="/agreement/disclaimer" target="_blank">《免责声明》</a>
                <a href="/agreement/privacy" target="_blank">《隐私保护政策》</a>
              </span>
            </div>
            <p v-if="showAgreementError && !form.agreed" class="agreement-error-msg">请先阅读并同意相关协议</p>
          </el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="submitting" :disabled="!form.agreed" @click="handleSubmit">注册</el-button>
        </el-form>
        <div class="register-footer">
          已有账号？<NuxtLink to="/login?type=student">去登录</NuxtLink>
          <span>|</span>
          想做家教？<NuxtLink to="/register/teacher">教员注册</NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useCityStore } from '~/stores/city'

const cityStore = useCityStore()
const router = useRouter()
const { post } = useApi()
const formRef = ref(null)
const submitting = ref(false)
const showAgreementError = ref(false)

const form = ref({
  mobile: '',
  realName: '',
  password: '',
  confirmPassword: '',
  agreed: false
})

const handleSubmit = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  if (!form.value.realName) {
    ElMessage.warning('请输入您的尊称')
    return
  }
  if (!form.value.password || form.value.password.length < 6) {
    ElMessage.warning('密码不能少于6位')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  if (!form.value.agreed) {
    showAgreementError.value = true
    ElMessage.warning('请先阅读并同意用户协议、免责声明和隐私保护政策')
    return
  }

  submitting.value = true
  try {
    const res = await post('/user/api/users/register/simple', {
      mobile: form.value.mobile,
      password: form.value.password,
      code: '',
      userType: 2,
      realName: form.value.realName
    })
    if (res.code === 200) {
      ElMessage.success('注册成功！')
      router.push('/login?type=student')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.student-register-page {
  height: 100vh;
  overflow: auto;
  background: var(--color-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-xl);
}

.student-register-container {
  max-width: 480px;
  width: 100%;
}

.register-brand {
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-2xl);
}

.register-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-3xl);
}

.register-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  text-align: center;
  margin-bottom: var(--space-xl);
}

.city-info {
  margin-bottom: var(--space-lg);
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
}

.city-info span {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.register-footer {
  text-align: center;
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin-top: var(--space-lg);
}

.register-footer span {
  margin: 0 var(--space-sm);
}

.register-footer a {
  color: var(--color-primary);
}

.agreement-check {
  display: flex;
  align-items: flex-start;
  gap: var(--space-sm);
  padding: var(--space-md) var(--space-lg);
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: all var(--transition-normal);
}

.agreement-check--error {
  background: #fef2f2;
  border-color: var(--color-error);
}

.agreement-text {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  line-height: var(--line-height-normal);
}

.agreement-text a {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.agreement-error-msg {
  color: var(--color-error);
  font-size: var(--font-size-xs);
  margin-top: 6px;
  padding-left: 4px;
}
</style>
