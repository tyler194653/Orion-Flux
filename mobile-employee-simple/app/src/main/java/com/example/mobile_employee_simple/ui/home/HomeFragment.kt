package com.example.mobile_employee_simple.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.NavOptions
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
// import com.example.mobile_employee_simple.ui.react.ReactNativeFragment

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

        // 原生Android UI元素
        val welcomeText = view.findViewById<TextView>(R.id.welcome_text)
        val workbenchButton = view.findViewById<Button>(R.id.workbench_button)
        val aiAssistantButton = view.findViewById<Button>(R.id.ai_assistant_button)

        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: "用户"
        welcomeText.text = "欢迎使用供应链员工端，${currentUser}"

        // 工作台按钮 - 跳转到工作台
        workbenchButton.setOnClickListener {
            showWorkbench()
        }

        // AI助手按钮 - 跳转到设置页面（暂时）
        aiAssistantButton.setOnClickListener {
            showSettings()
        }
    }

    private fun showWorkbench() {
        // 使用Navigation组件跳转到工作台
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.nav_workbench)
    }

    private fun showSettings() {
        // 使用Navigation组件跳转到设置页面
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.nav_settings)
    }
}