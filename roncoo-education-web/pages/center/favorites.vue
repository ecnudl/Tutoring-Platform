<template>
<div>
  <h2 class="page-title">我的收藏</h2>
  <el-table :data="list" border stripe empty-text="暂无收藏" v-loading="loading">
    <el-table-column label="类型" width="80">
      <template #default="{ row }">{{ row.targetType === 1 ? '教员' : '需求' }}</template>
    </el-table-column>
    <el-table-column prop="targetId" label="ID" width="200" />
    <el-table-column prop="gmtCreate" label="收藏时间" width="180" />
    <el-table-column label="操作" width="120">
      <template #default="{ row }">
        <el-popconfirm title="取消收藏？" @confirm="handleRemove(row.id)">
          <template #reference><el-button size="small" type="danger">取消</el-button></template>
        </el-popconfirm>
      </template>
    </el-table-column>
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
    const res = await post('/user/auth/favorite/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) list.value = res.data.list || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}
const handleRemove = async (id) => {
  const res = await del('/user/auth/favorite/remove', { id })
  if (res.code === 200) { ElMessage.success('已取消'); await load() }
  else ElMessage.error(res.msg)
}
onMounted(() => { load() })
</script>
