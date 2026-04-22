<template>
<div>
  <h2 class="page-title">证件认证</h2>

  <el-alert
    v-if="!userStore.isTutor"
    type="info"
    show-icon
    :closable="false"
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
        <div v-if="slotMap[slot.certType]" class="cert-status">
          <el-tag size="small" :type="auditTagType(slotMap[slot.certType].auditStatus)">
            {{ auditStatusLabel(slotMap[slot.certType].auditStatus) }}
          </el-tag>
          <span v-if="slotMap[slot.certType].auditRemark" class="cert-remark">
            备注：{{ slotMap[slot.certType].auditRemark }}
          </span>
        </div>
      </div>

      <div class="cert-action">
        <div v-if="slotMap[slot.certType]?.certUrl" class="cert-preview">
          <el-image
            :src="slotMap[slot.certType].certUrl"
            :preview-src-list="[slotMap[slot.certType].certUrl]"
            fit="cover"
            class="cert-image"
          />
        </div>
        <div class="cert-upload-slot" v-loading="uploading[idx]">
          <input
            type="file"
            :ref="el => fileInputs[idx] = el"
            accept="image/*"
            style="display:none"
            @change="e => handleUpload(e, slot)"
          />
          <button class="upload-btn" :disabled="!userStore.isTutor" @click="fileInputs[idx]?.click()">
            <el-icon :size="28" color="#ef4444"><Plus /></el-icon>
          </button>
          <div class="upload-btn-text">
            {{ slotMap[slot.certType]?.certUrl ? '重新上传' : '上传' }}
          </div>
        </div>
      </div>
    </div>
  </el-card>
</div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, InfoFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const { get, post } = useApi()
const config = useRuntimeConfig()

const slots = [
  { certType: 1, title: '身份证正面/护照', hint: '(必填)',                                     required: true  },
  { certType: 2, title: '身份证反面',       hint: '(必填)',                                     required: true  },
  { certType: 3, title: '学生证/毕业证',    hint: '(大学生必填，请上传带名字内容的那一页)',    required: false },
  { certType: 4, title: '教师资格证',       hint: '(专业老师必填)',                             required: false },
  { certType: 5, title: '其他身份证件',     hint: '(选填)',                                     required: false },
]

const certs = ref([])
const uploading = reactive([false, false, false, false, false])
const fileInputs = reactive([])

const slotMap = computed(() => {
  const m = {}
  for (const c of certs.value) m[c.certType] = c
  return m
})

const auditStatusLabel = (s) => ({ 0: '待审核', 1: '已通过', 2: '已驳回' }[s] ?? '未审核')
const auditTagType = (s) => ({ 0: 'warning', 1: 'success', 2: 'danger' }[s] ?? 'info')

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

const saveCert = async (slot, url) => {
  return post('/user/auth/tutor-profile/cert/save', {
    certType: slot.certType,
    certName: slot.title,
    certUrl: url,
    certNo: ''
  })
}

const handleUpload = async (e, slot) => {
  const file = e.target.files?.[0]
  e.target.value = ''
  if (!file) return

  const isImg = /^image\/(jpeg|jpg|png|gif|webp)$/i.test(file.type)
  if (!isImg)               { ElMessage.error('只能上传图片格式'); return }
  if (file.size > 10 * 1024 * 1024) { ElMessage.error('图片不能超过 10MB'); return }

  const idx = slots.findIndex(s => s.certType === slot.certType)
  uploading[idx] = true
  try {
    const url = await uploadImage(file)
    const saveRes = await saveCert(slot, url)
    if (saveRes.code === 200) {
      ElMessage.success(saveRes.msg || '上传成功，等待审核')
      await loadCerts()
    } else {
      ElMessage.error(saveRes.msg || '保存失败')
    }
  } catch (err) {
    ElMessage.error(err?.message || '上传失败，请重试')
  } finally {
    uploading[idx] = false
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

.cert-upload-slot {
  display: flex; flex-direction: column; align-items: center; gap: 6px;
}
.upload-btn {
  width: 100px; height: 100px;
  border: 1px dashed #d9d9d9; border-radius: 6px; background: #fff;
  cursor: pointer; display: flex; align-items: center; justify-content: center;
  transition: border-color 0.2s;
}
.upload-btn:hover { border-color: #409eff; }
.upload-btn-text { font-size: 12px; color: #9ca3af; }

@media (max-width: 768px) {
  .cert-row { flex-direction: column; align-items: stretch; }
  .cert-action { justify-content: flex-start; }
}
</style>
