<template>
<div>
  <h2 class="page-title">修改头像</h2>

  <div class="hp-tips">
    <template v-if="userStore.isTutor">
      <p>1. 上传<span class="hl">高清</span>、<span class="hl">友善</span>、<span class="hl">真实</span>的头像，会获得家长更多<span class="hl">信任</span>。</p>
      <p>2. 教员库<span class="hl">排序靠前</span>，展示更多。</p>
      <p>3. 本站<span class="hl">优先推荐</span>，更容易接到订单哦！</p>
    </template>
    <template v-else>
      <p>上传<span class="hl">真实</span>的头像，方便教员第一时间识别您。</p>
      <p>建议使用<span class="hl">清晰</span>的正面照，体现<span class="hl">友善</span>形象。</p>
    </template>
  </div>

  <div class="hp-preview-wrap">
    <div class="hp-preview" :class="{ 'is-empty': !currentUrl }">
      <img v-if="currentUrl" :src="currentUrl" :alt="userStore.isTutor ? '教员头像' : '用户头像'" />
      <svg v-else width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="1.5">
        <circle cx="12" cy="8" r="4" />
        <path d="M5 21c0-4 3-7 7-7s7 3 7 7" />
      </svg>
    </div>
  </div>

  <div class="hp-action">
    <input ref="fileInput" type="file" accept="image/jpeg,image/png,image/webp" hidden @change="onPicked" />
    <button class="hp-btn" :disabled="uploading" @click="triggerPick">
      <span v-if="uploading">上传中...</span>
      <span v-else>点击上传形象照</span>
    </button>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const userStore = useUserStore()
const { get, post } = useApi()

const fileInput = ref(null)
const uploading = ref(false)
const currentUrl = ref('')

const triggerPick = () => {
  if (uploading.value) return
  fileInput.value && fileInput.value.click()
}

const onPicked = async (e) => {
  const file = e.target.files && e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 5MB')
    e.target.value = ''
    return
  }
  if (!/^image\/(jpeg|png|webp)$/.test(file.type)) {
    ElMessage.warning('仅支持 jpg / png / webp')
    e.target.value = ''
    return
  }
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('picFile', file)
    const upRes = await post('/system/auth/upload/pic', fd)
    if (upRes?.code === 301 || upRes?.code === 302 || upRes?.code === 99) {
      ElMessage.error('登录已过期, 请重新登录')
      const redirect = encodeURIComponent('/center/head-photo')
      setTimeout(() => { location.href = '/login?redirect=' + redirect }, 1500)
      return
    }
    if (upRes?.code !== 200 || !upRes?.data) {
      ElMessage.error(upRes?.msg || '上传失败')
      return
    }
    const url = upRes.data
    // 教员: 改 tutor_profile.avatar (走专用 /avatar 端点, 不重置审核状态); 家长: 改 users.user_head
    const saveRes = userStore.isTutor
      ? await post('/user/auth/tutor-profile/avatar', { avatar: url })
      : await post('/user/auth/users/avatar', { userHead: url })
    if (saveRes?.code !== 200) {
      ElMessage.error(saveRes?.msg || '保存失败')
      return
    }
    currentUrl.value = url
    ElMessage.success('头像已更新')
  } catch (err) {
    console.error('[head-photo] error', err)
    ElMessage.error('上传失败, 请重试')
  } finally {
    uploading.value = false
    e.target.value = ''
  }
}

const loadCurrent = async () => {
  try {
    if (userStore.isTutor) {
      const r = await get('/user/auth/tutor-profile/view')
      if (r?.code === 200 && r.data?.avatar) currentUrl.value = r.data.avatar
    } else {
      const r = await get('/user/auth/users/view')
      if (r?.code === 200 && r.data?.userHead) currentUrl.value = r.data.userHead
    }
  } catch (e) { console.warn('[head-photo] load current failed', e) }
}

onMounted(loadCurrent)
</script>

<style scoped>
.hp-tips {
  background: #f8fafc;
  border-radius: 8px;
  padding: 18px 22px;
  margin-bottom: 32px;
  font-size: 14px;
  line-height: 2;
  color: #475569;
}
.hp-tips p { margin: 0; }
.hp-tips .hl {
  color: var(--color-accent-dark);
  font-weight: 600;
}

.hp-preview-wrap {
  display: flex;
  justify-content: center;
  margin: 32px 0 28px;
}
.hp-preview {
  width: 220px;
  height: 280px;
  border: 1.5px dashed #cbd5e1;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: #fff;
}
.hp-preview.is-empty {
  background: #fafafa;
}
.hp-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hp-action {
  display: flex;
  justify-content: center;
}
.hp-btn {
  background: var(--color-accent);
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 12px 40px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 2px;
  cursor: pointer;
  transition: background-color .15s;
}
.hp-btn:hover:not(:disabled) {
  background: var(--color-accent-dark);
}
.hp-btn:disabled {
  background: #cbd5e1;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .hp-preview { width: 180px; height: 230px; }
  .hp-btn { padding: 10px 32px; font-size: 14px; }
}
</style>
