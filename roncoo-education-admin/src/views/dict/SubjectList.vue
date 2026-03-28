<template>
  <div class="page-container">
    <h2 class="page-title">科目管理</h2>
    <el-button type="primary" style="margin-bottom:16px">新增</el-button>
    <el-table :data="list" border stripe empty-text="暂无数据">
      <el-table-column prop="subjectName" label="科目名称" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default><el-button size="small">编辑</el-button><el-button size="small" type="danger">删除</el-button></template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const list = ref([])
</script>
