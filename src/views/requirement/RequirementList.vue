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
      <el-table-column label="加急" width="60">
        <template #default="{ row }">
          <el-tag v-if="row.isUrgent === 1" type="danger" size="small">急</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column prop="applicationCount" label="申请" width="70" />
      <el-table-column prop="gmtCreate" label="创建时间" width="160" />
      <el-table-column label="操作" width="290" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="openMatch(row)" v-if="row.reqStatus !== 3 && row.reqStatus !== 5">接单确认</el-button>
          <el-popconfirm
            v-if="row.reqStatus === 3"
            title="撤销匹配后, 该订单回到公开池, 关联预约会被标 CANCELLED, 已录用的教员会收到通知. 确认?"
            confirm-button-text="撤销" cancel-button-text="取消"
            @confirm="unmatchOne(row)">
            <template #reference>
              <el-button size="small" type="info">撤销匹配</el-button>
            </template>
          </el-popconfirm>
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
      <!-- 编辑时: 修改前的城市/区域快照, 防止 admin 边改边忘 -->
      <div v-if="formId && originalSnapshot" class="snap-row">
        <span class="snap-label">当前位置</span>
        <span class="snap-val">
          <strong>{{ originalSnapshot.city || '未设置' }}</strong>
          <span v-if="originalSnapshot.districts" class="snap-sep">·</span>
          <span v-if="originalSnapshot.districts">{{ originalSnapshot.districts }}</span>
        </span>
        <span class="snap-no">{{ originalSnapshot.displayNo }}</span>
      </div>
      <el-form :model="form" label-width="100px" label-position="right">
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="标题">
              <el-input v-model="form.title" maxlength="100" placeholder="如: 浦东·初二·英语数学双科" />
              <div style="font-size:12px;color:#94a3b8;margin-top:4px;line-height:1.5">
                前台首页/学员库/详情页都用这个标题; 留空则展示科目列表 (如 "数学、英语"), 都没有时显示 "暂无科目要求"
              </div>
            </el-form-item>
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
            <el-form-item label="联系人称谓">
              <el-radio-group v-model="form.contactGender">
                <el-radio :value="0">未设置</el-radio>
                <el-radio :value="1">先生</el-radio>
                <el-radio :value="2">女士</el-radio>
              </el-radio-group>
              <div style="width:100%;font-size:12px;color:#909399;line-height:1.4;margin-top:2px">详情页联系人显示为"姓氏+称谓"(如 鲍先生);未设置则按姓名推断,都没有则显示"家长"</div>
            </el-form-item>
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
            <el-form-item label="年级"><el-input v-model="form.gradeName" maxlength="100" placeholder="如: 一年级 / 高三 / 大学一年级" /></el-form-item>
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

          <el-col :span="24">
            <el-form-item label="城市">
              <el-select v-model="form.cityId" placeholder="请选择城市" clearable filterable style="width:240px" @change="onCityChange">
                <el-option v-for="c in cityOptions" :key="c.id" :label="c.cityName" :value="Number(c.id)" />
              </el-select>
              <span v-if="!cityOptions.length" style="margin-left:12px;color:#94a3b8;font-size:12px">加载中...</span>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.cityId && districtOptions.length">
            <el-form-item label="区域 (多选)">
              <el-checkbox-group v-model="districtSel" class="chip-group">
                <el-checkbox v-for="d in districtOptions" :key="d.id" :value="d.districtName" :label="d.districtName" border size="small" />
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-else-if="form.cityId && !districtOptions.length">
            <el-form-item label="区域 (多选)">
              <span style="color:#94a3b8;font-size:12px">该城市暂无区数据</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="科目 (多选)">
              <el-checkbox-group v-model="subjectSel" class="chip-group">
                <el-checkbox v-for="s in SUBJECT_OPTIONS" :key="s" :value="s" :label="s" border size="small" />
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教员类型 (多选)">
              <el-checkbox-group v-model="tutorTypeSel" class="chip-group">
                <el-checkbox v-for="t in TUTOR_TYPE_OPTIONS" :key="t" :value="t" :label="t" border size="small" />
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教学方式">
              <el-select v-model="form.teachingMethod" placeholder="选择" style="width:240px">
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
          <el-col :span="12">
            <el-form-item label="加急">
              <el-switch v-model="form.isUrgent" :active-value="1" :inactive-value="0" active-text="加急" inactive-text="普通" inline-prompt />
              <span style="margin-left:10px;font-size:12px;color:#94a3b8">加急订单首页 \"急\" 红标</span>
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
        <el-form-item label="教员 ID">
          <el-input v-model="matchForm.tutorDisplayNo" placeholder="如 T572832 (admin/教员/家长全站可见的 T 编号)" maxlength="20" style="width:100%" />
          <div style="font-size:12px;color:#94a3b8;line-height:1.5;margin-top:4px">
            选填: 直接填教员 T 编号 (T+6位数字, 如 T572832); 系统自动 resolve, 同步该教员申请状态 + 发送站内信。<br>
            留空表示已通过电话协商, 不绑定具体账号 — 仅把订单标为已接单。
          </div>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="matchForm.remark" type="textarea" :rows="3" maxlength="500" placeholder="可填: 哪位教员接的 / 双方同意的时间地点 / 联系结果" />
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
const matchForm = ref<any>({ tutorDisplayNo: '', remark: '' })

