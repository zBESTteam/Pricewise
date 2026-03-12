package com.pricewise.feature.auth.impl.data.repository

import com.pricewise.feature.auth.impl.domain.model.AuthSession
import com.pricewise.feature.auth.impl.domain.model.AuthUser
import com.pricewise.feature.auth.impl.domain.model.LoginInput
import com.pricewise.feature.auth.impl.domain.model.RegisterInput
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
//private val api: PricewiseApi = NetworkModule.api
) : AuthRepository {
    override suspend fun signUp(input: RegisterInput): AuthSession {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(input: LoginInput): AuthSession {
        TODO("Not yet implemented")
    }

    override suspend fun getMe(token: String): AuthUser {
        TODO("Not yet implemented")
    }

    private fun saveToken(session: AuthSession) {
        TODO("Not yet implemented")
    }
}
