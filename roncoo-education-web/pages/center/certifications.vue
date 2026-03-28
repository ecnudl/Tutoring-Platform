<template>
<div>
  <h2 class="page-title">资质证书</h2>

  <!-- 新增证书表单 -->
  <el-card style="margin-bottom:20px">
    <h4 style="margin-bottom:12px">添加证书</h4>
    <el-form :model="certForm" label-width="80px" inline>
      <el-form-item label="类型">
        <el-select v-model="certForm.certType" placeholder="证书类型" style="width:140px">
          <el-option label="身份证" :value="1" />
          <el-option label="学生证" :value="2" />
          <el-option label="教师资格证" :value="3" />
          <el-option label="学历证" :value="4" />
          <el-option label="其他" :value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="certForm.certName" placeholder="证书名称" style="width:160px" />
      </el-form-item>
      <el-form-item label="编号">
        <el-input v-model="certForm.certNo" placeholder="证书编号(选填)" style="width:160px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="adding" @click="handleAdd">添加</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- 证书列表 -->
  <el-table :data="certs" border stripe empty-text="暂无证书记录">
    <el-table-column label="类型" width="120">
      <template #default="{ row }">{{ certTypeLabel(row.certType) }}</template>
    </el-table-column>
    <el-table-column prop="certName" label="证书名称" />
    <el-table-column prop="certNo" label="证书编号" />
    <el-table-column label="操作" width="100">
      <template #default="{ row }">
        <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
          <template #reference>
            <el-button size="small" type="danger">删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'

definePageMeta({ layout: 'center' })
const { get, post, del } = useApi()

const certs = ref([])
const certForm = ref({ certType: null, certName: '', certNo: '' })
const adding = ref(false)

const certTypeLabel = (t) => ({ 1:'身份证', 2:'学生证', 3:'教师资格证', 4:'学历证', 5:'其他' }[t] || '未知')

const loadCerts = async () => {
  try {
    const res = await get('/user/auth/tutor-profile/cert/list')
    if (res.code === 200) certs.value = res.data || []
  } catch (e) { console.error(e) }
}

const handleAdd = async () => {
  if (!certForm.value.certType) { ElMessage.warning('请选择证书类型'); return }
  if (!certForm.value.certName) { ElMessage.warning('请输入证书名称'); return }
  adding.value = true
  try {
    const res = await post('/user/auth/tutor-profile/cert/save', certForm.value)
    if (res.code === 200) {
      ElMessage.success('添加成功')
      certForm.value = { certType: null, certName: '', certNo: '' }
      await loadCerts()
    } else { ElMessage.error(res.msg || '添加失败') }
  } catch (e) { ElMessage.error('网络错误') }
  finally { adding.value = false }
}

const handleDelete = async (id) => {
  try {
    const res = await del('/user/auth/tutor-profile/cert/delete', { id })
    if (res.code === 200) {
      ElMessage.success('删除成功')
      await loadCerts()
    } else { ElMessage.error(res.msg || '删除失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { loadCerts() })
</script>
