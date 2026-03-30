<template>
  <div class="page-container">
    <h2 class="page-title">科目管理</h2>
    <el-button type="primary" style="margin-bottom:16px" @click="showAdd">新增科目</el-button>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="subjectName" label="科目名称" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="showEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑科目' : '新增科目'" width="400px">
      <el-form label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.subjectName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, put, del } from '@/api/index'
import { ElMessage } from 'element-plus'
const list = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editId = ref<number|null>(null)
const form = ref({ subjectName: '', sort: 0 })
const load = async () => { loading.value=true; try { const r=await post('/user/admin/dict-subject/page',{pageCurrent:1,pageSize:100}); if(r.code===200&&r.data) list.value=r.data.list||[] } catch(e){} finally{loading.value=false} }
const showAdd = () => { editId.value=null; form.value={subjectName:'',sort:0}; dialogVisible.value=true }
const showEdit = (row:any) => { editId.value=row.id; form.value={subjectName:row.subjectName,sort:row.sort||0}; dialogVisible.value=true }
const handleSave = async () => {
  const r = editId.value ? await put('/user/admin/dict-subject/edit',{id:editId.value,...form.value}) : await post('/user/admin/dict-subject/save',form.value)
  if(r.code===200){ElMessage.success(r.data);dialogVisible.value=false;await load()}else ElMessage.error(r.msg)
}
const handleDelete = async (id:number) => { const r=await del('/user/admin/dict-subject/delete',{id}); if(r.code===200){ElMessage.success('删除成功');await load()}else ElMessage.error(r.msg) }
onMounted(()=>{load()})
</script>
