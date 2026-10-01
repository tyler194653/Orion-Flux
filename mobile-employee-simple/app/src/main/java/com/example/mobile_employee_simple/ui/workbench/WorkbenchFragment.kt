package com.example.mobile_employee_simple.ui.workbench

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentWorkbenchBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.base.BaseFragment

class WorkbenchFragment : BaseFragment<FragmentWorkbenchBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWorkbenchBinding {
        return FragmentWorkbenchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        if (!LoginViewModel.isLoggedIn(requireContext())) {
            showLoginRequired()
            return
        }

        updateWelcomeGreeting()

        // 业务卡片点击事件
        binding.cardInventory.setOnClickListener {
            findNavController().navigate(R.id.nav_inventory)
        }

        binding.cardCamera.setOnClickListener {
            findNavController().navigate(R.id.nav_camera)
        }

        binding.cardCatalog.setOnClickListener {
            findNavController().navigate(R.id.nav_gallery)
        }

        binding.cardApprovals.setOnClickListener {
            findNavController().navigate(R.id.nav_approvals)
        }

        binding.cardReports.setOnClickListener {
            showReportsDialog()
        }

        binding.cardAiAssistant.setOnClickListener {
            findNavController().navigate(R.id.nav_ai_chat)
        }

        // 今日作业任务点击事件
        binding.taskInventoryCheck.setOnClickListener {
            showToast(R.string.workbench_task_inventory_check_toast)
            findNavController().navigate(R.id.nav_inventory)
        }

        binding.taskOrderDispatch.setOnClickListener {
            showTaskDispatchDialog()
        }

        binding.taskQualityCheck.setOnClickListener {
            showTaskQualityDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        if (LoginViewModel.isLoggedIn(requireContext())) {
            updateWelcomeGreeting()
        }
    }

    private fun updateWelcomeGreeting() {
        val defaultRole = getLocalizedString(R.string.workbench_welcome_default_role)
        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: defaultRole
        binding.workbenchWelcomeText.text = getLocalizedString(R.string.workbench_welcome_format, currentUser)
    }

    private fun showLoginRequired() {
        showToast(R.string.workbench_login_required)
        findNavController().navigate(R.id.nav_login)
    }

    private fun showTaskDispatchDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.workbench_task_dispatch_title)
            .setMessage(getLocalizedString(R.string.workbench_task_dispatch_content))
            .setPositiveButton(R.string.workbench_task_dispatch_btn_approvals) { _, _ ->
                findNavController().navigate(R.id.nav_approvals)
            }
            .setNeutralButton(R.string.workbench_task_dispatch_btn_start) { _, _ ->
                showToast(R.string.workbench_task_dispatch_toast)
            }
            .setNegativeButton(R.string.workbench_task_dispatch_btn_later, null)
            .show()
    }

    private fun showTaskQualityDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.workbench_task_quality_title)
            .setMessage(getLocalizedString(R.string.workbench_task_quality_content))
            .setPositiveButton(R.string.workbench_task_quality_btn_view) { _, _ ->
                showToast(R.string.workbench_task_quality_toast)
            }
            .setNegativeButton(R.string.workbench_task_quality_btn_close, null)
            .show()
    }

    private fun showReportsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.workbench_reports)
            .setMessage(getLocalizedString(R.string.workbench_reports_summary))
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}