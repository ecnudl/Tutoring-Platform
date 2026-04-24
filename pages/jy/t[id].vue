<template>
  <div class="detail-page-wrapper">
  <div class="container tutor-detail-page">
    <Head>
      <Title>T{{ displayNo }}教员详情 - 591家教网</Title>
      <Meta name="description" :content="`查看T${displayNo}教员的详细信息，包括学历、院校、教学经验、授课科目等。`" />
    </Head>

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/jy' }">教员库</el-breadcrumb-item>
      <el-breadcrumb-item>T{{ displayNo }}详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div v-loading="loading">
      <div v-if="tutor">
        <!-- ========= 顶部 Hero 卡（参考 ttgood 风格） ========= -->
        <div class="tutor-hero-card">
          <div class="hero-avatar-wrap">
            <img :src="tutor.avatar || '/placeholder/avatar.png'" alt="头像" class="hero-avatar-img" />
          </div>
          <div class="hero-identity">
            <div class="hero-no-row">
              <span class="hero-no-label">编号：</span>
              <span class="hero-no-value">T{{ displayNo }}</span>
            </div>
            <div class="hero-name-row">
              {{ getDisplayName(tutor) }}
              <span v-if="tutor.gender === 1 || tutor.gender === 2" class="hero-gender">[{{ genderMap[tutor.gender] }}]</span>
            </div>
            <div v-if="tutor.isStar === 1" class="hero-star-tag">
              <svg viewBox="0 0 24 24" width="14" height="14" fill="#f59e0b" xmlns="http://www.w3.org/2000/svg" style="margin-right:4px;vertical-align:-2px">
                <path d="M12 2l2.9 6.9L22 10l-5.5 4.8L18 22l-6-3.5L6 22l1.5-7.2L2 10l7.1-1.1L12 2z"/>
              </svg>
              明星教员
            </div>
          </div>
          <div class="hero-stats">
            <div class="stat-row">
              <span class="stat-label">浏览次数：</span>
              <span class="stat-value">{{ tutor.viewCount || 0 }}次</span>
              <span v-if="tutor.teachingMethod === 3 || tutor.teachingMethod === 4" class="online-chip">
                <svg viewBox="0 0 24 24" width="12" height="12" fill="none" stroke="#f59e0b" stroke-width="2" xmlns="http://www.w3.org/2000/svg" style="margin-right:3px;vertical-align:-1px">
                  <circle cx="12" cy="10" r="3"/><path d="M12 21s-7-7-7-11a7 7 0 1114 0c0 4-7 11-7 11z"/>
                </svg>
                可网络授课
              </span>
            </div>
            <div class="stat-row">
              <span class="stat-label">登录次数：</span>
              <span class="stat-value">{{ tutor.loginCount || 0 }}次</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">最近登录：</span>
              <span class="stat-value">{{ formatLastLogin(tutor.lastLoginTime) }}</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">认证情况：</span>
              <span v-if="tutor.isVerified === 1" class="cert-chip-inline">证件已认证</span>
              <span v-else class="cert-chip-none">未认证</span>
              <span v-if="tutor.isStar === 1" class="cert-chip-star">★</span>
            </div>
          </div>
        </div>

        <!-- ========= 正文详情 ========= -->
        <div class="detail-body">
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
const teachingMethodMap = { 1: '教师上门', 2: '学生上门', 3: '在线辅导', 4: '均可' }

function getDisplayName(t) {
  const name = (t.realName || '').trim()
  if (!name) return '教员'
  const surname = name.charAt(0)
  const suffix = t.tutorType === 1 ? '教员' : '老师'
  return surname + suffix
}
function formatLastLogin(dt) {
  if (!dt) return '—'
  const s = String(dt)
  // 常见返回：2026-04-24 10:23:00
  const m = s.match(/^(\d{4})-(\d{2})-(\d{2})/)
  return m ? `${m[2]}-${m[3]}` : s
}

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
.detail-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.tutor-detail-page {
  padding: 20px;
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.el-breadcrumb { margin-bottom: 20px; }

/* ========== Hero Card ========== */
.tutor-hero-card {
  display: grid;
  grid-template-columns: 160px 1fr 1.1fr;
  gap: 28px;
  align-items: center;
  padding: 24px 28px;
  background: #fff;
  border-radius: 10px;
  border: 1px solid #f0e6d6;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  margin-bottom: 20px;
}
.hero-avatar-wrap {
  width: 140px;
  height: 140px;
  border-radius: 6px;
  overflow: hidden;
  background: #f5f5f5;
  border: 1px solid #eee;
}
.hero-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.hero-identity {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.hero-no-row {
  font-size: 18px;
  font-weight: 600;
}
.hero-no-label { color: #f97316; }
.hero-no-value { color: #f97316; letter-spacing: 1px; }
.hero-name-row {
  font-size: 17px;
  color: #333;
  font-weight: 500;
}
.hero-gender {
  color: #888;
  font-size: 15px;
  margin-left: 4px;
}
.hero-star-tag {
  display: inline-flex;
  align-items: center;
  color: #f59e0b;
  font-size: 15px;
  font-weight: 600;
  width: fit-content;
}
.hero-stats {
  display: flex;
  flex-direction: column;
  gap: 12px;
  border-left: 1px solid #f0e6d6;
  padding-left: 28px;
}
.stat-row {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #555;
  gap: 6px;
}
.stat-label { color: #888; }
.stat-value { color: #333; }
.online-chip {
  display: inline-flex;
  align-items: center;
  margin-left: 12px;
  color: #f59e0b;
  font-size: 13px;
  font-weight: 500;
}
.cert-chip-inline {
  display: inline-flex;
  align-items: center;
  padding: 2px 10px;
  border: 1px solid #f97316;
  color: #f97316;
  font-size: 12px;
  font-weight: 500;
  border-radius: 3px;
  background: #fff;
}
.cert-chip-none {
  color: #bbb;
  font-size: 13px;
}
.cert-chip-star {
  color: #f59e0b;
  font-size: 15px;
  margin-left: 4px;
  font-weight: 700;
}

.detail-body { background: #fff; border-radius: 8px; padding: 24px; }
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
  .tutor-hero-card {
    grid-template-columns: 100px 1fr;
    gap: 16px;
    padding: 16px;
  }
  .hero-avatar-wrap { width: 100px; height: 100px; }
  .hero-stats {
    grid-column: 1 / -1;
    border-left: none;
    border-top: 1px solid #f0e6d6;
    padding-left: 0;
    padding-top: 14px;
    margin-top: 6px;
  }
  .detail-body { padding: 16px; }
  .el-descriptions { font-size: 13px; }
}

.cert-verified-chip {
  margin-top: 12px;
  display: inline-flex;
  align-items: center;
  padding: 3px 12px;
  font-size: 12px;
  font-weight: 600;
  color: #047857;
  letter-spacing: 0.3px;
  background: linear-gradient(180deg, #ecfdf5 0%, #d1fae5 100%);
  border: 1px solid #a7f3d0;
  border-radius: 999px;
  white-space: nowrap;
  box-shadow: 0 1px 2px rgba(5, 150, 105, 0.12);
}
</style>
