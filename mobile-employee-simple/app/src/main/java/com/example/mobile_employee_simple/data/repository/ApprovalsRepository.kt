package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.ApprovalItem
import com.example.mobile_employee_simple.data.model.ApprovalStatus
import com.example.mobile_employee_simple.utils.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ApprovalsRepository {
    suspend fun getApprovals(): List<ApprovalItem>
    suspend fun updateStatus(id: String, status: ApprovalStatus): Boolean
    suspend fun withdrawApproval(id: String): Boolean
    suspend fun submitApproval(item: ApprovalItem): Boolean

    companion object {
        fun createBilingualRequest(
            categoryType: String,
            userTitle: String,
            userDesc: String,
            applicant: String
        ): ApprovalItem {
            val now = Date()
            val nowStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(now)
            val timeTag = SimpleDateFormat("HHmm", Locale.getDefault()).format(now)
            val id = "REQ-2026-$timeTag"

            fun hasChinese(text: String): Boolean = text.any { it in '\u4e00'..'\u9fa5' }
            val inputHasChinese = hasChinese(userTitle) || hasChinese(userDesc)

            val titleZh: String
            val descZh: String
            val titleEn: String
            val descEn: String

            if (inputHasChinese) {
                titleZh = userTitle.ifEmpty {
                    when (categoryType) {
                        "supplies" -> "工器具与防护耗材申领"
                        "overtime" -> "延时加班调休申请"
                        "reimbursement" -> "差旅与公杂费用报销"
                        "incident" -> "作业异常与损耗报备"
                        else -> "员工业务申请"
                    }
                }
                descZh = userDesc.ifEmpty { "员工提交的业务申请说明。" }

                when (categoryType) {
                    "supplies" -> {
                        titleEn = when {
                            userTitle.contains("扫码") || userTitle.contains("枪") -> "Handheld Barcode Scanner Requisition"
                            userTitle.contains("手套") || userTitle.contains("防护") || userTitle.contains("劳保") -> "Protective PPE & Safety Gloves Requisition"
                            userTitle.contains("托盘") || userTitle.contains("车") -> "Pallet & Handling Gear Requisition"
                            else -> "Warehouse Tools & Consumable Supplies Requisition"
                        }
                        descEn = "Requisition for frontline warehouse operations and equipment replacement."
                    }
                    "overtime" -> {
                        titleEn = "Warehouse Overtime Comp Time Application"
                        descEn = "Application for compensatory time credit from warehouse overtime shift."
                    }
                    "reimbursement" -> {
                        titleEn = "Operational Travel & Out-of-Pocket Expense Claim"
                        descEn = "Reimbursement claim for emergency logistics transport with receipts attached."
                    }
                    "incident" -> {
                        titleEn = "Inbound/Outbound Cargo Damage & Incident Report"
                        descEn = "Outer packaging carton damage exception report with isolation photos filed."
                    }
                    else -> {
                        titleEn = "Employee Work Request & Application"
                        descEn = "Personal work-related operational request submitted by employee."
                    }
                }
            } else {
                titleEn = userTitle.ifEmpty {
                    when (categoryType) {
                        "supplies" -> "Supplies & PPE Requisition"
                        "overtime" -> "Overtime Comp Time Request"
                        "reimbursement" -> "Expense Reimbursement"
                        "incident" -> "Incident & Damage Report"
                        else -> "Work Request"
                    }
                }
                descEn = userDesc.ifEmpty { "Personal work request submitted by employee." }

                when (categoryType) {
                    "supplies" -> {
                        titleZh = "工器具与防护耗材申领"
                        descZh = "申领手持工业扫码枪及劳保耗材，用于货区盘点作业。"
                    }
                    "overtime" -> {
                        titleZh = "月末大盘点延时加班调休申请"
                        descZh = "申请延时加班工时折算存入个人调休假期账户。"
                    }
                    "reimbursement" -> {
                        titleZh = "紧急外勤差旅与交通费报销"
                        descZh = "紧急跨库调拨打车费用发票报销申请。"
                    }
                    "incident" -> {
                        titleZh = "作业异常与外箱破损报备"
                        descZh = "货物上架复核发现外包装破损异常，已贴隔离标并拍照留存。"
                    }
                    else -> {
                        titleZh = "个人工作业务申请"
                        descZh = "员工个人发起的日常工作事务申请。"
                    }
                }
            }

            val (catZh, approverZh) = when (categoryType) {
                "supplies" -> "物资申领" to "仓储一部·张明主管"
                "overtime" -> "加班调休" to "陈婷 (HR 考勤专员)"
                "reimbursement" -> "费用报销" to "王会计 (财务中心)"
                "incident" -> "异常报备" to "李工 (质检中心)"
                else -> "工作申请" to "直属主管"
            }

            val (catEn, approverEn) = when (categoryType) {
                "supplies" -> "Material Requisition" to "Zhang Ming (Warehouse Lead)"
                "overtime" -> "Overtime & Comp Time" to "Chen Ting (HR Attendance)"
                "reimbursement" -> "Expense Reimbursement" to "Accountant Wang (Finance)"
                "incident" -> "Incident & Damage Report" to "Engineer Li (QA)"
                else -> "Work Request" to "Direct Supervisor"
            }

            val detailsZh = "【申请单号】$id\n【申请员工】$applicant\n【申请类别】$catZh\n【申请事由】$titleZh\n【详细说明】$descZh\n【提交时间】$nowStr\n【审核主管】$approverZh\n【当前节点】等待主管初审"

            val detailsEn = "[Request ID] $id\n[Applicant] $applicant\n[Category] $catEn\n[Subject] $titleEn\n[Details] $descEn\n[Submitted At] $nowStr\n[Approver] $approverEn\n[Current Node] Awaiting Supervisor Review"

            val timelineZh = "$nowStr 员工提交申请 ➔ 待主管初审"
            val timelineEn = "$nowStr Submitted by employee ➔ Pending supervisor review"

            return ApprovalItem(
                id = id,
                title = titleZh,
                meta = "物资管理 · 提交于 $nowStr",
                desc = descZh,
                status = ApprovalStatus.PENDING,
                category = catZh,
                details = detailsZh,
                approver = approverZh,
                timeline = timelineZh,
                titleZh = titleZh,
                titleEn = titleEn,
                descZh = descZh,
                descEn = descEn,
                categoryType = categoryType,
                applicant = applicant,
                submitTime = nowStr,
                detailsZh = detailsZh,
                detailsEn = detailsEn,
                approverZh = approverZh,
                approverEn = approverEn,
                timelineZh = timelineZh,
                timelineEn = timelineEn
            )
        }
    }
}

