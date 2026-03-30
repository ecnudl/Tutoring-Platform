<template>
  <div class="page-container">
    <h2 class="page-title">申请管理</h2>
    <div class="filter-bar">
      <el-select v-model="appStatus" placeholder="状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="已申请" :value="0" />
        <el-option label="入围" :value="1" />
        <el-option label="录用" :value="2" />
        <el-option label="拒绝" :value="3" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="id" label="ID" width="180" />
      <el-table-column prop="requirementId" label="需求ID" width="180" />
      <el-table-column prop="tutorId" label="教员ID" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.appStatus)" size="small">{{ statusLabel(row.appStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="applyMessage" label="自荐语" show-overflow-tooltip />
      <el-table-column prop="gmtCreate" label="申请时间" width="170" />
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="申请详情" width="500px">
      <el-descriptions :column="1" border v-if="detail">
        <el-descriptions-item label="需求ID">{{ detail.requirementId }}</el-descriptions-item>
        <el-descriptions-item label="教员ID">{{ detail.tutorId }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusType(detail.appStatus)">{{ statusLabel(detail.appStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="自荐语">{{ detail.applyMessage || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detail.gmtCreate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get } from '@/api/index'

const appStatus = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)

const statusLabel = (s: number) => ({ 0:'已申请',1:'入围',2:'录用',3:'拒绝' }[s] || '未知')
const statusType = (s: number) => ({ 0:'info',1:'warning',2:'success',3:'danger' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/application/page', {
      pageCurrent: page.value, pageSize: 20, appStatus: appStatus.value
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
    const res = await get('/user/admin/application/view', { id: row.id })
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

onMounted(() => { search() })
</script>
