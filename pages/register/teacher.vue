<template>
  <div class="register-wrapper">
    <Head>
      <Title>教员注册 - 591家教网</Title>
      <Meta name="description" content="注册成为家教教员，接单赚取课时费。大学生、专职教员、在职教师均可注册。" />
    </Head>

    <!-- 顶部横幅 -->
    <div class="register-banner">
      <div class="container">
        <h1>教员注册</h1>
        <p>加入我们，开启您的家教之旅</p>
      </div>
    </div>

    <!-- 注册步骤指示 -->
    <div class="container">
      <div class="steps-wrapper">
        <div class="step-item active">
          <div class="step-number">1</div>
          <div class="step-text">填写信息</div>
        </div>
        <div class="step-line"></div>
        <div class="step-item">
          <div class="step-number">2</div>
          <div class="step-text">等待审核</div>
        </div>
        <div class="step-line"></div>
        <div class="step-item">
          <div class="step-number">3</div>
          <div class="step-text">开始接单</div>
        </div>
      </div>

      <!-- 注册表单 -->
      <div class="form-container">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">

          <!-- 账号信息 -->
          <div class="form-section">
            <div class="section-title">账号信息</div>
            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="form.mobile" placeholder="请输入手机号" size="large" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="密码" prop="password">
                  <el-input v-model="form.password" type="password" placeholder="6-20位密码" size="large" show-password />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 个人信息 -->
          <div class="form-section">
            <div class="section-title">个人信息</div>
            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="form.realName" placeholder="请输入真实姓名" size="large" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="form.gender" size="large">
                    <el-radio :label="1" border>男</el-radio>
                    <el-radio :label="2" border>女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="教员类型" prop="tutorType">
                  <el-select v-model="form.tutorType" placeholder="请选择教员类型" size="large" style="width:100%">
                    <el-option label="大学生" :value="1" />
                    <el-option label="专职教员" :value="2" />
                    <el-option label="在职教师" :value="3" />
                    <el-option label="海归外教" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="最高学历" prop="degree">
                  <el-select v-model="form.degree" placeholder="请选择学历" size="large" style="width:100%">
                    <el-option label="高中" :value="1" />
                    <el-option label="大专" :value="2" />
                    <el-option label="本科" :value="3" />
                    <el-option label="硕士" :value="4" />
                    <el-option label="博士" :value="5" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="毕业/在读院校" prop="universityId">
                  <el-select v-model="form.universityId" filterable placeholder="请选择或搜索学校" size="large" style="width:100%" @change="onUniversityChange">
                    <el-option v-for="u in universityOptions" :key="u.id" :label="u.uniName" :value="u.id" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="专业" prop="major">
                  <el-input v-model="form.major" placeholder="请输入专业" size="large" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="年级/入职年份">
                  <el-input v-model="form.gradeYear" placeholder="如：大三 / 2020年入职" size="large" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="授课方式">
                  <el-select v-model="form.teachingMethod" placeholder="请选择" size="large" style="width:100%">
                    <el-option label="上门家教" :value="1" />
                    <el-option label="在线辅导" :value="2" />
                    <el-option label="均可" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 家教信息 -->
          <div class="form-section">
            <div class="section-title">家教信息</div>
            <el-form-item label="可教授科目" prop="subjects">
              <el-select v-model="form.subjects" multiple collapse-tags placeholder="请选择可教授科目（可多选）" size="large" style="width:100%">
                <el-option v-for="s in subjectOptions" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>

            <el-form-item label="可授课区域" prop="districts">
              <el-select v-model="form.districts" multiple collapse-tags placeholder="请选择可授课区域（可多选）" size="large" style="width:100%">
                <el-option v-for="d in districtNames" :key="d" :label="d" :value="d" />
              </el-select>
            </el-form-item>

            <el-row :gutter="20">
              <el-col :span="12" :xs="24">
                <el-form-item label="期望薪资">
                  <el-input v-model="form.expectedSalary" placeholder="如：100-150" size="large">
                    <template #append>元/小时</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :xs="24">
                <el-form-item label="家教经验">
                  <el-select v-model="form.experience" placeholder="请选择" size="large" style="width:100%">
                    <el-option label="无经验" :value="0" />
                    <el-option label="1年以下" :value="1" />
                    <el-option label="1-3年" :value="2" />
                    <el-option label="3-5年" :value="3" />
                    <el-option label="5年以上" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="自我介绍">
              <el-input v-model="form.introduction" type="textarea" :rows="5" placeholder="请简要介绍您的教学经验、特长、获奖情况等（200字以内）" maxlength="200" show-word-limit />
            </el-form-item>
          </div>

          <!-- 资质证书（选填） -->
          <div class="form-section">
            <div class="section-title">
              资质证书
              <span class="section-optional">选填，上传后可获得认证标识</span>
            </div>
            <p class="cert-tip">上传教师资格证、学生证、学历证等资质证明，审核通过后将获得<strong>已认证</strong>标识，提升信任度和接单率。您也可以注册后在个人中心补充上传。</p>

            <div v-for="(cert, idx) in certList" :key="idx" class="cert-item">
              <el-row :gutter="12" align="middle">
                <el-col :span="6" :xs="12">
                  <el-select v-model="cert.certType" placeholder="证书类型" size="large" style="width:100%">
                    <el-option label="教师资格证" :value="3" />
                    <el-option label="学生证" :value="2" />
                    <el-option label="学历证" :value="4" />
                    <el-option label="身份证" :value="1" />
                    <el-option label="其他" :value="5" />
                  </el-select>
                </el-col>
                <el-col :span="6" :xs="12">
                  <el-input v-model="cert.certName" placeholder="证书名称" size="large" />
                </el-col>
                <el-col :span="8" :xs="16">
                  <div v-if="cert.certUrl" class="cert-preview">
                    <el-image :src="cert.certUrl" style="width:80px;height:56px;border-radius:4px" fit="cover" />
                    <el-button size="small" text type="danger" @click="cert.certUrl = ''">移除</el-button>
                  </div>
                  <div v-else>
                    <input type="file" :ref="el => certFileRefs[idx] = el" accept="image/*" style="display:none" @change="e => handleCertFileUpload(e, idx)" />
                    <el-button size="small" :loading="cert.uploading" @click="certFileRefs[idx]?.click()">上传照片</el-button>
                    <span class="cert-file-hint">jpg/png, 最大5MB</span>
                  </div>
                </el-col>
                <el-col :span="4" :xs="8">
                  <el-button text type="danger" @click="certList.splice(idx, 1)">删除</el-button>
                </el-col>
              </el-row>
            </div>

            <el-button v-if="certList.length < 3" type="primary" plain size="large" @click="addCert" style="margin-top:12px">
              + 添加证书
            </el-button>
          </div>

          <!-- 协议 -->
          <div class="agreement-box" :class="{ error: agreementError }">
            <el-checkbox v-model="form.agreed" @change="agreementError = false">
              我已阅读并同意
              <a href="/agreement/user" target="_blank">《用户服务协议》</a>
              和
              <a href="/agreement/privacy" target="_blank">《隐私政策》</a>
            </el-checkbox>
            <div v-if="agreementError" class="error-msg">请先阅读并同意服务协议</div>
          </div>

          <!-- 提交按钮 -->
          <div class="submit-box">
            <el-button type="primary" size="large" :loading="submitting" @click="handleSubmit" class="submit-btn">
              立即注册
            </el-button>
            <div class="login-tip">
              已有账号？<NuxtLink to="/login">立即登录</NuxtLink>
            </div>
          </div>

        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const cityStore = useCityStore()
