<template>
<div class="ct-home">
  <!-- 顶部信息卡 -->
  <div class="info-card">
    <div class="info-left">
      <NuxtLink :to="isTutor ? '/center/head-photo' : '/center/profile'" class="avatar-wrap">
        <el-avatar :size="96" :src="avatarUrl" shape="square" class="avatar" />
        <span v-if="!avatarUrl" class="avatar-badge">{{ isTutor ? '头像未上传' : '暂无头像' }}</span>
      </NuxtLink>
      <div class="completeness">
        <div class="comp-row">
          <span class="comp-label">信息完善度</span>
          <span class="comp-value">{{ completeness }}%</span>
        </div>
        <el-progress :percentage="completeness" :stroke-width="14" :show-text="false" color="#3b82f6" />
        <NuxtLink :to="isTutor ? '/center/tutor-profile' : '/center/profile'" class="comp-link">
          去完善 →
        </NuxtLink>
      </div>
    </div>

    <div class="info-right">
      <div class="user-line">
        <span class="user-no">{{ displayNo }}</span>
        <span class="user-name">{{ userStore.displayName || userStore.mobile }}</span>
      </div>
      <div class="meta">
        <span>简历浏览：<b>{{ stats.viewCount }}</b> 次</span>
        <span class="dot">·</span>
        <span>登录次数：<b>{{ stats.loginCount }}</b> 次</span>
      </div>
      <div class="meta" v-if="isTutor">
        简历状态：<span :class="['status', statusClass]">{{ statusText }}</span>
      </div>
      <div class="meta">
        热线：<a :href="`tel:${hotline}`" class="phone">{{ hotline }}</a>
      </div>
    </div>
  </div>

  <!-- 互动消息 + 我的订单 -->
  <div class="two-col">
    <div class="col">
      <div class="col-title">互动消息</div>
      <div class="col-items">
        <NuxtLink :to="isTutor ? '/center/applications' : '/center/received-applications'"
                  class="col-chip chip-a">
          <span>{{ isTutor ? '最新申请' : '收到申请' }}</span>
          <span class="chip-badge" v-if="stats.latestApply">{{ stats.latestApply }}</span>
        </NuxtLink>
        <NuxtLink to="/center/messages" class="col-chip chip-b">
          <span>消息</span>
          <span class="chip-badge" v-if="stats.unread">{{ stats.unread }}</span>
        </NuxtLink>
      </div>
    </div>

    <div class="col">
      <div class="col-title">我的订单</div>
      <div class="col-items">
        <NuxtLink to="/center/orders" class="col-chip chip-c">
          <span>全部订单</span>
          <span class="chip-badge" v-if="stats.orders">{{ stats.orders }}</span>
        </NuxtLink>
        <NuxtLink to="/center/reservations" class="col-chip chip-d">
          <span>我的预约</span>
          <span class="chip-badge" v-if="stats.reservations">{{ stats.reservations }}</span>
        </NuxtLink>
      </div>
    </div>
  </div>

  <!-- 快捷入口：教员 9 格 + 9 格 -->
  <div class="quick-grid" v-if="isTutor">
    <NuxtLink v-for="q in tutorQuick" :key="q.label" :to="q.href" class="qk-item">
      <span class="qk-icon" :style="{ background: q.bg, color: q.color }">{{ q.icon }}</span>
      <span class="qk-label">{{ q.label }}</span>
      <span v-if="q.hot" class="qk-hot">热</span>
    </NuxtLink>
  </div>
  <div class="quick-grid" v-else>
    <NuxtLink v-for="q in studentQuick" :key="q.label" :to="q.href" class="qk-item">
      <span class="qk-icon" :style="{ background: q.bg, color: q.color }">{{ q.icon }}</span>
      <span class="qk-label">{{ q.label }}</span>
    </NuxtLink>
  </div>

  <!-- 帮助卡（浅蓝渐变，ttgood 用的是浅黄） -->
  <div class="help-card">
    <div class="help-visual">
      <div class="help-icon">📘</div>
    </div>
    <div class="help-links">
      <div class="help-row">
        <NuxtLink :to="isTutor ? '/help/become-tutor' : '/help/request-process'">
          {{ isTutor ? '成为家教老师' : '请家教流程' }} <span>›</span>
        </NuxtLink>
        <NuxtLink :to="isTutor ? '/help/tutor-pricing' : '/zf'">
          {{ isTutor ? '做家教收费标准' : '请家教收费标准' }} <span>›</span>
        </NuxtLink>
      </div>
      <div class="help-row">
        <NuxtLink :to="isTutor ? '/help/tutor-faq' : '/help/request-faq'">
          {{ isTutor ? '做家教常见问题' : '请家教常见问题' }} <span>›</span>
        </NuxtLink>
        <NuxtLink :to="isTutor ? '/help/find-tutor' : '/help/find-tutor'">
          怎样快速找到老师 <span>›</span>
        </NuxtLink>
      </div>
      <div class="help-row">
        <NuxtLink to="/center/feedback">意见反馈 <span>›</span></NuxtLink>
        <NuxtLink to="/about/contact">联系客服 <span>›</span></NuxtLink>
      </div>
    </div>
  </div>

  <!-- 教员特别提醒 -->
  <div class="notice-box" v-if="isTutor">
    <div class="notice-title">教员特别提醒</div>
    <ol class="notice-list">
      <li>被两位家长投诉、经核实后，平台将不再合作。</li>
      <li class="warn">严禁以任何理由提前预收课时费或向家长借贷！</li>
      <li class="warn">严禁私自转课或找人代课，相关法律责任由教员自己承担！</li>
      <li>家教出现任何问题请及时联系客服处理，请勿直接和家长产生争执。</li>
      <li>我们会不定期向家长回访，好评教员会优先推荐优质订单。</li>
      <li>请认真对待每一份家教，成功率过低的教员平台会慎重考虑后期合作。</li>
      <li>特别注意仿冒 591 家教网的非法人员，接单有疑问请联系客服核实防受骗。</li>
    </ol>
  </div>
