package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.InventoryItem
import com.example.mobile_employee_simple.utils.LanguageManager

interface InventoryRepository {
    suspend fun getInventoryItems(): List<InventoryItem>
    suspend fun searchInventory(query: String): List<InventoryItem>
}

class MockInventoryRepositoryImpl : InventoryRepository {
    override suspend fun getInventoryItems(): List<InventoryItem> {
        val isZh = LanguageManager.isChinese()
        return if (isZh) {
            listOf(
                InventoryItem("1", "工业精密轴承", "SKU001", 120, "套", "A区-01-01", "2026-09-30"),
                InventoryItem("2", "减速齿轮箱总成", "SKU002", 45, "箱", "A区-01-02", "2026-09-30"),
                InventoryItem("3", "数字压力传感器", "SKU003", 310, "件", "B区-02-01", "2026-09-29"),
                InventoryItem("4", "高压液压接头", "SKU004", 80, "包", "B区-02-02", "2026-09-28"),
                InventoryItem("5", "屏蔽电控线束", "SKU005", 200, "束", "C区-03-01", "2026-09-28")
            )
        } else {
            listOf(
                InventoryItem("1", "Industrial Precision Bearings", "SKU001", 120, "sets", "Zone A-01-01", "2026-09-30"),
                InventoryItem("2", "Speed Reducer Gearbox Assembly", "SKU002", 45, "boxes", "Zone A-01-02", "2026-09-30"),
                InventoryItem("3", "Digital Pressure Sensor", "SKU003", 310, "pcs", "Zone B-02-01", "2026-09-29"),
                InventoryItem("4", "High-Pressure Hydraulic Fitting", "SKU004", 80, "packs", "Zone B-02-02", "2026-09-28"),
                InventoryItem("5", "Shielded Control Wiring Harness", "SKU005", 200, "bundles", "Zone C-03-01", "2026-09-28")
            )
        }
    }

    override suspend fun searchInventory(query: String): List<InventoryItem> {
        val items = getInventoryItems()
        if (query.isBlank()) return items
        return items.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.sku.contains(query, ignoreCase = true) ||
            it.location.contains(query, ignoreCase = true)
        }
    }
}
