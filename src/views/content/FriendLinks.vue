<template>
  <div class="page-container">
    <h2 class="page-title">友情链接管理</h2>
    <p class="page-tip">编辑后点击"保存全部"统一提交。删除/上移/下移在前端先生效，保存后才落库。</p>

    <div class="toolbar">
      <el-button type="primary" :icon="Plus" @click="openAddDialog">添加链接</el-button>
      <el-button type="success" :icon="Check" :loading="saving" @click="saveAll">保存全部</el-button>
      <el-button :icon="RefreshRight" @click="load">放弃修改 / 重新加载</el-button>
      <span v-if="dirty" class="dirty-tag">有未保存的修改</span>
    </div>

    <el-table :data="links" border stripe v-loading="loading" empty-text="暂无友情链接，点上方添加链接">
      <el-table-column type="index" label="序号" width="70" />
      <el-table-column prop="name" label="名称" width="220">
        <template #default="{ row, $index }">
          <el-input v-model="row.name" size="small" placeholder="如：复旦大学" @input="dirty = true" />
        </template>
      </el-table-column>
      <el-table-column prop="url" label="URL">
        <template #default="{ row }">
          <el-input v-model="row.url" size="small" placeholder="https://..." @input="dirty = true" />
        </template>
      </el-table-column>
      <el-table-column label="排序" width="160">
        <template #default="{ row, $index }">
          <el-button-group>
            <el-button size="small" :disabled="$index === 0" @click="moveUp($index)">↑ 上移</el-button>
            <el-button size="small" :disabled="$index === links.length - 1" @click="moveDown($index)">↓ 下移</el-button>
          </el-button-group>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row, $index }">
          <el-button size="small" type="danger" plain @click="removeRow($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加弹窗 -->
    <el-dialog v-model="addVisible" title="添加友情链接" width="460px">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="addForm.name" placeholder="如：华东师范大学" maxlength="40" />
        </el-form-item>
        <el-form-item label="URL" required>
          <el-input v-model="addForm.url" placeholder="https://www.example.com/" maxlength="200" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAdd">添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Check, RefreshRight } from '@element-plus/icons-vue'
import { post } from '@/api/index'

interface FriendLink { name: string; url: string }

const KEY = 'siteFriendLinksJson'

const links = ref<FriendLink[]>([])
const loading = ref(false)
const saving = ref(false)
const dirty = ref(false)
const addVisible = ref(false)
const addForm = ref<FriendLink>({ name: '', url: '' })

const load = async () => {
  loading.value = true
  try {
    const res = await post('/system/admin/sys/config/list', {})
    if (res?.code === 200 && Array.isArray(res.data)) {
      const row = res.data.find((c: any) => c.configKey === KEY)
      if (row && row.configValue) {
        try { links.value = JSON.parse(row.configValue) } catch { links.value = [] }
      } else {
        links.value = []
      }
    }
    dirty.value = false
  } catch (e) { console.error(e); ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const moveUp = (i: number) => {
  if (i <= 0) return
  ;[links.value[i - 1], links.value[i]] = [links.value[i], links.value[i - 1]]
  dirty.value = true
}
const moveDown = (i: number) => {
  if (i >= links.value.length - 1) return
  ;[links.value[i + 1], links.value[i]] = [links.value[i], links.value[i + 1]]
  dirty.value = true
}
const removeRow = async (i: number) => {
  try {
    await ElMessageBox.confirm(`确认删除"${links.value[i].name}"?`, '删除友情链接', { type: 'warning' })
    links.value.splice(i, 1)
    dirty.value = true
  } catch { /* cancel */ }
}

const openAddDialog = () => {
  addForm.value = { name: '', url: '' }
  addVisible.value = true
}
const confirmAdd = () => {
  const { name, url } = addForm.value
  if (!name?.trim()) { ElMessage.warning('名称不能为空'); return }
  if (!url?.trim()) { ElMessage.warning('URL 不能为空'); return }
  if (!/^https?:\/\//i.test(url)) { ElMessage.warning('URL 必须以 http:// 或 https:// 开头'); return }
  links.value.push({ name: name.trim(), url: url.trim() })
  dirty.value = true
  addVisible.value = false
}

const saveAll = async () => {
  // 校验
  for (const l of links.value) {
    if (!l.name?.trim() || !l.url?.trim()) {
      ElMessage.warning('存在空的名称或 URL，请补全或删除')
      return
    }
    if (!/^https?:\/\//i.test(l.url)) {
      ElMessage.warning(`"${l.name}" 的 URL 不合法（必须 http:// 或 https://）`)
      return
    }
  }
  saving.value = true
  try {
    const cleaned = links.value.map(l => ({ name: l.name.trim(), url: l.url.trim() }))
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: KEY,
      configValue: JSON.stringify(cleaned),
      configName: '友情链接'
    })
    if (res?.code === 200) {
      ElMessage.success('已保存，前端下次刷新生效')
      dirty.value = false
    } else {
      ElMessage.error(res?.msg || '保存失败')
    }
  } catch (e) { console.error(e); ElMessage.error('网络错误') }
  finally { saving.value = false }
}

onMounted(load)
</script>

<style scoped>
.page-tip { color: #94a3b8; font-size: 13px; margin-bottom: 14px; }
.toolbar { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; }
.dirty-tag {
  margin-left: 8px;
  padding: 3px 10px;
  background: #fff7ed;
  color: #c2410c;
  border: 1px solid #fed7aa;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
</style>
