package com.pricewise.feature.profile.impl.data.mapper

import com.pricewise.core.network.dto.PasswordChangeRequestDto
import com.pricewise.core.network.dto.ProfileDto
import com.pricewise.core.network.dto.ProfileUpdateRequestDto
import com.pricewise.feature.profile.impl.domain.model.PasswordChange
import com.pricewise.feature.profile.impl.domain.model.Profile
import com.pricewise.feature.profile.impl.domain.model.ProfileUpdate

fun ProfileDto.toDomain(): Profile {
    val idValue = id ?: 0L
    val emailValue = email?.trim().orEmpty()
    check(idValue > 0 && emailValue.isNotEmpty()) { "Invalid profile data" }
    return Profile(
        id = idValue,
        email = emailValue,
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        city = city.orEmpty(),
        region = region.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
    )
}

fun ProfileUpdate.toDto(): ProfileUpdateRequestDto = ProfileUpdateRequestDto(
    firstName = firstName,
    lastName = lastName,
    city = city,
    region = region,
    avatarUrl = avatarUrl,
)

fun PasswordChange.toDto(): PasswordChangeRequestDto = PasswordChangeRequestDto(
    currentPassword = currentPassword,
    newPassword = newPassword,
    newPasswordConfirm = newPasswordConfirm,
)
