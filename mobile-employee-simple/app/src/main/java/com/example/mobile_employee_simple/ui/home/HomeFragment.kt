package com.example.mobile_employee_simple.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.ui.auth.LoginViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val welcomeText = view.findViewById<TextView>(R.id.welcome_text)
        val workbenchButton = view.findViewById<Button>(R.id.workbench_button)
        val aiAssistantButton = view.findViewById<Button>(R.id.ai_assistant_button)

        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: getString(R.string.default_user)
        welcomeText.text = getString(R.string.home_welcome_with_user, currentUser)

        // 工作台按钮 - 跳转到工作台
        workbenchButton.setOnClickListener {
            val navController = Navigation.findNavController(requireView())
            navController.navigate(R.id.nav_workbench)
        }

        // AI助手按钮 - 打开原生智能助手对话框
        aiAssistantButton.setOnClickListener {
            showAiAssistantDialog()
        }
    }

    private fun showAiAssistantDialog() {
        val options = arrayOf(
            "⚠️ 查询当前库存预警",
            "📋 推荐今日优先工作",
            "🔍 快速盘点效率诊断"
        )

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.home_ai_assistant))
            .setItems(options) { _, which ->
                val answer = when (which) {
                    0 -> "🤖 【AI分析】产品B当前库存仅50箱，已触发低库存阈值，建议今日完成进货补仓。"
                    1 -> "🤖 【AI推荐】检测到A区有批次产品变动，建议上午优先执行【A区库存盘点】任务。"
                    else -> "🤖 【AI诊断】过去7天出入库核验准确率98.5%，扫码复核平均耗时12秒，状态良好。"
                }
                AlertDialog.Builder(requireContext())
                    .setTitle(options[which])
                    .setMessage(answer)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}