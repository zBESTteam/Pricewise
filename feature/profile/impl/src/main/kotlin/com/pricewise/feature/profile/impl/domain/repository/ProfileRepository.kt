package com.pricewise.feature.profile.impl.domain.repository

import com.pricewise.feature.profile.impl.domain.model.PasswordChange
import com.pricewise.feature.profile.impl.domain.model.Profile
import com.pricewise.feature.profile.impl.domain.model.ProfileUpdate

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(update: ProfileUpdate): Profile
    suspend fun changePassword(change: PasswordChange)
}
