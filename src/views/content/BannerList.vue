<template>
  <div class="page-container">
    <h2 class="page-title">Banner管理</h2>
    <div class="filter-bar">
      <el-select v-model="statusId" placeholder="状态" clearable style="width:120px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="正常" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="success" @click="handleAdd">新增Banner</el-button>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column prop="carouselTitle" label="标题" show-overflow-tooltip />
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image v-if="row.carouselImg" :src="row.carouselImg" style="width:80px;height:40px" fit="cover" :preview-src-list="[row.carouselImg]" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="carouselUrl" label="链接" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'info'" size="small">{{ row.statusId === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="beginTime" label="开始时间" width="170" />
      <el-table-column prop="endTime" label="结束时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除该Banner？" @confirm="handleDelete(row.id)">
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

    <el-dialog v-model="formVisible" :title="formId ? '编辑Banner' : '新增Banner'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.carouselTitle" placeholder="Banner标题" />
        </el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="form.carouselImg" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="链接地址">
          <el-input v-model="form.carouselUrl" placeholder="点击跳转URL" />
        </el-form-item>
        <el-form-item label="跳转方式">
          <el-select v-model="form.carouselTarget">
            <el-option label="当前窗口" :value="0" />
            <el-option label="新窗口" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusId" :active-value="1" :inactive-value="0" active-text="正常" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.beginTime" type="datetime" placeholder="可选" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="可选" value-format="YYYY-MM-DD HH:mm:ss" />
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

const statusId = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const formVisible = ref(false)
const formId = ref<number | null>(null)
const saving = ref(false)
const form = ref({ carouselTitle: '', carouselImg: '', carouselUrl: '', carouselTarget: 1, sort: 0, statusId: 1, beginTime: '', endTime: '' })

const search = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/website/carousel/page', {
      pageCurrent: page.value, pageSize: 20, statusId: statusId.value
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
  form.value = { carouselTitle: '', carouselImg: '', carouselUrl: '', carouselTarget: 1, sort: 0, statusId: 1, beginTime: '', endTime: '' }
  formVisible.value = true
}

const handleEdit = async (row: any) => {
  try {
    const res = await get('/system/admin/website/carousel/view', { id: row.id })
    if (res.code === 200 && res.data) {
      formId.value = res.data.id
      form.value = {
        carouselTitle: res.data.carouselTitle || '',
        carouselImg: res.data.carouselImg || '',
        carouselUrl: res.data.carouselUrl || '',
        carouselTarget: res.data.carouselTarget ?? 1,
        sort: res.data.sort ?? 0,
        statusId: res.data.statusId ?? 1,
        beginTime: res.data.beginTime || '',
        endTime: res.data.endTime || ''
      }
      formVisible.value = true
    }
  } catch (e) { ElMessage.error('获取详情失败') }
}

const submitForm = async () => {
  if (!form.value.carouselTitle) { ElMessage.warning('请输入标题'); return }
  saving.value = true
  try {
    let res
    if (formId.value) {
      res = await put('/system/admin/website/carousel/edit', { id: formId.value, ...form.value })
    } else {
      res = await post('/system/admin/website/carousel/save', form.value)
    }
    if (res.code === 200) { ElMessage.success('操作成功'); formVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const handleDelete = async (id: number) => {
  try {
    const res = await del('/system/admin/website/carousel/delete', { id })
    if (res.code === 200) { ElMessage.success('删除成功'); await search() }
    else ElMessage.error(res.msg || '删除失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