const { districtNames } = useCityData()
const router = useRouter()
const { get, post } = useApi()

const config = useRuntimeConfig()
const formRef = ref(null)
const submitting = ref(false)
const agreementError = ref(false)
const universityOptions = ref([])
const certList = reactive([])
const certFileRefs = ref([])

const addCert = () => {
  certList.push({ certType: 3, certName: '', certUrl: '', uploading: false })
}

const handleCertFileUpload = async (e, idx) => {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) { ElMessage.warning('图片不能超过5MB'); return }
  certList[idx].uploading = true
  try {
    const fd = new FormData()
    fd.append('picFile', file)
    // 注册时无 token，用公开上传接口
    const res = await $fetch(`${config.public.apiBase}/system/auth/upload/pic`, {
      method: 'POST', body: fd,
      headers: localStorage.getItem('token') ? { token: localStorage.getItem('token') } : {}
    })
    if (res.code === 200 && res.data) {
      certList[idx].certUrl = res.data
      ElMessage.success('照片上传成功')
    } else { ElMessage.error(res.msg || '上传失败') }
  } catch (err) { ElMessage.error('上传失败，请重试') }
  finally { certList[idx].uploading = false; e.target.value = '' }
}

const subjectOptions = ['语文', '数学', '英语', '物理', '化学', '生物', '政治', '历史', '地理', '钢琴', '小提琴', '古筝', '吉他', '美术', '书法', '舞蹈', '编程', '日语', '法语', '德语', '韩语', '西班牙语', '学前教育', '小学全科']

const form = reactive({
  mobile: '',
  password: '',
  agreed: false,
  realName: '',
  gender: null,
  tutorType: null,
  degree: null,
  university: '',
  universityId: null,
  major: '',
  gradeYear: '',
  teachingMethod: 3,
  subjects: [],
  districts: [],
  expectedSalary: '',
  experience: null,
  introduction: ''
})

