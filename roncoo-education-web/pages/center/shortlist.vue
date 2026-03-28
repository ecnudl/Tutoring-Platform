<template>
<div>
  <h2 class="page-title">备选老师</h2>
  <el-table :data="list" border stripe empty-text="暂无备选老师" v-loading="loading">
    <el-table-column prop="tutorId" label="教员ID" width="200" />
    <el-table-column prop="remark" label="备注" />
    <el-table-column prop="gmtCreate" label="添加时间" width="180" />
    <el-table-column label="操作" width="160">
      <template #default="{ row }">
        <el-button size="small" @click="$router.push('/tutor/' + row.tutorId)">查看</el-button>
        <el-popconfirm title="确定移除？" @confirm="handleRemove(row.id)">
          <template #reference>
            <el-button size="small" type="danger">移除</el-button>
          </template>
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
    const res = await post('/user/auth/shortlist/page', { pageCurrent: 1, pageSize: 50 })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleRemove = async (id) => {
  try {
    const res = await del('/user/auth/shortlist/remove', { id })
    if (res.code === 200) { ElMessage.success('已移除'); await load() }
    else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { load() })
</script>
