<template>
  <div class="page-container">
    <h2 class="page-title">公告管理</h2>
    <div class="filter-bar">
      <el-select v-model="category" placeholder="分类" clearable style="width:160px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="浮球公告" value="float" />
        <el-option label="网站公告" value="notice" />
        <el-option label="教育资讯" value="edu" />
        <el-option label="教/学员须知" value="notify" />
      </el-select>
      <el-input v-model="keyword" placeholder="标题关键词" clearable style="width:200px" @keyup.enter="search" />
      <el-select v-model="statusId" placeholder="状态" clearable style="width:120px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="上架" :value="1" />
        <el-option label="下架" :value="0" />
      </el-select>
      <el-button type="success" @click="handleAdd">新增公告</el-button>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column label="分类" width="110">
        <template #default="{ row }">{{ catLabel(row.category) }}</template>
      </el-table-column>
      <el-table-column prop="title" label="标题" show-overflow-tooltip />
      <el-table-column prop="linkUrl" label="跳转链接" show-overflow-tooltip />
      <el-table-column label="置顶" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.isTop === 1" type="warning" size="small">置顶</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'info'" size="small">{{ row.statusId === 1 ? '上架' : '下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="170">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除该公告？" @confirm="handleDelete(row.id)">
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

    <el-dialog v-model="formVisible" :title="formId ? '编辑公告' : '新增公告'" width="640px" top="5vh">
      <el-form :model="form" label-width="90px">
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width:100%">
            <el-option label="浮球公告 (float)" value="float" />
            <el-option label="网站公告 (notice)" value="notice" />
            <el-option label="教育资讯 (edu)" value="edu" />
            <el-option label="教/学员须知 (notify)" value="notify" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="公告标题" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="正文">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="公告正文（浮球公告会显示，列表公告可留空）" />
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="form.linkUrl" placeholder="如 /notice/xxx 或 https://..." />
        </el-form-item>
        <el-form-item label="置顶">
          <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="9999" />
          <span style="margin-left:10px;color:#999;font-size:12px">越小越靠前</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" active-text="上架" inactive-text="下架" />
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
const emptyForm = () => ({ category: 'notice', title: '', content: '', linkUrl: '', isTop: 0, sort: 0, statusId: 1 })
const form = ref(emptyForm())

const CAT_MAP: Record<string, string> = { float: '浮球', notice: '网站公告', edu: '教育资讯', notify: '须知' }
const catLabel = (c: string) => CAT_MAP[c] || c

const search = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/announcement/page', {
      pageCurrent: page.value, pageSize: 20,
      category: category.value, statusId: statusId.value,
      keyword: keyword.value || null
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) } finally { loading.value = false }
}

const handleAdd = () => { formId.value = null; form.value = emptyForm(); formVisible.value = true }
const handleEdit = async (row: any) => {
  try {
    const res = await get('/system/admin/announcement/view', { id: row.id })
    if (res.code === 200 && res.data) {
      const d = res.data
      formId.value = d.id
      form.value = {
        category: d.category || 'notice',
        title: d.title || '',
        content: d.content || '',
        linkUrl: d.linkUrl || '',
        isTop: d.isTop ?? 0,
        sort: d.sort ?? 0,
        statusId: d.statusId ?? 1
      }
      formVisible.value = true
    }
  } catch (e) { ElMessage.error('获取详情失败') }
}

const submitForm = async () => {
  if (!form.value.title) { ElMessage.warning('请输入标题'); return }
  if (!form.value.category) { ElMessage.warning('请选择分类'); return }
  saving.value = true
  try {
    const res = formId.value
      ? await put('/system/admin/announcement/edit', { id: formId.value, ...form.value })
      : await post('/system/admin/announcement/save', form.value)
    if (res.code === 200) { ElMessage.success('操作成功'); formVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') } finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  try {
    const res = await del('/system/admin/announcement/delete', { id })
    if (res.code === 200) { ElMessage.success('删除成功'); await search() }
    else ElMessage.error(res.msg || '删除失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
