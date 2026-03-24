package com.pricewise.feature.home.impl.presentation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class MainScreenState(
    val searchQuery: String,
    val isLoading: Boolean,
    val quickActions: List<QuickActionUiModel>,
    val popularQueries: List<PopularQueryUiModel>,
    val products: List<ProductUiModel>,
)

@Immutable
data class QuickActionUiModel(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val icon: ImageVector,
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
    val thumbnailStyle: ProductThumbnailStyle,
    val thumbnailBackground: Brush,
)

@Immutable
data class MarketplaceUiModel(
    val name: String,
    val shortName: String,
    val logoUrl: String?,
    val badgeBrush: Brush,
)

enum class ProductThumbnailStyle {
    Phone,
    Keyboard,
}
