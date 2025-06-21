// 已注释：该文件依赖React Native，暂时屏蔽
/*
package com.example.mobile_employee_simple.ui.react

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.react.ReactFragment
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.annotations.UnstableReactNativeAPI

@UnstableReactNativeAPI
class ReactNativeFragment : Fragment() {

    private var reactRootView: ReactRootView? = null
    private var reactInstanceManager: ReactInstanceManager? = null

    companion object {
        private const val ARG_COMPONENT_NAME = "componentName"
        private const val ARG_INITIAL_PROPS = "initialProps"

        fun newInstance(componentName: String, initialProps: Bundle? = null): ReactNativeFragment {
            return ReactNativeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMPONENT_NAME, componentName)
                    if (initialProps != null) {
                        putBundle(ARG_INITIAL_PROPS, initialProps)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val componentName = arguments?.getString(ARG_COMPONENT_NAME) ?: "App"
        val initialProps = arguments?.getBundle(ARG_INITIAL_PROPS)

        reactRootView = ReactRootView(requireContext())
        reactInstanceManager = (requireActivity().application as com.example.mobile_employee_simple.MobileEmployeeApplication)
            .reactNativeHost.reactInstanceManager

        reactRootView?.startReactApplication(
            reactInstanceManager,
            componentName,
            initialProps
        )

        return reactRootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        reactRootView?.unmountReactApplication()
        reactRootView = null
        reactInstanceManager = null
    }
}
*/ 