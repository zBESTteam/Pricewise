package com.example.pricewise.feature.auth.data

import com.example.pricewise.core.network.NetworkModule
import com.example.pricewise.core.network.PricewiseApi
import com.example.pricewise.core.network.TokenStorage
import com.example.pricewise.core.network.dto.AuthResponseDto
import com.example.pricewise.core.network.dto.LoginRequestDto
import com.example.pricewise.core.network.dto.RegisterRequestDto
import com.example.pricewise.core.network.dto.UserDto
import com.example.pricewise.feature.auth.domain.model.AuthSession
import com.example.pricewise.feature.auth.domain.model.AuthUser
import com.example.pricewise.feature.auth.domain.model.LoginInput
import com.example.pricewise.feature.auth.domain.model.RegisterInput
import com.example.pricewise.feature.auth.domain.repository.AuthRepository

class ApiAuthRepository(
    private val api: PricewiseApi = NetworkModule.api,
) : AuthRepository {

    override suspend fun signUp(input: RegisterInput): AuthSession {
        val response = api.register(
            RegisterRequestDto(
                email = input.email,
                password = input.password,
                passwordConfirm = input.passwordConfirm,
            ),
        )
        val session = response.toDomain()
        saveToken(session)
        return session
    }

    override suspend fun signIn(input: LoginInput): AuthSession {
        val response = api.login(
            LoginRequestDto(
                email = input.email,
                password = input.password,
            ),
        )
        val session = response.toDomain()
        saveToken(session)
        return session
    }

    override suspend fun getMe(token: String): AuthUser {
        val response = api.getMe(authorization = token.asBearer())
        return response.toDomain()
    }
    
    private fun saveToken(session: AuthSession) {
        TokenStorage.saveToken(session.accessToken)
    }
}

private fun AuthResponseDto.toDomain(): AuthSession {
    val token = accessToken?.trim().orEmpty()
    require(token.isNotEmpty()) { "Empty access token" }
    val type = tokenType?.trim().orEmpty().ifBlank { "bearer" }
    val userDto = user ?: throw IllegalStateException("Missing user")
    return AuthSession(accessToken = token, tokenType = type, user = userDto.toDomain())
}

private fun UserDto.toDomain(): AuthUser {
    val idValue = id ?: 0L
    val emailValue = email?.trim().orEmpty()
    require(idValue > 0 && emailValue.isNotEmpty()) { "Invalid user data" }
    return AuthUser(id = idValue, email = emailValue)
}

private fun String.asBearer(): String {
    val trimmed = trim()
    return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
        trimmed
    } else {
        "Bearer $trimmed"
    }
}
