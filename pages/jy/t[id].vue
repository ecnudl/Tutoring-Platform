<template>
  <div class="tt-page">
    <Head>
      <Title>T{{ displayNo }}教员详情 - 591家教网</Title>
      <Meta name="description" :content="`查看T${displayNo}教员的详细信息，包括学历、院校、教学经验、授课科目等。`" />
    </Head>

    <div class="tt-container" v-loading="loading">
      <el-breadcrumb separator="/" class="tt-crumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/jy' }">教员库</el-breadcrumb-item>
        <el-breadcrumb-item>T{{ displayNo }}详情</el-breadcrumb-item>
      </el-breadcrumb>

      <template v-if="tutor">
        <!-- ========== 顶部 HERO 卡 ========== -->
        <div class="tt-hero">
          <div class="tt-hero-photo">
            <img :src="tutor.avatar || '/placeholder/avatar.png'" alt="教员头像" />
          </div>

          <div class="tt-hero-info">
            <div class="tt-hero-no">
              <span class="tt-hero-no-label">编号：</span>
              <span class="tt-hero-no-value">T{{ displayNo }}</span>
            </div>
            <div class="tt-hero-name-row">
              <span class="tt-hero-name">{{ getDisplayName(tutor) }}</span>
              <span class="tt-hero-icons">
                <span v-if="tutor.isStar === 1" class="tt-hero-icon" title="明星教员">⭐</span>
                <span v-if="tutor.isVerified === 1" class="tt-hero-icon" title="证件已认证">✅</span>
                <span v-if="tutor.teachingMethod === 3 || tutor.teachingMethod === 4" class="tt-hero-icon" title="可网络授课">🌐</span>
              </span>
              <span v-if="tutor.gender === 1 || tutor.gender === 2" class="tt-hero-gender">[{{ genderMap[tutor.gender] }}]</span>
            </div>
            <div class="tt-hero-price">
              <span class="tt-hero-price-label">课时费：</span>
              <span class="tt-hero-price-value">¥{{ tutor.priceMin || 0 }} - {{ tutor.priceMax || 0 }}</span>
              <span class="tt-hero-price-unit">元/小时</span>
            </div>
          </div>

          <div class="tt-hero-stats">
            <div class="tt-hero-stat-row">
              <span class="tt-hero-stat-label">浏览次数：</span>
              <span class="tt-hero-stat-value">{{ tutor.viewCount || 0 }}次</span>
              <span v-if="tutor.teachingMethod === 3 || tutor.teachingMethod === 4" class="tt-hero-online-chip">
                <svg viewBox="0 0 24 24" width="12" height="12" fill="none" stroke="#D97706" stroke-width="2" style="margin-right:3px;vertical-align:-1px">
                  <circle cx="12" cy="10" r="3"/><path d="M12 21s-7-7-7-11a7 7 0 1114 0c0 4-7 11-7 11z"/>
                </svg>
                可网络授课
              </span>
            </div>
            <div class="tt-hero-stat-row">
              <span class="tt-hero-stat-label">登录次数：</span>
              <span class="tt-hero-stat-value">{{ tutor.loginCount || 0 }}次</span>
            </div>
            <div class="tt-hero-stat-row">
              <span class="tt-hero-stat-label">最近登录：</span>
              <span class="tt-hero-stat-value">{{ formatLastLogin(tutor.lastLoginTime) }}</span>
            </div>
            <div class="tt-hero-stat-row">
              <span class="tt-hero-stat-label">认证情况：</span>
              <span v-if="tutor.isVerified === 1" class="tt-hero-cert-ok">证件已认证 ★</span>
              <span v-else class="tt-hero-cert-no">未认证</span>
            </div>
          </div>
        </div>

        <!-- ========== 主体: 左主内容 + 右侧栏 ========== -->
        <div class="tt-body">

          <main class="tt-main">
            <!-- 锚点 tab -->
            <nav class="tt-tabs">
              <a href="#tt-basic" :class="['tt-tab', activeTab === 'basic' && 'is-active']" @click.prevent="scrollTo('basic')">基本信息</a>
              <a href="#tt-teaching" :class="['tt-tab', activeTab === 'teaching' && 'is-active']" @click.prevent="scrollTo('teaching')">家教信息</a>
              <a href="#tt-records" :class="['tt-tab', activeTab === 'records' && 'is-active']" @click.prevent="scrollTo('records')">
                成功记录
                <span v-if="successRecordCount" class="tt-tab-badge">{{ successRecordCount }}</span>
              </a>
            </nav>

            <!-- ========== 基本信息 ========== -->
            <section id="tt-basic" class="tt-card">
              <h2 class="tt-section-title">基本信息</h2>
              <dl class="tt-dl">
                <div><dt>姓 名：</dt><dd>{{ getDisplayName(tutor) }}<span v-if="tutor.gender" class="tt-gender">[{{ genderMap[tutor.gender] }}]</span></dd></div>
                <div><dt>出 生：</dt><dd>{{ formatBirth(tutor.birthDate) }}</dd></div>
                <div><dt>学 历：</dt><dd>{{ degreeMap[tutor.degree] || '—' }}</dd></div>
                <div><dt>省 份：</dt><dd>{{ tutor.cityName || '—' }}</dd></div>
                <div><dt>身 份：</dt><dd>{{ tutorTypeMap[tutor.tutorType] || '—' }}</dd></div>
                <div><dt>生 活：</dt><dd><span class="tt-pin">📍</span>{{ buildArea(tutor) }}</dd></div>
                <div v-if="tutor.university"><dt>高 校：</dt><dd>{{ tutor.university }}</dd></div>
                <div v-if="tutor.identityDetail"><dt>身份详情：</dt><dd>{{ tutor.identityDetail }}</dd></div>
                <div v-if="tutor.major"><dt>专 业：</dt><dd>{{ tutor.major }}</dd></div>
                <div><dt>位 置：</dt><dd><span class="tt-pin">📍</span>{{ tutor.cityName || '—' }}</dd></div>
                <div v-if="tutor.gradeYear"><dt>年级/年份：</dt><dd>{{ tutor.gradeYear }}</dd></div>
                <div v-if="tutor.highSchool"><dt>高中母校：</dt><dd>{{ tutor.highSchool }}</dd></div>
                <div v-if="tutor.hometownProvince"><dt>籍 贯：</dt><dd>{{ tutor.hometownProvince }}</dd></div>
              </dl>
            </section>

            <!-- ========== 家教信息 ========== -->
            <section id="tt-teaching" class="tt-card">
              <h2 class="tt-section-title">家教信息</h2>

              <div class="tt-block">
                <h3 class="tt-block-title">自我描述</h3>
                <p class="tt-block-text" v-if="tutor.selfIntroduction">{{ tutor.selfIntroduction }}</p>
                <p class="tt-block-empty" v-else>教员暂未填写自我介绍</p>
              </div>

              <div class="tt-block" v-if="tutor.certificatesDesc">
                <h3 class="tt-block-title">所获证书</h3>
                <p class="tt-block-text">{{ tutor.certificatesDesc }}</p>
              </div>

              <div class="tt-block">
                <h3 class="tt-block-title">可授课科目</h3>
                <p v-if="subjectNamesList.length" class="tt-block-text tt-block-strong">{{ subjectNamesList.join('、') }}</p>
                <p class="tt-block-empty" v-else>暂无</p>
              </div>

              <div class="tt-block">
                <h3 class="tt-block-title">可授课区域</h3>
                <div v-if="teachingAreaNames.length || tutor.cityName" class="tt-area">
                  <span v-if="tutor.cityName" class="tt-area-city">{{ tutor.cityName }}</span>
                  <template v-if="teachingAreaNames.length">
                    <span class="tt-area-sep">·</span>
                    <span v-for="(a, i) in teachingAreaNames" :key="i" class="tt-area-pill">{{ a }}</span>
                  </template>
                </div>
                <p class="tt-block-empty" v-else>暂无</p>
              </div>

              <div class="tt-block">
                <h3 class="tt-block-title">辅导方式</h3>
                <p class="tt-block-text">
                  {{ teachingMethodMap[tutor.teachingMethod] || '—' }}
                  <span v-if="tutor.freeTrial === 1" class="tt-free-trial">（支持免费试课）</span>
                </p>
              </div>

              <div class="tt-block" v-if="tutor.teachingExperience">
                <h3 class="tt-block-title">经历</h3>
                <p class="tt-block-text tt-block-multiline">{{ tutor.teachingExperience }}</p>
              </div>

              <div class="tt-block" v-if="tutor.salaryRemark">
                <h3 class="tt-block-title">薪水要求</h3>
                <p class="tt-block-text">{{ tutor.salaryRemark }}</p>
              </div>
            </section>

            <!-- ========== 成功记录 ========== -->
            <section id="tt-records" class="tt-card">
              <h2 class="tt-section-title">
                成功记录
                <span v-if="successRecordCount" class="tt-section-count">{{ successRecordCount }}</span>
              </h2>
              <div v-if="tutor.showSuccessRecord === 0" class="tt-block-empty tt-block-empty-large">
                教员选择不公开成功记录
              </div>
              <ol v-else-if="tutor.successRecords && tutor.successRecords.length" class="tt-records">
                <li v-for="(r, i) in tutor.successRecords" :key="i" class="tt-record">
                  <div class="tt-record-main">
                    <div class="tt-record-title">
                      <span v-if="r.grade" class="tt-record-grade">{{ r.grade }}</span>
                      <span v-if="r.subjects" class="tt-record-subjects">{{ formatSubjects(r.subjects) }}</span>
                      <span v-if="r.location" class="tt-record-loc">{{ r.location }}</span>
                    </div>
                    <p v-if="r.detail" class="tt-record-detail">{{ r.detail }}</p>
                  </div>
                  <span class="tt-record-date">{{ r.date }}</span>
                </li>
              </ol>
              <div v-else class="tt-block-empty tt-block-empty-large">暂无成功记录</div>
            </section>

            <!-- 底部行动条 -->
            <div class="tt-action-bar">
              <button type="button" class="tt-btn tt-btn-primary" @click="handleBooking">预约该教员</button>
              <button type="button" class="tt-btn tt-btn-ghost" @click="$router.back()">返回教员库</button>
            </div>
          </main>

          <!-- ========== 右侧栏 ========== -->
          <aside class="tt-aside">
            <div class="tt-aside-card tt-aside-cta">
              <div class="tt-aside-price">
                <span class="tt-aside-price-symbol">¥</span>
                <span class="tt-aside-price-num">{{ tutor.priceMin || 0 }} - {{ tutor.priceMax || 0 }}</span>
                <span class="tt-aside-price-unit">/小时</span>
              </div>
              <button type="button" class="tt-btn tt-btn-primary tt-aside-btn" @click="handleBooking">预约该教员</button>
              <p class="tt-aside-hint">
                <svg viewBox="0 0 24 24" width="13" height="13" fill="#1F4E8C" style="vertical-align:-2px;margin-right:4px"><path d="M12 1l3 6 6 .9-4.5 4.4 1 6.7L12 16l-5.5 3 1-6.7L3 7.9 9 7z"/></svg>
                提交后客服 1 小时内联系，免费安排面试
              </p>
            </div>

            <div class="tt-aside-card tt-aside-qr">
              <img src="/wechat-qr.png" alt="591 家教微信" class="tt-aside-qr-img" />
              <div class="tt-aside-qr-title">加微信，随时查</div>
              <div class="tt-aside-qr-sub">扫码关注微信服务号<br/>新消息第一时间通知</div>
            </div>

            <div class="tt-aside-card tt-aside-help">
              <div class="tt-aside-help-title">请家教须知</div>
              <ul class="tt-aside-help-list">
                <li><NuxtLink to="/about/contact">如何快速找到合适教员？</NuxtLink></li>
                <li><NuxtLink to="/about/contact">如何与教员结算报酬？</NuxtLink></li>
                <li><NuxtLink to="/qjj">591 请家教完整流程</NuxtLink></li>
                <li><NuxtLink to="/jy">浏览更多教员</NuxtLink></li>
              </ul>
            </div>
          </aside>
        </div>
      </template>

      <div v-else-if="!loading" class="tt-empty">
        <p>该教员信息不存在或已下架</p>
        <NuxtLink to="/jy">
          <button type="button" class="tt-btn tt-btn-primary">返回教员库</button>
        </NuxtLink>
      </div>
    </div>

    <!-- 移动端 sticky 底部 CTA -->
    <div v-if="tutor" class="tt-mobile-bar">
      <div class="tt-mobile-price">
        <span class="tt-mobile-price-num">¥{{ tutor.priceMin || 0 }}-{{ tutor.priceMax || 0 }}</span>
        <span class="tt-mobile-price-unit">/小时</span>
      </div>
      <button type="button" class="tt-btn tt-btn-primary tt-mobile-btn" @click="handleBooking">预约该教员</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'

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
const teachingMethodMap = { 1: '教员上门', 2: '学员上门', 3: '在线辅导', 4: '上门或网络均可' }

