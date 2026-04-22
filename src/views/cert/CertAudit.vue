<template>
  <div class="page-container">
    <h2 class="page-title">证件审核</h2>
    <div class="filter-bar">
      <el-select v-model="auditStatus" placeholder="审核状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
      <el-select v-model="certType" placeholder="证件类型" clearable style="width:160px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="身份证正面" :value="1" />
        <el-option label="身份证反面" :value="2" />
        <el-option label="学生证/毕业证" :value="3" />
        <el-option label="教师资格证" :value="4" />
        <el-option label="其他身份证件" :value="5" />
      </el-select>
      <el-button type="primary" @click="search">刷新</el-button>
    </div>

    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="tutorName" label="教员姓名" width="110" />
      <el-table-column prop="tutorMobile" label="手机号" width="130" />
      <el-table-column label="证件类型" width="130">
        <template #default="{ row }">{{ certTypeLabel(row.certType) }}</template>
      </el-table-column>
      <el-table-column label="证件照片" width="100">
        <template #default="{ row }">
          <el-image v-if="row.certUrl" :src="row.certUrl" style="width:60px;height:40px" fit="cover" :preview-src-list="[row.certUrl]" />
          <span v-else style="color:#999">未上传</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="auditTagType(row.auditStatus)" size="small">{{ auditLabel(row.auditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditRemark" label="审核备注" show-overflow-tooltip />
      <el-table-column prop="gmtCreate" label="提交时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="success" :disabled="row.auditStatus === 1" @click="approve(row)">通过</el-button>
          <el-button size="small" type="danger" :disabled="row.auditStatus === 2" @click="reject(row)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, put } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const auditStatus = ref<number | null>(0)   // 默认只显示待审核
const certType = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const certTypeLabel = (t: number) => ({ 1:'身份证正面', 2:'身份证反面', 3:'学生证/毕业证', 4:'教师资格证', 5:'其他身份证件' }[t] || '未知')
const auditLabel = (s: number) => ({ 0:'待审核', 1:'已通过', 2:'已驳回' }[s] || '未知')
const auditTagType = (s: number) => ({ 0:'warning', 1:'success', 2:'danger' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/cert/page', {
      pageCurrent: page.value, pageSize: 20,
      auditStatus: auditStatus.value, certType: certType.value
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const approve = async (row: any) => {
  try {
    const res = await put('/user/admin/cert/approve', { id: row.id, auditRemark: '' })
    if (res.code === 200) {
      ElMessage.success(res.msg || '已通过')
      await search()
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

const reject = async (row: any) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回证件', {
      confirmButtonText: '确认驳回', cancelButtonText: '取消',
      inputPattern: /.+/, inputErrorMessage: '驳回原因不能为空'
    })
    const res = await put('/user/admin/cert/reject', { id: row.id, auditRemark: value })
    if (res.code === 200) {
      ElMessage.success(res.msg || '已驳回')
      await search()
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

onMounted(() => { search() })
</script>

<style scoped>
.filter-bar { display: flex; gap: 12px; margin-bottom: 16px; }
</style>
