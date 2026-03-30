<template>
<div class="page-container">
  <h2 class="page-title">教员详情</h2>
  <el-descriptions :column="2" border v-if="detail" v-loading="loading">
    <el-descriptions-item label="姓名">{{ detail.realName }}</el-descriptions-item>
    <el-descriptions-item label="手机号">{{ detail.mobile }}</el-descriptions-item>
    <el-descriptions-item label="性别">{{ detail.gender === 1 ? '男' : '女' }}</el-descriptions-item>
    <el-descriptions-item label="类型">{{ { 1:'大学生',2:'专职',3:'在职教师',4:'退休教师' }[detail.tutorType] || '-' }}</el-descriptions-item>
    <el-descriptions-item label="学历">{{ { 1:'高中',2:'大专',3:'本科',4:'硕士',5:'博士' }[detail.degree] || '-' }}</el-descriptions-item>
    <el-descriptions-item label="学校">{{ detail.university || '-' }}</el-descriptions-item>
    <el-descriptions-item label="专业">{{ detail.major || '-' }}</el-descriptions-item>
    <el-descriptions-item label="课时费">{{ detail.priceMin }}-{{ detail.priceMax }}元/时</el-descriptions-item>
    <el-descriptions-item label="审核状态">
      <el-tag :type="{ 0:'info',1:'warning',2:'success',3:'danger' }[detail.auditStatus]" size="small">
        {{ { 0:'草稿',1:'待审核',2:'已通过',3:'已驳回' }[detail.auditStatus] }}
      </el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="排序权重">{{ detail.sort || 0 }}</el-descriptions-item>
    <el-descriptions-item label="自我介绍" :span="2">{{ detail.selfIntroduction || '-' }}</el-descriptions-item>
    <el-descriptions-item label="审核备注" :span="2">{{ detail.auditRemark || '-' }}</el-descriptions-item>
  </el-descriptions>
  <div v-if="!detail && !loading" style="text-align:center;padding:40px;color:#999">教员不存在</div>
  <el-button style="margin-top:16px" @click="$router.back()">返回</el-button>
</div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { get } from '@/api/index'

const route = useRoute()
const detail = ref<any>(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await get('/user/admin/tutor/view', { id: route.params.id })
    if (res.code === 200) detail.value = res.data
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})
</script>
