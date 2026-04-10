<template>
  <div class="xy-page-wrapper">
    <div class="container xy-page">
    <Head>
      <Title>学员需求 - 591家教网</Title>
      <Meta name="description" content="浏览最新学员家教需求，按区域、科目筛选，找到合适的家教单子。" />
    </Head>

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>学员需求</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 筛选栏 -->
    <div class="filter-section">
      <div class="filter-row">
        <span class="filter-label">区域：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.district }" @click="setFilter('district', null)">不限</span>
          <span v-for="d in districts" :key="d.id" class="ftag" :class="{ active: filters.district === d.id }" @click="setFilter('district', d.id)">{{ d.name }}</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">科目：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.subject }" @click="setFilter('subject', null)">不限</span>
          <span v-for="s in subjects" :key="s.id" class="ftag" :class="{ active: filters.subject === s.id }" @click="setFilter('subject', s.id)">{{ s.name }}</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">教员类型：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.tutorType }" @click="setFilter('tutorType', null)">不限</span>
          <span class="ftag" :class="{ active: filters.tutorType === 1 }" @click="setFilter('tutorType', 1)">大学生</span>
          <span class="ftag" :class="{ active: filters.tutorType === 2 }" @click="setFilter('tutorType', 2)">专职教员</span>
          <span class="ftag" :class="{ active: filters.tutorType === 3 }" @click="setFilter('tutorType', 3)">在职教师</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">授课方式：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.teachingMethod }" @click="setFilter('teachingMethod', null)">不限</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 1 }" @click="setFilter('teachingMethod', 1)">上门家教</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 2 }" @click="setFilter('teachingMethod', 2)">在线辅导</span>
        </div>
      </div>
    </div>

    <div class="result-header" v-if="total > 0">
      共找到 <strong>{{ total }}</strong> 条需求
    </div>

    <div v-if="loading" style="text-align:center;padding:60px">
      <el-icon class="is-loading" :size="32"><Loading /></el-icon>
      <p style="color:var(--color-text-muted);margin-top:8px">加载中...</p>
    </div>

    <template v-else>
      <div class="req-list">
        <div v-for="r in requirements" :key="r.id" class="req-card">
          <div class="req-body">
            <div class="req-title-row">
              <span class="req-title">{{ r.title || r.subjectName || '家教需求' }}</span>
              <el-tag size="small" type="success">招募中</el-tag>
            </div>
            <div class="req-no">订单编号：A{{ r.displayNo || r.id }}</div>
            <div class="req-info">
              <span v-if="r.districtName">{{ r.districtName }}</span>
              <span v-if="r.description" class="req-desc">{{ r.description.substring(0, 60) }}{{ r.description.length > 60 ? '...' : '' }}</span>
            </div>
            <div class="req-tags">
              <el-tag size="small" type="info" v-if="r.studentGender === 1">男学员</el-tag>
              <el-tag size="small" type="info" v-if="r.studentGender === 2">女学员</el-tag>
              <el-tag size="small" v-if="r.tutorTypePreference">要求{{ tutorTypeMap[r.tutorTypePreference] }}</el-tag>
              <el-tag size="small" type="warning" v-if="r.budgetMin">{{ r.budgetMin }}-{{ r.budgetMax }}元/时</el-tag>
            </div>
          </div>
          <div class="req-action">
            <NuxtLink :to="'/xy/a' + (r.displayNo || r.id)">
              <el-button type="primary" size="small">详情</el-button>
            </NuxtLink>
          </div>
        </div>
      </div>

      <div v-if="!requirements.length" style="text-align:center;padding:60px;color:var(--color-text-muted)">暂无符合条件的需求</div>

      <div style="display:flex;justify-content:center;margin:24px 0" v-if="total > 0">
        <el-pagination
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          :current-page="pageCurrent"
          @current-change="onPageChange"
        />
      </div>
    </template>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const cityStore = useCityStore()
