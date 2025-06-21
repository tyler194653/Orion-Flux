package com.example.mobile_employee_simple.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mobile_employee_simple.databinding.DialogHelpBinding

class HelpDialogFragment : DialogFragment() {
    
    private var _binding: DialogHelpBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogHelpBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.apply {
            // 设置帮助内容
            tvHelpTitle.text = "使用帮助"
            
            val helpContent = """
                1. 登录功能
                - 支持工号登录
                - 支持生物识别登录（指纹/面部）
                - 支持扫码登录
                
                2. 工作台
                - 查看今日任务
                - 快速操作入口
                - 消息通知中心
                
                3. 库存管理
                - 扫码操作库存
                - 入库/出库确认
                - 库存盘点
                
                4. 相机功能
                - 拍照上传
                - 二维码扫描
                - 条码扫描
                
                5. 设置
                - 生物识别设置
                - 通知设置
                - 数据同步设置
                
                如有问题，请联系技术支持。
            """.trimIndent()
            
            tvHelpContent.text = helpContent
            
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