<template>
  <div class="layout-center">
    <!-- 面包屑 -->
    <div class="container">
      <div class="center-crumbs">
        <NuxtLink to="/">
          <el-icon :size="14"><Location /></el-icon>首页
        </NuxtLink>
        <span class="sep">/</span>
        <NuxtLink to="/center">个人中心</NuxtLink>
      </div>
    </div>

    <div class="container">
      <div class="center-wrapper">
        <!-- 左菜单（仿 ttgood 列表式） -->
        <aside class="user-nav-link">
          <NuxtLink to="/center" class="nav-head">个人中心</NuxtLink>

          <div class="nav-group">
            <NuxtLink to="/center/messages">我的消息</NuxtLink>
            <NuxtLink to="/center/received-applications" v-if="!userStore.isTutor">收到的申请</NuxtLink>
            <NuxtLink to="/center/reservations" v-if="!userStore.isTutor">我的预约</NuxtLink>
            <NuxtLink to="/center/orders">我的订单</NuxtLink>
          </div>

          <div class="nav-group">
            <NuxtLink to="/center/head-photo">形象照片</NuxtLink>
          </div>

          <div class="nav-group" v-if="userStore.isTutor">
            <NuxtLink to="/center/tutor-profile">修改简历</NuxtLink>
            <NuxtLink to="/center/tutor-preview">简历预览</NuxtLink>
          </div>

          <div class="nav-group" v-if="!userStore.isTutor">
            <NuxtLink to="/center/favorites">备选教员</NuxtLink>
            <NuxtLink to="/center/shortlist">已备选</NuxtLink>
            <NuxtLink to="/center/footprint">浏览足迹</NuxtLink>
            <NuxtLink to="/center/requirements">我的需求</NuxtLink>
          </div>

          <div class="nav-group">
            <NuxtLink to="/center/credit" v-if="userStore.isTutor">信誉记录</NuxtLink>
            <NuxtLink to="/center/feedback">家教感言</NuxtLink>
          </div>

          <div class="nav-group">
            <a href="javascript:;" @click="handleTutorGroup" class="nav-tutor-group">
              <span class="nav-tg-dot"></span>接单群
            </a>
          </div>

          <div class="nav-group">
            <NuxtLink to="/center/profile">基本资料</NuxtLink>
            <NuxtLink to="/center/certifications">证件认证</NuxtLink>
            <a href="javascript:;" @click="openPwd">修改密码</a>
            <a href="javascript:;" @click="handleLogout">退出登录</a>
          </div>
        </aside>

        <!-- 右侧主内容 -->
        <main class="center-main">
          <slot />
        </main>
      </div>
    </div>

    <el-dialog v-model="pwdVisible" title="修改密码" width="420px">
      <el-form :model="pwdForm" label-width="80px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPwd" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPwd" type="password" show-password placeholder="6-20 位" />
        </el-form-item>
        <el-form-item label="确认新">
          <el-input v-model="pwdForm.confirmPwd" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdSaving" @click="submitPwd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()
const { post } = useApi()

const pwdVisible = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', confirmPwd: '' })

const openPwd = () => { pwdVisible.value = true; pwdForm.value = { oldPwd: '', newPwd: '', confirmPwd: '' } }

const pwdSaving = ref(false)
const submitPwd = async () => {
  if (!pwdForm.value.oldPwd || !pwdForm.value.newPwd) { ElMessage.warning('请填写完整'); return }
  if (pwdForm.value.newPwd.length < 6 || pwdForm.value.newPwd.length > 20) { ElMessage.warning('新密码需 6-20 位'); return }
  if (pwdForm.value.newPwd !== pwdForm.value.confirmPwd) { ElMessage.warning('两次密码不一致'); return }
  if (pwdForm.value.newPwd === pwdForm.value.oldPwd) { ElMessage.warning('新密码不能与原密码相同'); return }
  pwdSaving.value = true
  try {
    const res = await post('/user/auth/users/change-password', {
      oldPassword: pwdForm.value.oldPwd,
      newPassword: pwdForm.value.newPwd
    })
    if (res?.code === 200) {
      pwdVisible.value = false
      ElMessage.success('密码修改成功，请用新密码重新登录')
      userStore.logout()
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

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

const handleTutorGroup = () => {
  if (userStore.isTutor) {
    router.push('/center/tutor-group')
  } else {
    ElMessage.warning('您不是教员，无法进入')
  }
}
</script>

<style scoped>
.layout-center {
  background: var(--color-bg, #f5f6f8);
  min-height: 100vh;
  padding: 20px 0 60px;
}

.container {
  max-width: var(--content-width, 1200px);
  margin: 0 auto;
  padding: 0 var(--space-xl, 20px);
}

.center-crumbs {
  font-size: 13px;
  color: #64748b;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.center-crumbs a {
  color: #64748b;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.center-crumbs a:hover { color: var(--color-primary); }
.center-crumbs .sep { margin: 0 6px; color: #cbd5e1; }

.center-wrapper {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.user-nav-link {
  width: 160px;
  flex-shrink: 0;
  font-size: 15px;
}

.nav-head {
  display: block;
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary, #163B6B);
  padding-bottom: 16px;
  border-bottom: 2px solid var(--color-primary, #163B6B);
  margin-bottom: 16px;
  text-decoration: none;
  letter-spacing: 1px;
}

.nav-group {
  padding: 6px 0;
  border-bottom: 1px solid #f1f5f9;
  margin-bottom: 6px;
}
.nav-group:last-child { border-bottom: none; }

.nav-group a {
  display: block;
  padding: 7px 0;
  color: #475569;
  text-decoration: none;
  transition: color 0.18s ease;
}
.nav-group a:hover { color: var(--color-primary); }
.nav-group a.router-link-active { color: var(--color-primary); font-weight: 600; }

.center-main { flex: 1; min-width: 0; }

@media (max-width: 768px) {
  .center-wrapper { flex-direction: column; }
  .user-nav-link { width: 100%; }
  .nav-head { font-size: 18px; padding-bottom: 10px; margin-bottom: 10px; }
  .nav-group { display: flex; flex-wrap: wrap; gap: 6px 16px; padding: 10px 0; }
  .nav-group a { padding: 4px 0; font-size: 13px; }
}

.nav-tutor-group {
  position: relative;
  font-weight: 600;
  color: var(--color-primary) !important;
}
.nav-tg-dot {
  display: inline-block;
  width: 6px; height: 6px;
  border-radius: 50%;
  background: var(--color-accent);
  margin-right: 8px;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.18);
  vertical-align: middle;
}
</style>
