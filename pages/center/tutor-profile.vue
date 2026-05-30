<template>
<div>
  <h2 class="page-title">教员资料</h2>

  <!-- 审核状态提示 -->
  <el-alert v-if="profile.auditStatus === 0" title="资料为草稿状态, 填写完成后请提交审核" type="info" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 1" title="资料审核中, 暂不可修改" type="warning" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 2 || profile.auditStatus === 4" title="审核已通过, 您的资料已展示在教员库中。如需修改, 请直接编辑保存; 修改后会回到待审核, 公开页暂时下架, 直到 admin 重新审核通过。" type="success" :closable="false" style="margin-bottom:16px" />
  <el-alert v-if="profile.auditStatus === 3" :title="'审核被驳回: ' + (profile.auditRemark || '请修改后重新提交')" type="error" :closable="false" style="margin-bottom:16px" />

  <el-form :model="form" label-width="120px" class="tutor-form" v-loading="pageLoading">
    <!-- ========== 头像 + 基本信息 ========== -->
    <el-divider content-position="left">基本信息</el-divider>
    <el-form-item label="头像">
      <div style="display:flex;align-items:center;gap:16px">
        <el-avatar :size="80" :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
        <div v-if="!isLocked">
          <input type="file" ref="avatarInput" accept="image/jpeg,image/png,image/webp" style="display:none" @change="handleAvatarUpload" />
          <el-button size="small" :loading="uploadingAvatar" @click="$refs.avatarInput.click()">上传头像</el-button>
          <p style="font-size:12px;color:#999;margin-top:4px">支持 jpg/png/webp, 5MB 内, 建议正面清晰头像</p>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="真实姓名" required>
      <el-input v-model="form.realName" placeholder="前台只显示您的姓 + 教员, 如'王海'显示为'王教员'" :disabled="isLocked" maxlength="20" />
    </el-form-item>
    <el-form-item label="性别" required>
      <el-radio-group v-model="form.gender" :disabled="isLocked">
        <el-radio :label="1">男</el-radio>
        <el-radio :label="2">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="出生年月">
      <el-date-picker v-model="form.birthDate" type="month" value-format="YYYY-MM-DD" placeholder="选择年月" :disabled="isLocked" style="width:100%" />
    </el-form-item>
    <el-form-item label="籍贯">
      <el-input v-model="form.hometownProvince" placeholder="例: 山东" :disabled="isLocked" maxlength="50" />
    </el-form-item>
    <el-form-item label="证件号">
      <el-input v-model="form.idCard" placeholder="选填, 仅供 admin 审核, 公开页不展示" :disabled="isLocked" maxlength="20" show-password />
    </el-form-item>

    <!-- ========== 身份与学历 ========== -->
    <el-divider content-position="left">身份与学历</el-divider>
    <el-form-item label="教员类型" required>
      <el-select v-model="form.tutorType" style="width:100%" :disabled="isLocked" placeholder="请选择" @change="onTutorTypeChange">
        <el-option label="大学生" :value="1" />
        <el-option label="专职 / 已毕业" :value="2" />
        <el-option label="在职教师" :value="3" />
        <el-option label="海归外教" :value="4" />
      </el-select>
    </el-form-item>
    <el-form-item label="详细身份">
      <el-select v-if="form.tutorType === 1" v-model="form.identityDetail" style="width:100%" :disabled="isLocked" placeholder="请选择详细身份">
        <el-option v-for="o in studentIdentityOptions" :key="o" :label="o" :value="o" />
      </el-select>
      <el-select v-else-if="form.tutorType === 3" v-model="form.identityDetail" style="width:100%" :disabled="isLocked" placeholder="请选择详细身份">
        <el-option v-for="o in teacherIdentityOptions" :key="o" :label="o" :value="o" />
      </el-select>
      <el-input v-else v-model="form.identityDetail" :disabled="isLocked" placeholder="如: 工程师 / 翻译 / 海归外教" maxlength="50" />
    </el-form-item>
    <el-form-item label="目前学历" required>
      <el-select v-model="form.degree" style="width:100%" :disabled="isLocked" placeholder="请选择">
        <el-option label="高中" :value="1" />
        <el-option label="大专" :value="2" />
        <el-option label="本科" :value="3" />
        <el-option label="硕士" :value="4" />
        <el-option label="博士" :value="5" />
      </el-select>
    </el-form-item>
    <el-form-item v-if="form.tutorType === 1" label="高中母校">
      <el-input v-model="form.highSchool" placeholder="请填写全称, 如: 上海中学" :disabled="isLocked" maxlength="100" />
    </el-form-item>
    <el-form-item label="所在大学">
      <el-input v-model="form.university" placeholder="请输入学校名称" :disabled="isLocked" maxlength="100" />
    </el-form-item>
    <el-form-item v-if="form.tutorType === 3" label="工作单位">
      <el-input v-model="form.workUnit" placeholder="现任职学校/单位, 如: 上海市XX中学" :disabled="isLocked" maxlength="100" />
    </el-form-item>
    <el-form-item label="专业">
      <el-input v-model="form.major" placeholder="请输入专业" :disabled="isLocked" maxlength="100" />
    </el-form-item>
    <el-form-item label="年级 / 年份">
      <el-input v-model="form.gradeYear" placeholder="如: 大四 / 研二 / 工作 8 年" :disabled="isLocked" maxlength="50" />
    </el-form-item>

    <!-- ========== 联系方式 ========== -->
    <el-divider content-position="left">联系方式 (公开页不展示, 仅供客服联系)</el-divider>
    <el-form-item label="邮箱">
      <el-input v-model="form.email" placeholder="选填" :disabled="isLocked" maxlength="100" />
    </el-form-item>
    <el-form-item label="微信号">
      <el-input v-model="form.wechat" placeholder="选填, 方便客服快速联系" :disabled="isLocked" maxlength="60" />
    </el-form-item>

    <!-- ========== 居住地 ========== -->
    <el-divider content-position="left">居住地</el-divider>
    <el-form-item label="所在城市">
      <el-select v-model="form.cityId" style="width:100%" :disabled="isLocked" placeholder="选择城市" filterable @change="onCityChange">
        <el-option v-for="c in citiesList" :key="c.id" :label="c.cityName" :value="c.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="所在区域">
      <el-select v-model="form.districtId" style="width:100%" :disabled="isLocked" placeholder="选择区域" filterable>
        <el-option v-for="d in districtsList" :key="d.id" :label="d.districtName" :value="d.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="可授课区域">
      <el-select v-model="form.districts" multiple style="width:100%" :disabled="isLocked" placeholder="可勾选多个区域 (公开页展示给学员)" filterable collapse-tags collapse-tags-tooltip>
        <el-option v-for="d in districtsList" :key="d.id" :label="d.districtName" :value="d.id" />
      </el-select>
      <div style="font-size:12px;color:#94a3b8;margin-top:4px">先选好"所在城市", 然后从该城市的区域列表勾选你愿意上门授课的区</div>
    </el-form-item>
    <el-form-item label="通信地址">
      <el-input v-model="form.address" type="textarea" :rows="2" placeholder="街道 + 小区, 不写门牌号; 仅供撮合后定位, 公开页不展示" :disabled="isLocked" maxlength="200" />
    </el-form-item>

    <!-- ========== 家教信息 ========== -->
    <el-divider content-position="left">家教信息</el-divider>
    <el-form-item label="授课科目" required>
      <el-select v-model="subjectList" multiple style="width:100%" placeholder="选择科目" :disabled="isLocked">
        <el-option v-for="s in dictStore.subjects" :key="s.id" :label="s.subjectName" :value="String(s.id)" />
      </el-select>
    </el-form-item>
    <el-form-item label="上课方式">
      <el-select v-model="form.teachingMethod" style="width:100%" :disabled="isLocked" placeholder="请选择">
        <el-option label="教员上门" :value="1" />
        <el-option label="学员上门" :value="2" />
        <el-option label="网络辅导" :value="3" />
        <el-option label="均可 (上门 / 网络)" :value="4" />
      </el-select>
    </el-form-item>
    <el-form-item label="课时费区间">
      <div style="display:flex;gap:8px;align-items:center">
        <el-input-number v-model="form.priceMin" :min="0" :max="9999" :disabled="isLocked" />
        <span>-</span>
        <el-input-number v-model="form.priceMax" :min="0" :max="9999" :disabled="isLocked" />
        <span style="color:#94a3b8">元 / 小时</span>
      </div>
    </el-form-item>
    <el-form-item label="薪资备注">
      <el-input v-model="form.salaryRemark" type="textarea" :rows="2" placeholder="例: 小学 80, 初中 100, 高中 150; 默认执行平台标准" :disabled="isLocked" maxlength="200" />
    </el-form-item>
    <el-form-item label="自我介绍">
      <el-input v-model="form.selfIntroduction" type="textarea" :rows="5" placeholder="详细客观展示实力 + 性格特征 (建议 50 字以上). 如: 教学风格、学科竞赛奖项、高考分数、兴趣爱好" :disabled="isLocked" maxlength="800" show-word-limit />
    </el-form-item>
    <el-form-item label="所获证书">
      <el-input v-model="form.certificatesDesc" type="textarea" :rows="3" placeholder="例: 英语四六级、高级口译、钢琴 8 级、全国数学建模一等奖" :disabled="isLocked" maxlength="500" />
    </el-form-item>
    <el-form-item label="家教经验">
      <el-input v-model="form.teachingExperience" type="textarea" :rows="4" placeholder="例: 2024 年初三英语 (浦东) 学员从 70 分提到 95 分; 2023 年高一数学 (徐汇) 期末从 50 → 90" :disabled="isLocked" maxlength="800" />
    </el-form-item>
    <el-form-item label="成功记录">
      <el-switch
        v-model="form.showSuccessRecord"
        :active-value="1"
        :inactive-value="0"
        active-text="在主页展示"
        inactive-text="暂不展示"
        :loading="savingShowRecord"
        :disabled="savingShowRecord"
        @change="handleToggleShowRecord"
      />
      <div style="font-size:12px;color:#94a3b8;margin-top:4px">
        开启后, 已撮合的订单 (脱敏) 会展示在您的教员主页, 家长更倾向选择有成功记录的教员。
        <span style="color:#1F4E8C">展示偏好修改即时生效, 无需审核。</span>
      </div>
    </el-form-item>

    <el-form-item style="margin-top:24px">
      <el-button type="primary" :loading="saving" :disabled="isLocked" @click="handleSave">保存资料</el-button>
      <el-button type="success" :loading="submitting" :disabled="!canSubmit" @click="handleSubmitAudit">提交审核</el-button>
      <span v-if="!canSubmit && !isLocked" style="margin-left:12px;font-size:12px;color:#94a3b8">先点"保存资料", 再"提交审核"</span>
    </el-form-item>
  </el-form>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useDictStore } from '~/stores/dict'
