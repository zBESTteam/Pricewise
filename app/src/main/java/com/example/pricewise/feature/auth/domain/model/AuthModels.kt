package com.example.pricewise.feature.auth.domain.model

data class AuthUser(
    val id: Long,
    val email: String,
)

data class AuthSession(
    val accessToken: String,
    val tokenType: String = "bearer",
    val user: AuthUser,
)

data class LoginInput(
    val email: String,
    val password: String,
)

data class RegisterInput(
    val email: String,
    val password: String,
    val passwordConfirm: String,
)
