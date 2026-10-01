package com.example.mobile_employee_simple.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.NavOptions
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentProfileBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.settings.AboutDialogFragment
import com.example.mobile_employee_simple.ui.settings.HelpDialogFragment

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: "员工用户"
        binding.profileName.text = "$currentUser"

        binding.rowSettings.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.nav_settings)
        }

        binding.rowAbout.setOnClickListener {
            AboutDialogFragment().show(parentFragmentManager, "AboutDialog")
        }

        binding.rowHelp.setOnClickListener {
            HelpDialogFragment().show(parentFragmentManager, "HelpDialog")
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("确认退出")
                .setMessage("确定要退出当前账号并返回登录界面吗？")
                .setPositiveButton("退出登录") { _, _ ->
                    LoginViewModel.logout(requireContext())
                    Toast.makeText(requireContext(), "已退出登录", Toast.LENGTH_SHORT).show()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.mobile_navigation, true)
                        .build()
                    Navigation.findNavController(requireView()).navigate(R.id.nav_login, null, navOptions)
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
