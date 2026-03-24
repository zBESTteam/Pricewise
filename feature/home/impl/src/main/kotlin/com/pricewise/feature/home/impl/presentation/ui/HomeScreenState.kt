package com.pricewise.feature.home.impl.presentation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush

sealed interface HomeScreenState {
    data object Loading : HomeScreenState

    data class Loaded(
        val searchQuery: String,
        val quickActions: List<QuickActionUiModel>,
        val popularQueries: List<PopularQueryUiModel>,
        val products: List<ProductUiModel>,
    ) : HomeScreenState

    data class Error(
        val throwable: Throwable,
    ) : HomeScreenState
}

@Immutable
data class QuickActionUiModel(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val background: Brush,
)

@Immutable
data class PopularQueryUiModel(
    val id: String,
    val title: String,
)

@Immutable
data class ProductUiModel(
    val id: String,
    val title: String,
    val price: String,
    val isFavorite: Boolean,
    val thumbnailUrl: String?,
    val productUrl: String?,
    val marketplace: MarketplaceUiModel,
)

@Immutable
data class MarketplaceUiModel(
    val name: String,
    val shortName: String,
    val logoUrl: String?,
)