function getDisplayName(t) {
  const name = (t.realName || '').trim()
  if (!name) return '匿名教员'
  const surname = name.charAt(0)
  const suffix = t.tutorType === 1 ? '教员' : '老师'
  return surname + suffix
}

function formatLastLogin(dt) {
  if (!dt) return '—'
  const m = String(dt).match(/^(\d{4})-(\d{2})-(\d{2})/)
  return m ? `${m[2]}-${m[3]}` : String(dt).slice(5, 10)
}

function formatBirth(dt) {
  if (!dt) return '—'
  const m = String(dt).match(/^(\d{4})/)
  return m ? m[1] : '—'
}

function buildArea(t) {
  const parts = []
  if (t.cityName) parts.push(t.cityName)
  if (t.districtName) parts.push(t.districtName)
  return parts.length ? parts.join('-') : '—'
}

const subjectNamesList = computed(() => {
  const t = tutor.value
  if (!t) return []
  if (Array.isArray(t.subjectNames) && t.subjectNames.length) return t.subjectNames
  if (Array.isArray(t.subjects)) return t.subjects.map(s => s?.subjectName).filter(Boolean)
  return []
})

const teachingAreaNames = computed(() => {
  const t = tutor.value
  if (!t || !Array.isArray(t.teachingAreas)) return []
  return t.teachingAreas.map(a => a?.districtName).filter(Boolean)
})

