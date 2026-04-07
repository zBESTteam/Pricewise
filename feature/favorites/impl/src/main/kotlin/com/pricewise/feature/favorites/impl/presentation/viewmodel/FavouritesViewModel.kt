package com.pricewise.feature.favorites.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.core.ui.components.PriceWiseProductCardModel
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.favorites.impl.domain.model.FavoriteItem
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor (
    private val repository: FavoritesRepository,
    private val favoritesFeatureApi: FavoritesFeatureApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val token = runCatching { repository.getAuthHeader() }
            .getOrElse { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка доступа к данным пользователя",
                    )
                }
                return@launch
            }
        if (token.isBlank() || token == "Bearer " || token == "Bearer null") {
            _uiState.update { it.copy(isLoading = false, error = "Необходима авторизация") }
            return@launch
        }

        runCatching {
            repository.list()
        }.onSuccess { favorites ->
            val products = favorites
                .map { it.toPriceWiseProductCardModel() }
                .distinctBy { item -> item.id to item.marketplaceName }
            _uiState.update { state ->
                applyFiltersAndSorting(
                    state.copy(
                        isLoading = false,
                        allItems = products,
                        error = null,
                    ),
                )
            }
        }.onFailure { e ->
            val errorMessage = if (e is HttpException && e.code() == 401) {
                "Сессия истекла. Перезайдите в приложение."
            } else {
                e.message ?: "Ошибка загрузки"
            }
            _uiState.update { it.copy(isLoading = false, error = errorMessage) }
        }
    }

    fun setSortOption(option: FavoritesSortOption) {
        _uiState.update { state -> applyFiltersAndSorting(state.copy(sortOption = option)) }
    }

    fun setOnlyMarketplaces(value: Boolean) {
        _uiState.update { state ->
            applyFiltersAndSorting(
                state.copy(
                    onlyMarketplaces = value,
                    onlyOfflineShops = if (value && state.onlyOfflineShops) false else state.onlyOfflineShops,
                ),
            )
        }
    }

    fun setOnlyOfflineShops(value: Boolean) {
        _uiState.update { state ->
            applyFiltersAndSorting(
                state.copy(
                    onlyOfflineShops = value,
                    onlyMarketplaces = if (value && state.onlyMarketplaces) false else state.onlyMarketplaces,
                ),
            )
        }
    }

    fun setPriceRange(from: Long, to: Long) {
        _uiState.update { state ->
            applyFiltersAndSorting(
                state.copy(
                    priceFrom = from.coerceAtLeast(0L),
                    priceTo = to.coerceAtLeast(0L),
                ),
            )
        }
    }

    fun removeFavorite(product: PriceWiseProductCardModel) = viewModelScope.launch {
        val oldState = _uiState.value
        val updatedAllItems = oldState.allItems.filterNot { item ->
            item.id == product.id && item.marketplaceName == product.marketplaceName
        }
        _uiState.update { state -> applyFiltersAndSorting(state.copy(allItems = updatedAllItems, error = null)) }

        val externalId = product.id
        val source = product.marketplaceName

        runCatching {
            favoritesFeatureApi.removeFromFavorites(externalId = externalId, source = source)
        }.onFailure { e ->
            _uiState.value = oldState.copy(error = "Ошибка удаления: ${e.message}")
        }
    }

    private fun applyFiltersAndSorting(state: FavoritesUiState): FavoritesUiState {
        var filteredItems = state.allItems

        if (state.onlyMarketplaces) {
            filteredItems = filteredItems.filter { item -> item.marketplaceName in marketplaceSources }
        }
        if (state.onlyOfflineShops) {
            filteredItems = filteredItems.filter { item -> item.marketplaceName !in marketplaceSources }
        }
        if (state.priceFrom > 0L) {
            filteredItems = filteredItems.filter { item -> item.price.toPriceLong() >= state.priceFrom }
        }
        if (state.priceTo > 0L) {
            filteredItems = filteredItems.filter { item -> item.price.toPriceLong() <= state.priceTo }
        }

        filteredItems = when (state.sortOption) {
            FavoritesSortOption.NONE -> filteredItems
            FavoritesSortOption.PRICE_ASC -> filteredItems.sortedBy { item -> item.price.toPriceLong() }
            FavoritesSortOption.PRICE_DESC -> filteredItems.sortedByDescending { item -> item.price.toPriceLong() }
            FavoritesSortOption.BRAND_ASC -> filteredItems.sortedBy { item -> item.marketplaceName }
        }

        return state.copy(items = filteredItems)
    }

    private companion object {
        val marketplaceSources = setOf(
            "market.yandex.ru",
            "aliexpress.ru",
            "avito.ru",
            "cdek.shopping",
        )
    }
}

private fun FavoriteItem.toPriceWiseProductCardModel(): PriceWiseProductCardModel {
    return PriceWiseProductCardModel(
        id = this.externalId,
        title = this.title,
        price = rubleFormatter.format(this.price) + " ₽",
        thumbnailUrl = this.thumbnailUrl,
        marketplaceName = this.source,
        marketplaceShortName = this.source,
        marketplaceLogoUrl = this.merchantLogoUrl,
        isFavorite = true
    )
}

private fun String.toPriceLong(): Long {
    return this.filter { symbol -> symbol.isDigit() }.toLongOrNull() ?: 0L
}

private val rubleFormatter: NumberFormat =
    NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))

