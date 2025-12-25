package com.example.pricewise.feature.auth.presentation

import com.example.pricewise.feature.auth.domain.model.AuthSession

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: AuthSession? = null
)