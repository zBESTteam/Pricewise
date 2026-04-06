package com.pricewise.feature.favorites.impl.presentation.viewmodel

import com.pricewise.core.ui.components.PriceWiseProductCardModel

enum class FavoritesSortOption {
    NONE,
    PRICE_ASC,
    PRICE_DESC,
    BRAND_ASC,
}

data class FavoritesUiState(
    val allItems: List<PriceWiseProductCardModel> = emptyList(),
    val items: List<PriceWiseProductCardModel> = emptyList(),
    val sortOption: FavoritesSortOption = FavoritesSortOption.NONE,
    val onlyMarketplaces: Boolean = false,
    val onlyOfflineShops: Boolean = false,
    val priceFrom: Long = 0L,
    val priceTo: Long = 0L,
    val isLoading: Boolean = false,
    val error: String? = null,
)
