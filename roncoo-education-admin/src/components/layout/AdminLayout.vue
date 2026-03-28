<template>
  <el-container style="height:100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="background:#304156;transition:width .3s">
      <div style="height:60px;display:flex;align-items:center;justify-content:center;color:#fff;font-size:18px;font-weight:700">
        {{ isCollapse ? '家' : '家教管理后台' }}
      </div>
      <el-menu :default-active="$route.path" router :collapse="isCollapse" background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/dashboard"><el-icon><House /></el-icon><span>首页</span></el-menu-item>
        <el-sub-menu index="tutor"><template #title><el-icon><User /></el-icon><span>教员管理</span></template>
          <el-menu-item index="/tutor/audit">教员审核</el-menu-item><el-menu-item index="/tutor/list">教员列表</el-menu-item></el-sub-menu>
        <el-sub-menu index="requirement"><template #title><el-icon><Document /></el-icon><span>需求管理</span></template>
          <el-menu-item index="/requirement/audit">需求审核</el-menu-item><el-menu-item index="/requirement/list">需求列表</el-menu-item></el-sub-menu>
        <el-menu-item index="/reservation/list"><el-icon><Clock /></el-icon><span>预约管理</span></el-menu-item>
        <el-menu-item index="/application/list"><el-icon><List /></el-icon><span>申请管理</span></el-menu-item>
        <el-menu-item index="/student/list"><el-icon><UserFilled /></el-icon><span>学员管理</span></el-menu-item>
        <el-menu-item index="/feedback/list"><el-icon><ChatDotRound /></el-icon><span>反馈管理</span></el-menu-item>
        <el-menu-item index="/vip/list"><el-icon><StarFilled /></el-icon><span>VIP管理</span></el-menu-item>
        <el-sub-menu index="dict"><template #title><el-icon><Collection /></el-icon><span>字典管理</span></template>
          <el-menu-item index="/dict/subject">科目</el-menu-item><el-menu-item index="/dict/grade">年级</el-menu-item><el-menu-item index="/dict/tag">标签</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,.08)">
        <el-icon style="cursor:pointer;font-size:20px" @click="isCollapse=!isCollapse"><Fold /></el-icon>
        <el-dropdown><span style="cursor:pointer">管理员 <el-icon><ArrowDown /></el-icon></span>
          <template #dropdown><el-dropdown-menu><el-dropdown-item @click="logout">退出登录</el-dropdown-item></el-dropdown-menu></template>
        </el-dropdown>
      </el-header>
      <el-main style="background:#f0f2f5;padding:20px"><router-view /></el-main>
    </el-container>
  </el-container>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { House, User, UserFilled, Document, Clock, List, ChatDotRound, StarFilled, Collection, Fold, ArrowDown } from '@element-plus/icons-vue'
const isCollapse = ref(false)
const router = useRouter()
const logout = () => { localStorage.removeItem('admin_token'); router.push('/login') }
</script>
