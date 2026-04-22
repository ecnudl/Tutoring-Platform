<template>
<div>
  <h2 class="page-title">证件认证</h2>

  <el-alert
    v-if="!userStore.isTutor"
    type="info" show-icon :closable="false"
    style="margin-bottom:16px"
    title="证件认证仅对教员账号开放"
    description="当前账号是家长/学员，没有教员资料。若你是教员，请用教员账号登录后再上传证件。"
  />

  <div class="cert-tip">
    <el-icon :size="18" color="#f59e0b"><InfoFilled /></el-icon>
    <span>
      <strong>认证通过</strong>将获得 <span class="verified-chip">√ 证件已认证</span> 标识，大大提升<strong>家长信任度</strong>，
      本站也将<strong>优先推荐</strong>。上传图片仅用于后台审核，<strong>严格保护隐私</strong>。
    </span>
  </div>

  <el-card class="cert-card">
    <div v-for="(slot, idx) in slots" :key="slot.certType" class="cert-row">
      <div class="cert-label">
        <div class="cert-label-title">{{ slot.title }}</div>
        <div :class="['cert-label-hint', slot.required ? 'is-required' : '']">{{ slot.hint }}</div>
        <div class="cert-status" v-if="statusOf(slot.certType)">
          <el-tag size="small" :type="statusOf(slot.certType).tag">{{ statusOf(slot.certType).label }}</el-tag>
          <span v-if="slotMap[slot.certType]?.auditRemark" class="cert-remark">
            备注：{{ slotMap[slot.certType].auditRemark }}
          </span>
        </div>
      </div>

      <div class="cert-action">
        <div v-if="previewOf(slot.certType)" class="cert-preview">
          <el-image
            :src="previewOf(slot.certType)"
            :preview-src-list="[previewOf(slot.certType)]"
            fit="cover"
            class="cert-image"
          />
        </div>
        <div class="cert-upload-slot" v-loading="uploading[idx]">
          <label :class="['upload-btn', { disabled: !userStore.isTutor }]">
            <el-icon :size="28" color="#ef4444"><Plus /></el-icon>
            <input
              type="file"
              accept="image/*"
              :disabled="!userStore.isTutor"
              class="upload-input"
              @change="e => handleUpload(e, slot)"
            />
          </label>
          <div class="upload-btn-text">
            {{ previewOf(slot.certType) ? '重新上传' : '上传' }}
          </div>
        </div>
      </div>
    </div>
  </el-card>

  <div class="submit-bar" v-if="userStore.isTutor">
    <div class="submit-hint">
      <template v-if="pendingCount > 0">
        有 <strong>{{ pendingCount }}</strong> 个槽位已更新但<strong class="text-warn">尚未提交</strong>，点击右侧按钮提交审核。
      </template>
      <template v-else-if="hasCommitted">
        暂无待提交的更新。证件已在后台审核。
      </template>
      <template v-else>
        请至少上传必填证件（身份证正反面）后再提交审核。
      </template>
    </div>
    <el-button
      type="primary"
      :disabled="pendingCount === 0"
      :loading="submitting"
      @click="submitAll"
    >
      提交审核{{ pendingCount > 0 ? `（${pendingCount}）` : '' }}
    </el-button>
  </div>
</div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, InfoFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const userStore = useUserStore()
const { get, post } = useApi()
const config = useRuntimeConfig()

const slots = [
  { certType: 1, title: '身份证正面/护照', hint: '(必填)',                                 required: true  },
  { certType: 2, title: '身份证反面',       hint: '(必填)',                                 required: true  },
  { certType: 3, title: '学生证/毕业证',    hint: '(大学生必填，请上传带名字内容的那一页)', required: false },
  { certType: 4, title: '教师资格证',       hint: '(专业老师必填)',                         required: false },
  { certType: 5, title: '其他身份证件',     hint: '(选填)',                                 required: false },
]

const certs = ref([])                    // 后端已保存的证件
const pending = reactive({})             // { certType: url } 已上传到 MinIO 但还没提交后端
const uploading = reactive([false, false, false, false, false])
const submitting = ref(false)

const slotMap = computed(() => {
  const m = {}
  for (const c of certs.value) m[c.certType] = c
  return m
})

const previewOf = (ct) => pending[ct] || slotMap.value[ct]?.certUrl || ''

const pendingCount = computed(() => Object.keys(pending).length)
const hasCommitted = computed(() => certs.value.length > 0)

// 槽位显示状态：优先显示本地待提交，否则显示后端审核状态
const statusOf = (ct) => {
  if (pending[ct]) return { label: '待提交', tag: 'info' }
  const c = slotMap.value[ct]
  if (!c) return null
  const map = { 0: { label: '待审核', tag: 'warning' }, 1: { label: '已通过', tag: 'success' }, 2: { label: '已驳回', tag: 'danger' } }
  return map[c.auditStatus] || null
}

const loadCerts = async () => {
  try {
    const res = await get('/user/auth/tutor-profile/cert/list')
    if (res.code === 200) certs.value = res.data || []
  } catch (e) { console.error(e) }
}

