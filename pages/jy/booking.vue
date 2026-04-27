<template>
<div class="booking-page">
  <Head>
    <Title>在线预约教员 - 591家教网</Title>
  </Head>
  <div class="booking-header">
    <NuxtLink to="/jy" class="back-link">&lt; 去教员库继续选择</NuxtLink>
  </div>

  <div class="booking-body">
    <!-- 左：主区 -->
    <div class="main-col">
      <el-tabs v-model="activeTab" stretch>
        <!-- ============ 尚未预约 ============ -->
        <el-tab-pane label="尚未预约" name="pending">
          <div v-if="!shortlist.length" class="empty-box">
            <p>备选列表空空如也</p>
            <NuxtLink to="/jy"><el-button type="primary">去挑选教员</el-button></NuxtLink>
          </div>
          <div v-else>
            <div v-for="t in shortlist" :key="t.id" class="tutor-row">
              <el-checkbox v-model="t.checked" />
              <NuxtLink :to="'/jy/t' + (t.displayNo || '').replace(/^T/i,'')" class="row-avatar">
                <img :src="t.avatar || '/placeholder/avatar.png'" alt="头像" />
              </NuxtLink>
              <NuxtLink :to="'/jy/t' + (t.displayNo || '').replace(/^T/i,'')" class="row-info">
                <div class="row-name">{{ t.realName ? t.realName.charAt(0) + '教员' : '教员' }} <span class="row-no">{{ t.displayNo }}</span></div>
                <div class="row-school">{{ t.university || '' }}</div>
              </NuxtLink>
              <div class="row-tail">
                <div class="row-date">{{ formatDate(t.addedAt) }}</div>
                <button class="row-del" @click="removeTutor(t.id)">删除</button>
              </div>
            </div>

            <div class="form-wrap">
              <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
                <el-form-item prop="contactName">
                  <el-input v-model="form.contactName" placeholder="请填写称谓。例如：张女士" />
                </el-form-item>
                <el-form-item prop="contactMobile">
                  <el-input v-model="form.contactMobile" placeholder="请输入手机号" />
                </el-form-item>
                <div class="wx-same-row">
                  <el-checkbox v-model="form.wechatSameAsMobile" @change="onSyncChange">微信与手机同号</el-checkbox>
                </div>
                <el-form-item prop="contactWechat" v-if="!form.wechatSameAsMobile">
                  <el-input v-model="form.contactWechat" placeholder="请输入微信号(非必填)" />
                </el-form-item>
                <el-form-item prop="need">
                  <el-input v-model="form.need" type="textarea" placeholder="请填写需求：请简要叙述" :rows="3" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="large" :loading="submitting" @click="handleSubmit" class="submit-btn">提交预约</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="bottom-line">© 591家教网 热线：{{ hotline }}</div>
          </div>
        </el-tab-pane>

        <!-- ============ 已预约记录 ============ -->
        <el-tab-pane label="已预约记录" name="history">
          <div v-if="loadingHistory" class="empty-box"><p>加载中…</p></div>
          <div v-else-if="!history.length" class="empty-box">
            <p>还没有预约过教员</p>
            <NuxtLink to="/jy"><el-button type="primary">去挑选教员</el-button></NuxtLink>
          </div>
          <div v-else>
            <div v-for="r in history" :key="r.id" class="history-row">
              <div class="history-line"><span class="lbl">教员：</span>{{ r.tutorName || r.tutorUserId }}</div>
              <div class="history-line"><span class="lbl">称谓：</span>{{ r.contactName || '-' }} | <span class="lbl">手机：</span>{{ r.contactMobile || '-' }}</div>
              <div class="history-line"><span class="lbl">需求：</span>{{ r.remark || '-' }}</div>
              <div class="history-line"><span class="lbl">状态：</span><el-tag :type="resTagType(r.resStatus)" size="small">{{ resLabel(r.resStatus) }}</el-tag>
                <span class="history-time">{{ formatDate(r.gmtCreate) }}</span>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 右：注意事项 -->
    <aside class="tips-col">
      <h3 class="tips-title">预约教员注意事项</h3>
      <ol class="tips-list">
        <li>您可以先"备选" <em>多个</em> 教员，然后 <em>一次性</em> 提交预约！</li>
        <li>您最好选择 <em>近期登录</em> 的教员，这样教员接单可能性更大一点，可以节省您宝贵的时间。</li>
        <li>您预约的教员，可能会因 <em>多种原因（如授课时间冲突、距离太远等）</em> 无法为您执教，我们会向您说明情况，并 <em>及时推荐同等水平教员</em>，供您选择。</li>
        <li>收到您的订单后，客服将在 <em>24小时</em> 内致电给您确认详情。</li>
        <li>您也可以致电 <em>{{ hotline }}</em>（热线时间：9:00 - 20:00）由客服直接预约。</li>
      </ol>
    </aside>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useSiteConfig } from '~/composables/useSiteConfig'

