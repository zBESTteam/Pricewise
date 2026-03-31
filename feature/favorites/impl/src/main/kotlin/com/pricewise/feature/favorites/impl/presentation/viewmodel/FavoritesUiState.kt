package com.pricewise.feature.favorites.impl.presentation.viewmodel

data class FavoritesUiState(
    val items: List<ProductRecommendation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
