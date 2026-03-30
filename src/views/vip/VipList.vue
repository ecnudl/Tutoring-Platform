<template>
  <div class="page-container">
    <h2 class="page-title">VIP管理</h2>
    <div class="filter-bar">
      <el-button type="success" @click="handleGrant">开通VIP</el-button>
      <el-button type="primary" @click="search">刷新</el-button>
    </div>
    <el-table :data="list" border stripe empty-text="暂无数据" v-loading="loading">
      <el-table-column prop="userId" label="用户ID" width="180" />
      <el-table-column label="等级" width="80">
        <template #default="{ row }">V{{ row.vipLevel || 1 }}</template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="170" />
      <el-table-column prop="endTime" label="到期时间" width="170" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusId === 1 ? 'success' : 'info'" size="small">{{ row.statusId === 1 ? '生效' : '失效' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-popconfirm title="确定撤销该用户的VIP？" @confirm="handleRevoke(row.id)" v-if="row.statusId===1">
            <template #reference>
              <el-button size="small" type="danger">撤销</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="display:flex;justify-content:flex-end;margin-top:16px" v-if="total > 0">
      <el-pagination layout="total,prev,pager,next" :total="total" :page-size="20" :current-page="page" @current-change="p => { page=p; search() }" />
    </div>

    <el-dialog v-model="grantVisible" title="开通VIP" width="450px">
      <el-form label-width="80px">
        <el-form-item label="用户ID">
          <el-input v-model="grantForm.userId" placeholder="输入用户ID" />
        </el-form-item>
        <el-form-item label="VIP等级">
          <el-select v-model="grantForm.vipLevel">
            <el-option label="V1" :value="1" />
            <el-option label="V2" :value="2" />
            <el-option label="V3" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="天数">
          <el-input-number v-model="grantForm.days" :min="1" :max="3650" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="grantForm.remark" placeholder="开通原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="grantVisible = false">取消</el-button>
        <el-button type="primary" :loading="granting" @click="submitGrant">确认开通</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { post, put } from '@/api/index'
import { ElMessage } from 'element-plus'

const list = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const grantVisible = ref(false)
const grantForm = ref({ userId: '', vipLevel: 1, days: 30, remark: '' })
const granting = ref(false)

const search = async () => {
  loading.value = true
  try {
    const res = await post('/user/admin/vip/page', { pageCurrent: page.value, pageSize: 20 })
    if (res.code === 200 && res.data) {
      list.value = res.data.list || []
      total.value = res.data.totalCount || 0
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const handleGrant = () => {
  grantForm.value = { userId: '', vipLevel: 1, days: 30, remark: '' }
  grantVisible.value = true
}

const submitGrant = async () => {
  if (!grantForm.value.userId) { ElMessage.warning('请输入用户ID'); return }
  granting.value = true
  try {
    const res = await post('/user/admin/vip/grant', {
      ...grantForm.value, userId: Number(grantForm.value.userId)
    })
    if (res.code === 200) { ElMessage.success(res.data || '开通成功'); grantVisible.value = false; await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
  finally { granting.value = false }
}

const handleRevoke = async (id: number) => {
  try {
    const res = await put('/user/admin/vip/revoke', null, { params: { id } })
    if (res.code === 200) { ElMessage.success('撤销成功'); await search() }
    else ElMessage.error(res.msg || '操作失败')
  } catch (e) { ElMessage.error('网络错误') }
}

onMounted(() => { search() })
</script>
