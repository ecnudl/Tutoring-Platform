<template>
  <div class="page-container">
    <h2 class="page-title">科目分类管理</h2>
    <div class="filter-bar">
      <el-button type="primary" @click="openForm()">新增分类</el-button>
    </div>
    <el-table :data="list" v-loading="loading" border stripe row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column prop="id" label="ID" width="100" />
      <el-table-column prop="categoryName" label="分类名称" width="200" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'danger'" size="small">{{ row.statusId === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openForm(undefined, row.id)" v-if="!row.parentId">新增子分类</el-button>
          <el-button size="small" @click="openForm(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="formId ? '编辑分类' : '新增分类'" v-model="formVisible" width="500px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="父级">
          <el-select v-model="form.parentId" placeholder="顶级分类" clearable>
            <el-option :value="0" label="顶级分类" />
            <el-option v-for="c in topCategories" :key="c.id" :label="c.categoryName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类名称"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { post, put, del } from '@/api'
import { ElMessage } from 'element-plus'

const flatList = ref<any[]>([])
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const form = ref({ parentId: 0, categoryName: '', statusId: 1, sort: 0 })

const topCategories = computed(() => flatList.value.filter(c => !c.parentId || c.parentId === 0))
const list = computed(() => {
  const tops = flatList.value.filter(c => !c.parentId || c.parentId === 0)
  return tops.map(t => ({
    ...t,
    children: flatList.value.filter(c => c.parentId === t.id)
  }))
})

const load = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/subject-category/page', { pageCurrent: 1, pageSize: 500 })
    flatList.value = res.data?.list || res.data || []
  } finally { loading.value = false }
}

const openForm = (row?: any, parentId?: number) => {
  if (row) {
    formId.value = row.id
    form.value = { parentId: row.parentId || 0, categoryName: row.categoryName, statusId: row.statusId, sort: row.sort }
  } else {
    formId.value = null
    form.value = { parentId: parentId || 0, categoryName: '', statusId: 1, sort: 0 }
  }
  formVisible.value = true
}

const handleSave = async () => {
  saving.value = true
  try {
    if (formId.value) {
      await put('/system/admin/subject-category/edit', { id: formId.value, ...form.value })
    } else {
      await post('/system/admin/subject-category/save', form.value)
    }
    ElMessage.success('保存成功')
    formVisible.value = false
    load()
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  await del('/system/admin/subject-category/delete', { id })
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