const successRecordCount = computed(() => {
  const t = tutor.value
  if (!t || t.showSuccessRecord === 0) return 0
  return Array.isArray(t.successRecords) ? t.successRecords.length : 0
})

const formatSubjects = (csv) => {
  if (!csv) return ''
  return String(csv).split(',').map(s => s.trim()).filter(Boolean).join(' ')
}

const scrollTo = (key) => {
  activeTab.value = key
  const el = document.getElementById('tt-' + key)
  if (el) {
    const top = el.getBoundingClientRect().top + window.scrollY - 80
    window.scrollTo({ top, behavior: 'smooth' })
  }
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

const handleBooking = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再预约')
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath))
    return
  }
  if (!tutor.value || !tutor.value.id) {
    ElMessage.error('教员信息未加载完成，请稍后重试')
    return
  }
  if (!userStore.isStudent) {
    ElMessage.warning('仅家长/学员可发起预约（当前账号为教员或未设置类型）')
    return
  }
  try {
    const KEY = 'pending_booking_tutors'
    const raw = localStorage.getItem(KEY)
    const list = raw ? JSON.parse(raw) : []
    const id = String(tutor.value.id)
    if (!list.find(x => String(x.id) === id)) {
      list.push({ id, displayNo: tutor.value.displayNo, addedAt: new Date().toISOString() })
      localStorage.setItem(KEY, JSON.stringify(list))
    }
  } catch (_) {}
  router.push('/jy/booking')
}

