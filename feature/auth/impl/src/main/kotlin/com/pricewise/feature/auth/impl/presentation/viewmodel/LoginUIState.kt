package com.pricewise.feature.auth.impl.presentation.viewmodel

import com.pricewise.feature.auth.impl.domain.model.AuthSession

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val hasSavedSession: Boolean = false,
    val error: String? = null,
    val session: AuthSession? = null
)