// 与 dict_subject 表新 30 项 (id 3000~3029) 对齐. 顺序与 /xy 学员库 chip 一致.
const SUBJECT_OPTIONS = [
  '全科', '语文', '数学', '英语', '英语口语',
  '物理', '化学', '历史', '地理', '生物',
  '奥数', '高等数学',
  '钢琴', '小提琴', '古筝', '声乐', '二胡',
  '美术', '书法',
  '雅思', '托福', 'GRE', 'IB', 'A-level', 'IGCSE', '英语四级',
  '围棋', '游泳', '跆拳道', '乒乓球'
]
const TUTOR_TYPE_OPTIONS = ['大学生', '专职教员', '在职教师']

const districtSel = ref<string[]>([])
const subjectSel = ref<string[]>([])
const tutorTypeSel = ref<string[]>([])
const cityOptions = ref<any[]>([])
const districtOptions = ref<any[]>([])
const originalSnapshot = ref<{ city: string; districts: string; displayNo: string } | null>(null)

function emptyForm() {
  return {
    title: '', contactName: '', contactGender: 0, contactMobile: '', contactWechat: '',
    studentGender: 0, gradeName: '', requirementType: null,
    cityId: null as number | null,
    address: '', transport: '',
    requirementDetail: '',
    frequency: '', schedule: '',
    tutorGender: 0, budgetMin: 0, budgetMax: 0,
    otherRequirements: '', teachingMethod: 1, transportSubsidy: '',
    reqStatus: 2,
    isUrgent: 0
  }
}

const loadCities = async () => {
  if (cityOptions.value.length) return
  try {
    const res = await get('/user/api/dict/city/list')
    if (res.code === 200 && Array.isArray(res.data)) {
      cityOptions.value = res.data
    }
  } catch (e) { console.error(e) }
}

const loadDistricts = async (cityId: number | null) => {
  if (!cityId) { districtOptions.value = []; return }
  try {
    const res = await get('/user/api/dict/district/list', { cityId })
    if (res.code === 200 && Array.isArray(res.data)) {
      districtOptions.value = res.data
    } else {
      districtOptions.value = []
    }
  } catch (e) { console.error(e); districtOptions.value = [] }
}

