<template>
<div class="tp-page">
  <div class="tp-loading" v-if="state === 'loading'">
    <el-icon class="is-loading" :size="22"><Loading /></el-icon>
    <span>加载简历预览...</span>
  </div>
  <div class="tp-error" v-else-if="state === 'no-profile'">
    <p>您还没有教员资料, 无法预览。</p>
    <NuxtLink to="/center/tutor-profile">
      <el-button type="primary">先去填写教员资料</el-button>
    </NuxtLink>
  </div>
  <div class="tp-error" v-else-if="state === 'error'">
    <p>{{ errMsg || '加载失败' }}</p>
    <NuxtLink to="/center/tutor-profile">
      <el-button>返回个人中心</el-button>
    </NuxtLink>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const { get } = useApi()
const router = useRouter()

const state = ref('loading')
const errMsg = ref('')

onMounted(async () => {
  try {
    const res = await get('/user/auth/tutor-profile/view')
    if (res?.code !== 200 || !res?.data) {
      state.value = 'no-profile'
      errMsg.value = res?.msg || ''
      return
    }
    const displayNo = res.data.displayNo || ''
    if (!displayNo) {
      state.value = 'no-profile'
      return
    }
    // 跳到公开教员详情页, 看到的就是学员从教员库点你头像的样子
    const id = String(displayNo).replace(/^T/i, '')
    router.replace('/jy/t' + id)
  } catch (e) {
    state.value = 'error'
    errMsg.value = '网络错误, 请稍后重试'
    console.error('[tutor-preview]', e)
  }
})
</script>

<style scoped>
.tp-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 360px;
}
.tp-loading {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--color-text-secondary, #64748b);
  font-size: 14px;
}
.tp-error {
  text-align: center;
  color: var(--color-text-secondary, #64748b);
}
.tp-error p { margin: 0 0 16px; font-size: 14px; }
</style>
