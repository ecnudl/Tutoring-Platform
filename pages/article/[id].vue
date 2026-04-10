<template>
  <div class="article-page-wrapper">
  <div class="container article-detail-page">
    <div v-if="loading" style="text-align:center;padding:60px">
      <el-icon class="is-loading" :size="24"><Loading /></el-icon>
    </div>
    <div v-else-if="!article" style="text-align:center;padding:60px;color:#999">文章不存在</div>
    <el-card v-else class="article-card">
      <h1 class="article-title">{{ article.articleTitle }}</h1>
      <div class="article-meta">
        <span>发布时间：{{ article.gmtCreate }}</span>
        <span>浏览：{{ article.viewCount || 0 }} 次</span>
      </div>
      <el-divider />
      <div class="article-content" v-html="article.articleContent"></div>
    </el-card>
    <div style="margin-top:20px">
      <NuxtLink to="/article" class="back-link">← 返回资讯列表</NuxtLink>
    </div>
  </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Loading } from '@element-plus/icons-vue'

const config = useRuntimeConfig()
const route = useRoute()
const article = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await $fetch(`${config.public.apiBase}/system/api/website/article/view`, {
      params: { id: route.params.id }
    })
    if (res.code === 200) article.value = res.data
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})
</script>
<style scoped>
.article-page-wrapper {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 var(--space-4xl);
}
.article-detail-page {
  padding: 20px;
  min-height: 60vh;
  max-width: 800px;
  margin: 0 auto;
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.article-card { padding: 12px; }
.article-title { font-size: 24px; font-weight: 700; margin: 0 0 12px; line-height: 1.4; }
.article-meta { font-size: 13px; color: #999; display: flex; gap: 20px; }
.article-content { font-size: 15px; line-height: 1.8; color: #333; }
.article-content :deep(img) { max-width: 100%; height: auto; border-radius: 4px; }
.back-link { color: #409eff; font-size: 14px; }
@media (max-width: 768px) {
  .article-title { font-size: 20px; }
  .article-content { font-size: 14px; }
}
</style>
