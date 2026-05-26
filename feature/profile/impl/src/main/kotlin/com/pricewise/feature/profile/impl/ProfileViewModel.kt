package com.pricewise.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.feature.profile.impl.domain.model.PasswordChange
import com.pricewise.feature.profile.impl.domain.model.Profile
import com.pricewise.feature.profile.impl.domain.model.ProfileUpdate
import com.pricewise.feature.profile.impl.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val profile: Profile? = null,
    val loadError: String? = null,
    val saveError: String? = null,
    val passwordError: String? = null,
    val savedAt: Long = 0L,
    val passwordChangedAt: Long = 0L,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, loadError = null) }
        viewModelScope.launch {
            runCatching { repository.getProfile() }
                .onSuccess { profile ->
                    _uiState.update { it.copy(isLoading = false, profile = profile) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, loadError = error.message ?: "Не удалось загрузить профиль")
                    }
                }
        }
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        city: String,
    ) {
        if (_uiState.value.isSaving) return
        _uiState.update { it.copy(isSaving = true, saveError = null) }
        viewModelScope.launch {
            runCatching {
                repository.updateProfile(
                    ProfileUpdate(
                        firstName = firstName.trim().ifEmpty { null },
                        lastName = lastName.trim().ifEmpty { null },
                        city = city.trim().ifEmpty { null },
                        region = null,
                        avatarUrl = null,
                    )
                )
            }
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            profile = profile,
                            savedAt = System.currentTimeMillis(),
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isSaving = false, saveError = error.message ?: "Не удалось сохранить профиль")
                    }
                }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        newPasswordConfirm: String,
    ) {
        if (_uiState.value.isSaving) return
        if (newPassword != newPasswordConfirm) {
            _uiState.update { it.copy(passwordError = "Пароли не совпадают") }
            return
        }
        _uiState.update { it.copy(isSaving = true, passwordError = null) }
        viewModelScope.launch {
            runCatching {
                repository.changePassword(
                    PasswordChange(
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        newPasswordConfirm = newPasswordConfirm,
                    )
                )
            }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            passwordChangedAt = System.currentTimeMillis(),
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isSaving = false, passwordError = error.message ?: "Не удалось изменить пароль")
                    }
                }
        }
    }

    fun consumeSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    fun consumePasswordError() {
        _uiState.update { it.copy(passwordError = null) }
    }
}
