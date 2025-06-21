package com.example.mobile_employee_simple.services

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SyncService(private val context: Context) {
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val prefs: SharedPreferences = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()
    
    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()
    
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
            if (_syncState.value is SyncState.WaitingForNetwork) {
                startSync()
            }
        }
        
        override fun onLost(network: Network) {
            _isOnline.value = false
        }
    }
    
    init {
        registerNetworkCallback()
        checkInitialNetworkState()
    }
    
    /**
     * 注册网络回调
     */
    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
    
    /**
     * 检查初始网络状态
     */
    private fun checkInitialNetworkState() {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        _isOnline.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    
    /**
     * 开始同步
     */
    fun startSync() {
        if (!_isOnline.value) {
            _syncState.value = SyncState.WaitingForNetwork
            return
        }
        
        scope.launch {
            try {
                _syncState.value = SyncState.Syncing(0)
                
                // 同步任务数据
                syncTasks()
                _syncState.value = SyncState.Syncing(25)
                
                // 同步库存数据
                syncInventory()
                _syncState.value = SyncState.Syncing(50)
                
                // 同步产品数据
                syncProducts()
                _syncState.value = SyncState.Syncing(75)
                
                // 同步用户数据
                syncUserData()
                _syncState.value = SyncState.Syncing(100)
                
                // 更新最后同步时间
                updateLastSyncTime()
                
                _syncState.value = SyncState.Success
                
                // 延迟重置状态
                delay(2000)
                _syncState.value = SyncState.Idle
                
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "同步失败")
                
                // 延迟重置状态
                delay(3000)
                _syncState.value = SyncState.Idle
            }
        }
    }
    
    /**
     * 同步任务数据
     */
    private suspend fun syncTasks() {
        delay(500) // 模拟网络请求
        // TODO: 实现任务数据同步
    }
    
    /**
     * 同步库存数据
     */
    private suspend fun syncInventory() {
        delay(500) // 模拟网络请求
        // TODO: 实现库存数据同步
    }
    
    /**
     * 同步产品数据
     */
    private suspend fun syncProducts() {
        delay(500) // 模拟网络请求
        // TODO: 实现产品数据同步
    }
    
    /**
     * 同步用户数据
     */
    private suspend fun syncUserData() {
        delay(500) // 模拟网络请求
        // TODO: 实现用户数据同步
    }
    
    /**
     * 更新最后同步时间
     */
    private fun updateLastSyncTime() {
        val currentTime = System.currentTimeMillis()
        prefs.edit().putLong("last_sync_time", currentTime).apply()
    }
    
    /**
     * 获取最后同步时间
     */
    fun getLastSyncTime(): Long {
        return prefs.getLong("last_sync_time", 0)
    }
    
    /**
     * 格式化最后同步时间
     */
    fun getLastSyncTimeString(): String {
        val lastSyncTime = getLastSyncTime()
        if (lastSyncTime == 0L) {
            return "从未同步"
        }
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(lastSyncTime))
    }
    
    /**
     * 保存离线数据
     */
    fun saveOfflineData(key: String, data: String) {
        val offlineDir = File(context.filesDir, "offline_data")
        if (!offlineDir.exists()) {
            offlineDir.mkdirs()
        }
        
        val file = File(offlineDir, "$key.json")
        file.writeText(data)
    }
    
    /**
     * 读取离线数据
     */
    fun readOfflineData(key: String): String? {
        val file = File(context.filesDir, "offline_data/$key.json")
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
    
    /**
     * 清除离线数据
     */
    fun clearOfflineData(key: String) {
        val file = File(context.filesDir, "offline_data/$key.json")
        if (file.exists()) {
            file.delete()
        }
    }
    
    /**
     * 获取所有离线数据文件
     */
    fun getOfflineDataFiles(): List<File> {
        val offlineDir = File(context.filesDir, "offline_data")
        return if (offlineDir.exists()) {
            offlineDir.listFiles()?.filter { it.extension == "json" } ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    /**
     * 检查是否有待同步的数据
     */
    fun hasPendingSyncData(): Boolean {
        return getOfflineDataFiles().isNotEmpty()
    }
    
    /**
     * 强制同步
     */
    fun forceSync() {
        scope.launch {
            try {
                _syncState.value = SyncState.Syncing(0)
                
                // 同步所有离线数据
                val offlineFiles = getOfflineDataFiles()
                offlineFiles.forEachIndexed { index, file ->
                    val data = file.readText()
                    val key = file.nameWithoutExtension
                    
                    // TODO: 上传离线数据到服务器
                    delay(200) // 模拟上传
                    
                    val progress = ((index + 1) * 100) / offlineFiles.size
                    _syncState.value = SyncState.Syncing(progress)
                }
                
                // 清除已同步的离线数据
                offlineFiles.forEach { it.delete() }
                
                updateLastSyncTime()
                _syncState.value = SyncState.Success
                
                delay(2000)
                _syncState.value = SyncState.Idle
                
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "强制同步失败")
                delay(3000)
                _syncState.value = SyncState.Idle
            }
        }
    }
    
    /**
     * 清理资源
     */
    fun cleanup() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        scope.cancel()
    }
}

sealed class SyncState {
    object Idle : SyncState()
    object WaitingForNetwork : SyncState()
    data class Syncing(val progress: Int) : SyncState()
    object Success : SyncState()
    data class Error(val message: String) : SyncState()
} 