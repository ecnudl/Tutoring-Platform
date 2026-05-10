<template>
  <div class="page-container">
    <h2 class="page-title">需求审核</h2>
    <div class="filter-bar">
      <el-select v-model="reqStatus" placeholder="状态" clearable style="width:140px">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="1" />
        <el-option label="已发布" :value="2" />
        <el-option label="已接单" :value="3" />
        <el-option label="已驳回" :value="6" />
        <el-option label="已关闭(过期)" :value="5" />
      </el-select>
      <el-input v-model="keyword" placeholder="标题搜索" style="width:200px" clearable @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column prop="contactMobile" label="联系电话" width="130" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.reqStatus)" size="small">{{ statusLabel(row.reqStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">详情</el-button>
          <template v-if="row.reqStatus === 1">
            <el-button size="small" type="success" @click="handleAudit(row, 'approve')">通过</el-button>
            <el-button size="small" type="danger" @click="handleAudit(row, 'reject')">驳回</el-button>
          </template>
          <el-button v-if="row.reqStatus === 2" size="small" type="warning" @click="handleMatch(row)">标为已接单</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="需求详情" width="900px" top="4vh">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="标题" :span="2">{{ detail.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.contactName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactMobile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预算">{{ detail.budgetMin || 0 }}-{{ detail.budgetMax || 0 }} 元/时</el-descriptions-item>
        <el-descriptions-item label="时间安排">{{ detail.schedule || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学生情况" :span="2">
          <pre class="req-text">{{ detail.studentInfo || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="教员要求" :span="2">
          <pre class="req-text">{{ detail.tutorRequest || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="交通信息" :span="2">
          <pre class="req-text">{{ detail.trafficInfo || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="补充说明" :span="2">
          <pre class="req-text">{{ detail.requirementDetail || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(detail.reqStatus)">{{ statusLabel(detail.reqStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注">{{ detail.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 教员申请列表 -->
      <div v-if="detail" class="apps-section">
        <div class="apps-title">
          教员申请 <span class="apps-count">{{ apps.length }}</span>
        </div>
        <el-table v-if="apps.length" :data="apps" border size="small" stripe>
          <el-table-column label="教员 ID" width="190">
            <template #default="{ row }">
              <span class="mono">T{{ row.tutorDisplayNo || row.tutorId }}</span>
              <el-button size="small" link type="primary" @click="copy(row.tutorDisplayNo ? 'T'+row.tutorDisplayNo : String(row.tutorId))" style="margin-left:6px">复制</el-button>
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="80">
            <template #default="{ row }">{{ row.tutorRealName || '-' }}</template>
          </el-table-column>
          <el-table-column label="学校" min-width="140">
            <template #default="{ row }">{{ row.tutorUniversity || '-' }}</template>
          </el-table-column>
          <el-table-column label="联系电话" width="120">
            <template #default="{ row }">{{ row.mobile || '-' }}</template>
          </el-table-column>
          <el-table-column label="申请说明" min-width="220">
            <template #default="{ row }">
              <span :title="row.applyMessage" class="apply-msg">{{ truncate(row.applyMessage, 40) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag size="small" :type="appStatusTag(row.appStatus)">{{ appStatusLabel(row.appStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="{ row }">
              <template v-if="row.appStatus === 0 || row.appStatus === 1">
                <el-popconfirm title="将此教员匹配该需求? 同时驳回其他申请, 创建预约关联." confirm-button-text="确认匹配" @confirm="handleMatchApp(row)">
                  <template #reference>
                    <el-button size="small" type="primary">匹配</el-button>
                  </template>
                </el-popconfirm>
                <el-popconfirm title="确认驳回该申请?" confirm-button-text="驳回" @confirm="handleRejectApp(row)">
                  <template #reference>
                    <el-button size="small" type="danger" plain>驳回</el-button>
                  </template>
                </el-popconfirm>
              </template>
              <span v-else style="color:#94a3b8">—</span>
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="apps-empty">暂无教员申请</div>
      </div>
    </el-dialog>

    <!-- 审核弹窗 -->
    <el-dialog v-model="auditVisible" :title="auditAction === 'approve' ? '审核通过' : '审核驳回'" width="450px">
      <el-form label-width="80px">
        <el-form-item label="备注">
          <el-input v-model="auditRemark" type="textarea" :rows="3" :placeholder="auditAction === 'reject' ? '请输入驳回原因（必填）' : '审核备注（选填）'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button :type="auditAction === 'approve' ? 'success' : 'danger'" :loading="submitting" @click="submitAudit">
          {{ auditAction === 'approve' ? '确认通过' : '确认驳回' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const reqStatus = ref(1)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const detailVisible = ref(false)
const detail = ref<any>(null)
const apps = ref<any[]>([])
const auditVisible = ref(false)
const auditAction = ref('')
const auditRemark = ref('')
const auditRow = ref<any>(null)
const submitting = ref(false)

const statusLabel = (s: number) => ({ 0: '草稿', 1: '待审核', 2: '已发布', 3: '已接单', 5: '已关闭', 6: '已驳回' }[s] || '未知')
const statusTagType = (s: number) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'warning', 5: 'info', 6: 'danger' }[s] || 'info')

const appStatusLabel = (s: number) => ({ 0: '已申请', 1: '入围', 2: '已录用', 3: '已驳回' }[s] || '未知')
const appStatusTag = (s: number) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }[s] || 'info')

const truncate = (s: string, n: number) => {
  if (!s) return ''
  return s.length > n ? s.slice(0, n) + '…' : s
}

const copy = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('已复制: ' + text)
  } catch {
    ElMessage.warning('复制失败, 请手动选择文本')
  }
}

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/requirement-audit/page', {
      pageCurrent: page.value, pageSize: 20,
      reqStatus: reqStatus.value,
      keyword: keyword.value || undefined
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const viewDetail = async (row: any) => {
  try {
    const res = await get('/user/admin/requirement-audit/view', { id: row.id })
    if (res.code === 200) {
      detail.value = res.data
      detailVisible.value = true
      apps.value = []
      // 拉取此需求的教员申请列表
      try {
        const ar = await post('/user/admin/application/page', {
          pageCurrent: 1, pageSize: 50, requirementId: row.id
        })
        if (ar.code === 200 && ar.data) apps.value = ar.data.list || []
      } catch (e) { console.error('load apps failed', e) }
    }
  } catch (e) { console.error(e) }
}

const handleMatchApp = async (row: any) => {
  try {
    const res = await put(`/user/admin/application/match?id=${row.id}`)
    if (res.code === 200) {
      ElMessage.success(res.data || '匹配成功')
      // 刷新需求列表 + 关闭详情
      await search()
      detailVisible.value = false
    } else {
      ElMessage.error(res.msg || '匹配失败')
    }
  } catch { ElMessage.error('网络错误') }
}

const handleRejectApp = async (row: any) => {
  try {
    const res = await put(`/user/admin/application/reject?id=${row.id}`)
    if (res.code === 200) {
      ElMessage.success(res.data || '已驳回')
      // 重新拉取本需求的申请列表
      const ar = await post('/user/admin/application/page', {
        pageCurrent: 1, pageSize: 50, requirementId: detail.value?.id
      })
      if (ar.code === 200 && ar.data) apps.value = ar.data.list || []
    } else {
      ElMessage.error(res.msg || '驳回失败')
    }
  } catch { ElMessage.error('网络错误') }
}

const handleAudit = (row: any, action: string) => {
  auditRow.value = row; auditAction.value = action; auditRemark.value = ''; auditVisible.value = true
}

const submitAudit = async () => {
  if (auditAction.value === 'reject' && !auditRemark.value) { ElMessage.warning('请输入驳回原因'); return }
  submitting.value = true
  try {
    const url = auditAction.value === 'approve' ? '/user/admin/requirement-audit/approve' : '/user/admin/requirement-audit/reject'
    const res = await put(url, { id: auditRow.value.id, auditRemark: auditRemark.value })
    if (res.code === 200) { ElMessage.success(res.data); auditVisible.value = false; await search() }
    else { ElMessage.error(res.msg) }
  } catch (e) { ElMessage.error('网络错误') }
  finally { submitting.value = false }
}

onMounted(() => { search() })

const handleMatch = async (row: any) => {
  let remark = ''
  try {
    const r = await ElMessageBox.prompt(
      '标为「已接单」？这会让需求在学员库显示已接单, 并在 1 周后自动清理。\n\n备注（接单教员姓名/电话, 便于以后查账）：',
      '标为已接单',
      { confirmButtonText: '确认', cancelButtonText: '取消', inputType: 'textarea', inputPlaceholder: '可选, 例如：李教员（13xxxx1234）' }
    )
    remark = r.value || ''
  } catch { return }
  try {
    const res = await put('/user/admin/requirement-audit/match', { id: row.id, matchedTutorRemark: remark })
    if (res.code === 200) { ElMessage.success(res.data); await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('网络错误') }
}
</script>

<style scoped>
.page-container .req-text {
  margin: 0;
  padding: 0;
  font-family: inherit;
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.7;
  color: #1F2937;
  font-size: 13px;
  max-height: 240px;
  overflow-y: auto;
}
.apps-section { margin-top: 18px; }
.apps-title {
  font-size: 14px;
  font-weight: 600;
  color: #1F2937;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 2px solid #f1f5f9;
}
.apps-count {
  display: inline-block;
  background: #163B6B;
  color: #fff;
  border-radius: 10px;
  padding: 1px 8px;
  font-size: 12px;
  margin-left: 6px;
}
.apps-section :deep(.mono) {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12.5px;
  color: #163B6B;
  font-weight: 500;
}
.apps-section :deep(.apply-msg) {
  font-size: 12.5px;
  color: #4b5563;
  display: inline-block;
  max-width: 100%;
}
.apps-empty {
  padding: 20px;
  text-align: center;
  color: #94a3b8;
  background: #f8fafc;
  border-radius: 6px;
  font-size: 13px;
}
</style>
