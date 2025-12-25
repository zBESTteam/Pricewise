package com.example.pricewise.core.network

import android.content.Context
import android.content.SharedPreferences

object TokenStorage {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"

    private var prefs: SharedPreferences? = null
    
    // Храним в памяти для быстрого доступа
    var accessToken: String? = null
        private set

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        accessToken = prefs?.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveToken(token: String) {
        accessToken = token
        prefs?.edit()?.putString(KEY_ACCESS_TOKEN, token)?.apply()
    }

    fun clear() {
        accessToken = null
        prefs?.edit()?.remove(KEY_ACCESS_TOKEN)?.apply()
    }

    fun getAuthHeader(): String {
        // Принудительно используем Bearer с большой буквы, чтобы избежать проблем с сервером
        return "Bearer ${accessToken.orEmpty()}"
    }
}