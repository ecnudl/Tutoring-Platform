<template>
  <div class="jy-page-wrapper">
    <div class="container jy-page">
    <Head>
      <Title>{{ cityStore.cityName }}教员库 - 591家教网</Title>
      <Meta name="description" :content="`浏览${cityStore.cityName}优质家教教员，按区域、科目、教员类型筛选，找到最适合的家教老师。`" />
    </Head>

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>教员库</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 筛选栏 -->
    <div class="filter-section">
      <div class="filter-row filter-search-row">
        <span class="filter-label">搜索：</span>
        <el-input
          v-model="kw"
          placeholder="教师编号 / 科目 / 院校 / 工作单位 / 姓氏"
          clearable
          class="kw-input"
          @keyup.enter="doKeywordSearch"
          @clear="doKeywordSearch"
        >
          <template #append><el-button @click="doKeywordSearch">搜索</el-button></template>
        </el-input>
      </div>
      <div class="filter-row">
        <span class="filter-label">区域：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.district }" @click="setFilter('district', null)">不限</span>
          <span v-for="d in districts" :key="d.id" class="ftag" :class="{ active: filters.district === d.id }" @click="setFilter('district', d.id)">{{ d.name }}</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">科目：</span>
        <div class="filter-tags-wrap">
          <div class="filter-tags" :class="{ expanded: subjectExpanded }">
            <span class="ftag" :class="{ active: !filters.subject }" @click="setFilter('subject', null)">不限</span>
            <span v-for="s in allSubjects" :key="s.name" class="ftag" :class="{ active: filters.subject === s.name }" @click="setFilter('subject', s.name)">{{ s.name }}</span>
          </div>
          <span class="expand-btn" @click.prevent="subjectExpanded = !subjectExpanded">
            {{ subjectExpanded ? '收起' : '展开' }}
            <el-icon size="12"><component :is="subjectExpanded ? ArrowUp : ArrowDown" /></el-icon>
          </span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">院校：</span>
        <div class="filter-tags-wrap">
          <div class="filter-tags" :class="{ expanded: universityExpanded }">
            <span class="ftag" :class="{ active: !filters.university }" @click="setFilter('university', null)">不限</span>
            <span v-for="u in universities" :key="u" class="ftag" :class="{ active: filters.university === u }" @click="setFilter('university', u)">{{ u }}</span>
            <NuxtLink to="/university" class="ftag more-link">更多高校 &rarr;</NuxtLink>
          </div>
          <span class="expand-btn" @click.prevent="universityExpanded = !universityExpanded">
            {{ universityExpanded ? '收起' : '展开' }}
            <el-icon size="12"><component :is="universityExpanded ? ArrowUp : ArrowDown" /></el-icon>
          </span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">教员类型：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.tutorType }" @click="setFilter('tutorType', null)">不限</span>
          <span class="ftag" :class="{ active: filters.tutorType === 1 }" @click="setFilter('tutorType', 1)">大学生</span>
          <span class="ftag" :class="{ active: filters.tutorType === 2 }" @click="setFilter('tutorType', 2)">专职教员</span>
          <span class="ftag" :class="{ active: filters.tutorType === 3 }" @click="setFilter('tutorType', 3)">在职教师</span>
          <span class="ftag" :class="{ active: filters.tutorType === 4 }" @click="setFilter('tutorType', 4)">海归外教</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">授课方式：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.teachingMethod }" @click="setFilter('teachingMethod', null)">不限</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 1 }" @click="setFilter('teachingMethod', 1)">教师上门</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 2 }" @click="setFilter('teachingMethod', 2)">学生上门</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 3 }" @click="setFilter('teachingMethod', 3)">在线辅导</span>
          <span class="ftag" :class="{ active: filters.teachingMethod === 4 }" @click="setFilter('teachingMethod', 4)">均可</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">性别：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.gender }" @click="setFilter('gender', null)">不限</span>
          <span class="ftag" :class="{ active: filters.gender === 1 }" @click="setFilter('gender', 1)">男</span>
          <span class="ftag" :class="{ active: filters.gender === 2 }" @click="setFilter('gender', 2)">女</span>
        </div>
      </div>
      <div class="filter-row">
        <span class="filter-label">价格：</span>
        <div class="filter-tags">
          <span class="ftag" :class="{ active: !filters.priceRange }" @click="setFilter('priceRange', null)">不限</span>
          <span class="ftag" :class="{ active: filters.priceRange === '0-100' }" @click="setFilter('priceRange', '0-100')">100以下</span>
          <span class="ftag" :class="{ active: filters.priceRange === '100-200' }" @click="setFilter('priceRange', '100-200')">100-200</span>
          <span class="ftag" :class="{ active: filters.priceRange === '200-300' }" @click="setFilter('priceRange', '200-300')">200-300</span>
          <span class="ftag" :class="{ active: filters.priceRange === '300-500' }" @click="setFilter('priceRange', '300-500')">300-500</span>
          <span class="ftag" :class="{ active: filters.priceRange === '500-9999' }" @click="setFilter('priceRange', '500-9999')">500以上</span>
        </div>
      </div>
    </div>

    <!-- 结果 -->
    <div class="result-header" v-if="total > 0">
      <span>共找到 <strong>{{ total }}</strong> 位教员</span>
    </div>

    <div v-if="loading" style="text-align:center;padding:60px">
      <el-icon class="is-loading" :size="32"><Loading /></el-icon>
      <p style="color:var(--color-text-muted);margin-top:8px">加载中...</p>
    </div>

    <template v-else>
      <div class="tutor-list">
        <div v-for="t in tutors" :key="t.id" class="tutor-card-h">
          <div class="tutor-avatar">
            <el-avatar :size="64" :src="tutorAvatarUrl(t)" />
          </div>
          <div class="tutor-body">
            <div class="tutor-top">
              <div class="tt-left">
                <span class="tutor-name">{{ t.surname || (t.realName ? t.realName.charAt(0) : '') }}老师</span>
                <TutorIcons :tutor="t" :size="16" />
                <el-tag size="small" v-if="t.tutorType">{{ tutorTypeMap[t.tutorType] }}</el-tag>
                <el-tag size="small" type="info" v-if="t.gender === 1">男</el-tag>
                <el-tag size="small" type="danger" v-if="t.gender === 2">女</el-tag>
                <span v-if="t.identityDetail" class="tutor-identity">{{ t.identityDetail }}</span>
              </div>
              <div class="tt-meta">
                <span v-if="t.displayNo" class="tm-no">{{ t.displayNo }}</span>
                <span v-if="t.successCount" class="tm-stat">成{{ t.successCount }}单</span>
                <span v-if="t.isStar === 1" class="tm-star">★ 明星</span>
                <span v-if="t.lastLoginTime" class="tm-date">{{ fmtActive(t.lastLoginTime) }}</span>
              </div>
            </div>
            <div class="tutor-detail">
              <span v-if="t.university">{{ t.university }}</span>
              <span v-if="t.major">{{ t.major }}</span>
              <span v-if="t.degree">{{ degreeMap[t.degree] }}</span>
            </div>
            <div class="tutor-intro" v-if="t.selfIntroduction">自我介绍：{{ t.selfIntroduction }}</div>
            <div class="tutor-subjects-row" v-if="t.subjects">
              <span class="ts-label">家教科目：</span>
              <el-tag v-for="sub in t.subjects.split(',').filter(Boolean).slice(0, 4)" :key="sub" size="small" type="info">{{ sub }}</el-tag>
            </div>
            <div class="tutor-districts" v-if="t.districtNames || t.teachingMethod">
              <span v-if="t.districtNames">授课区域：{{ t.districtNames }}</span>
              <span v-if="t.teachingMethod" class="tutor-method">{{ teachingMethodMap[t.teachingMethod] }}</span>
            </div>
          </div>
          <div class="tutor-right">
            <div class="tutor-price" v-if="t.priceMin">{{ t.priceMin }}-{{ t.priceMax }}元/时</div>
            <NuxtLink :to="'/jy/t' + (t.displayNo ? t.displayNo.replace(/^T/i, '') : t.id)">
              <el-button type="primary" size="small">查看详情</el-button>
            </NuxtLink>
          </div>
        </div>
      </div>

      <div v-if="!tutors.length && !loading" style="text-align:center;padding:60px;color:var(--color-text-muted)">暂无符合条件的教员</div>

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
import { ref, onMounted, watch } from 'vue'
import { Loading, ArrowDown, ArrowUp } from '@element-plus/icons-vue'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'
import { SUBJECT_OBJECTS as allSubjects } from '~/composables/subjectList'
import { tutorAvatarUrl } from '~/composables/useTutorAvatar'

