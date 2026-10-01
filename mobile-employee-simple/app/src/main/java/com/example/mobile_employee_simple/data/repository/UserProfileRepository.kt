package com.example.mobile_employee_simple.data.repository

import com.example.mobile_employee_simple.data.model.UserProfile
import com.example.mobile_employee_simple.utils.LanguageManager

interface UserProfileRepository {
    fun getUserProfile(username: String): UserProfile
}

class MockUserProfileRepositoryImpl : UserProfileRepository {
    override fun getUserProfile(username: String): UserProfile {
        val isZh = LanguageManager.isChinese()
        return if (isZh) {
            UserProfile(
                username = username,
                departmentAndRole = "供应链运营部 · 资深仓储主管",
                employeeIdAndLevel = "工号: EMP202609 · 权限级别: L3"
            )
        } else {
            UserProfile(
                username = username,
                departmentAndRole = "Supply Chain Operations · Senior Warehouse Lead",
                employeeIdAndLevel = "Emp ID: EMP202609 · Level: L3"
            )
        }
    }
}