class MockApprovalsRepositoryImpl : ApprovalsRepository {
    private val cachedStatuses = mutableMapOf<String, ApprovalStatus>()
    private val userSubmittedItems = mutableListOf<ApprovalItem>()

    private val defaultBilingualItems = listOf(
        ApprovalItem(
            id = "REQ-2026-101",
            title = "工器具与防护耗材申领",
            meta = "物资管理 · 提交于 2026-10-01 09:30",
            desc = "申领手持工业扫码枪 1 台、防静电劳保手套 2 双，用于 A2 货区扫码盘点作业。",
            status = ApprovalStatus.PENDING,
            category = "物资申领",
            details = "【申请单号】REQ-2026-101\n【申请员工】test (工号 EMP202609)\n【申请类别】物资申领\n【物资明细】\n• 斑马手持工业扫码枪 TC26 × 1台\n• 防静电透气防护手套 × 2双\n【用途说明】A2 货架全流程扫码作业，现有设备电量老化频繁断联\n【审核主管】仓储一部·张明主管\n【当前节点】等待直属主管审批",
            approver = "张明主管 (仓储一部)",
            timeline = "10-01 09:30 员工提交申请 ➔ 待直属主管审批",
            titleZh = "工器具与防护耗材申领",
            titleEn = "Supplies & Safety Gear Requisition",
            descZh = "申领手持工业扫码枪 1 台、防静电劳保手套 2 双，用于 A2 货区扫码盘点作业。",
            descEn = "Requisition for 1 handheld industrial scanner and 2 pairs of ESD gloves for Zone A2 inventory counting.",
            categoryType = "supplies",
            submitTime = "2026-10-01 09:30",
            detailsZh = "【申请单号】REQ-2026-101\n【申请员工】test (工号 EMP202609)\n【申请类别】物资申领\n【物资明细】\n• 斑马手持工业扫码枪 TC26 × 1台\n• 防静电透气防护手套 × 2双\n【用途说明】A2 货架全流程扫码作业，现有设备电量老化频繁断联\n【审核主管】仓储一部·张明主管\n【当前节点】等待直属主管审批",
            detailsEn = "[Request ID] REQ-2026-101\n[Applicant] test (EMP202609)\n[Category] Material Requisition\n[Items]\n• Zebra TC26 Handheld Scanner × 1 unit\n• ESD Safety Gloves × 2 pairs\n[Purpose] Full zone scanning in Zone A2, current scanner battery degrades quickly\n[Approver] Zhang Ming (Warehouse Lead Dept 1)\n[Current Status] Awaiting direct supervisor review",
            approverZh = "张明主管 (仓储一部)",
            approverEn = "Zhang Ming (Warehouse Lead)",
            timelineZh = "10-01 09:30 员工提交申请 ➔ 待直属主管审批",
            timelineEn = "10-01 09:30 Submitted by employee ➔ Pending supervisor review"
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
            timeline = "09-30 18:20 员工提交 ➔ 09-30 20:15 主管初审通过 ➔ 人事部门备案中",
            titleZh = "月末大盘点延时加班调休申请",
            titleEn = "Cycle Count Overtime Comp Time Request",
            descZh = "9月29日配合三号库季度盘点延时作业 4.0 小时，申请折算存入个人调休假期账户。",
            descEn = "Overtime 4.0 hours for quarterly warehouse cycle count on Sept 29; request crediting to comp time account.",
            categoryType = "overtime",
            submitTime = "2026-09-30 18:20",
            detailsZh = "【申请单号】REQ-2026-098\n【申请员工】test (工号 EMP202609)\n【申请类别】加班调休\n【加班时段】2026-09-29 18:00 - 22:00 (共计 4.0 小时)\n【折算方案】存入调休假余额 (1:1 调休)\n【审批链条】直属主管初审同意 ➔ 人事部门备案中\n【当前节点】人事考勤专员核对打卡工时中",
            detailsEn = "[Request ID] REQ-2026-098\n[Applicant] test (EMP202609)\n[Category] Overtime & Comp Time\n[Time Window] 2026-09-29 18:00 - 22:00 (4.0 hrs)\n[Credit Option] 1:1 Comp time balance\n[Approval Chain] Supervisor approved ➔ HR filing in progress\n[Current Status] HR verifying clock-in punch record",
            approverZh = "陈婷 (HR 考勤专员)",
            approverEn = "Chen Ting (HR Attendance)",
            timelineZh = "09-30 18:20 员工提交 ➔ 09-30 20:15 主管初审通过 ➔ 人事部门备案中",
            timelineEn = "09-30 18:20 Submitted ➔ 09-30 20:15 Supervisor approved ➔ HR filing"
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
            timeline = "09-28 14:15 发起报备 ➔ 09-28 15:30 质检到场复核 ➔ 09-28 16:00 已归档完成",
            titleZh = "进料上架外箱微损异常报备",
            titleEn = "Inbound Damaged Carton Incident Report",
            descZh = "卸货复核时发现 SKU002 外包装角部挤压变形，内部总成无损，已贴黄色隔离标并留存现场照片。",
            descEn = "Outer carton corner damage found during SKU002 unloading. Inner assembly intact; yellow tag applied with photos attached.",
            categoryType = "incident",
            submitTime = "2026-09-28 14:15",
            detailsZh = "【申请单号】REQ-2026-085\n【申请员工】test (工号 EMP202609)\n【申请类别】异常报备\n【异常物料】SKU002 减速齿轮箱总成 (批次 B2609)\n【处理结论】质检复检合格，重新加固封装贴标入库\n【审核人员】质检中心·李工\n【最终结果】已批准并归档备案",
            detailsEn = "[Request ID] REQ-2026-085\n[Applicant] test (EMP202609)\n[Category] Quality Report\n[Target Item] SKU002 Gearbox Assembly (Batch B2609)\n[Resolution] Quality checked inner parts intact; repackaged and released\n[Approver] QA Engineer Li\n[Outcome] Approved and archived",
            approverZh = "李工 (质检中心)",
            approverEn = "Engineer Li (QA)",
            timelineZh = "09-28 14:15 发起报备 ➔ 09-28 15:30 质检到场复核 ➔ 09-28 16:00 已归档完成",
            timelineEn = "09-28 14:15 Reported ➔ 09-28 15:30 QA onsite inspected ➔ 09-28 16:00 Archived"
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
            timeline = "09-26 11:00 发起报销 ➔ 09-27 10:20 财务审核通过 ➔ 待打款发放",
            titleZh = "跨厂区应急调料交通费报销",
            titleEn = "Urgent Dispatch Taxi Fare Reimbursement",
            descZh = "支援二号厂区突发总成装配缺料，紧急打车跨库调送取件发票报销 ¥42.00。",
            descEn = "Emergency taxi trip to Facility #2 for urgent assembly parts retrieval, receipt ¥42.00.",
            categoryType = "reimbursement",
            submitTime = "2026-09-26 11:00",
            detailsZh = "【申请单号】REQ-2026-072\n【申请员工】test (工号 EMP202609)\n【申请类别】差旅报销\n【报销金额】¥42.00 (电子出租车发票已上传至 ERP)\n【审核财务】财务中心·王会计\n【打款进度】审核通过，随本月工资单发放",
            detailsEn = "[Request ID] REQ-2026-072\n[Applicant] test (EMP202609)\n[Category] Travel & Transport\n[Amount] ¥42.00 (e-receipt uploaded to ERP)\n[Reviewer] Accountant Wang (Finance)\n[Payment Status] Approved; credited to upcoming payroll",
            approverZh = "王会计 (财务中心)",
            approverEn = "Accountant Wang (Finance)",
            timelineZh = "09-26 11:00 发起报销 ➔ 09-27 10:20 财务审核通过 ➔ 待打款发放",
            timelineEn = "09-26 11:00 Submitted ➔ 09-27 10:20 Approved by Finance ➔ In payroll queue"
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
            timeline = "09-24 16:45 发起调班 ➔ 09-25 09:10 员工主动撤回申请",
            titleZh = "特种作业证复审调班申请",
            titleEn = "Forklift Recertification Shift Swap Request",
            descZh = "因参加安监局叉车操作证复审培训，申请将 10月5日早班与同事王磊对调。",
            descEn = "Attending OSHA forklift certification seminar; requested morning shift swap with colleague Wang Lei on Oct 5.",
            categoryType = "overtime",
            submitTime = "2026-09-24 16:45",
            detailsZh = "【申请单号】REQ-2026-055\n【申请员工】test (工号 EMP202609)\n【申请类别】排班对调\n【对调班次】10月5日早班 (08:00 - 16:30) ➔ 调至 10月8日\n【对调员工】王磊 (EMP202615) 双方已确认\n【撤回说明】申请人于 09-25 09:10 主动撤回申请（培训时间顺延下周进行）",
            detailsEn = "[Request ID] REQ-2026-055\n[Applicant] test (EMP202609)\n[Category] Shift Swap\n[Shift] Oct 5 Morning (08:00 - 16:30) ➔ Swapped to Oct 8\n[Partner] Wang Lei (EMP202615) mutually agreed\n[Withdrawal Reason] Withdrawn by applicant on 09-25 09:10 (Seminar rescheduled)",
            approverZh = "排班调度组",
            approverEn = "Scheduling Team",
            timelineZh = "09-24 16:45 发起调班 ➔ 09-25 09:10 员工主动撤回申请",
            timelineEn = "09-24 16:45 Submitted ➔ 09-25 09:10 Withdrawn by applicant"
        )
    )

