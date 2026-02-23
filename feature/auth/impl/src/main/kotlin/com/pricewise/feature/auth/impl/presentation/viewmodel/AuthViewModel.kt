package com.pricewise.feature.auth.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
//    private val _uiState = MutableStateFlow(AuthUiState())
//    val uiState = _uiState.asStateFlow()
}
