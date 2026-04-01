<template>
<div>
  <h2 class="page-title">教员资料</h2>

  <!-- 审核状态提示 -->
  <el-alert v-if="profile.auditStatus === 0" title="资料为草稿状态，填写完成后请提交审核" type="info" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 1" title="资料审核中，请耐心等待" type="warning" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 2" title="审核已通过，您的资料已展示在教员库中" type="success" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 3" :title="'审核被驳回：' + (profile.auditRemark || '请修改后重新提交')" type="error" :closable="false" style="margin-bottom:16px" />

  <el-form :model="form" label-width="100px" class="tutor-form" v-loading="pageLoading">
    <el-form-item label="头像">
      <div style="display:flex;align-items:center;gap:16px">
        <el-avatar :size="80" :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
        <div v-if="!isLocked">
          <input type="file" ref="avatarInput" accept="image/*" style="display:none" @change="handleAvatarUpload" />
          <el-button size="small" :loading="uploadingAvatar" @click="$refs.avatarInput.click()">上传头像</el-button>
          <p style="font-size:12px;color:#999;margin-top:4px">支持 jpg/png，建议正方形</p>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="真实姓名">
      <el-input v-model="form.realName" placeholder="请输入真实姓名" :disabled="isLocked" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.gender" :disabled="isLocked">
        <el-radio :label="1">男</el-radio>
        <el-radio :label="2">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="教员类型">
      <el-select v-model="form.tutorType" style="width:100%" :disabled="isLocked" placeholder="请选择">
        <el-option label="大学生" :value="1" />
        <el-option label="专职" :value="2" />
        <el-option label="在职教师" :value="3" />
        <el-option label="退休教师" :value="4" />
      </el-select>
    </el-form-item>
    <el-form-item label="学历">
      <el-select v-model="form.degree" style="width:100%" :disabled="isLocked" placeholder="请选择">
        <el-option label="高中" :value="1" />
        <el-option label="大专" :value="2" />
        <el-option label="本科" :value="3" />
        <el-option label="硕士" :value="4" />
        <el-option label="博士" :value="5" />
      </el-select>
    </el-form-item>
    <el-form-item label="学校">
      <el-input v-model="form.university" placeholder="请输入学校" :disabled="isLocked" />
    </el-form-item>
    <el-form-item label="专业">
      <el-input v-model="form.major" placeholder="请输入专业" :disabled="isLocked" />
    </el-form-item>
    <el-form-item label="授课科目">
      <el-select v-model="subjectList" multiple style="width:100%" placeholder="选择科目" :disabled="isLocked">
        <el-option v-for="s in dictStore.subjects" :key="s.id" :label="s.subjectName" :value="String(s.id)" />
      </el-select>
    </el-form-item>
    <el-form-item label="课时费(元/时)">
      <div style="display:flex;gap:8px;align-items:center">
        <el-input-number v-model="form.priceMin" :min="0" :max="9999" :disabled="isLocked" />
        <span>-</span>
        <el-input-number v-model="form.priceMax" :min="0" :max="9999" :disabled="isLocked" />
      </div>
    </el-form-item>
    <el-form-item label="自我介绍">
      <el-input v-model="form.selfIntroduction" type="textarea" :rows="4" placeholder="介绍您的教学经验和风格" :disabled="isLocked" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="saving" :disabled="isLocked" @click="handleSave">保存资料</el-button>
      <el-button type="success" :loading="submitting" :disabled="!canSubmit" @click="handleSubmitAudit">提交审核</el-button>
    </el-form-item>
  </el-form>
</div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useDictStore } from '~/stores/dict'
import { ElMessage } from 'element-plus'

definePageMeta({ layout: 'center' })

const dictStore = useDictStore()
const { get, post } = useApi()
const config = useRuntimeConfig()

const profile = ref({})
const form = ref({
  avatar: '', realName: '', gender: 1, tutorType: null, degree: null,
  university: '', major: '', selfIntroduction: '',
  priceMin: 0, priceMax: 0
})
const subjectList = ref([])
const saving = ref(false)
const submitting = ref(false)
const pageLoading = ref(false)
const uploadingAvatar = ref(false)

const isLocked = computed(() => profile.value.auditStatus === 1 || profile.value.auditStatus === 2)
const canSubmit = computed(() => profile.value.auditStatus === 0 || profile.value.auditStatus === 3)

const handleAvatarUpload = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) { ElMessage.warning('图片不能超过5MB'); return }
  uploadingAvatar.value = true
  try {
    const fd = new FormData()
    fd.append('picFile', file)
    const token = localStorage.getItem('token')
    const res = await $fetch(`${config.public.apiBase}/system/auth/upload/pic`, {
      method: 'POST', body: fd, headers: { token }
    })
    if (res.code === 200 && res.data) {
      form.value.avatar = res.data
      ElMessage.success('头像上传成功')
    } else { ElMessage.error(res.msg || '上传失败') }
  } catch (err) { ElMessage.error('上传失败，请重试') }
  finally { uploadingAvatar.value = false; e.target.value = '' }
}

const loadProfile = async () => {
  pageLoading.value = true
  try {
    const res = await get('/user/auth/tutor-profile/view')
    if (res.code === 200 && res.data) {
      profile.value = res.data
      form.value.avatar = res.data.avatar || ''
      form.value.realName = res.data.realName || ''
      form.value.gender = res.data.gender || 1
      form.value.tutorType = res.data.tutorType || null
      form.value.degree = res.data.degree || null
      form.value.university = res.data.university || ''
      form.value.major = res.data.major || ''
      form.value.selfIntroduction = res.data.selfIntroduction || ''
      form.value.priceMin = res.data.priceMin || 0
      form.value.priceMax = res.data.priceMax || 0
      try {
        subjectList.value = JSON.parse(res.data.subjects || '[]').map(String)
      } catch { subjectList.value = [] }
    }
  } catch (e) { console.error(e) }
  finally { pageLoading.value = false }
}

const handleSave = async () => {
  if (!form.value.realName) { ElMessage.warning('请输入真实姓名'); return }
  if (!form.value.tutorType) { ElMessage.warning('请选择教员类型'); return }
  if (subjectList.value.length === 0) { ElMessage.warning('请选择授课科目'); return }
  saving.value = true
  try {
    const res = await post('/user/auth/tutor-profile/save', {
      ...form.value,
      subjects: JSON.stringify(subjectList.value.map(Number))
    })
    if (res.code === 200) {
      ElMessage.success('保存成功')
      await loadProfile()
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (e) { ElMessage.error('网络错误，请重试') }
  finally { saving.value = false }
}

const handleSubmitAudit = async () => {
  submitting.value = true
  try {
    const res = await post('/user/auth/tutor-profile/submit-audit', {})
    if (res.code === 200) {
      ElMessage.success('提交成功，请等待审核')
      await loadProfile()
    } else {
      ElMessage.error(res.msg || '提交失败')
    }
  } catch (e) {
    console.error('提交审核失败:', e)
    ElMessage.error('网络错误，请重试')
  }
  finally { submitting.value = false }
}

onMounted(async () => {
  await dictStore.fetchAll()
  await loadProfile()
})
</script>
<style scoped>
.tutor-form { max-width: 600px; }
@media (max-width: 768px) {
  .tutor-form { max-width: 100%; }
  .tutor-form :deep(.el-form-item__label) { width: 80px !important; font-size: 13px; }
  .tutor-form :deep(.el-form-item__content) { margin-left: 80px !important; }
  .tutor-form :deep(.el-input-number) { width: 100% !important; }
}
</style>
