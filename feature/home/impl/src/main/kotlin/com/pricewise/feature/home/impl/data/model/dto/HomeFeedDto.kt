package com.pricewise.feature.home.impl.data.model.dto

data class HomeFeedDto(
    val searchQuery: String,
    val quickActions: List<QuickActionDto>,
    val popularQueries: List<PopularQueryDto>,
    val products: List<ProductDto>,
)

data class QuickActionDto(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val iconType: QuickActionIconTypeDto,
    val gradientColors: List<Long>,
)

data class PopularQueryDto(
    val id: String,
    val title: String,
)

data class ProductDto(
    val id: String,
    val title: String,
    val price: String,
    val isFavorite: Boolean,
    val thumbnailUrl: String?,
    val productUrl: String?,
    val marketplace: MarketplaceDto,
    val thumbnailStyle: ProductThumbnailStyleDto,
    val thumbnailColors: List<Long>,
)

data class MarketplaceDto(
    val name: String,
    val shortName: String,
    val logoUrl: String?,
    val badgeColors: List<Long>,
)

enum class QuickActionIconTypeDto {
    SearchGuide,
    SearchSettings,
    AiRecommendations,
    Favorites,
}

enum class ProductThumbnailStyleDto {
    Phone,
    Keyboard,
}