import { ElMessage } from 'element-plus'

definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

const dictStore = useDictStore()
const { get, post } = useApi()

const profile = ref({})
const form = ref({
  avatar: '', realName: '', gender: 1, tutorType: null, degree: null,
  university: '', workUnit: '', major: '', selfIntroduction: '',
  priceMin: 0, priceMax: 0, showSuccessRecord: 1,
  // 新增 11 字段
  birthDate: '', hometownProvince: '', idCard: '', identityDetail: '',
  highSchool: '', gradeYear: '', email: '', wechat: '',
  certificatesDesc: '', teachingMethod: null, salaryRemark: '',
  teachingExperience: '',
  cityId: null, districtId: null, districts: [], address: ''
})
const subjectList = ref([])
const saving = ref(false)
const submitting = ref(false)
const pageLoading = ref(false)
const uploadingAvatar = ref(false)
const savingShowRecord = ref(false)
const citiesList = ref([])
const districtsList = ref([])

const isLocked = computed(() => profile.value.auditStatus === 1)
const canSubmit = computed(() => {
  // 必须先保存过 (profile 有 id) + 状态非 PENDING 非 APPROVED/PUBLISHED
  if (!profile.value.id) return false
  const s = profile.value.auditStatus
  return s === 0 || s === 3
})

