import { format } from 'date-fns'

/**
 * 格式化日期
 */
export const formatDate = (date: Date | string | number, pattern = 'yyyy-MM-dd'): string => {
  return format(new Date(date), pattern)
}

/**
 * 格式化日期时间
 */
export const formatDateTime = (date: Date | string | number): string => {
  return format(new Date(date), 'yyyy-MM-dd HH:mm:ss')
}

/**
 * 格式化货币
 */
export const formatCurrency = (amount: number, currency = '¥'): string => {
  return `${currency}${amount.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })}`
}

/**
 * 格式化数字
 */
export const formatNumber = (num: number): string => {
  return num.toLocaleString('zh-CN')
}

/**
 * 格式化文件大小
 */
export const formatFileSize = (bytes: number): string => {
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  if (bytes === 0) return '0 B'
  const i = Math.floor(Math.log(bytes) / Math.log(1024))
  return `${Math.round(bytes / Math.pow(1024, i) * 100) / 100} ${sizes[i]}`
}

/**
 * 截断文本
 */
export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
} 