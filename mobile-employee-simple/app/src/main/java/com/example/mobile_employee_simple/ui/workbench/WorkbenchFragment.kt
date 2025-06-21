package com.example.mobile_employee_simple.ui.workbench

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.auth.LoginFragment
// import com.example.mobile_employee_simple.ui.react.ReactNativeFragment

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
        val reportsButton = view.findViewById<Button>(R.id.reports_button)

        titleText.text = "工作台"

        // 任务列表 - 使用React Native组件
        taskListButton.setOnClickListener {
            // showReactNativeTaskList()
            Toast.makeText(context, "任务列表功能开发中", Toast.LENGTH_SHORT).show()
        }

        // 库存管理 - 使用原生Android
        inventoryButton.setOnClickListener {
            showNativeInventory()
        }

        // 报表 - 使用React Native组件
        reportsButton.setOnClickListener {
            // showReactNativeReports()
            Toast.makeText(context, "报表功能开发中", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoginRequired() {
        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        val loginFragment = LoginFragment()
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment_content_main, loginFragment)
            // 不添加到返回栈，这样用户按返回键时不会回到工作台页面
        }
    }

    private fun showReactNativeTaskList() {
        // val taskListFragment = ReactNativeFragment.newInstance("TaskListScreen")
        // parentFragmentManager.commit {
        //     replace(R.id.nav_host_fragment_content_main, taskListFragment)
        //     addToBackStack(null)
        // }
    }

    private fun showNativeInventory() {
        // val inventoryFragment = InventoryFragment()
        // parentFragmentManager.commit {
        //     replace(R.id.nav_host_fragment_content_main, inventoryFragment)
        //     addToBackStack(null)
        // }
        Toast.makeText(context, "库存管理功能开发中", Toast.LENGTH_SHORT).show()
    }

    private fun showReactNativeReports() {
        // val reportsFragment = ReactNativeFragment.newInstance("ReportsScreen")
        // parentFragmentManager.commit {
        //     replace(R.id.nav_host_fragment_content_main, reportsFragment)
        //     addToBackStack(null)
        // }
    }
} 