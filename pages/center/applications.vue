<template>
<div>
  <h2 class="page-title">我的申请</h2>
  <el-table :data="applications" border stripe empty-text="暂无申请记录" v-loading="loading">
    <el-table-column prop="requirementId" label="需求ID" width="200" />
    <el-table-column label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="statusType(row.appStatus)" size="small">{{ statusLabel(row.appStatus) }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="gmtCreate" label="申请时间" width="180" />
    <el-table-column label="操作" width="100">
      <template #default="{ row }">
        <el-popconfirm v-if="row.appStatus === 0" title="确定取消申请？" @confirm="handleCancel(row.id)">
          <template #reference>
            <el-button size="small" type="danger">取消</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div style="display:flex;justify-content:center;margin-top:16px" v-if="total > 0">
    <el-pagination layout="prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; load() }" />
  </div>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'

definePageMeta({ layout: 'center' })
const { post, del } = useApi()

const applications = ref([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const statusLabel = (s) => ({ 0:'已申请', 1:'入围', 2:'录用', 3:'拒绝' }[s] || '未知')
const statusType = (s) => ({ 0:'info', 1:'warning', 2:'success', 3:'danger' }[s] || 'info')

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/application/page', { pageCurrent: page.value, pageSize: 20 })
    if (res.code === 200 && res.data) {
      applications.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleCancel = async (id) => {
  try {
    const res = await del('/user/auth/application/cancel', { id })
    if (res.code === 200) { ElMessage.success('已取消'); await load() }
    else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { load() })
</script>
<style scoped>
@media (max-width: 768px) {
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
}
</style>
