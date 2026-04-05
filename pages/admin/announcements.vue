<template>
<div class="admin-announcement">
  <div class="page-header">
    <h2>公告管理</h2>
    <el-button type="primary" @click="showAddDialog">发布新公告</el-button>
  </div>

  <el-table :data="announcements" border stripe>
    <el-table-column prop="id" label="ID" width="80" />
    <el-table-column prop="title" label="标题" min-width="200" />
    <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
    <el-table-column prop="gmtCreate" label="发布时间" width="180">
      <template #default="{ row }">
        {{ formatDate(row.gmtCreate) }}
      </template>
    </el-table-column>
    <el-table-column prop="status" label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'info'">
          {{ row.status === 1 ? '上线' : '下线' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200" fixed="right">
      <template #default="{ row }">
        <el-button size="small" @click="editAnnouncement(row)">编辑</el-button>
        <el-button size="small" type="danger" @click="deleteAnnouncement(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 添加/编辑弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑公告' : '发布新公告'"
    width="600px"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="标题" required>
        <el-input v-model="form.title" placeholder="请输入公告标题" />
      </el-form-item>
      <el-form-item label="内容" required>
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="8"
          placeholder="请输入公告内容"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">上线</el-radio>
          <el-radio :label="0">下线</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="saveAnnouncement">保存</el-button>
    </template>
  </el-dialog>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '~/stores/user'

definePageMeta({ layout: 'default' })

const userStore = useUserStore()
const router = useRouter()

// 权限验证
onMounted(() => {
  if (!userStore.isAdmin) {
    ElMessage.error('无权访问管理后台')
    router.push('/')
    return
  }
  loadAnnouncements()
})

const announcements = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  title: '',
  content: '',
  status: 1
})

// 模拟数据存储（实际应该调用后端API）
const STORAGE_KEY = 'admin_announcements'

const loadAnnouncements = () => {
  const stored = localStorage.getItem(STORAGE_KEY)
  if (stored) {
    try {
      announcements.value = JSON.parse(stored)
    } catch (e) {
      announcements.value = []
    }
  } else {
    // 初始化默认公告
    announcements.value = [
      {
        id: 1,
        title: '欢迎使用51家教网',
        content: '51家教网致力于为学员和家长提供优质的家教服务，我们拥有大量经过认证的优秀教员，覆盖小学、初中、高中各个年级和科目。',
        gmtCreate: new Date().toISOString(),
        status: 1
      },
      {
        id: 2,
        title: '平台服务升级通知',
        content: '为了给您提供更好的服务体验，我们将在本周末进行系统升级维护，届时部分功能可能暂时无法使用，预计维护时间为2小时，给您带来的不便敬请谅解。',
        gmtCreate: new Date(Date.now() - 86400000).toISOString(),
        status: 1
      },
      {
        id: 3,
        title: '教员认证流程优化',
        content: '为了提高教员认证效率，我们优化了认证流程，现在只需要上传学生证或教师资格证即可快速完成认证，审核时间缩短至24小时内。',
        gmtCreate: new Date(Date.now() - 172800000).toISOString(),
        status: 1
      }
    ]
    saveToStorage()
  }
}

const saveToStorage = () => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(announcements.value))
  // 同步到前端公告组件
  localStorage.setItem('announcements', JSON.stringify(announcements.value.filter(a => a.status === 1)))
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    id: null,
    title: '',
    content: '',
    status: 1
  }
  dialogVisible.value = true
}

const editAnnouncement = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const saveAnnouncement = () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('请填写完整信息')
    return
  }

  if (isEdit.value) {
    // 编辑
    const index = announcements.value.findIndex(a => a.id === form.value.id)
    if (index !== -1) {
      announcements.value[index] = {
        ...form.value,
        gmtModified: new Date().toISOString()
      }
    }
  } else {
    // 新增
    const newId = Math.max(...announcements.value.map(a => a.id), 0) + 1
    announcements.value.unshift({
      ...form.value,
      id: newId,
      gmtCreate: new Date().toISOString(),
      gmtModified: new Date().toISOString()
    })
  }

  saveToStorage()
  ElMessage.success(isEdit.value ? '修改成功' : '发布成功')
  dialogVisible.value = false
}

const deleteAnnouncement = (row) => {
  ElMessageBox.confirm('确定删除这条公告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    announcements.value = announcements.value.filter(a => a.id !== row.id)
    saveToStorage()
    ElMessage.success('删除成功')
  }).catch(() => {})
}

</script>

<style scoped>
.admin-announcement {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
}
</style>