</div>
</template>

<script setup>
definePageMeta({ layout: 'center', middleware: 'auth' })
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'
import { useSiteConfig } from '~/composables/useSiteConfig'

const userStore = useUserStore()
const { post, get } = useApi()
const { config, load: loadSiteConfig } = useSiteConfig()

const isTutor = computed(() => userStore.isTutor)
const hotline = computed(() => config.value.siteHotline || '13795420591')

const displayNo = ref('T000000')
const stats = ref({
  viewCount: 0,
  loginCount: 0,
  latestApply: 0,
  unread: 0,
  orders: 0,
  reservations: 0,
  completeness: 0
})
const profile = ref(null)
const userHead = ref('')
const avatarUrl = computed(() => {
  if (isTutor.value) return profile.value?.avatar || ''
  return userHead.value || ''
})

const statusText = computed(() => {
  const s = profile.value?.auditStatus
  if (s === 0) return '草稿（待提交）'
  if (s === 1) return '审核中'
  if (s === 2) return '已通过'
  if (s === 3) return '已驳回'
  if (s === 4) return '已发布'
  return '未完善'
})
const statusClass = computed(() => {
  const s = profile.value?.auditStatus
  if (s === 2 || s === 4) return 'st-ok'
  if (s === 3) return 'st-bad'
  if (s === 1) return 'st-wait'
  return 'st-draft'
})

// 完善度粗略估算（教员：基本 + 简历 + 认证）
const completeness = computed(() => {
  const p = profile.value
  if (!p) return isTutor.value ? 15 : 20
  let total = 0
  const keys = isTutor.value
    ? ['realName', 'gender', 'university', 'major', 'degree', 'subjects', 'selfIntroduction', 'avatar', 'city_id', 'priceMin']
    : ['realName', 'gender', 'mobile']
  keys.forEach(k => { if (p[k] != null && p[k] !== '') total++ })
  return Math.min(100, Math.round((total / keys.length) * 100))
})

const tutorQuick = [
  { label: '相册',     icon: '🖼', href: '/center/photos',          bg: '#eff6ff', color: '#1d4ed8' },
  { label: '认证',     icon: '🪪', href: '/center/certifications',  bg: '#ecfdf5', color: '#047857' },
  { label: '改头像',   icon: '🧑', href: '/center/head-photo',      bg: '#fef3c7', color: '#a16207' },
  { label: '改简历',   icon: '📝', href: '/center/tutor-profile',   bg: '#f3e8ff', color: '#7e22ce' },
  { label: '改城市',   icon: '📍', href: '/center/tutor-profile',   bg: '#fee2e2', color: '#b91c1c', hot: true },
  { label: '简历预览', icon: '👁', href: '/center/tutor-preview',   bg: '#e0f2fe', color: '#0369a1' },
  { label: '信誉记录', icon: '⭐', href: '/center/credit',          bg: '#fef9c3', color: '#a16207' },
  { label: '明星教员', icon: '🏆', href: '/center/profile',         bg: '#fce7f3', color: '#be185d' },
  { label: '资金流水', icon: '💰', href: '/center/orders',          bg: '#ecfdf5', color: '#047857' }
]

