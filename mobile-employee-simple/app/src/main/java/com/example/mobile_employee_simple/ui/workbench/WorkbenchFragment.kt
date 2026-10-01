package com.example.mobile_employee_simple.ui.workbench

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentWorkbenchBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel

class WorkbenchFragment : Fragment() {

    private var _binding: FragmentWorkbenchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkbenchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        if (!LoginViewModel.isLoggedIn(requireContext())) {
            showLoginRequired()
            return
        }

        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: "仓储主管"
        binding.workbenchWelcomeText.text = "您好，$currentUser"

        // 2x2 业务卡片点击事件
        // 1. 库存管理
        binding.cardInventory.setOnClickListener {
            findNavController().navigate(R.id.nav_inventory)
        }

        // 2. 扫码拍照
        binding.cardCamera.setOnClickListener {
            findNavController().navigate(R.id.nav_camera)
        }

        // 3. 产品图鉴
        binding.cardCatalog.setOnClickListener {
            findNavController().navigate(R.id.nav_gallery)
        }

        // 4. 数据报表
        binding.cardReports.setOnClickListener {
            showReportsDialog()
        }

        // 今日作业任务点击事件
        binding.taskInventoryCheck.setOnClickListener {
            Toast.makeText(requireContext(), "正在进入 A区-01 盘点任务...", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.nav_inventory)
        }

        binding.taskOrderDispatch.setOnClickListener {
            showTaskDispatchDialog()
        }

        binding.taskQualityCheck.setOnClickListener {
            showTaskQualityDialog()
        }
    }

    private fun showLoginRequired() {
        Toast.makeText(context, getString(R.string.workbench_login_required), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.nav_login)
    }

    private fun showTaskDispatchDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("加急发运任务 #SO-9921")
            .setMessage("""
                • 客户: 华东精密制造中心
                • 目标件数: 48 箱高强度连接件
                • 状态: 待装车复核 (高优先级)
                • 出库通道: Dock 03
            """.trimIndent())
            .setPositiveButton("开始复核") { _, _ ->
                Toast.makeText(requireContext(), "已分配装车工单并通知 Dock 03", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("稍后处理", null)
            .show()
    }

    private fun showTaskQualityDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("零件质量抽检报告")
            .setMessage("""
                • 批次号: 2026-B-88301
                • 抽检数量: 120 件
                • 合格率: 100% (公差均在 ±0.02mm 内)
                • 检验员: QA-07 (已签名确认)
                • 状态: 已归档
            """.trimIndent())
            .setPositiveButton("查看报告详情") { _, _ ->
                Toast.makeText(requireContext(), "质检记录已上链同步至 ERP", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("关闭", null)
            .show()
    }

    private fun showReportsDialog() {
        val message = """
            📊 今日概览:
            • 完成任务: 25 件
            • 进行中任务: 8 件
            • 待处理任务: 3 件

            📦 核心库存状况:
            • 轴承组 A: 1,420 套 (正常)
            • 传感器组件: 58 组 (⚠️ 低库存预警)
            • 传动皮带: 310 条 (正常)

            🎯 质量指标:
            • 综合合格率: 99.4%
            • 出库准时率: 98.8%
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.workbench_reports))
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}