    override suspend fun getApprovals(): List<ApprovalItem> {
        val isZh = LanguageManager.isChinese()
        val allRawItems = userSubmittedItems + defaultBilingualItems

        return allRawItems.map { rawItem ->
            val localizedItem = rawItem.localized(isZh)
            val cached = cachedStatuses[rawItem.id]
            val finalStatus = cached ?: rawItem.status
            val statusUpdated = localizedItem.copy(status = finalStatus)

            if (finalStatus == ApprovalStatus.WITHDRAWN) {
                if (isZh) {
                    val wd = if (!statusUpdated.details.contains("撤回说明")) {
                        "${statusUpdated.details}\n【撤回说明】申请人已主动撤回该申请"
                    } else statusUpdated.details
                    val wt = if (!statusUpdated.timeline.contains("撤回")) {
                        "${statusUpdated.timeline} ➔ 员工主动撤回申请"
                    } else statusUpdated.timeline
                    statusUpdated.copy(details = wd, timeline = wt)
                } else {
                    val wd = if (!statusUpdated.details.contains("Withdrawal Note") && !statusUpdated.details.contains("Withdrawal Reason")) {
                        "${statusUpdated.details}\n[Withdrawal Note] Withdrawn by applicant"
                    } else statusUpdated.details
                    val wt = if (!statusUpdated.timeline.contains("Withdrawn")) {
                        "${statusUpdated.timeline} ➔ Withdrawn by applicant"
                    } else statusUpdated.timeline
                    statusUpdated.copy(details = wd, timeline = wt)
                }
            } else {
                statusUpdated
            }
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