const uploadImage = async (file) => {
  const fd = new FormData()
  fd.append('picFile', file)
  const token = localStorage.getItem('token')
  const res = await $fetch(`${config.public.apiBase}/system/auth/upload/pic`, {
    method: 'POST', body: fd, headers: { token }
  })
  if (res.code === 200 && res.data) return res.data
  throw new Error(res.msg || '上传失败')
}

const handleUpload = async (e, slot) => {
  const file = e.target.files?.[0]
  e.target.value = ''
  if (!file) return
  if (!/^image\/(jpeg|jpg|png|gif|webp)$/i.test(file.type)) { ElMessage.error('只能上传图片'); return }
  if (file.size > 10 * 1024 * 1024) { ElMessage.error('图片不能超过 10MB'); return }

  const idx = slots.findIndex(s => s.certType === slot.certType)
  uploading[idx] = true
  try {
    const url = await uploadImage(file)
    pending[slot.certType] = url
    ElMessage.success('图片已就绪，点击下方提交审核完成提交')
  } catch (err) {
    ElMessage.error(err?.message || '上传失败')
  } finally {
    uploading[idx] = false
  }
}

const submitAll = async () => {
  if (pendingCount.value === 0) return
  // 若是首次提交，必填（身份证正反面）若仍空则提醒
  const missingRequired = slots
    .filter(s => s.required)
    .filter(s => !previewOf(s.certType))
    .map(s => s.title)
  if (missingRequired.length > 0) {
    try {
      await ElMessageBox.confirm(
        `还缺少必填证件：${missingRequired.join('、')}，确定要继续仅提交当前已上传的证件吗？`,
        '提示', { confirmButtonText: '继续提交', cancelButtonText: '返回补充', type: 'warning' }
      )
    } catch { return }
  }

  submitting.value = true
  try {
    const entries = Object.entries(pending)
    let okCount = 0
    for (const [certTypeStr, url] of entries) {
      const ct = Number(certTypeStr)
      const title = slots.find(s => s.certType === ct)?.title || '证件'
      const res = await post('/user/auth/tutor-profile/cert/save', {
        certType: ct,
        certName: title,
        certUrl: url,
        certNo: ''
      })
      if (res.code === 200) {
        okCount++
        delete pending[ct]
      } else {
        ElMessage.error(`${title}提交失败：${res.msg || '未知错误'}`)
      }
    }
    if (okCount > 0) {
      ElMessage.success(`已提交 ${okCount} 项，等待管理员审核`)
      await loadCerts()
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => { if (userStore.isTutor) loadCerts() })
</script>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; color: #111827; }

.cert-tip {
  display: flex; align-items: flex-start; gap: 8px;
  background: #fff8e6; border: 1px solid #fde68a;
  padding: 12px 14px; border-radius: 8px;
  font-size: 14px; line-height: 1.7; color: #374151;
  margin-bottom: 20px;
}
.cert-tip strong { color: #ea580c; }
.verified-chip {
  display: inline-block; padding: 1px 6px;
  border: 1px solid #ea580c; color: #ea580c; background: #fff;
  border-radius: 4px; font-weight: 600; margin: 0 2px;
}

.cert-card { padding: 8px 4px; }

.cert-row {
  display: flex; justify-content: space-between; align-items: flex-start;
  gap: 24px; padding: 20px 16px;
  border-bottom: 1px solid #f1f5f9;
}
.cert-row:last-child { border-bottom: none; }

.cert-label { flex: 1; min-width: 180px; }
.cert-label-title { font-size: 16px; font-weight: 600; color: #111827; }
.cert-label-hint  { font-size: 13px; color: #6b7280; margin-top: 4px; }
.cert-label-hint.is-required { color: #dc2626; }

.cert-status { margin-top: 8px; display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.cert-remark { font-size: 12px; color: #6b7280; }

.cert-action { display: flex; align-items: center; gap: 16px; }
.cert-preview .cert-image {
  width: 140px; height: 100px; border-radius: 6px;
  border: 1px solid #e5e7eb; cursor: zoom-in;
}

.cert-upload-slot { display: flex; flex-direction: column; align-items: center; gap: 6px; }
.upload-btn {
  position: relative;
  width: 100px; height: 100px;
  border: 1px dashed #d9d9d9; border-radius: 6px; background: #fff;
  cursor: pointer; display: flex; align-items: center; justify-content: center;
  transition: border-color 0.2s;
}
.upload-btn:hover:not(.disabled) { border-color: #409eff; }
.upload-btn.disabled { cursor: not-allowed; opacity: 0.5; }
.upload-input {
  position: absolute; inset: 0;
  width: 100%; height: 100%;
  opacity: 0; cursor: pointer;
}
.upload-btn.disabled .upload-input { cursor: not-allowed; pointer-events: none; }
.upload-btn-text { font-size: 12px; color: #9ca3af; }

.submit-bar {
  margin-top: 20px;
  display: flex; justify-content: space-between; align-items: center;
  padding: 16px 20px;
  background: #fff; border: 1px solid #e5e7eb; border-radius: 8px;
}
.submit-hint { font-size: 14px; color: #374151; }
.text-warn { color: #ea580c; }

@media (max-width: 768px) {
  .cert-row { flex-direction: column; align-items: stretch; }
  .cert-action { justify-content: flex-start; }
  .submit-bar { flex-direction: column; gap: 12px; align-items: stretch; }
}
</style>
