package com.example.mobile_employee_simple.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.mobile_employee_simple.utils.LanguageManager

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding is only valid between onCreateView and onDestroyView")

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun isChinese(): Boolean = LanguageManager.isChinese(requireContext())

    protected fun showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
        context?.let {
            Toast.makeText(it, messageRes, duration).show()
        }
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        context?.let {
            Toast.makeText(it, message, duration).show()
        }
    }

    protected fun getLocalizedString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return if (formatArgs.isNotEmpty()) {
            getString(resId, *formatArgs)
        } else {
            getString(resId)
        }
    }
}
