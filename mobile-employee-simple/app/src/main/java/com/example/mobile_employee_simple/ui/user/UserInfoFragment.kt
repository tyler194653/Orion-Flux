package com.example.mobile_employee_simple.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.ui.auth.LoginViewModel

class UserInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        val permissionText = view.findViewById<TextView>(R.id.permission_text)
        val context = requireContext()
        val currentUser = LoginViewModel.getCurrentUser(context) ?: "未登录"
        userInfoText.text = "当前用户：$currentUser"
        permissionText.text = "权限：普通员工（示例）"
        return view
    }
} 