const cityStore = useCityStore()
const route = useRoute()
const router = useRouter()
const { post } = useApi()

const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const degreeMap = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }
const teachingMethodMap = { 1: '教师上门', 2: '学生上门', 3: '在线辅导', 4: '上门/在线' }
// 关键词搜索框(独立于结构化筛选)
const kw = ref('')
// 最近活跃日期 yyyy-MM-dd
const fmtActive = (dt) => {
  if (!dt) return ''
  return String(dt).slice(0, 10)
}

const { districts, universities } = useCityData()

const subjectExpanded = ref(false)
const universityExpanded = ref(false)

const filters = ref({
  district: null,
  subject: null,
  university: null,
  tutorType: null,
  teachingMethod: null,
  gender: null,
  priceRange: null,
  keyword: null
})

const tutors = ref([])
const total = ref(0)
const pageCurrent = ref(1)
const pageSize = ref(15)
const loading = ref(false)

const initFiltersFromQuery = () => {
  const q = route.query
  if (q.district) filters.value.district = Number(q.district)
  if (q.subject) filters.value.subject = q.subject
  if (q.tutorType) filters.value.tutorType = Number(q.tutorType)
  if (q.teachingMethod) filters.value.teachingMethod = Number(q.teachingMethod)
  if (q.gender) filters.value.gender = Number(q.gender)
  if (q.university) filters.value.university = q.university
  if (q.priceRange) filters.value.priceRange = q.priceRange
  if (q.keyword) filters.value.keyword = q.keyword
  if (q.page) pageCurrent.value = Number(q.page)
}

