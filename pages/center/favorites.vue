<template>
<div>
  <h2 class="page-title">备选教员</h2>
  <p style="color:#666;margin-bottom:16px">您收藏的教员列表，方便对比选择</p>

  <div v-if="loading" style="text-align:center;padding:40px">
    <el-icon class="is-loading" :size="32"><Loading /></el-icon>
  </div>

  <template v-else>
    <div v-if="list.length" class="card-grid">
      <el-card v-for="item in list" :key="item.id" shadow="hover">
        <div style="display:flex;gap:12px">
          <el-avatar :size="60" :src="item.tutor?.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
          <div style="flex:1">
            <h3 style="margin-bottom:4px">{{ item.tutor?.realName || '教员' }}</h3>
            <p style="color:#999;font-size:13px">{{ item.tutor?.university || '' }} {{ item.tutor?.major ? '· ' + item.tutor?.major : '' }}</p>
            <p style="color:#e6a23c;margin-top:4px;font-weight:600" v-if="item.tutor?.priceMin">
              {{ item.tutor.priceMin }}-{{ item.tutor.priceMax }}元/小时
            </p>
            <div style="margin-top:8px;display:flex;gap:8px">
              <el-button size="small" type="primary" @click="$router.push('/jy/t' + item.targetId)">查看详情</el-button>
              <el-popconfirm title="确定移除？" @confirm="handleRemove(item.id)">
                <template #reference>
                  <el-button size="small" type="danger">移除</el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </div>
        <div style="margin-top:8px;padding-top:8px;border-top:1px solid #eee;font-size:12px;color:#999">
          收藏时间：{{ formatDate(item.gmtCreate) }}
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无备选教员" />
  </template>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const { post, del } = useApi()
const list = ref([])
const loading = ref(false)

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/auth/favorite/list', { targetType: 1 })
    if (res.code === 200 && res.data) {
      list.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleRemove = async (id) => {
  try {
    const res = await del('/user/auth/favorite/remove', { id })
    if (res.code === 200) {
      ElMessage.success('已移除')
      await load()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  load()
})
</script>

<style scoped>
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
}
</style>
