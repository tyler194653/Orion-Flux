package com.example.mobile_employee_simple.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class LoginViewModel(private val app: Application) : ViewModel() {
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val TEST_USER = "test"
        private const val TEST_PASS = "123456"

        fun isLoggedIn(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean(KEY_LOGGED_IN, false)
        }
        fun logout(context: Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }
        fun getCurrentUser(context: Context): String? {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getString(KEY_USERNAME, null)
        }
        fun saveTestAccount(context: Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            if (!prefs.contains(KEY_USERNAME)) {
                prefs.edit().putString(KEY_USERNAME, TEST_USER).apply()
            }
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    fun login(username: String, password: String) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                // 模拟网络请求延迟
                delay(1000)
                
                // TODO: 调用实际的登录API
                val success = validateCredentials(username, password)
                
                if (success) {
                    saveLoginState(username)
                    _loginResult.postValue(LoginResult.Success)
                } else {
                    _loginResult.postValue(LoginResult.Error("用户名或密码错误"))
                }
                
            } catch (e: Exception) {
                _loginResult.postValue(LoginResult.Error("登录失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun loginWithBiometric() {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                // 模拟生物识别验证延迟
                delay(500)
                
                // TODO: 验证生物识别并获取用户信息
                // 直接用测试账户登录
                saveLoginState(TEST_USER)
                _loginResult.postValue(LoginResult.Success)
                
            } catch (e: Exception) {
                _loginResult.postValue(LoginResult.Error("生物识别登录失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun loginWithQRCode(qrData: String) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                // 模拟扫码登录延迟
                delay(800)
                
                // TODO: 解析二维码数据并验证
                val success = validateQRCode(qrData)
                
                if (success) {
                    _loginResult.postValue(LoginResult.Success)
                } else {
                    _loginResult.postValue(LoginResult.Error("二维码无效或已过期"))
                }
                
            } catch (e: Exception) {
                _loginResult.postValue(LoginResult.Error("扫码登录失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun loginWithEmployeeId(employeeId: String) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                // 模拟工号登录延迟
                delay(600)
                
                // TODO: 验证工号
                val success = validateEmployeeId(employeeId)
                
                if (success) {
                    _loginResult.postValue(LoginResult.Success)
                } else {
                    _loginResult.postValue(LoginResult.Error("工号无效或权限不足"))
                }
                
            } catch (e: Exception) {
                _loginResult.postValue(LoginResult.Error("工号登录失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    private fun validateCredentials(username: String, password: String): Boolean {
        return username == TEST_USER && password == TEST_PASS
    }
    
    private fun validateQRCode(qrData: String): Boolean {
        // TODO: 实现二维码验证逻辑
        return qrData.startsWith("LOGIN:")
    }
    
    private fun validateEmployeeId(employeeId: String): Boolean {
        // TODO: 实现工号验证逻辑
        return employeeId.matches(Regex("^EMP\\d{6}$"))
    }
    
    private fun saveLoginState(username: String) {
        val prefs = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LOGGED_IN, true).putString(KEY_USERNAME, username).apply()
    }
    
    fun clearLoginResult() {
        _loginResult.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
} 