package com.pricewise.feature.auth.impl.data.repository

import com.pricewise.core.auth.TokenManager
import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.dto.LoginRequestDto
import com.pricewise.core.network.dto.RegisterRequestDto
import com.pricewise.feature.auth.impl.data.mapper.toDomain
import com.pricewise.feature.auth.impl.domain.model.AuthSession
import com.pricewise.feature.auth.impl.domain.model.AuthUser
import com.pricewise.feature.auth.impl.domain.model.LoginInput
import com.pricewise.feature.auth.impl.domain.model.RegisterInput
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PriceWiseApi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signUp(input: RegisterInput): AuthSession {
        val session = api.register(
            RegisterRequestDto(
                email = input.email,
                password = input.password,
                passwordConfirm = input.passwordConfirm
            )
        ).toDomain()
        tokenManager.saveToken(session.accessToken)
        return session
    }

    override suspend fun signIn(input: LoginInput): AuthSession {
        val session = api.login(
            LoginRequestDto(
                email = input.email,
                password = input.password
            )
        ).toDomain()
        tokenManager.saveToken(session.accessToken)
        return session
    }

    override suspend fun getMe(token: String): AuthUser {
        val authHeader = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"
        return api.getMe(authHeader).toDomain()
    }
}
