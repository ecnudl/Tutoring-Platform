<template>
<div class="xa-page">
  <Head>
    <Title>A{{ displayNoNum }} 学员需求 - 591家教网</Title>
    <Meta name="description" :content="`A${displayNoNum}学员需求详情，区域 / 年级 / 科目 / 预算 / 上课要求。`" />
  </Head>

  <!-- 面包屑 -->
  <div class="container xa-crumb">
    <NuxtLink to="/">首页</NuxtLink>
    <span class="sep">›</span>
    <NuxtLink to="/xy">学员库</NuxtLink>
    <span class="sep">›</span>
    <span class="cur">A{{ displayNoNum }}</span>
  </div>

  <div v-loading="loading">

    <!-- ============ MATCHED · 已接单全页 ============ -->
    <div v-if="state === 'matched'" class="container xa-state-screen">
      <div class="state-card">
        <div class="state-mark matched">
          <svg viewBox="0 0 64 64" width="56" height="56" aria-hidden="true">
            <circle cx="32" cy="32" r="28" fill="none" stroke="currentColor" stroke-width="2.5"/>
            <path d="M20 33 L29 42 L45 24" fill="none" stroke="currentColor" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <p class="state-eyebrow">需求 A{{ displayNoNum }}</p>
        <h2 class="state-title">该需求已接单</h2>
        <p class="state-desc">感谢您的关注。客服已为这位学员安排了合适的教员，本订单不再公开接受新申请。</p>
        <p class="state-tip">还可以为您匹配类似订单 — 在学员库继续挑选，或直接联系客服。</p>
        <div class="state-actions">
          <NuxtLink to="/xy"><el-button type="primary">浏览其他需求</el-button></NuxtLink>
          <el-button @click="showCs = true">联系客服</el-button>
        </div>
      </div>
    </div>

    <!-- ============ 找不到/不可查看 ============ -->
    <div v-else-if="state === 'missing'" class="container xa-state-screen">
      <div class="state-card">
        <div class="state-mark missing">
          <svg viewBox="0 0 64 64" width="48" height="48" aria-hidden="true">
            <circle cx="32" cy="32" r="28" fill="none" stroke="currentColor" stroke-width="2.5"/>
            <path d="M22 24 q 10 -10 20 0 q 0 8 -10 12 v 4" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round"/>
            <circle cx="32" cy="48" r="2.5" fill="currentColor"/>
          </svg>
        </div>
        <p class="state-eyebrow">需求 A{{ displayNoNum }}</p>
        <h2 class="state-title">未找到该需求</h2>
        <p class="state-desc">需求可能已下架、已关闭或暂未通过审核。请返回学员库挑选其他订单。</p>
        <div class="state-actions">
          <NuxtLink to="/xy"><el-button type="primary">返回学员库</el-button></NuxtLink>
        </div>
      </div>
    </div>

    <!-- ============ PUBLISHED · 卷宗主体 ============ -->
    <div v-else-if="state === 'open' && req" class="container xa-grid">

      <!-- LEFT · 主区 -->
      <article class="xa-main">

        <!-- Case Header -->
        <header class="xa-header">
          <div class="xa-pills">
            <span class="pill pill-region" v-if="locationLabel">{{ locationLabel }}</span>
            <span class="pill pill-grade" v-if="req.gradeName">{{ req.gradeName }}</span>
            <span class="pill pill-subject" v-if="subjectsLabel">{{ subjectsLabel }}</span>
            <span class="pill pill-mode" v-if="req.teachingMethod">{{ teachingMethodMap[req.teachingMethod] }}</span>
          </div>

          <h1 class="xa-title">{{ req.title || '家教需求' }}</h1>

          <div class="xa-meta-row">
            <div class="xa-no-stamp">
              <span class="lbl">订单</span>
              <span class="num">A<em>{{ displayNoNum }}</em></span>
            </div>
            <div class="xa-meta-side">
              <span><svg viewBox="0 0 24 24" width="13" height="13" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg> {{ req.viewCount || 0 }}</span>
              <span><svg viewBox="0 0 24 24" width="13" height="13" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg> {{ shortDate(req.gmtCreate) }}</span>
            </div>
          </div>

          <div class="xa-rule" aria-hidden="true"></div>
        </header>

        <!-- 学员信息 -->
        <section class="xa-block">
          <h3 class="xa-h">
            <span class="bullet"></span>学员信息
          </h3>
          <dl class="xa-dl">
            <div class="xa-row"><dt>订单编号</dt><dd class="mono">A{{ displayNoNum }}</dd></div>
            <div class="xa-row"><dt>学员性别</dt><dd>{{ genderLabel(req.studentGender) }}</dd></div>
            <div class="xa-row"><dt>联系人</dt><dd>{{ req.contactName || '—' }}</dd></div>
            <div class="xa-row"><dt>所在年级</dt><dd>{{ req.gradeName || '—' }}</dd></div>
            <div class="xa-row"><dt>所在区域</dt><dd>{{ locationLabel || '—' }}</dd></div>
            <div class="xa-row"><dt>大致位置</dt><dd>{{ req.displayLocation || req.address || '面议' }}</dd></div>
            <div class="xa-row" v-if="req.transport"><dt>交通线路</dt><dd>{{ req.transport }}</dd></div>
          </dl>
        </section>

        <!-- 家教信息 -->
        <section class="xa-block">
          <h3 class="xa-h">
            <span class="bullet"></span>家教信息
          </h3>
          <dl class="xa-dl">
            <div class="xa-row"><dt>求教科目</dt><dd>{{ subjectsLabel || '—' }}</dd></div>
            <div class="xa-row" v-if="req.requirementDetail"><dt>学员情况</dt><dd>{{ req.requirementDetail }}</dd></div>
            <div class="xa-row" v-if="req.frequency"><dt>频　　次</dt><dd>{{ req.frequency }}</dd></div>
            <div class="xa-row"><dt>辅导时间</dt><dd>{{ req.schedule || '—' }}</dd></div>
            <div class="xa-row" v-if="req.requirementType"><dt>求教性质</dt><dd>{{ requirementTypeMap[req.requirementType] || '—' }}</dd></div>
            <div class="xa-row"><dt>性别要求</dt><dd>{{ genderLabel(req.tutorGender, '不限') }}</dd></div>
            <div class="xa-row" v-if="req.otherRequirements"><dt>其它要求</dt><dd>{{ req.otherRequirements }}</dd></div>
            <div class="xa-row xa-row-budget">
              <dt>报　　酬</dt>
              <dd>
                <span v-if="req.budgetMin || req.budgetMax" class="budget">
                  <em>{{ req.budgetMin || 0 }}</em>
                  <span v-if="req.budgetMax">~ <em>{{ req.budgetMax }}</em></span>
                  <span class="unit">元/小时</span>
                </span>
                <span v-else>面议</span>
              </dd>
            </div>
            <div class="xa-row"><dt>教学方式</dt><dd>{{ teachingMethodMap[req.teachingMethod] || '不限' }}</dd></div>
            <div class="xa-row" v-if="req.transportSubsidy"><dt>有无车贴</dt><dd>{{ req.transportSubsidy }}</dd></div>
          </dl>
        </section>

        <!-- 行动条 -->
        <div class="xa-action-bar">
          <button class="xa-cta" @click="goApply">
            <span>申请订单</span>
            <svg viewBox="0 0 24 24" width="18" height="18"><path d="M5 12h14M13 6l6 6-6 6" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/></svg>
          </button>
          <span class="xa-applied">已有 <strong>{{ req.applicationCount || 0 }}</strong> 人申请</span>
          <NuxtLink to="/xy" class="xa-back">‹ 返回学员库</NuxtLink>
        </div>

        <p class="xa-apply-tip">注：申请订单后，若条件匹配，客服会在 1-2 个工作日与您联系；若超过 2 个工作日，请申请其他订单。</p>

        <!-- 相关订单 -->
        <section class="xa-related" v-if="related.length">
          <h3 class="xa-h"><span class="bullet"></span>相关订单</h3>
          <NuxtLink v-for="r in related" :key="r.id" :to="'/xy/a' + (r.displayNo || '').replace(/^A/i, '')" class="xa-rel-item">
            <div class="rel-head">
              <span class="rel-title">{{ r.title || '家教需求' }}</span>
              <span class="rel-no mono">{{ r.displayNo }}</span>
            </div>
            <div class="rel-meta">
              <span v-if="r.address">{{ r.address }}</span>
              <span class="rel-budget" v-if="r.budgetMin || r.budgetMax">
                {{ r.budgetMin || 0 }}<span v-if="r.budgetMax">-{{ r.budgetMax }}</span> 元/h
              </span>
              <span class="rel-budget" v-else>面议</span>
            </div>
          </NuxtLink>
        </section>
      </article>

      <!-- RIGHT · 客服 + 小贴士 -->
      <aside class="xa-aside">

        <div class="xa-cs-card">
          <div class="cs-stamp">591 · 客服直通</div>
          <h4 class="cs-title">想接这单？</h4>
          <p class="cs-sub">直接联系客服报名，1-2 个工作日内致电您并安排试讲匹配。</p>
          <ul class="cs-list">
            <li>
              <span class="ic"><svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg></span>
              <div>
                <div class="cs-lbl">客服热线</div>
                <a :href="`tel:${csHotline}`" class="cs-val">{{ csHotline }}</a>
                <div class="cs-fine">9:00 — 20:00</div>
              </div>
            </li>
            <li>
              <span class="ic"><svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg></span>
              <div>
                <div class="cs-lbl">客服微信</div>
                <span class="cs-val mono">{{ csWechat }}</span>
                <div class="cs-fine">备注「接单 + 编号 A{{ displayNoNum }}」</div>
              </div>
            </li>
          </ul>
          <button class="cs-modal-btn" @click="showCs = true">
            <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12" y2="8"/></svg>
            查看复制提示
          </button>
        </div>

        <div class="xa-tips-card">
          <h4 class="tips-title">
            <span class="tips-tag">小贴士</span>
            为什么走客服匹配？
          </h4>
          <ol class="tips-list">
            <li><strong>双向核验</strong> — 学员信息逐单核实，避免假单浪费您的时间。</li>
            <li><strong>续单 100% 归您</strong> — 中介费一次性 (等同首次课时费); 续单课时费由家长直接支付给您, 不经平台。</li>
            <li><strong>分发更公平</strong> — 客服按学员需求与教员条件做匹配，不是先到先得。</li>
          </ol>
        </div>
      </aside>
    </div>
  </div>

  <!-- 联系客服 模态 -->
  <el-dialog v-model="showCs" :show-close="false" width="380px" align-center class="xa-cs-dialog">
    <template #header>
      <div class="dlg-head">
        <div class="dlg-stamp">CONTACT</div>
        <h3>联系客服接单</h3>
      </div>
    </template>
    <p class="dlg-lead">看到合适的需求，请联系客服报名。客服核验您的资质后会致电与您匹配。</p>
    <div class="dlg-row">
      <span class="dlg-lbl">客服微信</span>
      <span class="dlg-val mono">{{ csWechat }}</span>
    </div>
    <div class="dlg-row">
      <span class="dlg-lbl">客服电话</span>
      <a :href="`tel:${csHotline}`" class="dlg-val">{{ csHotline }}</a>
    </div>
    <div class="dlg-row dlg-row-no">
      <span class="dlg-lbl">提及编号</span>
      <span class="dlg-val mono">A{{ displayNoNum }}</span>
    </div>
    <p class="dlg-fine">为保障双方权益，平台不在站内放学员联系方式，所有匹配走客服。</p>
    <template #footer>
      <el-button @click="showCs = false">关闭</el-button>
      <el-button type="primary" @click="copyContact">复制全部</el-button>
    </template>
  </el-dialog>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useSiteConfig } from '~/composables/useSiteConfig'

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { get } = useApi()