const studentIdentityOptions = [
  '本科大一学生', '本科大二学生', '本科大三学生', '本科大四学生',
  '在读硕士生', '在读博士生', '大专在读学生', '硕士进修老师'
]
const teacherIdentityOptions = [
  '专职家教老师', '在职小学教师', '在职初中教师', '在职高中教师',
  '在职大学教师', '退休小学教师', '退休初中教师', '退休高中教师'
]

const onTutorTypeChange = () => { form.value.identityDetail = '' }

// 头像上传 — 走专用 /tutor-profile/avatar 端点, 不重置审核状态
const handleAvatarUpload = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) { ElMessage.warning('图片不能超过 5MB'); e.target.value = ''; return }
  if (!/^image\/(jpeg|png|webp)$/.test(file.type)) { ElMessage.warning('仅支持 jpg/png/webp'); e.target.value = ''; return }
  uploadingAvatar.value = true
  try {
    const fd = new FormData()
    fd.append('picFile', file)
    const upRes = await post('/system/auth/upload/pic', fd)
    if (upRes?.code !== 200 || !upRes?.data) {
      ElMessage.error(upRes?.msg || '上传失败')
      return
    }
    const url = upRes.data
    const r = await post('/user/auth/tutor-profile/avatar', { avatar: url })
    if (r?.code !== 200) { ElMessage.error(r?.msg || '保存失败'); return }
    form.value.avatar = url
    ElMessage.success('头像已更新')
  } catch (err) { console.error(err); ElMessage.error('上传失败, 请重试') }
  finally { uploadingAvatar.value = false; e.target.value = '' }
}

