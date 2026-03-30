<template>
  <div class="page-container">
    <h2 class="page-title">反馈管理</h2>
    <div class="filter-bar">
      <el-select v-model="fbStatus" placeholder="状态" clearable style="width:140px" @change="search">
        <el-option label="全部" :value="null" />
        <el-option label="待处理" :value="0" />
        <el-option label="已回复" :value="1" />
        <el-option label="已关闭" :value="2" />
      </el-select>
      <el-button type="primary" @click="search">搜索</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="userId" label="用户ID" width="180" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="contact" label="联系方式" width="140" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="fbTagType(row.fbStatus)" size="small">{{ fbLabel(row.fbStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reply" label="回复" show-overflow-tooltip />
      <el-table-column prop="gmtCreate" label="提交时间" width="170" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
          <el-button size="small" type="primary" v-if="row.fbStatus===0" @click="handleReply(row)">回复</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="detailVisible" title="反馈详情" width="500px">
      <el-descriptions :column="1" border v-if="detail">
        <el-descriptions-item label="用户ID">{{ detail.userId }}</el-descriptions-item>
        <el-descriptions-item label="内容">{{ detail.content }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ detail.contact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="fbTagType(detail.fbStatus)">{{ fbLabel(detail.fbStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="回复" v-if="detail.reply">{{ detail.reply }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detail.gmtCreate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="replyVisible" title="回复反馈" width="450px">
      <el-form label-width="80px">
        <el-form-item label="反馈内容">
          <div style="color:#666">{{ replyRow?.content }}</div>
        </el-form-item>
        <el-form-item label="回复">
          <el-input v-model="replyText" type="textarea" :rows="3" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyVisible = false">取消</el-button>
        <el-button type="primary" :loading="replying" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, get, put } from '@/api/index'
import { ElMessage } from 'element-plus'

const fbStatus = ref<number | null>(null)
const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)
const replyVisible = ref(false)
const replyRow = ref<any>(null)
const replyText = ref('')
const replying = ref(false)

const fbLabel = (s: number) => ({ 0:'待处理',1:'已回复',2:'已关闭' }[s] || '未知')
const fbTagType = (s: number) => ({ 0:'warning',1:'success',2:'info' }[s] || 'info')

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/feedback/page', {
      pageCurrent: page.value, pageSize: 20, fbStatus: fbStatus.value
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
    const res = await get('/user/admin/feedback/view', { id: row.id })
    if (res.code === 200) { detail.value = res.data; detailVisible.value = true }
  } catch (e) { console.error(e) }
}

const handleReply = (row: any) => {
  replyRow.value = row
  replyText.value = ''
  replyVisible.value = true
}

const submitReply = async () => {
  if (!replyText.value) { ElMessage.warning('请输入回复内容'); return }
  replying.value = true
  try {
    const res = await put('/user/admin/feedback/reply', { id: replyRow.value.id, reply: replyText.value })
    if (res.code === 200) { ElMessage.success('回复成功'); replyVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { replying.value = false }
}

onMounted(() => { search() })
</script>
