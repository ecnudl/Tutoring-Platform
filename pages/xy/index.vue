<template>
  <div class="xy-page-wrapper">
    <div class="container xy-page">
    <Head>
      <Title>{{ cityStore.cityName }}学员需求 - 591家教网</Title>
      <Meta name="description" :content="`浏览${cityStore.cityName}最新学员家教需求，按区域、科目筛选，找到合适的家教单子。`" />
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
          <span v-for="d in districts" :key="d.id" class="ftag" :class="{ active: filters.district === d.name }" @click="setFilter('district', d.name)">{{ d.name }}</span>
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
          <span class="ftag" :class="{ active: filters.tutorType === '大学生' }" @click="setFilter('tutorType', '大学生')">大学生</span>
          <span class="ftag" :class="{ active: filters.tutorType === '专职教员' }" @click="setFilter('tutorType', '专职教员')">专职教员</span>
          <span class="ftag" :class="{ active: filters.tutorType === '在职教师' }" @click="setFilter('tutorType', '在职教师')">在职教师</span>
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
    </div>

    <div class="result-header" v-if="total > 0">
      共找到 <strong>{{ total }}</strong> 条需求
    </div>

    <div v-if="loading" style="text-align:center;padding:60px">
      <el-icon class="is-loading" :size="32"><Loading /></el-icon>
      <p style="color:var(--color-text-muted);margin-top:8px">加载中...</p>
    </div>

    <template v-else>
      <div class="req-list" v-if="requirements.length">
        <template v-for="r in requirements" :key="r.id">
          <!-- 未接单: NuxtLink, 整行可点 -->
          <NuxtLink
            v-if="r.reqStatus !== 3"
            :to="'/xy/s' + (r.displayNo ? r.displayNo.replace(/^S/i, '') : r.id)"
            class="req-row"
          >
            <div class="rc-c1">
              <div class="rc-title">{{ buildCardTitle(r) }}</div>
              <div class="rc-no">{{ r.displayNo || ('S' + r.id) }}</div>
            </div>
            <div class="rc-c2">
              <template v-if="Number(r.teachingMethod) === 3">
                <div class="rc-online">
                  <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="9"/><path d="M3 12h18M12 3a14 14 0 0 1 0 18M12 3a14 14 0 0 0 0 18"/></svg>
                  网络授课
                </div>
              </template>
              <template v-else>
                <div class="rc-region">{{ buildRegion(r) }}</div>
                <div class="rc-addr">{{ r.address || '大致位置需确认' }}</div>
              </template>
            </div>
            <div class="rc-c3">
              <div class="rc-req">{{ r.otherRequirements || '没有额外要求' }}</div>
              <div class="rc-pref">{{ buildTutorPref(r) }}</div>
            </div>
            <div class="rc-c4">
              <div class="rc-budget">{{ buildBudget(r) }}</div>
              <span class="rc-cta">详情</span>
            </div>
          </NuxtLink>

          <!-- 已接单: 不可点 -->
          <div v-else class="req-row is-matched">
            <div class="rc-c1">
              <div class="rc-title">{{ buildCardTitle(r) }}</div>
              <div class="rc-no">{{ r.displayNo || ('S' + r.id) }}</div>
            </div>
            <div class="rc-c2">
              <template v-if="Number(r.teachingMethod) === 3">
                <div class="rc-online">
                  <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="9"/><path d="M3 12h18M12 3a14 14 0 0 1 0 18M12 3a14 14 0 0 0 0 18"/></svg>
                  网络授课
                </div>
              </template>
              <template v-else>
                <div class="rc-region">{{ buildRegion(r) }}</div>
                <div class="rc-addr">{{ r.address || '大致位置需确认' }}</div>
              </template>
            </div>
            <div class="rc-c3">
              <div class="rc-req">{{ r.otherRequirements || '没有额外要求' }}</div>
              <div class="rc-pref">{{ buildTutorPref(r) }}</div>
            </div>
            <div class="rc-c4">
              <div class="rc-budget">{{ buildBudget(r) }}</div>
              <span class="rc-done">已接单</span>
            </div>
          </div>
        </template>
      </div>

      <div v-else style="text-align:center;padding:60px;color:var(--color-text-muted)">暂无符合条件的需求</div>

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
import { Loading, ArrowDown, ArrowUp } from '@element-plus/icons-vue'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const cityStore = useCityStore()
const route = useRoute()
const router = useRouter()
const { post } = useApi()
const { districts, universities } = useCityData()

const tutorTypeMap = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }

// ---- 卡片渲染助手 ----
const csvFirst = (s) => {
  if (!s) return ''
  return String(s).split(',').map(x => x.trim()).filter(Boolean)[0] || ''
}
const csvJoin = (s) => {
  if (!s) return ''
  return String(s).split(',').map(x => x.trim()).filter(Boolean).join('、')
}
const buildCardTitle = (r) => {
  const subjects = csvJoin(r.subjectIds)
  return subjects || '暂无科目要求'
}
const buildRegion = (r) => {
  const dn = csvFirst(r.districtNames)
  return dn || '区域待确认'
}
const buildTutorPref = (r) => {
  const gPart = r.tutorGender === 1 ? '[男]' : r.tutorGender === 2 ? '[女]' : '[无限制]'
  const types = csvJoin(r.tutorTypePref)
  return types ? `${gPart}需要教员身份${types}` : `${gPart}不限教员类型`
}
const buildBudget = (r) => {
  if (r.budgetMin && r.budgetMax && Number(r.budgetMin) !== Number(r.budgetMax)) {
    return `${r.budgetMin}-${r.budgetMax} 元/小时`
  }
  if (r.budgetMin) return `${r.budgetMin} 元/小时`
  if (r.budgetMax) return `${r.budgetMax} 元/小时`
  return '面议'
}

