package com.example.pricewise.feature.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.feature.main.data.ApiMainRepository
import com.example.pricewise.feature.main.domain.usecase.GetMainScreenDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val useCase = MainScreenDependencies.defaultUseCase()
            runCatching { useCase() }
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            banners = data.banners,
                            popularQueries = data.popularQueries,
                            recommendations = data.recommendations
                        )
                    }
                    Log.d(
                        TAG,
                        "Loaded banners=${data.banners.size}, popular=${data.popularQueries.size}, recommendations=${data.recommendations.size}"
                    )
                }
                .onFailure { err ->
                    Log.e(TAG, "Failed to load main screen data", err)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}

private object MainScreenDependencies {
    fun defaultUseCase(): GetMainScreenDataUseCase {
        return GetMainScreenDataUseCase(ApiMainRepository())
    }
}

private const val TAG = "MainScreenVM"
