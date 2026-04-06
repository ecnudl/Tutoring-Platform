<template>
  <div class="container tutor-detail-page">
    <Head>
      <Title>T{{ displayNo }}教员详情 - 51家教网</Title>
      <Meta name="description" :content="`查看T${displayNo}教员的详细信息，包括学历、院校、教学经验、授课科目等。`" />
    </Head>

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/jy' }">教员库</el-breadcrumb-item>
      <el-breadcrumb-item>T{{ displayNo }}详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div v-loading="loading">
      <div v-if="tutor" class="detail-layout">
        <!-- 左侧 -->
        <div class="detail-left">
          <el-avatar :size="120" :src="tutor.avatar || '/placeholder/avatar.png'" />
          <div class="badges">
            <el-tag v-if="tutor.verified" type="success" size="small">已认证</el-tag>
            <el-tag v-if="tutor.idVerified" type="success" size="small">身份已验</el-tag>
            <el-tag v-if="tutor.degreeVerified" type="success" size="small">学历已验</el-tag>
          </div>
          <div class="display-no">编号：T{{ displayNo }}</div>
        </div>

        <!-- 右侧 -->
        <div class="detail-right">
          <h1 class="tutor-name-title">{{ tutor.surname || (tutor.realName ? tutor.realName.charAt(0) : '') }}老师</h1>
          <el-descriptions :column="2" border style="margin-bottom:20px">
            <el-descriptions-item label="编号">T{{ displayNo }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ tutor.surname || (tutor.realName ? tutor.realName.charAt(0) + '老师' : '-') }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ genderMap[tutor.gender] || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="学历">{{ degreeMap[tutor.degree] || '-' }}</el-descriptions-item>
            <el-descriptions-item label="院校">{{ tutor.university || '-' }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ tutor.major || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所在区域" :span="2">{{ tutor.districtName || '-' }}</el-descriptions-item>
          </el-descriptions>

          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="basic">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="教员类型">{{ tutorTypeMap[tutor.tutorType] || '-' }}</el-descriptions-item>
                <el-descriptions-item label="年级/年份">{{ tutor.gradeYear || '-' }}</el-descriptions-item>
                <el-descriptions-item label="籍贯">{{ tutor.hometown || '-' }}</el-descriptions-item>
                <el-descriptions-item label="民族">{{ tutor.ethnicity || '-' }}</el-descriptions-item>
                <el-descriptions-item label="注册时间">{{ tutor.gmtCreate || '-' }}</el-descriptions-item>
                <el-descriptions-item label="最近登录">{{ tutor.lastLoginTime || '-' }}</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>

            <el-tab-pane label="家教信息" name="teaching">
              <div class="info-section">
                <h3>自我介绍</h3>
                <p class="info-text">{{ tutor.selfIntroduction || '暂无' }}</p>
              </div>
              <div class="info-section">
                <h3>证书资质</h3>
                <p class="info-text">{{ tutor.certificates || '暂无' }}</p>
              </div>
              <div class="info-section">
                <h3>可教授科目</h3>
                <div v-if="tutor.subjects">
                  <el-tag v-for="sub in tutor.subjects.split(',')" :key="sub" style="margin:2px 4px">{{ sub }}</el-tag>
                </div>
                <p v-else class="info-text">暂无</p>
              </div>
              <div class="info-section">
                <h3>可授课区域</h3>
                <p class="info-text">{{ tutor.teachingAreas || '暂无' }}</p>
              </div>
              <div class="info-section">
                <h3>授课方式</h3>
                <p class="info-text">{{ teachingMethodMap[tutor.teachingMethod] || '暂无' }}</p>
              </div>
              <div class="info-section">
                <h3>教学经验</h3>
                <p class="info-text">{{ tutor.teachingExperience || '暂无' }}</p>
              </div>
              <div class="info-section">
                <h3>课时费</h3>
                <p class="info-text price">{{ tutor.priceMin || 0 }}-{{ tutor.priceMax || 0 }} 元/小时</p>
              </div>
            </el-tab-pane>

            <el-tab-pane label="成功记录" name="records">
              <div v-if="tutor.successRecords && tutor.successRecords.length">
                <div v-for="(r, i) in tutor.successRecords" :key="i" class="record-item">
                  <div class="record-title">{{ r.subject }} - {{ r.grade }}</div>
                  <div class="record-meta">{{ r.district }} | {{ r.time }}</div>
                </div>
              </div>
              <div v-else style="padding:24px;text-align:center;color:#999">暂无成功记录</div>
            </el-tab-pane>
          </el-tabs>

          <div class="action-bar">
            <el-button type="primary" size="large" @click="handleBooking">预约该教员</el-button>
            <el-button size="large" @click="$router.back()">返回列表</el-button>
          </div>
        </div>
      </div>

      <div v-else-if="!loading" style="text-align:center;padding:60px;color:#999">
        <p>教员信息不存在或已下架</p>
        <NuxtLink to="/jy"><el-button type="primary" style="margin-top:16px">返回教员库</el-button></NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useCityStore } from '~/stores/city'
