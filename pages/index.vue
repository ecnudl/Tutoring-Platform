<template>
  <div class="home-page">
    <Head>
      <Title>51家教网 - 找家教上门辅导一对一</Title>
      <Meta name="description" content="51家教网,提供上门家教一对一辅导服务,大学生家教、专职教员、在职教师,覆盖小学初中高中全科辅导。" />
    </Head>

    <div class="container">
      <!-- Hero: 双入口 -->
      <div class="hero-section">
        <div class="hero-text">
          <h1>{{ cityStore.cityName }}家教上门辅导平台</h1>
          <p class="hero-desc">大学生教员、专职教员、在职教师，一对一上门辅导，覆盖小学到高中全科。提交需求后 24 小时内推荐合适教员。</p>
          <div class="hero-actions">
            <NuxtLink to="/qjj">
              <el-button type="primary" size="large">免费发布需求</el-button>
            </NuxtLink>
            <NuxtLink to="/jy">
              <el-button size="large" plain>浏览教员库</el-button>
            </NuxtLink>
          </div>
          <div class="hero-stats">
            <span><strong>5000+</strong> 注册教员</span>
            <span><strong>20000+</strong> 成功匹配</span>
            <span><strong>5</strong> 城市覆盖</span>
          </div>
        </div>
        <div class="hero-entry">
          <NuxtLink to="/qjj" class="entry-card entry-parent">
            <div class="entry-icon">👨‍👩‍👧</div>
            <h3>我是家长</h3>
            <p>快速找到合适的家教老师</p>
          </NuxtLink>
          <NuxtLink to="/register/teacher" class="entry-card entry-teacher">
            <div class="entry-icon">🎓</div>
            <h3>我是教员</h3>
            <p>注册接单赚取课时费</p>
          </NuxtLink>
        </div>
      </div>

      <!-- 快速筛选 -->
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
            <NuxtLink v-for="u in universities" :key="u" :to="'/jy?university=' + encodeURIComponent(u)">{{ u }}</NuxtLink>
            <NuxtLink to="/university" class="more-link">更多高校 &rarr;</NuxtLink>
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
          <NuxtLink to="/jy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>
        <div v-if="tutors.length" class="tutor-grid">
          <NuxtLink v-for="t in tutors" :key="t.id" :to="'/jy/t' + (t.displayNo || t.id)" class="tutor-card">
            <el-avatar :size="56" :src="t.avatar" />
            <div class="tutor-info">
              <div class="tutor-name">{{ t.realName || '教员' }}</div>
              <div class="tutor-school">{{ t.university || '未填写' }}</div>
              <div class="tutor-price" v-if="t.priceMin">{{ t.priceMin }}-{{ t.priceMax }}元/时</div>
            </div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无教员数据" />
      </div>

      <!-- 最新需求 -->
      <div class="section-box">
        <div class="section-header">
          <h2>最新学员需求</h2>
          <NuxtLink to="/xy" class="more-link">查看更多 &rarr;</NuxtLink>
        </div>
        <div v-if="requirements.length" class="req-list">
          <NuxtLink v-for="r in requirements" :key="r.id" :to="'/xy/a' + r.id" class="req-item">
            <div class="req-title">{{ r.title || '家教需求' }}</div>
            <div class="req-budget" v-if="r.budgetMin">预算: {{ r.budgetMin }}-{{ r.budgetMax }}元/小时</div>
          </NuxtLink>
        </div>
        <el-empty v-else description="暂无需求数据" />
      </div>

      <!-- 服务保障 -->
      <div class="guarantee-section">
        <div class="guarantee-item">
          <div class="g-icon">&#x2714;</div>
          <div>
            <h4>教员实名认证</h4>
            <p>所有教员均通过身份和学历审核</p>
          </div>
        </div>
        <div class="guarantee-item">
          <div class="g-icon">&#x2714;</div>
          <div>
            <h4>免费试讲</h4>
            <p>首次可安排免费试讲，满意后再上课</p>
          </div>
        </div>
        <div class="guarantee-item">
          <div class="g-icon">&#x2714;</div>
          <div>
            <h4>无中介费</h4>
            <p>平台不收取中介费用，课时费直接结算</p>
          </div>
        </div>
        <div class="guarantee-item">
          <div class="g-icon">&#x2714;</div>
          <div>
            <h4>7天免费换师</h4>
            <p>开课7天内不满意可免费更换教员</p>
          </div>
        </div>
      </div>

      <!-- 请家教流程 -->
      <div class="section-box">
        <div class="section-header">
          <h2>请家教流程</h2>
        </div>
        <div class="flow-steps">
          <div class="flow-step">
            <div class="flow-num">1</div>
            <h4>提交需求</h4>
            <p>填写科目、年级、时间等信息</p>
          </div>
          <div class="flow-arrow">&rarr;</div>
          <div class="flow-step">
            <div class="flow-num">2</div>
            <h4>匹配教员</h4>
            <p>平台推荐合适的教员人选</p>
          </div>
          <div class="flow-arrow">&rarr;</div>
          <div class="flow-step">
            <div class="flow-num">3</div>
            <h4>免费试讲</h4>
            <p>与教员沟通并安排试讲</p>
          </div>
          <div class="flow-arrow">&rarr;</div>
          <div class="flow-step">
            <div class="flow-num">4</div>
            <h4>开始上课</h4>
            <p>确认后正式开始辅导</p>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const cityStore = useCityStore()
