package com.pricewise.feature.auth.impl.presentation.viewmodel

import com.pricewise.feature.auth.impl.domain.model.AuthSession

data class RegistrationUIState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: AuthSession? = null
)
