<template>
<div>
  <h2 class="page-title">我的收藏</h2>
  <el-table :data='[]' empty-text='暂无收藏'><el-table-column prop='targetType' label='类型' /><el-table-column prop='name' label='名称' /><el-table-column label='操作'><template #default><el-button size='small' type='danger'>取消收藏</el-button></template></el-table-column></el-table>
</div>
</template>
<script setup>
definePageMeta({ layout: "center" })
</script>