const { districts, universities } = useCityData()
const { post } = useApi()

const tutors = ref([])
const requirements = ref([])

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
.home-page {
  background: var(--color-bg);
  padding-bottom: var(--space-4xl);
}
.container {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
}

/* Hero */
.hero-section {
  display: flex;
  gap: var(--space-2xl);
  align-items: center;
  padding: var(--space-3xl) 0;
}

.hero-text {
  flex: 1;
}

.hero-text h1 {
  font-size: 28px;
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
  margin-bottom: var(--space-md);
  line-height: var(--line-height-tight);
}

.hero-desc {
  font-size: var(--font-size-md);
  color: var(--color-text-secondary);
  line-height: var(--line-height-relaxed);
  margin-bottom: var(--space-2xl);
  max-width: 480px;
}

.hero-actions {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-2xl);
}

.hero-stats {
  display: flex;
  gap: var(--space-2xl);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.hero-stats strong {
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

.hero-entry {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
  flex-shrink: 0;
  width: 380px;
}

.entry-card {
  display: flex;
  align-items: center;
  gap: var(--space-lg);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-xl) var(--space-2xl);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}
.entry-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-lg);
}

.entry-icon {
  font-size: 32px;
  flex-shrink: 0;
}

.entry-card h3 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 2px;
}

.entry-card p {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}

/* Category nav */
.category-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-lg) var(--space-xl);
  margin-bottom: var(--space-2xl);
}

.category-row {
  display: flex;
  padding: var(--space-sm) 0;
  border-bottom: 1px solid var(--color-border-light);
}
.category-row:last-child {
  border-bottom: none;
}

.category-label {
  width: 80px;
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  flex-shrink: 0;
  font-size: var(--font-size-base);
  line-height: 28px;
}

.category-links {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-lg);
}
.category-links a {
  color: var(--color-text-secondary);
  font-size: var(--font-size-base);
  transition: color var(--transition-fast);
  line-height: 28px;
}
.category-links a:hover {
  color: var(--color-primary);
}

/* Section box */
.section-box {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  margin-bottom: var(--space-2xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-xl);
  padding-bottom: var(--space-md);
  border-bottom: 1px solid var(--color-border-light);
}

.section-header h2 {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.more-link {
  color: var(--color-primary);
  font-size: var(--font-size-sm);
}
.more-link:hover {
  color: var(--color-primary-light);
}

/* Tutor cards */
.tutor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-lg);
}

.tutor-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color var(--transition-fast);
}
.tutor-card:hover {
  border-color: var(--color-primary);
}

.tutor-info {
  text-align: center;
  margin-top: var(--space-md);
  width: 100%;
}

.tutor-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 2px;
}

.tutor-school {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-bottom: 4px;
}

.tutor-price {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-accent-dark);
}

/* Requirement list */
.req-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.req-item {
  padding: var(--space-lg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color var(--transition-fast);
}
.req-item:hover {
  border-color: var(--color-primary);
}

.req-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  margin-bottom: var(--space-sm);
}

.req-budget {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-accent-dark);
}

/* Guarantee */
.guarantee-section {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-lg);
  margin-bottom: var(--space-2xl);
}

.guarantee-item {
  display: flex;
  gap: var(--space-md);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
}

.g-icon {
  width: 28px;
  height: 28px;
  background: var(--color-success);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
}

.guarantee-item h4 {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 2px;
}

.guarantee-item p {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}

/* Flow steps */
.flow-steps {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  gap: var(--space-lg);
}

.flow-step {
  text-align: center;
  flex: 1;
}

.flow-num {
  width: 36px;
  height: 36px;
  background: var(--color-primary);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-lg);
  margin: 0 auto var(--space-md);
}

.flow-step h4 {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 4px;
}

.flow-step p {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.flow-arrow {
  color: var(--color-text-muted);
  font-size: 20px;
  margin-top: 8px;
  flex-shrink: 0;
}

@media (max-width: 768px) {
  .hero-section {
    flex-direction: column;
    padding: var(--space-xl) 0;
  }

  .hero-entry {
    flex-direction: row;
    width: 100%;
  }
  .entry-card { flex: 1; }

  .tutor-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .guarantee-section {
    grid-template-columns: repeat(2, 1fr);
  }

  .category-row {
    flex-direction: column;
  }
  .category-label {
    margin-bottom: var(--space-xs);
  }

  .flow-steps {
    flex-wrap: wrap;
  }
  .flow-arrow {
    display: none;
  }
  .flow-step {
    min-width: 120px;
  }
}
</style>
