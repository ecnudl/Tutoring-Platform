<template>
  <div class="page-container">
    <h2 class="page-title">审核日志</h2>
    <div class="filter-bar">
      <el-select v-model="auditType" placeholder="审核类型" clearable @change="load" style="width: 150px">
        <el-option value="" label="全部" />
        <el-option value="tutor" label="教员审核" />
        <el-option value="requirement" label="需求审核" />
        <el-option value="certification" label="证书审核" />
      </el-select>
      <el-input v-model="keyword" placeholder="搜索关键词" clearable style="width: 200px" @keyup.enter="load" />
      <el-button type="primary" @click="load">搜索</el-button>
    </div>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ typeMap[row.auditType] || row.auditType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="targetId" label="目标ID" width="120" />
      <el-table-column label="审核动作" width="100">
        <template #default="{ row }">
          <el-tag :type="row.auditAction === 1 ? 'success' : 'danger'" size="small">{{ row.auditAction === 1 ? '通过' : '拒绝' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditRemark" label="审核备注" show-overflow-tooltip />
      <el-table-column prop="auditorName" label="审核人" width="100" />
      <el-table-column prop="gmtCreate" label="时间" width="170" />
    </el-table>
    <el-pagination
      v-if="total > 0"
      style="margin-top: 16px; justify-content: flex-end"
      :current-page="page"
      :page-size="20"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="p => { page = p; load() }"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post } from '@/api'

const typeMap: Record<string, string> = { tutor: '教员审核', requirement: '需求审核', certification: '证书审核' }
const auditType = ref('')
const keyword = ref('')
const list = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)

const load = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/audit-log/page', {
      auditType: auditType.value || undefined,
      keyword: keyword.value || undefined,
      pageCurrent: page.value,
      pageSize: 20
    })
    list.value = res.data?.list || res.data || []
    total.value = res.data?.totalCount || 0
  } finally { loading.value = false }
}

onMounted(load)
</script>
