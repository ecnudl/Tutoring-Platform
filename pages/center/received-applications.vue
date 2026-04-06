<template>
<div>
  <h2 class="page-title">收到的申请</h2>
  <div style="margin-bottom:16px">
    <el-select v-model="selectedReqId" placeholder="选择需求" style="width:300px;max-width:100%" @change="load">
      <el-option v-for="r in myRequirements" :key="r.id" :label="r.title" :value="r.id" />
    </el-select>
  </div>

  <div v-if="!selectedReqId" style="text-align:center;padding:40px;color:#999">请先选择一个需求查看收到的申请</div>

  <template v-else>
    <el-table :data="applications" border stripe empty-text="暂无申请" v-loading="loading">
      <el-table-column prop="tutorId" label="教员ID" width="180" />
      <el-table-column prop="applyMessage" label="自荐语" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="statusType(row.appStatus)" size="small">{{ statusLabel(row.appStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="申请时间" width="170" />
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button size="small" @click="viewTutor(row.tutorId)">查看教员</el-button>
          <template v-if="row.appStatus === 0">
            <el-button size="small" type="warning" @click="handleAction(row.id, 'shortlist')">入围</el-button>
            <el-button size="small" type="danger" @click="handleAction(row.id, 'reject')">拒绝</el-button>
          </template>
          <template v-if="row.appStatus === 0 || row.appStatus === 1">
            <el-button size="small" type="success" @click="handleAction(row.id, 'accept')">录用</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </template>
</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const { post, put } = useApi()
const router = useRouter()

const myRequirements = ref([])
const selectedReqId = ref(null)
const applications = ref([])
const loading = ref(false)

const statusLabel = (s) => ({ 0:'已申请', 1:'入围', 2:'已录用', 3:'已拒绝' }[s] || '未知')
const statusType = (s) => ({ 0:'info', 1:'warning', 2:'success', 3:'danger' }[s] || 'info')

const loadRequirements = async () => {
  try {
    const res = await post('/user/auth/requirement/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) {
      myRequirements.value = res.data.list || []
      if (myRequirements.value.length > 0 && !selectedReqId.value) {
        selectedReqId.value = myRequirements.value[0].id
        await load()
      }
    }
  } catch (e) { console.error(e) }
}

const load = async () => {
  if (!selectedReqId.value) return
  loading.value = true
  try {
    const res = await post('/user/auth/application/received', { requirementId: selectedReqId.value })
    if (res.code === 200) {
      applications.value = res.data || []
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleAction = async (id, action) => {
  try {
    const res = await put(`/user/auth/application/${action}?id=${id}`, {})
    if (res.code === 200) {
      ElMessage.success(res.data || '操作成功')
      await load()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}

const viewTutor = (tutorId) => {
  router.push('/jy/t' + tutorId)
}

onMounted(() => { loadRequirements() })
</script>
<style scoped>
@media (max-width: 768px) {
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
}
</style>
