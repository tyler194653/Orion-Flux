package com.example.mobile_employee_simple.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.NavOptions
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.MainActivity
import com.example.mobile_employee_simple.databinding.FragmentLoginBinding
import com.example.mobile_employee_simple.services.BiometricService
import com.example.mobile_employee_simple.ui.home.HomeFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var biometricService: BiometricService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(requireActivity().application))[LoginViewModel::class.java]
        biometricService = BiometricService(requireContext())

        setupUI()
        observeViewModel()
        checkBiometricAvailability()
        
        // 保存测试账户
        LoginViewModel.saveTestAccount(requireContext())
    }
    
    private fun setupUI() {
        binding.apply {
            // 登录按钮
            loginButton.setOnClickListener {
                performLogin()
            }
            
            // 生物识别登录按钮
            btnBiometricLogin.setOnClickListener {
                performBiometricLogin()
            }
            
            // 扫码登录按钮
            btnQrLogin.setOnClickListener {
                performQRLogin()
            }
            
            // 工号登录按钮
            btnEmployeeLogin.setOnClickListener {
                showEmployeeLoginDialog()
            }
        }
    }
    
    private fun observeViewModel() {
        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginButton.isEnabled = !isLoading
        }
        
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                    // 启用侧边导航
                    (activity as? MainActivity)?.enableNavigationAfterLogin()
                    showHome()
                }
                is LoginResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    
    private fun checkBiometricAvailability() {
        val canUseBiometric = biometricService.canAuthenticate()
        binding.btnBiometricLogin.visibility = if (canUseBiometric) View.VISIBLE else View.GONE
    }

    private fun performLogin() {
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return
        }

        loginViewModel.login(username, password)
    }
    
    private fun performBiometricLogin() {
        biometricService.showBiometricPrompt(
            activity = requireActivity(),
            title = "生物识别登录",
            subtitle = "请使用指纹或面部识别进行登录",
            onSuccess = {
                // 生物识别成功，执行登录
                loginViewModel.loginWithBiometric()
            },
            onError = { error ->
                Toast.makeText(context, "生物识别错误: $error", Toast.LENGTH_SHORT).show()
            },
            onFailed = {
                Toast.makeText(context, "生物识别失败，请重试", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun performQRLogin() {
        // TODO: 实现扫码登录
        Toast.makeText(context, "扫码登录功能开发中", Toast.LENGTH_SHORT).show()
    }
    
    private fun showEmployeeLoginDialog() {
        // TODO: 显示工号登录对话框
        Toast.makeText(context, "工号登录功能开发中", Toast.LENGTH_SHORT).show()
    }

    private fun showHome() {
        val navController = Navigation.findNavController(requireView())
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_login, true)
            .build()
        navController.navigate(R.id.nav_home, null, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ViewModel Factory
class LoginViewModelFactory(private val application: android.app.Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 