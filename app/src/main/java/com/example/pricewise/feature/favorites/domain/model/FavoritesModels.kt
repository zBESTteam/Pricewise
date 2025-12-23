package com.example.pricewise.feature.favorites.domain.model

data class FavoriteItem(
    val id: Long,
    val externalId: String,
    val source: String,
    val title: String,
    val price: Long,
    val thumbnailUrl: String,
    val productUrl: String,
    val merchantLogoUrl: String,
)

data class FavoriteCreate(
    val externalId: String,
    val source: String,
    val title: String,
    val price: Long,
    val thumbnailUrl: String = "",
    val productUrl: String = "",
    val merchantLogoUrl: String = "",
)
