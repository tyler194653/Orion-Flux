package com.example.mobile_employee_simple.ui.workbench

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.ui.auth.LoginViewModel

class WorkbenchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workbench, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        if (!LoginViewModel.isLoggedIn(requireContext())) {
            showLoginRequired()
            return
        }

        val titleText = view.findViewById<TextView>(R.id.workbench_title)
        val taskListButton = view.findViewById<Button>(R.id.task_list_button)
        val inventoryButton = view.findViewById<Button>(R.id.inventory_button)
        val cameraButton = view.findViewById<Button>(R.id.camera_button)
        val reportsButton = view.findViewById<Button>(R.id.reports_button)

        titleText.text = getString(R.string.workbench_title)

        // 任务列表
        taskListButton.setOnClickListener {
            showTaskListDialog()
        }

        // 库存管理
        inventoryButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.nav_inventory)
        }

        // 扫码与拍照盘点
        cameraButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.nav_camera)
        }

        // 报表统计
        reportsButton.setOnClickListener {
            showReportsDialog()
        }
    }

    private fun showLoginRequired() {
        Toast.makeText(context, getString(R.string.workbench_login_required), Toast.LENGTH_SHORT).show()
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.nav_login)
    }

    private fun showTaskListDialog() {
        val tasks = arrayOf(
            "📋 A区库存盘点 [高优先级 · 进行中]",
            "📦 今日新订单发货处理 [中优先级 · 待处理]",
            "🔍 B批次产品抽检 [低优先级 · 已完成]",
            "⚙️ 仓储传送设备定期维护 [中优先级 · 待处理]"
        )

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.workbench_task_list))
            .setItems(tasks) { _, which ->
                when (which) {
                    0 -> {
                        // 跳转到库存管理
                        Navigation.findNavController(requireView()).navigate(R.id.nav_inventory)
                    }
                    1 -> {
                        Toast.makeText(requireContext(), "已打开订单发货任务详情", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(requireContext(), "B批次抽检已归档", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        Toast.makeText(requireContext(), "设备维护工单已下发", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showReportsDialog() {
        val message = """
            📊 今日概览:
            • 完成任务: 25 件
            • 进行中任务: 8 件
            • 待处理任务: 3 件

            📦 核心库存状况:
            • 产品A: 100个 (正常)
            • 产品B: 50箱 (⚠️ 低库存预警)
            • 产品C: 200件 (正常)

            🎯 质量指标:
            • 综合合格率: 98.5%
            • 抽检返工率: 1.2%
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.workbench_reports))
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
} 