const onCityChange = async (cityId) => {
  form.value.districtId = null
  form.value.districts = []   // 切城市时清空多选 (旧城市的 districtId 在新城市不可选)
  if (!cityId) { districtsList.value = []; return }
  try {
    const r = await get('/user/api/dict/district/list', { cityId })
    if (r?.code === 200 && Array.isArray(r.data)) districtsList.value = r.data
  } catch { districtsList.value = [] }
}

const loadCities = async () => {
  try {
    const r = await get('/user/api/dict/city/list')
    if (r?.code === 200 && Array.isArray(r.data)) citiesList.value = r.data
  } catch { citiesList.value = [] }
}

const loadProfile = async () => {
  pageLoading.value = true
  try {
    const res = await get('/user/auth/tutor-profile/view')
    if (res?.code === 200 && res.data) {
      profile.value = res.data
      const f = form.value
      f.avatar = res.data.avatar || ''
      f.realName = res.data.realName || ''
      f.gender = res.data.gender || 1
      f.tutorType = res.data.tutorType || null
      f.degree = res.data.degree || null
      f.university = res.data.university || ''
      f.workUnit = res.data.workUnit || ''
      f.major = res.data.major || ''
      f.selfIntroduction = res.data.selfIntroduction || ''
      f.priceMin = res.data.priceMin || 0
      f.priceMax = res.data.priceMax || 0
      f.showSuccessRecord = (res.data.showSuccessRecord === 0 || res.data.showSuccessRecord === '0') ? 0 : 1
      // 新字段
      f.birthDate = res.data.birthDate || ''
      f.hometownProvince = res.data.hometownProvince || ''
      f.idCard = res.data.idCard || ''
      f.identityDetail = res.data.identityDetail || ''
      f.highSchool = res.data.highSchool || ''
      f.gradeYear = res.data.gradeYear || ''
      f.email = res.data.email || ''
      f.wechat = res.data.wechat || ''
      f.certificatesDesc = res.data.certificatesDesc || ''
      f.teachingMethod = res.data.teachingMethod || null
      f.salaryRemark = res.data.salaryRemark || ''
      f.teachingExperience = res.data.teachingExperience || ''
      f.cityId = res.data.cityId || null
      f.districtId = res.data.districtId || null
      f.address = res.data.address || ''
      // subjects (可能是 JSON [id,id] 或 CSV 名称)
      const raw = res.data.subjects || ''
      if (raw.startsWith('[')) {
        try { subjectList.value = JSON.parse(raw).map(String) } catch { subjectList.value = [] }
      } else if (raw) {
        // CSV 名称 — 反查 dict 转 ID
        const names = raw.split(',').map(s => s.trim()).filter(Boolean)
        subjectList.value = names.map(n => {
          const s = (dictStore.subjects || []).find(x => x.subjectName === n)
          return s ? String(s.id) : null
        }).filter(Boolean)
      } else {
        subjectList.value = []
      }
      // 加载该城市的区域列表 (不要调 onCityChange — 它会重置 districtId)
      if (f.cityId) {
        try {
          const r2 = await get('/user/api/dict/district/list', { cityId: f.cityId })
          if (r2?.code === 200 && Array.isArray(r2.data)) districtsList.value = r2.data
        } catch { districtsList.value = [] }
      }
      // 加载授课区域 (多选)
      try {
        const r3 = await get('/user/auth/tutor-profile/teaching-areas')
        if (r3?.code === 200 && Array.isArray(r3.data)) f.districts = r3.data.map(Number)
      } catch { f.districts = [] }
    }
  } catch (e) { console.error(e) }
  finally { pageLoading.value = false }
}

