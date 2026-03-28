<template>
  <div class="page-container">
    <h2 class="page-title">教员列表</h2>
    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="搜索..." style="width:200px" clearable />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据">
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="mobile" label="手机号" />
        <el-table-column prop="tutorType" label="类型" />
        <el-table-column prop="university" label="学校" />
        <el-table-column prop="auditStatus" label="状态" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="handleView(row)">查看</el-button>\n          <el-button size="small" type="primary" @click="">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" layout="total,prev,pager,next" :total="total" :page-size="20" />
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const keyword = ref('')
const list = ref([])
const total = ref(0)
const search = () => {}
const handleView = (row: any) => {}
</script>
