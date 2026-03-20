package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class MainResponseDto(
    val banners: List<BannerDto>?,
    val recommendations: List<ProductDto>?,
)

data class TrendingResponseDto(
    val items: List<TrendingItemDto>?,
)

data class SearchResponseDto(
    val items: List<ProductDto>?,
    @field:Json(name = "has_more")
    val hasMore: Boolean?,
    @field:Json(name = "checked_sources")
    val checkedSources: Int?,
    @field:Json(name = "total_sources")
    val totalSources: Int?,
    @field:Json(name = "pending_sources")
    val pendingSources: List<String>?,
)

data class BannerDto(
    val id: Any?,
    val title: String?,
    @field:Json(name = "image_url")
    val imageUrl: String?,
)

data class TrendingItemDto(
    val query: String?,
    val count: Int?,
)

data class ProductDto(
    val id: Any?,
    val title: String?,
    val price: Long?,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "product_url")
    val productUrl: String?,
    val source: String?,
    val merchant: MerchantDto?,
    @Json(name = "merchant_name")
    val merchantName: String?,
    @Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
    @Json(name = "logo_url")
    val logoUrl: String?,
    @Json(name = "merchant_id")
    val merchantId: String?,
    @Json(name = "is_favorite")
    val isFavorite: Boolean?,
)

data class MerchantDto(
    val id: Any?,
    val name: String?,
    @field:Json(name = "logo_url")
    val logoUrl: String?,
)

data class AuthResponseDto(
    @field:Json(name = "access_token")
    val accessToken: String?,
    @field:Json(name = "token_type")
    val tokenType: String?,
    val user: UserDto?,
)

data class UserDto(
    val id: Long?,
    val email: String?,
)

data class RegisterRequestDto(
    val email: String,
    val password: String,
    @field:Json(name = "password_confirm")
    val passwordConfirm: String,
)

data class LoginRequestDto(
    val email: String,
    val password: String,
)

data class FavoritesResponseDto(
    val items: List<FavoriteDto>?,
)

data class FavoriteDto(
    val id: Long?,
    @field:Json(name = "external_id")
    val externalId: String?,
    val source: String?,
    val title: String?,
    val price: Long?,
    @field:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @field:Json(name = "product_url")
    val productUrl: String?,
    @field:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
)

data class FavoriteCreateRequestDto(
    @field:Json(name = "external_id")
    val externalId: String,
    val source: String,
    val title: String,
    val price: Long,
    @field:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @field:Json(name = "product_url")
    val productUrl: String?,
    @field:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
)

data class ApiStatusDto(
    val status: String?,
)
