package com.example.mobile_employee_simple.ui.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.mobile_employee_simple.R

object LocaleDialogHelper {

    fun showConfirmDialog(
        context: Context,
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        @StringRes positiveRes: Int = android.R.string.ok,
        @StringRes negativeRes: Int = R.string.cancel,
        onPositive: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(positiveRes) { _, _ -> onPositive() }
            .setNegativeButton(negativeRes, null)
            .show()
    }

    fun showInfoDialog(
        context: Context,
        @StringRes titleRes: Int,
        message: CharSequence,
        @StringRes buttonRes: Int = android.R.string.ok
    ) {
        AlertDialog.Builder(context)
            .setTitle(titleRes)
            .setMessage(message)
            .setPositiveButton(buttonRes, null)
            .show()
    }
}
