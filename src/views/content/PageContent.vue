<template>
  <div class="page-container">
    <h2 class="page-title">页面内容管理</h2>
    <p class="page-tip">
      管理"请家教流程 / 怎样找到老师 / 成为家教老师 / 公司简介..." 等长文本页面。
      支持 <strong>Markdown 语法</strong>, 不懂语法也可以直接粘贴纯文本。
    </p>

    <div class="toolbar">
      <el-select v-model="currentKey" placeholder="选择要编辑的页面" style="width:280px" @change="onChangePage">
        <el-option v-for="p in PAGES" :key="p.key" :label="p.label" :value="p.key">
          <span style="float:left">{{ p.label }}</span>
          <span style="float:right;color:#94a3b8;font-size:12px">{{ p.url }}</span>
        </el-option>
      </el-select>
      <el-button :icon="View" @click="openPreviewUrl" :disabled="!currentPage">查看前端页面</el-button>
      <el-button type="success" :icon="Check" :loading="saving" @click="saveAll" :disabled="!dirty">保存</el-button>
      <el-button :icon="RefreshRight" @click="load">放弃修改 / 重新加载</el-button>
      <el-popconfirm title="清空当前页面 admin 自定义内容? 前端将回到代码内置的默认文案。" @confirm="restoreDefault" confirm-button-text="确认清空" cancel-button-text="取消">
        <template #reference>
          <el-button :icon="Delete" type="danger" plain :disabled="!source">恢复默认</el-button>
        </template>
      </el-popconfirm>
      <span v-if="dirty" class="dirty-tag">有未保存的修改</span>
    </div>

    <div class="md-help">
      <el-popover placement="bottom-start" :width="500" trigger="click">
        <template #reference>
          <el-link type="primary" :underline="false">📖 Markdown 语法速查</el-link>
        </template>
        <div class="md-cheat">
          <div class="cheat-row"><code># 一级标题</code><span># 后面跟空格</span></div>
          <div class="cheat-row"><code>## 二级标题</code><span>## 后空格</span></div>
          <div class="cheat-row"><code>**粗体**</code><span>用两个星号包裹</span></div>
          <div class="cheat-row"><code>*斜体*</code><span>用单个星号</span></div>
          <div class="cheat-row"><code>- 列表项</code><span>- 后空格, 每行一条</span></div>
          <div class="cheat-row"><code>1. 有序列表</code><span>数字 + 点 + 空格</span></div>
          <div class="cheat-row"><code>[文字](https://链接)</code><span>方括号+圆括号</span></div>
          <div class="cheat-row"><code>&gt; 引用</code><span>大于号开头</span></div>
          <div class="cheat-row"><code>---</code><span>横线分隔</span></div>
          <div class="cheat-row"><code>`代码`</code><span>反引号包裹</span></div>
          <div class="cheat-tip">💡 不写语法直接打字也行, 段落之间空一行即可换段。</div>
        </div>
      </el-popover>
    </div>

    <div class="editor-grid" v-loading="loading">
      <div class="editor-pane">
        <div class="pane-label">编辑区 (Markdown 源码)</div>
        <textarea
          v-model="source"
          class="md-textarea"
          placeholder="在这里写内容...
例如:
# 请家教流程
1. 在首页点「请家教」
2. 填写需求
3. 客服 24 小时内联系

支持 **Markdown** 语法, 也可以直接打字, 段间空一行换段."
          @input="dirty = true"
        ></textarea>
        <div class="pane-foot">{{ source.length }} 字符 · 保存后前端立即生效</div>
      </div>

      <div class="editor-pane preview-pane">
        <div class="pane-label">实时预览 (前端最终展示效果)</div>
        <div v-if="renderedHtml" class="preview-content" v-html="renderedHtml"></div>
        <div v-else class="preview-empty">
          左侧输入内容即可看到预览。<br>
          当前无自定义内容, 前端展示的是代码内置默认文案。
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Check, RefreshRight, Delete, View } from '@element-plus/icons-vue'
import { post, get } from '@/api/index'
import { marked } from 'marked'

interface PageMeta {
  key: string
  label: string
  url: string
}

// 与 web 端 composables/helpArticles.ts + sys_config key 对应
const PAGES: PageMeta[] = [
  { key: 'helpRequestProcessHtml', label: '请家教流程', url: '/help/request-process' },
  { key: 'helpFindTutorHtml',      label: '怎样找到老师', url: '/help/find-tutor' },
  { key: 'helpRequestFaqHtml',     label: '请家教常见问题', url: '/help/request-faq' },
  { key: 'helpBecomeTutorHtml',    label: '成为家教老师', url: '/help/become-tutor' },
  { key: 'helpTutorFaqHtml',       label: '做家教常见问题', url: '/help/tutor-faq' },
  { key: 'helpTutorPricingHtml',   label: '做家教收费标准', url: '/help/tutor-pricing' },
  { key: 'helpPaymentMethodHtml',  label: '付款方式',     url: '/help/payment-method' },
  { key: 'siteAboutIntroHtml',     label: '公司简介',     url: '/about/intro' }
]

const currentKey = ref<string>(PAGES[0].key)
const allConfig = ref<Record<string, string>>({})
const source = ref('')          // 编辑区当前内容 (markdown 或 HTML)
const loading = ref(false)
const saving = ref(false)
const dirty = ref(false)

const currentPage = computed(() => PAGES.find(p => p.key === currentKey.value))

// 实时预览: marked 把 markdown 转 HTML
const renderedHtml = computed(() => {
  if (!source.value) return ''
  try {
    return marked.parse(source.value, { breaks: true, gfm: true })
  } catch (e) {
    console.warn('marked parse error', e)
    return source.value  // fallback: 当作纯 HTML 显示
  }
})

