<template>
  <div class="page-container">
    <h2 class="page-title">FAQ 帮助管理</h2>
    <div class="filter-bar">
      <el-select v-model="category" placeholder="分类" clearable style="width:180px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="家长/学员指南" value="parent" />
        <el-option label="教员指南" value="tutor" />
        <el-option label="收费标准" value="price" />
        <el-option label="账号相关" value="account" />
        <el-option label="联系我们" value="contact" />
      </el-select>
      <el-input v-model="keyword" placeholder="问题或答案关键词" clearable style="width:220px" @keyup.enter="search" />
      <el-select v-model="statusId" placeholder="状态" clearable style="width:120px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="显示" :value="1" />
        <el-option label="隐藏" :value="0" />
      </el-select>
      <el-button type="success" @click="handleAdd">新增 FAQ</el-button>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column label="分类" width="160">
        <template #default="{ row }">
          <span style="font-size:16px;margin-right:4px">{{ row.categoryIcon }}</span>{{ row.categoryLabel }}
        </template>
      </el-table-column>
      <el-table-column prop="question" label="问题" show-overflow-tooltip />
      <el-table-column prop="answer" label="答案" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'info'" size="small">{{ row.statusId === 1 ? '显示' : '隐藏' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="170">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除该 FAQ？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="formVisible" :title="formId ? '编辑 FAQ' : '新增 FAQ'" width="700px" top="5vh">
      <el-form :model="form" label-width="110px">
        <el-form-item label="分类 key">
          <el-select v-model="form.category" placeholder="请选择分类" style="width:100%" @change="onCategoryChange">
            <el-option label="家长/学员指南 (parent)" value="parent" />
            <el-option label="教员指南 (tutor)" value="tutor" />
            <el-option label="收费标准 (price)" value="price" />
            <el-option label="账号相关 (account)" value="account" />
            <el-option label="联系我们 (contact)" value="contact" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类图标">
          <el-input v-model="form.categoryIcon" placeholder="emoji，如 👨‍👩‍👧" style="width:120px" />
          <span style="margin-left:12px;color:#999;font-size:12px">同一分类可复用同一 emoji</span>
        </el-form-item>
        <el-form-item label="分类显示名">
          <el-input v-model="form.categoryLabel" placeholder="如：家长/学员指南" />
        </el-form-item>
        <el-form-item label="问题">
          <el-input v-model="form.question" placeholder="如：如何发布家教需求？" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="答案">
          <el-input v-model="form.answer" type="textarea" :rows="8" placeholder="答案正文（可使用多段）" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="9999" />
          <span style="margin-left:10px;color:#999;font-size:12px">同分类下越小越靠前</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" active-text="显示" inactive-text="隐藏" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put, del } from '@/api/index'
import { ElMessage } from 'element-plus'

const keyword = ref('')
const category = ref<string | null>(null)
const statusId = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const emptyForm = () => ({ category: 'parent', categoryIcon: '👨‍👩‍👧', categoryLabel: '家长/学员指南', question: '', answer: '', sort: 0, statusId: 1 })
const form = ref(emptyForm())

const CAT_DEFAULTS: Record<string, { icon: string; label: string }> = {
  parent: { icon: '👨‍👩‍👧', label: '家长/学员指南' },
  tutor: { icon: '👨‍🏫', label: '教员指南' },
  price: { icon: '💰', label: '收费标准' },
  account: { icon: '🔒', label: '账号相关' },
  contact: { icon: '📞', label: '联系我们' }
}

const onCategoryChange = (val: string) => {
  const d = CAT_DEFAULTS[val]
  if (d) { form.value.categoryIcon = d.icon; form.value.categoryLabel = d.label }
}

const search = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/faq/page', {
      pageCurrent: page.value, pageSize: 20,
      category: category.value, statusId: statusId.value,
      keyword: keyword.value || null
    })
    if (res.code === 200 && res.data) { list.value = res.data.list || []; total.value = res.data.totalCount || 0 }
  } catch (e) { console.error(e) } finally { loading.value = false }
}

const handleAdd = () => { formId.value = null; form.value = emptyForm(); formVisible.value = true }
const handleEdit = async (row: any) => {
  try {
    const res = await get('/system/admin/faq/view', { id: row.id })
    if (res.code === 200 && res.data) {
      const d = res.data
      formId.value = d.id
      form.value = {
        category: d.category || 'parent',
        categoryIcon: d.categoryIcon || '',
        categoryLabel: d.categoryLabel || '',
        question: d.question || '',
        answer: d.answer || '',
        sort: d.sort ?? 0,
        statusId: d.statusId ?? 1
      }
      formVisible.value = true
    }
  } catch (e) { ElMessage.error('获取详情失败') }
}

const submitForm = async () => {
  if (!form.value.question) { ElMessage.warning('请输入问题'); return }
  if (!form.value.answer) { ElMessage.warning('请输入答案'); return }
  saving.value = true
  try {
    const res = formId.value
      ? await put('/system/admin/faq/edit', { id: formId.value, ...form.value })
      : await post('/system/admin/faq/save', form.value)
    if (res.code === 200) { ElMessage.success('操作成功'); formVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') } finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  try {
    const res = await del('/system/admin/faq/delete', { id })
    if (res.code === 200) { ElMessage.success('删除成功'); await search() }
    else ElMessage.error(res.msg || '删除失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
