/**
 * 简单的 markdown 渲染封装. 给前端 v-html 用.
 *
 * 注意安全: marked 默认会渲染 HTML, 因此内容只能来自可信源 (admin 自己写的 sys_config) 或内置默认.
 * 不要拿用户输入直接喂. (家教站点的 admin sys_config 是可信的)
 */
import { marked } from 'marked'

export function renderMarkdown(src: string | null | undefined): string {
  if (!src) return ''
  try {
    return marked.parse(src, { breaks: true, gfm: true }) as string
  } catch (e) {
    console.warn('[useMarkdown] parse error', e)
    return ''
  }
}
