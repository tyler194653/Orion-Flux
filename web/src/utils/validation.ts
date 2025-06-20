/**
 * 验证邮箱格式
 */
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

/**
 * 验证手机号格式
 */
export const isValidPhone = (phone: string): boolean => {
  const phoneRegex = /^1[3-9]\d{9}$/
  return phoneRegex.test(phone)
}

/**
 * 验证密码强度
 */
export const isStrongPassword = (password: string): boolean => {
  // 至少8位，包含大小写字母、数字
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/
  return passwordRegex.test(password)
}

/**
 * 验证身份证号
 */
export const isValidIdCard = (idCard: string): boolean => {
  const idCardRegex = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
  return idCardRegex.test(idCard)
}

/**
 * 验证URL格式
 */
export const isValidUrl = (url: string): boolean => {
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

/**
 * 验证是否为数字
 */
export const isNumber = (value: string): boolean => {
  return !isNaN(Number(value)) && !isNaN(parseFloat(value))
}

/**
 * 验证是否为正整数
 */
export const isPositiveInteger = (value: string | number): boolean => {
  const num = typeof value === 'string' ? parseInt(value) : value
  return Number.isInteger(num) && num > 0
}

/**
 * 验证是否为正数（包含小数）
 */
export const isPositiveNumber = (value: string | number): boolean => {
  const num = typeof value === 'string' ? parseFloat(value) : value
  return !isNaN(num) && num > 0
} 