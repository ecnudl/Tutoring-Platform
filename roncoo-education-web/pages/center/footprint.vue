<template>
<div>
  <h2 class="page-title">浏览足迹</h2>
  <el-button type="danger" size="small" style="margin-bottom:16px" @click="handleClear" :disabled="!list.length">清空足迹</el-button>
  <el-table :data="list" border stripe empty-text="暂无浏览记录" v-loading="loading">
    <el-table-column label="类型" width="80">
      <template #default="{ row }">{{ row.targetType === 1 ? '教员' : '需求' }}</template>
    </el-table-column>
    <el-table-column prop="targetId" label="ID" width="200" />
    <el-table-column prop="gmtCreate" label="浏览时间" />
  </el-table>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
definePageMeta({ layout: 'center' })
const { post, del } = useApi()
const list = ref([])
const loading = ref(false)
const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/footprint/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}
const handleClear = async () => {
  const res = await del('/user/auth/footprint/clear')
  if (res.code === 200) { ElMessage.success('已清空'); list.value = [] }
  else ElMessage.error(res.msg)
}
onMounted(() => { load() })
</script>
