<template>
  <div class="page-container">
    <h2 class="page-title">教员修改重审</h2>
    <p style="color:#94a3b8;margin-bottom:14px;font-size:13px">
      已发布教员修改资料后, 自动回到待审核状态, 集中在此审核. 跟"教员审核 (首次注册)"完全互斥, 不重叠。
    </p>

    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="按姓名搜索" style="width:200px" clearable @keyup.enter="load" />
      <el-button type="primary" @click="load">搜索</el-button>
    </div>

    <el-table :data="list" border stripe v-loading="loading" empty-text="暂无修改重审">
      <el-table-column prop="displayNo" label="编号" width="100" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="mobile" label="手机号" width="130" />
      <el-table-column label="审核状态" width="90">
        <template #default="{ row }">
          <el-tag type="warning" size="small">待重审</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="教员类型" width="90">
        <template #default="{ row }">{{ tutorTypeMap[row.tutorType] || '-' }}</template>
      </el-table-column>
      <el-table-column label="首次发布" width="170">
        <template #default="{ row }">{{ row.lastPublishedAt || '-' }}</template>
      </el-table-column>
      <el-table-column label="本次修改" width="170">
        <template #default="{ row }">{{ row.gmtModified || '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDetail(row)">查看详情</el-button>
          <el-button size="small" type="success" @click="approve(row)">通过</el-button>
          <el-popconfirm
            title="驳回原因 (在弹窗内填)"
            confirm-button-text="确认驳回"
            cancel-button-text="取消"
            @confirm="openReject(row)">
            <template #reference>
              <el-button size="small" type="danger" plain>驳回</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情对话框 (复用 TutorAudit 样式 — 4 大块全字段) -->
    <el-dialog v-model="detailVisible" title="教员详情 - 修改重审" width="820px">
      <el-descriptions :column="2" border v-if="detail" title="基本信息">
        <el-descriptions-item label="姓名">{{ detail.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.mobile }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="出生年月">{{ detail.birthDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="籍贯">{{ detail.hometownProvince || '-' }}</el-descriptions-item>
        <el-descriptions-item label="证件号">{{ detail.idCard || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="身份与学历" style="margin-top:16px">
        <el-descriptions-item label="教员类型">{{ tutorTypeMap[detail.tutorType] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细身份">{{ detail.identityDetail || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ degreeMap[detail.degree] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="高中母校">{{ detail.highSchool || '-' }}</el-descriptions-item>
        <el-descriptions-item label="大学">{{ detail.university || '-' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ detail.major || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年级 / 年份">{{ detail.gradeYear || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="联系方式" style="margin-top:16px">
        <el-descriptions-item label="邮箱">{{ detail.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="微信">{{ detail.wechat || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在城市">{{ detail.cityId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在区域">{{ detail.districtId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="通信地址" :span="2">{{ detail.address || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="家教信息" style="margin-top:16px">
        <el-descriptions-item label="授课方式">{{ teachingMethodMap[detail.teachingMethod] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="课时费">{{ detail.priceMin || 0 }}-{{ detail.priceMax || 0 }} 元/时</el-descriptions-item>
        <el-descriptions-item label="授课科目" :span="2">{{ detail.subjects || '-' }}</el-descriptions-item>
        <el-descriptions-item label="薪资备注" :span="2">{{ detail.salaryRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">{{ detail.selfIntroduction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所获证书" :span="2">{{ detail.certificatesDesc || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家教经验" :span="2">{{ detail.teachingExperience || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="审核状态" style="margin-top:16px">
        <el-descriptions-item label="审核状态">
          <el-tag type="warning">待重审</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="首次发布">{{ detail.lastPublishedAt || '-' }}</el-descriptions-item>
        <el-descriptions-item label="本次修改">{{ detail.gmtModified || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核备注" :span="2">{{ detail.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="success" @click="approveFromDetail">通过</el-button>
        <el-button type="danger" plain @click="rejectFromDetail">驳回</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="驳回理由" width="500px">
      <el-input v-model="rejectRemark" type="textarea" :rows="4" placeholder="请填写驳回理由 (必填)" maxlength="500" />
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { get, post, put } from '@/api/index'

const list = ref<any[]>([])
const loading = ref(false)
const keyword = ref('')
const detail = ref<any>(null)
const detailVisible = ref(false)
const rejectVisible = ref(false)
const rejectRemark = ref('')
const rejectTargetId = ref<number | null>(null)
const tutorTypeMap: Record<number, string> = { 1: '大学生', 2: '专职', 3: '在职教师', 4: '海归外教' }
const degreeMap: Record<number, string> = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }
const teachingMethodMap: Record<number, string> = { 1: '教员上门', 2: '学员上门', 3: '网络辅导', 4: '均可' }

const load = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/tutor-audit/recent-edited', { keyword: keyword.value, limit: 50 })
    if (res?.code === 200 && res.data) {
      list.value = res.data.list || []
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const openDetail = async (row: any) => {
  try {
    const r = await get('/user/admin/tutor-audit/view', { id: row.id })
    if (r?.code === 200) { detail.value = r.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

const approve = async (row: any) => {
  try {
    const r = await put('/user/admin/tutor-audit/approve', { id: row.id, auditRemark: '修改通过' })
    if (r?.code === 200) {
      ElMessage.success('已通过')
      await load()
    } else { ElMessage.error(r?.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

const approveFromDetail = async () => {
  if (!detail.value) return
  await approve(detail.value)
  detailVisible.value = false
}

const openReject = (row: any) => {
  rejectTargetId.value = row.id
  rejectRemark.value = ''
  rejectVisible.value = true
}

const rejectFromDetail = () => {
  if (!detail.value) return
  rejectTargetId.value = detail.value.id
  rejectRemark.value = ''
  rejectVisible.value = true
}

const confirmReject = async () => {
  if (!rejectRemark.value.trim()) { ElMessage.warning('驳回原因不能为空'); return }
  try {
    const r = await put('/user/admin/tutor-audit/reject', { id: rejectTargetId.value, auditRemark: rejectRemark.value.trim() })
    if (r?.code === 200) {
      ElMessage.success('已驳回')
      rejectVisible.value = false
      detailVisible.value = false
      await load()
    } else { ElMessage.error(r?.msg || '操作失败') }
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; gap: 12px; align-items: center; margin-bottom: 16px; }
</style>
