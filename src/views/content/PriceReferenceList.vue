<template>
  <div class="page-container">
    <h2 class="page-title">价格参考管理</h2>
    <div class="filter-bar">
      <el-select v-model="cityId" placeholder="选择城市" @change="load" style="width: 200px">
        <el-option v-for="c in cities" :key="c.id" :label="c.cityName" :value="c.id" />
      </el-select>
      <el-button type="primary" @click="openForm()" :disabled="!cityId">新增</el-button>
    </div>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column label="教员类型" width="120">
        <template #default="{ row }">{{ tutorTypeMap[row.tutorType] || row.tutorType }}</template>
      </el-table-column>
      <el-table-column label="学段" width="100">
        <template #default="{ row }">{{ gradeLevelMap[row.gradeLevel] || row.gradeLevel }}</template>
      </el-table-column>
      <el-table-column label="参考价格" width="200">
        <template #default="{ row }">{{ row.priceMin }} - {{ row.priceMax }} 元/小时</template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" />
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

    <el-dialog :title="formId ? '编辑' : '新增'" v-model="formVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="教员类型">
          <el-select v-model="form.tutorType">
            <el-option :value="1" label="大学生" />
            <el-option :value="2" label="专职教员" />
            <el-option :value="3" label="在职教师" />
            <el-option :value="4" label="海归外教" />
          </el-select>
        </el-form-item>
        <el-form-item label="学段">
          <el-select v-model="form.gradeLevel">
            <el-option :value="1" label="小学" />
            <el-option :value="2" label="初中" />
            <el-option :value="3" label="高中" />
          </el-select>
        </el-form-item>
        <el-form-item label="最低价"><el-input-number v-model="form.priceMin" :min="0" /></el-form-item>
        <el-form-item label="最高价"><el-input-number v-model="form.priceMax" :min="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
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
import { post, put, del } from '@/api'
import { ElMessage } from 'element-plus'

const tutorTypeMap: Record<number, string> = { 1: '大学生', 2: '专职教员', 3: '在职教师', 4: '海归外教' }
const gradeLevelMap: Record<number, string> = { 1: '小学', 2: '初中', 3: '高中' }

const cities = ref<any[]>([])
const cityId = ref<number | null>(null)
const list = ref<any[]>([])
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const form = ref({ tutorType: 1, gradeLevel: 1, priceMin: 0, priceMax: 0, remark: '', sort: 0 })

const loadCities = async () => {
  const res = await post('/system/admin/city/page', { pageCurrent: 1, pageSize: 100 })
  cities.value = res.data?.list || res.data || []
  if (cities.value.length > 0) { cityId.value = cities.value[0].id; load() }
}

const load = async () => {
  if (!cityId.value) return
  loading.value = true
  try {
    const res = await post('/system/admin/price-reference/page', { cityId: cityId.value, pageCurrent: 1, pageSize: 100 })
    list.value = res.data?.list || res.data || []
  } finally { loading.value = false }
}

const openForm = (row?: any) => {
  if (row) {
    formId.value = row.id
    form.value = { tutorType: row.tutorType, gradeLevel: row.gradeLevel, priceMin: row.priceMin, priceMax: row.priceMax, remark: row.remark || '', sort: row.sort }
  } else {
    formId.value = null
    form.value = { tutorType: 1, gradeLevel: 1, priceMin: 0, priceMax: 0, remark: '', sort: 0 }
  }
  formVisible.value = true
}

const handleSave = async () => {
  saving.value = true
  try {
    if (formId.value) {
      await put('/system/admin/price-reference/edit', { id: formId.value, cityId: cityId.value, ...form.value })
    } else {
      await post('/system/admin/price-reference/save', { cityId: cityId.value, ...form.value })
    }
    ElMessage.success('保存成功')
    formVisible.value = false
    load()
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  await del('/system/admin/price-reference/delete', { id })
  ElMessage.success('删除成功')
  load()
}

onMounted(loadCities)
</script>
