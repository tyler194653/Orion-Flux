package com.example.mobile_employee_simple.data

import com.example.mobile_employee_simple.data.repository.*

object DataRepositoryProvider {
    val approvalsRepository: ApprovalsRepository by lazy { MockApprovalsRepositoryImpl() }
    val userProfileRepository: UserProfileRepository by lazy { MockUserProfileRepositoryImpl() }
    val aiChatRepository: AIChatRepository by lazy { MockAIChatRepositoryImpl() }
    val inventoryRepository: InventoryRepository by lazy { MockInventoryRepositoryImpl() }
}
