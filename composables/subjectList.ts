/**
 * 全站共享的科目列表（顺序与首页"分类速查"一致）。
 * - 首页 pages/index.vue 用 `SUBJECT_OBJECTS`（带 {name} 包裹）
 * - 教员注册 pages/register/teacher.vue 用 `SUBJECT_NAMES`（纯字符串）
 */
export const SUBJECT_NAMES: string[] = [
  '幼儿学前', '小学全科', '初中理科', '初中文科',
  '高中理科', '高中文科', '语文', '英语',
  '数学', '奥数', '物理', '化学',
  '生物', '历史', '地理', '政治',
  '钢琴', '小提琴', '古筝', '跳绳',
  '篮球', '游泳', '围棋', '书法',
  '美术', '英语口语', '四级', '托福',
  '雅思', 'SAT', 'AP', 'A-level',
  'IB', 'IGCSE', '日语', '德语',
  '高数', '计算机', '初中', '高中',
  '大学考研', '高考志愿填报', '生涯规划', '心理辅导'
]

export const SUBJECT_OBJECTS = SUBJECT_NAMES.map(name => ({ name }))
