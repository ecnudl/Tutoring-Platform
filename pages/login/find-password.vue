<template>
<div class="fp-page">
  <Head><Title>找回密码 - 591家教网</Title></Head>
  <div class="fp-card">
    <h2 class="fp-title">找回密码</h2>
    <p class="fp-sub">通过注册时设置的安全问题重置</p>

    <!-- Step 1: 输入手机号, 拿安全问题 -->
    <el-form v-if="step === 1" :model="form" ref="step1Ref" label-position="top">
      <el-form-item label="注册手机号" prop="mobile">
        <el-input v-model="form.mobile" placeholder="请输入您注册时的手机号" size="large" />
      </el-form-item>
      <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleStep1">下一步</el-button>
    </el-form>

    <!-- Step 2: 答安全问题 + 设置新密码 -->
    <el-form v-if="step === 2" :model="form" ref="step2Ref" label-position="top">
      <el-form-item label="安全问题">
        <div class="fp-question">{{ securityQuestion }}</div>
      </el-form-item>
      <el-form-item label="您的答案">
        <el-input v-model="form.answer" placeholder="不区分大小写" size="large" />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.password" type="password" placeholder="6-20 位" size="large" show-password />
      </el-form-item>
      <el-form-item label="确认新密码">
        <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入" size="large" show-password />
      </el-form-item>
      <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleStep2">提交重置</el-button>
      <div class="fp-back" @click="step = 1">&lt; 返回上一步</div>
    </el-form>

    <div class="fp-footer">
      想起密码了？<NuxtLink to="/login">回到登录</NuxtLink>
      <span>·</span>
      没设置安全问题？<a href="javascript:;" @click="showContact = true">联系客服</a>
    </div>

    <el-dialog v-model="showContact" title="联系客服重置密码" width="420px">
      <p style="line-height:1.8">客服将核实您的身份后人工重置密码。</p>
      <p style="line-height:1.8">客服热线：<strong style="color:var(--color-primary)">{{ hotline }}</strong></p>
      <p style="line-height:1.8">微信：591jiajiao</p>
    </el-dialog>
  </div>
</div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useSiteConfig } from '~/composables/useSiteConfig'

const { post } = useApi()
const router = useRouter()
const { config, load: loadSiteConfig } = useSiteConfig()
loadSiteConfig()

const step = ref(1)
const loading = ref(false)
const showContact = ref(false)
const securityQuestion = ref('')
const form = ref({ mobile: '', answer: '', password: '', confirmPassword: '' })

const hotline = computed(() => config.value.siteHotline || '13795420591')

const handleStep1 = async () => {
  if (!form.value.mobile || !/^1[3-9]\d{9}$/.test(form.value.mobile)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  loading.value = true
  try {
    const res = await post('/user/api/users/password/security-question', { mobile: form.value.mobile })
    if (res.code === 200 && res.data) {
      securityQuestion.value = res.data
      step.value = 2
    } else {
      ElMessage.error(res.msg || '获取安全问题失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally { loading.value = false }
}

const handleStep2 = async () => {
  if (!form.value.answer) { ElMessage.warning('请填写安全答案'); return }
  if (!form.value.password || form.value.password.length < 6) { ElMessage.warning('密码不能少于 6 位'); return }
  if (form.value.password !== form.value.confirmPassword) { ElMessage.warning('两次密码不一致'); return }
  loading.value = true
  try {
    const res = await post('/user/api/users/password/reset-by-question', {
      mobile: form.value.mobile,
      answer: form.value.answer,
      password: form.value.password
    })
    if (res.code === 200) {
      ElMessage.success('密码重置成功，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '重置失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { loading.value = false }
}
</script>

<style scoped>
.fp-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex; align-items: center; justify-content: center;
  padding: 40px 20px;
}
.fp-card {
  width: 100%; max-width: 440px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: 12px;
  padding: 36px 32px;
  box-shadow: var(--shadow-lg);
}
.fp-title { font-size: 22px; font-weight: 600; color: var(--color-text); margin: 0 0 6px; letter-spacing: 1px; }
.fp-sub { font-size: 13px; color: var(--color-text-secondary); margin: 0 0 24px; }

.fp-question {
  background: var(--color-primary-lighter);
  color: var(--color-primary-dark);
  padding: 12px 14px; border-radius: 6px;
  font-size: 15px; font-weight: 500;
}

.fp-back {
  margin-top: 14px; text-align: center;
  font-size: 13px; color: var(--color-text-secondary);
  cursor: pointer;
}
.fp-back:hover { color: var(--color-primary); }

.fp-footer {
  margin-top: 22px; text-align: center;
  font-size: 13px; color: var(--color-text-secondary);
}
.fp-footer a { color: var(--color-primary); margin: 0 6px; }
.fp-footer a:hover { color: var(--color-primary-dark); text-decoration: underline; }
.fp-footer span { color: var(--color-border); margin: 0 4px; }
</style>
