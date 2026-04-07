<template>
  <div class="container zf-page">
    <Head>
      <Title>家教价格参考 - 51家教网</Title>
      <Meta name="description" content="上海家教收费标准参考，涵盖大学生、专职教员、在职教师各年级段课时费。" />
    </Head>

    <el-breadcrumb separator="/" style="margin-bottom:20px">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>价格参考</el-breadcrumb-item>
    </el-breadcrumb>

    <h1 class="page-title">{{ cityStore.cityName }}家教价格参考</h1>
    <p class="page-sub">以下价格为平台参考价，实际价格以教员标价为准（单位：元/小时）</p>

    <div v-loading="loading">
      <el-table :data="priceData" border stripe style="width:100%;margin-bottom:24px">
        <el-table-column prop="tutorType" label="教员类型" width="120" fixed />
        <el-table-column prop="primary" label="小学" min-width="100" />
        <el-table-column prop="juniorMiddle" label="初一初二" min-width="100" />
        <el-table-column prop="juniorThird" label="初三" min-width="100" />
        <el-table-column prop="seniorFirst" label="高一高二" min-width="100" />
        <el-table-column prop="seniorThird" label="高三" min-width="100" />
        <el-table-column prop="college" label="大学/成人" min-width="100" />
      </el-table>
    </div>

    <div class="notes-section">
      <h2>价格说明</h2>
      <ul class="notes-list">
        <li>以上价格为上门一对一辅导参考价格，在线辅导一般可享 <strong>8-9折</strong> 优惠。</li>
        <li>艺术类（钢琴、美术、书法等）价格另议，通常高于文化课 20%-50%。</li>
        <li>小语种（日语、法语、德语等）价格参考大学/成人栏目。</li>
        <li>首次上课可安排免费试讲30分钟（需与教员协商）。</li>
        <li>平台不收取中介费，教员课时费由教员自定，家长与教员直接结算。</li>
        <li>以上价格仅供参考，具体价格请与教员沟通确认。</li>
      </ul>
    </div>

    <div class="cta-section">
      <h2>需要找家教？</h2>
      <div class="cta-buttons">
        <NuxtLink to="/qjj"><el-button type="primary" size="large">免费提交需求</el-button></NuxtLink>
        <NuxtLink to="/jy"><el-button size="large">浏览教员库</el-button></NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'

const cityStore = useCityStore()
const { get } = useApi()
const loading = ref(false)

const defaultPriceData = [
  { tutorType: '大学生', primary: '80-120', juniorMiddle: '100-150', juniorThird: '120-180', seniorFirst: '150-200', seniorThird: '180-250', college: '150-250' },
  { tutorType: '研究生', primary: '100-150', juniorMiddle: '120-180', juniorThird: '150-220', seniorFirst: '180-250', seniorThird: '200-300', college: '200-300' },
  { tutorType: '专职教员', primary: '120-180', juniorMiddle: '150-220', juniorThird: '180-280', seniorFirst: '200-300', seniorThird: '250-400', college: '250-400' },
  { tutorType: '在职教师', primary: '150-250', juniorMiddle: '200-300', juniorThird: '250-400', seniorFirst: '300-500', seniorThird: '350-600', college: '300-500' },
  { tutorType: '海归外教', primary: '200-350', juniorMiddle: '250-400', juniorThird: '300-500', seniorFirst: '350-600', seniorThird: '400-800', college: '400-800' }
]

const priceData = ref(defaultPriceData)

onMounted(async () => {
  loading.value = true
  try {
    const res = await get('/user/api/homepage/price-reference', { cityId: cityStore.cityId })
    if (res.code === 200 && res.data && res.data.length) {
      priceData.value = res.data
    }
  } catch (e) {
    // use default data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.zf-page { padding: var(--space-xl); }
.page-sub { color: var(--color-text-muted); margin-bottom: var(--space-2xl); margin-top: calc(-1 * var(--space-md)); }

.notes-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  margin-bottom: var(--space-2xl);
}
.notes-section h2 { font-size: var(--font-size-xl); margin-bottom: var(--space-md); color: var(--color-text); }
.notes-list { padding-left: var(--space-xl); }
.notes-list li { font-size: var(--font-size-base); color: var(--color-text-secondary); line-height: 2; }
.notes-list strong { color: var(--color-primary); }

.cta-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-3xl);
  text-align: center;
}
.cta-section h2 { font-size: var(--font-size-2xl); margin-bottom: var(--space-lg); color: var(--color-text); }
.cta-buttons { display: flex; gap: var(--space-md); justify-content: center; }

@media (max-width: 768px) {
  .zf-page { padding: var(--space-md); }
  .el-table { font-size: var(--font-size-xs); }
}
</style>
