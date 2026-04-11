<template>
  <div class="tutor-detail-wrapper">
    <div class="container tutor-detail-page">
    <el-card style="max-width:900px;margin:0 auto" v-loading="loading">
      <div v-if="tutor">
        <div class="tutor-header">
          <el-avatar :size="80" :src="tutor.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
          <div>
            <h1 style="margin-bottom:4px">{{ tutor.realName || '教员' }}</h1>
            <p style="color:#999">{{ tutor.university || '' }} {{ tutor.major ? '· ' + tutor.major : '' }}</p>
            <p style="color:#e6a23c;font-size:18px;font-weight:600;margin-top:4px" v-if="tutor.priceMin || tutor.priceMax">
              {{ tutor.priceMin || 0 }}-{{ tutor.priceMax || 0 }} 元/小时
            </p>
            <div style="margin-top:8px">
              <el-tag size="small" style="margin-right:4px">{{ tutorTypeMap[tutor.tutorType] || '教员' }}</el-tag>
              <el-tag size="small" type="info" v-if="tutor.degree">{{ degreeMap[tutor.degree] || '' }}</el-tag>
              <el-tag size="small" type="success" v-if="tutor.freeTrial === 1">可免费试讲</el-tag>
            </div>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="性别">{{ tutor.gender === 1 ? '男' : tutor.gender === 2 ? '女' : '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="教员类型">{{ tutorTypeMap[tutor.tutorType] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="学历">{{ degreeMap[tutor.degree] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="学校">{{ tutor.university || '-' }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ tutor.major || '-' }}</el-descriptions-item>
          <el-descriptions-item label="课时费">{{ tutor.priceMin || 0 }}-{{ tutor.priceMax || 0 }}元/小时</el-descriptions-item>
          <el-descriptions-item label="浏览次数">{{ tutor.viewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="成功次数">{{ tutor.successCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="自我介绍" :span="2">{{ tutor.selfIntroduction || '暂无介绍' }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top:24px;display:flex;gap:12px">
          <el-button type="primary" size="large" @click="handleReservation" v-if="userStore.isStudent">
            预约这位老师
          </el-button>
          <el-button type="warning" size="large" @click="handleShortlist" v-if="userStore.isLoggedIn">
            加入备选
          </el-button>
          <el-button size="large" @click="$router.back()">返回列表</el-button>
        </div>
      </div>
      <div v-else-if="!loading" style="text-align:center;padding:40px;color:#999">教员信息不存在</div>
    </el-card>

    <!-- 预约弹窗 -->
    <el-dialog v-model="reserveVisible" title="预约老师" width="500px">
      <el-form label-width="80px">
        <el-form-item label="预约时间">
          <el-input v-model="reserveForm.scheduleTime" placeholder="例如：周六下午2-5点" />
        </el-form-item>
        <el-form-item label="上课地点">
          <el-input v-model="reserveForm.address" placeholder="上课地点" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="reserveForm.remark" type="textarea" :rows="3" placeholder="简单描述您的辅导需求" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reserveVisible = false">取消</el-button>
        <el-button type="primary" :loading="reserving" @click="submitReservation">确认预约</el-button>
      </template>
    </el-dialog>
  </div>
  </div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'

const route = useRoute()
const { get, post } = useApi()
const userStore = useUserStore()

const tutorId = route.params.id
const tutor = ref(null)
const loading = ref(true)

const tutorTypeMap = { 1: '大学生', 2: '专职', 3: '在职教师', 4: '退休教师' }
const degreeMap = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }

// 预约
const reserveVisible = ref(false)
const reserveForm = ref({ scheduleTime: '', address: '', remark: '' })
const reserving = ref(false)

const loadTutor = async () => {
  loading.value = true
  try {
    const res = await get('/user/api/tutor/view', { id: tutorId })
    if (res.code === 200) {
      tutor.value = res.data
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleReservation = () => {
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录'); return }
  reserveForm.value = { scheduleTime: '', address: '', remark: '' }
  reserveVisible.value = true
}

const submitReservation = async () => {
  if (!reserveForm.value.scheduleTime) { ElMessage.warning('请填写预约时间'); return }
  reserving.value = true
  try {
    const res = await post('/user/auth/reservation/create', {
      tutorUserId: tutor.value.userId,
      scheduleTime: reserveForm.value.scheduleTime,
      address: reserveForm.value.address,
      remark: reserveForm.value.remark
    })
    if (res.code === 200) {
      ElMessage.success('预约成功，等待教员确认')
      reserveVisible.value = false
    } else {
      ElMessage.error(res.msg || '预约失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { reserving.value = false }
}

const handleShortlist = async () => {
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录'); return }
  try {
    const res = await post('/user/auth/shortlist/add', { tutorId: tutorId })
    if (res.code === 200) {
      ElMessage.success('已加入备选')
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { loadTutor() })
</script>
<style scoped>
.tutor-header { display: flex; gap: 20px; margin-bottom: 24px; }
@media (max-width: 768px) {
  .tutor-header { flex-direction: column; align-items: center; text-align: center; gap: 12px; }
  .el-descriptions { font-size: 13px; }
}
.tutor-detail-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.tutor-detail-page {
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-xl);
}
</style>
