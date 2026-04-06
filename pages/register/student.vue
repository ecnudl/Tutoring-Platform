<template>
  <div class="student-register-page">
    <Head>
      <Title>家长/学员注册 - 51家教网</Title>
    </Head>
    <div class="student-register-container">
      <div class="register-header">
        <h2>家长注册</h2>
      </div>
      <el-card>
        <div class="city-info">目前所在城市：<span>{{ cityStore.cityName }}</span></div>
        <el-form :model="form" ref="formRef" label-position="top">

          <el-form-item label="手机号">
            <el-input v-model="form.mobile" placeholder="请输入手机号" size="large" />
          </el-form-item>
          <el-form-item label="您的尊称">
            <el-input v-model="form.realName" placeholder="请输入您的尊称：例如 张女士" size="large" />
          </el-form-item>
          <el-form-item label="短信验证码">
            <div style="display:flex;gap:8px">
              <el-input v-model="form.smsCode" placeholder="请输入短信验证码" size="large" style="flex:1" />
              <el-button size="large" :disabled="countdown > 0" :loading="sendingCode" @click="sendSms">{{ countdown > 0 ? `${countdown}秒后重发` : '发送验证码' }}</el-button>
            </div>
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
      </el-card>
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
const countdown = ref(0)
const sendingCode = ref(false)

const form = ref({
  mobile: '',
  realName: '',
  smsCode: '',
  password: '',
  confirmPassword: '',
  agreed: false
})

const sendSms = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  sendingCode.value = true
  try {
    const res = await post('/user/api/users/send/code', { mobile: form.value.mobile })
    if (res.code === 200) {
      ElMessage.success('验证码已发送，请注意查收')
      countdown.value = 60
      const timer = setInterval(() => {
        if (--countdown.value <= 0) clearInterval(timer)
      }, 1000)
    } else {
      ElMessage.error(res.msg || '发送失败')
    }
  } catch (e) {
    ElMessage.error('发送失败')
  } finally {
    sendingCode.value = false
  }
}

const handleSubmit = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  if (!form.value.realName) {
    ElMessage.warning('请输入您的尊称')
    return
  }
  if (!form.value.smsCode) {
    ElMessage.warning('请输入短信验证码')
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
    const res = await post('/user/api/register/student', {
      mobile: form.value.mobile,
      realName: form.value.realName,
      smsCode: form.value.smsCode,
      password: form.value.password,
      cityId: cityStore.cityId
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
  min-height: 100vh;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  overflow-y: auto;
}

.student-register-container {
  max-width: 500px;
  width: 100%;
  margin: auto;
}

.register-header {
  text-align: center;
  margin-bottom: 24px;
}

.register-header h2 {
  color: #fff;
  font-size: 28px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.city-info {
  margin-bottom: 16px;
  color: #666;
  font-size: 14px;
}

.city-info span {
  color: #409eff;
  font-weight: 500;
}

.register-footer {
  text-align: center;
  color: #999;
  font-size: 13px;
  margin-top: 16px;
}

.register-footer span {
  margin: 0 8px;
}

.register-footer a {
  color: #409eff;
  text-decoration: none;
}

.register-footer a:hover {
  text-decoration: underline;
}

a {
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.agreement-check {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 12px 16px;
  background: #f8f9fc;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  transition: all 0.3s;
}

.agreement-check--error {
  background: #fef0f0;
  border-color: #f56c6c;
}

.agreement-text {
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
}
.agreement-text a {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}
.agreement-text a:hover {
  text-decoration: underline;
}
.agreement-error-msg {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 6px;
  padding-left: 4px;
}
</style>
