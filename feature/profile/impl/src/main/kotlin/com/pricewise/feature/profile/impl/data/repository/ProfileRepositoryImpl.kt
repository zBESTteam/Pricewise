package com.pricewise.feature.profile.impl.data.repository

import com.pricewise.core.auth.TokenManager
import com.pricewise.core.network.PriceWiseApi
import com.pricewise.feature.profile.impl.data.mapper.toDomain
import com.pricewise.feature.profile.impl.data.mapper.toDto
import com.pricewise.feature.profile.impl.domain.model.PasswordChange
import com.pricewise.feature.profile.impl.domain.model.Profile
import com.pricewise.feature.profile.impl.domain.model.ProfileUpdate
import com.pricewise.feature.profile.impl.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: PriceWiseApi,
    private val tokenManager: TokenManager,
) : ProfileRepository {

    override suspend fun getProfile(): Profile =
        api.getProfile(authHeader()).toDomain()

    override suspend fun updateProfile(update: ProfileUpdate): Profile =
        api.updateProfile(authHeader(), update.toDto()).toDomain()

    override suspend fun changePassword(change: PasswordChange) {
        api.changePassword(authHeader(), change.toDto())
    }

    private fun authHeader(): String {
        val token = tokenManager.getToken()?.trim().orEmpty()
        check(token.isNotEmpty()) { "Missing access token" }
        return if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"
    }
}
