<template>
  <div class="page-container">
    <h2 class="page-title">城市管理</h2>
    <div class="filter-bar">
      <el-button type="primary" @click="openForm()">新增城市</el-button>
    </div>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="cityName" label="城市名称" width="120" />
      <el-table-column prop="cityPinyin" label="拼音" width="120" />
      <el-table-column prop="provinceName" label="省份" width="120" />
      <el-table-column label="热门" width="80">
        <template #default="{ row }">
          <el-tag :type="row.isHot === 1 ? 'danger' : 'info'" size="small">{{ row.isHot === 1 ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'danger'" size="small">{{ row.statusId === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openForm(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="formId ? '编辑城市' : '新增城市'" v-model="formVisible" width="500px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="城市名称"><el-input v-model="form.cityName" /></el-form-item>
        <el-form-item label="拼音"><el-input v-model="form.cityPinyin" /></el-form-item>
        <el-form-item label="省份"><el-input v-model="form.provinceName" /></el-form-item>
        <el-form-item label="热门">
          <el-switch v-model="form.isHot" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" />
        </el-form-item>
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
import { ref, onMounted } from 'vue'
import { get, post, put, del } from '@/api'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const form = ref({ cityName: '', cityPinyin: '', provinceName: '', isHot: 0, statusId: 1, sort: 0 })

const load = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/city/page', { pageCurrent: 1, pageSize: 100 })
    list.value = res.data?.list || res.data || []
  } finally { loading.value = false }
}

const openForm = (row?: any) => {
  if (row) {
    formId.value = row.id
    form.value = { cityName: row.cityName, cityPinyin: row.cityPinyin, provinceName: row.provinceName, isHot: row.isHot, statusId: row.statusId, sort: row.sort }
  } else {
    formId.value = null
    form.value = { cityName: '', cityPinyin: '', provinceName: '', isHot: 0, statusId: 1, sort: 0 }
  }
  formVisible.value = true
}

const handleSave = async () => {
  saving.value = true
  try {
    if (formId.value) {
      await put('/system/admin/city/edit', { id: formId.value, ...form.value })
    } else {
      await post('/system/admin/city/save', form.value)
    }
    ElMessage.success('保存成功')
    formVisible.value = false
    load()
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  await del('/system/admin/city/delete', { id })
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
