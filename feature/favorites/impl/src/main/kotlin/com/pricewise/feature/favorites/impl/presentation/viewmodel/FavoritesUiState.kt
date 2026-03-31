package com.pricewise.feature.favorites.impl.presentation.viewmodel

import com.pricewise.core.ui.components.PriceWiseProductCardModel

data class FavoritesUiState(
    val items: List<PriceWiseProductCardModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
