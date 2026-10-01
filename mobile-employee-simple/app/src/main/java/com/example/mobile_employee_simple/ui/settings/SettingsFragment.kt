package com.example.mobile_employee_simple.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        updateVersionInfo()
    }
    
    private fun setupUI() {
        binding.apply {
            // 语言设置
            btnLanguageSettings.setOnClickListener {
                showLanguageSelectDialog()
            }

            // 通知设置按钮 - 跳转到系统通知设置
            btnNotificationSettings.setOnClickListener {
                openNotificationSettings()
            }
            
            // 隐私设置按钮 - 功能开发中
            btnPrivacySettings.setOnClickListener {
                Toast.makeText(context, "隐私设置功能开发中", Toast.LENGTH_SHORT).show()
            }
            
            // 数据同步设置按钮 - 功能开发中
            btnSyncSettings.setOnClickListener {
                Toast.makeText(context, "数据同步设置功能开发中", Toast.LENGTH_SHORT).show()
            }
            
            // 应用信息按钮 - 显示应用详细信息
            btnAppInfo.setOnClickListener {
                showAppInfoDialog()
            }
            
            // 帮助与反馈按钮 - 发送邮件反馈
            btnHelpFeedback.setOnClickListener {
                sendFeedback()
            }
            
            // 关于应用按钮 - 显示关于对话框
            btnAbout.setOnClickListener {
                showAboutDialog()
            }
        }
    }
    
    private fun openNotificationSettings() {
        try {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            startActivity(intent)
        } catch (e: Exception) {
            // 如果无法打开应用通知设置，则打开通用通知设置
            val intent = Intent("android.settings.NOTIFICATION_SETTINGS")
            startActivity(intent)
        }
    }
    
    private fun showAppInfoDialog() {
        val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val appName = packageInfo.applicationInfo?.loadLabel(requireContext().packageManager)?.toString() ?: "供应链员工端"
        val versionName = packageInfo.versionName ?: "1.0.0"
        val versionCode = packageInfo.versionCode.toString()
        val packageName = packageInfo.packageName
        
        val message = """
            应用名称: $appName
            版本号: $versionName
            版本代码: $versionCode
            包名: $packageName
        """.trimIndent()
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("应用信息")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
    
    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:support@supplychain.com")
            putExtra(Intent.EXTRA_SUBJECT, "供应链员工端应用反馈")
            putExtra(Intent.EXTRA_TEXT, """
                请在此处描述您的问题或建议：
                
                应用版本: ${getAppVersion()}
                设备信息: ${android.os.Build.MODEL}
                系统版本: Android ${android.os.Build.VERSION.RELEASE}
                
                反馈内容：
                
            """.trimIndent())
        }
        
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, "未找到邮件应用，请手动发送邮件到 support@supplychain.com", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun showAboutDialog() {
        val message = """
            供应链员工端
            
            版本: ${getAppVersion()}
            
            本应用为供应链管理系统员工端，提供以下功能：
            • 工作台管理
            • 库存管理
            • 扫码功能
            • AI助手
            
            如有问题请联系技术支持。
        """.trimIndent()
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("关于应用")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
    
    private fun showLanguageSelectDialog() {
        val languages = arrayOf(
            getString(R.string.language_zh_cn),
            getString(R.string.language_en)
        )
        val currentLang = com.example.mobile_employee_simple.utils.LanguageManager.getCurrentLanguage(requireContext())
        val checkedItem = if (currentLang == com.example.mobile_employee_simple.utils.LanguageManager.LANGUAGE_ZH) 0 else 1

        android.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.language_dialog_title))
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val targetLang = if (which == 0) {
                    com.example.mobile_employee_simple.utils.LanguageManager.LANGUAGE_ZH
                } else {
                    com.example.mobile_employee_simple.utils.LanguageManager.LANGUAGE_EN
                }
                dialog.dismiss()
                if (targetLang != currentLang) {
                    com.example.mobile_employee_simple.utils.LanguageManager.setLanguage(requireContext(), targetLang)
                    val msg = if (targetLang == com.example.mobile_employee_simple.utils.LanguageManager.LANGUAGE_ZH) {
                        getString(R.string.language_switched_zh)
                    } else {
                        getString(R.string.language_switched_en)
                    }
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = requireContext().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            )
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }
    
    private fun updateVersionInfo() {
        binding.tvVersion.text = getString(R.string.settings_version, getAppVersion())
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 