const displayNo = route.params.id
const displayNoNum = computed(() => String(displayNo).replace(/^A/i, ''))

const req = ref(null)
const related = ref([])
const loading = ref(true)
const state = ref('loading')          // 'open' | 'matched' | 'missing' | 'loading'
const showCs = ref(false)

const { config, load: loadSiteConfig } = useSiteConfig()
const csWechat = computed(() => config.value.siteCsWechat || '591jiajiao')
const csHotline = computed(() => config.value.siteHotline || '13795420591')

const genderMap = { 1: '男', 2: '女' }
const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const teachingMethodMap = { 1: '教师上门', 2: '学员上门', 3: '在线辅导', 4: '均可' }
const requirementTypeMap = { 1: '提高型', 2: '同步辅导', 3: '竞赛', 4: '考前冲刺', 5: '陪学', 6: '其他' }

const genderLabel = (g, defaultLbl = '不限') => {
  if (g === 1) return '男'
  if (g === 2) return '女'
  return defaultLbl
}

const subjectsLabel = computed(() => req.value?.subjectNames || req.value?.subjectIds || '')
const locationLabel = computed(() => {
  if (!req.value) return ''
  const city = req.value.cityName || (req.value.cityId === '1' || req.value.cityId === 1 ? '上海' : '')
  const dist = req.value.districtName || ''
  return [city, dist].filter(Boolean).join(' · ')
})

