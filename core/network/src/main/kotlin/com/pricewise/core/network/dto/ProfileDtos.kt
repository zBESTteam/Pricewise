package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class ProfileDto(
    val id: Long?,
    val email: String?,
    @param:Json(name = "first_name")
    val firstName: String?,
    @param:Json(name = "last_name")
    val lastName: String?,
    val region: String?,
    @param:Json(name = "avatar_url")
    val avatarUrl: String?,
)

data class ProfileUpdateRequestDto(
    @param:Json(name = "first_name")
    val firstName: String?,
    @param:Json(name = "last_name")
    val lastName: String?,
    val region: String?,
    @param:Json(name = "avatar_url")
    val avatarUrl: String?,
)

data class PasswordChangeRequestDto(
    @param:Json(name = "current_password")
    val currentPassword: String,
    @param:Json(name = "new_password")
    val newPassword: String,
    @param:Json(name = "new_password_confirm")
    val newPasswordConfirm: String,
)
