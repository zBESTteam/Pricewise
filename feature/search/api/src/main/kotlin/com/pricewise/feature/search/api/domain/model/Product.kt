package com.pricewise.feature.search.api.domain.model

data class Product(
    val id: String,
    val title: String,
    val price: Long,
    val merchant: Merchant,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
)
