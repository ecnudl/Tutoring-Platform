<template>
  <div class="container req-page">
    <h1 class="page-title">找老师需求</h1>
    <div class="req-search-bar">
      <el-input v-model="keyword" placeholder="搜索需求标题" clearable @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>

    <div v-if="loading" style="text-align:center;padding:40px">
      <el-icon class="is-loading" :size="32"><Loading /></el-icon>
    </div>
    <template v-else>
      <div v-if="requirements.length" class="card-grid">
        <el-card v-for="r in requirements" :key="r.id" shadow="hover" style="cursor:pointer" @click="$router.push('/requirement/'+r.id)">
          <h3 style="margin-bottom:8px">{{ r.title }}</h3>
          <div style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:8px">
            <el-tag size="small" type="success">招募中</el-tag>
            <el-tag size="small" type="info" v-if="r.tutorGender === 1">要求男教员</el-tag>
            <el-tag size="small" type="info" v-if="r.tutorGender === 2">要求女教员</el-tag>
          </div>
          <p style="color:#e6a23c;font-weight:600" v-if="r.budgetMin || r.budgetMax">
            预算：{{ r.budgetMin || 0 }}-{{ r.budgetMax || 0 }}元/小时
          </p>
          <p style="color:#999;font-size:12px;margin-top:6px">{{ r.gmtCreate }}</p>
        </el-card>
      </div>
      <div v-else style="text-align:center;padding:60px;color:#999">暂无需求数据</div>
      <div style="display:flex;justify-content:center;margin-top:20px" v-if="total > 0">
        <el-pagination
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="pageCurrent"
          @current-change="onPageChange"
        />
      </div>
    </template>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'

const { post } = useApi()

const keyword = ref('')
const requirements = ref([])
const total = ref(0)
const pageCurrent = ref(1)
const pageSize = ref(12)
const loading = ref(false)

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/api/requirement/search', {
      pageCurrent: pageCurrent.value,
      pageSize: pageSize.value,
      keyword: keyword.value || undefined
    })
    if (res.code === 200 && res.data) {
      requirements.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) {
    console.error('搜索失败', e)
  } finally {
    loading.value = false
  }
}

const onPageChange = (page) => {
  pageCurrent.value = page
  search()
}

onMounted(() => { search() })
</script>
<style scoped>
.req-page { padding: 20px; }
.req-search-bar { margin-bottom: 16px; display: flex; gap: 12px; align-items: center; }
.req-search-bar .el-input { width: 300px; }
@media (max-width: 768px) {
  .req-page { padding: 12px; }
  .req-search-bar { flex-direction: column; align-items: stretch; }
  .req-search-bar .el-input { width: 100%; }
}
</style>
