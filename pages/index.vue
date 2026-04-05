<template>
  <div class="home-page">
    <Head>
      <Title>51家教网 - 找家教上门辅导一对一</Title>
      <Meta name="description" content="51家教网,提供上门家教一对一辅导服务,大学生家教、专职教员、在职教师,覆盖小学初中高中全科辅导。" />
    </Head>

    <div class="container">
      <!-- 轮播横幅 -->
      <div class="banner-section">
        <el-carousel height="280px" :interval="4000" arrow="always">
          <el-carousel-item v-for="i in 3" :key="i">
            <div class="banner-slide" :class="'slide-' + i">
              <div class="banner-content">
                <h2 v-if="i === 1">专业上门家教服务</h2>
                <p v-if="i === 1">名校大学生、在职教师，一对一上门辅导</p>
                <h2 v-if="i === 2">严选优质教员</h2>
                <p v-if="i === 2">身份认证、学历审核、试讲评估</p>
                <h2 v-if="i === 3">快速匹配教员</h2>
                <p v-if="i === 3">提交需求24小时内推荐合适教员</p>
                <el-button type="primary" size="large" @click="$router.push('/jy')" style="margin-top:16px">
                  立即找教员
                </el-button>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 快捷入口 -->
      <div class="quick-entry">
        <NuxtLink to="/qjj" class="entry-card parent">
          <div class="entry-icon">👨‍👩‍👧</div>
          <h3>我是家长</h3>
          <p>快速找到合适的家教老师</p>
        </NuxtLink>
        <NuxtLink to="/register/teacher" class="entry-card teacher">
          <div class="entry-icon">🎓</div>
          <h3>我是教员</h3>
          <p>注册成为家教老师接单</p>
        </NuxtLink>
        <div class="entry-card hotline">
          <div class="entry-icon">📞</div>
          <h3>服务热线</h3>
          <p>400-000-0000</p>
        </div>
      </div>

      <!-- 分类导航 -->
      <div class="category-section">
        <div class="category-row">
          <div class="category-label">按区域</div>
          <div class="category-links">
            <NuxtLink v-for="d in districts" :key="d.id" :to="'/jy?district=' + d.id">{{ d.name }}</NuxtLink>
          </div>
        </div>

        <div class="category-row">
          <div class="category-label">按科目</div>
          <div class="category-links">
            <NuxtLink v-for="s in hotSubjects" :key="s.id" :to="'/jy?subject=' + s.id">{{ s.name }}</NuxtLink>
          </div>
        </div>

        <div class="category-row">
          <div class="category-label">按高校</div>
          <div class="category-links">
            <NuxtLink to="/jy?university=1">复旦大学</NuxtLink>
            <NuxtLink to="/jy?university=2">上海交大</NuxtLink>
            <NuxtLink to="/jy?university=3">同济大学</NuxtLink>
            <NuxtLink to="/jy?university=4">华东师大</NuxtLink>
            <NuxtLink to="/jy?university=5">上海财大</NuxtLink>
            <NuxtLink to="/jy?university=6">华东理工</NuxtLink>
            <NuxtLink to="/jy?university=7">上海大学</NuxtLink>
            <NuxtLink to="/university">更多高校</NuxtLink>
          </div>
        </div>

        <div class="category-row">
          <div class="category-label">按类型</div>
          <div class="category-links">
            <NuxtLink to="/jy?tutorType=1">大学生</NuxtLink>
            <NuxtLink to="/jy?tutorType=2">专职教员</NuxtLink>
            <NuxtLink to="/jy?tutorType=3">在职教师</NuxtLink>
            <NuxtLink to="/jy?tutorType=4">海归外教</NuxtLink>
          </div>
        </div>
      </div>

      <!-- 最新教员 -->
      <div class="section-box">
        <div class="section-header">
          <h2>最新教员</h2>
          <NuxtLink to="/jy" class="more-link">查看更多 →</NuxtLink>
        </div>
        <div v-if="tutors.length" class="tutor-grid">
          <NuxtLink v-for="t in tutors" :key="t.id" :to="'/jy/t' + (t.displayNo || t.id)" class="tutor-card">
            <el-avatar :size="64" :src="t.avatar" />
            <div class="tutor-info">
              <div class="tutor-name">{{ t.realName || '教员' }}</div>
              <div class="tutor-school">{{ t.university || '未填写' }}</div>
              <div class="tutor-price" v-if="t.priceMin">{{ t.priceMin }}-{{ t.priceMax }}元/小时</div>
            </div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无教员数据" />
      </div>

      <!-- 最新需求 -->
      <div class="section-box">
        <div class="section-header">
          <h2>最新学员需求</h2>
          <NuxtLink to="/xy" class="more-link">查看更多 →</NuxtLink>
        </div>
        <div v-if="requirements.length" class="req-list">
          <NuxtLink v-for="r in requirements" :key="r.id" :to="'/xy/a' + r.id" class="req-item">
            <div class="req-title">{{ r.title || '家教需求' }}</div>
            <div class="req-budget" v-if="r.budgetMin">预算: {{ r.budgetMin }}-{{ r.budgetMax }}元/小时</div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无需求数据" />
      </div>
    </div>

    <!-- 公告悬浮球 -->
    <AnnouncementFloat />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'

