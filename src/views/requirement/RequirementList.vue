<template>
  <div class="page-container">
    <h2 class="page-title">需求列表</h2>
    <div class="filter-bar">
      <el-select v-model="reqStatus" placeholder="状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="草稿" :value="0" />
        <el-option label="审核中" :value="1" />
        <el-option label="已发布" :value="2" />
        <el-option label="已关闭" :value="5" />
        <el-option label="已驳回" :value="6" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.reqStatus)" size="small">{{ statusLabel(row.reqStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column prop="applicationCount" label="申请数" width="80" />
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="需求详情" width="600px">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="标题" :span="2">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactMobile }}</el-descriptions-item>
        <el-descriptions-item label="预算">{{ detail.budgetMin }}-{{ detail.budgetMax }}元/时</el-descriptions-item>
        <el-descriptions-item label="性别要求">{{ {0:'不限',1:'男',2:'女'}[detail.tutorGender] || '不限' }}</el-descriptions-item>
        <el-descriptions-item label="时间安排" :span="2">{{ detail.schedule || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细需求" :span="2">{{ detail.requirementDetail || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusType(detail.reqStatus)">{{ statusLabel(detail.reqStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="浏览/申请">{{ detail.viewCount }}/{{ detail.applicationCount }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get } from '@/api/index'

const reqStatus = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)

const statusLabel = (s: number) => ({ 0:'草稿',1:'审核中',2:'已发布',3:'已匹配',4:'已完成',5:'已关闭',6:'已驳回' }[s] || '未知')
const statusType = (s: number) => ({ 0:'info',1:'warning',2:'success',5:'info',6:'danger' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/requirement-audit/page', {
      pageCurrent: page.value, pageSize: 20,
      auditStatus: reqStatus.value
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

onMounted(() => { search() })
</script>