const setFilter = (key, val) => {
  filters.value[key] = val
  pageCurrent.value = 1
  updateQuery()
  search()
}

const doKeywordSearch = () => {
  filters.value.keyword = (kw.value || '').trim() || null
  pageCurrent.value = 1
  updateQuery()
  search()
}

const updateQuery = () => {
  const q = {}
  const f = filters.value
  if (f.district) q.district = f.district
  if (f.subject) q.subject = f.subject
  if (f.tutorType) q.tutorType = f.tutorType
  if (f.teachingMethod) q.teachingMethod = f.teachingMethod
  if (f.gender) q.gender = f.gender
  if (f.university) q.university = f.university
  if (f.priceRange) q.priceRange = f.priceRange
  if (f.keyword) q.keyword = f.keyword
  if (pageCurrent.value > 1) q.page = pageCurrent.value
  router.replace({ query: q })
}

const search = async () => {
  loading.value = true
  try {
    const f = filters.value
    let priceMin, priceMax
    if (f.priceRange) {
      const parts = f.priceRange.split('-')
      priceMin = Number(parts[0])
      priceMax = Number(parts[1])
    }
    const params = {
      pageCurrent: pageCurrent.value,
      pageSize: pageSize.value,
      cityId: cityStore.cityId,
      districtId: f.district || undefined,
      subject: f.subject || undefined,
      university: f.university || undefined,
      tutorType: f.tutorType || undefined,
      teachingMethod: f.teachingMethod || undefined,
      gender: f.gender || undefined,
      priceMin: priceMin || undefined,
      priceMax: priceMax || undefined,
      keyword: f.keyword || undefined
    }
    const res = await post('/user/api/tutor/search', params)
    if (res.code === 200 && res.data) {
      tutors.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const onPageChange = (page) => {
  pageCurrent.value = page
  updateQuery()
  search()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  initFiltersFromQuery()
  kw.value = filters.value.keyword || '' // 搜索框回填(支持 URL 直链带 keyword)
  search()
})
</script>

<style scoped>
.jy-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.jy-page {
  padding: var(--space-xl);
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
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

.filter-tags-wrap {
  flex: 1;
  display: flex;
  align-items: flex-start;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  flex: 1;
  max-height: 30px;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.filter-tags.expanded {
  max-height: none;
}

.filter-tags-wrap + .filter-tags,
.filter-row > .filter-tags {
  max-height: none;
}

.expand-btn {
  display: inline-flex;
  align-items: center;
  gap: 2px;
  padding: 4px 10px;
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  cursor: pointer;
  white-space: nowrap;
  flex-shrink: 0;
  margin-left: 8px;
  line-height: 20px;
}
.expand-btn:hover {
  opacity: 0.8;
}

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
.ftag:hover {
  color: var(--color-primary);
}
.ftag.active {
  background: var(--color-primary);
  color: #fff;
}

.ftag.more-link {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}
.ftag.more-link:hover {
  background: var(--color-primary-lighter);
}

.result-header {
  margin-bottom: var(--space-md);
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
}
.result-header strong {
  color: var(--color-primary);
}

.tutor-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.tutor-card-h {
  display: flex;
  gap: var(--space-lg);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  align-items: flex-start;     /* 改: 顶对齐, 头像靠上 */
  transition: border-color var(--transition-fast);
}
.tutor-card-h:hover {
  border-color: var(--color-primary);
}

/* 头像列: 列宽固定 80px (容纳 chip), 头像 + chip 上下排列, 永远在 card 内 */
.tutor-avatar {
  width: 80px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}
.tutor-body { flex: 1; min-width: 0; padding-top: 4px; }   /* body 与头像顶端微对齐 */

.tutor-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 6px;
  flex-wrap: wrap;
}
.tt-left { display: flex; align-items: center; gap: 6px; flex-wrap: wrap; min-width: 0; }
.tt-meta { display: flex; align-items: center; gap: 10px; flex-shrink: 0; font-size: 12px; color: var(--color-text-muted); }
.tm-no { font-family: ui-monospace, "SF Mono", Consolas, monospace; }
.tm-stat { color: #047857; }
.tm-star { color: #d97706; font-weight: 600; }
.tutor-identity { font-size: 12.5px; color: var(--color-text-secondary); }

.tutor-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.tutor-detail {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-bottom: 6px;
  display: flex;
  gap: var(--space-md);
}

.tutor-intro {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.tutor-subjects-row {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 4px;
}
.ts-label { font-size: var(--font-size-xs); color: var(--color-text-muted); }

.tutor-districts {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  display: flex;
  gap: 14px;
  align-items: center;
  flex-wrap: wrap;
}
.tutor-method { color: var(--color-primary); }

.filter-search-row .kw-input { max-width: 380px; }

.tutor-right {
  flex-shrink: 0;
  text-align: center;
}

.tutor-price {
  color: var(--color-accent-dark);
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--space-sm);
}

@media (max-width: 768px) {
  .jy-page { padding: var(--space-md); }
  .filter-row { flex-direction: column; }
  .filter-label { width: auto; margin-bottom: 4px; }
  .tutor-card-h { flex-direction: column; text-align: center; }
  .tutor-right { width: 100%; display: flex; justify-content: space-between; align-items: center; }
}

</style>
