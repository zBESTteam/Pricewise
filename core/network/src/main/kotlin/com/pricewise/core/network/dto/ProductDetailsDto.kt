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
    @field:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @field:Json(name = "product_url")
    val productUrl: String?,
    @field:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
    @field:Json(name = "is_favorite")
    val isFavorite: Boolean?,
    val specs: List<ProductSpecDto>?,
    val description: String?,
)