import { useUserStore } from '~/stores/user'

const cityStore = useCityStore()
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { get } = useApi()

const displayNo = route.params.id
const tutor = ref(null)
const loading = ref(true)
const activeTab = ref('basic')

const genderMap = { 1: '男', 2: '女' }
const degreeMap = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }
const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const teachingMethodMap = { 1: '上门家教', 2: '在线辅导', 3: '均可' }

const loadTutor = async () => {
  loading.value = true
  try {
    const res = await get('/user/api/tutor/view', { displayNo: 'T' + displayNo })
    if (res.code === 200 && res.data) {
      tutor.value = res.data
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleBooking = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再预约')
    router.push('/login')
    return
  }
  try {
    const res = await post('/user/auth/reservation/create', {
      tutorId: tutor.value.id,
      message: '我对您的教学经历很感兴趣，希望能预约试讲'
    })
    if (res.code === 200) {
      ElMessage.success('预约请求已发送，工作人员将尽快与您联系')
    } else {
      ElMessage.error(res.msg || '预约失败')
    }
  } catch (e) {
    ElMessage.error('预约失败，请稍后重试')
  }
}

onMounted(() => { loadTutor() })
</script>

<style scoped>
.tutor-detail-page { padding: 20px; }
.el-breadcrumb { margin-bottom: 20px; }

.detail-layout { display: flex; gap: 24px; }
.detail-left { width: 160px; flex-shrink: 0; text-align: center; }
.badges { margin-top: 12px; display: flex; flex-direction: column; gap: 4px; align-items: center; }
.display-no { margin-top: 8px; font-size: 13px; color: #999; }

.detail-right { flex: 1; background: #fff; border-radius: 8px; padding: 24px; }
.tutor-name-title { font-size: 22px; margin-bottom: 16px; }

.info-section { margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px solid #f5f5f5; }
.info-section:last-child { border-bottom: none; }
.info-section h3 { font-size: 14px; color: #333; margin-bottom: 8px; }
.info-text { font-size: 14px; color: #666; line-height: 1.6; white-space: pre-wrap; }
.info-text.price { color: #e6a23c; font-size: 18px; font-weight: 600; }

.record-item { padding: 12px; border-bottom: 1px solid #f5f5f5; }
.record-title { font-weight: 500; }
.record-meta { font-size: 13px; color: #999; margin-top: 4px; }

.action-bar { margin-top: 24px; display: flex; gap: 12px; }

@media (max-width: 768px) {
  .tutor-detail-page { padding: 12px; }
  .detail-layout { flex-direction: column; }
  .detail-left { width: 100%; margin-bottom: 16px; }
  .detail-right { padding: 16px; }
  .el-descriptions { font-size: 13px; }
}
</style>
