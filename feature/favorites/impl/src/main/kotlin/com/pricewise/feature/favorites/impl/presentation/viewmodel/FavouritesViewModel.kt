package com.pricewise.feature.favorites.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.HttpException
import com.pricewise.core.auth.TokenManager
import com.pricewise.core.ui.components.PriceWiseProductCardModel
import com.pricewise.feature.favorites.impl.domain.model.FavoriteItem
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class FavouritesViewModel @Inject constructor (
    val items: List<PriceWiseProductCardModel>,
    val isLoading: Boolean,
    val error: String?,
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val token = repository.getAuthHeader()
        if (token.isBlank() || token == "Bearer " || token == "Bearer null") {
            _uiState.update { it.copy(isLoading = false, error = "Необходима авторизация") }
            return@launch
        }

        runCatching {
            repository.list()
        }.onSuccess { favorites ->
            val products = favorites.map { it.toPriceWiseProductCardModel() }
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

    fun removeFavorite(product: PriceWiseProductCardModel) = viewModelScope.launch {
        val oldItems = _uiState.value.items
        _uiState.update { it.copy(items = oldItems.filter { item -> item.id != product.id }) }

        val token = repository.getAuthHeader()
        val externalId = product.id

        runCatching {
            repository.remove(token, externalId)
        }.onFailure { e ->
            _uiState.update { it.copy(items = oldItems, error = "Ошибка удаления: ${e.message}") }
        }
    }
}

private fun FavoriteItem.toPriceWiseProductCardModel(): PriceWiseProductCardModel {
    return PriceWiseProductCardModel(
        id = this.externalId,
        title = this.title,
        price = this.price.toString(),
        thumbnailUrl = this.thumbnailUrl,
        marketplaceName = this.source,
        marketplaceShortName = this.source,
        marketplaceLogoUrl = this.merchantLogoUrl,
        isFavorite = true
    )
}