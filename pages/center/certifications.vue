<template>
<div>
  <h2 class="page-title">资质证书</h2>

  <!-- 新增证书表单 -->
  <el-card style="margin-bottom:20px">
    <h4 style="margin-bottom:12px">添加证书</h4>
    <el-form :model="certForm" label-width="80px">
      <el-form-item label="类型">
        <el-select v-model="certForm.certType" placeholder="证书类型" style="width:200px">
          <el-option label="身份证" :value="1" />
          <el-option label="学生证" :value="2" />
          <el-option label="教师资格证" :value="3" />
          <el-option label="学历证" :value="4" />
          <el-option label="其他" :value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="certForm.certName" placeholder="证书名称" style="width:200px" />
      </el-form-item>
      <el-form-item label="编号">
        <el-input v-model="certForm.certNo" placeholder="证书编号(选填)" style="width:200px" />
      </el-form-item>
      <el-form-item label="证书照片">
        <div>
          <div v-if="certForm.certUrl" style="margin-bottom:8px">
            <el-image :src="certForm.certUrl" style="width:200px;height:140px;border-radius:4px" fit="cover" :preview-src-list="[certForm.certUrl]" />
            <el-button size="small" type="danger" style="margin-left:8px" @click="certForm.certUrl = ''">移除</el-button>
          </div>
          <div v-else>
            <input type="file" ref="certFileInput" accept="image/*" style="display:none" @change="handleCertUpload" />
            <el-button size="small" :loading="uploadingCert" @click="$refs.certFileInput.click()">上传证书照片</el-button>
            <span style="font-size:12px;color:#999;margin-left:8px">支持 jpg/png，最大5MB</span>
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="adding" @click="handleAdd">添加证书</el-button>
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
    <el-table-column label="证书照片" width="120">
      <template #default="{ row }">
        <el-image v-if="row.certUrl" :src="row.certUrl" style="width:60px;height:40px" fit="cover" :preview-src-list="[row.certUrl]" />
        <span v-else style="color:#999">未上传</span>
      </template>
    </el-table-column>
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
const config = useRuntimeConfig()

const certs = ref([])
const certForm = ref({ certType: null, certName: '', certNo: '', certUrl: '' })
const adding = ref(false)
const uploadingCert = ref(false)

const certTypeLabel = (t) => ({ 1:'身份证', 2:'学生证', 3:'教师资格证', 4:'学历证', 5:'其他' }[t] || '未知')

const handleCertUpload = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) { ElMessage.warning('图片不能超过5MB'); return }
  uploadingCert.value = true
  try {
    const fd = new FormData()
    fd.append('picFile', file)
    const token = localStorage.getItem('token')
    const res = await $fetch(`${config.public.apiBase}/system/auth/upload/pic`, {
      method: 'POST', body: fd, headers: { token }
    })
    if (res.code === 200 && res.data) {
      certForm.value.certUrl = res.data
      ElMessage.success('照片上传成功')
    } else { ElMessage.error(res.msg || '上传失败') }
  } catch (err) { ElMessage.error('上传失败，请重试') }
  finally { uploadingCert.value = false; e.target.value = '' }
}

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
      certForm.value = { certType: null, certName: '', certNo: '', certUrl: '' }
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
<style scoped>
@media (max-width: 768px) {
  .el-table { font-size: 13px; }
}
</style>
