<template>
<div>
  <h2 class="page-title">我的预约</h2>
  <el-table :data="list" border stripe empty-text="暂无预约记录" v-loading="loading">
    <el-table-column label="对方" width="200">
      <template #default="{ row }">
        {{ userStore.isTutor ? ('学员 #' + row.studentUserId) : ('教员 #' + row.tutorUserId) }}
      </template>
    </el-table-column>
    <el-table-column prop="scheduleTime" label="预约时间" />
    <el-table-column prop="address" label="地点" />
    <el-table-column label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="statusType(row.resStatus)" size="small">{{ statusLabel(row.resStatus) }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="gmtCreate" label="创建时间" width="180" />
    <el-table-column label="操作" width="180">
      <template #default="{ row }">
        <template v-if="userStore.isTutor && row.resStatus === 0">
          <el-button size="small" type="success" @click="handleConfirm(row.id)">确认</el-button>
          <el-button size="small" type="danger" @click="handleCancel(row.id)">拒绝</el-button>
        </template>
        <template v-if="userStore.isStudent && row.resStatus === 0">
          <el-button size="small" type="danger" @click="handleCancel(row.id)">取消</el-button>
        </template>
        <span v-if="row.resStatus === 3 && row.cancelReason" style="color:#999;font-size:12px">
          原因：{{ row.cancelReason }}
        </span>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'

definePageMeta({ layout: 'center' })
const { post, put } = useApi()
const userStore = useUserStore()

const list = ref([])
const loading = ref(false)

const statusLabel = (s) => ({ 0:'待确认', 1:'已确认', 2:'已完成', 3:'已取消' }[s] || '未知')
const statusType = (s) => ({ 0:'warning', 1:'success', 2:'info', 3:'danger' }[s] || 'info')

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/reservation/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
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
