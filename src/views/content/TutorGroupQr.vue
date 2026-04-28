<template>
  <div class="page-container">
    <h2 class="page-title">教员接单群二维码</h2>
    <p class="page-tip">这张图会显示在教员个人中心的「接单群」页面。学员看不到。</p>

    <div class="tg-row">
      <!-- 左：当前二维码预览 -->
      <el-card class="tg-card preview" shadow="never">
        <h4 class="tg-h">当前线上二维码</h4>
        <div class="tg-preview-frame" v-if="currentUrl">
          <img :src="currentUrl" />
        </div>
        <div class="tg-preview-empty" v-else>
          <el-icon :size="40"><PictureFilled /></el-icon>
          <p>暂未上传，教员页面会显示空状态占位。</p>
        </div>
        <div class="tg-url-line" v-if="currentUrl">
          <span class="lbl">URL</span>
          <code>{{ currentUrl }}</code>
        </div>
      </el-card>

      <!-- 右：上传新图 -->
      <el-card class="tg-card upload" shadow="never">
        <h4 class="tg-h">上传新二维码</h4>
        <el-upload
          drag
          :show-file-list="false"
          :http-request="customUpload"
          accept="image/png, image/jpeg, image/jpg"
          :before-upload="beforeUpload"
          class="tg-uploader"
        >
          <div v-if="!stagedUrl">
            <el-icon class="upload-ico" :size="48"><UploadFilled /></el-icon>
            <div class="tg-up-title">点击或拖拽图片到此处</div>
            <div class="tg-up-sub">PNG / JPG，建议正方形且 ≥ 300×300，≤ 2MB</div>
          </div>
          <div v-else class="tg-staged">
            <img :src="stagedUrl" />
            <div class="tg-staged-tip">已上传至 MinIO；点 <strong>保存</strong> 才会发布到线上</div>
          </div>
        </el-upload>

        <div class="tg-actions">
          <el-button :disabled="!stagedUrl" @click="stagedUrl = ''">放弃</el-button>
          <el-button type="primary" :disabled="!stagedUrl || saving" :loading="saving" @click="save">
            保存并发布
          </el-button>
          <el-button type="danger" plain v-if="currentUrl" @click="clearOnline">
            下架（清空）
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, PictureFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { post } from '@/api/index'

const currentUrl = ref<string>('')
const stagedUrl = ref<string>('')
const saving = ref(false)

const loadCurrent = async () => {
  try {
    const res = await fetch('/system/api/site/config').then(r => r.json())
    if (res?.code === 200) currentUrl.value = res.data?.tutorGroupQrUrl || ''
  } catch (e) { console.error(e) }
}

const beforeUpload = (file: File) => {
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不得超过 2MB')
    return false
  }
  return true
}

const customUpload = async (opt: any) => {
  const fd = new FormData()
  fd.append('picFile', opt.file)
  try {
    const token = localStorage.getItem('admin_token') || ''
    const res = await axios.post('/system/admin/upload/pic', fd, {
      headers: { 'Content-Type': 'multipart/form-data', token }
    })
    if (res.data?.code === 200) {
      stagedUrl.value = res.data.data
      ElMessage.success('已上传，点保存才会发布')
    } else {
      ElMessage.error(res.data?.msg || '上传失败')
    }
  } catch (e: any) {
    ElMessage.error('上传失败：' + (e?.message || '网络错误'))
  }
}

const save = async () => {
  if (!stagedUrl.value) return
  saving.value = true
  try {
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: 'tutorGroupQrUrl',
      configValue: stagedUrl.value,
      configName: '教员接单群二维码'
    })
    if (res.code === 200) {
      ElMessage.success('已发布。教员端刷新即可看到新二维码')
      currentUrl.value = stagedUrl.value
      stagedUrl.value = ''
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const clearOnline = async () => {
  try {
    await ElMessageBox.confirm('确认清空线上二维码？教员端会显示"暂未发布"占位。', '下架确认', {
      confirmButtonText: '确认下架', cancelButtonText: '取消', type: 'warning'
    })
  } catch { return }
  try {
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: 'tutorGroupQrUrl',
      configValue: '',
      configName: '教员接单群二维码'
    })
    if (res.code === 200) {
      ElMessage.success('已下架')
      currentUrl.value = ''
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(loadCurrent)
</script>

<style scoped>
.page-tip { color: #64748b; font-size: 13px; margin: 0 0 18px; }

.tg-row {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 16px;
}
@media (max-width: 980px) {
  .tg-row { grid-template-columns: 1fr; }
}

.tg-card { border: 1px solid #e5e7eb; border-radius: 8px; }
.tg-h { margin: 0 0 16px; font-size: 14px; color: #1f2937; font-weight: 600; }

.tg-preview-frame {
  border: 2px dashed #1F4E8C;
  border-radius: 12px;
  padding: 12px;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 240px; height: 240px;
}
.tg-preview-frame img { max-width: 100%; max-height: 100%; object-fit: contain; }

.tg-preview-empty {
  border: 2px dashed #cbd5e1;
  border-radius: 12px;
  width: 240px; height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  background: #f8fafc;
  gap: 8px;
}
.tg-preview-empty p { font-size: 12px; margin: 0; }

.tg-url-line {
  margin-top: 14px;
  font-size: 12px;
  color: #64748b;
  display: flex;
  gap: 8px;
  align-items: baseline;
}
.tg-url-line .lbl { color: #94a3b8; }
.tg-url-line code {
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 4px;
  word-break: break-all;
  font-family: ui-monospace, SFMono-Regular, monospace;
}

.tg-uploader { width: 100%; }
:deep(.el-upload-dragger) {
  width: 100%;
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
}
.upload-ico { color: #1F4E8C; margin-bottom: 8px; }
.tg-up-title { font-size: 14px; color: #1f2937; font-weight: 600; }
.tg-up-sub { font-size: 12px; color: #94a3b8; margin-top: 4px; }

.tg-staged {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
.tg-staged img { max-width: 200px; max-height: 200px; border-radius: 8px; }
.tg-staged-tip { font-size: 12px; color: #f59e0b; }

.tg-actions {
  margin-top: 14px;
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
</style>
