<template>
  <div class="page-container">
    <h2 class="page-title">页脚菜单管理</h2>
    <p class="page-tip">
      管理网站底部三栏 (默认 "我是学员 / 我是教员 / 关于我们")。
      <el-popover placement="bottom-start" :width="380" trigger="hover">
        <template #reference>
          <el-link type="primary" :underline="false">常用页面路径参考 ›</el-link>
        </template>
        <div class="ref-list">
          <div class="ref-h">站内常用路径 (链接填这些)</div>
          <div v-for="r in REFERENCE_PATHS" :key="r.path" class="ref-row">
            <code class="ref-path">{{ r.path }}</code>
            <span class="ref-desc">{{ r.desc }}</span>
          </div>
          <div class="ref-h" style="margin-top:8px">外部链接</div>
          <div class="ref-row">
            <code class="ref-path">https://...</code>
            <span class="ref-desc">完整 URL, 会在新窗口打开</span>
          </div>
        </div>
      </el-popover>
    </p>

    <div class="toolbar">
      <el-button type="primary" :icon="Plus" @click="addGroup">新增分组</el-button>
      <el-button type="success" :icon="Check" :loading="saving" @click="saveAll">保存全部</el-button>
      <el-button :icon="RefreshRight" @click="load">放弃修改 / 重新加载</el-button>
      <span v-if="dirty" class="dirty-tag">有未保存的修改</span>
    </div>

    <div v-loading="loading" class="groups-grid">
      <div v-for="(g, gi) in groups" :key="gi" class="group-card">
        <div class="group-head">
          <el-input v-model="g.group" placeholder="分组标题, 例: 我是学员" size="default" class="group-title-input" @input="dirty = true">
            <template #prepend>分组</template>
          </el-input>
          <div class="group-ops">
            <el-button-group size="small">
              <el-button :disabled="gi === 0" @click="moveGroupLeft(gi)" title="左移">←</el-button>
              <el-button :disabled="gi === groups.length - 1" @click="moveGroupRight(gi)" title="右移">→</el-button>
            </el-button-group>
            <el-popconfirm :title="`删除「${g.group}」整个分组? 含 ${g.items.length} 项`" @confirm="removeGroup(gi)" confirm-button-text="删除分组" cancel-button-text="取消">
              <template #reference>
                <el-button size="small" type="danger" plain>删除分组</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>

        <div class="items-block">
          <div v-for="(it, ii) in g.items" :key="ii" class="item-row">
            <div class="item-fields">
              <el-input v-model="it.label" placeholder="显示文字, 如 请家教流程" size="small" class="label-input" @input="dirty = true" />
              <el-input v-model="it.href" placeholder="链接, 如 /help 或 https://..." size="small" class="href-input" @input="dirty = true" />
            </div>
            <div class="item-ops">
              <el-button-group size="small">
                <el-button :disabled="ii === 0" @click="moveItemUp(gi, ii)" :icon="ArrowUp" />
                <el-button :disabled="ii === g.items.length - 1" @click="moveItemDown(gi, ii)" :icon="ArrowDown" />
              </el-button-group>
              <el-button size="small" type="danger" plain :icon="Delete" @click="removeItem(gi, ii)" />
            </div>
          </div>
          <el-button class="add-item-btn" type="primary" link :icon="Plus" @click="addItem(gi)">添加条目</el-button>
        </div>
      </div>

      <div v-if="!groups.length && !loading" class="empty-card">
        <p>暂无分组, 点上方"新增分组"开始编辑</p>
      </div>
    </div>

    <p class="footer-tip">
      💡 提示: 保存后<strong>前端自动生效</strong>, 用户下次刷新页面就能看到新菜单。
      链接可以填站内路径 (如 <code>/help</code>) 或完整外部 URL (如 <code>https://...</code>)。
    </p>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Check, RefreshRight, ArrowUp, ArrowDown, Delete } from '@element-plus/icons-vue'
import { post, get } from '@/api/index'

interface FooterItem { label: string; href: string }
interface FooterGroup { group: string; items: FooterItem[] }

const KEY = 'siteFooterMenusJson'

const REFERENCE_PATHS = [
  { path: '/', desc: '首页' },
  { path: '/jy', desc: '教员库' },
  { path: '/xy', desc: '学员需求库' },
  { path: '/qjj', desc: '请家教' },
  { path: '/zf', desc: '收费标准' },
  { path: '/help', desc: '帮助中心' },
  { path: '/about/contact', desc: '联系我们' },
  { path: '/about/intro', desc: '公司简介' },
  { path: '/agreement/user', desc: '用户协议' },
  { path: '/agreement/disclaimer', desc: '免责声明' },
  { path: '/agreement/privacy', desc: '隐私保护' },
  { path: '/login', desc: '登录页' }
]

const groups = ref<FooterGroup[]>([])
const loading = ref(false)
const saving = ref(false)
const dirty = ref(false)