const shortDate = (s) => {
  if (!s) return ''
  return String(s).slice(0, 10)
}

const loadReq = async () => {
  loading.value = true
  try {
    const r = await get('/user/api/requirement/view', { displayNo: 'A' + displayNoNum.value })
    if (r.code === 200 && r.data) {
      req.value = r.data
      state.value = (r.data.reqStatus === 3) ? 'matched' : 'open'
    } else {
      state.value = 'missing'
    }
  } catch (e) {
    console.error(e)
    state.value = 'missing'
  } finally {
    loading.value = false
  }
}

const loadRelated = async () => {
  try {
    const r = await get('/user/api/requirement/related', { displayNo: 'A' + displayNoNum.value, limit: 5 })
    if (r.code === 200 && Array.isArray(r.data)) {
      related.value = r.data
    }
  } catch (e) { /* ignore */ }
}

const goApply = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录教员账号')
    router.push('/login?redirect=' + encodeURIComponent(`/xy/apply/A${displayNoNum.value}`))
    return
  }
  if (!userStore.isTutor) {
    ElMessage.warning('该入口仅教员可用，请使用教员账号登录')
    return
  }
  router.push(`/xy/apply/A${displayNoNum.value}`)
}

const copyContact = async () => {
  const text = `客服微信 ${csWechat.value}\n客服电话 ${csHotline.value}\n订单编号 A${displayNoNum.value}\n（591家教网）`
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('客服联系方式已复制')
  } catch {
    ElMessage.warning('复制失败，请手动抄写')
  }
}

