<template>
  <div class="page-container">
    <h2 class="page-title">家教感言审核</h2>
    <p class="page-tip">家教感言由教员/家长在用户中心提交，审核通过后展示在网站首页"家教感言"栏目。</p>
    <div class="filter-bar">
      <el-select v-model="fbStatus" placeholder="审核状态" clearable style="width:160px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="userId" label="用户ID" width="180" />
      <el-table-column prop="content" label="感言内容" show-overflow-tooltip />
      <el-table-column prop="contact" label="署名/联系" width="140" />
      <el-table-column label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="fbTagType(row.fbStatus)" size="small">{{ fbLabel(row.fbStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reply" label="审核备注" show-overflow-tooltip />
      <el-table-column prop="gmtCreate" label="提交时间" width="170" />
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
          <el-button size="small" type="success" v-if="row.fbStatus===0" @click="handleAudit(row, 'pass')">通过</el-button>
          <el-button size="small" type="danger"  v-if="row.fbStatus===0" @click="handleAudit(row, 'reject')">驳回</el-button>
          <el-button size="small" type="warning" v-if="row.fbStatus===1" @click="handleAudit(row, 'unshelf')">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="感言详情" width="500px">
      <el-descriptions :column="1" border v-if="detail">
        <el-descriptions-item label="用户ID">{{ detail.userId }}</el-descriptions-item>
        <el-descriptions-item label="内容">{{ detail.content }}</el-descriptions-item>
        <el-descriptions-item label="署名/联系">{{ detail.contact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="fbTagType(detail.fbStatus)">{{ fbLabel(detail.fbStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注" v-if="detail.reply">{{ detail.reply }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detail.gmtCreate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="auditVisible" :title="auditAction==='pass'?'审核通过':(auditAction==='unshelf'?'下架感言':'审核驳回')" width="450px">
      <el-form label-width="90px">
        <el-form-item label="感言内容">
          <div style="color:#666">{{ auditRow?.content }}</div>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input v-model="auditNote" type="textarea" :rows="3" :placeholder="auditAction==='pass' ? '可选：留下审核备注' : '请说明驳回原因（选填）'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button :type="auditAction==='pass'?'success':'danger'" :loading="auditing" @click="submitAudit">
          {{ auditAction==='pass'?'确认通过':(auditAction==='unshelf'?'确认下架':'确认驳回') }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage } from 'element-plus'

const fbStatus = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)
const auditVisible = ref(false)
const auditRow = ref<any>(null)
const auditAction = ref<'pass'|'reject'|'unshelf'>('pass')
const auditNote = ref('')
const auditing = ref(false)

const fbLabel = (s: number) => ({ 0:'待审核',1:'已通过',2:'已驳回' }[s] || '未知')
const fbTagType = (s: number) => ({ 0:'warning',1:'success',2:'info' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/feedback/page', {
      pageCurrent: page.value, pageSize: 20, fbStatus: fbStatus.value
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
    const res = await get('/user/admin/feedback/view', { id: row.id })
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

const handleAudit = (row: any, action: 'pass'|'reject'|'unshelf') => {
  auditRow.value = row
  auditAction.value = action
  auditNote.value = ''
  auditVisible.value = true
}

const submitAudit = async () => {
  auditing.value = true
  try {
    const path = auditAction.value === 'pass'
      ? '/user/admin/feedback/audit-pass'
      : '/user/admin/feedback/audit-reject'  // 'reject' / 'unshelf' 都走同一端点, 文案区分仅在 UI
    const res = await put(path, { id: auditRow.value.id, reply: auditNote.value })
    if (res.code === 200) {
      ElMessage.success(res.data || (auditAction.value === 'pass' ? '已通过' : (auditAction.value === 'unshelf' ? '已下架' : '已驳回')))
      auditVisible.value = false
      await search()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { auditing.value = false }
}

onMounted(() => { search() })
</script>
<style scoped>
.page-tip { color:#64748b;font-size:13px;margin:0 0 14px }
.filter-bar { display:flex;gap:10px;margin-bottom:14px }
</style>
