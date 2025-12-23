package com.example.pricewise.core.network.dto

import com.squareup.moshi.Json

data class MainResponseDto(
    val banners: List<BannerDto>? = null,
    val recommendations: List<ProductDto>? = null,
)

data class TrendingResponseDto(
    val items: List<TrendingItemDto>? = null,
)

data class SearchResponseDto(
    val items: List<ProductDto>? = null,
    @Json(name = "has_more")
    val hasMore: Boolean? = null,
    @Json(name = "checked_sources")
    val checkedSources: Int? = null,
    @Json(name = "total_sources")
    val totalSources: Int? = null,
    @Json(name = "pending_sources")
    val pendingSources: List<String>? = null,
)

data class BannerDto(
    val id: Any? = null,
    val title: String? = null,
    @Json(name = "image_url")
    val imageUrl: String? = null,
)

data class TrendingItemDto(
    val query: String? = null,
    val count: Int? = null,
)

data class ProductDto(
    val id: Any? = null,
    val title: String? = null,
    val price: Long? = null,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String? = null,
    @Json(name = "image_url")
    val imageUrl: String? = null,
    @Json(name = "image")
    val image: String? = null,
    val source: String? = null,
    @Json(name = "merchant")
    val merchant: MerchantDto? = null,
    @Json(name = "merchant_name")
    val merchantName: String? = null,
    @Json(name = "merchant_logo_url")
    val merchantLogoUrl: String? = null,
    @Json(name = "logo_url")
    val logoUrl: String? = null,
    @Json(name = "merchant_id")
    val merchantId: String? = null,
    @Json(name = "is_favorite")
    val isFavorite: Boolean? = null,
)

data class MerchantDto(
    val id: Any? = null,
    val name: String? = null,
    @Json(name = "logo_url")
    val logoUrl: String? = null,
)

data class AuthResponseDto(
    @Json(name = "access_token")
    val accessToken: String? = null,
    @Json(name = "token_type")
    val tokenType: String? = null,
    val user: UserDto? = null,
)

data class UserDto(
    val id: Long? = null,
    val email: String? = null,
)

data class RegisterRequestDto(
    val email: String,
    val password: String,
    @Json(name = "password_confirm")
    val passwordConfirm: String,
)

data class LoginRequestDto(
    val email: String,
    val password: String,
)

data class FavoritesResponseDto(
    val items: List<FavoriteDto>? = null,
)

data class FavoriteDto(
    val id: Long? = null,
    @Json(name = "external_id")
    val externalId: String? = null,
    val source: String? = null,
    val title: String? = null,
    val price: Long? = null,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String? = null,
    @Json(name = "product_url")
    val productUrl: String? = null,
    @Json(name = "merchant_logo_url")
    val merchantLogoUrl: String? = null,
)

data class FavoriteCreateRequestDto(
    @Json(name = "external_id")
    val externalId: String,
    val source: String,
    val title: String,
    val price: Long,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String? = null,
    @Json(name = "product_url")
    val productUrl: String? = null,
    @Json(name = "merchant_logo_url")
    val merchantLogoUrl: String? = null,
)

data class ApiStatusDto(
    val status: String? = null,
)
