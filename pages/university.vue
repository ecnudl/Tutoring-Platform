<template>
  <div class="container university-page">
    <Head>
      <Title>{{ cityStore.cityName }}高校教员 - 51家教网</Title>
      <Meta name="description" :content="`${cityStore.cityName}各大高校家教教员，覆盖${cityStore.cityName}所有重点大学，名校大学生一对一上门辅导。`" />
    </Head>

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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'

const cityStore = useCityStore()
const { get } = useApi()
const universities = ref([])

onMounted(async () => {
  cityStore.loadFromStorage()
  try {
    const res = await get('/user/api/dict/university/list', { cityId: cityStore.cityId })
    if (res.code === 200 && res.data) {
      universities.value = res.data
    }
  } catch (e) {
    console.error('加载高校列表失败', e)
  }
})
</script>

<style scoped>
.university-page { padding: 24px 0; }
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
