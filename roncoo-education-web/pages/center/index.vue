<template>
<div>
  <h2 class="page-title">个人中心</h2>
  <p style="margin-bottom:20px;color:#666">欢迎回来，{{ userStore.mobile }}
    <el-tag size="small" :type="userStore.isTutor ? 'warning' : 'success'" style="margin-left:8px">
      {{ userStore.isTutor ? '教员' : '学员' }}
    </el-tag>
  </p>

  <!-- 教员快捷入口 -->
  <template v-if="userStore.isTutor">
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:16px">
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/tutor-profile')">
        <h3 style="color:#409eff">教员资料</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">查看和编辑我的教员信息</p>
      </el-card>
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/certifications')">
        <h3 style="color:#67c23a">资质证书</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">管理我的证书</p>
      </el-card>
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/applications')">
        <h3 style="color:#e6a23c">我的申请</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">查看申请的需求</p>
      </el-card>
    </div>
  </template>

  <!-- 学员快捷入口 -->
  <template v-if="userStore.isStudent">
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:16px">
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/requirements')">
        <h3 style="color:#409eff">我的需求</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">管理发布的需求</p>
      </el-card>
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/requirement-new')">
        <h3 style="color:#67c23a">发布需求</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">发布新的找老师需求</p>
      </el-card>
      <el-card shadow="hover" style="cursor:pointer" @click="$router.push('/center/reservations')">
        <h3 style="color:#e6a23c">我的预约</h3>
        <p style="color:#999;font-size:13px;margin-top:4px">查看预约记录</p>
      </el-card>
    </div>
  </template>
</div>
</template>
<script setup>
import { useUserStore } from '~/stores/user'
definePageMeta({ layout: 'center' })
const userStore = useUserStore()
</script>