const route = useRoute()
const router = useRouter()
const { post } = useApi()
const { districts } = useCityData()

const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const subjects = [
  { id: 1, name: '数学' }, { id: 2, name: '英语' }, { id: 3, name: '语文' }, { id: 4, name: '物理' },
  { id: 5, name: '化学' }, { id: 6, name: '钢琴' }, { id: 7, name: '美术' }, { id: 8, name: '编程' }
]

const filters = ref({ district: null, subject: null, tutorType: null, teachingMethod: null })
const requirements = ref([])
const total = ref(0)
const pageCurrent = ref(1)
const pageSize = ref(15)
const loading = ref(false)

const setFilter = (key, val) => {
  filters.value[key] = val
  pageCurrent.value = 1
  search()
}

const search = async () => {
  loading.value = true
  try {
    const f = filters.value
    const res = await post('/user/api/requirement/search', {
      pageCurrent: pageCurrent.value,
      pageSize: pageSize.value,
      cityId: cityStore.cityId,
      districtId: f.district || undefined,
      subjectId: f.subject || undefined,
      tutorType: f.tutorType || undefined,
      teachingMethod: f.teachingMethod || undefined
    })
    if (res.code === 200 && res.data) {
      requirements.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const onPageChange = (page) => {
  pageCurrent.value = page
  search()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => { search() })
</script>

<style scoped>
.xy-page-wrapper {
  background: var(--color-bg);
  padding: var(--space-2xl) 0 var(--space-4xl);
}
.xy-page {
  padding: var(--space-xl);
  background: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.el-breadcrumb { margin-bottom: var(--space-lg); }

.filter-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  margin-bottom: var(--space-lg);
}

.filter-row {
  display: flex;
  align-items: flex-start;
  padding: var(--space-sm) 0;
  border-bottom: 1px solid var(--color-border-light);
}
.filter-row:last-child { border-bottom: none; }

.filter-label {
  width: 80px;
  flex-shrink: 0;
  font-size: var(--font-size-base);
  color: var(--color-text);
  font-weight: var(--font-weight-medium);
  line-height: 28px;
}

.filter-tags { display: flex; flex-wrap: wrap; gap: 6px; flex: 1; }

.ftag {
  display: inline-block;
  padding: 4px 12px;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  line-height: 20px;
}
.ftag:hover { color: var(--color-primary); }
.ftag.active { background: var(--color-primary); color: #fff; }

.result-header { margin-bottom: var(--space-md); font-size: var(--font-size-base); color: var(--color-text-secondary); }
.result-header strong { color: var(--color-primary); }

.req-list { display: flex; flex-direction: column; gap: var(--space-md); }

.req-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  transition: border-color var(--transition-fast);
}
.req-card:hover { border-color: var(--color-primary); }

.req-body { flex: 1; }
.req-title-row { display: flex; align-items: center; gap: var(--space-sm); margin-bottom: 4px; }
.req-title { font-size: var(--font-size-lg); font-weight: var(--font-weight-semibold); color: var(--color-text); }
.req-no { font-size: var(--font-size-xs); color: var(--color-text-muted); margin-bottom: 6px; }
.req-info { font-size: var(--font-size-sm); color: var(--color-text-muted); margin-bottom: 6px; display: flex; gap: var(--space-md); }
.req-desc { color: var(--color-text-secondary); }
.req-tags { display: flex; gap: 4px; flex-wrap: wrap; }
.req-action { flex-shrink: 0; margin-left: var(--space-lg); }

@media (max-width: 768px) {
  .xy-page { padding: var(--space-md); }
  .filter-row { flex-direction: column; }
  .filter-label { width: auto; margin-bottom: 4px; }
  .req-card { flex-direction: column; align-items: flex-start; gap: var(--space-sm); }
  .req-action { margin-left: 0; width: 100%; }
  .req-action .el-button { width: 100%; }
}
</style>
