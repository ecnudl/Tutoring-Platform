<template>
  <div class="register-wrapper">
    <Head>
      <Title>{{ cityStore.cityName }}教员注册 - 591家教网</Title>
      <Meta name="description" :content="`${cityStore.cityName}_注册成为家教老师。`" />
      <Meta name="keywords" :content="`${cityStore.cityName}教员注册,注册成为${cityStore.cityName}家教老师`" />
    </Head>

    <div class="reg-container">
      <!-- 顶部 -->
      <div class="reg-top">
        <div class="reg-step-title">
          <span v-if="currentStep > 1" class="back" @click="currentStep--">&lsaquo;</span>
          <span>教员注册 {{ currentStep }}/3</span>
        </div>
        <NuxtLink to="/" class="reg-logo">
          <SiteLogo :showText="true" />
        </NuxtLink>
      </div>

      <!-- 横向步骤指示 -->
      <div class="steps-bar">
        <div :class="['step-dot', currentStep >= 1 && 'active']">
          <div class="dot">1</div>
          <span>账号信息</span>
        </div>
        <div class="step-line" :class="{ active: currentStep >= 2 }"></div>
        <div :class="['step-dot', currentStep >= 2 && 'active']">
          <div class="dot">2</div>
          <span>完善简历</span>
        </div>
        <div class="step-line" :class="{ active: currentStep >= 3 }"></div>
        <div :class="['step-dot', currentStep >= 3 && 'active']">
          <div class="dot">3</div>
          <span>家教信息</span>
        </div>
      </div>

      <!-- ============== 第一步：账号 + 城市 ============== -->
      <div v-show="currentStep === 1" class="form-panel">
        <div class="reg-tip">
          <span class="tip-face">😊</span>
          请您 <b class="text-orange">认真</b>选择所在城市与手机号，这是后续推荐订单的重要依据。
        </div>

        <el-form :model="step1" :rules="step1Rules" ref="step1Ref" label-position="top" size="large">
          <el-form-item label="所在城市" prop="cityId" required>
            <el-select v-model="step1.cityId" placeholder="请选择所在城市" style="width:100%" @change="onCityChange">
              <el-option v-for="c in CITY_LIST" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
            <div class="hint-muted">当前访问：{{ cityStore.cityName }}站（切换城市将跳转到对应子站注册）</div>
          </el-form-item>

          <el-form-item label="手机号" prop="mobile" required>
            <el-input v-model="step1.mobile" placeholder="请输入11位手机号" maxlength="11" />
          </el-form-item>



          <el-form-item label="设置密码" prop="password" required>
            <el-input v-model="step1.password" type="password" placeholder="6-20 位，字母+数字" show-password />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword" required>
            <el-input v-model="step1.confirmPassword" type="password" placeholder="再次输入密码" show-password />
          </el-form-item>

          <el-form-item label="安全问题" prop="securityQuestion" required>
            <el-select v-model="step1.securityQuestion" placeholder="请选择一个问题（用于忘记密码后找回）" style="width:100%">
              <el-option label="您母亲的姓名是？" value="您母亲的姓名是？" />
              <el-option label="您父亲的姓名是？" value="您父亲的姓名是？" />
              <el-option label="您毕业的小学名字是？" value="您毕业的小学名字是？" />
              <el-option label="您出生的城市是？" value="您出生的城市是？" />
              <el-option label="您家中宠物的名字是？" value="您家中宠物的名字是？" />
              <el-option label="您最好朋友的姓名是？" value="您最好朋友的姓名是？" />
            </el-select>
          </el-form-item>

          <el-form-item label="安全答案" prop="securityAnswer" required>
            <el-input v-model="step1.securityAnswer" placeholder="不区分大小写，请记牢；忘记密码时需要回答" />
          </el-form-item>

          <el-form-item v-if="captchaRequired" label="图形验证码" prop="verCode">
            <div style="display:flex;gap:8px;width:100%">
              <el-input v-model="step1.verCode" placeholder="请输入右图字符" style="flex:1" />
              <img v-if="captchaImg" :src="captchaImg" alt="captcha" @click="loadCaptcha" style="height:40px;cursor:pointer;border-radius:6px" title="点击刷新" />
              <el-button v-else @click="loadCaptcha">获取验证码</el-button>
            </div>
          </el-form-item>
          <label aria-hidden="true" style="position:absolute;left:-9999px;width:1px;height:1px;opacity:0;pointer-events:none">
            如果您是真人请勿填写
            <input v-model="step1.honeypot" type="text" name="hp_zxq3" autocomplete="off" tabindex="-1" />
          </label>

          <div class="agreement-box" :class="{ error: agreementError }">
            <el-checkbox v-model="step1.agreed" @change="agreementError = false">
              我已阅读并同意
              <a href="/agreement/user" target="_blank">《用户服务协议》</a>
              <a href="/agreement/disclaimer" target="_blank">《免责声明》</a>
              <a href="/agreement/privacy" target="_blank">《隐私政策》</a>
            </el-checkbox>
          </div>

          <el-button type="primary" class="next-btn" @click="goStep2">下一步</el-button>

          <div class="bottom-tip">
            已有账号？<NuxtLink to="/login?type=teacher">教员登录</NuxtLink>
            <span class="sep">|</span>
            <NuxtLink to="/forgot-password">找回密码</NuxtLink>
          </div>
        </el-form>
      </div>

      <!-- ============== 第二步：详细简历 ============== -->
      <div v-show="currentStep === 2" class="form-panel">
        <div class="reg-tip">
          <span class="tip-face">😊</span>
          认真<b class="text-orange">详细</b>的简历，将<b class="text-orange">有助于</b>家长的选择，并获得我们<b class="text-orange">更多的推荐</b>。
        </div>

        <el-form :model="step2" :rules="step2Rules" ref="step2Ref" label-position="top" size="large">
          <el-form-item label="真实姓名" prop="realName" required>
            <el-input v-model="step2.realName" placeholder='前台只显示您的姓，如"王海"只显示为"王教员"。' />
          </el-form-item>

          <el-form-item label="目前身份" prop="tutorType" required>
            <el-select v-model="step2.tutorType" placeholder="选择身份" @change="onTutorTypeChange" style="width:100%">
              <el-option label="在校大学生/研究生,不含留学生" :value="1" />
              <el-option label="教师(在职/培训机构/专职家教/进修/离职)" :value="3" />
              <el-option label="外籍人士(留学生/外教)，海外归国" :value="4" />
              <el-option label="其他(已经毕业离校的人员)" :value="2" />
            </el-select>
          </el-form-item>

          <!-- 在校大学生 子字段 -->
          <template v-if="step2.tutorType === 1">
            <el-form-item label="详细身份" prop="identityDetail" required>
              <el-select v-model="step2.identityDetail" placeholder="请选择详细身份" style="width:100%">
                <el-option v-for="o in studentIdentityOptions" :key="o" :label="o" :value="o" />
              </el-select>
            </el-form-item>
            <el-form-item label="籍贯（省份）" prop="hometownProvince" required>
              <el-input v-model="step2.hometownProvince" placeholder="请输入籍贯" />
            </el-form-item>
          </template>

          <!-- 其他（已毕业）子字段 -->
          <template v-if="step2.tutorType === 2">
            <el-form-item label="目前职业" prop="identityDetail" required>
              <el-input v-model="step2.identityDetail" placeholder="目前职业，如：工程师、翻译、财务、自由职业者等" />
            </el-form-item>
            <el-form-item label="籍贯（省份）" prop="hometownProvince" required>
              <el-input v-model="step2.hometownProvince" placeholder="请输入籍贯" />
            </el-form-item>
          </template>

          <!-- 在职教师 子字段 -->
          <template v-if="step2.tutorType === 3">
            <el-form-item label="详细身份" prop="identityDetail" required>
              <el-select v-model="step2.identityDetail" placeholder="请选择详细身份" style="width:100%">
                <el-option-group v-for="g in teacherIdentityGroups" :key="g.label" :label="g.label">
                  <el-option v-for="o in g.options" :key="o" :label="o" :value="o" />
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item label="籍贯（省份）" prop="hometownProvince" required>
              <el-input v-model="step2.hometownProvince" placeholder="请输入籍贯" />
            </el-form-item>
          </template>

          <!-- 外籍/海归 子字段 -->
          <template v-if="step2.tutorType === 4">
            <el-form-item label="目前职业" prop="identityDetail" required>
              <el-input v-model="step2.identityDetail" placeholder="目前职业，如：工程师、翻译、财务、自由职业者等" />
            </el-form-item>
            <el-form-item label="国籍/留学国家或地区" prop="hometownProvince" required>
              <el-input v-model="step2.hometownProvince" placeholder="以护照为准" />
            </el-form-item>
          </template>

          <el-form-item label="性别" prop="gender" required>
            <el-radio-group v-model="step2.gender">
              <el-radio :label="1">男性</el-radio>
              <el-radio :label="2">女性</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="出生年月" prop="birthDate" required>
            <el-date-picker
              v-model="step2.birthDate"
              type="month"
              value-format="YYYY-MM"
              placeholder="选择年月"
              style="width:100%" />
          </el-form-item>

          <el-form-item label="专业" prop="major" required>
            <el-input v-model="step2.major" placeholder="例如：数学与应用数学" />
          </el-form-item>

          <el-form-item label="目前学历" prop="degree" required>
            <el-select v-model="step2.degree" placeholder="选择学历" style="width:100%">
              <el-option label="大专以下" :value="1" />
              <el-option label="大专" :value="2" />
              <el-option label="本科在读" :value="31" />
              <el-option label="本科毕业" :value="32" />
              <el-option label="硕士在读" :value="41" />
              <el-option label="硕士毕业" :value="42" />
              <el-option label="博士在读" :value="51" />
              <el-option label="博士毕业" :value="52" />
            </el-select>
          </el-form-item>

          <el-form-item label="身份证/护照号" prop="idCard" required>
            <el-input v-model="step2.idCard" placeholder="此项将严格保密，不对外公开" />
          </el-form-item>

          <!-- 大学生：高中母校 + 所在大学 -->
          <template v-if="step2.tutorType === 1">
            <el-form-item label="高中母校" prop="highSchool" required>
              <el-input v-model="step2.highSchool" placeholder="请填写全称" />
            </el-form-item>
            <el-form-item label="所在大学" prop="universityId" required>
              <el-input v-model="step2.universityId" placeholder="请输入所在高校全称, 例如: 上海大学" maxlength="100" />
            </el-form-item>
          </template>

          <!-- 其他 / 在职 / 外籍：大学 -->
          <template v-if="step2.tutorType === 2 || step2.tutorType === 3 || step2.tutorType === 4">
            <el-form-item label="所在大学/或毕业大学" prop="universityId" required>
              <el-input v-model="step2.universityId" placeholder="请输入大学全称, 例如: 复旦大学" maxlength="100" />
            </el-form-item>
          </template>

          <!-- 生活住址：当前城市 + 区 -->
          <el-form-item label="生活住址" prop="districtId" required>
            <div class="two-col">
              <el-input :model-value="cityStore.cityName" disabled style="flex:1" />
              <el-select v-model="step2.districtId" placeholder="选择所在区" style="flex:1">
                <el-option v-for="d in districtsRef" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </div>
          </el-form-item>

          <el-form-item label="e-mail" prop="email" required>
            <el-input v-model="step2.email" placeholder="用于接收订单推荐和系统通知" />
          </el-form-item>

          <el-form-item label="微信">
            <el-input v-model="step2.wechat" placeholder="微信号（选填，方便家长联系）" />
          </el-form-item>

          <el-form-item label="通信地址" prop="address" required>
            <el-input v-model="step2.address" type="textarea" :rows="3" placeholder="不对外公布，以方便地图定位，给您推荐家教。" />
          </el-form-item>

          <div class="btn-row">
            <el-button class="prev-btn" @click="currentStep = 1">上一步</el-button>
            <el-button type="primary" class="next-btn" @click="goStep3">下一步</el-button>
          </div>
        </el-form>
      </div>

      <!-- ============== 第三步：家教信息 ============== -->
      <div v-show="currentStep === 3" class="form-panel">
        <div class="reg-tip">
          <span class="tip-face">😊</span>
          家教信息是家长筛选的关键，请<b class="text-orange">尽可能详细</b>地填写您的授课范围与价格。
        </div>

        <el-form :model="step3" :rules="step3Rules" ref="step3Ref" label-position="top" size="large">
          <el-form-item label="可教授科目" prop="subjects" required>
            <el-select v-model="step3.subjects" multiple collapse-tags placeholder="请选择可教授科目（可多选）" style="width:100%">
              <el-option v-for="s in SUBJECT_NAMES" :key="s" :label="s" :value="s" />
            </el-select>
          </el-form-item>

          <el-form-item label="可授课区域" prop="districtList" required>
            <el-select v-model="step3.districtList" multiple collapse-tags placeholder="请选择可授课区域（可多选）" style="width:100%">
              <el-option v-for="d in districtsRef" :key="d.id" :label="d.name" :value="d.name" />
            </el-select>
          </el-form-item>

          <el-form-item label="授课方式" prop="teachingMethod" required>
            <el-select v-model="step3.teachingMethod" style="width:100%">
              <el-option label="教师上门" :value="1" />
              <el-option label="学生上门" :value="2" />
              <el-option label="在线辅导" :value="3" />
              <el-option label="均可" :value="4" />
            </el-select>
          </el-form-item>

          <el-form-item label="期望薪资（元/小时）">
            <div class="two-col">
              <el-input v-model.number="step3.priceMin" type="number" placeholder="最低" />
              <span class="dash">—</span>
              <el-input v-model.number="step3.priceMax" type="number" placeholder="最高" />
            </div>
          </el-form-item>

          <el-form-item label="家教经验">
            <el-select v-model="step3.gradeYear" placeholder="请选择" style="width:100%">
              <el-option label="无经验" value="无经验" />
              <el-option label="1 年以下" value="1 年以下" />
              <el-option label="1-3 年" value="1-3 年" />
              <el-option label="3-5 年" value="3-5 年" />
              <el-option label="5 年以上" value="5 年以上" />
            </el-select>
          </el-form-item>

          <el-form-item label="自我介绍" prop="selfIntroduction" required>
            <el-input v-model="step3.selfIntroduction" type="textarea" :rows="5" placeholder="请简要介绍您的教学经验、特长、获奖情况等（200字以内）" maxlength="400" show-word-limit />
          </el-form-item>

          <el-form-item label="资质证书说明（选填）">
            <el-input v-model="step3.certificatesDesc" type="textarea" :rows="2" placeholder="例：持有高中数学教师资格证、华东师大本科毕业证。审核通过后可在个人中心补充上传证书照片。" />
          </el-form-item>

          <div class="btn-row">
            <el-button class="prev-btn" @click="currentStep = 2">上一步</el-button>
            <el-button type="primary" class="next-btn" :loading="submitting" @click="handleSubmit">提交注册</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'
