<template>
<div>
  <h2 class="page-title">我的预约</h2>
  <p class="page-tip">{{ userStore.isTutor ? '已为您撮合的家教订单, 可在此查看需求详情' : '您已撮合的教员订单' }}</p>

  <el-table :data="list" border stripe empty-text="暂无预约记录" v-loading="loading">
    <el-table-column label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="statusType(row.resStatus)" size="small">{{ row.statusText || statusLabel(row.resStatus) }}</el-tag>
      </template>
    </el-table-column>

    <el-table-column label="年级 / 科目" min-width="180">
      <template #default="{ row }">
        <span v-if="row.grade" class="cell-grade">{{ row.grade }}</span>
        <span v-if="row.subjects" class="cell-subjects">{{ formatSubjects(row.subjects) }}</span>
        <span v-if="!row.grade && !row.subjects" style="color:#94a3b8">—</span>
      </template>
    </el-table-column>

    <el-table-column label="地点" min-width="160">
      <template #default="{ row }">
        <span v-if="row.location" class="cell-location">📍 {{ row.location }}</span>
        <span v-else style="color:#94a3b8">—</span>
      </template>
    </el-table-column>

    <el-table-column label="需求详情" min-width="220">
      <template #default="{ row }">
        <span v-if="row.detail" :title="row.detail" class="cell-detail">{{ truncate(row.detail, 60) }}</span>
        <span v-else style="color:#94a3b8">—</span>
      </template>
    </el-table-column>

    <el-table-column label="撮合日期" width="120">
      <template #default="{ row }">
        <span v-if="row.date" class="cell-date">{{ row.date }}</span>
        <span v-else style="color:#94a3b8">—</span>
      </template>
    </el-table-column>

    <el-table-column label="备注 / 操作" min-width="160">
      <template #default="{ row }">
        <template v-if="userStore.isTutor && row.resStatus === 0">
          <el-button size="small" type="success" @click="handleConfirm(row.id)">确认</el-button>
          <el-button size="small" type="danger" @click="handleCancel(row.id)">拒绝</el-button>
        </template>
        <template v-else-if="userStore.isStudent && row.resStatus === 0">
          <el-button size="small" type="danger" @click="handleCancel(row.id)">取消</el-button>
        </template>
        <span v-else-if="row.resStatus === 3 && row.cancelReason" class="cell-cancel-reason">
          已取消 — {{ row.cancelReason }}
        </span>
        <span v-else style="color:#94a3b8">—</span>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const { post, put } = useApi()
const userStore = useUserStore()

const list = ref([])
const loading = ref(false)

const statusLabel = (s) => ({ 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消' }[s] || '未知')
const statusType = (s) => ({ 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }[s] || 'info')

const formatSubjects = (csv) => {
  if (!csv) return ''
  return String(csv).split(',').map(x => x.trim()).filter(Boolean).join(' · ')
}
const truncate = (s, n) => {
  const str = String(s || '')
  return str.length > n ? str.slice(0, n) + '…' : str
}

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/reservation/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleConfirm = async (id) => {
  try {
    const res = await put('/user/auth/reservation/confirm?id=' + id, {})
    if (res.code === 200) { ElMessage.success('已确认'); await load() }
    else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { console.error(e); ElMessage.error('网络错误') }
}

const handleCancel = async (id) => {
  try {
    const res = await put('/user/auth/reservation/cancel?id=' + id, {})
    if (res.code === 200) { ElMessage.success('已取消'); await load() }
    else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { console.error(e); ElMessage.error('网络错误') }
}

onMounted(() => { load() })
</script>

<style scoped>
.page-title { font-size: 18px; font-weight: 600; margin: 0 0 4px; color: #1F2937; }
.page-tip { color: #94a3b8; font-size: 13px; margin: 0 0 16px; }

.cell-grade {
  display: inline-block;
  padding: 2px 8px;
  background: #fef3c7;
  color: #b45309;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 600;
  margin-right: 6px;
}
.cell-subjects {
  color: #1F2937;
  font-size: 13px;
}
.cell-location {
  color: #1F2937;
  font-size: 13px;
}
.cell-detail {
  color: #4b5563;
  font-size: 13px;
  line-height: 1.6;
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}
.cell-date {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12.5px;
  color: #64748b;
}
.cell-cancel-reason {
  color: #94a3b8;
  font-size: 12px;
  font-style: italic;
}

@media (max-width: 768px) {
  :deep(.el-table) { font-size: 13px; }
  :deep(.el-table .el-table__cell) { padding: 8px 4px; }
}
</style>
