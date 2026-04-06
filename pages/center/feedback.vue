<template>
<div>
  <h2 class="page-title">意见反馈</h2>
  <el-card style="margin-bottom:20px" class="feedback-card">
    <h4 style="margin-bottom:12px">提交反馈</h4>
    <el-form label-width="80px">
      <el-form-item label="反馈内容">
        <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入您的意见或建议" />
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.contact" placeholder="手机号或邮箱（选填）" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <h4 style="margin-bottom:12px">我的反馈</h4>
  <el-table :data="list" border stripe empty-text="暂无反馈" v-loading="loading">
    <el-table-column prop="content" label="内容" />
    <el-table-column label="状态" width="80">
      <template #default="{ row }">
        <el-tag :type="row.fbStatus===1?'success':'info'" size="small">{{ {0:'待处理',1:'已回复',2:'已关闭'}[row.fbStatus] }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="reply" label="回复" />
    <el-table-column prop="gmtCreate" label="提交时间" width="170" />
  </el-table>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const { post } = useApi()
const form = ref({ content: '', contact: '' })
const list = ref([])
const loading = ref(false)
const submitting = ref(false)
const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/feedback/page', { pageCurrent: 1, pageSize: 20 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}
const handleSubmit = async () => {
  if (!form.value.content) { ElMessage.warning('请输入反馈内容'); return }
  submitting.value = true
  try {
    const res = await post('/user/auth/feedback/submit', form.value)
    if (res.code === 200) { ElMessage.success('提交成功'); form.value = { content: '', contact: '' }; await load() }
    else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('网络错误') }
  finally { submitting.value = false }
}
onMounted(() => { load() })
</script>
<style scoped>
.feedback-card { max-width: 500px; }
@media (max-width: 768px) {
  .feedback-card { max-width: 100%; }
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
  :deep(.el-form-item__label) { width: 70px !important; font-size: 13px; }
  :deep(.el-form-item__content) { margin-left: 70px !important; }
}
</style>
