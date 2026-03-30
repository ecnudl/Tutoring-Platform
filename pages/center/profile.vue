<template>
<div>
  <h2 class="page-title">个人资料</h2>
  <el-form :model="form" label-width="100px" class="profile-form" v-loading="loading">
    <el-form-item label="手机号">
      <el-input :model-value="userStore.mobile" disabled />
    </el-form-item>
    <el-form-item label="角色">
      <el-tag :type="userStore.isTutor ? 'warning' : 'success'">{{ userStore.isTutor ? '教员' : '学员' }}</el-tag>
    </el-form-item>
    <el-form-item label="昵称">
      <el-input v-model="form.nickname" placeholder="请输入昵称" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.userSex">
        <el-radio :label="1">男</el-radio>
        <el-radio :label="2">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
    </el-form-item>
  </el-form>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'
definePageMeta({ layout: 'center' })
const { get, post } = useApi()
const userStore = useUserStore()
const form = ref({ nickname: '', userSex: 1 })
const loading = ref(false)
const saving = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await get('/user/auth/users/view')
    if (res.code === 200 && res.data) {
      form.value.nickname = res.data.nickname || ''
      form.value.userSex = res.data.userSex || 1
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})

const handleSave = async () => {
  saving.value = true
  try {
    const res = await post('/user/auth/users/edit', form.value)
    if (res.code === 200) ElMessage.success('保存成功')
    else ElMessage.error(res.msg || '保存失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}
</script>
<style scoped>
.profile-form { max-width: 500px; }
@media (max-width: 768px) {
  .profile-form { max-width: 100%; }
  .profile-form :deep(.el-form-item__label) { width: 70px !important; font-size: 13px; }
  .profile-form :deep(.el-form-item__content) { margin-left: 70px !important; }
}
</style>
