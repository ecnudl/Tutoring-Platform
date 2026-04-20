<template>
<div>
  <h2 class="page-title">家教感言 / 意见反馈</h2>

  <el-card v-if="userStore.isTutor" style="margin-bottom:20px" class="feedback-card">
    <h4 style="margin-bottom:6px">发布家教感言</h4>
    <p style="font-size:12px;color:#64748b;margin:0 0 12px">
      分享您的教学经历或对平台的感受。审核通过后将展示在首页"家教感言"栏目。
    </p>
    <el-form label-width="80px">
      <el-form-item label="内容">
        <el-input v-model="form.content" type="textarea" :rows="4" placeholder="例如：在平台上教了两年了，学生资源稳定，平台不收中介费…" />
      </el-form-item>
      <el-form-item label="署名">
        <el-input v-model="form.contact" placeholder="您希望展示的称呼，如：陈老师（选填）" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-alert v-else type="info" show-icon :closable="false" style="margin-bottom:20px">
    <template #title>家教感言仅限教员发布</template>
    <template #default>
      <p style="margin:6px 0 0;font-size:13px;color:#475569">
        首页"家教感言"栏目专门展示教员在平台上的教学体验分享；学员/家长的意见和投诉请拨打客服热线或通过微信联系我们。
      </p>
    </template>
  </el-alert>

  <h4 style="margin-bottom:12px">{{ userStore.isTutor ? '我发布的感言' : '我的反馈记录' }}</h4>
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
import { useUserStore } from '~/stores/user'
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const userStore = useUserStore()
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
