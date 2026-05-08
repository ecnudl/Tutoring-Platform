/**
 * 全站共享的科目列表 — 与后端 dict_subject 表的新 30 项 (id 3000~3029) 完全对齐。
 * 顺序参考 /xy 学员需求库 chip 截图: 全科 / 语文 / 数学 / 英语 / ... / 乒乓球.
 *
 * - 首页 pages/index.vue 用 `SUBJECT_OBJECTS` ({name} 包裹)
 * - 教员注册 pages/register/teacher.vue 用 `SUBJECT_NAMES` (纯字符串)
 * - 教员库 pages/jy/index.vue chip 用 `SUBJECT_OBJECTS`
 * - 学员库 pages/xy/index.vue chip 用 `SUBJECT_OBJECTS`
 *
 * 后端 dict_subject 已用 status_id 软删旧 83 项, 仅 status_id=1 的 30 项有效;
 * dictStore.subjects (从 API 拉) 也会跟着自动一致.
 */
export const SUBJECT_NAMES: string[] = [
  '全科', '语文', '数学', '英语', '英语口语',
  '物理', '化学', '历史', '地理', '生物',
  '奥数', '高等数学',
  '钢琴', '小提琴', '古筝', '声乐', '二胡',
  '美术', '书法',
  '雅思', '托福', 'GRE', 'IB', 'A-level', 'IGCSE', '英语四级',
  '围棋', '游泳', '跆拳道', '乒乓球'
]

export const SUBJECT_OBJECTS = SUBJECT_NAMES.map(name => ({ name }))
