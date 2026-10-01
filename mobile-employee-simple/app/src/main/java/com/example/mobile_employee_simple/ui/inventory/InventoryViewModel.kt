package com.example.mobile_employee_simple.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.model.InventoryItem
import com.example.mobile_employee_simple.data.model.InventoryOperationResult
import com.example.mobile_employee_simple.utils.LanguageManager
import kotlinx.coroutines.*

class InventoryViewModel : ViewModel() {
    
    private val repository = DataRepositoryProvider.inventoryRepository
    private val _inventoryItems = MutableLiveData<List<InventoryItem>>()
    val inventoryItems: LiveData<List<InventoryItem>> = _inventoryItems
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _scanResult = MutableLiveData<String?>()
    val scanResult: LiveData<String?> = _scanResult
    
    private val _operationResult = MutableLiveData<InventoryOperationResult?>()
    val operationResult: LiveData<InventoryOperationResult?> = _operationResult
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    fun loadInventoryData() {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                delay(300) // 模拟网络延迟
                val items = repository.getInventoryItems()
                _inventoryItems.postValue(items)
            } catch (e: Exception) {
                // 处理错误
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun refreshInventoryData() {
        loadInventoryData()
    }
    
    fun setScanResult(result: String) {
        _scanResult.value = result
    }
    
    fun processStockIn(scanData: InventoryFragment.ScanData) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                delay(300)
                val success = performStockIn(scanData)
                val isZh = LanguageManager.isChinese()
                if (success) {
                    val msg = if (isZh) "入库成功: ${scanData.productName}" else "Stock In Success: ${scanData.productName}"
                    _operationResult.postValue(InventoryOperationResult.Success(msg))
                    refreshInventoryData()
                } else {
                    val msg = if (isZh) "入库失败" else "Stock In Failed"
                    _operationResult.postValue(InventoryOperationResult.Error(msg))
                }
            } catch (e: Exception) {
                val isZh = LanguageManager.isChinese()
                val prefix = if (isZh) "入库失败" else "Stock In Error"
                _operationResult.postValue(InventoryOperationResult.Error("$prefix: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun processStockOut(scanData: InventoryFragment.ScanData) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                delay(300)
                val success = performStockOut(scanData)
                val isZh = LanguageManager.isChinese()
                if (success) {
                    val msg = if (isZh) "出库成功: ${scanData.productName}" else "Stock Out Success: ${scanData.productName}"
                    _operationResult.postValue(InventoryOperationResult.Success(msg))
                    refreshInventoryData()
                } else {
                    val msg = if (isZh) "出库失败" else "Stock Out Failed"
                    _operationResult.postValue(InventoryOperationResult.Error(msg))
                }
            } catch (e: Exception) {
                val isZh = LanguageManager.isChinese()
                val prefix = if (isZh) "出库失败" else "Stock Out Error"
                _operationResult.postValue(InventoryOperationResult.Error("$prefix: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    private suspend fun performStockIn(scanData: InventoryFragment.ScanData): Boolean {
        // TODO: 预留接入实际数据库或API
        return true
    }
    
    private suspend fun performStockOut(scanData: InventoryFragment.ScanData): Boolean {
        // TODO: 预留接入实际数据库或API
        return true
    }
    
    fun searchInventory(query: String) {
        scope.launch {
            _isLoading.postValue(true)
            try {
                val filtered = repository.searchInventory(query)
                _inventoryItems.postValue(filtered)
            } catch (e: Exception) {
                // 处理搜索错误
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun clearScanResult() {
        _scanResult.value = null
    }
    
    fun clearOperationResult() {
        _operationResult.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}