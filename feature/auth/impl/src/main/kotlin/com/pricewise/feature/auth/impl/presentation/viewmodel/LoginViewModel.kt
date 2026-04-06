package com.pricewise.feature.auth.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.core.auth.TokenManager
import com.pricewise.feature.auth.impl.domain.model.LoginInput
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    tokenManager: TokenManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    init {
        val hasSavedSession = !tokenManager.getToken().isNullOrBlank()
        if (hasSavedSession) {
            _uiState.update { state -> state.copy(hasSavedSession = true) }
        }
    }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }

    fun login() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        val currentState = _uiState.value

        runCatching {
            repo.signIn(LoginInput(currentState.email, currentState.password))
        }.onSuccess { session ->
            _uiState.update { it.copy(isLoading = false, session = session) }
        }.onFailure { throwable ->
            _uiState.update { it.copy(isLoading = false, error = throwable.message) }
        }
    }
}
