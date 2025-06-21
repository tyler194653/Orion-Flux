package com.example.mobile_employee_simple.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SettingsViewModel : ViewModel() {
    
    private lateinit var prefs: SharedPreferences
    
    private val _biometricEnabled = MutableLiveData<Boolean>()
    val biometricEnabled: LiveData<Boolean> = _biometricEnabled
    
    private val _notificationsEnabled = MutableLiveData<Boolean>()
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled
    
    private val _autoSyncEnabled = MutableLiveData<Boolean>()
    val autoSyncEnabled: LiveData<Boolean> = _autoSyncEnabled
    
    private val _offlineModeEnabled = MutableLiveData<Boolean>()
    val offlineModeEnabled: LiveData<Boolean> = _offlineModeEnabled
    
    private val _lastSyncTime = MutableLiveData<String>()
    val lastSyncTime: LiveData<String> = _lastSyncTime
    
    private val _cacheSize = MutableLiveData<String>()
    val cacheSize: LiveData<String> = _cacheSize
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        loadSettings()
    }
    
    private fun loadSettings() {
        _biometricEnabled.value = getBiometricEnabled()
        _notificationsEnabled.value = getNotificationsEnabled()
        _autoSyncEnabled.value = getAutoSyncEnabled()
        _offlineModeEnabled.value = getOfflineModeEnabled()
        updateCacheSize()
    }
    
    fun setBiometricEnabled(enabled: Boolean) {
        _biometricEnabled.value = enabled
        prefs.edit().putBoolean("biometric_enabled", enabled).apply()
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
    
    fun setAutoSyncEnabled(enabled: Boolean) {
        _autoSyncEnabled.value = enabled
        prefs.edit().putBoolean("auto_sync_enabled", enabled).apply()
    }
    
    fun setOfflineModeEnabled(enabled: Boolean) {
        _offlineModeEnabled.value = enabled
        prefs.edit().putBoolean("offline_mode_enabled", enabled).apply()
    }
    
    fun getBiometricEnabled(): Boolean {
        return prefs.getBoolean("biometric_enabled", false)
    }
    
    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", true)
    }
    
    fun getAutoSyncEnabled(): Boolean {
        return prefs.getBoolean("auto_sync_enabled", true)
    }
    
    fun getOfflineModeEnabled(): Boolean {
        return prefs.getBoolean("offline_mode_enabled", false)
    }
    
    fun updateLastSyncTime(time: Long) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timeString = dateFormat.format(Date(time))
        _lastSyncTime.value = timeString
        prefs.edit().putLong("last_sync_time", time).apply()
    }
    
    fun getLastSyncTime(): Long {
        return prefs.getLong("last_sync_time", 0)
    }
    
    fun updateCacheSize() {
        val cacheSize = calculateCacheSize()
        _cacheSize.value = formatFileSize(cacheSize)
    }
    
    fun getCacheSize(): String {
        return _cacheSize.value ?: "0 MB"
    }
    
    fun clearCache() {
        // 清除应用缓存目录
        clearCacheDirectory()
        updateCacheSize()
    }
    
    private fun clearCacheDirectory() {
        // 这里应该清除应用的缓存目录
        // 由于ViewModel无法直接访问Context，这个功能应该在Fragment中实现
    }
    
    private fun calculateCacheSize(): Long {
        // 这里应该计算缓存目录的大小
        // 由于ViewModel无法直接访问Context，这个功能应该在Fragment中实现
        return 0L
    }
    
    private fun formatFileSize(size: Long): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> "${size / (1024 * 1024 * 1024)} GB"
        }
    }
    
    fun resetToDefaults() {
        setBiometricEnabled(false)
        setNotificationsEnabled(true)
        setAutoSyncEnabled(true)
        setOfflineModeEnabled(false)
        loadSettings()
    }
    
    fun exportSettings(): String {
        return """
            {
                "biometric_enabled": ${getBiometricEnabled()},
                "notifications_enabled": ${getNotificationsEnabled()},
                "auto_sync_enabled": ${getAutoSyncEnabled()},
                "offline_mode_enabled": ${getOfflineModeEnabled()},
                "last_sync_time": ${getLastSyncTime()}
            }
        """.trimIndent()
    }
    
    fun importSettings(settingsJson: String) {
        try {
            // 这里应该解析JSON并应用设置
            // 为了简化，这里只是示例
            // 实际实现需要JSON解析
        } catch (e: Exception) {
            // 处理导入错误
        }
    }
} 