onMounted(async () => {
  loadSiteConfig()
  await loadReq()
  loadRelated()
})
</script>

<style scoped>
/* =====================================================
   591家教网 · 学员需求详情 (卷宗 / dossier 风)
   navy + amber, 编辑性, 信息密度受控
   ===================================================== */

.xa-page { padding-bottom: 80px; min-height: 70vh; }

/* ===== Breadcrumb ===== */
.xa-crumb {
  margin-top: 18px;
  font-size: 13px;
  color: var(--color-text-secondary);
}
.xa-crumb a { color: var(--color-text-secondary); transition: color .15s; }
.xa-crumb a:hover { color: var(--color-primary); }
.xa-crumb .sep { margin: 0 8px; color: var(--color-text-muted); }
.xa-crumb .cur {
  color: var(--color-primary);
  font-weight: 600;
  font-family: ui-monospace, "SF Mono", "Cascadia Code", Consolas, monospace;
  letter-spacing: 1px;
}

/* ===== Grid ===== */
.xa-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 24px;
  margin-top: 16px;
}

/* ===== Main column ===== */
.xa-main {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: 14px;
  padding: 36px 44px 32px;
  box-shadow: 0 8px 24px -16px rgba(31, 78, 140, 0.18);
  animation: xa-rise 0.5s cubic-bezier(0.22, 1, 0.36, 1) both;
}

/* ----- Header ----- */
.xa-pills {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 14px;
}
.pill {
  display: inline-block;
  font-size: 12px;
  letter-spacing: 1px;
  padding: 4px 12px;
  border-radius: 4px;
  background: var(--color-primary-lighter);
  color: var(--color-primary);
  font-weight: 500;
}
.pill-grade { background: rgba(245, 158, 11, 0.12); color: var(--color-accent-dark); }
.pill-subject { background: rgba(46, 125, 50, 0.10); color: var(--color-success); }
.pill-mode {
  background: transparent;
  border: 1px dashed var(--color-border);
  color: var(--color-text-secondary);
}

.xa-title {
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 2px;
  color: var(--color-text);
  margin: 0 0 12px;
  line-height: 1.3;
}

