<template>
  <div class="page-container">
    <h2 class="page-title">需求审核</h2>
    <div class="filter-bar">
      <el-select v-model="reqStatus" placeholder="状态" clearable style="width:140px">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="1" />
        <el-option label="已发布" :value="2" />
        <el-option label="已驳回" :value="6" />
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
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="需求详情" width="600px">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="标题" :span="2">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactMobile }}</el-descriptions-item>
        <el-descriptions-item label="预算">{{ detail.budgetMin || 0 }}-{{ detail.budgetMax || 0 }}元/时</el-descriptions-item>
        <el-descriptions-item label="时间安排">{{ detail.schedule || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细需求" :span="2">{{ detail.requirementDetail || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(detail.reqStatus)">{{ statusLabel(detail.reqStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注">{{ detail.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
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
import { ElMessage } from 'element-plus'

const keyword = ref('')
const reqStatus = ref(1)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const detailVisible = ref(false)
const detail = ref<any>(null)
const auditVisible = ref(false)
const auditAction = ref('')
const auditRemark = ref('')
const auditRow = ref<any>(null)
const submitting = ref(false)

const statusLabel = (s: number) => ({ 0: '草稿', 1: '待审核', 2: '已发布', 5: '已关闭', 6: '已驳回' }[s] || '未知')
const statusTagType = (s: number) => ({ 0: 'info', 1: 'warning', 2: 'success', 5: 'info', 6: 'danger' }[s] || 'info')

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
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
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
</script>