const rules = {
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  tutorType: [{ required: true, message: '请选择教员类型', trigger: 'change' }],
  degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
  universityId: [{ required: true, message: '请选择学校', trigger: 'change' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  subjects: [{ required: true, message: '请选择至少一个科目', trigger: 'change', type: 'array', min: 1 }],
  districts: [{ required: true, message: '请选择至少一个区域', trigger: 'change', type: 'array', min: 1 }]
}

const onUniversityChange = (id) => {
  const uni = universityOptions.value.find(u => u.id === id)
  form.university = uni ? uni.uniName : ''
}

onMounted(async () => {
  try {
    const res = await get('/user/api/dict/university/list', { cityId: cityStore.cityId })
    if (res.code === 200 && res.data) {
      universityOptions.value = res.data
    }
  } catch (e) {
    console.error('加载高校列表失败', e)
  }
})

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (!form.agreed) { agreementError.value = true; ElMessage.warning('请先阅读并同意服务协议'); return }

  submitting.value = true
  try {
    const res = await post('/user/api/users/register/simple', {
      mobile: form.mobile,
      password: form.password,
      code: '',
      userType: 1,
      realName: form.realName
    })
    if (res.code === 200) {
      // 注册成功后，如有上传证书则尝试保存（需要先用返回的 token 认证）
      if (certList.length > 0 && res.data?.token) {
        const token = res.data.token
        for (const cert of certList) {
          if (cert.certType && cert.certName) {
            try {
              await $fetch(`${config.public.apiBase}/user/auth/tutor-profile/cert/save`, {
                method: 'POST',
                body: { certType: cert.certType, certName: cert.certName, certUrl: cert.certUrl || '', certNo: '' },
                headers: { 'Content-Type': 'application/json', token }
              })
            } catch (e) { /* 证书保存失败不影响注册流程 */ }
          }
        }
      }
      ElMessage.success('注册成功！审核通过后即可接单。')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '注册失败，请稍后重试')
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.register-wrapper {
  height: 100vh;
  overflow: auto;
  background: var(--color-bg);
}

/* Banner */
.register-banner {
  background: var(--color-primary);
  color: white;
  padding: 40px var(--space-xl);
  text-align: center;
}

.register-banner h1 {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-semibold);
  margin: 0 0 var(--space-sm) 0;
}

.register-banner p {
  font-size: var(--font-size-md);
  opacity: 0.85;
  margin: 0;
}

/* Steps */
.steps-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-3xl) 0;
  gap: 0;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-sm);
}

.step-number {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--color-border);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-lg);
}

.step-item.active .step-number {
  background: var(--color-primary);
}

.step-text {
  font-size: var(--font-size-base);
  color: var(--color-text-muted);
}

.step-item.active .step-text {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.step-line {
  width: 80px;
  height: 2px;
  background: var(--color-border);
  margin: 0 var(--space-lg);
  margin-bottom: 22px;
}

/* Form */
.form-container {
  max-width: 900px;
  margin: 0 auto;
  padding-bottom: 60px;
}

.form-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl) var(--space-3xl);
  margin-bottom: var(--space-xl);
}

.section-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: var(--space-2xl);
  padding-bottom: var(--space-md);
  border-bottom: 1px solid var(--color-border-light);
}

/* Section optional hint */
.section-optional {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  font-weight: normal;
  margin-left: var(--space-sm);
}

.cert-tip {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin: 0 0 var(--space-xl) 0;
  line-height: 1.6;
}
.cert-tip strong {
  color: #2563eb;
}

.cert-item {
  padding: var(--space-md) 0;
  border-bottom: 1px solid var(--color-border-light);
}
.cert-item:last-child {
  border-bottom: none;
}

.cert-preview {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
}

.cert-file-hint {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}

/* Code input */
.code-input-group {
  display: flex;
  gap: 10px;
  width: 100%;
}
.code-input-group .el-input { flex: 1; }
.code-input-group .el-button { white-space: nowrap; min-width: 120px; }

/* Agreement */
.agreement-box {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-xl) var(--space-3xl);
  margin-bottom: var(--space-xl);
  transition: all var(--transition-normal);
}

.agreement-box.error {
  border-color: var(--color-error);
  background: #fef2f2;
}

.agreement-box a {
  color: var(--color-primary);
}

.agreement-box .error-msg {
  color: var(--color-error);
  font-size: var(--font-size-xs);
  margin-top: var(--space-sm);
  padding-left: 24px;
}

/* Submit */
.submit-box {
  text-align: center;
  padding: 10px 0 40px;
}

.submit-btn {
  width: 320px;
  height: 48px;
  font-size: var(--font-size-lg);
  border-radius: var(--radius-lg);
}

.login-tip {
  margin-top: var(--space-lg);
  font-size: var(--font-size-base);
  color: var(--color-text-muted);
}

.login-tip a {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

@media (max-width: 768px) {
  .register-banner {
    padding: var(--space-3xl) var(--space-lg);
  }
  .register-banner h1 {
    font-size: var(--font-size-2xl);
  }
  .form-container {
    padding: 0 var(--space-md) 40px;
  }
  .form-section {
    padding: var(--space-xl) var(--space-lg);
  }
  .agreement-box {
    padding: var(--space-lg);
  }
  .submit-btn {
    width: 100%;
  }
  .step-line {
    width: 40px;
  }
  .steps-wrapper {
    padding: var(--space-xl) 0;
  }
}
</style>
