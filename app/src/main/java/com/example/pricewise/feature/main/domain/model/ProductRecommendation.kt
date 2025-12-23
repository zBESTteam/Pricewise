package com.example.pricewise.feature.main.domain.model

data class ProductRecommendation(
    val id: String,
    val title: String,
    val price: Long,
    val merchant: Merchant,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
)
