<template>
  <div class="tutor-page-wrapper">
    <div class="container tutor-page">
    <h1 class="page-title">教员库</h1>

    <!-- 移动端筛选折叠 -->
    <div class="mobile-filter-toggle" @click="showFilter = !showFilter">
      筛选条件 {{ showFilter ? '▲' : '▼' }}
    </div>

    <div class="tutor-layout">
      <aside class="tutor-sidebar" :class="{ 'mobile-show': showFilter }">
        <el-card>
          <h4 style="margin-bottom:12px" class="desktop-only">筛选条件</h4>
          <el-form label-position="top" size="small">
            <el-form-item label="关键词">
              <el-input v-model="filters.keyword" placeholder="姓名搜索" clearable @keyup.enter="search" />
            </el-form-item>
            <el-form-item label="科目">
              <el-select v-model="filters.subject" placeholder="全部科目" clearable filterable style="width:100%">
                <el-option v-for="s in dictStore.subjects" :key="s.id" :label="s.subjectName" :value="s.subjectName" />
              </el-select>
            </el-form-item>
            <el-form-item label="教员类型">
              <el-select v-model="filters.tutorType" placeholder="全部" clearable style="width:100%">
                <el-option label="大学生" :value="1" />
                <el-option label="专职教师" :value="2" />
                <el-option label="在职教师" :value="3" />
                <el-option label="退休教师" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item label="学历">
              <el-select v-model="filters.degree" placeholder="不限" clearable style="width:100%">
                <el-option label="高中" :value="1" />
                <el-option label="大专" :value="2" />
                <el-option label="本科" :value="3" />
                <el-option label="硕士" :value="4" />
                <el-option label="博士" :value="5" />
              </el-select>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="filters.gender">
                <el-radio :label="0">不限</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="价格范围(元/时)">
              <div style="display:flex;gap:8px;align-items:center">
                <el-input-number v-model="filters.priceMin" :min="0" :max="9999" :step="10" controls-position="right" placeholder="最低" style="width:50%" size="small" />
                <span>-</span>
                <el-input-number v-model="filters.priceMax" :min="0" :max="9999" :step="10" controls-position="right" placeholder="最高" style="width:50%" size="small" />
              </div>
            </el-form-item>
            <el-form-item label="排序">
              <el-select v-model="filters.sortField" placeholder="默认排序" clearable style="width:100%">
                <el-option label="价格最低" value="priceMin" />
                <el-option label="浏览最多" value="viewCount" />
                <el-option label="成功最多" value="successCount" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width:100%" @click="doSearch">搜索</el-button>
              <el-button style="width:100%;margin-top:8px;margin-left:0" @click="resetFilters">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </aside>
      <main class="tutor-main">
        <div v-if="loading" style="text-align:center;padding:40px">
          <el-icon class="is-loading" :size="32"><Loading /></el-icon>
          <p style="color:#999;margin-top:8px">加载中...</p>
        </div>
        <template v-else>
          <p class="result-count" v-if="total > 0">共 {{ total }} 位教员</p>
          <div v-if="tutors.length" class="card-grid">
            <el-card v-for="t in tutors" :key="t.id" shadow="hover" style="cursor:pointer" @click="$router.push('/tutor/'+t.id)">
              <div style="display:flex;gap:12px">
                <el-avatar :size="60" :src="t.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
                <div style="flex:1">
                  <h3 style="margin-bottom:4px">{{ t.realName || '教员' }}
                    <el-tag v-if="t.gender===1" size="small" type="info" style="margin-left:4px">男</el-tag>
                    <el-tag v-if="t.gender===2" size="small" type="danger" style="margin-left:4px">女</el-tag>
                  </h3>
                  <p style="color:#999;font-size:13px">{{ t.university || '' }} {{ t.major ? '· ' + t.major : '' }}{{ t.degree ? ' · ' + getDegreeLabel(t.degree) : '' }}</p>
                  <p style="color:#e6a23c;margin-top:4px;font-weight:600" v-if="t.priceMin || t.priceMax">
                    {{ t.priceMin || 0 }}-{{ t.priceMax || 0 }}元/小时
                  </p>
                  <div style="margin-top:6px;display:flex;gap:4px;flex-wrap:wrap">
                    <el-tag v-if="t.tutorType" size="small" type="info">{{ getTutorTypeLabel(t.tutorType) }}</el-tag>
                    <el-tag v-if="t.subjects" size="small" v-for="sub in t.subjects.split(',').slice(0,3)" :key="sub">{{ sub }}</el-tag>
                    <el-tag v-if="t.freeTrial===1" size="small" type="success">可试课</el-tag>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
          <div v-else style="text-align:center;padding:60px;color:#999">暂无符合条件的教员</div>
          <div style="display:flex;justify-content:center;margin-top:20px" v-if="total > 0">
            <el-pagination layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageCurrent" @current-change="onPageChange" />
          </div>
        </template>
      </main>
    </div>
  </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import { useDictStore } from '~/stores/dict'

