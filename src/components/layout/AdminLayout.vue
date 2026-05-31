<template>
  <el-container style="height:100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="background:#304156;transition:width .3s">
      <div style="height:60px;display:flex;align-items:center;justify-content:center;color:#fff;font-size:18px;font-weight:700">
        {{ isCollapse ? '家' : '家教管理后台' }}
      </div>
      <el-menu :default-active="$route.path" router :collapse="isCollapse" background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/dashboard"><el-icon><House /></el-icon><span>首页</span></el-menu-item>
        <el-sub-menu index="tutor"><template #title><el-icon><User /></el-icon><span>教员管理</span></template>
          <el-menu-item index="/tutor/audit">注册审核</el-menu-item><el-menu-item index="/tutor/recent-edited">修改重审</el-menu-item><el-menu-item index="/tutor/list">教员列表</el-menu-item><el-menu-item index="/cert/audit">证件审核</el-menu-item></el-sub-menu>
        <el-sub-menu index="requirement"><template #title><el-icon><Document /></el-icon><span>需求管理</span></template>
          <el-menu-item index="/requirement/audit">需求审核</el-menu-item><el-menu-item index="/requirement/list">需求列表</el-menu-item></el-sub-menu>
        <el-menu-item index="/reservation/list"><el-icon><Clock /></el-icon><span>预约管理</span></el-menu-item>
        <el-menu-item index="/student/list"><el-icon><UserFilled /></el-icon><span>学员管理</span></el-menu-item>
        <el-menu-item index="/feedback/list"><el-icon><ChatDotRound /></el-icon><span>反馈管理</span></el-menu-item>
        <el-sub-menu index="content"><template #title><el-icon><Notebook /></el-icon><span>内容管理</span></template>
          <el-menu-item index="/content/banner">轮播图</el-menu-item><el-menu-item index="/content/announcement">公告</el-menu-item><el-menu-item index="/content/faq">FAQ 帮助</el-menu-item><el-menu-item index="/content/tutor-group-qr">教员接单群二维码</el-menu-item><el-menu-item index="/content/friend-links">友情链接</el-menu-item><el-menu-item index="/content/footer-menus">页脚菜单</el-menu-item><el-menu-item index="/content/pages">页面内容</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,.08)">
        <el-icon style="cursor:pointer;font-size:20px" @click="isCollapse=!isCollapse"><Fold /></el-icon>
        <el-dropdown><span style="cursor:pointer">管理员 <el-icon><ArrowDown /></el-icon></span>
          <template #dropdown><el-dropdown-menu><el-dropdown-item @click="openPwd">修改密码</el-dropdown-item><el-dropdown-item divided @click="logout">退出登录</el-dropdown-item></el-dropdown-menu></template>
        </el-dropdown>
      </el-header>
      <el-main style="background:#f0f2f5;padding:20px"><router-view /></el-main>
    </el-container>

    <el-dialog v-model="pwdVisible" title="修改密码" width="420px">
      <el-form :model="pwdForm" label-width="80px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPwd" type="password" show-password placeholder="请输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPwd" type="password" show-password placeholder="6-20 位" />
        </el-form-item>
        <el-form-item label="确认新">
          <el-input v-model="pwdForm.confirmPwd" type="password" show-password placeholder="再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdSaving" @click="submitPwd">确定</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { House, User, UserFilled, Document, Clock, ChatDotRound, Fold, ArrowDown, Notebook } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { put } from '@/api/index'
const isCollapse = ref(false)
const router = useRouter()
const logout = () => { localStorage.removeItem('admin_token'); router.push('/login') }

const pwdVisible = ref(false)
const pwdSaving = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', confirmPwd: '' })
const openPwd = () => { pwdForm.value = { oldPwd: '', newPwd: '', confirmPwd: '' }; pwdVisible.value = true }
const submitPwd = async () => {
  if (!pwdForm.value.oldPwd || !pwdForm.value.newPwd) { ElMessage.warning('请填写完整'); return }
  if (pwdForm.value.newPwd.length < 6 || pwdForm.value.newPwd.length > 20) { ElMessage.warning('新密码需 6-20 位'); return }
  if (pwdForm.value.newPwd !== pwdForm.value.confirmPwd) { ElMessage.warning('两次密码不一致'); return }
  if (pwdForm.value.newPwd === pwdForm.value.oldPwd) { ElMessage.warning('新密码不能与原密码相同'); return }
  pwdSaving.value = true
  try {
    const res: any = await put('/system/admin/sys/user/change-password', {
      oldPassword: pwdForm.value.oldPwd,
      newPassword: pwdForm.value.newPwd
    })
    if (res?.code === 200) {
      pwdVisible.value = false
      ElMessage.success('密码修改成功，请用新密码重新登录')
      localStorage.removeItem('admin_token')
      router.push('/login')
    } else {
      ElMessage.error(res?.msg || '修改失败')
    }
  } catch (e) {
    ElMessage.error('网络错误, 请重试')
  } finally {
    pwdSaving.value = false
  }
}
</script>
