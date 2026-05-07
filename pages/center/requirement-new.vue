<template>
<div>
  <h2 class="page-title">{{ isEdit ? '编辑需求' : '发布需求' }}</h2>
  <el-form :model="form" label-width="100px" class="req-new-form">
    <el-form-item label="需求标题">
      <el-input v-model="form.title" placeholder="例如：找高一数学老师" />
    </el-form-item>
    <el-form-item label="辅导科目">
      <el-select v-model="subjectList" multiple style="width:100%" placeholder="选择科目">
        <el-option v-for="s in dictStore.subjects" :key="s.id" :label="s.subjectName" :value="String(s.id)" />
      </el-select>
    </el-form-item>
    <el-form-item label="预算(元/时)">
      <div style="display:flex;gap:8px;align-items:center">
        <el-input-number v-model="form.budgetMin" :min="0" />
        <span>-</span>
        <el-input-number v-model="form.budgetMax" :min="0" />
      </div>
    </el-form-item>
    <el-form-item label="时间安排">
      <el-input v-model="form.schedule" placeholder="例如：每周六下午2-5点" />
    </el-form-item>
    <el-form-item label="联系人">
      <el-input v-model="form.contactName" placeholder="联系人姓名" />
    </el-form-item>
    <el-form-item label="联系电话">
      <el-input v-model="form.contactMobile" placeholder="联系电话" />
    </el-form-item>
    <el-form-item label="学生情况">
      <el-input v-model="form.studentInfo" type="textarea" :rows="4"
        placeholder="请填写学生的程度、目前学习情况，需要辅导的科目和年级，以及希望的上课频率和时间。例如：高一学生，物理薄弱常 50 分上下，希望补到 75 分以上，每周六下午 2 小时。" />
    </el-form-item>
    <el-form-item label="教员要求">
      <el-input v-model="form.tutorRequest" type="textarea" :rows="3"
        placeholder="请填写您对教员的具体要求。例如：教员性别（女老师优先 / 不限）、教员居住地区或便于上课的区域、是否有教学经验要求、学历背景、其他偏好等。" />
    </el-form-item>
    <el-form-item label="交通信息">
      <el-input v-model="form.trafficInfo" type="textarea" :rows="3"
        placeholder="如果是线上授课请填「线上授课，无需上门」；如果需要老师上门，请写明具体小区/弄号或最近地铁站，以及大致路口。" />
    </el-form-item>
    <el-form-item label="补充说明">
      <el-input v-model="form.requirementDetail" type="textarea" :rows="3" placeholder="（可选）其他想告诉教员的信息" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="saving" @click="handleSave">保存草稿</el-button>
      <el-button @click="$router.push('/center/requirements')">返回列表</el-button>
    </el-form-item>
  </el-form>
</div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref, computed, onMounted } from 'vue'
import { useDictStore } from '~/stores/dict'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
const dictStore = useDictStore()
const { get, post } = useApi()
const route = useRoute()
const router = useRouter()

const editId = computed(() => route.query.id ? Number(route.query.id) : null)
const isEdit = computed(() => !!editId.value)

const form = ref({ title: '', budgetMin: 0, budgetMax: 0, schedule: '', requirementDetail: '', contactName: '', contactMobile: '', studentInfo: '', tutorRequest: '', trafficInfo: '' })
const subjectList = ref([])
const saving = ref(false)

const handleSave = async () => {
  if (!form.value.title) { ElMessage.warning('请输入需求标题'); return }
  if (!form.value.contactName) { ElMessage.warning('请输入联系人'); return }
  if (!form.value.contactMobile) { ElMessage.warning('请输入联系电话'); return }
  if (!form.value.studentInfo) { ElMessage.warning('请填写学生情况'); return }
  if (!form.value.tutorRequest) { ElMessage.warning('请填写教员要求'); return }
  if (!form.value.trafficInfo) { ElMessage.warning('请填写交通信息'); return }
  saving.value = true
  try {
    const data = {
      ...form.value,
      subjectIds: JSON.stringify(subjectList.value.map(Number)),
      id: editId.value || undefined
    }
    const res = await post('/user/auth/requirement/save', data)
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '保存成功' : '创建成功')
      router.push('/center/requirements')
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const loadExisting = async () => {
  if (!editId.value) return
  try {
    const res = await get('/user/auth/requirement/view', { id: editId.value })
    if (res.code === 200 && res.data) {
      const d = res.data
      form.value = { title: d.title||'', budgetMin: d.budgetMin||0, budgetMax: d.budgetMax||0, schedule: d.schedule||'', requirementDetail: d.requirementDetail||'', contactName: d.contactName||'', contactMobile: d.contactMobile||'', studentInfo: d.studentInfo||'', tutorRequest: d.tutorRequest||'', trafficInfo: d.trafficInfo||'' }
      try { subjectList.value = JSON.parse(d.subjectIds||'[]').map(String) } catch { subjectList.value = [] }
    }
  } catch (e) { console.error(e) }
}

onMounted(async () => {
  await dictStore.fetchAll()
  await loadExisting()
})
</script>
<style scoped>
.req-new-form { max-width: 600px; }
@media (max-width: 768px) {
  .req-new-form { max-width: 100%; }
  .req-new-form :deep(.el-form-item__label) { width: 80px !important; font-size: 13px; }
  .req-new-form :deep(.el-form-item__content) { margin-left: 80px !important; }
  .req-new-form :deep(.el-input-number) { width: 100% !important; }
}
</style>