const subjectExpanded = ref(false)
const universityExpanded = ref(false)

const allSubjects = [
  { name: '幼儿学前' }, { name: '小学全科' }, { name: '初中理科' }, { name: '初中文科' },
  { name: '高中理科' }, { name: '高中文科' }, { name: '语文' }, { name: '英语' },
  { name: '数学' }, { name: '奥数' }, { name: '物理' }, { name: '化学' },
  { name: '生物' }, { name: '历史' }, { name: '地理' }, { name: '政治' },
  { name: '钢琴' }, { name: '小提琴' }, { name: '古筝' }, { name: '跳绳' },
  { name: '篮球' }, { name: '游泳' }, { name: '围棋' }, { name: '书法' },
  { name: '美术' }, { name: '英语口语' }, { name: '四级' }, { name: '托福' },
  { name: '雅思' }, { name: 'SAT' }, { name: 'AP' }, { name: 'A-level' },
  { name: 'IB' }, { name: 'IGCSE' }, { name: '日语' }, { name: '德语' },
  { name: '高数' }, { name: '计算机' }, { name: '初中' }, { name: '高中' },
  { name: '大学考研' }, { name: '高考志愿填报' }, { name: '生涯规划' }, { name: '心理辅导' }
]

const filters = ref({ district: null, subject: null, university: null, tutorType: null, teachingMethod: null })
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
      district: f.district || undefined,
      subject: f.subject || undefined,
      university: f.university || undefined,
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
  padding: 0 0 var(--space-4xl);
}
.xy-page {
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
.ftag:hover { color: var(--color-primary); }
.ftag.active { background: var(--color-primary); color: #fff; }

.ftag.more-link {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}
.ftag.more-link:hover {
  background: var(--color-primary-lighter);
}

.result-header { margin-bottom: var(--space-md); font-size: var(--font-size-base); color: var(--color-text-secondary); }
.result-header strong { color: var(--color-primary); }

/* ============================================
   学员订单 ttgood 风格行卡 (4 列)
   ============================================ */
.req-list {
  display: flex; flex-direction: column;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.req-row {
  display: grid;
  grid-template-columns: 220px 200px 1fr 160px;
  gap: 24px;
  align-items: center;
  padding: 22px 24px;
  background: #fff;
  border-bottom: 1px dashed #e5e7eb;
  text-decoration: none;
  position: relative;
  transition: background 0.15s, transform 0.15s;
}
.req-row:hover {
  background: #f8fafc;
}
.req-row:hover .rc-cta {
  background: #b45309;
  transform: translateX(2px);
  box-shadow: 0 6px 20px -8px rgba(180, 83, 9, 0.45);
}
.req-row:first-child { border-top: 1px dashed #e5e7eb; }
.req-row.is-matched {
  opacity: 0.65;
  cursor: default;
  background: #fafbfc;
}
.req-row.is-matched:hover { background: #fafbfc; }
.req-row.is-matched:hover .rc-cta { background: #d97706; transform: none; box-shadow: none; }

/* col 1 */
.rc-c1 { display: flex; flex-direction: column; gap: 4px; min-width: 0; position: relative; }
.rc-title {
  font-size: 18px; font-weight: 700;
  color: #111827;
  letter-spacing: 0.5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.rc-no {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12.5px;
  color: #94a3b8;
  letter-spacing: 0.5px;
}

/* col 2 */
.rc-c2 { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.rc-region {
  font-size: 14px;
  color: #111827;
  font-weight: 500;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.rc-addr {
  font-size: 13px;
  color: #64748b;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.rc-online {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #d97706;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 1px;
}
.rc-online svg { flex-shrink: 0; }

/* col 3 */
.rc-c3 { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
.rc-req {
  font-size: 14px;
  color: #111827;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
  letter-spacing: 0.3px;
}
.rc-pref {
  font-size: 13px;
  color: #64748b;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}

/* col 4 */
.rc-c4 {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 16px;
  min-width: 0;
}
.rc-budget {
  font-size: 16px;
  font-weight: 700;
  color: #d97706;
  font-family: Georgia, serif;
  letter-spacing: 0.5px;
  white-space: nowrap;
}
.rc-cta {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  background: #d97706;
  color: #fff;
  padding: 8px 18px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 4px;
  transition: background 0.15s, transform 0.15s, box-shadow 0.15s;
}
.rc-done {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  color: #94a3b8;
  padding: 8px 18px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 4px;
  border: 1px dashed #cbd5e1;
  border-radius: 4px;
  cursor: default;
  user-select: none;
  background: transparent;
}

@media (max-width: 1100px) {
  .req-row {
    grid-template-columns: 1fr 1fr;
    gap: 14px 20px;
    padding: 18px 20px;
  }
  .rc-c4 {
    grid-column: 1 / -1;
    justify-content: space-between;
    border-top: 1px dashed #f1f5f9;
    padding-top: 14px;
  }
}

@media (max-width: 768px) {
  .xy-page { padding: var(--space-md); }
  .filter-row { flex-direction: column; }
  .filter-label { width: auto; margin-bottom: 4px; }
  .req-row {
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 16px;
  }
  .rc-c4 {
    flex-direction: row-reverse;
    border-top: 1px dashed #f1f5f9;
    padding-top: 12px;
    margin-top: 4px;
  }
  .rc-cta { padding: 8px 16px; }
}
</style>