import { CITY_LIST, navigateToCity, PINYIN_TO_CITY } from '~/composables/useCityMap'
import { SUBJECT_NAMES } from '~/composables/subjectList'
import { useFingerprint } from '~/composables/useFingerprint'

const cityStore = useCityStore()
const { districts } = useCityData()
const router = useRouter()
const { post, get } = useApi()
const { get: getFingerprint } = useFingerprint()
const config = useRuntimeConfig()

const currentStep = ref(1)
const submitting = ref(false)
const agreementError = ref(false)

const step1Ref = ref(null)
const step2Ref = ref(null)
const step3Ref = ref(null)

const districtsRef = computed(() => districts.value)

// —— Step1: 账号信息 ——
const step1 = reactive({
  cityId: cityStore.cityId,
  mobile: '',
  password: '',
  confirmPassword: '',
  securityQuestion: '',
  securityAnswer: '',
  verToken: '',
  verCode: '',
  honeypot: '',
  agreed: false
})

const captchaImg = ref('')
const captchaRequired = ref(false)
const loadCaptcha = async () => {
  try {
    const r = await get('/system/api/common/code')
    if (r?.code === 200 && r.data) {
      step1.verToken = r.data.verToken
      captchaImg.value = r.data.img
      step1.verCode = ''
    }
  } catch (e) { ElMessage.error('图形码获取失败, 请刷新页面') }
}