onMounted(() => { loadTutor() })
</script>

<style scoped>
/* ===========================================================
   591 教员详情页 — ttgood 信息工具风
   白底 + 浅灰卡片 + 橙色 accent + 蓝辅色
   =========================================================== */
.tt-page {
  background: var(--color-bg);
  min-height: 100vh;
  padding-bottom: 60px;
  /* 主色: 蓝 (591 token primary) — 用于编号 / section 标题 / 主 CTA / 价格 / 链接 */
  --tt-primary: #1F4E8C;
  --tt-primary-soft: #E8EFF8;
  --tt-primary-deep: #163B6B;
  /* 辅色: 金橙 (591 token accent) — 仅作"亮点"标记: 明星 / 试课 / 已认证 / 可网络授课 */
  --tt-accent: #F59E0B;
  --tt-accent-soft: #FFFBF1;
  --tt-accent-deep: #D97706;
  --tt-card: #FFFFFF;
  --tt-line: #EAECF0;
  --tt-line-soft: #F2F4F7;
}

.tt-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 16px 24px;
}

/* breadcrumb */
.tt-crumb { margin-bottom: 12px; padding: 4px 0; }
.tt-crumb :deep(.el-breadcrumb__inner),
.tt-crumb :deep(.el-breadcrumb__inner.is-link) {
  color: #667085 !important;
  font-weight: 400 !important;
  font-size: 13px;
}
.tt-crumb :deep(.el-breadcrumb__inner.is-link:hover) { color: var(--tt-primary) !important; }
.tt-crumb :deep(.el-breadcrumb__separator) { color: #cbd5e1; }

/* ============ HERO CARD ============ */
.tt-hero {
  background: var(--tt-card);
  border-radius: 8px;
  border: 1px solid var(--tt-line);
  padding: 24px 28px;
  display: grid;
  grid-template-columns: 180px 1fr 240px;
  gap: 28px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(16, 24, 40, 0.04);
}
.tt-hero-photo {
  width: 180px;
  height: 180px;
  border-radius: 6px;
  border: 1px solid var(--tt-line);
  overflow: hidden;
  background: var(--tt-line-soft);
}
.tt-hero-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.tt-hero-info { display: flex; flex-direction: column; gap: 12px; padding-top: 4px; }
.tt-hero-no { font-size: 22px; line-height: 1.2; }
.tt-hero-no-label { color: #667085; font-size: 14px; font-weight: 400; }
.tt-hero-no-value { color: var(--tt-primary); font-weight: 600; letter-spacing: 0.5px; font-size: 22px; }
.tt-hero-name-row { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.tt-hero-name { font-size: 20px; color: #1F2937; font-weight: 500; }
.tt-hero-gender { color: #94a3b8; font-size: 15px; }
.tt-hero-icons { display: inline-flex; align-items: center; gap: 4px; }
.tt-hero-icon { display: inline-block; font-size: 18px; line-height: 1; }
.tt-hero-star {
  display: inline-flex; align-items: center; gap: 4px;
  align-self: flex-start;
  padding: 3px 10px;
  background: var(--tt-accent-soft);
  color: var(--tt-accent-deep);
  border: 1px solid #fde0c1;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
.tt-hero-star-mark { color: var(--tt-accent); }
.tt-hero-price {
  display: flex; align-items: baseline; gap: 4px;
  font-size: 14px;
  color: #4b5563;
}
.tt-hero-price-label { color: #667085; font-size: 13px; }
.tt-hero-price-value { color: var(--tt-primary); font-weight: 600; font-size: 22px; letter-spacing: 0.3px; }
.tt-hero-price-unit { color: #94a3b8; font-size: 13px; margin-left: 2px; }

.tt-hero-stats {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
  padding-left: 28px;
  border-left: 1px dashed var(--tt-line);
}
.tt-hero-stat-row {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #1F2937;
  gap: 4px;
  white-space: nowrap;
}
.tt-hero-stat-label { color: #667085; flex-shrink: 0; }
.tt-hero-stat-value { color: #1F2937; font-weight: 500; }
.tt-hero-online-chip {
  display: inline-flex;
  align-items: center;
  margin-left: 10px;
  padding: 2px 8px;
  background: var(--tt-accent-soft);
  border: 1px solid #fde0c1;
  border-radius: 3px;
  color: var(--tt-accent-deep);
  font-size: 12px;
}
.tt-hero-cert-ok {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  padding: 2px 10px;
  border: 1px solid var(--tt-accent);
  background: var(--tt-accent-soft);
  color: var(--tt-accent-deep);
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
.tt-hero-cert-no { color: #94a3b8; font-size: 13px; }

/* ============ BODY ============ */
.tt-body {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 16px;
  align-items: start;
}
.tt-main { min-width: 0; }

/* ============ TABS (anchor scroll) ============ */
.tt-tabs {
  display: flex;
  gap: 0;
  background: var(--tt-card);
  border: 1px solid var(--tt-line);
  border-bottom: 0;
  border-radius: 8px 8px 0 0;
  padding: 0 8px;
  position: sticky;
  top: 0;
  z-index: 5;
}
.tt-tab {
  padding: 14px 20px;
  font-size: 14px;
  color: #4b5563;
  text-decoration: none;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  transition: color 0.15s ease, border-color 0.15s ease;
}
.tt-tab:hover { color: var(--tt-primary); }
.tt-tab.is-active {
  color: var(--tt-primary);
  border-bottom-color: var(--tt-primary);
  font-weight: 500;
}
.tt-tab-badge {
  display: inline-block;
  padding: 0 6px;
  height: 16px;
  line-height: 16px;
  font-size: 11px;
  background: var(--tt-primary);
  color: #fff;
  border-radius: 8px;
  font-weight: 500;
}

/* ============ CARD (内容块) ============ */
.tt-card {
  background: var(--tt-card);
  border: 1px solid var(--tt-line);
  border-top: 0;
  padding: 24px 28px;
}
.tt-card:first-of-type { border-top: 0; }
.tt-card + .tt-card { margin-top: -1px; }
.tt-card:last-of-type { border-radius: 0 0 8px 8px; }

.tt-section-title {
  font-size: 16px;
  color: var(--tt-primary);
  font-weight: 600;
  margin: 0 0 18px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--tt-line-soft);
  display: flex;
  align-items: baseline;
  gap: 10px;
}
.tt-section-count {
  display: inline-block;
  padding: 0 8px;
  height: 18px;
  line-height: 18px;
  background: var(--tt-primary);
  color: #fff;
  border-radius: 9px;
  font-size: 11px;
  font-weight: 500;
}

/* ============ DL (基本信息) ============ */
.tt-dl {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  column-gap: 32px;
  row-gap: 0;
  margin: 0;
}
.tt-dl > div {
  display: flex;
  align-items: baseline;
  padding: 10px 0;
  border-bottom: 1px dashed var(--tt-line-soft);
  gap: 8px;
  font-size: 14px;
}
.tt-dl dt {
  color: #667085;
  flex-shrink: 0;
  width: 78px;
}
.tt-dl dd {
  margin: 0;
  color: #1F2937;
  flex: 1;
  word-break: break-word;
}
.tt-gender { color: #94a3b8; margin-left: 4px; font-size: 13px; }
.tt-pin { color: var(--tt-accent); margin-right: 2px; }

/* ============ BLOCK (家教信息子块) ============ */
.tt-block {
  padding: 14px 0;
  border-bottom: 1px dashed var(--tt-line-soft);
}
.tt-block:last-child { border-bottom: 0; padding-bottom: 0; }
.tt-block:first-child { padding-top: 0; }
.tt-block-title {
  font-size: 14px;
  color: var(--tt-primary);
  font-weight: 600;
  margin: 0 0 8px;
}
.tt-block-text {
  font-size: 14px;
  color: #1F2937;
  line-height: 1.85;
  margin: 0;
  word-break: break-word;
}
.tt-block-strong { color: #1F2937; font-weight: 500; letter-spacing: 0.3px; }
.tt-block-multiline { white-space: pre-line; line-height: 1.95; }
.tt-block-empty { color: #94a3b8; font-size: 13px; margin: 0; }
.tt-block-empty-large { color: #94a3b8; font-size: 14px; padding: 32px 0; text-align: center; margin: 0; }
.tt-free-trial { color: var(--tt-accent-deep); font-size: 13px; margin-left: 4px; }

/* area */
.tt-area {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #1F2937;
}
.tt-area-city { font-weight: 500; }
.tt-area-sep { color: #cbd5e1; margin: 0 4px; }
.tt-area-pill {
  display: inline-block;
  padding: 2px 10px;
  background: var(--tt-primary-soft);
  color: var(--tt-primary);
  border-radius: 3px;
  font-size: 13px;
}

/* ============ RECORDS ============ */
.tt-records {
  list-style: none;
  margin: 0;
  padding: 0;
}
.tt-record {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  padding: 14px 0;
  border-bottom: 1px dashed var(--tt-line-soft);
}
.tt-record:last-child { border-bottom: 0; }
.tt-record-main { flex: 1; min-width: 0; }
.tt-record-title {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 8px;
  font-size: 14px;
  color: #1F2937;
}
.tt-record-grade { font-weight: 500; }
.tt-record-subjects { color: #4b5563; }
.tt-record-loc { color: #94a3b8; font-size: 13px; }
.tt-record-detail { font-size: 13px; color: #6b7280; margin: 4px 0 0; line-height: 1.7; }
.tt-record-date { font-size: 12px; color: #9ca3af; white-space: nowrap; flex-shrink: 0; padding-top: 2px; }

/* ============ ACTION BAR ============ */
.tt-action-bar {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}
.tt-btn {
  padding: 11px 28px;
  font-size: 14px;
  border: 1px solid var(--tt-line);
  border-radius: 4px;
  background: #fff;
  color: #1F2937;
  cursor: pointer;
  transition: all 0.15s ease;
  font-weight: 500;
}
.tt-btn:hover { border-color: var(--tt-primary); color: var(--tt-primary); }
.tt-btn-primary {
  background: var(--tt-primary);
  color: #fff;
  border-color: var(--tt-primary);
}
.tt-btn-primary:hover {
  background: var(--tt-primary-deep);
  border-color: var(--tt-primary-deep);
  color: #fff;
}
.tt-btn-ghost {
  background: #fff;
  color: #4b5563;
  border-color: var(--tt-line);
}
.tt-btn-ghost:hover { color: var(--tt-primary); border-color: var(--tt-primary); background: var(--tt-primary-soft); }

/* ============ ASIDE ============ */
.tt-aside {
  position: sticky;
  top: 60px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.tt-aside-card {
  background: var(--tt-card);
  border: 1px solid var(--tt-line);
  border-radius: 8px;
  padding: 18px 20px;
}
.tt-aside-cta {
  text-align: center;
  border-top: 3px solid var(--tt-primary);
  padding-top: 18px;
}
.tt-aside-price {
  display: flex;
  justify-content: center;
  align-items: baseline;
  gap: 2px;
  margin-bottom: 12px;
  color: var(--tt-primary);
}
.tt-aside-price-symbol { font-size: 16px; }
.tt-aside-price-num { font-size: 28px; font-weight: 700; letter-spacing: 0.3px; }
.tt-aside-price-unit { color: #94a3b8; font-size: 13px; margin-left: 2px; }
.tt-aside-btn { width: 100%; padding: 12px 0; font-size: 15px; }
.tt-aside-hint {
  margin: 12px 0 0;
  font-size: 12px;
  color: #6b7280;
  line-height: 1.5;
}

.tt-aside-qr { text-align: center; }
.tt-aside-qr-img {
  width: 160px;
  height: 160px;
  margin: 0 auto 8px;
  display: block;
  border: 1px solid var(--tt-line);
  padding: 6px;
  border-radius: 4px;
  background: #fff;
}
.tt-aside-qr-title {
  font-size: 14px;
  color: var(--tt-primary);
  font-weight: 600;
  margin-bottom: 4px;
}
.tt-aside-qr-sub {
  font-size: 12px;
  color: #6b7280;
  line-height: 1.6;
}

.tt-aside-help-title {
  font-size: 14px;
  color: var(--tt-primary);
  font-weight: 600;
  padding-bottom: 10px;
  margin-bottom: 8px;
  border-bottom: 1px solid var(--tt-line-soft);
}
.tt-aside-help-list {
  list-style: none;
  margin: 0;
  padding: 0;
}
.tt-aside-help-list li {
  padding: 7px 0;
  font-size: 13px;
}
.tt-aside-help-list a {
  color: #4b5563;
  text-decoration: none;
}
.tt-aside-help-list a:hover { color: var(--tt-primary); }

/* ============ EMPTY STATE ============ */
.tt-empty {
  text-align: center;
  padding: 80px 24px;
  color: #6b7280;
}
.tt-empty p { margin-bottom: 16px; font-size: 15px; }

/* ============ MOBILE BAR ============ */
.tt-mobile-bar { display: none; }

/* ============ RESPONSIVE ============ */
@media (max-width: 1024px) {
  .tt-body { grid-template-columns: 1fr; }
  .tt-aside {
    position: static;
    flex-direction: row;
    flex-wrap: wrap;
  }
  .tt-aside-card { flex: 1 1 280px; }
  .tt-aside-qr-img { width: 140px; height: 140px; }
}

@media (max-width: 720px) {
  .tt-container { padding: 12px 12px 100px; }
  .tt-hero {
    grid-template-columns: 100px 1fr;
    gap: 14px;
    padding: 16px;
  }
  .tt-hero-photo { width: 100px; height: 100px; }
  .tt-hero-stats {
    grid-column: 1 / -1;
    border-left: 0;
    border-top: 1px dashed var(--tt-line);
    padding-left: 0;
    padding-top: 12px;
    margin-top: 4px;
  }
  .tt-hero-no, .tt-hero-no-value { font-size: 18px; }
  .tt-hero-name { font-size: 17px; }
  .tt-hero-price-value { font-size: 18px; }

  .tt-tabs {
    border-radius: 8px 8px 0 0;
    padding: 0 4px;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }
  .tt-tab { padding: 12px 14px; font-size: 13px; }

  .tt-card { padding: 16px; }
  .tt-section-title { font-size: 15px; margin-bottom: 14px; }

  .tt-dl { grid-template-columns: 1fr; column-gap: 0; }
  .tt-dl > div { padding: 8px 0; font-size: 13px; }
  .tt-dl dt { width: 70px; font-size: 13px; }

  .tt-block-title { font-size: 13px; }
  .tt-block-text { font-size: 13px; }

  .tt-record { flex-direction: column; gap: 4px; padding: 12px 0; }
  .tt-record-date { padding-top: 0; }

  .tt-action-bar { display: none; }

  .tt-aside-card { flex: 1 1 100%; }
  .tt-aside-cta { display: none; }   /* 移动端用底部 sticky bar 替代 */
  .tt-aside-qr-img { width: 130px; height: 130px; }

  /* mobile sticky bar */
  .tt-mobile-bar {
    display: flex;
    position: fixed;
    bottom: 0; left: 0; right: 0;
    z-index: 50;
    padding: 10px 14px;
    background: #fff;
    border-top: 1px solid var(--tt-line);
    box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.04);
    align-items: center;
    justify-content: space-between;
    gap: 12px;
  }
  .tt-mobile-price { display: flex; align-items: baseline; gap: 2px; color: var(--tt-primary); }
  .tt-mobile-price-num { font-size: 20px; font-weight: 700; }
  .tt-mobile-price-unit { font-size: 12px; color: #94a3b8; }
  .tt-mobile-btn { padding: 10px 24px; font-size: 14px; flex-shrink: 0; }
}
</style>
