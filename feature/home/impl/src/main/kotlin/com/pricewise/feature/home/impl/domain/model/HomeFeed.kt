package com.pricewise.feature.home.impl.domain.model

data class HomeFeed(
    val quickActions: List<HomeQuickAction>,
    val popularQueries: List<HomePopularQuery>,
    val products: List<HomeProduct>,
)

data class HomeQuickAction(
    val id: String,
    val title: String,
    val imageUrl: String?,
)

data class HomePopularQuery(
    val id: String,
    val title: String,
)

data class HomeProduct(
    val id: String,
    val title: String,
    val price: Long,
    val isFavorite: Boolean,
    val thumbnailUrl: String?,
    val productUrl: String?,
    val marketplace: HomeMarketplace,
)

data class HomeMarketplace(
    val name: String,
    val logoUrl: String?,
)
