<template>
<div>
  <h2 class="page-title">{{ isEdit ? '编辑需求' : '发布需求' }}</h2>
  <el-form :model="form" label-width="100px" style="max-width:600px">
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
    <el-form-item label="详细要求">
      <el-input v-model="form.requirementDetail" type="textarea" :rows="4" placeholder="描述您的辅导需求" />
    </el-form-item>
    <el-form-item label="联系人">
      <el-input v-model="form.contactName" placeholder="联系人姓名" />
    </el-form-item>
    <el-form-item label="联系电话">
      <el-input v-model="form.contactMobile" placeholder="联系电话" />
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

definePageMeta({ layout: 'center' })
const dictStore = useDictStore()
const { get, post } = useApi()
const route = useRoute()
const router = useRouter()

const editId = computed(() => route.query.id ? Number(route.query.id) : null)
const isEdit = computed(() => !!editId.value)

const form = ref({ title: '', budgetMin: 0, budgetMax: 0, schedule: '', requirementDetail: '', contactName: '', contactMobile: '' })
const subjectList = ref([])
const saving = ref(false)

const handleSave = async () => {
  if (!form.value.title) { ElMessage.warning('请输入需求标题'); return }
  if (!form.value.contactName) { ElMessage.warning('请输入联系人'); return }
  if (!form.value.contactMobile) { ElMessage.warning('请输入联系电话'); return }
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
      form.value = { title: d.title||'', budgetMin: d.budgetMin||0, budgetMax: d.budgetMax||0, schedule: d.schedule||'', requirementDetail: d.requirementDetail||'', contactName: d.contactName||'', contactMobile: d.contactMobile||'' }
      try { subjectList.value = JSON.parse(d.subjectIds||'[]').map(String) } catch { subjectList.value = [] }
    }
  } catch (e) { console.error(e) }
}

onMounted(async () => {
  await dictStore.fetchAll()
  await loadExisting()
})
</script>
