<template>
  <div class="container article-list-page">
    <h1 class="page-title">资讯动态</h1>
    <div v-if="loading" style="text-align:center;padding:40px">
      <el-icon class="is-loading" :size="24"><Loading /></el-icon>
    </div>
    <div v-else-if="list.length === 0" style="text-align:center;padding:60px;color:#999">暂无资讯</div>
    <div v-else class="article-grid">
      <NuxtLink v-for="item in list" :key="item.id" :to="'/article/' + item.id" class="article-card">
        <div class="article-cover" v-if="item.articleCover">
          <img :src="item.articleCover" :alt="item.articleTitle" />
        </div>
        <div class="article-info">
          <h3>{{ item.articleTitle }}</h3>
          <p class="summary">{{ item.articleSummary || '暂无摘要' }}</p>
          <div class="meta">
            <span>{{ formatDate(item.gmtCreate) }}</span>
            <span>{{ item.viewCount || 0 }} 次浏览</span>
          </div>
        </div>
      </NuxtLink>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'

const config = useRuntimeConfig()
const list = ref([])
const loading = ref(true)

const formatDate = (d) => d ? d.substring(0, 10) : ''

onMounted(async () => {
  try {
    const res = await $fetch(`${config.public.apiBase}/system/api/website/article/list`)
    if (res.code === 200) list.value = res.data || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})
</script>
<style scoped>
.article-list-page { padding: 20px 0; min-height: 60vh; }
.page-title { font-size: 24px; font-weight: 700; margin-bottom: 24px; }
.article-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
.article-card { display: flex; flex-direction: column; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,.06); transition: box-shadow .2s; text-decoration: none; color: inherit; }
.article-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,.12); }
.article-cover { height: 180px; overflow: hidden; }
.article-cover img { width: 100%; height: 100%; object-fit: cover; }
.article-info { padding: 16px; flex: 1; display: flex; flex-direction: column; }
.article-info h3 { font-size: 16px; margin: 0 0 8px; line-height: 1.4; }
.summary { font-size: 14px; color: #666; flex: 1; margin: 0 0 12px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.meta { display: flex; justify-content: space-between; font-size: 12px; color: #999; }
@media (max-width: 768px) {
  .article-grid { grid-template-columns: 1fr; gap: 12px; }
  .article-cover { height: 160px; }
  .page-title { font-size: 20px; }
}
</style>
