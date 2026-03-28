<template>
<div>
  <h2 class="page-title">修改密码</h2>
  <el-card style="max-width:500px">
    <el-form label-width="100px">
      <el-form-item label="手机号">
        <el-input :model-value="userStore.mobile" disabled />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.password" type="password" placeholder="输入新密码(6位以上)" show-password />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入新密码" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="handleSave">确认修改</el-button>
      </el-form-item>
    </el-form>
    <el-alert title="修改密码后需要重新登录" type="info" :closable="false" style="margin-top:12px" />
  </el-card>
</div>
</template>
<script setup>
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'
definePageMeta({ layout: 'center' })
const userStore = useUserStore()
const router = useRouter()
const form = ref({ password: '', confirmPassword: '' })
const saving = ref(false)
const handleSave = () => {
  if (!form.value.password || form.value.password.length < 6) { ElMessage.warning('密码不能少于6位'); return }
  if (form.value.password !== form.value.confirmPassword) { ElMessage.warning('两次密码不一致'); return }
  ElMessage.info('修改密码功能需要对接短信验证码，当前暂不支持。请联系管理员重置密码。')
}
</script>
