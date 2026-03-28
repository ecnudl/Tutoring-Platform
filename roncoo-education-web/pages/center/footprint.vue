<template>
<div>
  <h2 class="page-title">浏览足迹</h2>
  <el-button type='danger' size='small' style='margin-bottom:16px'>清空足迹</el-button><el-table :data='[]' empty-text='暂无浏览记录'><el-table-column prop='targetType' label='类型' /><el-table-column prop='name' label='名称' /><el-table-column prop='gmtCreate' label='浏览时间' /></el-table>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
