<template>
  <div class="page-container">
    <h2 class="page-title">年级管理</h2>
    <el-button type="primary" style="margin-bottom:16px" @click="showAdd">新增年级</el-button>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="gradeName" label="年级名称" />
      <el-table-column label="级别" width="100">
        <template #default="{row}">{{({1:'小学',2:'初中',3:'高中',4:'大学'})[row.gradeLevel]||'-'}}</template>
      </el-table-column>
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
    <el-dialog v-model="dialogVisible" :title="editId?'编辑':'新增'" width="400px">
      <el-form label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.gradeName" /></el-form-item>
        <el-form-item label="级别"><el-select v-model="form.gradeLevel" style="width:100%"><el-option label="小学" :value="1"/><el-option label="初中" :value="2"/><el-option label="高中" :value="3"/><el-option label="大学" :value="4"/></el-select></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, put, del } from '@/api/index'
import { ElMessage } from 'element-plus'
const list = ref<any[]>([]); const loading = ref(false); const dialogVisible = ref(false); const editId = ref<number|null>(null)
const form = ref({ gradeName:'', gradeLevel:1, sort:0 })
const load = async () => { loading.value=true; try{const r=await post('/user/admin/dict-grade/page',{pageCurrent:1,pageSize:100});if(r.code===200&&r.data)list.value=r.data.list||[]}catch(e){}finally{loading.value=false} }
const showAdd = () => { editId.value=null; form.value={gradeName:'',gradeLevel:1,sort:0}; dialogVisible.value=true }
const showEdit = (row:any) => { editId.value=row.id; form.value={gradeName:row.gradeName,gradeLevel:row.gradeLevel||1,sort:row.sort||0}; dialogVisible.value=true }
const handleSave = async () => { const r=editId.value?await put('/user/admin/dict-grade/edit',{id:editId.value,...form.value}):await post('/user/admin/dict-grade/save',form.value);if(r.code===200){ElMessage.success(r.data);dialogVisible.value=false;await load()}else ElMessage.error(r.msg) }
const handleDelete = async (id:number) => { const r=await del('/user/admin/dict-grade/delete',{id});if(r.code===200){ElMessage.success('删除成功');await load()}else ElMessage.error(r.msg) }
onMounted(()=>{load()})
</script>
