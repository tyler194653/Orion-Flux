package com.example.mobile_employee_simple.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.DialogAboutBinding

class AboutDialogFragment : DialogFragment() {
    
    private var _binding: DialogAboutBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAboutBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.apply {
            // 设置应用信息
            tvAppName.text = "供应链员工端"
            tvVersion.text = "版本 1.0.0"
            tvDescription.text = "专业的供应链管理移动应用，为一线员工提供高效的工作工具。"
            
            // 设置公司信息
            tvCompany.text = "开发公司: Example Corp"
            tvCopyright.text = "© 2024 Example Corp. All rights reserved."
            
            // 关闭按钮
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 