const studentQuick = [
  { label: '发布需求', icon: '➕', href: '/qjj',                     bg: '#eff6ff', color: '#1d4ed8' },
  { label: '我的需求', icon: '📄', href: '/center/requirements',     bg: '#f3e8ff', color: '#7e22ce' },
  { label: '备选教员', icon: '⭐', href: '/center/favorites',        bg: '#fef3c7', color: '#a16207' },
  { label: '已备选',   icon: '📌', href: '/center/shortlist',        bg: '#ecfdf5', color: '#047857' },
  { label: '浏览足迹', icon: '👣', href: '/center/footprint',        bg: '#e0f2fe', color: '#0369a1' },
  { label: '我的订单', icon: '📋', href: '/center/orders',           bg: '#fee2e2', color: '#b91c1c' },
  { label: '预约管理', icon: '📅', href: '/center/reservations',     bg: '#f3e8ff', color: '#7e22ce' },
  { label: '基本资料', icon: '👤', href: '/center/profile',          bg: '#eff6ff', color: '#1d4ed8' },
  { label: '意见反馈', icon: '💬', href: '/center/feedback',         bg: '#fef9c3', color: '#a16207' }
]

onMounted(async () => {
  loadSiteConfig()
  try {
    if (isTutor.value) {
      // 拉教员简历信息
      const res = await get('/user/auth/tutor-profile/view')
      if (res?.code === 200 && res.data) {
        profile.value = res.data
        displayNo.value = res.data.displayNo || 'T' + (String(res.data.id || '000000').slice(-6))
        stats.value.viewCount = res.data.viewCount || 0
        stats.value.loginCount = res.data.loginCount || 0
      }
    } else {
      const res = await get('/user/auth/student-profile/view').catch(() => null)
      if (res?.code === 200 && res.data) profile.value = res.data
      // 家长头像在 users.user_head
      const u = await get('/user/auth/users/view').catch(() => null)
      if (u?.code === 200 && u.data?.userHead) userHead.value = u.data.userHead
    }
  } catch (e) { /* silent */ }

  // 申请数、预约数等的粗略统计（有接口就拉，没有就零）
  try {
    const r1 = await post('/user/auth/reservation/page', { pageCurrent: 1, pageSize: 1 }).catch(() => null)
    if (r1?.code === 200 && r1.data) stats.value.reservations = r1.data.totalCount || 0
  } catch (e) {}
})
</script>

<style scoped>
.ct-home { display: flex; flex-direction: column; gap: 20px; }

/* 头部信息卡 */
.info-card {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  background: #fff;
  border-radius: 18px 0 18px 0;
  padding: 28px 32px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  border: 1px solid #f1f5f9;
}

.info-left { display: flex; gap: 24px; align-items: flex-start; }

