package com.example.mobile_employee_simple.data.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mobile_employee_simple.R

enum class ApprovalStatus(
    @StringRes val titleRes: Int,
    @ColorRes val textColorRes: Int,
    @DrawableRes val bgDrawableRes: Int
) {
    PENDING(
        R.string.approval_status_pending,
        R.color.status_pending_text,
        R.drawable.bg_badge_pending
    ),
    IN_PROGRESS(
        R.string.approval_status_in_progress,
        R.color.status_in_progress_text,
        R.drawable.bg_badge_in_progress
    ),
    COMPLETED(
        R.string.approval_status_completed,
        R.color.status_completed_text,
        R.drawable.bg_badge_completed
    ),
    REJECTED(
        R.string.approval_status_rejected,
        R.color.status_critical_text,
        R.drawable.bg_badge_rejected
    ),
    WITHDRAWN(
        R.string.approval_status_withdrawn,
        R.color.status_neutral_text,
        R.drawable.bg_badge_withdrawn
    )
}

data class ApprovalItem(
    val id: String,
    val title: String,
    val meta: String,
    val desc: String,
    var status: ApprovalStatus,
    val category: String = "",
    var details: String = "",
    val approver: String = "",
    var timeline: String = "",
    // Bilingual backing fields for dynamic runtime localization
    val titleZh: String = title,
    val titleEn: String = title,
    val descZh: String = desc,
    val descEn: String = desc,
    val categoryType: String = "",
    val applicant: String = "",
    val submitTime: String = "",
    val detailsZh: String = details,
    val detailsEn: String = details,
    val approverZh: String = approver,
    val approverEn: String = approver,
    val timelineZh: String = timeline,
    val timelineEn: String = timeline
) {
    val canWithdraw: Boolean
        get() = status == ApprovalStatus.PENDING || status == ApprovalStatus.IN_PROGRESS

    fun localized(isZh: Boolean): ApprovalItem {
        val resolvedTitle = if (isZh) titleZh.ifEmpty { title } else titleEn.ifEmpty { title }
        val resolvedDesc = if (isZh) descZh.ifEmpty { desc } else descEn.ifEmpty { desc }
        val resolvedApprover = if (isZh) approverZh.ifEmpty { approver } else approverEn.ifEmpty { approver }
        val resolvedTimeline = if (isZh) timelineZh.ifEmpty { timeline } else timelineEn.ifEmpty { timeline }
        val resolvedDetails = if (isZh) detailsZh.ifEmpty { details } else detailsEn.ifEmpty { details }

        val resolvedCategory: String
        val resolvedMeta: String
        if (categoryType.isNotEmpty()) {
            val (catName, tagPrefix) = if (isZh) {
                when (categoryType) {
                    "supplies" -> "工器具与防护耗材申领" to "物资管理"
                    "overtime" -> "延时加班调休申请" to "人事考勤"
                    "reimbursement" -> "差旅与公杂费用报销" to "财务报销"
                    "incident" -> "作业异常与损耗报备" to "作业报备"
                    else -> "工作申请" to "综合事务"
                }
            } else {
                when (categoryType) {
                    "supplies" -> "Supplies & PPE Requisition" to "Supplies"
                    "overtime" -> "Overtime Comp Time Request" to "HR & Attendance"
                    "reimbursement" -> "Expense Reimbursement" to "Finance Reimbursement"
                    "incident" -> "Incident & Damage Report" to "Operations"
                    else -> "Work Request" to "General Request"
                }
            }
            resolvedCategory = catName
            val timePart = submitTime.ifEmpty { "2026-10-01" }
            resolvedMeta = if (isZh) "$tagPrefix · 提交于 $timePart" else "$tagPrefix · Submitted $timePart"
        } else {
            resolvedCategory = category
            resolvedMeta = meta
        }

        return this.copy(
            title = resolvedTitle,
            meta = resolvedMeta,
            desc = resolvedDesc,
            category = resolvedCategory,
            details = resolvedDetails,
            approver = resolvedApprover,
            timeline = resolvedTimeline
        )
    }
}
