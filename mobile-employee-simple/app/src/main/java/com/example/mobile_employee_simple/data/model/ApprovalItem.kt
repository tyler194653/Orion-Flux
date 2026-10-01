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
        R.drawable.bg_badge_pending
    )
}

data class ApprovalItem(
    val id: String,
    val title: String,
    val meta: String,
    val desc: String,
    var status: ApprovalStatus
)
