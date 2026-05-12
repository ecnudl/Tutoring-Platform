<template>
<div class="msg-page">
  <div class="msg-header">
    <h2>消息中心</h2>
  </div>

  <!-- 两个 Tab -->
  <div class="msg-tabs">
    <div
      v-for="t in tabs" :key="t.key"
      class="tab" :class="{ active: activeTab === t.key }"
      @click="activeTab = t.key"
    >
      {{ t.label }}
    </div>
  </div>

  <!-- 系统消息 -->
  <div v-show="activeTab === 'system'" class="msg-list">
    <div v-if="loadingSystem" class="msg-empty">加载中…</div>
    <div v-else-if="!systemMessages.length" class="msg-empty">
      <div class="empty-ico">📭</div>
      <p>暂无系统消息</p>
    </div>
    <div v-for="(m, i) in systemMessages" :key="i" class="msg-item">
      <div :class="['tag', m.tagClass || 'tag-audit']">{{ m.tag }}</div>
      <div class="body">
        <div class="text">{{ m.text }}</div>
        <div class="time">{{ m.time }}</div>
      </div>
      <NuxtLink v-if="m.to" :to="m.to" class="more">详情 ›</NuxtLink>
    </div>
  </div>

  <!-- 网站公告 -->
  <div v-show="activeTab === 'notice'" class="msg-list">
    <div v-if="loadingNotice" class="msg-empty">加载中…</div>
    <div v-else-if="!notices.length" class="msg-empty">
      <div class="empty-ico">📢</div>
      <p>暂无网站公告</p>
    </div>
    <div v-for="n in notices" :key="n.id" class="msg-item" @click="openNotice(n)" style="cursor:pointer">
      <div class="tag tag-notice">告</div>
      <div class="body">
        <div class="text">
          <span v-if="isWithinDays(n.gmtCreate, 3)" class="new-badge">新</span>
          {{ n.title }}
        </div>
        <div class="time">{{ formatTime(n.gmtCreate) }}</div>
      </div>
      <span class="more">详情 ›</span>
    </div>
  </div>

  <!-- 公告详情弹窗 -->
  <el-dialog v-model="dlgVisible" :title="dlgItem?.title" width="560px">
    <div class="dlg-time">{{ formatTime(dlgItem?.gmtCreate) }}</div>
    <div class="dlg-content" v-if="dlgItem?.content">{{ dlgItem.content }}</div>
    <div class="dlg-content muted" v-else>暂无详细内容。可点击下方链接查看更多。</div>
    <template #footer>
      <el-button @click="dlgVisible = false">关闭</el-button>
      <el-button v-if="dlgItem?.linkUrl && !dlgItem.linkUrl.startsWith('/notice/')"
                 type="primary"
                 @click="goExternal(dlgItem.linkUrl)">查看详情</el-button>
    </template>
  </el-dialog>
</div>
</template>

<script setup>
definePageMeta({ layout: 'center', middleware: 'auth' })
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'
import { isWithinDays } from '~/composables/useNewBadge'

const userStore = useUserStore()
const { get, post } = useApi()
const router = useRouter()

const activeTab = ref('system')
const systemMessages = ref([])
const notices = ref([])
const loadingSystem = ref(false)
const loadingNotice = ref(false)
const dlgVisible = ref(false)
const dlgItem = ref(null)
const tabs = computed(() => [
  { key: 'system', label: '系统消息' },
  { key: 'notice', label: '网站公告' }
])

