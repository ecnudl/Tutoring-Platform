<template>
  <div class="page-container">
    <h2 class="page-title">教员列表</h2>
    <div class="filter-bar">
      <el-select v-model="auditStatus" placeholder="审核状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="草稿" :value="0" />
        <el-option label="待审核" :value="1" />
        <el-option label="已通过" :value="2" />
        <el-option label="已驳回" :value="3" />
        <el-option label="已发布" :value="4" />
      </el-select>
      <el-select v-model="statusFilter" placeholder="启用状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="已启用" :value="1" />
        <el-option label="已禁用" :value="0" />
      </el-select>
      <el-input v-model="keyword" placeholder="姓名搜索" style="width:200px" clearable @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="realName" label="姓名" width="90" fixed="left" />
      <el-table-column label="性别" width="60">
        <template #default="{ row }">{{ genderMap[row.gender] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="mobile" label="手机号" width="125" />
      <el-table-column label="邮箱" width="185" show-overflow-tooltip>
        <template #default="{ row }">{{ row.email || '-' }}</template>
      </el-table-column>
      <el-table-column label="类型" width="80">
        <template #default="{ row }">{{ tutorTypeMap[row.tutorType] || '-' }}</template>
      </el-table-column>
      <el-table-column label="工作单位" width="150" show-overflow-tooltip>
        <template #default="{ row }">{{ row.workUnit || '-' }}</template>
      </el-table-column>
      <el-table-column prop="university" label="学校" min-width="130" show-overflow-tooltip />
      <el-table-column label="籍贯" width="100" show-overflow-tooltip>
        <template #default="{ row }">{{ row.hometownProvince || '-' }}</template>
      </el-table-column>
      <el-table-column label="学历" width="70">
        <template #default="{ row }">{{ degreeMap[row.degree] || '-' }}</template>
      </el-table-column>
      <el-table-column label="出生年月" width="100">
        <template #default="{ row }">{{ fmtBirth(row.birthDate) }}</template>
      </el-table-column>
      <el-table-column label="身份证" width="170">
        <template #default="{ row }">{{ row.idCard || '-' }}</template>
      </el-table-column>
      <el-table-column label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="auditTagType(row.auditStatus)" size="small">{{ auditLabel(row.auditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'danger'" size="small">
            {{ row.statusId === 1 ? '已启用' : '已禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="明星" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isStar === 1" type="warning" size="small">★ 明星</el-tag>
          <span v-else style="color:#cbd5e1">—</span>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="380" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">详情</el-button>
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.statusId === 1" size="small" type="warning" @click="quickToggle(row, 0)">禁用</el-button>
          <el-button v-else size="small" type="success" @click="quickToggle(row, 1)">启用</el-button>
          <el-button
            v-if="row.auditStatus === 2 || row.auditStatus === 4"
            size="small"
            :type="row.isStar === 1 ? 'info' : 'warning'"
            @click="toggleStar(row)"
          >{{ row.isStar === 1 ? '取消明星' : '设为明星' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="教员详情" width="600px">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="姓名">{{ detail.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.mobile }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ tutorTypeMap[detail.tutorType] }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ degreeMap[detail.degree] }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ detail.university }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ detail.major }}</el-descriptions-item>
        <el-descriptions-item label="课时费">{{ detail.priceMin }}-{{ detail.priceMax }}元/时</el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">{{ detail.selfIntroduction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag :type="auditTagType(detail.auditStatus)">{{ auditLabel(detail.auditStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="启用状态">
          <el-tag :type="detail.statusId === 1 ? 'success' : 'danger'">
            {{ detail.statusId === 1 ? '已启用' : '已禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="排序权重">{{ detail.sort || 0 }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="editVisible" title="编辑教员" width="400px">
      <el-form label-width="80px">
        <el-form-item label="排序权重">
          <el-input-number v-model="editForm.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.statusId">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const auditStatus = ref<number | null>(null)
const statusFilter = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const detailVisible = ref(false)
const detail = ref<any>(null)
const editVisible = ref(false)
const editForm = ref({ id: 0, sort: 0, statusId: 1 })
const saving = ref(false)

const tutorTypeMap: Record<number, string> = { 1: '大学生', 2: '专职', 3: '在职教师', 4: '退休教师' }
const degreeMap: Record<number, string> = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }
const genderMap: Record<number, string> = { 1: '男', 2: '女' }
const fmtBirth = (d: string) => d ? String(d).slice(0, 7) : '-' // 取 yyyy-MM
const auditLabel = (s: number) => ({ 0: '草稿', 1: '待审核', 2: '已通过', 3: '已驳回', 4: '已发布' }[s] || '未知')
const auditTagType = (s: number) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'danger', 4: 'success' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/tutor/page', {
      pageCurrent: page.value, pageSize: 20,
      auditStatus: auditStatus.value,
      statusId: statusFilter.value,
      keyword: keyword.value || undefined
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const viewDetail = async (row: any) => {
  try {
    const res = await get('/user/admin/tutor/view', { id: row.id })
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

const handleEdit = (row: any) => {
  editForm.value = { id: row.id, sort: row.sort ?? 0, statusId: row.statusId ?? 1 }
  editVisible.value = true
}

const submitEdit = async () => {
  saving.value = true
  try {
    const res = await put('/user/admin/tutor/edit', editForm.value)
    if (res.code === 200) { ElMessage.success('保存成功'); editVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const toggleStar = async (row: any) => {
  const next = row.isStar === 1 ? 0 : 1
  const action = next === 1 ? '设为明星教员' : '取消明星教员'
  try {
    await ElMessageBox.confirm(
      `确认${action}「${row.realName}」？`,
      `${action}确认`,
      { confirmButtonText: action, cancelButtonText: '取消', type: 'warning' }
    )
  } catch { return }
  try {
    const res = await put('/user/admin/tutor-audit/star', { id: row.id, isStar: next })
    if (res.code === 200) {
      ElMessage.success(res.msg || `${action}成功`)
      row.isStar = next
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}

const quickToggle = async (row: any, target: 0 | 1) => {
  const action = target === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确认${action}教员「${row.realName}」？` + (target === 0 ? '禁用后将不在前台教员列表中展示。' : ''), `${action}确认`, {
      confirmButtonText: action,
      cancelButtonText: '取消',
      type: target === 1 ? 'success' : 'warning'
    })
  } catch { return }
  try {
    const res = await put('/user/admin/tutor/edit', { id: row.id, sort: row.sort ?? 0, statusId: target })
    if (res.code === 200) { ElMessage.success(`${action}成功`); await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
