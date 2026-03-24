package com.pricewise.feature.home.impl.domain.model

data class HomeFeed(
    val searchQuery: String,
    val quickActions: List<HomeQuickAction>,
    val popularQueries: List<HomePopularQuery>,
    val products: List<HomeProduct>,
)

data class HomeQuickAction(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val iconType: HomeQuickActionIconType,
    val gradientColors: List<Long>,
)

data class HomePopularQuery(
    val id: String,
    val title: String,
)

data class HomeProduct(
    val id: String,
    val title: String,
    val price: String,
    val isFavorite: Boolean,
    val thumbnailUrl: String?,
    val productUrl: String?,
    val marketplace: HomeMarketplace,
    val thumbnailStyle: HomeProductThumbnailStyle,
    val thumbnailColors: List<Long>,
)

data class HomeMarketplace(
    val name: String,
    val shortName: String,
    val logoUrl: String?,
    val badgeColors: List<Long>,
)

enum class HomeQuickActionIconType {
    SearchGuide,
    SearchSettings,
    AiRecommendations,
    Favorites,
}

enum class HomeProductThumbnailStyle {
    Phone,
    Keyboard,
}