.avatar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}
.avatar {
  border-radius: 14px 0 14px 0 !important;
  box-shadow: 3px 3px 8px rgba(0,0,0,0.08);
}
.avatar-badge {
  display: inline-block;
  padding: 3px 8px;
  background: var(--color-primary, #163B6B);
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
  letter-spacing: 1px;
}

.completeness { display: flex; flex-direction: column; justify-content: flex-end; gap: 8px; min-width: 180px; }
.comp-row { display: flex; align-items: baseline; gap: 10px; }
.comp-label { font-size: 14px; color: #64748b; text-decoration: underline; }
.comp-value { font-size: 32px; font-weight: 700; color: var(--color-primary, #163B6B); font-family: Georgia, serif; }
.comp-link { font-size: 13px; color: var(--color-primary); text-decoration: none; }
.comp-link:hover { text-decoration: underline; }

.info-right { text-align: right; display: flex; flex-direction: column; justify-content: flex-end; gap: 6px; }
.user-line { font-size: 14px; color: #64748b; }
.user-no { font-family: Georgia, serif; letter-spacing: 1px; }
.user-name { font-size: 26px; color: #111827; font-weight: 700; margin-left: 10px; }
.meta { font-size: 13px; color: #64748b; }
.meta b { color: #111827; margin: 0 2px; }
.meta .dot { margin: 0 6px; color: #cbd5e1; }
.status { font-weight: 600; margin-left: 6px; }
.st-ok { color: #047857; }
.st-wait { color: #d97706; }
.st-bad { color: #b91c1c; }
.st-draft { color: #64748b; }
.phone { font-family: Georgia, serif; color: var(--color-primary); font-weight: 700; text-decoration: none; }

/* 两列条状 */
.two-col { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.col { background: #fff; border-radius: 10px; padding: 18px 20px; border: 1px solid #f1f5f9; }
.col-title { font-size: 16px; font-weight: 700; color: #111827; margin-bottom: 12px; padding-bottom: 8px; }
.col-items { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.col-chip {
  position: relative;
  display: flex; align-items: center; justify-content: center;
  padding: 14px 12px; border-radius: 8px; color: #fff;
  text-decoration: none; font-size: 15px; font-weight: 600;
  transition: transform 0.15s;
}
.col-chip:hover { transform: translateY(-2px); }
.chip-a { background: #e7bf62; }
.chip-b { background: #a4d4ac; }
.chip-c { background: #f4b3b3; }
.chip-d { background: #97d4dd; }
.chip-badge {
  position: absolute; top: 6px; right: 8px;
  background: #dc2626; color: #fff; font-size: 11px;
  min-width: 18px; height: 18px; padding: 0 5px; border-radius: 10px;
  display: inline-flex; align-items: center; justify-content: center;
  font-family: Georgia, serif;
}

/* 快捷 9 格 */
.quick-grid {
  display: grid; grid-template-columns: repeat(9, 1fr); gap: 10px;
  background: #fff; border-radius: 10px; padding: 20px; border: 1px solid #f1f5f9;
}
.qk-item {
  position: relative; text-decoration: none;
  display: flex; flex-direction: column; align-items: center; gap: 8px;
  padding: 12px 6px; border-radius: 8px;
  transition: background 0.15s;
}
.qk-item:hover { background: #f8fafc; }
.qk-icon {
  width: 44px; height: 44px; border-radius: 50%;
  display: inline-flex; align-items: center; justify-content: center;
  font-size: 22px;
}
.qk-label { font-size: 13px; color: #475569; }
.qk-item:hover .qk-label { color: var(--color-primary); }
.qk-hot {
  position: absolute; top: 4px; right: 14px;
  background: #dc2626; color: #fff; font-size: 11px;
  min-width: 18px; height: 18px; border-radius: 50%;
  display: inline-flex; align-items: center; justify-content: center;
}

/* 帮助卡 */
.help-card {
  display: flex; align-items: center; justify-content: space-between;
  background: linear-gradient(135deg, #f0f6fd, #e8f0fa);
  border-radius: 10px; padding: 16px 28px;
  border: 1px solid #dbeafe;
}
.help-visual { display: flex; align-items: center; }
.help-icon { font-size: 48px; }
.help-links { display: flex; flex-direction: column; gap: 6px; text-align: right; font-size: 14px; }
.help-row { display: flex; justify-content: flex-end; gap: 16px; }
.help-row a { color: #475569; text-decoration: none; transition: color 0.15s; }
.help-row a span { color: var(--color-primary); margin-left: 3px; }
.help-row a:hover { color: var(--color-primary); }

/* 特别提醒 */
.notice-box { background: #fff; border-radius: 10px; padding: 20px 28px; border: 1px solid #f1f5f9; }
.notice-title { font-size: 17px; font-weight: 700; color: #111827; margin-bottom: 12px; }
.notice-list { margin: 0; padding-left: 22px; list-style: decimal; color: #475569; font-size: 14px; line-height: 1.9; }
.notice-list li.warn { color: #b91c1c; font-weight: 500; }

@media (max-width: 768px) {
  .info-card { flex-direction: column; }
  .info-right { text-align: left; }
  .two-col { grid-template-columns: 1fr; }
  .quick-grid { grid-template-columns: repeat(3, 1fr); }
  .help-card { flex-direction: column; gap: 12px; padding: 16px; }
  .help-links { text-align: left; }
  .help-row { justify-content: flex-start; }
}
</style>
