package com.example.mobile_employee_simple.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class InventoryViewModel : ViewModel() {
    
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
                delay(1000) // 模拟网络请求
                
                // 模拟库存数据
                val items = listOf(
                    InventoryItem("1", "产品A", "SKU001", 100, "个", "A区-01-01", "2024-01-15"),
                    InventoryItem("2", "产品B", "SKU002", 50, "箱", "A区-01-02", "2024-01-14"),
                    InventoryItem("3", "产品C", "SKU003", 200, "件", "B区-02-01", "2024-01-13"),
                    InventoryItem("4", "产品D", "SKU004", 75, "包", "B区-02-02", "2024-01-12"),
                    InventoryItem("5", "产品E", "SKU005", 120, "个", "C区-03-01", "2024-01-11")
                )
                
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
                delay(500) // 模拟处理延迟
                
                // TODO: 调用实际的入库API
                val success = performStockIn(scanData)
                
                if (success) {
                    _operationResult.postValue(InventoryOperationResult.Success("入库成功: ${scanData.productName}"))
                    refreshInventoryData()
                } else {
                    _operationResult.postValue(InventoryOperationResult.Error("入库失败"))
                }
                
            } catch (e: Exception) {
                _operationResult.postValue(InventoryOperationResult.Error("入库失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun processStockOut(scanData: InventoryFragment.ScanData) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                delay(500) // 模拟处理延迟
                
                // TODO: 调用实际的出库API
                val success = performStockOut(scanData)
                
                if (success) {
                    _operationResult.postValue(InventoryOperationResult.Success("出库成功: ${scanData.productName}"))
                    refreshInventoryData()
                } else {
                    _operationResult.postValue(InventoryOperationResult.Error("出库失败"))
                }
                
            } catch (e: Exception) {
                _operationResult.postValue(InventoryOperationResult.Error("出库失败: ${e.message}"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    private suspend fun performStockIn(scanData: InventoryFragment.ScanData): Boolean {
        // TODO: 实现实际的入库逻辑
        return true
    }
    
    private suspend fun performStockOut(scanData: InventoryFragment.ScanData): Boolean {
        // TODO: 实现实际的出库逻辑
        return true
    }
    
    fun searchInventory(query: String) {
        scope.launch {
            _isLoading.postValue(true)
            
            try {
                delay(500) // 模拟搜索延迟
                
                val currentItems = _inventoryItems.value ?: emptyList()
                val filteredItems = if (query.isEmpty()) {
                    currentItems
                } else {
                    currentItems.filter { item ->
                        item.name.contains(query, ignoreCase = true) ||
                        item.sku.contains(query, ignoreCase = true) ||
                        item.location.contains(query, ignoreCase = true)
                    }
                }
                
                _inventoryItems.postValue(filteredItems)
                
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