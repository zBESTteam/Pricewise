package com.pricewise.feature.auth.impl.data.repository

import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.di.NetworkModule.provideMoshi
import com.pricewise.core.network.di.NetworkModule.provideOkHttpClient
import com.pricewise.core.network.di.NetworkModule.providePriceWiseApi
import com.pricewise.core.network.di.NetworkModule.provideRetrofit
import com.pricewise.core.network.dto.LoginRequestDto
import com.pricewise.core.network.dto.RegisterRequestDto
import com.pricewise.feature.auth.impl.data.mapper.asBearer
import com.pricewise.feature.auth.impl.data.mapper.toDomain
import com.pricewise.feature.auth.impl.domain.model.AuthSession
import com.pricewise.feature.auth.impl.domain.model.AuthUser
import com.pricewise.feature.auth.impl.domain.model.LoginInput
import com.pricewise.feature.auth.impl.domain.model.RegisterInput
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PriceWiseApi = providePriceWiseApi(
        provideRetrofit(
            provideOkHttpClient(),
            provideMoshi()
    ))
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
        return session
    }

    override suspend fun getMe(token: String): AuthUser {
        val response = api.getMe(authorization = token.asBearer())
        return response.toDomain()
    }
}
