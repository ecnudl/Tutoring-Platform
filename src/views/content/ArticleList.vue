<template>
  <div class="page-container">
    <h2 class="page-title">文章管理</h2>
    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="文章标题" clearable style="width:200px" @keyup.enter="search" />
      <el-select v-model="statusId" placeholder="状态" clearable style="width:120px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="正常" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="success" @click="handleAdd">新增文章</el-button>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column prop="articleTitle" label="标题" show-overflow-tooltip />
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <el-image v-if="row.articleCover" :src="row.articleCover" style="width:60px;height:40px" fit="cover" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="articleSummary" label="摘要" show-overflow-tooltip />
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'info'" size="small">{{ row.statusId === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除该文章？" @confirm="handleDelete(row.id)">
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

    <el-dialog v-model="formVisible" :title="formId ? '编辑文章' : '新增文章'" width="700px" top="5vh">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.articleTitle" placeholder="文章标题" />
        </el-form-item>
        <el-form-item label="封面URL">
          <el-input v-model="form.articleCover" placeholder="封面图片地址" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.articleSummary" type="textarea" :rows="2" placeholder="文章摘要" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.articleContent" type="textarea" :rows="10" placeholder="文章正文（支持HTML）" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" active-text="正常" inactive-text="禁用" />
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
const statusId = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const emptyForm = () => ({ articleTitle: '', articleCover: '', articleSummary: '', articleContent: '', sort: 0, statusId: 1 })
const form = ref(emptyForm())

const search = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/website/article/page', {
      pageCurrent: page.value, pageSize: 20, statusId: statusId.value,
      articleTitle: keyword.value || null
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleAdd = () => {
  formId.value = null
  form.value = emptyForm()
  formVisible.value = true
}

const handleEdit = async (row: any) => {
  try {
    const res = await get('/system/admin/website/article/view', { id: row.id })
    if (res.code === 200 && res.data) {
      const d = res.data
      formId.value = d.id
      form.value = {
        articleTitle: d.articleTitle || '',
        articleCover: d.articleCover || '',
        articleSummary: d.articleSummary || '',
        articleContent: d.articleContent || '',
        sort: d.sort ?? 0,
        statusId: d.statusId ?? 1
      }
      formVisible.value = true
    }
  } catch (e) { ElMessage.error('获取详情失败') }
}

const submitForm = async () => {
  if (!form.value.articleTitle) { ElMessage.warning('请输入标题'); return }
  saving.value = true
  try {
    let res
    if (formId.value) {
      res = await put('/system/admin/website/article/edit', { id: formId.value, ...form.value })
    } else {
      res = await post('/system/admin/website/article/save', form.value)
    }
    if (res.code === 200) { ElMessage.success('操作成功'); formVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  try {
    const res = await del('/system/admin/website/article/delete', { id })
    if (res.code === 200) { ElMessage.success('删除成功'); await search() }
    else ElMessage.error(res.msg || '删除失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
