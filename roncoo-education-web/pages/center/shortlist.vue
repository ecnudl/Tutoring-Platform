<template>
<div>
  <h2 class="page-title">备选老师</h2>
  <el-table :data='[]' empty-text='暂无备选老师'><el-table-column prop='tutorName' label='教员' /><el-table-column label='操作'><template #default><el-button size='small'>查看</el-button><el-button size='small' type='danger'>移除</el-button></template></el-table-column></el-table>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
