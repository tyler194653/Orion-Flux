package com.example.mobile_employee_simple.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobile_employee_simple.ui.inventory.InventoryItem

class GalleryViewModel : ViewModel() {

    private val _products = MutableLiveData<List<InventoryItem>>().apply {
        value = listOf(
            InventoryItem("1", "工业精密轴承", "SKU001", 120, "个", "A区-01-01", "2024-01-15"),
            InventoryItem("2", "减速齿轮箱总成", "SKU002", 45, "箱", "A区-01-02", "2024-01-14"),
            InventoryItem("3", "数字压力传感器", "SKU003", 310, "件", "B区-02-01", "2024-01-13"),
            InventoryItem("4", "高压液压接头", "SKU004", 80, "包", "B区-02-02", "2024-01-12"),
            InventoryItem("5", "屏蔽电控线束", "SKU005", 200, "束", "C区-03-01", "2024-01-11")
        )
    }
    val products: LiveData<List<InventoryItem>> = _products
}