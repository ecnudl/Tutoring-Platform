<template>
  <div class="qjj-page-wrapper">
    <div class="container qjj-page">
    <Head>
      <Title>{{ cityStore.cityName }}请家教 - 591家教网</Title>
      <Meta name="description" :content="`在${cityStore.cityName}快速提交您的家教需求，我们将在24小时内为您推荐合适的教员。`" />
    </Head>


    <h1 class="page-title">请家教 - 快速提交您的需求</h1>
    <p class="page-sub">{{ subtitle }}</p>

    <div class="methods-grid">
      <el-card shadow="never" class="method-card">
        <div class="method-num">方式一</div>
        <h2>拨打服务热线</h2>
        <p class="method-desc">直接拨打我们的服务热线，专业顾问为您推荐教员</p>
        <div class="phone-number">{{ hotline }}</div>
        <p class="phone-time">工作时间：{{ workTime }}</p>
      </el-card>

      <el-card shadow="never" class="method-card method-form-card">
        <div class="method-num">方式二</div>
        <h2>在线提交需求</h2>
        <p class="method-desc">填写简单信息，我们将在24小时内联系您</p>
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top" style="margin-top:16px">
          <el-form-item label="联系人姓名" prop="contactName">
            <el-input v-model="form.contactName" placeholder="请输入您的姓名" />
          </el-form-item>
          <el-form-item label="联系电话" prop="contactMobile">
            <el-input v-model="form.contactMobile" placeholder="请输入手机号码" />
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="form.contactWechat">手机号同微信号</el-checkbox>
          </el-form-item>
          <el-form-item label="学生情况" prop="studentInfo">
            <el-input v-model="form.studentInfo" type="textarea" :rows="4"
              placeholder="请填写学生的程度、目前学习情况，需要辅导的科目和年级，以及希望的上课频率和时间。例如：高一学生，物理薄弱常 50 分上下，希望补到 75 分以上，每周六下午 2 小时。" />
          </el-form-item>
          <el-form-item label="教员要求" prop="tutorRequest">
            <el-input v-model="form.tutorRequest" type="textarea" :rows="3"
              placeholder="请填写您对教员的具体要求。例如：教员性别（女老师优先 / 不限）、教员居住地区或便于上课的区域、是否有教学经验要求、学历背景、其他偏好等。" />
          </el-form-item>
          <el-form-item label="交通信息" prop="trafficInfo">
            <el-input v-model="form.trafficInfo" type="textarea" :rows="3"
              placeholder="如果是线上授课请填写「线上授课，无需上门」；如果需要老师上门，请写明具体小区/弄号或最近地铁站，以及大致路口（例如：徐汇区天钥桥路 100 弄，地铁 1 号线徐家汇站附近，xx 路与 yy 路交界）。" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" style="width:100%" :loading="submitting" @click="handleSubmit">提交需求</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card shadow="never" class="method-card">
        <div class="method-num">方式三</div>
        <h2>自助浏览教员</h2>
        <p class="method-desc">直接浏览教员库，按条件筛选合适的教员</p>
        <div class="browse-links">
          <NuxtLink to="/jy?tutorType=1"><el-button style="width:100%;margin-bottom:8px">大学生教员</el-button></NuxtLink>
          <NuxtLink to="/jy?tutorType=2"><el-button style="width:100%;margin-bottom:8px">专职教员</el-button></NuxtLink>
          <NuxtLink to="/jy?tutorType=3"><el-button style="width:100%;margin-bottom:8px">在职教师</el-button></NuxtLink>
          <NuxtLink to="/jy?tutorType=4"><el-button style="width:100%;margin-bottom:8px">海归外教</el-button></NuxtLink>
          <NuxtLink to="/jy"><el-button type="primary" style="width:100%">浏览全部教员</el-button></NuxtLink>
        </div>
      </el-card>

      <el-card shadow="never" class="method-card">
        <div class="method-num">方式四</div>
        <h2>微信咨询</h2>
        <p class="method-desc">扫描二维码添加客服微信，一对一咨询</p>
        <div class="wechat-qr">
          <div class="qr-placeholder">
            <img src="/wechat-qr.png" alt="微信二维码" class="qr-image" />
          </div>
          <p style="margin-top:8px;font-size:13px;color:var(--color-text-muted)">扫码添加客服微信</p>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="successVisible" title="提交成功" width="400px" :close-on-click-modal="false">
      <div style="text-align:center;padding:20px">
        <el-icon :size="48" color="#2E7D32"><CircleCheck /></el-icon>
        <h3 style="margin-top:12px">需求提交成功！</h3>
        <p style="color:var(--color-text-secondary);margin-top:8px">{{ successTip }}</p>
      </div>
      <template #footer>
        <el-button type="primary" @click="successVisible = false; $router.push('/')">返回首页</el-button>
        <el-button @click="successVisible = false">继续浏览</el-button>
      </template>
    </el-dialog>
  </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { CircleCheck } from '@element-plus/icons-vue'
