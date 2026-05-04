<template>
  <div class="page-container">
    <h2 class="page-title">教员审核</h2>
    <div class="filter-bar">
      <el-select v-model="auditStatus" placeholder="审核状态" clearable style="width:140px">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="1" />
        <el-option label="已通过" :value="2" />
        <el-option label="已驳回" :value="3" />
        <el-option label="已发布" :value="4" />
      </el-select>
      <el-input v-model="keyword" placeholder="姓名搜索" style="width:200px" clearable @keyup.enter="search" />
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="mobile" label="手机号" width="130" />
      <el-table-column label="类型" width="80">
        <template #default="{ row }">{{ tutorTypeMap[row.tutorType] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="university" label="学校" />
      <el-table-column label="学历" width="70">
        <template #default="{ row }">{{ degreeMap[row.degree] || '-' }}</template>
      </el-table-column>
      <el-table-column label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="auditTagType(row.auditStatus)" size="small">{{ auditLabel(row.auditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="明星" width="90" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isStar === 1" type="warning" size="small">★ 明星</el-tag>
          <span v-else style="color:#bbb;font-size:12px">—</span>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" width="170" />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">详情</el-button>
          <template v-if="row.auditStatus === 1">
            <el-button size="small" type="success" @click="handleAudit(row, 'approve')">通过</el-button>
            <el-button size="small" type="danger" @click="handleAudit(row, 'reject')">驳回</el-button>
          </template>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="教员详情 - 审核" width="820px">
      <!-- 顶部概要: 头像 + 姓名 + 类型 + 明星 -->
      <div v-if="detail" class="detail-header">
        <el-image v-if="detail.avatar" :src="detail.avatar" class="detail-avatar" :preview-src-list="[detail.avatar]" />
        <div v-else class="detail-avatar detail-avatar-empty">未上传</div>
        <div>
          <div class="detail-name">
            {{ detail.realName || '匿名' }}
            <span class="detail-no">{{ detail.displayNo }}</span>
          </div>
          <div class="detail-meta">
            {{ tutorTypeMap[detail.tutorType] || '-' }}
            <span v-if="detail.gender">· {{ detail.gender === 1 ? '男' : '女' }}</span>
            <span v-if="detail.isVerified === 1" class="detail-chip detail-chip-success">已认证</span>
            <span v-if="detail.isStar === 1" class="detail-chip detail-chip-star">★ 明星教员</span>
          </div>
        </div>
      </div>

      <el-descriptions :column="2" border v-if="detail" title="基本信息">
        <el-descriptions-item label="姓名">{{ detail.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.mobile || '-' }}</el-descriptions-item>
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
        <el-descriptions-item label="所在城市">{{ detail.cityName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在区域">{{ detail.districtName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="通信地址" :span="2">{{ detail.address || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="家教信息" style="margin-top:16px">
        <el-descriptions-item label="授课方式">{{ teachingMethodMap[detail.teachingMethod] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="课时费">{{ detail.priceMin || 0 }}-{{ detail.priceMax || 0 }} 元/时</el-descriptions-item>
        <el-descriptions-item label="授课科目" :span="2">{{ detail.subjectNames || '-' }}</el-descriptions-item>
        <el-descriptions-item label="可授课区域" :span="2">
          <template v-if="detail.teachingAreasResolved && detail.teachingAreasResolved.length">
            <span style="color:#666;margin-right:6px">{{ detail.cityName || '' }} ·</span>
            <el-tag
              v-for="(a, i) in detail.teachingAreasResolved" :key="i"
              type="info" size="small" style="margin:2px 4px"
            >{{ a.districtName || '-' }}</el-tag>
          </template>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="免费试课">
          <el-tag :type="detail.freeTrial === 1 ? 'success' : 'info'" size="small">
            {{ detail.freeTrial === 1 ? '支持' : '不支持' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="主页展示成功记录">
          <el-tag :type="detail.showSuccessRecord === 0 ? 'info' : 'success'" size="small">
            {{ detail.showSuccessRecord === 0 ? '不展示' : '展示' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="薪资备注" :span="2">{{ detail.salaryRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="自我介绍" :span="2">{{ detail.selfIntroduction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所获证书 (描述)" :span="2">{{ detail.certificatesDesc || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家教经验" :span="2">{{ detail.teachingExperience || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="2" border v-if="detail" title="审核状态" style="margin-top:16px">
        <el-descriptions-item label="认证状态">
          <el-tag :type="detail.isVerified === 1 ? 'success' : 'info'" size="small">
            {{ detail.isVerified === 1 ? '已认证' : '未认证' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag :type="auditTagType(detail.auditStatus)">{{ auditLabel(detail.auditStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核备注" :span="2">{{ detail.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 资质证书 -->
      <div style="margin-top:20px" v-if="detailCerts.length > 0">
        <h4 style="margin-bottom:12px;font-weight:600">资质证书（{{ detailCerts.length }}份）</h4>
        <el-table :data="detailCerts" border stripe size="small">
          <el-table-column label="类型" width="100">
            <template #default="{ row }">{{ certTypeLabel(row.certType) }}</template>
          </el-table-column>
          <el-table-column prop="certName" label="名称" />
          <el-table-column prop="certNo" label="编号" width="120">
            <template #default="{ row }">{{ row.certNo || '-' }}</template>
          </el-table-column>
          <el-table-column label="照片" width="100">
            <template #default="{ row }">
              <el-image v-if="row.certUrl" :src="row.certUrl" style="width:50px;height:35px" fit="cover" :preview-src-list="[row.certUrl]" />
              <span v-else style="color:#999;font-size:12px">未上传</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag size="small" :type="{ 0:'warning', 1:'success', 2:'danger' }[row.auditStatus] || 'info'">
                {{ { 0:'待审核', 1:'通过', 2:'驳回' }[row.auditStatus] || '未知' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="{ row }">
              <el-button size="small" type="success" :disabled="row.auditStatus === 1" @click="certApprove(row)">通过</el-button>
              <el-button size="small" type="danger" :disabled="row.auditStatus === 2" @click="certReject(row)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else style="margin-top:16px;color:#999;font-size:13px">暂无资质证书</div>
    </el-dialog>

    <!-- 审核弹窗 -->
    <el-dialog v-model="auditVisible" :title="auditAction === 'approve' ? '审核通过' : '审核驳回'" width="450px">
      <el-form label-width="80px">
        <el-form-item label="备注">
          <el-input v-model="auditRemark" type="textarea" :rows="3" :placeholder="auditAction === 'reject' ? '请输入驳回原因（必填）' : '审核备注（选填）'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button :type="auditAction === 'approve' ? 'success' : 'danger'" :loading="submitting" @click="submitAudit">
          {{ auditAction === 'approve' ? '确认通过' : '确认驳回' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const auditStatus = ref(1)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)

const detailVisible = ref(false)
const detail = ref<any>(null)
const detailCerts = ref<any[]>([])

const certTypeLabel = (t: number) => ({ 1:'身份证正面', 2:'身份证反面', 3:'学生证/毕业证', 4:'教师资格证', 5:'其他身份证件' }[t] || '未知')

const auditVisible = ref(false)
const auditAction = ref('')
const auditRemark = ref('')
const auditRow = ref<any>(null)
const submitting = ref(false)

const tutorTypeMap: Record<number, string> = { 1: '大学生', 2: '专职', 3: '在职教师', 4: '海归外教' }
const degreeMap: Record<number, string> = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }
const teachingMethodMap: Record<number, string> = { 1: '教员上门', 2: '学员上门', 3: '网络辅导', 4: '均可' }
const auditLabel = (s: number) => ({ 0: '草稿', 1: '待审核', 2: '已通过', 3: '已驳回', 4: '已发布' }[s] || '未知')
const auditTagType = (s: number) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'danger', 4: 'success' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/tutor-audit/page', {
      pageCurrent: page.value,
      pageSize: 20,
      auditStatus: auditStatus.value,
      firstAudit: true,  // 仅"首次注册待审" — 修改重审走 /tutor/recent-edited
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
    const res = await get('/user/admin/tutor-audit/view', { id: row.id })
    if (res.code === 200) {
      detail.value = res.data
      detailVisible.value = true
      // 加载该教员的证书列表
      detailCerts.value = []
      try {
        const certRes = await get('/user/admin/tutor-audit/cert/list', { tutorId: row.id })
        if (certRes.code === 200) detailCerts.value = certRes.data || []
      } catch (e) { /* 证书加载失败不影响详情展示 */ }
    }
  } catch (e) { console.error(e) }
}

const handleAudit = (row: any, action: string) => {
  auditRow.value = row
  auditAction.value = action
  auditRemark.value = ''
  auditVisible.value = true
}

const certApprove = async (row) => {
  try {
    const res = await put('/user/admin/tutor-audit/cert/approve', { id: row.id, auditRemark: '' })
    if (res.code === 200) {
      ElMessage.success(res.msg || '已通过')
      if (detail.value) { const cr = await get('/user/admin/tutor-audit/cert/list', { tutorId: detail.value.id }); if (cr.code === 200) detailCerts.value = cr.data || [] }
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}
const certReject = async (row) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回证件', { confirmButtonText: '确认驳回', cancelButtonText: '取消', inputPattern: /.+/, inputErrorMessage: '驳回原因不能为空' })
    const res = await put('/user/admin/tutor-audit/cert/reject', { id: row.id, auditRemark: value })
    if (res.code === 200) {
      ElMessage.success(res.msg || '已驳回')
      if (detail.value) { const cr = await get('/user/admin/tutor-audit/cert/list', { tutorId: detail.value.id }); if (cr.code === 200) detailCerts.value = cr.data || [] }
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

const toggleStar = async (row: any) => {
  const next = row.isStar === 1 ? 0 : 1
  try {
    const res = await put('/user/admin/tutor-audit/star', { id: row.id, isStar: next })
    if (res.code === 200) {
      ElMessage.success(res.msg || (next === 1 ? '已设为明星教员' : '已取消明星'))
      row.isStar = next
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
}

const submitAudit = async () => {
  if (auditAction.value === 'reject' && !auditRemark.value) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  submitting.value = true
  try {
    const url = auditAction.value === 'approve'
      ? '/user/admin/tutor-audit/approve'
      : '/user/admin/tutor-audit/reject'
    const res = await put(url, { id: auditRow.value.id, auditRemark: auditRemark.value })
    if (res.code === 200) {
      ElMessage.success(auditAction.value === 'approve' ? '审核通过' : '已驳回')
      auditVisible.value = false
      await search()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { submitting.value = false }
}

onMounted(() => { search() })
</script>

<style scoped>
.detail-header {
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 8px 0 16px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}
.detail-avatar {
  width: 80px; height: 80px;
  border-radius: 6px; object-fit: cover;
  border: 1px solid #eee;
}
.detail-avatar-empty {
  background: #f5f5f5;
  color: #999;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.detail-name {
  font-weight: 600;
  font-size: 15px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}
.detail-no { color: #999; font-weight: 400; font-size: 13px; }
.detail-meta {
  color: #666;
  font-size: 13px;
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.detail-chip {
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
.detail-chip-success {
  color: #047857;
  background: #ecfdf5;
  border: 1px solid #a7f3d0;
}
.detail-chip-star {
  color: #d97706;
  background: #fffbeb;
  border: 1px solid #fde68a;
}
</style>
