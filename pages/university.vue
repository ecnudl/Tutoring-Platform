<template>
  <div class="uni-page-wrapper">
  <div class="container university-page">
    <Head>
      <Title>{{ cityStore.cityName }}高校教员 - 591家教网</Title>
      <Meta name="description" :content="`${cityStore.cityName}各大高校家教教员，覆盖${cityStore.cityName}所有重点大学，名校大学生一对一上门辅导。`" />
    </Head>

    <el-breadcrumb separator="/" style="margin-bottom:20px">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>高校教员</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="page-header">
      <h1 class="page-title">{{ cityStore.cityName }}高校教员</h1>
      <p class="page-sub">选择高校查看该校教员，快速找到名校大学生家教</p>
    </div>

    <div class="uni-grid" v-if="universities.length">
      <NuxtLink
        v-for="u in universities"
        :key="u.id"
        :to="'/jy?university=' + u.id"
        class="uni-card"
        :class="{ 'is-hot': u.isHot === 1 }"
      >
        <div class="uni-icon">🎓</div>
        <div class="uni-info">
          <div class="uni-name">{{ u.uniName }}</div>
          <div class="uni-short" v-if="u.uniShort">{{ u.uniShort }}</div>
        </div>
        <el-tag v-if="u.isHot === 1" type="danger" size="small" effect="light">热门</el-tag>
      </NuxtLink>
    </div>

    <div v-else style="text-align:center;padding:60px;color:#999">
      <p>暂无高校数据</p>
    </div>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'
import { useCityData } from '~/composables/useCityData'

const cityStore = useCityStore()
const { get } = useApi()
const { universities: localUniversities } = useCityData()
const universities = ref([])

onMounted(async () => {
  try {
    const res = await get('/user/api/dict/university/list', { cityId: cityStore.cityId })
    if (res.code === 200 && res.data && res.data.length) {
      universities.value = res.data
    } else {
      // API 无数据时使用本地高校列表，统一卡片格式
      universities.value = localUniversities.value.map((name, idx) => ({
        id: idx + 1,
        uniName: name,
        uniShort: '',
        isHot: idx < 3 ? 1 : 0
      }))
    }
  } catch (e) {
    console.error('加载高校列表失败', e)
    // 请求失败也用本地数据兜底
    universities.value = localUniversities.value.map((name, idx) => ({
      id: idx + 1,
      uniName: name,
      uniShort: '',
      isHot: idx < 3 ? 1 : 0
    }))
  }
})
</script>

<style scoped>
.uni-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.university-page {
  padding: 24px;
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.page-header { margin-bottom: 24px; }
.page-title { font-size: 24px; font-weight: 700; }
.page-sub { color: #999; margin-top: 4px; }

.uni-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.uni-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  transition: box-shadow 0.3s, transform 0.2s;
  border: 1px solid #f0f0f0;
}
.uni-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transform: translateY(-2px);
}
.uni-card.is-hot {
  border-color: #f56c6c33;
  background: #fff5f5;
}

.uni-icon { font-size: 28px; flex-shrink: 0; }
.uni-info { flex: 1; min-width: 0; }
.uni-name { font-size: 15px; font-weight: 600; color: #333; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.uni-short { font-size: 12px; color: #999; margin-top: 2px; }

@media (max-width: 768px) {
  .uni-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 480px) {
  .uni-grid { grid-template-columns: 1fr; }
}
</style>
