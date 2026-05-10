/**
 * 教员头像默认值: 没填头像时按性别 fallback
 * - 男 (gender=1) → /avatar-default-male.png
 * - 女 (gender=2) → /avatar-default-female.png
 * - 未填 → 男版默认 (中性偏向, 避免空白)
 */
const MALE = '/avatar-default-male.png'
const FEMALE = '/avatar-default-female.png'

export const tutorAvatarUrl = (tutor: any): string => {
  const a = tutor?.avatar
  if (a && typeof a === 'string' && a.trim().length > 0) return a
  return tutor?.gender === 2 ? FEMALE : MALE
}
