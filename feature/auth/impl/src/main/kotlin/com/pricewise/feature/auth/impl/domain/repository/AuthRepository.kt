package com.pricewise.feature.auth.impl.domain.repository

import com.pricewise.feature.auth.impl.domain.model.AuthSession
import com.pricewise.feature.auth.impl.domain.model.AuthUser
import com.pricewise.feature.auth.impl.domain.model.LoginInput
import com.pricewise.feature.auth.impl.domain.model.RegisterInput

interface AuthRepository {
    suspend fun signUp(input: RegisterInput): AuthSession
    suspend fun signIn(input: LoginInput): AuthSession
    suspend fun getMe(token: String): AuthUser
}
