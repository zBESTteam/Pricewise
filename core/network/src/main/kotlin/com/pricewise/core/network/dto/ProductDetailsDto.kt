package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class ProductSpecDto(
    val label: String?,
    val value: String?,
)

data class ProductDetailsDto(
    val id: String?,
    val source: String?,
    val title: String?,
    val price: Long?,
    @param:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @param:Json(name = "product_url")
    val productUrl: String?,
    @param:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
    @param:Json(name = "is_favorite")
    val isFavorite: Boolean?,
    val specs: List<ProductSpecDto>?,
    val description: String?,
)
