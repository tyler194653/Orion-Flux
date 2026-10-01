package com.example.mobile_employee_simple

import android.app.Application
import com.example.mobile_employee_simple.utils.LanguageManager

class MobileEmployeeApplication : Application() {

    companion object {
        lateinit var instance: MobileEmployeeApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Initialize application-level language configuration
        LanguageManager.init(this)
    }
}