// 切换"是否展示成功记录" — 走独立端点, 不触发整页审核
const handleToggleShowRecord = async (val) => {
  // val 是 el-switch 切换后的新值 (1 或 0)
  savingShowRecord.value = true
  // 先记下旧值用于回滚 (val 即新值, 旧值 = 1 - val)
  const oldVal = val === 1 ? 0 : 1
  try {
    const res = await post('/user/auth/tutor-profile/show-success-record', { showSuccessRecord: val })
    if (res?.code === 200) {
      ElMessage.success(res.msg || (val === 1 ? '已开启主页展示' : '已关闭主页展示'))
    } else {
      ElMessage.error(res?.msg || '保存失败')
      form.value.showSuccessRecord = oldVal   // 回滚 UI
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('网络错误, 请重试')
    form.value.showSuccessRecord = oldVal
  } finally { savingShowRecord.value = false }
}

const handleSave = async () => {
  if (!form.value.realName) { ElMessage.warning('请输入真实姓名'); return }
  if (!form.value.tutorType) { ElMessage.warning('请选择教员类型'); return }
  if (!form.value.degree) { ElMessage.warning('请选择学历'); return }
  if (subjectList.value.length === 0) { ElMessage.warning('请选择授课科目'); return }
  saving.value = true
  try {
    // showSuccessRecord 走独立端点 /show-success-record (展示偏好, 不触发审核流转), 这里剔除避免回滚审核状态
    const { showSuccessRecord: _omit, ...rest } = form.value
    const payload = {
      ...rest,
      // 工作单位仅对在职教师有效; 切到其它类型后清掉残留, 避免误存到非在职教师
      workUnit: rest.tutorType === 3 ? (rest.workUnit || '') : '',
      subjects: JSON.stringify(subjectList.value.map(Number)),
      districts: JSON.stringify((form.value.districts || []).map(Number))
    }
    // 临时诊断日志: 保存前打印 payload, 帮助定位"保存后字段没存"问题
    console.log('[tutor-profile] save payload:', JSON.stringify(payload).slice(0, 1000))
    const res = await post('/user/auth/tutor-profile/save', payload)
    console.log('[tutor-profile] save response:', JSON.stringify(res))
    if (res?.code === 200) {
      ElMessage.success('保存成功' + (profile.value.auditStatus === 2 || profile.value.auditStatus === 4 ? ', 已回到待审核状态, 等待客服重新审核' : ''))
      await loadProfile()
    } else {
      ElMessage.error(res?.msg || '保存失败')
    }
  } catch (e) { console.error('[tutor-profile] save error:', e); ElMessage.error('网络错误, 请重试') }
  finally { saving.value = false }
}

const handleSubmitAudit = async () => {
  submitting.value = true
  try {
    const res = await post('/user/auth/tutor-profile/submit-audit', {})
    if (res?.code === 200) {
      ElMessage.success('已提交审核, 请等待客服审核')
      await loadProfile()
    } else {
      ElMessage.error(res?.msg || '提交失败')
    }
  } catch (e) { console.error(e); ElMessage.error('网络错误, 请重试') }
  finally { submitting.value = false }
}

onMounted(async () => {
  await Promise.all([dictStore.fetchAll(), loadCities()])
  await loadProfile()
})
</script>

<style scoped>
.tutor-form { max-width: 720px; }
.tutor-form :deep(.el-divider__text) {
  font-weight: 600;
  color: var(--color-primary);
  font-size: 15px;
}
@media (max-width: 768px) {
  .tutor-form { max-width: 100%; }
  .tutor-form :deep(.el-form-item__label) { width: 90px !important; font-size: 13px; }
  .tutor-form :deep(.el-form-item__content) { margin-left: 90px !important; }
  .tutor-form :deep(.el-input-number) { width: 100% !important; }
}
</style>
