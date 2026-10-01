package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.InventoryItem
import com.example.mobile_employee_simple.utils.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CargoInfo(
    val barcode: String,
    val sku: String,
    val nameZh: String,
    val nameEn: String,
    val specZh: String,
    val specEn: String,
    val locationZh: String,
    val locationEn: String,
    var quantity: Int,
    val unitZh: String,
    val unitEn: String
) {
    fun getFormattedInfo(isZh: Boolean): String {
        return if (isZh) {
            """
            【条码数据】
            $barcode

            【货物信息】
            • 货物名称: $nameZh
            • 物料编号: $sku
            • 规格型号: $specZh
            • 存放库位: $locationZh
            • 当前在库: $quantity $unitZh
            """.trimIndent()
        } else {
            """
            [Barcode]
            $barcode

            [Cargo Details]
            • Item Name: $nameEn
            • SKU Code: $sku
            • Specification: $specEn
            • Location: $locationEn
            • Current Stock: $quantity $unitEn
            """.trimIndent()
        }
    }
}

interface InventoryRepository {
    suspend fun getInventoryItems(): List<InventoryItem>
    suspend fun searchInventory(query: String): List<InventoryItem>
    suspend fun getCargoByBarcode(barcode: String): CargoInfo
    suspend fun stockIn(skuOrBarcode: String, quantity: Int): Result<CargoInfo>
    suspend fun stockOut(skuOrBarcode: String, quantity: Int): Result<CargoInfo>
}

class MockInventoryRepositoryImpl : InventoryRepository {

    private val cargoCatalog = mutableMapOf<String, CargoInfo>()

    init {
        // 初始化预置数据
        val item1 = CargoInfo("6901234567890", "SKU001", "工业精密轴承", "Industrial Precision Bearings", "6205-2RS 级高精度", "Grade 6205-2RS High Precision", "A区-01-01", "Zone A-01-01", 120, "套", "sets")
        val item2 = CargoInfo("6901234567891", "SKU002", "减速齿轮箱总成", "Speed Reducer Gearbox Assembly", "NMRV-050 标准型", "NMRV-050 Standard", "A区-01-02", "Zone A-01-02", 45, "箱", "boxes")
        val item3 = CargoInfo("6901234567892", "SKU003", "数字压力传感器", "Digital Pressure Sensor", "0-10MPa 4-20mA输出", "0-10MPa 4-20mA Output", "B区-02-01", "Zone B-02-01", 310, "件", "pcs")
        val item4 = CargoInfo("6901234567893", "SKU004", "高压液压接头", "High-Pressure Hydraulic Fitting", "M22×1.5 碳钢镀锌", "M22x1.5 Carbon Steel Zinc-Plated", "B区-02-02", "Zone B-02-02", 80, "包", "packs")
        val item5 = CargoInfo("6901234567894", "SKU005", "屏蔽电控线束", "Shielded Control Wiring Harness", "UL1007 18AWG 双绞阻燃", "UL1007 18AWG Flame Retardant", "C区-03-01", "Zone C-03-01", 200, "束", "bundles")

        listOf(item1, item2, item3, item4, item5).forEach { cargo ->
            cargoCatalog[cargo.barcode] = cargo
            cargoCatalog[cargo.sku] = cargo
            cargoCatalog[cargo.sku.lowercase()] = cargo
        }
    }

    override suspend fun getCargoByBarcode(barcode: String): CargoInfo {
        val trimmed = barcode.trim()
        cargoCatalog[trimmed]?.let { return it }

        // 智能匹配或动态生成 Mock 货物记录，以保证任何扫码输入均有完整货物详情
        val digits = trimmed.filter { it.isDigit() }
        val suffix = if (digits.isNotEmpty()) digits.takeLast(4).padStart(4, '0') else trimmed.takeLast(4).uppercase()
        val generatedSku = "SKU-M$suffix"
        val newCargo = CargoInfo(
            barcode = trimmed,
            sku = generatedSku,
            nameZh = "标准仓储通用物料 (#$suffix)",
            nameEn = "Standard Warehouse Material (#$suffix)",
            specZh = "工业通用标准件 (Grade-A)",
            specEn = "Industrial Standard Part (Grade-A)",
            locationZh = "暂存过渡区-T01",
            locationEn = "Transit Staging Zone-T01",
            quantity = 50,
            unitZh = "件",
            unitEn = "pcs"
        )
        cargoCatalog[trimmed] = newCargo
        cargoCatalog[generatedSku] = newCargo
        return newCargo
    }

    override suspend fun stockIn(skuOrBarcode: String, quantity: Int): Result<CargoInfo> {
        val cargo = getCargoByBarcode(skuOrBarcode)
        cargo.quantity += quantity
        return Result.success(cargo)
    }

    override suspend fun stockOut(skuOrBarcode: String, quantity: Int): Result<CargoInfo> {
        val cargo = getCargoByBarcode(skuOrBarcode)
        if (cargo.quantity < quantity) {
            return Result.failure(IllegalStateException("INSUFFICIENT_STOCK:${cargo.quantity}"))
        }
        cargo.quantity -= quantity
        return Result.success(cargo)
    }

    override suspend fun getInventoryItems(): List<InventoryItem> {
        val isZh = LanguageManager.isChinese()
        val nowStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // 从已注册的 cargo 中提取唯一的物料
        val uniqueCargos = cargoCatalog.values.distinctBy { it.sku }
        return uniqueCargos.mapIndexed { index, cargo ->
            InventoryItem(
                id = (index + 1).toString(),
                name = if (isZh) cargo.nameZh else cargo.nameEn,
                sku = cargo.sku,
                quantity = cargo.quantity,
                unit = if (isZh) cargo.unitZh else cargo.unitEn,
                location = if (isZh) cargo.locationZh else cargo.locationEn,
                lastUpdated = nowStr
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
