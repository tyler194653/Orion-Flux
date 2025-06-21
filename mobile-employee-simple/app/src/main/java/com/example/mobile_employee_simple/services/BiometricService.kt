package com.example.mobile_employee_simple.services

import android.content.Context
import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BiometricService(private val context: Context) {
    
    private val biometricManager = BiometricManager.from(context)
    
    /**
     * 检查设备是否支持生物识别
     */
    fun canAuthenticate(): Boolean {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }
    
    /**
     * 显示生物识别对话框
     */
    fun showBiometricPrompt(
        activity: FragmentActivity,
        title: String = "身份验证",
        subtitle: String = "请使用指纹或面部识别进行验证",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("取消")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    /**
     * 挂起函数版本的生物识别验证
     */
    suspend fun authenticateAsync(
        activity: FragmentActivity,
        title: String = "身份验证",
        subtitle: String = "请使用指纹或面部识别进行验证"
    ): Boolean = suspendCancellableCoroutine { continuation ->
        showBiometricPrompt(
            activity = activity,
            title = title,
            subtitle = subtitle,
            onSuccess = {
                continuation.resume(true)
            },
            onError = { error ->
                continuation.resume(false)
            },
            onFailed = {
                continuation.resume(false)
            }
        )
    }
} 