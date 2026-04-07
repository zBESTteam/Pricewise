package com.pricewise.core.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeManager {
    const val PREFS_NAME = "theme_prefs"
    const val BOOLEAN_KEY = "is_dark_theme"
    var isDarkTheme by mutableStateOf(false)
        private set

    fun toggleTheme(context: Context) {
        isDarkTheme = !isDarkTheme
        saveTheme(context)
    }

    fun loadTheme(context: Context){
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getBoolean(BOOLEAN_KEY, false)
        isDarkTheme = saved
    }

    private fun saveTheme(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(BOOLEAN_KEY, isDarkTheme)
            apply()
        }
    }
}