const cityStore = useCityStore()
const { post } = useApi()

const tutors = ref([])
const requirements = ref([])

const districts = [
  { id: 1, name: '浦东新区' }, { id: 2, name: '徐汇区' }, { id: 3, name: '长宁区' }, { id: 4, name: '静安区' },
  { id: 5, name: '普陀区' }, { id: 6, name: '虹口区' }, { id: 7, name: '杨浦区' }, { id: 8, name: '闵行区' },
  { id: 9, name: '宝山区' }, { id: 10, name: '嘉定区' }, { id: 11, name: '金山区' }, { id: 12, name: '松江区' }
]

const hotSubjects = [
  { id: 1, name: '数学' }, { id: 2, name: '英语' }, { id: 3, name: '语文' }, { id: 4, name: '物理' },
  { id: 5, name: '化学' }, { id: 6, name: '钢琴' }, { id: 7, name: '美术' }, { id: 8, name: '编程' },
  { id: 9, name: '日语' }, { id: 10, name: '法语' }, { id: 11, name: '小提琴' }, { id: 12, name: '书法' }
]

onMounted(async () => {
  cityStore.loadFromStorage()
  try {
    const [tRes, rRes] = await Promise.all([
      post('/user/api/tutor/search', { pageCurrent: 1, pageSize: 8 }),
      post('/user/api/requirement/search', { pageCurrent: 1, pageSize: 6 })
    ])
    if (tRes.code === 200 && tRes.data) tutors.value = tRes.data.list || []
    if (rRes.code === 200 && rRes.data) requirements.value = rRes.data.list || []
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.home-page { background: #f5f7fa; padding-bottom: 40px; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }

/* 轮播横幅 */
.banner-section { margin: 20px 0; }
.banner-slide { height: 280px; border-radius: 8px; display: flex; align-items: center; justify-content: center; }
.slide-1 { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.slide-2 { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.slide-3 { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.banner-content { text-align: center; color: #fff; }
.banner-content h2 { font-size: 32px; margin-bottom: 12px; }
.banner-content p { font-size: 16px; opacity: 0.9; }

/* 快捷入口 */
.quick-entry { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin: 24px 0; }
.entry-card { background: #fff; border-radius: 8px; padding: 24px; text-align: center; transition: all 0.3s; border-top: 3px solid; }
.entry-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); transform: translateY(-2px); }
.parent { border-color: #409eff; }
.teacher { border-color: #67c23a; }
.hotline { border-color: #e6a23c; }
.entry-icon { font-size: 36px; margin-bottom: 8px; }
.entry-card h3 { font-size: 18px; margin-bottom: 4px; color: #333; }
.entry-card p { color: #999; font-size: 13px; }

/* 分类导航 */
.category-section { background: #fff; border-radius: 8px; padding: 20px; margin: 24px 0; }
.category-row { display: flex; padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.category-row:last-child { border-bottom: none; }
.category-label { width: 100px; font-weight: 600; color: #333; flex-shrink: 0; }
.category-links { flex: 1; display: flex; flex-wrap: wrap; gap: 16px; }
.category-links a { color: #666; font-size: 14px; transition: color 0.2s; }
.category-links a:hover { color: #409eff; }

/* 区块 */
.section-box { background: #fff; border-radius: 8px; padding: 24px; margin: 24px 0; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding-bottom: 12px; border-bottom: 2px solid #f0f0f0; }
.section-header h2 { font-size: 20px; font-weight: 600; color: #333; }
.more-link { color: #409eff; font-size: 14px; }
.more-link:hover { color: #66b1ff; }

/* 教员卡片 */
.tutor-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.tutor-card { display: flex; flex-direction: column; align-items: center; padding: 16px; border: 1px solid #eee; border-radius: 8px; transition: all 0.3s; }
.tutor-card:hover { border-color: #409eff; box-shadow: 0 2px 8px rgba(64,158,255,0.2); }
.tutor-info { text-align: center; margin-top: 12px; width: 100%; }
.tutor-name { font-size: 16px; font-weight: 600; color: #333; margin-bottom: 4px; }
.tutor-school { font-size: 13px; color: #999; margin-bottom: 4px; }
.tutor-price { font-size: 14px; font-weight: 600; color: #f56c6c; }

/* 需求列表 */
.req-list { display: flex; flex-direction: column; gap: 12px; }
.req-item { padding: 16px; border: 1px solid #eee; border-radius: 8px; transition: all 0.3s; }
.req-item:hover { border-color: #409eff; background: #f0f7ff; }
.req-title { font-size: 15px; font-weight: 500; color: #333; margin-bottom: 8px; }
.req-budget { font-size: 14px; font-weight: 600; color: #f56c6c; }

@media (max-width: 768px) {
  .quick-entry { grid-template-columns: 1fr; }
  .tutor-grid { grid-template-columns: repeat(2, 1fr); }
  .category-row { flex-direction: column; }
  .category-label { margin-bottom: 8px; }
}
</style>
