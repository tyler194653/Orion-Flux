package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.ApprovalItem
import com.example.mobile_employee_simple.data.model.ApprovalStatus
import com.example.mobile_employee_simple.utils.LanguageManager

interface ApprovalsRepository {
    suspend fun getApprovals(): List<ApprovalItem>
    suspend fun updateStatus(id: String, status: ApprovalStatus): Boolean
}

class MockApprovalsRepositoryImpl : ApprovalsRepository {
    private val cachedStatuses = mutableMapOf<String, ApprovalStatus>()

    override suspend fun getApprovals(): List<ApprovalItem> {
        val isZh = LanguageManager.isChinese()
        val items = if (isZh) {
            listOf(
                ApprovalItem(
                    id = "PR-2026-088",
                    title = "紧急采购补货申请",
                    meta = "仓储一部 · 提交于 2026-09-30",
                    desc = "申请紧急补充 SKU002 (减速齿轮箱总成) 50 箱，预算 ¥24,500。",
                    status = ApprovalStatus.PENDING
                ),
                ApprovalItem(
                    id = "SO-2026-042",
                    title = "货损出库核销复核",
                    meta = "质检中心 · 提交于 2026-09-30",
                    desc = "申请复核并下架 A区-01 货架受潮纸箱包装配件 2 件，作退厂返工处理。",
                    status = ApprovalStatus.PENDING
                ),
                ApprovalItem(
                    id = "TR-2026-015",
                    title = "跨库精密轴承调拨",
                    meta = "调度中心 · 提交于 2026-09-29",
                    desc = "从中央一号库向分拨二号库调配 100 套工业精密轴承，支援产线急需。",
                    status = ApprovalStatus.IN_PROGRESS
                ),
                ApprovalItem(
                    id = "MT-2026-003",
                    title = "输送机易损备件申领",
                    meta = "运维工程部 · 提交于 2026-09-28",
                    desc = "申领高压液压接头 10 包用于 3 号传送辊道日常维保保养。",
                    status = ApprovalStatus.COMPLETED
                )
            )
        } else {
            listOf(
                ApprovalItem(
                    id = "PR-2026-088",
                    title = "Urgent Procurement Replenishment",
                    meta = "Warehouse Dept 1 · Submitted 2026-09-30",
                    desc = "Urgent replenishment for SKU002 (Gearbox Assembly) 50 boxes, budget ¥24,500.",
                    status = ApprovalStatus.PENDING
                ),
                ApprovalItem(
                    id = "SO-2026-042",
                    title = "Damaged Goods Write-Off Review",
                    meta = "Quality Inspection · Submitted 2026-09-30",
                    desc = "Review and delist 2 damaged carton-packaged parts in Shelf A-01 for factory rework.",
                    status = ApprovalStatus.PENDING
                ),
                ApprovalItem(
                    id = "TR-2026-015",
                    title = "Inter-warehouse Bearings Transfer",
                    meta = "Dispatch Center · Submitted 2026-09-29",
                    desc = "Transfer 100 sets of precision bearings from Central Warehouse #1 to Hub #2.",
                    status = ApprovalStatus.IN_PROGRESS
                ),
                ApprovalItem(
                    id = "MT-2026-003",
                    title = "Conveyor Wear Parts Requisition",
                    meta = "Maintenance Dept · Submitted 2026-09-28",
                    desc = "Request 10 packs of high-pressure hydraulic fittings for Conveyor Roller #3 routine maintenance.",
                    status = ApprovalStatus.COMPLETED
                )
            )
        }

        return items.map { item ->
            val cached = cachedStatuses[item.id]
            if (cached != null) item.copy(status = cached) else item
        }
    }

    override suspend fun updateStatus(id: String, status: ApprovalStatus): Boolean {
        cachedStatuses[id] = status
        return true
    }
}