const formatTime = (s) => {
  if (!s) return ''
  if (typeof s === 'string' && /^\d{4}-/.test(s)) return s.replace('T', ' ').slice(0, 19)
  try { return new Date(s).toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-') } catch { return String(s) }
}

// —— 系统消息：从用户资料派生 ——
async function loadSystem() {
  loadingSystem.value = true
  systemMessages.value = []
  try {
    if (userStore.isTutor) {
      const res = await get('/user/auth/tutor-profile/view')
      if (res?.code === 200 && res.data) {
        const p = res.data
        const now = new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
        const s = p.auditStatus
        if (s === 0) {
          systemMessages.value.push({
            tag: '编', tagClass: 'tag-draft',
            text: `${p.realName || '您'}好，您的简历尚未提交审核，完善后请点击"提交审核"。`,
            time: now, to: '/center/tutor-profile'
          })
        } else if (s === 1) {
          systemMessages.value.push({
            tag: '审', tagClass: 'tag-audit',
            text: `${p.realName || '您'}教员您好：您的简历已提交，正在审核中，请耐心等待。`,
            time: now, to: '/center/tutor-profile'
          })
        } else if (s === 2 || s === 4) {
          systemMessages.value.push({
            tag: '审', tagClass: 'tag-ok',
            text: `${p.realName || '您'}教员您好：您的简历已通过审核。点击查看详情。`,
            time: now, to: '/center/tutor-profile'
          })
        } else if (s === 3) {
          systemMessages.value.push({
            tag: '驳', tagClass: 'tag-bad',
            text: `${p.realName || '您'}教员您好：您的简历未通过审核，请根据反馈修改后重新提交。`,
            time: now, to: '/center/tutor-profile'
          })
        }
        if (!p.avatar) {
          systemMessages.value.push({
            tag: '提', tagClass: 'tag-tip',
            text: '提示：您尚未上传头像，头像将显著提高家长咨询意愿。',
            time: now, to: '/center/head-photo'
          })
        }
        if (!p.selfIntroduction) {
          systemMessages.value.push({
            tag: '提', tagClass: 'tag-tip',
            text: '提示：您的"自我介绍"尚未填写，完善后可获得更多优质订单推荐。',
            time: now, to: '/center/tutor-profile'
          })
        }
      }
    } else {
      // 非教员：用户默认问候信息
      const now = new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
      systemMessages.value.push({
        tag: '迎', tagClass: 'tag-tip',
        text: '欢迎使用 591 家教网！您可在"请家教"页发布需求，或在"教员库"主动联系心仪教员。',
        time: now, to: '/qjj'
      })
    }
    // —— 真正的站内信（申请/审核状态变化）——
    try {
      const res = await post('/user/auth/msg/user/list', { pageCurrent: 1, pageSize: 30 })
      if (res?.code === 200 && res.data?.list) {
        for (const m of res.data.list) {
          systemMessages.value.unshift({
            tag: m.isRead ? '读' : '新',
            tagClass: m.isRead ? 'tag-tip' : 'tag-ok',
            text: (m.msgTitle ? (m.msgTitle + '：') : '') + (m.msgText || ''),
            time: formatTime(m.gmtCreate)
          })
        }
      }
    } catch (e) { /* ignore msg errors */ }
  } catch (e) { /* ignore */ }
  finally { loadingSystem.value = false }
}

// —— 网站公告：读 sys_announcement category=notice ——
async function loadNotices() {
  loadingNotice.value = true
  try {
    const res = await get('/system/api/announcement/list', { category: 'notice', limit: 50 })
    if (res?.code === 200 && Array.isArray(res.data)) {
      notices.value = res.data
    }
  } catch (e) {}
  finally { loadingNotice.value = false }
}

const openNotice = (n) => {
  dlgItem.value = n
  dlgVisible.value = true
}
const goExternal = (url) => {
  window.location.href = url
}

onMounted(async () => {
  await loadSystem()
  await loadNotices()
  // 后端 mark-all-read 仍然调一下 (清后端 unread, 即便前端不再显示 badge)
  try { await post('/user/auth/msg/user/mark-all-read', {}) } catch (_) {}
})
</script>

<style scoped>
.msg-page { background: #fff; border-radius: 12px; padding: 0; box-shadow: 0 2px 12px rgba(0,0,0,0.04); border: 1px solid #f1f5f9; overflow: hidden; }

.msg-header {
  padding: 22px 28px 12px;
  text-align: right;
}
.msg-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #111827;
  margin: 0;
  letter-spacing: 2px;
}

/* Tabs */
.msg-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  border-bottom: 2px solid #f1f5f9;
  position: relative;
}
.tab {
  position: relative;
  padding: 14px 0;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: color 0.15s;
}
.tab:hover { color: var(--color-primary); }
.tab.active {
  color: var(--color-primary);
}
.tab.active::after {
  content: '';
  position: absolute;
  left: 20%;
  right: 20%;
  bottom: -2px;
  height: 2px;
  background: var(--color-primary, #163B6B);
}
.tab-badge {
  display: inline-block;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  margin-left: 6px;
  background: #dc2626;
  color: #fff;
  font-size: 11px;
  border-radius: 10px;
  line-height: 18px;
}

/* 列表 */
.msg-list { padding: 10px 28px 24px; }
.msg-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 8px;
  border-bottom: 1px dashed #eef2f7;
}
.msg-item:last-child { border-bottom: none; }

.tag {
  flex-shrink: 0;
  width: 36px; height: 36px;
  border-radius: 50%;
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  display: inline-flex; align-items: center; justify-content: center;
}
.tag-audit  { background: #059669; }
.tag-ok     { background: #059669; }
.tag-bad    { background: #dc2626; }
.tag-draft  { background: #6b7280; }
.tag-notice { background: var(--color-primary, #163B6B); }
.tag-tip    { background: #d97706; }

.body { flex: 1; min-width: 0; }
.text {
  font-size: 14px;
  color: #334155;
  line-height: 1.7;
  margin-bottom: 6px;
}
.new-badge {
  display: inline-block;
  background: #ef4444;
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 5px;
  border-radius: 6px;
  margin-right: 4px;
  vertical-align: middle;
  letter-spacing: 0;
  line-height: 1.4;
}
.time {
  display: inline-block;
  background: #dc2626;
  color: #fff;
  font-size: 12px;
  font-family: Georgia, serif;
  padding: 2px 10px;
  border-radius: 4px;
}

.more {
  flex-shrink: 0;
  font-size: 14px;
  color: var(--color-primary, #163B6B);
  text-decoration: none;
  font-weight: 600;
  white-space: nowrap;
  transition: opacity 0.15s;
}
.more:hover { opacity: 0.75; }

.msg-empty {
  text-align: center; padding: 60px 20px;
  color: #94a3b8; font-size: 14px;
}
.empty-ico { font-size: 48px; margin-bottom: 10px; }

.dlg-time {
  font-size: 12px; color: #94a3b8; margin-bottom: 12px;
  padding-bottom: 12px; border-bottom: 1px solid #f1f5f9;
}
.dlg-content {
  font-size: 14px; color: #334155; line-height: 1.8; white-space: pre-wrap;
}
.dlg-content.muted { color: #94a3b8; font-style: italic; }
</style>