const load = async () => {
  loading.value = true
  try {
    const res = await get('/system/api/site/config')
    if (res?.code === 200 && res.data) {
      allConfig.value = res.data
      source.value = (allConfig.value[currentKey.value] || '').trim()
      dirty.value = false
    }
  } catch (e) { console.error(e); ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const onChangePage = () => {
  if (dirty.value) {
    if (!confirm('当前页面有未保存修改, 切换页面会丢失. 确认切换?')) {
      // 切回原值 (用户取消, 但 v-model 已经改了 currentKey, 这里不好回退;
      // 简单处理: 让 dirty 标志保留, 用户重新选回去能看到旧编辑)
      return
    }
  }
  source.value = (allConfig.value[currentKey.value] || '').trim()
  dirty.value = false
}

const saveAll = async () => {
  if (!currentPage.value) return
  saving.value = true
  try {
    // 存 HTML (前端 v-html 直接渲染); admin 输入是 markdown, 这里转 HTML 落库
    const html = marked.parse(source.value || '', { breaks: true, gfm: true }) as string
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: currentKey.value,
      configValue: html,
      configName: '页面内容 - ' + currentPage.value.label
    })
    if (res?.code === 200) {
      ElMessage.success(`已保存「${currentPage.value.label}」, 前端立即生效`)
      // 更新本地缓存, 切换页面回来不丢
      allConfig.value[currentKey.value] = html
      dirty.value = false
    } else {
      ElMessage.error(res?.msg || '保存失败')
    }
  } catch (e) { console.error(e); ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const restoreDefault = async () => {
  if (!currentPage.value) return
  saving.value = true
  try {
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: currentKey.value,
      configValue: '',
      configName: '页面内容 - ' + currentPage.value.label
    })
    if (res?.code === 200) {
      ElMessage.success(`已清空, 前端恢复内置默认文案`)
      allConfig.value[currentKey.value] = ''
      source.value = ''
      dirty.value = false
    } else {
      ElMessage.error(res?.msg || '操作失败')
    }
  } catch (e) { console.error(e); ElMessage.error('网络错误') }
  finally { saving.value = false }
}

const openPreviewUrl = () => {
  if (!currentPage.value) return
  window.open('https://591jiajiao.cn' + currentPage.value.url, '_blank')
}

onMounted(load)
</script>

<style scoped>
.page-container { padding: 20px; }
.page-title { font-size: 18px; font-weight: 600; margin: 0 0 6px; }
.page-tip { color: #6b7280; font-size: 13px; margin: 0 0 14px; }
.toolbar { display: flex; gap: 8px; align-items: center; margin-bottom: 10px; flex-wrap: wrap; }
.dirty-tag { color: #d97706; font-size: 13px; margin-left: 4px; }

.md-help { margin-bottom: 12px; }
.md-cheat { font-size: 12px; line-height: 1.7; }
.cheat-row {
  display: grid;
  grid-template-columns: 180px 1fr;
  gap: 8px;
  align-items: center;
  padding: 2px 0;
}
.cheat-row code {
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 11.5px;
  color: #163B6B;
}
.cheat-tip { margin-top: 8px; padding-top: 8px; border-top: 1px solid #f1f5f9; color: #6b7280; }

.editor-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  min-height: 480px;
}

.editor-pane {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.pane-label {
  background: #f9fafb;
  padding: 8px 14px;
  font-size: 13px;
  font-weight: 600;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
}

.md-textarea {
  flex: 1;
  width: 100%;
  border: none;
  resize: none;
  padding: 14px;
  font-family: ui-monospace, "SF Mono", Consolas, monospace;
  font-size: 13.5px;
  line-height: 1.7;
  color: #1F2937;
  outline: none;
  background: #fafbfc;
}
.md-textarea:focus { background: #fff; }

.pane-foot {
  padding: 6px 14px;
  font-size: 11.5px;
  color: #94a3b8;
  background: #f9fafb;
  border-top: 1px solid #e5e7eb;
  text-align: right;
}

.preview-pane { background: #fff; }
.preview-content {
  flex: 1;
  padding: 18px 22px;
  overflow-y: auto;
  color: #1F2937;
  font-size: 14px;
  line-height: 1.8;
}
.preview-content :deep(h1) { font-size: 22px; font-weight: 700; margin: 12px 0 10px; color: #111827; }
.preview-content :deep(h2) { font-size: 18px; font-weight: 600; margin: 18px 0 8px; color: #111827; border-bottom: 1px solid #f1f5f9; padding-bottom: 4px; }
.preview-content :deep(h3) { font-size: 15px; font-weight: 600; margin: 14px 0 6px; color: #374151; }
.preview-content :deep(p) { margin: 8px 0; }
.preview-content :deep(ul), .preview-content :deep(ol) { padding-left: 24px; margin: 8px 0; }
.preview-content :deep(li) { margin: 4px 0; }
.preview-content :deep(a) { color: #163B6B; text-decoration: underline; }
.preview-content :deep(code) { background: #f1f5f9; padding: 1px 5px; border-radius: 3px; font-family: ui-monospace, monospace; font-size: 13px; }
.preview-content :deep(blockquote) {
  border-left: 3px solid #163B6B;
  padding: 6px 14px;
  margin: 10px 0;
  background: #f9fafb;
  color: #4b5563;
}
.preview-content :deep(hr) { border: none; border-top: 1px solid #e5e7eb; margin: 14px 0; }
.preview-content :deep(strong) { color: #111827; }

.preview-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #94a3b8;
  font-size: 13px;
  line-height: 1.8;
  padding: 30px;
}

@media (max-width: 1024px) {
  .editor-grid { grid-template-columns: 1fr; }
}
</style>
