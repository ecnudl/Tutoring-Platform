<template>
  <div class="detail-page-wrapper">
  <div class="container req-detail-page">
    <Head>
      <Title>A{{ displayNo }}需求详情 - 591家教网</Title>
      <Meta name="description" :content="`查看A${displayNo}学员需求的详细信息，科目、年级、预算、上课要求等。`" />
    </Head>

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/xy' }">学员需求</el-breadcrumb-item>
      <el-breadcrumb-item>A{{ displayNo }}详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div v-loading="loading">
      <el-card v-if="req" style="max-width:900px">
        <h1 class="req-title">{{ req.title || '家教需求' }}</h1>
        <div class="req-no">需求编号：A{{ displayNo }}</div>

        <el-descriptions :column="2" border style="margin-top:16px">
          <el-descriptions-item label="辅导科目">{{ req.subjectName || req.subjects || '-' }}</el-descriptions-item>
          <el-descriptions-item label="学员年级">{{ req.grade || '-' }}</el-descriptions-item>
          <el-descriptions-item label="所在区域">{{ req.districtName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{ req.address || '面议' }}</el-descriptions-item>
          <el-descriptions-item label="学员性别">{{ genderMap[req.studentGender] || '不限' }}</el-descriptions-item>
          <el-descriptions-item label="当前成绩">{{ req.currentLevel || '-' }}</el-descriptions-item>
          <el-descriptions-item label="期望教员类型">{{ tutorTypeMap[req.tutorTypePreference] || '不限' }}</el-descriptions-item>
          <el-descriptions-item label="期望教员性别">{{ genderMap[req.tutorGenderPreference] || '不限' }}</el-descriptions-item>
          <el-descriptions-item label="授课方式">{{ teachingMethodMap[req.teachingMethod] || '不限' }}</el-descriptions-item>
          <el-descriptions-item label="预算">
            <span class="price" v-if="req.budgetMin">{{ req.budgetMin }}-{{ req.budgetMax }}元/小时</span>
            <span v-else>面议</span>
          </el-descriptions-item>
          <el-descriptions-item label="辅导频率">{{ req.frequency || '-' }}</el-descriptions-item>
          <el-descriptions-item label="期望上课时间">{{ req.schedule || '-' }}</el-descriptions-item>
          <el-descriptions-item label="详细需求" :span="2">
            <div class="detail-text">{{ req.description || req.requirementDetail || '暂无详细描述' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="发布时间" :span="2">{{ req.gmtCreate || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div class="action-bar">
          <el-button type="primary" size="large" @click="handleApply">申请该需求</el-button>
          <el-button size="large" @click="$router.back()">返回列表</el-button>
        </div>
      </el-card>

      <div v-else-if="!loading" style="text-align:center;padding:60px;color:#999">
        <p>需求信息不存在或已关闭</p>
        <NuxtLink to="/xy"><el-button type="primary" style="margin-top:16px">返回需求列表</el-button></NuxtLink>
      </div>
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
const req = ref(null)
const loading = ref(true)

const genderMap = { 1: '男', 2: '女' }
const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const teachingMethodMap = { 1: '上门家教', 2: '在线辅导', 3: '均可' }

const loadReq = async () => {
  loading.value = true
  try {
    const res = await get('/user/api/requirement/view', { displayNo: 'A' + displayNo })
    if (res.code === 200 && res.data) {
      req.value = res.data
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleApply = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再申请')
    router.push('/login')
    return
  }
  if (!userStore.isTutor) {
    ElMessage.warning('仅教员可以申请需求')
    return
  }
  try {
    const res = await post('/user/auth/application/apply', {
      requirementId: requirement.value.id,
      message: '您好，我对这个需求很感兴趣，希望能为您提供教学服务'
    })
    if (res.code === 200) {
      ElMessage.success('申请已提交，请等待学员确认')
    } else {
      ElMessage.error(res.msg || '申请失败')
    }
  } catch (e) {
    ElMessage.error('申请失败，请稍后重试')
  }
}

onMounted(() => { loadReq() })
</script>

<style scoped>
.detail-page-wrapper {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 var(--space-4xl);
}
.req-detail-page {
  padding: 20px;
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.el-breadcrumb { margin-bottom: 20px; }
.req-title { font-size: 22px; margin-bottom: 4px; }
.req-no { font-size: 13px; color: #bbb; }
.price { color: #e6a23c; font-weight: 600; font-size: 16px; }
.detail-text { white-space: pre-wrap; line-height: 1.6; color: #666; }
.action-bar { margin-top: 24px; display: flex; gap: 12px; }

@media (max-width: 768px) {
  .req-detail-page { padding: 12px; }
  .el-descriptions { font-size: 13px; }
}
</style>
