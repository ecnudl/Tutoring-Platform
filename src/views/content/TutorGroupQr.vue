<template>
  <div class="page-container">
    <h2 class="page-title">教员接单群二维码</h2>
    <p class="page-tip">这张图会显示在教员个人中心的「接单群」页面。学员看不到。</p>

    <div class="tg-row">
      <!-- 左：当前线上 -->
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

      <!-- 右：选图 → 裁剪 → 暂存 → 保存 -->
      <el-card class="tg-card upload" shadow="never">
        <h4 class="tg-h">{{ cropping ? '裁剪二维码' : (stagedUrl ? '已上传 · 待发布' : '上传新二维码') }}</h4>

        <!-- ① 选图 -->
        <el-upload
          v-if="!cropping && !stagedUrl"
          drag
          :show-file-list="false"
          :http-request="onFileSelected"
          accept="image/png, image/jpeg, image/jpg"
          :before-upload="beforeUpload"
          class="tg-uploader"
        >
          <el-icon class="upload-ico" :size="48"><UploadFilled /></el-icon>
          <div class="tg-up-title">点击或拖拽图片到此处</div>
          <div class="tg-up-sub">支持 PNG / JPG，截图含多余内容也没关系，下一步可裁剪</div>
        </el-upload>

        <!-- ② 裁剪 -->
        <div v-if="cropping" class="tg-cropwrap">
          <Cropper
            :src="cropSrc"
            :stencil-props="{ aspectRatio: 1 }"
            stencil-component="square-stencil"
            :auto-zoom="true"
            class="tg-cropper"
            ref="cropperRef"
          />
          <p class="tg-crop-hint">把方框拖动 / 调整到只包含二维码 + 一圈白边即可。</p>
          <div class="tg-actions">
            <el-button @click="cancelCrop">取消</el-button>
            <el-button type="primary" :loading="uploading" @click="confirmCrop">
              确认裁剪并上传
            </el-button>
          </div>
        </div>

        <!-- ③ 已上传待保存 -->
        <div v-if="stagedUrl" class="tg-staged">
          <img :src="stagedUrl" />
          <p class="tg-staged-tip">已上传到 MinIO，<strong>点保存才会发布到线上</strong></p>
          <div class="tg-actions">
            <el-button @click="discardStaged">重新选图</el-button>
            <el-button type="primary" :loading="saving" @click="save">保存并发布</el-button>
          </div>
        </div>

        <div class="tg-actions tg-actions-bottom" v-if="currentUrl && !cropping && !stagedUrl">
          <el-button type="danger" plain @click="clearOnline">下架（清空线上图）</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, PictureFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { post } from '@/api/index'
import { Cropper } from 'vue-advanced-cropper'
import 'vue-advanced-cropper/dist/style.css'

const currentUrl = ref<string>('')
const cropping = ref(false)
const cropSrc = ref<string>('')
const cropperRef = ref<any>(null)
const uploading = ref(false)
const stagedUrl = ref<string>('')
const saving = ref(false)

const loadCurrent = async () => {
  try {
    const res = await fetch('/system/api/site/config').then(r => r.json())
    if (res?.code === 200) currentUrl.value = res.data?.tutorGroupQrUrl || ''
  } catch (e) { console.error(e) }
}

const beforeUpload = (file: File) => {
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不得超过 5MB')
    return false
  }
  return true
}

// 选图后不直接上传，先进裁剪界面
const onFileSelected = (opt: any) => {
  if (cropSrc.value) URL.revokeObjectURL(cropSrc.value)
  cropSrc.value = URL.createObjectURL(opt.file)
  cropping.value = true
}

const cancelCrop = () => {
  if (cropSrc.value) URL.revokeObjectURL(cropSrc.value)
  cropSrc.value = ''
  cropping.value = false
}

const confirmCrop = async () => {
  if (!cropperRef.value) return
  const result = cropperRef.value.getResult()
  const canvas: HTMLCanvasElement | null = result?.canvas || null
  if (!canvas) {
    ElMessage.error('裁剪失败，请重试')
    return
  }
  uploading.value = true
  try {
    const blob: Blob | null = await new Promise(r => canvas.toBlob(r, 'image/png', 0.95))
    if (!blob) {
      ElMessage.error('图片导出失败')
      return
    }
    const fd = new FormData()
    fd.append('picFile', blob, 'qr-cropped.png')
    const token = localStorage.getItem('admin_token') || ''
    const res = await axios.post('/system/admin/upload/pic', fd, {
      headers: { 'Content-Type': 'multipart/form-data', token }
    })
    if (res.data?.code === 200) {
      stagedUrl.value = res.data.data
      ElMessage.success('裁剪并上传完成，点保存才会发布')
      cancelCrop()
    } else {
      ElMessage.error(res.data?.msg || '上传失败')
    }
  } catch (e: any) {
    ElMessage.error('上传失败：' + (e?.message || '网络错误'))
  } finally {
    uploading.value = false
  }
}

const discardStaged = () => {
  stagedUrl.value = ''
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
onBeforeUnmount(() => { if (cropSrc.value) URL.revokeObjectURL(cropSrc.value) })
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
  display: flex; align-items: center; justify-content: center;
  width: 240px; height: 240px;
}
.tg-preview-frame img { max-width: 100%; max-height: 100%; object-fit: contain; }

.tg-preview-empty {
  border: 2px dashed #cbd5e1;
  border-radius: 12px;
  width: 240px; height: 240px;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  color: #94a3b8; background: #f8fafc; gap: 8px;
}
.tg-preview-empty p { font-size: 12px; margin: 0; }

.tg-url-line {
  margin-top: 14px; font-size: 12px; color: #64748b;
  display: flex; gap: 8px; align-items: baseline;
}
.tg-url-line .lbl { color: #94a3b8; }
.tg-url-line code {
  background: #f1f5f9; padding: 2px 6px; border-radius: 4px;
  word-break: break-all; font-family: ui-monospace, SFMono-Regular, monospace;
}

.tg-uploader { width: 100%; }
:deep(.el-upload-dragger) {
  width: 100%; height: 240px;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  border-radius: 12px;
}
.upload-ico { color: #1F4E8C; margin-bottom: 8px; }
.tg-up-title { font-size: 14px; color: #1f2937; font-weight: 600; }
.tg-up-sub { font-size: 12px; color: #94a3b8; margin-top: 4px; padding: 0 12px; text-align: center; }

.tg-cropwrap { padding: 0; }
.tg-cropper {
  height: 360px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}
.tg-crop-hint {
  font-size: 12px;
  color: #f59e0b;
  margin: 12px 0 0;
  text-align: center;
}

.tg-staged {
  display: flex; flex-direction: column; align-items: center; gap: 12px;
  padding: 16px;
}
.tg-staged img {
  max-width: 220px; max-height: 220px;
  border-radius: 10px;
  border: 2px dashed #f59e0b;
  padding: 8px;
  background: #fff;
}
.tg-staged-tip { font-size: 13px; color: #f59e0b; margin: 0; }

.tg-actions {
  margin-top: 14px;
  display: flex; gap: 8px; justify-content: center;
}
.tg-actions-bottom { margin-top: 18px; padding-top: 14px; border-top: 1px solid #f1f5f9; justify-content: flex-end; }
</style>
