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
            .getOrElse {
                _uiState.update {
                    it.copy(isLoading = false, error = FavoritesError.UNAUTHORIZED)
                }
                return@launch
            }
        if (token.isBlank() || token == "Bearer " || token == "Bearer null") {
            _uiState.update { it.copy(isLoading = false, error = FavoritesError.UNAUTHORIZED) }
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
            val error = if (e is HttpException && e.code() == 401) {
                FavoritesError.UNAUTHORIZED
            } else {
                FavoritesError.LOAD_FAILED
            }
            _uiState.update { it.copy(isLoading = false, error = error) }
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
        val externalId = product.id
        val source = product.marketplaceName
        val removedIndex = _uiState.value.allItems.indexOfFirst { item ->
            item.id == externalId && item.marketplaceName == source
        }
        if (removedIndex < 0) return@launch

        _uiState.update { state ->
            val updatedAllItems = state.allItems.toMutableList().apply { removeAt(removedIndex) }
            applyFiltersAndSorting(state.copy(allItems = updatedAllItems, error = null))
        }

        runCatching {
            favoritesFeatureApi.removeFromFavorites(externalId = externalId, source = source)
        }.onFailure {
            _uiState.update { state ->
                val alreadyPresent = state.allItems.any { item ->
                    item.id == externalId && item.marketplaceName == source
                }
                if (alreadyPresent) {
                    state.copy(error = FavoritesError.REMOVE_FAILED)
                } else {
                    val restored = state.allItems.toMutableList().apply {
                        add(removedIndex.coerceAtMost(size), product)
                    }
                    applyFiltersAndSorting(state.copy(allItems = restored, error = FavoritesError.REMOVE_FAILED))
                }
            }
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
        price = rubleFormatter.format(this.price),
        deliveryText = "",
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
