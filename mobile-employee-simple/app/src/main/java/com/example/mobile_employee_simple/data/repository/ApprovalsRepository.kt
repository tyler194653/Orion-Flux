package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.ApprovalItem
import com.example.mobile_employee_simple.data.model.ApprovalStatus
import com.example.mobile_employee_simple.utils.LanguageManager

interface ApprovalsRepository {
    suspend fun getApprovals(): List<ApprovalItem>
    suspend fun updateStatus(id: String, status: ApprovalStatus): Boolean
    suspend fun withdrawApproval(id: String): Boolean
    suspend fun submitApproval(item: ApprovalItem): Boolean
}

class MockApprovalsRepositoryImpl : ApprovalsRepository {
    private val cachedStatuses = mutableMapOf<String, ApprovalStatus>()
    private val userSubmittedItems = mutableListOf<ApprovalItem>()

    override suspend fun getApprovals(): List<ApprovalItem> {
        val isZh = LanguageManager.isChinese()
        val defaultItems = if (isZh) {
            listOf(
                ApprovalItem(
                    id = "REQ-2026-101",
                    title = "工器具与防护耗材申领",
                    meta = "物资管理 · 提交于 2026-10-01 09:30",
                    desc = "申领手持工业扫码枪 1 台、防静电劳保手套 2 双，用于 A2 货区扫码盘点作业。",
                    status = ApprovalStatus.PENDING,
                    category = "物资申领",
                    details = "【申请单号】REQ-2026-101\n【申请员工】test (工号 EMP202609)\n【申请类别】物资申领\n【物资明细】\n• 斑马手持工业扫码枪 TC26 × 1台\n• 防静电透气防护手套 × 2双\n【用途说明】A2 货架全流程扫码作业，现有设备电量老化频繁断联\n【审核主管】仓储一部·张明主管\n【当前节点】等待直属主管审批",
                    approver = "张明主管 (仓储一部)",
                    timeline = "10-01 09:30 员工提交申请 ➔ 待直属主管审批"
                ),
                ApprovalItem(
                    id = "REQ-2026-098",
                    title = "月末大盘点延时加班调休申请",
                    meta = "人事考勤 · 提交于 2026-09-30 18:20",
                    desc = "9月29日配合三号库季度盘点延时作业 4.0 小时，申请折算存入个人调休假期账户。",
                    status = ApprovalStatus.IN_PROGRESS,
                    category = "考勤加班",
                    details = "【申请单号】REQ-2026-098\n【申请员工】test (工号 EMP202609)\n【申请类别】加班调休\n【加班时段】2026-09-29 18:00 - 22:00 (共计 4.0 小时)\n【折算方案】存入调休假余额 (1:1 调休)\n【审批链条】直属主管初审同意 ➔ 人事部门备案中\n【当前节点】人事考勤专员核对打卡工时中",
                    approver = "陈婷 (HR 考勤专员)",
                    timeline = "09-30 18:20 员工提交 ➔ 09-30 20:15 主管初审通过 ➔ 人事部门备案中"
                ),
                ApprovalItem(
                    id = "REQ-2026-085",
                    title = "进料上架外箱微损异常报备",
                    meta = "作业报备 · 提交于 2026-09-28 14:15",
                    desc = "卸货复核时发现 SKU002 外包装角部挤压变形，内部总成无损，已贴黄色隔离标并留存现场照片。",
                    status = ApprovalStatus.COMPLETED,
                    category = "作业异常",
                    details = "【申请单号】REQ-2026-085\n【申请员工】test (工号 EMP202609)\n【申请类别】异常报备\n【异常物料】SKU002 减速齿轮箱总成 (批次 B2609)\n【处理结论】质检复检合格，重新加固封装贴标入库\n【审核人员】质检中心·李工\n【最终结果】已批准并归档备案",
                    approver = "李工 (质检中心)",
                    timeline = "09-28 14:15 发起报备 ➔ 09-28 15:30 质检到场复核 ➔ 09-28 16:00 已归档完成"
                ),
                ApprovalItem(
                    id = "REQ-2026-072",
                    title = "跨厂区应急调料交通费报销",
                    meta = "财务报销 · 提交于 2026-09-26 11:00",
                    desc = "支援二号厂区突发总成装配缺料，紧急打车跨库调送取件发票报销 ¥42.00。",
                    status = ApprovalStatus.COMPLETED,
                    category = "财务报销",
                    details = "【申请单号】REQ-2026-072\n【申请员工】test (工号 EMP202609)\n【申请类别】差旅报销\n【报销金额】¥42.00 (电子出租车发票已上传至 ERP)\n【审核财务】财务中心·王会计\n【打款进度】审核通过，随本月工资单发放",
                    approver = "王会计 (财务中心)",
                    timeline = "09-26 11:00 发起报销 ➔ 09-27 10:20 财务审核通过 ➔ 待打款发放"
                ),
                ApprovalItem(
                    id = "REQ-2026-055",
                    title = "特种作业证复审调班申请",
                    meta = "排班调度 · 提交于 2026-09-24 16:45",
                    desc = "因参加安监局叉车操作证复审培训，申请将 10月5日早班与同事王磊对调。",
                    status = ApprovalStatus.WITHDRAWN,
                    category = "排班调度",
                    details = "【申请单号】REQ-2026-055\n【申请员工】test (工号 EMP202609)\n【申请类别】排班对调\n【对调班次】10月5日早班 (08:00 - 16:30) ➔ 调至 10月8日\n【对调员工】王磊 (EMP202615) 双方已确认\n【撤回说明】申请人于 09-25 09:10 主动撤回申请（培训时间顺延下周进行）",
                    approver = "排班调度组",
                    timeline = "09-24 16:45 发起调班 ➔ 09-25 09:10 员工主动撤回申请"
                )
            )
        } else {
            listOf(
                ApprovalItem(
                    id = "REQ-2026-101",
                    title = "Supplies & Safety Gear Requisition",
                    meta = "Supplies · Submitted 2026-10-01 09:30",
                    desc = "Requisition for 1 handheld industrial scanner and 2 pairs of ESD gloves for Zone A2 inventory counting.",
                    status = ApprovalStatus.PENDING,
                    category = "Supplies",
                    details = "[Request ID] REQ-2026-101\n[Applicant] test (EMP202609)\n[Category] Material Requisition\n[Items]\n• Zebra TC26 Handheld Scanner × 1 unit\n• ESD Safety Gloves × 2 pairs\n[Purpose] Full zone scanning in Zone A2, current scanner battery degrades quickly\n[Approver] Zhang Ming (Warehouse Lead Dept 1)\n[Current Status] Awaiting direct supervisor review",
                    approver = "Zhang Ming (Warehouse Lead)",
                    timeline = "10-01 09:30 Submitted by employee ➔ Pending supervisor review"
                ),
                ApprovalItem(
                    id = "REQ-2026-098",
                    title = "Cycle Count Overtime Comp Time Request",
                    meta = "HR & Attendance · Submitted 2026-09-30 18:20",
                    desc = "Overtime 4.0 hours for quarterly warehouse cycle count on Sept 29; request crediting to comp time account.",
                    status = ApprovalStatus.IN_PROGRESS,
                    category = "Attendance",
                    details = "[Request ID] REQ-2026-098\n[Applicant] test (EMP202609)\n[Category] Overtime & Comp Time\n[Time Window] 2026-09-29 18:00 - 22:00 (4.0 hrs)\n[Credit Option] 1:1 Comp time balance\n[Approval Chain] Supervisor approved ➔ HR filing in progress\n[Current Status] HR verifying clock-in punch record",
                    approver = "Chen Ting (HR Attendance)",
                    timeline = "09-30 18:20 Submitted ➔ 09-30 20:15 Supervisor approved ➔ HR filing"
                ),
                ApprovalItem(
                    id = "REQ-2026-085",
                    title = "Inbound Damaged Carton Incident Report",
                    meta = "Quality Assurance · Submitted 2026-09-28 14:15",
                    desc = "Outer carton corner damage found during SKU002 unloading. Inner assembly intact; yellow tag applied with photos attached.",
                    status = ApprovalStatus.COMPLETED,
                    category = "Incident Report",
                    details = "[Request ID] REQ-2026-085\n[Applicant] test (EMP202609)\n[Category] Quality Report\n[Target Item] SKU002 Gearbox Assembly (Batch B2609)\n[Resolution] Quality checked inner parts intact; repackaged and released\n[Approver] QA Engineer Li\n[Outcome] Approved and archived",
                    approver = "Engineer Li (QA)",
                    timeline = "09-28 14:15 Reported ➔ 09-28 15:30 QA onsite inspected ➔ 09-28 16:00 Archived"
                ),
                ApprovalItem(
                    id = "REQ-2026-072",
                    title = "Urgent Dispatch Taxi Fare Reimbursement",
                    meta = "Finance Reimbursement · Submitted 2026-09-26 11:00",
                    desc = "Emergency taxi trip to Facility #2 for urgent assembly parts retrieval, receipt ¥42.00.",
                    status = ApprovalStatus.COMPLETED,
                    category = "Reimbursement",
                    details = "[Request ID] REQ-2026-072\n[Applicant] test (EMP202609)\n[Category] Travel & Transport\n[Amount] ¥42.00 (e-receipt uploaded to ERP)\n[Reviewer] Accountant Wang (Finance)\n[Payment Status] Approved; credited to upcoming payroll",
                    approver = "Accountant Wang (Finance)",
                    timeline = "09-26 11:00 Submitted ➔ 09-27 10:20 Approved by Finance ➔ In payroll queue"
                ),
                ApprovalItem(
                    id = "REQ-2026-055",
                    title = "Forklift Recertification Shift Swap Request",
                    meta = "Shift Scheduling · Submitted 2026-09-24 16:45",
                    desc = "Attending OSHA forklift certification seminar; requested morning shift swap with colleague Wang Lei on Oct 5.",
                    status = ApprovalStatus.WITHDRAWN,
                    category = "Scheduling",
                    details = "[Request ID] REQ-2026-055\n[Applicant] test (EMP202609)\n[Category] Shift Swap\n[Shift] Oct 5 Morning (08:00 - 16:30) ➔ Swapped to Oct 8\n[Partner] Wang Lei (EMP202615) mutually agreed\n[Withdrawal Reason] Withdrawn by applicant on 09-25 09:10 (Seminar rescheduled)",
                    approver = "Scheduling Team",
                    timeline = "09-24 16:45 Submitted ➔ 09-25 09:10 Withdrawn by applicant"
                )
            )
        }

        val allItems = userSubmittedItems + defaultItems
        return allItems.map { item ->
            val cached = cachedStatuses[item.id]
            if (cached != null) item.copy(status = cached) else item
        }
    }

    override suspend fun updateStatus(id: String, status: ApprovalStatus): Boolean {
        cachedStatuses[id] = status
        return true
    }

    override suspend fun withdrawApproval(id: String): Boolean {
        cachedStatuses[id] = ApprovalStatus.WITHDRAWN
        return true
    }

    override suspend fun submitApproval(item: ApprovalItem): Boolean {
        userSubmittedItems.add(0, item)
        cachedStatuses[item.id] = item.status
        return true
    }
}
