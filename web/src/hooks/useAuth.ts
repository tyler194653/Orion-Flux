import { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { RootState, AppDispatch } from '../store'
import { loginSuccess, loginFailure, setLoading } from '../store/slices/authSlice'

export const useAuth = () => {
  const dispatch = useDispatch<AppDispatch>()
  const auth = useSelector((state: RootState) => state.auth)
  const { user, token, isAuthenticated, isLoading } = auth

  useEffect(() => {
    const initAuth = async () => {
      const storedToken = localStorage.getItem('token')
      
      if (storedToken) {
        try {
          // 这里应该验证token的有效性，暂时模拟
          // const response = await api.verifyToken(storedToken)
          
          // 模拟用户数据
          const mockUser = {
            id: '1',
            email: 'admin@example.com',
            name: '管理员',
            role: 'admin' as const,
            companyId: 'company-1',
            companyName: '示例公司',
          }
          
          dispatch(loginSuccess({ user: mockUser, token: storedToken }))
        } catch (error) {
          dispatch(loginFailure())
        }
      } else {
        dispatch(setLoading(false))
      }
    }

    initAuth()
  }, [dispatch])

  return {
    user,
    token,
    isAuthenticated,
    isLoading,
  }
} 