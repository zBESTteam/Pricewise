package com.example.pricewise.feature.auth.domain.repository

import com.example.pricewise.feature.auth.domain.model.AuthSession
import com.example.pricewise.feature.auth.domain.model.AuthUser
import com.example.pricewise.feature.auth.domain.model.LoginInput
import com.example.pricewise.feature.auth.domain.model.RegisterInput

interface AuthRepository {
    suspend fun register(input: RegisterInput): AuthSession
    suspend fun login(input: LoginInput): AuthSession
    suspend fun getMe(token: String): AuthUser
}
