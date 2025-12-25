package com.example.pricewise.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.core.network.TokenStorage
import com.example.pricewise.feature.favorites.data.ApiFavoritesRepository
import com.example.pricewise.feature.favorites.domain.model.FavoriteItem
import com.example.pricewise.feature.favorites.domain.repository.FavoritesRepository
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class FavoritesUiState(
    val items: List<ProductRecommendation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoritesViewModel : ViewModel() {

    private val repository: FavoritesRepository = ApiFavoritesRepository()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        val token = TokenStorage.getAuthHeader()
        if (token.isBlank() || token == "Bearer " || token == "Bearer null") {
            _uiState.update { it.copy(isLoading = false, error = "Необходима авторизация") }
            return@launch
        }

        runCatching {
            repository.list(token)
        }.onSuccess { favorites ->
            val products = favorites.map { it.toProductRecommendation() }
            _uiState.update { it.copy(isLoading = false, items = products) }
        }.onFailure { e ->
            val errorMessage = if (e is HttpException && e.code() == 401) {
                "Сессия истекла. Перезайдите в приложение."
            } else {
                e.message ?: "Ошибка загрузки"
            }
            _uiState.update { it.copy(isLoading = false, error = errorMessage) }
        }
    }

    fun removeFavorite(product: ProductRecommendation) = viewModelScope.launch {
        val oldItems = _uiState.value.items
        _uiState.update { it.copy(items = oldItems.filter { item -> item.id != product.id }) }

        val token = TokenStorage.getAuthHeader()
        val source = product.merchant.id 
        val externalId = product.id 

        runCatching {
            repository.remove(token, externalId, source)
        }.onFailure { e ->
            _uiState.update { it.copy(items = oldItems, error = "Ошибка удаления: ${e.message}") }
        }
    }
}

private fun FavoriteItem.toProductRecommendation(): ProductRecommendation {
    return ProductRecommendation(
        id = this.externalId,
        title = this.title,
        price = this.price,
        merchant = Merchant(
            id = this.source,
            name = this.source,
            logoUrl = this.merchantLogoUrl
        ),
        thumbnailUrl = this.thumbnailUrl,
        isFavorite = true
    )
}