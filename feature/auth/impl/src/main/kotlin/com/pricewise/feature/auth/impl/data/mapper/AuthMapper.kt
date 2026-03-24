package com.pricewise.feature.auth.impl.data.mapper

import com.pricewise.core.network.dto.AuthResponseDto
import com.pricewise.core.network.dto.UserDto
import com.pricewise.feature.auth.impl.domain.model.AuthSession
import com.pricewise.feature.auth.impl.domain.model.AuthUser

fun AuthResponseDto.toDomain(): AuthSession {
    val token = accessToken?.trim().orEmpty()
    require(token.isNotEmpty()) { "Empty access token" }
    val type = tokenType?.trim().orEmpty().ifBlank { "bearer" }
    val userDto = user ?: throw IllegalStateException("Missing user")
    return AuthSession(accessToken = token, tokenType = type, user = userDto.toDomain())
}

fun UserDto.toDomain(): AuthUser {
    val idValue = id ?: 0L
    val emailValue = email?.trim().orEmpty()
    require(idValue > 0 && emailValue.isNotEmpty()) { "Invalid user data" }
    return AuthUser(id = idValue, email = emailValue)
}

fun String.asBearer(): String {
    val trimmed = trim()
    return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
        trimmed
    } else {
        "Bearer $trimmed"
    }
}