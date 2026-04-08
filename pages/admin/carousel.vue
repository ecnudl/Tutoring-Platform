<template>
  <div class="carousel-admin">
    <div class="container">
      <h1 class="page-title">轮播图管理</h1>

      <div class="admin-card">
        <div class="card-header">
          <span>当前轮播图 ({{ slides.length }})</span>
          <el-button type="primary" size="small" @click="addSlide">添加轮播图</el-button>
        </div>

        <div class="slide-list">
          <div v-for="(slide, idx) in slides" :key="slide.id" class="slide-row">
            <div class="slide-preview">
              <img v-if="slide.imageUrl" :src="slide.imageUrl" class="preview-img" />
              <div v-else class="preview-empty">暂无图片</div>
            </div>
            <div class="slide-fields">
              <el-input v-model="slide.title" placeholder="标题" size="small" />
              <el-input v-model="slide.imageUrl" placeholder="图片URL (建议尺寸: 1200x380)" size="small" />
              <el-input v-model="slide.link" placeholder="点击跳转链接 (可选)" size="small" />
            </div>
            <div class="slide-actions">
              <el-button size="small" :disabled="idx === 0" @click="moveSlide(idx, -1)">上移</el-button>
              <el-button size="small" :disabled="idx === slides.length - 1" @click="moveSlide(idx, 1)">下移</el-button>
              <el-button size="small" type="danger" @click="removeSlide(idx)">删除</el-button>
            </div>
          </div>
          <el-empty v-if="!slides.length" description="暂无轮播图" />
        </div>

        <div class="card-footer">
          <el-button type="primary" @click="saveSlides">保存并发布</el-button>
          <span class="save-tip">保存后首页轮播图将立即更新</span>
        </div>
      </div>

      <div class="admin-card">
        <div class="card-header">
          <span>图片规格说明</span>
        </div>
        <div class="spec-info">
          <p>建议图片尺寸：<strong>1200 x 380 像素</strong>（宽高比约 3.16:1）</p>
          <p>图片格式：JPG / PNG / WebP</p>
          <p>图片将自动裁剪为容器大小，居中显示（object-fit: cover）</p>
          <p>建议上传清晰度较高的图片，文件大小控制在 500KB 以内</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

definePageMeta({ layout: 'center' })

const slides = ref([])

let nextId = 100

const addSlide = () => {
  slides.value.push({
    id: nextId++,
    title: '',
    imageUrl: '',
    link: ''
  })
}

const removeSlide = (idx) => {
  slides.value.splice(idx, 1)
}

const moveSlide = (idx, dir) => {
  const target = idx + dir
  if (target < 0 || target >= slides.value.length) return
  const temp = slides.value[idx]
  slides.value[idx] = slides.value[target]
  slides.value[target] = temp
  slides.value = [...slides.value]
}

const saveSlides = () => {
  localStorage.setItem('carousel_slides', JSON.stringify(slides.value))
  ElMessage.success('轮播图已保存，首页将立即更新')
}

onMounted(() => {
  const stored = localStorage.getItem('carousel_slides')
  if (stored) {
    try {
      slides.value = JSON.parse(stored)
      nextId = Math.max(...slides.value.map(s => s.id), 99) + 1
    } catch (e) {
      slides.value = []
    }
  }
  if (!slides.value.length) {
    slides.value = [
      { id: 1, title: '专业家教上门辅导', imageUrl: '', link: '/qjj' },
      { id: 2, title: '优质教员等你来', imageUrl: '', link: '/jy' },
      { id: 3, title: '新学期辅导计划', imageUrl: '', link: '/qjj' }
    ]
  }
})
</script>

<style scoped>
.carousel-admin {
  padding: var(--space-2xl) 0 var(--space-4xl);
}

.admin-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-2xl);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-lg) var(--space-xl);
  background: var(--color-bg);
  border-bottom: 1px solid var(--color-border);
  font-weight: var(--font-weight-semibold);
}

.slide-list {
  padding: var(--space-lg) var(--space-xl);
}

.slide-row {
  display: flex;
  gap: var(--space-lg);
  padding: var(--space-lg) 0;
  border-bottom: 1px solid var(--color-border-light);
  align-items: center;
}
.slide-row:last-child { border-bottom: none; }

.slide-preview {
  width: 180px;
  height: 60px;
  flex-shrink: 0;
  border-radius: var(--radius-sm);
  overflow: hidden;
  border: 1px solid var(--color-border);
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-empty {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg);
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
}

.slide-fields {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.slide-actions {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
  flex-shrink: 0;
}

.card-footer {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  padding: var(--space-lg) var(--space-xl);
  border-top: 1px solid var(--color-border);
}

.save-tip {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}

.spec-info {
  padding: var(--space-lg) var(--space-xl);
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  line-height: 2;
}
.spec-info strong {
  color: var(--color-primary);
}
</style>
