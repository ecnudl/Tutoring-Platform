<template>
  <div class="container" style="padding:20px">
    <el-card style="max-width:900px;margin:0 auto">
      <h1 style="margin-bottom:20px">教员详情</h1>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">--</el-descriptions-item>
        <el-descriptions-item label="性别">--</el-descriptions-item>
        <el-descriptions-item label="学历">--</el-descriptions-item>
        <el-descriptions-item label="学校">--</el-descriptions-item>
        <el-descriptions-item label="专业">--</el-descriptions-item>
        <el-descriptions-item label="教员类型">--</el-descriptions-item>
        <el-descriptions-item label="授课科目">--</el-descriptions-item>
        <el-descriptions-item label="课时费">--元/小时</el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">--</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top:20px;display:flex;gap:12px">
        <el-button type="primary">预约试讲</el-button>
        <el-button>收藏</el-button>
        <el-button @click="$router.back()">返回</el-button>
      </div>
    </el-card>
  </div>
</template>
<script setup>
const route = useRoute()
const tutorId = route.params.id
</script>