const dictStore = useDictStore()
const { post } = useApi()

const defaultFilters = () => ({ keyword: '', subject: null, tutorType: null, degree: null, gender: 0, priceMin: null, priceMax: null, sortField: null })
const filters = ref(defaultFilters())
const tutors = ref([])
const total = ref(0)
const pageCurrent = ref(1)
const pageSize = ref(12)
const loading = ref(false)
const showFilter = ref(false)

const getTutorTypeLabel = (type) => ({ 1: '大学生', 2: '专职教师', 3: '在职教师', 4: '退休教师' }[type] || '')
const getDegreeLabel = (d) => ({ 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }[d] || '')

const search = async () => {
  loading.value = true
  try {
    const f = filters.value
    const params = {
      pageCurrent: pageCurrent.value, pageSize: pageSize.value,
      keyword: f.keyword || undefined,
      subject: f.subject || undefined,
      tutorType: f.tutorType || undefined,
      degree: f.degree || undefined,
      gender: f.gender || undefined,
      priceMin: f.priceMin || undefined,
      priceMax: f.priceMax || undefined,
      sortField: f.sortField || undefined,
      sortOrder: f.sortField ? (['viewCount', 'successCount'].includes(f.sortField) ? 'desc' : 'asc') : undefined
    }
    const res = await post('/user/api/tutor/search', params)
    if (res.code === 200 && res.data) { tutors.value = res.data.list || []; total.value = res.data.totalCount || 0 }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const doSearch = () => { pageCurrent.value = 1; search(); showFilter.value = false }
const resetFilters = () => { filters.value = defaultFilters(); doSearch() }
const onPageChange = (page) => { pageCurrent.value = page; search() }

onMounted(async () => { await dictStore.fetchAll(); await search() })
</script>
<style scoped>
.tutor-page { padding: 20px; }
.tutor-layout { display: flex; gap: 20px; }
.tutor-sidebar { width: 260px; flex-shrink: 0; }
.tutor-main { flex: 1; }
.mobile-filter-toggle { display: none; }
.desktop-only { display: block; }
.result-count { font-size: 14px; color: #666; margin-bottom: 12px; }

@media (max-width: 768px) {
  .tutor-page { padding: 12px; }
  .tutor-layout { flex-direction: column; }
  .tutor-sidebar { width: 100%; display: none; }
  .tutor-sidebar.mobile-show { display: block; }
  .mobile-filter-toggle {
    display: block; text-align: center; padding: 10px; margin-bottom: 12px;
    background: #fff; border-radius: 8px; color: #409eff; font-size: 14px; cursor: pointer;
  }
  .desktop-only { display: none; }
}
.tutor-page-wrapper {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 var(--space-4xl);
}
.tutor-page {
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--space-xl);
}
</style>
