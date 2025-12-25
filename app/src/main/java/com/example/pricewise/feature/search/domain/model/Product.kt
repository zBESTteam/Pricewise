package com.example.pricewise.feature.search.domain.model

import com.example.pricewise.feature.main.domain.model.Merchant

data class Product(
    val id: String,
    val title: String,
    val price: Long,
    val merchant: Merchant,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
)