import { useCityStore } from '~/stores/city'
import { useUserStore } from '~/stores/user'
import { useSiteConfig } from '~/composables/useSiteConfig'

const cityStore = useCityStore()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const { post } = useApi()
const { config, load } = useSiteConfig()

const hotline = computed(() => config.value.siteHotline || '13795420591')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00-21:00')
const subtitle = computed(() => config.value.qjjSubtitle || '我们提供多种方式帮您找到满意的家教老师')
const successTip = computed(() => config.value.qjjSuccessTip || '我们将在24小时内联系您，请保持电话畅通。')

onMounted(() => { load() })

const formRef = ref(null)
const submitting = ref(false)
const successVisible = ref(false)

const form = reactive({
  contactName: '',
  contactMobile: '',
  contactWechat: false,
  studentInfo: '',
  tutorRequest: '',
  trafficInfo: ''
})

const rules = {
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  contactMobile: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  studentInfo:  [{ required: true, message: '请填写学生情况', trigger: 'blur' }],
  tutorRequest: [{ required: true, message: '请填写教员要求', trigger: 'blur' }],
  trafficInfo:  [{ required: true, message: '请填写交通信息 (线上授课请填「线上授课，无需上门」)', trigger: 'blur' }]
}

const handleSubmit = async () => {
  // 必须登录
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再提交需求')
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath))
    return
  }
  // 必须是学员账号 (家长)
  if (!userStore.isStudent) {
    ElMessage.warning('仅家长账号可提交需求 (当前账号为教员或未设置类型)')
    return
  }
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const res = await post('/user/auth/requirement/quick-submit', {
      contactName: form.contactName,
      contactMobile: form.contactMobile,
      contactWechat: form.contactWechat ? form.contactMobile : '',
      studentInfo: form.studentInfo,
      tutorRequest: form.tutorRequest,
      trafficInfo: form.trafficInfo,
      cityId: cityStore.cityId
    })
    if (res.code === 200) {
      successVisible.value = true
      form.contactName = ''
      form.contactMobile = ''
      form.contactWechat = false
      form.studentInfo = ''
      form.tutorRequest = ''
      form.trafficInfo = ''
    } else {
      ElMessage.error(res.msg || '提交失败，请稍后重试')
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.qjj-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.qjj-page {
  padding: var(--space-xl);
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.page-sub { color: var(--color-text-muted); margin-bottom: var(--space-2xl); margin-top: calc(-1 * var(--space-md)); }

.methods-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: var(--space-xl); }

.method-card { text-align: center; }
.method-card :deep(.el-card__body) { border: 1px solid var(--color-border); border-radius: var(--radius-lg); }
.method-form-card { text-align: left; }

.method-num {
  display: inline-block;
  background: var(--color-primary);
  color: #fff;
  padding: 2px 12px;
  border-radius: 12px;
  font-size: var(--font-size-sm);
  margin-bottom: var(--space-md);
}

.method-card h2 { font-size: var(--font-size-xl); margin-bottom: var(--space-sm); color: var(--color-text); }
.method-desc { color: var(--color-text-muted); font-size: var(--font-size-base); margin-bottom: var(--space-lg); }

.phone-number { font-size: 28px; font-weight: var(--font-weight-bold); color: var(--color-accent-dark); margin: var(--space-lg) 0 var(--space-sm); }
.phone-time { font-size: var(--font-size-sm); color: var(--color-text-muted); }

.wechat-qr { display: flex; flex-direction: column; align-items: center; margin-top: var(--space-lg); }
.qr-placeholder {
  width: 160px;
  height: 160px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  background: var(--color-bg);
  overflow: hidden;
  padding: 6px;
}
.qr-image { width: 100%; height: 100%; object-fit: contain; display: block; }

.browse-links { margin-top: var(--space-lg); }

@media (max-width: 768px) {
  .qjj-page { padding: var(--space-md); }
  .methods-grid { grid-template-columns: 1fr; }
}
</style>
