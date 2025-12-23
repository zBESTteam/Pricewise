package com.example.pricewise.feature.auth.domain.repository

import com.example.pricewise.feature.auth.domain.model.AuthSession
import com.example.pricewise.feature.auth.domain.model.AuthUser
import com.example.pricewise.feature.auth.domain.model.LoginInput
import com.example.pricewise.feature.auth.domain.model.RegisterInput

interface AuthRepository {
    suspend fun signUp(input: RegisterInput): AuthSession
    suspend fun signIn(input: LoginInput): AuthSession
    suspend fun getMe(token: String): AuthUser
}