.xa-meta-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 6px;
}
.xa-no-stamp {
  display: inline-flex;
  align-items: baseline;
  gap: 8px;
  padding: 4px 14px 6px;
  border: 1.5px solid var(--color-primary);
  border-radius: 4px;
  background: #fff;
  position: relative;
}
.xa-no-stamp .lbl {
  font-size: 11px;
  color: var(--color-primary);
  letter-spacing: 4px;
  font-weight: 600;
}
.xa-no-stamp .num {
  font-family: 'EB Garamond', Georgia, serif;
  font-size: 20px;
  font-weight: 700;
  color: var(--color-primary-dark);
  letter-spacing: 1px;
}
.xa-no-stamp .num em {
  font-style: normal;
  font-feature-settings: 'tnum' 1;
}
.xa-meta-side {
  display: inline-flex;
  gap: 18px;
  font-size: 12.5px;
  color: var(--color-text-secondary);
}
.xa-meta-side span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.xa-meta-side svg { color: var(--color-text-muted); }

.xa-rule {
  height: 1px;
  background: linear-gradient(to right, var(--color-accent) 0, var(--color-accent) 36px, var(--color-border-light) 36px, var(--color-border-light) 100%);
  margin: 22px 0 0;
}

/* ----- Section blocks ----- */
.xa-block { margin-top: 26px; }
.xa-h {
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 4px;
  color: var(--color-text);
  margin: 0 0 14px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.xa-h .bullet {
  display: inline-block;
  width: 14px; height: 2px;
  background: var(--color-primary);
  border-radius: 2px;
}

.xa-dl {
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 32px;
  row-gap: 0;
}
.xa-row {
  display: grid;
  grid-template-columns: 84px 1fr;
  gap: 14px;
  padding: 11px 0;
  border-bottom: 1px dashed var(--color-border-light);
  font-size: 14px;
}
.xa-row dt {
  color: var(--color-text-muted);
  font-weight: 400;
  letter-spacing: 1px;
  font-size: 13px;
}
.xa-row dd {
  margin: 0;
  color: var(--color-text);
  font-weight: 500;
}
.xa-row-budget { grid-column: 1 / -1; }
.xa-row-budget .budget { display: inline-flex; align-items: baseline; gap: 6px; }
.xa-row-budget .budget em {
  font-style: normal;
  font-size: 22px;
  font-weight: 700;
  color: var(--color-accent-dark);
  font-family: 'EB Garamond', Georgia, serif;
  letter-spacing: 1px;
}
.xa-row-budget .budget .unit {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-left: 4px;
}

/* ----- Quote block ----- */
.xa-quote-block { margin-top: 28px; }
.xa-quote {
  position: relative;
  background: var(--color-bg);
  border-left: 3px solid var(--color-primary);
  padding: 18px 24px 18px 26px;
  border-radius: 0 8px 8px 0;
  margin: 0;
  font-size: 14.5px;
  color: var(--color-text);
  line-height: 1.85;
  letter-spacing: 0.4px;
}
.xa-quote .qmark {
  font-size: 22px;
  color: var(--color-primary-light);
  font-weight: 700;
  display: inline-block;
  vertical-align: -2px;
}
.xa-quote .qmark.left { margin-right: 4px; }
.xa-quote .qmark.right { margin-left: 4px; }

/* ----- Action bar ----- */
.xa-action-bar {
  margin-top: 32px;
  padding-top: 22px;
  border-top: 1px solid var(--color-border-light);
  display: flex;
  align-items: center;
  gap: 16px;
}

.xa-cta {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  background: var(--color-primary);
  color: #fff;
  border: none;
  padding: 14px 36px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 4px;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.2s ease;
  box-shadow: 0 6px 20px -10px rgba(31, 78, 140, 0.55);
}
.xa-cta::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(120deg, transparent 30%, rgba(252, 211, 77, 0.28) 50%, transparent 70%);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}
.xa-cta:hover {
  background: var(--color-primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 12px 30px -12px rgba(31, 78, 140, 0.65);
}
.xa-cta:hover::after { transform: translateX(100%); }
.xa-cta svg { transition: transform .2s; }
.xa-cta:hover svg { transform: translateX(3px); }

.xa-back {
  font-size: 13px;
  color: var(--color-text-secondary);
  letter-spacing: 1px;
  transition: color .15s;
  margin-left: auto;
}
.xa-back:hover { color: var(--color-primary); }

.xa-applied {
  font-size: 13px;
  color: var(--color-text-secondary);
  letter-spacing: 0.5px;
}
.xa-applied strong {
  color: #d97706;
  font-family: Georgia, serif;
  font-weight: 700;
  font-size: 16px;
  margin: 0 2px;
}

.xa-apply-tip {
  margin-top: 12px;
  font-size: 12.5px;
  color: var(--color-text-muted);
  line-height: 1.7;
  letter-spacing: 0.3px;
}

/* ----- Related orders ----- */
.xa-related {
  margin-top: 36px;
  padding-top: 24px;
  border-top: 1px dashed var(--color-border-light);
}
.xa-rel-item {
  display: block;
  padding: 14px 18px;
  margin-top: 10px;
  background: #f8fafc;
  border-left: 3px solid var(--color-primary, #163B6B);
  border-radius: 6px;
  text-decoration: none;
  transition: background 0.2s, transform 0.2s;
}
.xa-rel-item:hover {
  background: #f0f6fd;
  transform: translateX(2px);
}
.rel-head {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 4px;
}
.rel-title {
  font-size: 14px;
  color: #111827;
  font-weight: 600;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.rel-no {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12px;
  color: var(--color-primary, #163B6B);
  letter-spacing: 0.5px;
  flex-shrink: 0;
}
.rel-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  font-size: 12.5px;
  color: var(--color-text-secondary);
}
.rel-meta span { padding: 1px 0; }
.rel-budget {
  color: #d97706;
  font-weight: 600;
  font-family: Georgia, serif;
}

/* ----- mono helper ----- */
.mono {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  letter-spacing: 0.5px;
  color: var(--color-primary, #163B6B);
}

/* ===== Aside ===== */
.xa-aside {
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: sticky;
  top: 20px;
  align-self: start;
}

.xa-cs-card {
  background: linear-gradient(155deg, var(--color-primary-dark) 0%, var(--color-primary) 100%);
  color: #fff;
  border-radius: 14px;
  padding: 26px 24px 22px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 14px 30px -16px rgba(31, 78, 140, 0.55);
}
.xa-cs-card::before {
  content: "";
  position: absolute;
  inset: -40% -20% auto auto;
  width: 240px;
  height: 240px;
  background: radial-gradient(circle, rgba(252, 211, 77, 0.25), transparent 60%);
  pointer-events: none;
}
.cs-stamp {
  display: inline-block;
  font-size: 11px;
  letter-spacing: 5px;
  color: #FCD34D;
  border: 1px solid rgba(252, 211, 77, 0.5);
  padding: 3px 10px;
  border-radius: 4px;
  margin-bottom: 16px;
}
.cs-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 6px;
  letter-spacing: 2px;
}
.cs-sub {
  margin: 0 0 18px;
  font-size: 13px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.78);
}
.cs-list {
  list-style: none;
  margin: 0 0 18px;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.cs-list li {
  display: grid;
  grid-template-columns: 24px 1fr;
  gap: 10px;
  align-items: flex-start;
  padding-bottom: 14px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.cs-list li:last-child { border-bottom: 0; padding-bottom: 0; }
.cs-list .ic {
  color: #FCD34D;
  margin-top: 2px;
}
.cs-lbl {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.55);
  letter-spacing: 2px;
  margin-bottom: 2px;
}
.cs-val {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
  display: inline-block;
}
.cs-val.mono {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
}
a.cs-val:hover { color: #FCD34D; }
.cs-fine {
  font-size: 11.5px;
  color: rgba(255, 255, 255, 0.55);
  letter-spacing: 0.5px;
  margin-top: 4px;
}
.cs-modal-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: rgba(252, 211, 77, 0.12);
  border: 1px solid rgba(252, 211, 77, 0.4);
  color: #FCD34D;
  padding: 7px 14px;
  border-radius: 6px;
  font-size: 12.5px;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all .15s;
}
.cs-modal-btn:hover {
  background: rgba(252, 211, 77, 0.22);
  border-color: #FCD34D;
}

/* ----- Tips card ----- */
.xa-tips-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: 14px;
  padding: 22px 22px 20px;
  position: relative;
}
.tips-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text);
  margin: 0 0 14px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.tips-tag {
  font-size: 11px;
  letter-spacing: 2px;
  color: #fff;
  background: var(--color-accent);
  padding: 3px 8px;
  border-radius: 3px;
  font-weight: 600;
}
.tips-list {
  margin: 0;
  padding-left: 18px;
  font-size: 13px;
  line-height: 1.95;
  color: var(--color-text-secondary);
  counter-reset: tip;
}
.tips-list li { padding-left: 4px; margin-bottom: 4px; }
.tips-list strong {
  color: var(--color-primary-dark);
  font-weight: 600;
  margin-right: 4px;
}
.tips-list li::marker {
  color: var(--color-accent);
  font-weight: 700;
}

/* ===== State screens (matched / missing) ===== */
.xa-state-screen {
  margin-top: 40px;
}
.state-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: 16px;
  padding: 56px 32px 48px;
  max-width: 520px;
  margin: 0 auto;
  text-align: center;
  box-shadow: 0 8px 28px -18px rgba(31, 78, 140, 0.25);
  animation: xa-rise 0.5s cubic-bezier(0.22, 1, 0.36, 1) both;
}
.state-mark {
  width: 96px; height: 96px;
  border-radius: 50%;
  margin: 0 auto 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.state-mark.matched {
  background: rgba(245, 158, 11, 0.12);
  color: var(--color-accent-dark);
}
.state-mark.matched::before {
  content: "";
  position: absolute;
  inset: -6px;
  border: 2px dashed var(--color-accent);
  border-radius: 50%;
  opacity: 0.55;
}
.state-mark.missing {
  background: var(--color-bg);
  color: var(--color-text-muted);
  border: 1px solid var(--color-border);
}
.state-eyebrow {
  font-size: 12px;
  letter-spacing: 4px;
  color: var(--color-text-muted);
  margin: 0 0 8px;
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
}
.state-title {
  font-size: 24px;
  font-weight: 700;
  letter-spacing: 4px;
  color: var(--color-text);
  margin: 0 0 12px;
}
.state-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.85;
  margin: 0 0 8px;
  padding: 0 16px;
}
.state-tip {
  font-size: 13px;
  color: var(--color-text-muted);
  margin: 0 0 28px;
}
.state-actions { display: flex; gap: 12px; justify-content: center; }

