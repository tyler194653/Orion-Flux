import { createSlice, PayloadAction } from '@reduxjs/toolkit'

export interface User {
  id: string
  email: string
  name: string
  role: 'admin' | 'supplier' | 'buyer'
  companyId?: string
  companyName?: string
}

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
}

const initialState: AuthState = {
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: false,
  isLoading: true,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginStart: (state) => {
      state.isLoading = true
    },
    loginSuccess: (state, action: PayloadAction<{ user: User; token: string }>) => {
      state.user = action.payload.user
      state.token = action.payload.token
      state.isAuthenticated = true
      state.isLoading = false
      localStorage.setItem('token', action.payload.token)
    },
    loginFailure: (state) => {
      state.user = null
      state.token = null
      state.isAuthenticated = false
      state.isLoading = false
      localStorage.removeItem('token')
    },
    logout: (state) => {
      state.user = null
      state.token = null
      state.isAuthenticated = false
      state.isLoading = false
      localStorage.removeItem('token')
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload
    },
  },
})

export const { loginStart, loginSuccess, loginFailure, logout, setLoading } = authSlice.actions
export default authSlice.reducer 