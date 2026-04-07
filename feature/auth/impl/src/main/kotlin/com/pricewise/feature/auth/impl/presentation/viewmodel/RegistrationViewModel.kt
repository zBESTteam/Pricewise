package com.pricewise.feature.auth.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.feature.auth.impl.domain.model.RegisterInput
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onPasswordConfirmChange(value: String) = _uiState.update { it.copy(passwordConfirm = value) }

    fun register() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        val currentState = _uiState.value

        if (currentState.password != currentState.passwordConfirm) {
            _uiState.update { it.copy(isLoading = false, error = "Пароли не совпадают") }
            return@launch
        }

        runCatching {
            repo.signUp(
                RegisterInput(
                    currentState.email,
                    currentState.password,
                    currentState.passwordConfirm
                )
            )
        }.onSuccess { session ->
            _uiState.update { it.copy(isLoading = false, session = session) }
        }.onFailure { throwable ->
            _uiState.update { it.copy(isLoading = false, error = throwable.message) }
        }
    }
}
