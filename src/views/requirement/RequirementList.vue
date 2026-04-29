<template>
  <div class="page-container">
    <h2 class="page-title">需求列表</h2>
    <div class="filter-bar">
      <el-input v-model="kw" placeholder="标题 / 关键字" clearable style="width:200px" @keyup.enter="search" />
      <el-input v-model="displayNoKw" placeholder="订单编号" clearable style="width:160px" @keyup.enter="search" />
      <el-select v-model="reqStatus" placeholder="状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="草稿" :value="0" />
        <el-option label="审核中" :value="1" />
        <el-option label="已发布" :value="2" />
        <el-option label="已匹配" :value="3" />
        <el-option label="已关闭" :value="5" />
        <el-option label="已驳回" :value="6" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button type="success" @click="openCreate">新建需求</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="displayNo" label="编号" width="110" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column prop="contactMobile" label="电话" width="130" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.reqStatus)" size="small">{{ statusLabel(row.reqStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column prop="applicationCount" label="申请" width="70" />
      <el-table-column prop="gmtCreate" label="创建时间" width="160" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="openMatch(row)" v-if="row.reqStatus !== 3 && row.reqStatus !== 5">接单确认</el-button>
          <el-popconfirm title="关闭后不再展示, 确认?" @confirm="closeOne(row)">
            <template #reference>
              <el-button size="small" type="danger" v-if="row.reqStatus !== 5">关闭</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <!-- 编辑 / 新增 弹窗 -->
    <el-dialog v-model="editVisible" :title="formId ? '编辑需求' : '新建需求'" width="780px">
      <el-form :model="form" label-width="100px" label-position="right">
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="标题"><el-input v-model="form.title" maxlength="100" placeholder="如: 浦东新区·一年级·全科" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="联系人"><el-input v-model="form.contactName" maxlength="50" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="电话"><el-input v-model="form.contactMobile" maxlength="20" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="微信"><el-input v-model="form.contactWechat" maxlength="50" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="学员性别">
              <el-radio-group v-model="form.studentGender">
                <el-radio :value="0">未知</el-radio>
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年级 ID"><el-input-number v-model="form.gradeId" :min="0" controls-position="right" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="求教性质">
              <el-select v-model="form.requirementType" placeholder="选择" clearable style="width:100%">
                <el-option label="提高型" :value="1" />
                <el-option label="同步辅导" :value="2" />
                <el-option label="竞赛" :value="3" />
                <el-option label="考前冲刺" :value="4" />
                <el-option label="陪学" :value="5" />
                <el-option label="其他" :value="6" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="城市 ID"><el-input-number v-model="form.cityId" :min="0" controls-position="right" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区县 ID"><el-input-number v-model="form.districtId" :min="0" controls-position="right" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="教学方式">
              <el-select v-model="form.teachingMethod" placeholder="选择" style="width:100%">
                <el-option label="教师上门" :value="1" />
                <el-option label="学员上门" :value="2" />
                <el-option label="在线辅导" :value="3" />
                <el-option label="均可" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="大致位置"><el-input v-model="form.address" maxlength="200" placeholder="街道+小区, 不要门牌号" /></el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="交通线路"><el-input v-model="form.transport" maxlength="200" placeholder="如: 公交方便 / 地铁2号线步行5分钟" /></el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="科目">
              <el-input v-model="form.subjectIds" placeholder="科目 (多个用逗号分隔, 如: 数学,英语 或全科)" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="学员情况">
              <el-input v-model="form.requirementDetail" type="textarea" :rows="3" maxlength="1000" placeholder="基础如何, 薄弱在哪, 已学到哪等" />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="频次"><el-input v-model="form.frequency" maxlength="50" placeholder="如: 5次/周" /></el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="辅导时间"><el-input v-model="form.schedule" maxlength="200" placeholder="如: 周一至周五晚上 5-7 点, 每次两小时" /></el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="性别要求">
              <el-radio-group v-model="form.tutorGender">
                <el-radio :value="0">不限</el-radio>
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算 ¥/h"><el-input-number v-model="form.budgetMin" :min="0" :step="10" controls-position="right" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="~"><el-input-number v-model="form.budgetMax" :min="0" :step="10" controls-position="right" style="width:100%" /></el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="其它要求"><el-input v-model="form.otherRequirements" maxlength="500" placeholder="对教员的额外要求 (如: 985大学生, 经验丰富)" /></el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="车贴"><el-input v-model="form.transportSubsidy" maxlength="100" placeholder="无 / 有, 单次 30 元" /></el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.reqStatus" style="width:100%">
                <el-option label="已发布(对外可见)" :value="2" />
                <el-option label="审核中" :value="1" />
                <el-option label="草稿" :value="0" />
                <el-option label="已关闭" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">{{ formId ? '保存' : '创建' }}</el-button>
      </template>
    </el-dialog>

    <!-- 接单确认 弹窗 -->
    <el-dialog v-model="matchVisible" title="确认接单" width="480px">
      <el-form :model="matchForm" label-width="120px">
        <el-form-item label="需求编号">
          <span>{{ matchTarget?.displayNo }} · {{ matchTarget?.title }}</span>
        </el-form-item>
        <el-form-item label="教员 user_id">
          <el-input-number v-model="matchForm.tutorUserId" :min="0" controls-position="right" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="matchForm.remark" type="textarea" :rows="3" maxlength="500" placeholder="可填: 哪位教员接的, 双方同意时间等" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="matchVisible = false">取消</el-button>
        <el-button type="warning" @click="submitMatch" :loading="submitting">确认接单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { post, get, put, del } from '@/api/index'