const load = async () => {
  loading.value = true
  try {
    const res = await get('/system/api/site/config')
    if (res?.code === 200 && res.data) {
      const raw = res.data.siteFooterMenusJson
      if (raw) {
        try {
          const parsed = JSON.parse(raw)
          if (Array.isArray(parsed)) {
            groups.value = parsed.map((g: any) => ({
              group: String(g.group || ''),
              items: Array.isArray(g.items) ? g.items.map((i: any) => ({
                label: String(i.label || ''),
                href: String(i.href || '')
              })) : []
            }))
          }
        } catch (e) {
          ElMessage.error('JSON 解析失败, 后台数据格式异常')
          groups.value = []
        }
      } else {
        groups.value = []
      }
    }
    dirty.value = false
  } catch (e) { console.error(e); ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const addGroup = () => {
  groups.value.push({ group: '新分组', items: [{ label: '', href: '' }] })
  dirty.value = true
}
const removeGroup = (gi: number) => {
  groups.value.splice(gi, 1)
  dirty.value = true
}
const moveGroupLeft = (gi: number) => {
  if (gi <= 0) return
  ;[groups.value[gi - 1], groups.value[gi]] = [groups.value[gi], groups.value[gi - 1]]
  dirty.value = true
}
const moveGroupRight = (gi: number) => {
  if (gi >= groups.value.length - 1) return
  ;[groups.value[gi + 1], groups.value[gi]] = [groups.value[gi], groups.value[gi + 1]]
  dirty.value = true
}

const addItem = (gi: number) => {
  groups.value[gi].items.push({ label: '', href: '' })
  dirty.value = true
}
const removeItem = (gi: number, ii: number) => {
  groups.value[gi].items.splice(ii, 1)
  dirty.value = true
}
const moveItemUp = (gi: number, ii: number) => {
  if (ii <= 0) return
  const items = groups.value[gi].items
  ;[items[ii - 1], items[ii]] = [items[ii], items[ii - 1]]
  dirty.value = true
}
const moveItemDown = (gi: number, ii: number) => {
  const items = groups.value[gi].items
  if (ii >= items.length - 1) return
  ;[items[ii + 1], items[ii]] = [items[ii], items[ii + 1]]
  dirty.value = true
}

const saveAll = async () => {
  // 校验
  for (const g of groups.value) {
    if (!g.group?.trim()) { ElMessage.warning('存在空分组名'); return }
    for (const it of g.items) {
      if (!it.label?.trim() || !it.href?.trim()) {
        ElMessage.warning(`"${g.group}" 分组下有未填写的条目, 请补全或删除`)
        return
      }
    }
  }
  saving.value = true
  try {
    const cleaned = groups.value.map(g => ({
      group: g.group.trim(),
      items: g.items.map(i => ({ label: i.label.trim(), href: i.href.trim() }))
    }))
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: KEY,
      configValue: JSON.stringify(cleaned),
      configName: '页脚菜单'
    })
    if (res?.code === 200) {
      ElMessage.success('已保存, 前端下次刷新页面生效')
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
.page-container { padding: 20px; }
.page-title { font-size: 18px; font-weight: 600; margin: 0 0 6px; }
.page-tip { color: #6b7280; font-size: 13px; margin: 0 0 16px; }
.toolbar { display: flex; gap: 8px; align-items: center; margin-bottom: 16px; }
.dirty-tag { color: #d97706; font-size: 13px; margin-left: 8px; }

.groups-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  gap: 16px;
}

.group-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.group-head {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.group-title-input :deep(.el-input__wrapper) { background: #f9fafb; }
.group-ops { display: flex; gap: 6px; justify-content: flex-end; }

.items-block { display: flex; flex-direction: column; gap: 6px; }
.item-row {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px;
  border-radius: 6px;
  border: 1px dashed transparent;
  transition: all 0.15s;
}
.item-row:hover {
  border-color: #e5e7eb;
  background: #fafafa;
}
.item-fields {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  min-width: 0;
}
.label-input :deep(.el-input__wrapper) { font-weight: 500; }
.href-input :deep(.el-input__wrapper) { font-family: ui-monospace, "SF Mono", Consolas, monospace; font-size: 12px; }
.item-ops { display: flex; gap: 4px; flex-shrink: 0; }

.add-item-btn {
  margin-top: 4px;
  justify-content: flex-start;
}

.empty-card {
  grid-column: 1 / -1;
  padding: 60px;
  text-align: center;
  color: #94a3b8;
  background: #f9fafb;
  border-radius: 10px;
}

.footer-tip {
  margin-top: 24px;
  color: #6b7280;
  font-size: 13px;
  line-height: 1.7;
  background: #f9fafb;
  padding: 12px 16px;
  border-radius: 6px;
  border-left: 3px solid var(--el-color-primary, #163B6B);
}
.footer-tip code {
  background: #eef2f7;
  padding: 1px 5px;
  border-radius: 3px;
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 12px;
}

.ref-list { font-size: 12px; line-height: 1.7; }
.ref-h { font-weight: 600; color: #374151; margin-bottom: 4px; }
.ref-row { display: flex; align-items: center; gap: 8px; padding: 2px 0; }
.ref-path { background: #f1f5f9; padding: 1px 6px; border-radius: 3px; color: #163B6B; font-family: ui-monospace, "SF Mono", Consolas, monospace; }
.ref-desc { color: #6b7280; }
</style>
