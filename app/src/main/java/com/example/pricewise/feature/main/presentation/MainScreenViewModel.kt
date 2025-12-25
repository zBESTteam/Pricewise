package com.example.pricewise.feature.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.core.network.TokenStorage
import com.example.pricewise.feature.favorites.data.ApiFavoritesRepository
import com.example.pricewise.feature.favorites.domain.model.FavoriteCreate
import com.example.pricewise.feature.favorites.domain.repository.FavoritesRepository
import com.example.pricewise.feature.main.data.ApiMainRepository
import com.example.pricewise.feature.main.domain.model.Product
import com.example.pricewise.feature.main.domain.usecase.GetMainScreenDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    private val favoritesRepository: FavoritesRepository = ApiFavoritesRepository()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
    
    fun submitSearch() {
        // Заглушка, если поиска пока нет в этом VM
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
    
    fun toggleFavorite(product: Product) {
        val currentList = _uiState.value.recommendations
        val isFavorite = product.isFavorite
        
        // Оптимистичное обновление UI
        val updatedList = currentList.map { 
            if (it.id == product.id) it.copy(isFavorite = !isFavorite) else it
        }
        _uiState.update { it.copy(recommendations = updatedList) }
        
        viewModelScope.launch {
            val token = TokenStorage.getAuthHeader()
            if (token.isBlank() || token == "Bearer " || token == "Bearer null") {
                // Если нет токена, откатываем изменение (или показываем тост, но здесь у нас нет контекста)
                // Для простоты оставим UI обновленным, но в реальном приложении надо бы уведомить
                Log.e(TAG, "Cannot toggle favorite: No token")
                 _uiState.update { it.copy(recommendations = currentList) } // Откат
                return@launch
            }

            runCatching {
                // У ProductRecommendation id может быть не Long, а String (мы это меняли в маппере)
                // Но в модели ProductRecommendation id: String.
                // А методы репозитория:
                // favoriteRecommendation(token, recommendationId: Long)
                // add(token, favorite: FavoriteCreate)
                
                // Если id парсится в Long, то это recommendationId. Если нет - это externalId.
                val recId = product.id.toLongOrNull()
                
                if (isFavorite) {
                    // Было избранное -> удаляем
                    if (recId != null) {
                        favoritesRepository.unfavoriteRecommendation(token, recId)
                    } else {
                        favoritesRepository.remove(token, product.id, product.merchant.id)
                    }
                } else {
                    // Не было -> добавляем
                    if (recId != null) {
                        favoritesRepository.favoriteRecommendation(token, recId)
                    } else {
                        // Если это не рекомендация с ID, а просто товар, добавляем через add
                        favoritesRepository.add(
                            token, 
                            FavoriteCreate(
                                externalId = product.id,
                                source = product.merchant.id,
                                title = product.title,
                                price = product.price,
                                thumbnailUrl = product.thumbnailUrl,
                                productUrl = "", // Нет в модели
                                merchantLogoUrl = product.merchant.logoUrl
                            )
                        )
                    }
                }
            }.onFailure { e ->
                Log.e(TAG, "Failed to toggle favorite", e)
                // Откат при ошибке
                _uiState.update { it.copy(recommendations = currentList) }
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
