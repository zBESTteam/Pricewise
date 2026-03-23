package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class MainResponseDto(
    val banners: List<BannerDto>?,
    val recommendations: List<ProductDto>?,
)

data class BannersResponseDto(
    val items: List<BannerDto>?,
)

data class TrendingResponseDto(
    val items: List<TrendingItemDto>?,
)

data class SearchResponseDto(
    val items: List<ProductDto>?,
    @param:Json(name = "has_more")
    val hasMore: Boolean?,
    @param:Json(name = "checked_sources")
    val checkedSources: Int?,
    @param:Json(name = "total_sources")
    val totalSources: Int?,
    @param:Json(name = "pending_sources")
    val pendingSources: List<String>?,
)

data class BannerDto(
    val id: Any?,
    val title: String?,
    @param:Json(name = "image_url")
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
    @param:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @param:Json(name = "image_url")
    val imageUrl: String?,
    @param:Json(name = "image")
    val image: String?,
    @param:Json(name = "product_url")
    val productUrl: String?,
    val source: String?,
    val merchant: MerchantDto?,
    @param:Json(name = "merchant_name")
    val merchantName: String?,
    @param:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
    @param:Json(name = "logo_url")
    val logoUrl: String?,
    @param:Json(name = "merchant_id")
    val merchantId: String?,
    @param:Json(name = "is_favorite")
    val isFavorite: Boolean?,
)

data class MerchantDto(
    val id: Any?,
    val name: String?,
    @param:Json(name = "logo_url")
    val logoUrl: String?,
)

data class AuthResponseDto(
    @param:Json(name = "access_token")
    val accessToken: String?,
    @param:Json(name = "token_type")
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
    @param:Json(name = "password_confirm")
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
    @param:Json(name = "external_id")
    val externalId: String?,
    val source: String?,
    val title: String?,
    val price: Long?,
    @param:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @param:Json(name = "product_url")
    val productUrl: String?,
    @param:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
)

data class FavoriteCreateRequestDto(
    @param:Json(name = "external_id")
    val externalId: String,
    val source: String,
    val title: String,
    val price: Long,
    @param:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @param:Json(name = "product_url")
    val productUrl: String?,
    @param:Json(name = "merchant_logo_url")
    val merchantLogoUrl: String?,
)

data class ApiStatusDto(
    val status: String?,
)
