<template>
<div>
  <h2 class="page-title">家教感言</h2>

  <el-card style="margin-bottom:20px" class="feedback-card">
    <h4 style="margin-bottom:6px">发布家教感言</h4>
    <p style="font-size:12px;color:#64748b;margin:0 0 12px">
      欢迎教员/家长分享您的真实体验。提交后由管理员审核，通过后将展示在网站首页"家教感言"栏目。
    </p>
    <el-form label-width="80px">
      <el-form-item label="内容">
        <el-input v-model="form.content" type="textarea" :rows="4" maxlength="500" show-word-limit
                  :placeholder="userStore.isTutor ? '例如：学生资源稳定，纠纷处理及时，匹配靠谱…' : '例如：老师非常耐心，孩子数学从70分提到了95分…'" />
      </el-form-item>
      <el-form-item label="署名">
        <el-input v-model="form.contact" maxlength="50" :placeholder="userStore.isTutor ? '您希望展示的称呼，如：陈老师（选填）' : '您希望展示的称呼，如：张女士（选填）'" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">提交审核</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <h4 style="margin-bottom:12px">我提交的感言</h4>
  <el-table :data="list" border stripe empty-text="暂无记录" v-loading="loading">
    <el-table-column prop="content" label="内容" />
    <el-table-column label="审核状态" width="100">
      <template #default="{ row }">
        <el-tag :type="tagType(row.fbStatus)" size="small">{{ statusLabel(row.fbStatus) }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="reply" label="审核备注" />
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

const statusLabel = (s) => ({ 0:'待审核', 1:'已通过', 2:'已驳回' }[s] || '未知')
const tagType = (s) => ({ 0:'warning', 1:'success', 2:'info' }[s] || 'info')

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/feedback/page', { pageCurrent: 1, pageSize: 20 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}
const handleSubmit = async () => {
  if (!form.value.content || !form.value.content.trim()) { ElMessage.warning('请输入感言内容'); return }
  submitting.value = true
  try {
    const res = await post('/user/auth/feedback/submit', form.value)
    if (res.code === 200) {
      ElMessage.success(res.data || '提交成功，审核通过后将展示在首页')
      form.value = { content: '', contact: '' }
      await load()
    } else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('网络错误') }
  finally { submitting.value = false }
}
onMounted(() => { load() })
</script>
<style scoped>
.feedback-card { max-width: 560px; }
@media (max-width: 768px) {
  .feedback-card { max-width: 100%; }
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
  :deep(.el-form-item__label) { width: 70px !important; font-size: 13px; }
  :deep(.el-form-item__content) { margin-left: 70px !important; }
}
</style>
