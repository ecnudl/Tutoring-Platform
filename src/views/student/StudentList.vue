<template>
  <div class="page-container">
    <h2 class="page-title">学员管理</h2>
    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="姓名搜索" style="width:200px" clearable @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="realName" label="姓名" width="120" />
      <el-table-column label="性别" width="60">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '-' }}</template>
      </el-table-column>
      <el-table-column prop="grade" label="年级" width="100" />
      <el-table-column prop="school" label="学校" />
      <el-table-column prop="mobile" label="手机号" width="130" />
      <el-table-column prop="gmtCreate" label="注册时间" width="170" />
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="学员详情" width="500px">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="姓名">{{ detail.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.gender === 1 ? '男' : detail.gender === 2 ? '女' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ detail.grade || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ detail.school || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.mobile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ detail.gmtCreate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get } from '@/api/index'

const keyword = ref('')
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/student/page', {
      pageCurrent: page.value, pageSize: 20, keyword: keyword.value || undefined
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
    const res = await get('/user/admin/student/view', { id: row.id })
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

onMounted(() => { search() })
</script>
