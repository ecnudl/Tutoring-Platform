<template>
  <div class="page-container">
    <h2 class="page-title">页面内容管理</h2>
    <p class="page-tip">
      管理"请家教流程 / 怎样找到老师 / 成为家教老师 / 公司简介..." 等长文本页面。
      <strong>像 Word 一样编辑</strong>：选中文字点工具栏即可加粗 / 改字号 / 换颜色 / 列表 / 插入图片，不用懂代码。
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
      <el-button :icon="RefreshRight" @click="reloadCurrent">放弃修改 / 重新加载</el-button>
      <el-popconfirm title="清空当前页面 admin 自定义内容? 前端将回到代码内置的默认文案。" @confirm="restoreDefault" confirm-button-text="确认清空" cancel-button-text="取消">
        <template #reference>
          <el-button :icon="Delete" type="danger" plain>恢复默认</el-button>
        </template>
      </el-popconfirm>
      <span v-if="dirty" class="dirty-tag">有未保存的修改</span>
    </div>

    <div class="editor-card" v-loading="loading">
      <Toolbar :editor="editorRef" :defaultConfig="toolbarConfig" mode="default" class="pc-toolbar" />
      <Editor :defaultConfig="editorConfig" mode="default" class="pc-body" @onCreated="handleCreated" @onChange="onEditorChange" />
    </div>
    <div class="editor-foot">保存后前端立即生效；当前页面无自定义内容时，前端展示的是代码内置的默认文案。</div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, shallowRef, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { Check, RefreshRight, Delete, View } from '@element-plus/icons-vue'
import axios from 'axios'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { marked } from 'marked'
import { post, get } from '@/api/index'
import { HELP_ARTICLES_MD } from '@/composables/helpArticlesMd'

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
let prevKey = PAGES[0].key
const allConfig = ref<Record<string, string>>({})
const loading = ref(false)
const saving = ref(false)
const dirty = ref(false)

const currentPage = computed(() => PAGES.find(p => p.key === currentKey.value))

// ===== wangEditor 富文本 (所见即所得, 像 Word) =====
const editorRef = shallowRef<any>()
const toolbarConfig: any = {}
// 当前载入编辑器的(规范化后)HTML, 用来判断是否真的改动过
let loadedHtml = ''
const editorConfig: any = {
  placeholder: '在这里编辑页面内容，可加粗 / 改字号 / 列表 / 标题 / 插入图片…',
  MENU_CONF: {
    // 图片上传 — 复用后台现成接口, 存 minio
    uploadImage: {
      async customUpload(file: File, insertFn: (url: string) => void) {
        const token = localStorage.getItem('admin_token') || ''
        if (!token) { ElMessage.error('登录已失效，请重新登录后再上传图片'); return }
        const fd = new FormData()
        fd.append('picFile', file)
        try {
          const res = await axios.post('/system/admin/upload/pic', fd, {
            headers: { 'Content-Type': 'multipart/form-data', token }
          })
          const url = res.data?.data
          if (res.data?.code === 200 && typeof url === 'string' && url) insertFn(url)
          else ElMessage.error(res.data?.msg || '图片上传失败')
        } catch (e) {
          console.error('[pagecontent image upload]', e)
          ElMessage.error('图片上传失败，请重试')
        }
      }
    }
  }
}

const handleCreated = (editor: any) => {
  editorRef.value = editor
  // 编辑器就绪后载入当前页内容(此时 allConfig 可能还没回来, load() 拿到后会再设一次)
  setEditorHtml(currentHtmlForKey(currentKey.value))
}
onBeforeUnmount(() => { editorRef.value?.destroy(); editorRef.value = null })

const onEditorChange = (editor: any) => {
  // 与载入时的内容比对, 真正改动过才算 dirty (避免 setHtml 误判)
  dirty.value = editor.getHtml() !== loadedHtml
}

// markdown 或 HTML 一律转成 HTML 给富文本编辑器用
const toHtml = (src: string): string => {
  const t = (src || '').trim()
  if (!t) return ''
  // 富文本存的就是 HTML(以块级标签开头), 直接用; 否则按 markdown 解析(兼容内置默认 + 老数据)
  if (/^<(p|h[1-6]|ul|ol|div|blockquote|table|img|hr|strong|em|span|a|br)[\s>/]/i.test(t)) return t
  try {
    return marked.parse(t, { breaks: true, gfm: true }) as string
  } catch (e) {
    console.warn('[pagecontent] markdown parse error', e)
    return t
  }
}
const currentHtmlForKey = (key: string): string => {
  const adminVal = (allConfig.value[key] || '').trim()
  return toHtml(adminVal || HELP_ARTICLES_MD[key] || '')
}
const setEditorHtml = (html: string) => {
  const e = editorRef.value
  if (!e) return
  e.setHtml(html || '<p><br></p>')
  loadedHtml = e.getHtml()
  dirty.value = false
}

const load = async () => {
  loading.value = true
  try {
    const res = await get('/system/api/site/config')
    if (res?.code === 200 && res.data) {
      allConfig.value = res.data
      setEditorHtml(currentHtmlForKey(currentKey.value))
    }
  } catch (e) { console.error(e); ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const onChangePage = () => {
  if (dirty.value && !confirm('当前页面有未保存修改, 切换页面会丢失。确认切换?')) {
    currentKey.value = prevKey // 撤回切换
    return
  }
  prevKey = currentKey.value
  setEditorHtml(currentHtmlForKey(currentKey.value))
}

const reloadCurrent = () => { load() }

const saveAll = async () => {
  if (!currentPage.value || !editorRef.value) return
  saving.value = true
  try {
    const html = editorRef.value.getHtml() || ''
    const res = await post('/system/admin/sys/config/save-by-key', {
      configKey: currentKey.value,
      configValue: html,
      configName: '页面内容 - ' + currentPage.value.label
    })
    if (res?.code === 200) {
      ElMessage.success(`已保存「${currentPage.value.label}」, 前端立即生效`)
      allConfig.value[currentKey.value] = html
      loadedHtml = html
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
      ElMessage.success('已清空, 前端恢复内置默认文案')
      allConfig.value[currentKey.value] = ''
      setEditorHtml(currentHtmlForKey(currentKey.value)) // 载入内置默认(转 HTML)
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
.toolbar { display: flex; gap: 8px; align-items: center; margin-bottom: 12px; flex-wrap: wrap; }
.dirty-tag { color: #d97706; font-size: 13px; margin-left: 4px; }

.editor-card { border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden; background: #fff; }
.pc-toolbar { border-bottom: 1px solid #e5e7eb; }
.pc-body { height: 520px; overflow-y: auto; }
.editor-foot { margin-top: 8px; font-size: 12px; color: #94a3b8; text-align: right; }
</style>
<style>
/* wangEditor 下拉面板 / 弹窗 / 全屏容器层级 */
.w-e-drop-panel,
.w-e-select-list,
.w-e-modal { z-index: 9999 !important; }
.w-e-full-screen-container { z-index: 3000 !important; }
</style>