definePageMeta({ middleware: 'auth' })

const userStore = useUserStore()
const { post, get } = useApi()
const router = useRouter()
const route = useRoute()
const { config, load: loadSiteConfig } = useSiteConfig()

const STORAGE_KEY = 'pending_booking_tutors'

const activeTab = ref('pending')
const shortlist = ref([])
const history = ref([])
const loadingHistory = ref(false)
const submitting = ref(false)
const formRef = ref(null)

const hotline = computed(() => config.value.siteHotline || '13795420591')

const form = ref({
  contactName: '',
  contactMobile: userStore.mobile || '',
  contactWechat: '',
  wechatSameAsMobile: false,
  need: ''
})

const rules = {
  contactName: [{ required: true, message: '请填写称谓', trigger: 'blur' }, { min: 2, max: 8, message: '长度 2-8 字', trigger: 'blur' }],
  contactMobile: [{ required: true, pattern: /^1\d{10}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  need: [{ required: true, message: '请填写需求', trigger: 'blur' }]
}

const resLabel = (s) => ({ 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消' }[s] || '未知')
const resTagType = (s) => ({ 0: 'warning', 1: 'success', 2: '', 3: 'info' }[s] || 'info')

const formatDate = (s) => {
  if (!s) return ''
  if (typeof s === 'string') return s.slice(0, 10)
  try { return new Date(s).toISOString().slice(0, 10) } catch { return '' }
}

const onSyncChange = (val) => {
  if (val) form.value.contactWechat = form.value.contactMobile
}

const readStorage = () => {
  if (typeof localStorage === 'undefined') return []
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) : []
  } catch { return [] }
}
const writeStorage = (arr) => {
  if (typeof localStorage === 'undefined') return
  localStorage.setItem(STORAGE_KEY, JSON.stringify(arr))
}

async function loadShortlist() {
  const stored = readStorage()
  if (route.query.tutorId) {
    const id = String(route.query.tutorId)
    if (!stored.find(x => String(x.id) === id)) {
      stored.push({ id, addedAt: new Date().toISOString() })
      writeStorage(stored)
    }
  }
  // hydrate each entry with tutor data; keep entries on transient fetch failure
  const out = []
  for (const entry of stored) {
    try {
      const dn = entry.displayNo || entry.id
      const res = await get('/user/api/tutor/view', dn.toString().match(/^T/i) ? { displayNo: dn } : { id: entry.id })
      if (res?.code === 200 && res.data) {
        out.push({ ...res.data, addedAt: entry.addedAt, checked: true })
      } else {
        // 教员被删/审核驳回 -> 显式标记为不可选, 不提交
        out.push({ id: entry.id, displayNo: entry.displayNo, realName: '(已下架教员)', addedAt: entry.addedAt, checked: false, _unavailable: true })
      }
    } catch (_) {
      // 网络抖动 -> 保留占位, 不写回 storage 避免静默丢失
      out.push({ id: entry.id, displayNo: entry.displayNo, realName: '(加载失败, 请刷新)', addedAt: entry.addedAt, checked: false, _loadError: true })
    }
  }
  shortlist.value = out
  // 不重写 storage; 只有用户手动删除/成功提交才动 storage
}

const removeTutor = (id) => {
  shortlist.value = shortlist.value.filter(t => String(t.id) !== String(id))
  writeStorage(shortlist.value.map(t => ({ id: t.id, displayNo: t.displayNo, addedAt: t.addedAt })))
}

async function loadHistory() {
  loadingHistory.value = true
  try {
    const res = await post('/user/auth/reservation/page', { pageCurrent: 1, pageSize: 30 })
    if (res?.code === 200 && res.data?.list) history.value = res.data.list
  } catch (e) { console.error(e) }
  finally { loadingHistory.value = false }
}

async function handleSubmit() {
  const checked = shortlist.value.filter(t => t.checked)
  if (!checked.length) {
    ElMessage.warning('请至少勾选一位教员')
    return
  }
  let valid = false
  await formRef.value.validate(v => valid = v).catch(() => {})
  if (!valid) return
  if (form.value.wechatSameAsMobile) form.value.contactWechat = form.value.contactMobile

  submitting.value = true
  const okIds = []
  const errors = []
  try {
    for (const t of checked) {
      try {
        const res = await post('/user/auth/reservation/create', {
          tutorId: t.id,
          contactName: form.value.contactName,
          contactMobile: form.value.contactMobile,
          contactWechat: form.value.contactWechat,
          remark: form.value.need
        })
        if (res?.code === 200) okIds.push(String(t.id))
        else errors.push(`${t.realName || t.displayNo}: ${res?.msg || '失败'}`)
      } catch (e) {
        errors.push(`${t.realName || t.displayNo}: ${e?.response?.data?.msg || '网络错误'}`)
      }
    }
  } finally { submitting.value = false }

  if (okIds.length > 0) {
    shortlist.value = shortlist.value.filter(t => !okIds.includes(String(t.id)))
    writeStorage(shortlist.value.map(t => ({ id: t.id, displayNo: t.displayNo, addedAt: t.addedAt })))

    const msg = errors.length
      ? `成功提交 ${okIds.length} 条，以下失败：\n${errors.join('\n')}`
      : '提交成功，客服将在 24 小时内致电给您确认详情，请保持电话畅通！'
    try {
      await ElMessageBox.alert(msg, '提交结果', { confirmButtonText: '知道了' })
    } catch (_) {}
    if (errors.length === 0) {
      activeTab.value = 'history'
      await loadHistory()
    }
  } else {
    ElMessage.error(errors.length ? errors.join('；') : '提交失败')
  }
}

onMounted(async () => {
  loadSiteConfig()
  await loadShortlist()
  await loadHistory()
})
</script>

<style scoped>
.booking-page { background: #f6f7f9; min-height: 100vh; padding: 24px 0; }
.booking-header { max-width: 1200px; margin: 0 auto 16px; padding: 0 24px; }
.back-link { color: #1f2937; font-weight: 600; }
.back-link:hover { color: #f59e0b; }

.booking-body { max-width: 1200px; margin: 0 auto; display: grid; grid-template-columns: 3fr 1fr; gap: 16px; padding: 0 24px; }

.main-col { background: #fff; border-radius: 8px; padding: 16px 32px; min-height: 600px; }

.tips-col { background: #f5f5f5; border-radius: 8px; padding: 20px; height: fit-content; }
.tips-title { font-size: 16px; color: #1f2937; margin: 0 0 12px; }
.tips-list { padding-left: 18px; line-height: 1.9; color: #475569; font-size: 13px; }
.tips-list li { margin-bottom: 6px; }
.tips-list em { color: #f59e0b; font-style: normal; font-weight: 600; }

.empty-box { text-align: center; padding: 60px 0; color: #94a3b8; }
.empty-box p { margin-bottom: 16px; }

.tutor-row { display: flex; align-items: center; padding: 14px 0; border-bottom: 1px solid #f1f5f9; gap: 14px; }
.row-avatar img { width: 48px; height: 48px; border-radius: 50%; object-fit: cover; }
.row-info { flex: 1; line-height: 1.6; color: #1f2937; }
.row-name { font-size: 15px; font-weight: 600; }
.row-no { color: #94a3b8; font-weight: 400; font-size: 13px; margin-left: 6px; }
.row-school { color: #64748b; font-size: 13px; }
.row-tail { display: flex; flex-direction: column; align-items: flex-end; gap: 6px; }
.row-date { color: #94a3b8; font-size: 13px; }
.row-del { background: #f59e0b; color: #fff; border: none; padding: 4px 10px; border-radius: 4px; cursor: pointer; font-size: 12px; }
.row-del:hover { background: #d97706; }

.form-wrap { padding: 20px 60px; }
.wx-same-row { display: flex; align-items: center; color: #64748b; margin-bottom: 14px; }
.submit-btn { width: 100%; background: #f59e0b; border-color: #f59e0b; }
.submit-btn:hover, .submit-btn:focus { background: #d97706; border-color: #d97706; }

.bottom-line { text-align: center; color: #94a3b8; font-size: 12px; padding: 16px 0; }

.history-row { padding: 14px 0; border-bottom: 1px solid #f1f5f9; }
.history-line { line-height: 1.8; color: #334155; font-size: 14px; }
.history-line .lbl { color: #94a3b8; }
.history-time { color: #94a3b8; font-size: 12px; margin-left: 10px; }

@media (max-width: 768px) {
  .booking-body { grid-template-columns: 1fr; }
  .main-col { padding: 16px; }
  .form-wrap { padding: 12px 0; }
}
</style>
