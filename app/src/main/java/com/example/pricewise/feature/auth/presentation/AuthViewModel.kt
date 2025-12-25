package com.example.pricewise.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.feature.auth.data.ApiAuthRepository
import com.example.pricewise.feature.auth.domain.model.LoginInput
import com.example.pricewise.feature.auth.domain.model.RegisterInput
import com.example.pricewise.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo: AuthRepository = ApiAuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onPasswordConfirmChange(value: String) = _uiState.update { it.copy(passwordConfirm = value) }

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

    fun register() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        val currentState = _uiState.value

        if (currentState.password != currentState.passwordConfirm) {
            _uiState.update { it.copy(isLoading = false, error = "Пароли не совпадают") }
            return@launch
        }

        runCatching {
            repo.signUp(RegisterInput(currentState.email, currentState.password, currentState.passwordConfirm))
        }.onSuccess { session ->
            _uiState.update { it.copy(isLoading = false, session = session) }
        }.onFailure { throwable ->
            _uiState.update { it.copy(isLoading = false, error = throwable.message) }
        }
    }
}