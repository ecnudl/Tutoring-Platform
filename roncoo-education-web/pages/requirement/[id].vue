<template>
  <div class="container" style="padding:20px">
    <el-card style="max-width:900px;margin:0 auto" v-loading="loading">
      <div v-if="req">
        <h1 style="margin-bottom:16px">{{ req.title }}</h1>
        <div style="margin-bottom:16px">
          <el-tag type="success" size="small">招募中</el-tag>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="预算">
            {{ req.budgetMin || 0 }}-{{ req.budgetMax || 0 }}元/小时
          </el-descriptions-item>
          <el-descriptions-item label="教员性别要求">
            {{ req.tutorGender === 1 ? '男' : req.tutorGender === 2 ? '女' : '不限' }}
          </el-descriptions-item>
          <el-descriptions-item label="时间安排">{{ req.schedule || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ req.contactName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ req.contactMobile || '-' }}</el-descriptions-item>
          <el-descriptions-item label="浏览次数">{{ req.viewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="详细需求" :span="2">
            {{ req.requirementDetail || '暂无描述' }}
          </el-descriptions-item>
          <el-descriptions-item label="发布时间" :span="2">{{ req.gmtCreate }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top:24px;display:flex;gap:12px">
          <el-button type="primary" size="large" @click="handleApply" v-if="userStore.isTutor">
            我要应聘
          </el-button>
          <el-button size="large" @click="$router.back()">返回列表</el-button>
        </div>
      </div>
      <div v-else-if="!loading" style="text-align:center;padding:40px;color:#999">需求不存在或已关闭</div>
    </el-card>

    <!-- 申请弹窗 -->
    <el-dialog v-model="applyVisible" title="申请这个需求" width="500px">
      <el-form label-width="80px">
        <el-form-item label="自荐语">
          <el-input v-model="applyMessage" type="textarea" :rows="4" placeholder="简单介绍您为什么适合这个需求" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyVisible = false">取消</el-button>
        <el-button type="primary" :loading="applying" @click="submitApply">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'

const route = useRoute()
const { get, post } = useApi()
const userStore = useUserStore()

const reqId = route.params.id
const req = ref(null)
const loading = ref(true)

const applyVisible = ref(false)
const applyMessage = ref('')
const applying = ref(false)

const loadReq = async () => {
  loading.value = true
  try {
    const res = await get('/user/api/requirement/view', { id: reqId })
    if (res.code === 200) {
      req.value = res.data
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleApply = () => {
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录'); return }
  applyMessage.value = ''
  applyVisible.value = true
}

const submitApply = async () => {
  applying.value = true
  try {
    const res = await post('/user/auth/application/apply', {
      requirementId: Number(reqId),
      applyMessage: applyMessage.value
    })
    if (res.code === 200) {
      ElMessage.success('申请成功')
      applyVisible.value = false
    } else {
      ElMessage.error(res.msg || '申请失败')
    }
  } catch (e) { ElMessage.error('网络错误') }
  finally { applying.value = false }
}

onMounted(() => { loadReq() })
</script>