const step1Rules = {
  cityId: [{ required: true, message: '请选择城市', trigger: 'change' }],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度 6-20 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: (_, v, cb) => v === step1.password ? cb() : cb(new Error('两次密码不一致')), trigger: 'blur' }
  ],
  securityQuestion: [{ required: true, message: '请选择安全问题', trigger: 'change' }],
  securityAnswer: [{ required: true, message: '请填写安全答案', trigger: 'blur' }]
  // verCode 不再硬性必填; 由 captchaRequired 控制
}

const onCityChange = (newId) => {
  if (newId === cityStore.cityId) return
  const target = CITY_LIST.find(c => c.id === newId)
  if (!target) return
  // 跳转到目标城市子站的注册页
  navigateToCity(target, '/register/teacher')
}

const goStep2 = async () => {
  if (!step1Ref.value) return
  const valid = await step1Ref.value.validate().catch(() => false)
  if (!valid) return
  if (!step1.agreed) { agreementError.value = true; ElMessage.warning('请先阅读并同意协议'); return }
  // 预留同步 realName 到 step2
  currentStep.value = 2
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// —— Step2: 简历 ——
const step2 = reactive({
  realName: '',
  tutorType: null,
  identityDetail: '',
  hometownProvince: '',
  gender: null,
  birthDate: '',
  major: '',
  degree: null,
  idCard: '',
  highSchool: '',
  universityId: '',
  districtId: null,
  email: '',
  wechat: '',
  address: ''
})

const studentIdentityOptions = [
  '自学考试生', '函授（网络）学生', '成教学生', '大专在读学生',
  '本科大一学生', '本科大二学生', '本科大三学生', '本科大四学生', '本科大五学生',
  '在读硕士生', '硕士进修老师', '在读博士生', '其他学生'
]

const teacherIdentityGroups = [
  { label: '专职家教', options: ['专职家教老师'] },
  { label: '机构教师', options: ['离职专业培训机构教师', '退休专业培训机构教师', '进修专业培训机构教师', '在职专业培训机构教师'] },
  { label: '幼儿教师', options: ['离职幼儿教师', '退休幼儿教师', '进修幼儿教师', '在职幼儿教师'] },
  { label: '小学教师', options: ['离职小学教师', '退休小学教师', '进修小学教师', '在职小学教师'] },
  { label: '初中教师', options: ['离职初中教师', '退休初中教师', '进修初中教师', '在职初中教师'] },
  { label: '高中教师', options: ['离职高中教师', '退休高中教师', '进修高中教师', '在职高中教师'] },
  { label: '大学教师', options: ['硕士进修老师', '离职大学教师', '退休大学教师', '在职大学教师'] },
  { label: '其他教师', options: ['其它教师'] }
]

const onTutorTypeChange = () => {
  step2.identityDetail = ''
  step2.hometownProvince = ''
}

const step2Rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 12, message: '长度 2-12 个字符', trigger: 'blur' }
  ],
  tutorType: [{ required: true, message: '请选择身份', trigger: 'change' }],
  identityDetail: [{ required: true, message: '请填写详细身份', trigger: 'blur' }],
  hometownProvince: [{ required: true, message: '请填写籍贯/国籍', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  birthDate: [{ required: true, message: '请选择出生年月', trigger: 'change' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
  idCard: [{ required: true, message: '请输入证件号码', trigger: 'blur' }],
  highSchool: [{ required: true, message: '请填写高中母校', trigger: 'blur' }],
  universityId: [{ required: true, message: '请输入所在大学/毕业大学', trigger: 'blur' }],
  districtId: [{ required: true, message: '请选择所在区', trigger: 'change' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  address: [{ required: true, message: '请输入通信地址', trigger: 'blur' }]
}

const goStep3 = async () => {
  if (!step2Ref.value) return
  const valid = await step2Ref.value.validate().catch(() => false)
  if (!valid) {
    setTimeout(() => {
      const err = document.querySelector('.is-error')
      if (err) err.scrollIntoView({ behavior: 'smooth', block: 'center' })
    }, 200)
    return
  }
  currentStep.value = 3
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// —— Step3: 家教信息 ——
const step3 = reactive({
  subjects: [],
  districtList: [],
  teachingMethod: 4,
  priceMin: null,
  priceMax: null,
  gradeYear: '',
  selfIntroduction: '',
  certificatesDesc: ''
})

const step3Rules = {
  subjects: [{ type: 'array', required: true, min: 1, message: '请至少选择一个科目', trigger: 'change' }],
  districtList: [{ type: 'array', required: true, min: 1, message: '请至少选择一个区域', trigger: 'change' }],
  teachingMethod: [{ required: true, message: '请选择授课方式', trigger: 'change' }],
  selfIntroduction: [
    { required: true, message: '请填写自我介绍', trigger: 'blur' },
    { min: 20, message: '自我介绍不少于 20 字', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!step3Ref.value) return
  const valid = await step3Ref.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const visitorId = await getFingerprint()
    // 1. 注册账号
    const regRes = await post('/user/api/users/register/simple', {
      mobile: step1.mobile,
      password: step1.password,
      userType: 1,
      realName: step2.realName,
      securityQuestion: step1.securityQuestion,
      securityAnswer: step1.securityAnswer,
      verToken: step1.verToken,
      verCode: step1.verCode,
      honeypot: step1.honeypot,
      visitorId
    })
    if (regRes.code !== 200) {
      submitting.value = false
      if (regRes.msg === 'CAPTCHA_REQUIRED') {
        // 触发智能 captcha — 切回 step1, 弹出验证码
        captchaRequired.value = true
        currentStep.value = 1
        await loadCaptcha()
        ElMessage.warning('请完成图形验证码后重试')
        return
      }
      ElMessage.error(regRes.msg || '注册失败')
      currentStep.value = 1
      if (captchaRequired.value) await loadCaptcha()
      return
    }

    const token = regRes.data?.token
    if (!token) {
      ElMessage.error('注册成功但未拿到 token，请登录后手动完善简历')
      submitting.value = false
      return
    }
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('token', token)
    }

    // 2. 调用 tutor-profile/save 补全详细资料
    const profilePayload = {
      realName: step2.realName,
      gender: step2.gender,
      birthDate: step2.birthDate ? step2.birthDate + '-01' : null,
      idCard: step2.idCard,
      tutorType: step2.tutorType,
      identityDetail: step2.identityDetail,
      degree: mapDegreeForBackend(step2.degree),
      university: step2.universityId,
      major: step2.major,
      highSchool: step2.highSchool,
      hometownProvince: step2.hometownProvince,
      email: step2.email,
      wechat: step2.wechat,
      selfIntroduction: step3.selfIntroduction,
      certificatesDesc: step3.certificatesDesc,
      teachingMethod: step3.teachingMethod,
      subjects: step3.subjects.join(','),
      gradeYear: step3.gradeYear,
      cityId: cityStore.cityId,
      districtId: step2.districtId,
      address: step2.address,
      priceMin: step3.priceMin || 0,
      priceMax: step3.priceMax || 0,
      tags: step3.districtList.join(','),  // 可授课区域暂存 tags
      freeTrial: 0
    }

    await $fetch(`${config.public.apiBase}/user/auth/tutor-profile/save`, {
      method: 'POST',
      body: profilePayload,
      headers: { 'Content-Type': 'application/json', token }
    })

    // 3. 自动提交审核，让简历进入 admin"待审核"列表
    let auditOk = true
    let auditMsg = ''
    try {
      const auditRes = await $fetch(`${config.public.apiBase}/user/auth/tutor-profile/submit-audit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', token }
      })
      if (auditRes && auditRes.code !== 200) {
        auditOk = false
        auditMsg = auditRes.msg || '审核提交未成功'
      }
    } catch (e) {
      auditOk = false
      auditMsg = '审核提交未成功，可在个人中心手动提交'
      console.warn('自动提交审核失败', e)
    }

    if (auditOk) {
      ElMessage.success('注册成功！资料已提交审核，通过后即可接单。')
    } else {
      ElMessage.warning(`注册成功，但${auditMsg}，请登录后在个人中心手动提交审核。`)
    }
    // 退出登录状态，引导用户重新登录
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token')
    }
    router.push('/login?type=teacher')
  } catch (e) {
    console.error(e)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}

/**
 * 前端学历选项到后端 degree tinyint 的映射
 * 1=大专以下, 2=大专, 3=本科, 4=硕士, 5=博士（后端口径）
 */
function mapDegreeForBackend(val) {
  if (val == null) return null
  if (val === 1 || val === 2) return val
  if (val === 31 || val === 32) return 3
  if (val === 41 || val === 42) return 4
  if (val === 51 || val === 52) return 5
  return val
}

onMounted(() => {
  step1.cityId = cityStore.cityId
  // 不再 onMount 加载 captcha; 触发智能升级时再加载
})
</script>

<style scoped>
.register-wrapper {
  background: var(--color-bg);
  min-height: 100vh;
  padding: 20px 0 60px;
}
.reg-container {
  max-width: 680px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 顶部 */
.reg-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 4px 18px;
}
.reg-step-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 700;
  color: #111827;
}
.reg-step-title .back {
  cursor: pointer;
  font-size: 24px;
  line-height: 1;
  color: #94a3b8;
  padding: 4px 8px;
}
.reg-step-title .back:hover { color: var(--color-primary); }

.reg-logo { line-height: 0; }

/* 步骤条 */
.steps-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 14px 0 26px;
  gap: 4px;
}
.step-dot {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  color: #94a3b8;
  font-size: 13px;
}
.step-dot .dot {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #e2e8f0;
  color: #fff;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: background 0.2s;
}
.step-dot.active { color: var(--color-primary, #163B6B); }
.step-dot.active .dot { background: var(--color-primary, #163B6B); }
.step-line {
  width: 56px;
  height: 2px;
  background: #e2e8f0;
  margin: 0 4px 22px;
  transition: background 0.2s;
}
.step-line.active { background: var(--color-primary, #163B6B); }

/* 表单面板 */
.form-panel {
  background: #fff;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  padding: 22px 24px 30px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.03);
}
.reg-tip {
  background: #f8fafc;
  padding: 14px 18px;
  border-radius: 8px;
  font-size: 14px;
  color: #475569;
  line-height: 1.7;
  margin-bottom: 20px;
}
.tip-face { margin-right: 6px; }
.text-orange { color: #f97316; font-weight: 600; }

:deep(.el-form-item__label) {
  font-size: 15px;
  font-weight: 700;
  color: #f97316;
  padding-bottom: 4px;
}
:deep(.el-form-item) { margin-bottom: 16px; }

.hint-muted {
  font-size: 12px;
  color: #94a3b8;
  margin-top: 4px;
}

.code-row {
  display: flex;
  gap: 10px;
  width: 100%;
}
.code-row .el-button { min-width: 120px; }

.two-col {
  display: flex;
  gap: 10px;
  align-items: center;
  width: 100%;
}
.two-col .el-input { flex: 1; }
.two-col .dash { color: #94a3b8; font-size: 14px; }
.two-col :deep(.el-form-item) { margin-bottom: 0; }

.agreement-box {
  padding: 10px 14px;
  background: #f8fafc;
  border-radius: 6px;
  margin: 6px 0 18px;
  font-size: 13px;
}
.agreement-box.error { background: #fef2f2; border: 1px solid #dc2626; }
.agreement-box a { color: var(--color-primary); margin: 0 2px; }

.next-btn {
  width: 100%;
  height: 48px;
  font-size: 17px;
  font-weight: 600;
  background: #f97316;
  border-color: #f97316;
  color: #fff;
}
.next-btn:hover, .next-btn:focus { background: #ea580c; border-color: #ea580c; color: #fff; }

.prev-btn {
  height: 48px;
  font-size: 15px;
  padding: 0 28px;
}

.btn-row {
  display: flex;
  gap: 12px;
  margin-top: 10px;
}
.btn-row .prev-btn { flex: 0 0 auto; }
.btn-row .next-btn { flex: 1; }

.bottom-tip {
  text-align: center;
  margin-top: 16px;
  font-size: 13px;
  color: #94a3b8;
}
.bottom-tip a { color: var(--color-primary); margin: 0 4px; }
.bottom-tip .sep { margin: 0 4px; color: #cbd5e1; }

@media (max-width: 640px) {
  .reg-container { padding: 0 12px; }
  .form-panel { padding: 16px 14px 22px; }
  .step-line { width: 30px; }
  .step-dot span { font-size: 12px; }
}
</style>
