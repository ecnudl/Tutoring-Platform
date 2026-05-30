/**
 * 页面内容渲染封装. 给前端 v-html 用.
 *
 * 内容来源两种:
 *  1) admin 后台「页面内容管理」用富文本编辑器填写 → sys_config 里存的是 HTML;
 *  2) 该 key 没被 admin 改过 → 前端用内置默认文案, 是 markdown。
 * 所以这里要兼容: 已是 HTML 的直接用, 是 markdown 的用 marked 转 HTML。
 *
 * 注意安全: 只渲染可信源 (admin sys_config / 代码内置默认), 不要直接喂用户输入。
 */
import { marked } from 'marked'

// 富文本编辑器输出的就是 HTML(以 "<标签" 开头, 含注释 <!--); markdown 则以 #/-/数字/文字/> 等开头。
// 用 "trim 后以 < + 字母或 ! 开头" 判断是否已是 HTML, 比枚举标签白名单更稳。
const HTML_LEADING = /^<[a-z!]/i

export function renderMarkdown(src: string | null | undefined): string {
  if (!src) return ''
  try {
    const t = src.trim()
    if (HTML_LEADING.test(t)) return t // 富文本 HTML, 直接用
    return marked.parse(src, { breaks: true, gfm: true }) as string // markdown (内置默认 / 老数据)
  } catch (e) {
    console.warn('[useMarkdown] parse error', e)
    return ''
  }
}
