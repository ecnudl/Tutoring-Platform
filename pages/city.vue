<template>
  <div class="city-page-wrapper">
  <div class="container city-page">
    <Head>
      <Title>选择城市 - 591家教网</Title>
      <Meta name="description" content="选择您所在的城市，获取本地家教服务。" />
    </Head>

    <el-breadcrumb separator="/" style="margin-bottom:20px">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>选择城市</el-breadcrumb-item>
    </el-breadcrumb>

    <h1 class="page-title">选择城市</h1>

    <el-card>
      <div class="current-city">
        <span>当前城市：</span>
        <el-tag type="primary" size="large">{{ cityStore.cityName }}</el-tag>
      </div>

      <h3 style="margin:24px 0 12px">可选城市</h3>
      <div class="city-grid">
        <div
          v-for="c in cities"
          :key="c.pinyin"
          class="city-item"
          :class="{ active: cityStore.cityId === c.id, disabled: !c.enabled }"
          @click="selectCity(c)"
        >
          <span class="city-name">{{ c.name }}</span>
          <span class="city-domain" v-if="c.enabled">{{ c.domain }}</span>
          <el-tag v-if="!c.enabled" size="small" type="info" class="city-badge">即将开通</el-tag>
        </div>
      </div>

      <div class="city-note">
        <el-icon><InfoFilled /></el-icon>
        <span>已开通上海、苏州、南京、合肥、杭州五个城市。切换城市后将展示对应城市的教员和需求信息。</span>
      </div>
    </el-card>
  </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import { useCityStore } from '~/stores/city'

const cityStore = useCityStore()
const router = useRouter()

const cities = [
  { id: 1, name: '上海', pinyin: 'shanghai', enabled: true, domain: '主站' },
  { id: 2, name: '苏州', pinyin: 'suzhou', enabled: true, domain: 'suzhou' },
  { id: 5, name: '南京', pinyin: 'nanjing', enabled: true, domain: 'nanjing' },
  { id: 3, name: '合肥', pinyin: 'hefei', enabled: true, domain: 'hefei' },
  { id: 4, name: '杭州', pinyin: 'hangzhou', enabled: true, domain: 'hangzhou' }
]

const selectCity = (city) => {
  if (!city.enabled) {
    ElMessage.info(`${city.name}站即将开通，敬请期待`)
    return
  }
  cityStore.setCity(city.id, city.name)
  ElMessage.success(`已切换到${city.name}`)
  router.push('/')
}
</script>

<style scoped>
.city-page-wrapper {
  background: var(--color-bg);
  padding: 0 0 var(--space-4xl);
}
.city-page {
  padding: 20px;
  background: var(--color-surface);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.current-city { display: flex; align-items: center; gap: 8px; font-size: 16px; }
.city-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; }
.city-item {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 16px; border: 2px solid #eee; border-radius: 8px; cursor: pointer; transition: all 0.2s;
  min-height: 70px;
}
.city-item:hover { border-color: #409eff; }
.city-item.active { border-color: #409eff; background: #ecf5ff; }
.city-item.disabled { opacity: 0.6; cursor: not-allowed; }
.city-item.disabled:hover { border-color: #eee; }
.city-name { font-size: 16px; font-weight: 500; }
.city-domain { font-size: 11px; color: #999; margin-top: 4px; }
.city-badge { margin-top: 4px; }
.city-note { display: flex; align-items: center; gap: 6px; margin-top: 24px; padding: 12px; background: #fdf6ec; border-radius: 4px; font-size: 14px; color: #e6a23c; }

@media (max-width: 768px) {
  .city-grid { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 480px) {
  .city-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
