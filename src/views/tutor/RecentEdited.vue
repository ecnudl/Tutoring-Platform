<template>
  <div class="page-container">
    <h2 class="page-title">教员资料近期编辑</h2>
    <p style="color:#94a3b8;margin-bottom:14px;font-size:13px">
      已审核通过 (audit_status 3 / 4) 的教员 profile, 按 gmt_modified 倒序; 用于运营巡检教员是否擅自修改简介 / 价格 / 科目等已审通过内容.
    </p>

    <div class="filter-bar">
      <span style="font-size:13px;color:#475569">最近</span>
      <el-select v-model="days" style="width:100px" @change="load">
        <el-option label="7 天" :value="7" />
        <el-option label="30 天" :value="30" />
        <el-option label="90 天" :value="90" />
      </el-select>
      <el-button type="primary" @click="load">刷新</el-button>
    </div>

    <el-table :data="list" border stripe v-loading="loading" empty-text="暂无近期编辑">
      <el-table-column prop="displayNo" label="编号" width="100" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="userId" label="user_id" width="180" />
      <el-table-column label="审核状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.auditStatus === 3 ? 'success' : 'info'" size="small">
            {{ row.auditStatus === 3 ? '已发布' : (row.auditStatus === 4 ? '已通过' : row.auditStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="tutorType" label="教员类型" width="90">
        <template #default="{ row }">{{ tutorTypeMap[row.tutorType] || '-' }}</template>
      </el-table-column>
      <el-table-column label="价格" width="120">
        <template #default="{ row }">{{ row.priceMin ? `${row.priceMin}-${row.priceMax || row.priceMin}` : '-' }} 元/h</template>
      </el-table-column>
      <el-table-column label="近期修改时间" width="180">
        <template #default="{ row }">{{ row.gmtModified }}</template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" @click="goDetail(row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { post } from '@/api/index'

const router = useRouter()
const days = ref(30)
const list = ref<any[]>([])
const loading = ref(false)
const tutorTypeMap: Record<number, string> = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/tutor-audit/recent-edited', { days: days.value, limit: 50 })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const goDetail = (row: any) => router.push(`/tutor/detail/${row.id}`)

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; gap: 12px; align-items: center; margin-bottom: 16px; }
</style>
