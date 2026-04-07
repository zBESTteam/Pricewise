package com.pricewise.feature.search.api.domain.model

data class Product(
    val id: String,
    val title: String,
    val price: Long,
    val merchant: Merchant,
    val source: String,
    val productUrl: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
)
