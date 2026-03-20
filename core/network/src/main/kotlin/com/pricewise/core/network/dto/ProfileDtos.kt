package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class ProfileDto(
    val id: Long?,
    val email: String?,
    @field:Json(name = "first_name")
    val firstName: String?,
    @field:Json(name = "last_name")
    val lastName: String?,
    val region: String?,
    @field:Json(name = "avatar_url")
    val avatarUrl: String?,
)

data class ProfileUpdateRequestDto(
    @field:Json(name = "first_name")
    val firstName: String?,
    @field:Json(name = "last_name")
    val lastName: String?,
    val region: String?,
    @field:Json(name = "avatar_url")
    val avatarUrl: String?,
)

data class PasswordChangeRequestDto(
    @field:Json(name = "current_password")
    val currentPassword: String,
    @field:Json(name = "new_password")
    val newPassword: String,
    @field:Json(name = "new_password_confirm")
    val newPasswordConfirm: String,
)