const reqStatus = ref<number | null>(null)
const kw = ref('')
const displayNoKw = ref('')
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const submitting = ref(false)

const editVisible = ref(false)
const formId = ref<number | null>(null)
const form = ref<any>(emptyForm())

const matchVisible = ref(false)
const matchTarget = ref<any>(null)
const matchForm = ref<any>({ tutorUserId: 0, remark: '' })

function emptyForm() {
  return {
    title: '', contactName: '', contactMobile: '', contactWechat: '',
    studentGender: 0, gradeId: null, requirementType: null,
    cityId: null, districtId: null, address: '', transport: '',
    subjectIds: '', requirementDetail: '',
    frequency: '', schedule: '',
    tutorGender: 0, budgetMin: 0, budgetMax: 0,
    otherRequirements: '', teachingMethod: 1, transportSubsidy: '',
    reqStatus: 2
  }
}

const statusLabel = (s: number) => ({ 0: '草稿', 1: '审核中', 2: '已发布', 3: '已匹配', 4: '已完成', 5: '已关闭', 6: '已驳回' }[s] || '未知')
const statusType = (s: number) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'success', 5: 'info', 6: 'danger' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/requirement/page', {
      pageCurrent: page.value, pageSize: 20,
      reqStatus: reqStatus.value, keyword: kw.value, displayNo: displayNoKw.value
    })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const openCreate = () => {
  formId.value = null
  form.value = emptyForm()
  editVisible.value = true
}

const openEdit = async (row: any) => {
  try {
    const res = await get('/user/admin/requirement/view', { id: row.id })
    if (res.code === 200 && res.data) {
      const d = res.data
      formId.value = d.id
      form.value = {
        title: d.title || '',
        contactName: d.contactName || '', contactMobile: d.contactMobile || '', contactWechat: d.contactWechat || '',
        studentGender: d.studentGender ?? 0,
        gradeId: d.gradeId, requirementType: d.requirementType,
        cityId: d.cityId, districtId: d.districtId, address: d.address || '', transport: d.transport || '',
        subjectIds: d.subjectIds || '', requirementDetail: d.requirementDetail || '',
        frequency: d.frequency || '', schedule: d.schedule || '',
        tutorGender: d.tutorGender ?? 0, budgetMin: d.budgetMin ?? 0, budgetMax: d.budgetMax ?? 0,
        otherRequirements: d.otherRequirements || '', teachingMethod: d.teachingMethod ?? 1, transportSubsidy: d.transportSubsidy || '',
        reqStatus: d.reqStatus ?? 2
      }
      editVisible.value = true
    }
  } catch (e) { console.error(e) }
}

const submitForm = async () => {
  if (!form.value.title) { ElMessage.warning('请填写标题'); return }
  submitting.value = true
  try {
    const payload: any = { ...form.value }
    if (formId.value) {
      payload.id = formId.value
      const res = await put('/user/admin/requirement/edit', payload)
      if (res.code === 200) {
        ElMessage.success('已保存')
        editVisible.value = false
        search()
      } else { ElMessage.error(res.msg || '保存失败') }
    } else {
      const res = await post('/user/admin/requirement/save', payload)
      if (res.code === 200) {
        ElMessage.success(`已创建 (${res.data?.displayNo || ''})`)
        editVisible.value = false
        search()
      } else { ElMessage.error(res.msg || '创建失败') }
    }
  } catch (e) { console.error(e); ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const closeOne = async (row: any) => {
  try {
    const res = await del('/user/admin/requirement/delete', { id: row.id })
    if (res.code === 200) {
      ElMessage.success('已关闭')
      search()
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { console.error(e) }
}

const openMatch = (row: any) => {
  matchTarget.value = row
  matchForm.value = { tutorUserId: 0, remark: '' }
  matchVisible.value = true
}

const submitMatch = async () => {
  if (!matchForm.value.tutorUserId) { ElMessage.warning('请填写教员 user_id'); return }
  submitting.value = true
  try {
    const res = await put('/user/admin/requirement/confirm-match', {
      requirementId: matchTarget.value.id,
      tutorUserId: matchForm.value.tutorUserId,
      remark: matchForm.value.remark
    })
    if (res.code === 200) {
      ElMessage.success('已确认接单')
      matchVisible.value = false
      search()
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { console.error(e); ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

onMounted(() => { search() })
</script>

<style scoped>
.filter-bar { display: flex; gap: 12px; align-items: center; margin-bottom: 16px; flex-wrap: wrap; }
</style>
