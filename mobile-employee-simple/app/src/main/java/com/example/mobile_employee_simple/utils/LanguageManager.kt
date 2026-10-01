package com.example.mobile_employee_simple.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.annotation.StringRes
import com.example.mobile_employee_simple.MobileEmployeeApplication
import java.util.Locale

object LanguageManager {
    const val LANGUAGE_ZH = "zh-CN"
    const val LANGUAGE_EN = "en"
    private const val PREFS_NAME = "app_language_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_LANGUAGE)) {
            // 默认设置为中文界面
            prefs.edit().putString(KEY_LANGUAGE, LANGUAGE_ZH).apply()
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(LANGUAGE_ZH))
        } else {
            val saved = prefs.getString(KEY_LANGUAGE, LANGUAGE_ZH) ?: LANGUAGE_ZH
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            if (currentLocales.isEmpty) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(saved))
            }
        }
    }

    fun getCurrentLanguage(context: Context = MobileEmployeeApplication.instance): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        if (!appLocales.isEmpty) {
            val tag = appLocales.get(0)?.toLanguageTag() ?: ""
            if (tag.startsWith("zh", ignoreCase = true)) return LANGUAGE_ZH
            if (tag.startsWith("en", ignoreCase = true)) return LANGUAGE_EN
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_ZH) ?: LANGUAGE_ZH
    }

    fun isChinese(context: Context = MobileEmployeeApplication.instance): Boolean {
        return getCurrentLanguage(context) == LANGUAGE_ZH
    }

    fun setLanguage(context: Context, languageTag: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageTag).apply()
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }

    fun toggleLanguage(context: Context): String {
        val nextLang = if (isChinese(context)) LANGUAGE_EN else LANGUAGE_ZH
        setLanguage(context, nextLang)
        return nextLang
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        val context = MobileEmployeeApplication.instance
        return if (formatArgs.isNotEmpty()) {
            context.getString(resId, *formatArgs)
        } else {
            context.getString(resId)
        }
    }
}
