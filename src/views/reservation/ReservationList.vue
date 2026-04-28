<template>
  <div class="page-container">
    <h2 class="page-title">学员预约</h2>
    <p class="page-tip">学员定向预约某教员（Flow 1）。Admin 联系教员撮合 → 收中介费 → 点「确认匹配」。或选「驳回」/「转入公开需求池」。</p>

    <div class="filter-bar">
      <el-radio-group v-model="resStatus" @change="search">
        <el-radio-button :label="null">全部</el-radio-button>
        <el-radio-button :label="0">待处理</el-radio-button>
        <el-radio-button :label="1">已匹配</el-radio-button>
        <el-radio-button :label="3">已驳回/取消</el-radio-button>
        <el-radio-button :label="2">已过期</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="list" border stripe v-loading="loading" empty-text="暂无数据">
      <el-table-column label="预约时间" width="160">
        <template #default="{ row }">
          <div style="font-size:12px">{{ row.gmtCreate }}</div>
          <div v-if="row.matchedAt" style="font-size:12px;color:#16a34a">匹配于：{{ row.matchedAt }}</div>
        </template>
      </el-table-column>
      <el-table-column label="目标教员" min-width="180">
        <template #default="{ row }">
          <div><strong>{{ tutorMap[row.tutorId]?.realName || '...' }}</strong> <span style="color:#94a3b8">{{ tutorMap[row.tutorId]?.displayNo }}</span></div>
          <div style="font-size:12px">📱 {{ tutorMap[row.tutorId]?.mobile || '...' }}</div>
        </template>
      </el-table-column>
      <el-table-column label="学员联系方式" min-width="180">
        <template #default="{ row }">
          <div><strong>{{ row.contactName || '-' }}</strong></div>
          <div style="font-size:12px">📱 {{ row.contactMobile || '-' }}</div>
          <div style="font-size:12px">💬 {{ row.contactWechat || '-' }}</div>
        </template>
      </el-table-column>
      <el-table-column label="需求/备注" min-width="200" prop="remark" show-overflow-tooltip />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.resStatus)" size="small">{{ statusLabel(row.resStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <template v-if="row.resStatus === 0">
            <el-button size="small" type="success" @click="doMatch(row)">确认匹配</el-button>
            <el-button size="small" type="warning" @click="doPublish(row)">转公开池</el-button>
            <el-button size="small" type="danger" @click="doReject(row)">驳回</el-button>
          </template>
          <el-button v-else size="small" @click="viewDetail(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="预约详情" width="500px">
      <el-descriptions :column="1" border v-if="detail">
        <el-descriptions-item label="教员">{{ tutorMap[detail.tutorId]?.realName }} {{ tutorMap[detail.tutorId]?.displayNo }} · {{ tutorMap[detail.tutorId]?.mobile }}</el-descriptions-item>
        <el-descriptions-item label="学员称谓">{{ detail.contactName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学员手机">{{ detail.contactMobile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学员微信">{{ detail.contactWechat || '-' }}</el-descriptions-item>
        <el-descriptions-item label="需求/备注">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detail.gmtCreate }}</el-descriptions-item>
        <el-descriptions-item label="匹配时间" v-if="detail.matchedAt">{{ detail.matchedAt }}</el-descriptions-item>
        <el-descriptions-item label="取消原因" v-if="detail.cancelReason">{{ detail.cancelReason }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute } from 'vue-router'

const route = useRoute()
const resStatus = ref<number | null>(route.query.resStatus !== undefined ? Number(route.query.resStatus) : 0)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)
const tutorMap = ref<Record<string, any>>({})

const statusLabel = (s: number) => ({ 0: '待处理', 1: '已匹配', 2: '已过期', 3: '已驳回/取消' }[s] || '未知')
const statusType = (s: number) => ({ 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/reservation/page', {
      pageCurrent: page.value, pageSize: 20, resStatus: resStatus.value
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
      // Hydrate tutor info
      const tutorIds = [...new Set(list.value.map(r => r.tutorId).filter(Boolean))]
      for (const tid of tutorIds) {
        if (!tutorMap.value[tid]) {
          try {
            const tres = await get('/user/admin/tutor/view', { id: tid })
            if (tres.code === 200) tutorMap.value[tid] = tres.data
          } catch (_) {}
        }
      }
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const viewDetail = (row: any) => { detail.value = row; detailVisible.value = true }

const doMatch = async (row: any) => {
  let remark = ''
  try {
    const r = await ElMessageBox.prompt(
      `确认匹配预约？这将把对应「学员需求」也标为已接单（学员库显示已接单 1 周后清理）。\n\n备注（可记接单教员姓名/电话，便于以后查账）：`,
      '确认匹配',
      { confirmButtonText: '确认', cancelButtonText: '取消', inputType: 'text', inputPlaceholder: '可选，例如：李教员（13xxxxx1234）' }
    )
    remark = r.value || ''
  } catch { return }
  try {
    const res = await put('/user/admin/reservation/match', { id: row.id, matchedTutorRemark: remark })
    if (res.code === 200) { ElMessage.success(res.data || '已匹配'); await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('网络错误') }
}

const doPublish = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      '目标教员未接？把该需求转入公开需求池（其他教员可联系客服接单），原预约将自动取消。',
      '转入公开池', { confirmButtonText: '确认转入', cancelButtonText: '取消', type: 'warning' }
    )
  } catch { return }
  try {
    const res = await put('/user/admin/reservation/publish-requirement', { id: row.id })
    if (res.code === 200) { ElMessage.success(res.data); await search() }
    else ElMessage.error(res.msg)
  } catch { ElMessage.error('网络错误') }
}

const doReject = async (row: any) => {
  let reason = ''
  try {
    const r = await ElMessageBox.prompt('驳回原因（学员会收到站内信）', '驳回预约', {
      confirmButtonText: '驳回',
      cancelButtonText: '取消',
      inputType: 'textarea',
      inputPlaceholder: '例如：教员近期已满档；学员需求不明确等',
      inputValidator: v => !!v || '请填写原因',
      type: 'warning'
    })
    reason = r.value || ''
  } catch { return }
  try {
    const res = await put('/user/admin/reservation/reject', { id: row.id, reason })
    if (res.code === 200) { ElMessage.success(res.data); await search() }
    else ElMessage.error(res.msg)
  } catch { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
<style scoped>
.page-tip { color: #64748b; font-size: 13px; margin: 0 0 14px; }
.filter-bar { margin-bottom: 16px; }
</style>
