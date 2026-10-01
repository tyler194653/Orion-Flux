package com.example.mobile_employee_simple.data.model

data class InventoryItem(
    val id: String,
    val name: String,
    val sku: String,
    val quantity: Int,
    val unit: String,
    val location: String = "",
    val lastUpdated: String = ""
)

sealed class InventoryOperationResult {
    data class Success(val message: String) : InventoryOperationResult()
    data class Error(val message: String) : InventoryOperationResult()
}
