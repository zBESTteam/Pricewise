package com.pricewise.feature.profile.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class ProfileData(
    val firstName: String = "Иван",
    val lastName: String = "Иванов",
    val city: String = "Москва"
)

private const val PREFS_NAME = "profile_prefs"
private const val KEY_FIRST_NAME = "first_name"
private const val KEY_LAST_NAME = "last_name"
private const val KEY_CITY = "city"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun loadProfile(): ProfileData {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return ProfileData(
            firstName = prefs.getString(KEY_FIRST_NAME, "Иван") ?: "Иван",
            lastName = prefs.getString(KEY_LAST_NAME, "Иванов") ?: "Иванов",
            city = prefs.getString(KEY_CITY, "Москва") ?: "Москва"
        )
    }

    fun saveProfile(profile: ProfileData) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_FIRST_NAME, profile.firstName)
            .putString(KEY_LAST_NAME, profile.lastName)
            .putString(KEY_CITY, profile.city)
            .apply()
    }
}
