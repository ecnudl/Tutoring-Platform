<template>
  <div class="page-container">
    <h2 class="page-title">首页配置</h2>
    <div class="filter-bar">
      <el-select v-model="cityId" placeholder="选择城市" @change="load" style="width: 200px">
        <el-option v-for="c in cities" :key="c.id" :label="c.cityName" :value="c.id" />
      </el-select>
    </div>
    <el-form :model="configs" label-width="120px" v-loading="loading" style="max-width: 700px">
      <el-form-item label="服务热线"><el-input v-model="configs.hotline" /></el-form-item>
      <el-form-item label="服务时间"><el-input v-model="configs.hotline_hours" /></el-form-item>
      <el-form-item label="SEO标题"><el-input v-model="configs.seo_title" /></el-form-item>
      <el-form-item label="SEO描述"><el-input v-model="configs.seo_desc" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="SEO关键词"><el-input v-model="configs.seo_keywords" /></el-form-item>
      <el-form-item label="平台介绍"><el-input v-model="configs.intro_text" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="服务说明"><el-input v-model="configs.service_desc" type="textarea" :rows="3" /></el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="handleSave">保存配置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, put } from '@/api'
import { ElMessage } from 'element-plus'

const cities = ref<any[]>([])
const cityId = ref<number | null>(null)
const loading = ref(false)
const saving = ref(false)
const configs = ref<Record<string, string>>({
  hotline: '', hotline_hours: '', seo_title: '', seo_desc: '', seo_keywords: '', intro_text: '', service_desc: ''
})

const loadCities = async () => {
  const res = await post('/system/admin/city/page', { pageCurrent: 1, pageSize: 100 })
  cities.value = res.data?.list || res.data || []
  if (cities.value.length > 0) { cityId.value = cities.value[0].id; load() }
}

const load = async () => {
  if (!cityId.value) return
  loading.value = true
  try {
    const res = await post('/system/admin/homepage-config/list', { cityId: cityId.value })
    const items = res.data || []
    const map: Record<string, string> = {}
    items.forEach((item: any) => { map[item.configKey] = item.configValue || '' })
    configs.value = { hotline: map.hotline || '', hotline_hours: map.hotline_hours || '', seo_title: map.seo_title || '', seo_desc: map.seo_desc || '', seo_keywords: map.seo_keywords || '', intro_text: map.intro_text || '', service_desc: map.service_desc || '' }
  } finally { loading.value = false }
}

const handleSave = async () => {
  saving.value = true
  try {
    await put('/system/admin/homepage-config/batch-save', { cityId: cityId.value, configs: configs.value })
    ElMessage.success('保存成功')
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}

onMounted(loadCities)
</script>
