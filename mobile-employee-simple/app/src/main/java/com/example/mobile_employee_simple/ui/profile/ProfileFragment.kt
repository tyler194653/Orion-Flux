package com.example.mobile_employee_simple.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.NavOptions
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.databinding.FragmentProfileBinding
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.base.BaseFragment
import com.example.mobile_employee_simple.ui.base.LocaleDialogHelper
import com.example.mobile_employee_simple.ui.settings.AboutDialogFragment
import com.example.mobile_employee_simple.ui.settings.HelpDialogFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val profileRepository = DataRepositoryProvider.userProfileRepository

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            LocaleDialogHelper.showConfirmDialog(
                context = requireContext(),
                titleRes = R.string.profile_logout_confirm_title,
                messageRes = R.string.profile_logout_confirm_message,
                positiveRes = R.string.profile_logout,
                negativeRes = R.string.cancel,
                onPositive = {
                    LoginViewModel.logout(requireContext())
                    showToast(R.string.profile_logged_out_toast)
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.mobile_navigation, true)
                        .build()
                    Navigation.findNavController(requireView()).navigate(R.id.nav_login, null, navOptions)
                }
            )
        }

        loadUserProfile()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val defaultUser = getLocalizedString(R.string.profile_default_user)
        val currentUser = LoginViewModel.getCurrentUser(requireContext()) ?: defaultUser
        val profile = profileRepository.getUserProfile(currentUser)

        binding.profileName.text = profile.username
        binding.profileRole.text = profile.departmentAndRole
        binding.profileEmpId.text = profile.employeeIdAndLevel
    }
}
