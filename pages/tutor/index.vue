<template>
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
              <el-input v-model="filters.keyword" placeholder="姓名搜索" clearable />
            </el-form-item>
            <el-form-item label="科目">
              <el-select v-model="filters.subjectId" placeholder="全部科目" clearable style="width:100%">
                <el-option v-for="s in dictStore.subjects" :key="s.id" :label="s.subjectName" :value="s.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="教员类型">
              <el-select v-model="filters.tutorType" placeholder="全部" clearable style="width:100%">
                <el-option label="大学生" :value="1" />
                <el-option label="专职" :value="2" />
                <el-option label="在职教师" :value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="filters.gender">
                <el-radio :label="0">不限</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width:100%" @click="search(); showFilter = false">搜索</el-button>
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
          <div v-if="tutors.length" class="card-grid">
            <el-card v-for="t in tutors" :key="t.id" shadow="hover" style="cursor:pointer" @click="$router.push('/tutor/'+t.id)">
              <div style="display:flex;gap:12px">
                <el-avatar :size="60" :src="t.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
                <div style="flex:1">
                  <h3 style="margin-bottom:4px">{{ t.realName || '教员' }}</h3>
                  <p style="color:#999;font-size:13px">{{ t.university || '' }} {{ t.major ? '· ' + t.major : '' }}</p>
                  <p style="color:#e6a23c;margin-top:4px;font-weight:600" v-if="t.priceMin || t.priceMax">
                    {{ t.priceMin || 0 }}-{{ t.priceMax || 0 }}元/小时
                  </p>
                  <div style="margin-top:6px" v-if="t.tutorType">
                    <el-tag size="small" type="info">{{ getTutorTypeLabel(t.tutorType) }}</el-tag>
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
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import { useDictStore } from '~/stores/dict'

const dictStore = useDictStore()
const { post } = useApi()

const filters = ref({ keyword: '', subjectId: null, tutorType: null, gender: 0 })
const tutors = ref([])
const total = ref(0)
const pageCurrent = ref(1)
const pageSize = ref(12)
const loading = ref(false)
const showFilter = ref(false)

const getTutorTypeLabel = (type) => ({ 1: '大学生', 2: '专职', 3: '在职教师', 4: '退休教师' }[type] || '')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/api/tutor/search', {
      pageCurrent: pageCurrent.value, pageSize: pageSize.value,
      keyword: filters.value.keyword || undefined,
      tutorType: filters.value.tutorType || undefined,
      gender: filters.value.gender || undefined
    })
    if (res.code === 200 && res.data) { tutors.value = res.data.list || []; total.value = res.data.totalCount || 0 }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const onPageChange = (page) => { pageCurrent.value = page; search() }

onMounted(async () => { await dictStore.fetchAll(); await search() })
</script>
<style scoped>
.tutor-page { padding: 20px; }
.tutor-layout { display: flex; gap: 20px; }
.tutor-sidebar { width: 240px; flex-shrink: 0; }
.tutor-main { flex: 1; }
.mobile-filter-toggle { display: none; }
.desktop-only { display: block; }

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
</style>
