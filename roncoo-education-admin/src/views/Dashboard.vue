<template>
  <div class="page-container">
    <h2 class="page-title">仪表盘</h2>
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:16px;margin-bottom:24px">
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/tutor/audit')">
        <el-statistic title="待审核教员" :value="stats.pendingTutors" />
        <template #extra><el-tag type="warning" size="small" v-if="stats.pendingTutors > 0">需处理</el-tag></template>
      </el-card>
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/requirement/audit')">
        <el-statistic title="待审核需求" :value="stats.pendingRequirements" />
        <template #extra><el-tag type="warning" size="small" v-if="stats.pendingRequirements > 0">需处理</el-tag></template>
      </el-card>
      <el-card shadow="hover"><el-statistic title="已通过教员" :value="stats.approvedTutors" /></el-card>
      <el-card shadow="hover"><el-statistic title="已发布需求" :value="stats.publishedRequirements" /></el-card>
      <el-card shadow="hover"><el-statistic title="全部教员" :value="stats.totalTutors" /></el-card>
      <el-card shadow="hover"><el-statistic title="全部需求" :value="stats.totalRequirements" /></el-card>
    </div>
    <div style="margin-top:20px">
      <h3 style="margin-bottom:12px">快捷操作</h3>
      <el-button type="primary" @click="$router.push('/tutor/audit')">教员审核</el-button>
      <el-button type="primary" @click="$router.push('/requirement/audit')">需求审核</el-button>
      <el-button @click="$router.push('/tutor/list')">教员列表</el-button>
      <el-button @click="$router.push('/requirement/list')">需求列表</el-button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post } from '@/api/index'

const stats = ref({
  pendingTutors: 0, approvedTutors: 0, totalTutors: 0,
  pendingRequirements: 0, publishedRequirements: 0, totalRequirements: 0
})

const loadStats = async () => {
  try {
    // Load tutor stats
    const [pending, approved, all] = await Promise.all([
      post('/user/admin/tutor-audit/page', { pageCurrent: 1, pageSize: 1, auditStatus: 1 }),
      post('/user/admin/tutor-audit/page', { pageCurrent: 1, pageSize: 1, auditStatus: 2 }),
      post('/user/admin/tutor-audit/page', { pageCurrent: 1, pageSize: 1 })
    ])
    stats.value.pendingTutors = pending?.data?.totalCount || 0
    stats.value.approvedTutors = approved?.data?.totalCount || 0
    stats.value.totalTutors = all?.data?.totalCount || 0

    // Load requirement stats
    const [rPending, rPub, rAll] = await Promise.all([
      post('/user/admin/requirement-audit/page', { pageCurrent: 1, pageSize: 1, reqStatus: 1 }),
      post('/user/admin/requirement-audit/page', { pageCurrent: 1, pageSize: 1, reqStatus: 2 }),
      post('/user/admin/requirement-audit/page', { pageCurrent: 1, pageSize: 1 })
    ])
    stats.value.pendingRequirements = rPending?.data?.totalCount || 0
    stats.value.publishedRequirements = rPub?.data?.totalCount || 0
    stats.value.totalRequirements = rAll?.data?.totalCount || 0
  } catch (e) { console.error(e) }
}

onMounted(() => { loadStats() })
</script>
