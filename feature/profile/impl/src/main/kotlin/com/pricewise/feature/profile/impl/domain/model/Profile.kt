package com.pricewise.feature.profile.impl.domain.model

data class Profile(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val region: String,
    val avatarUrl: String,
)

data class ProfileUpdate(
    val firstName: String?,
    val lastName: String?,
    val city: String?,
    val region: String?,
    val avatarUrl: String?,
)

data class PasswordChange(
    val currentPassword: String,
    val newPassword: String,
    val newPasswordConfirm: String,
)
