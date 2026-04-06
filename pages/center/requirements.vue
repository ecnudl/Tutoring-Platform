<template>
<div>
  <h2 class="page-title">我的需求</h2>
  <el-button type="primary" style="margin-bottom:16px" @click="$router.push('/center/requirement-new')">发布需求</el-button>
  <el-table :data="list" border stripe empty-text="暂无需求" v-loading="loading">
    <el-table-column prop="title" label="标题" />
    <el-table-column label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="statusType(row.reqStatus)" size="small">{{ statusLabel(row.reqStatus) }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="gmtCreate" label="创建时间" width="180" />
    <el-table-column label="操作" width="220">
      <template #default="{ row }">
        <el-button size="small" @click="$router.push('/center/requirement-new?id='+row.id)">编辑</el-button>
        <el-button v-if="row.reqStatus===0||row.reqStatus===6" size="small" type="success" @click="submitAudit(row.id)">提交审核</el-button>
        <el-popconfirm v-if="row.reqStatus===0||row.reqStatus===6" title="确定删除？" @confirm="handleDelete(row.id)">
          <template #reference><el-button size="small" type="danger">删除</el-button></template>
        </el-popconfirm>
      </template>
    </el-table-column>
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
const { post, del } = useApi()
const list = ref([])
const loading = ref(false)

const statusLabel = (s) => ({ 0:'草稿', 1:'审核中', 2:'已发布', 3:'已匹配', 4:'已完成', 5:'已关闭', 6:'已驳回' }[s] || '未知')
const statusType = (s) => ({ 0:'info', 1:'warning', 2:'success', 5:'info', 6:'danger' }[s] || 'info')

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/requirement/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const submitAudit = async (id) => {
  const res = await post('/user/auth/requirement/submit-audit?id=' + id)
  if (res.code === 200) { ElMessage.success('提交成功'); await load() }
  else ElMessage.error(res.msg)
}

const handleDelete = async (id) => {
  const res = await del('/user/auth/requirement/delete', { id })
  if (res.code === 200) { ElMessage.success('删除成功'); await load() }
  else ElMessage.error(res.msg)
}

onMounted(() => { load() })
</script>
<style scoped>
@media (max-width: 768px) {
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
}
</style>