/* ===== Dialog (复用 contact 模态) ===== */
:deep(.xa-cs-dialog .el-dialog__header) { padding: 0; margin: 0; }
:deep(.xa-cs-dialog .el-dialog__body) { padding: 0 24px 8px; }
.dlg-head {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%);
  color: #fff;
  padding: 22px 24px 18px;
  border-radius: 8px 8px 0 0;
  margin: 0 -24px 16px;
}
.dlg-stamp {
  font-size: 11px;
  letter-spacing: 6px;
  color: #FCD34D;
  margin-bottom: 4px;
}
.dlg-head h3 {
  font-size: 18px;
  margin: 0;
  letter-spacing: 2px;
}
.dlg-lead {
  font-size: 13.5px;
  color: var(--color-text-secondary);
  line-height: 1.75;
  margin: 0 0 14px;
}
.dlg-row {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  padding: 10px 0;
  border-bottom: 1px dashed var(--color-border-light);
  font-size: 14px;
}
.dlg-row:last-of-type { border-bottom: 0; }
.dlg-lbl { color: var(--color-text-muted); letter-spacing: 1px; font-size: 13px; }
.dlg-val { color: var(--color-primary-dark); font-weight: 600; }
.dlg-val.mono { font-family: ui-monospace, "SF Mono", Consolas, monospace; }
.dlg-row-no .dlg-val { color: var(--color-accent-dark); }
.dlg-fine {
  font-size: 12px;
  color: var(--color-text-muted);
  margin: 12px 0 0;
  line-height: 1.6;
  padding: 10px 12px;
  background: var(--color-bg);
  border-radius: 6px;
}

/* ===== Animations ===== */
@keyframes xa-rise {
  from { opacity: 0; transform: translateY(14px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ===== Responsive ===== */
@media (max-width: 980px) {
  .xa-grid { grid-template-columns: 1fr; }
  .xa-aside { position: static; }
  .xa-main { padding: 28px 24px; }
}
@media (max-width: 560px) {
  .xa-dl { grid-template-columns: 1fr; }
  .xa-row { grid-template-columns: 76px 1fr; }
  .xa-title { font-size: 22px; letter-spacing: 1px; }
  .xa-cta { padding: 12px 22px; letter-spacing: 2px; font-size: 14px; }
  .state-card { padding: 40px 22px 36px; }
  .state-title { font-size: 20px; letter-spacing: 2px; }
}
</style>