const onCityChange = (newCityId: number | null) => {
  // 切换城市时, 已选区与新城市无关 -> 清空
  districtSel.value = []
  loadDistricts(newCityId)
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

const splitCsv = (s: string | null | undefined): string[] =>
  s ? s.split(',').map(x => x.trim()).filter(Boolean) : []

const openCreate = async () => {
  await loadCities()
  formId.value = null
  form.value = emptyForm()
  districtSel.value = []
  subjectSel.value = []
  tutorTypeSel.value = []
  districtOptions.value = []
  originalSnapshot.value = null
  editVisible.value = true
}

const openEdit = async (row: any) => {
  await loadCities()
  try {
    const res = await get('/user/admin/requirement/view', { id: row.id })
    if (res.code === 200 && res.data) {
      const d = res.data
      formId.value = d.id
      let cityIdNum: number | null = d.cityId ? Number(d.cityId) : null
      // 历史数据里的 cityId 可能 dict 里已经没有 -> 当作未设置
      if (cityIdNum != null && !cityOptions.value.some((c: any) => Number(c.id) === cityIdNum)) {
        cityIdNum = null
      }
      form.value = {
        title: d.title || '',
        contactName: d.contactName || '', contactGender: d.contactGender ?? 0, contactMobile: d.contactMobile || '', contactWechat: d.contactWechat || '',
        studentGender: d.studentGender ?? 0,
        gradeName: d.gradeName || '', requirementType: d.requirementType,
        cityId: cityIdNum,
        address: d.address || '', transport: d.transport || '',
        requirementDetail: d.requirementDetail || '',
        frequency: d.frequency || '', schedule: d.schedule || '',
        tutorGender: d.tutorGender ?? 0, budgetMin: d.budgetMin ?? 0, budgetMax: d.budgetMax ?? 0,
        otherRequirements: d.otherRequirements || '', teachingMethod: d.teachingMethod ?? 1, transportSubsidy: d.transportSubsidy || '',
        reqStatus: d.reqStatus ?? 2,
        isUrgent: d.isUrgent ?? 0
      }
      // 先把当前城市的区列表拉好, 再根据已存的区名打勾
      await loadDistricts(cityIdNum)
      districtSel.value = splitCsv(d.districtNames)
      subjectSel.value = splitCsv(d.subjectIds)
      tutorTypeSel.value = splitCsv(d.tutorTypePref)

      // 保存修改前的快照, 在弹窗顶部展示
      const cityNameSnap = cityIdNum != null
        ? (cityOptions.value.find((c: any) => Number(c.id) === cityIdNum)?.cityName || '')
        : ''
      originalSnapshot.value = {
        city: cityNameSnap,
        districts: (d.districtNames || '').split(',').map((s: string) => s.trim()).filter(Boolean).join(', '),
        displayNo: d.displayNo || ''
      }

      editVisible.value = true
    }
  } catch (e) { console.error(e) }
}

const submitForm = async () => {
  if (!form.value.title) { ElMessage.warning('请填写标题'); return }
  submitting.value = true
  try {
    const payload: any = {
      ...form.value,
      cityId: form.value.cityId ?? '',
      districtNames: districtSel.value.join(','),
      subjectIds: subjectSel.value.join(','),
      tutorTypePref: tutorTypeSel.value.join(',')
    }
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

const unmatchOne = async (row: any) => {
  try {
    const res = await put(`/user/admin/requirement/unmatch?id=${row.id}`)
    if (res.code === 200) {
      ElMessage.success(res.data || '已撤销匹配')
      search()
    } else { ElMessage.error(res.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

const openMatch = (row: any) => {
  matchTarget.value = row
  matchForm.value = { tutorDisplayNo: '', remark: '' }
  matchVisible.value = true
}

const submitMatch = async () => {
  submitting.value = true
  try {
    const res = await put('/user/admin/requirement/confirm-match', {
      requirementId: matchTarget.value.id,
      tutorDisplayNo: matchForm.value.tutorDisplayNo || '',
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
.chip-group { display: flex; flex-wrap: wrap; gap: 6px 8px; }
.chip-group :deep(.el-checkbox.is-bordered) { margin: 0; padding: 4px 12px; height: 30px; line-height: 22px; border-radius: 14px; }
.chip-group :deep(.el-checkbox__label) { font-size: 13px; }

.snap-row {
  display: flex; align-items: baseline; gap: 12px;
  padding: 10px 14px; margin: 0 0 18px;
  background: linear-gradient(to right, #f0f6fd, #fffbeb);
  border-left: 3px solid #d97706;
  border-radius: 4px;
  font-size: 13px;
}
.snap-label {
  font-weight: 600; color: #b45309;
  letter-spacing: 1px;
  flex-shrink: 0;
}
.snap-val { color: #111827; flex: 1; }
.snap-val strong { color: #163B6B; font-weight: 600; }
.snap-sep { margin: 0 6px; color: #cbd5e1; }
.snap-no {
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12px;
  color: #64748b;
  letter-spacing: 0.5px;
}
</style>
