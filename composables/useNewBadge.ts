/**
 * "新" 标判定: 时间戳是否在过去 N 天内.
 * 用于公告等列表项展示"新"角标, 自动过期.
 *
 * @param timestamp - 时间戳 (ISO string / number / Date)
 * @param days - 多少天内算新
 */
export const isWithinDays = (timestamp: string | number | Date | null | undefined, days: number): boolean => {
  if (!timestamp) return false
  const t = timestamp instanceof Date ? timestamp.getTime() :
            typeof timestamp === 'string' ? new Date(timestamp).getTime() :
            Number(timestamp)
  if (isNaN(t)) return false
  return (Date.now() - t) < days * 24 * 60 * 60 * 1000
}
