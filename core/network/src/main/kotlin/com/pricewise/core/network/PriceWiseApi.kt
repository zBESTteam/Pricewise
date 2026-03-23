package com.pricewise.core.network

import com.pricewise.core.network.dto.ApiStatusDto
import com.pricewise.core.network.dto.AuthResponseDto
import com.pricewise.core.network.dto.BannersResponseDto
import com.pricewise.core.network.dto.FavoriteCreateRequestDto
import com.pricewise.core.network.dto.FavoriteDto
import com.pricewise.core.network.dto.FavoritesResponseDto
import com.pricewise.core.network.dto.LoginRequestDto
import com.pricewise.core.network.dto.MainResponseDto
import com.pricewise.core.network.dto.PasswordChangeRequestDto
import com.pricewise.core.network.dto.ProductDetailsDto
import com.pricewise.core.network.dto.ProfileDto
import com.pricewise.core.network.dto.ProfileUpdateRequestDto
import com.pricewise.core.network.dto.RegisterRequestDto
import com.pricewise.core.network.dto.SearchResponseDto
import com.pricewise.core.network.dto.TrendingResponseDto
import com.pricewise.core.network.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PriceWiseApi {
    @GET("api/banners")
    suspend fun getBanners(): BannersResponseDto

    @GET("api/main")
    suspend fun getMain(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): MainResponseDto

    @GET("api/search/trending")
    suspend fun getTrending(
        @Query("limit") limit: Int,
        @Query("days") days: Int,
    ): TrendingResponseDto

    @GET("api/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("per_source") perSource: Boolean?,
        @Query("partial") partial: Boolean?,
        @Query("sources") sources: String?,
        @Query("sort") sort: String?,
        @Query("price_min") priceMin: Long?,
        @Query("price_max") priceMax: Long?,
        @Query("delivery") delivery: String?,
        @Query("only_original") onlyOriginal: Boolean?,
        @Query("only_new") onlyNew: Boolean?,
        @Query("only_used") onlyUsed: Boolean?,
        @Query("marketplace_only") marketplaceOnly: Boolean?,
        @Query("offline_only") offlineOnly: Boolean?,
        @Query("pay_later_only") payLaterOnly: Boolean?,
    ): SearchResponseDto

    @GET("api/product")
    suspend fun getProductDetails(
        @Query("source") source: String,
        @Query("external_id") externalId: String,
    ): ProductDetailsDto

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto,
    ): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): AuthResponseDto

    @GET("api/me")
    suspend fun getMe(
        @Header("Authorization") authorization: String,
    ): UserDto

    @GET("api/profile")
    suspend fun getProfile(
        @Header("Authorization") authorization: String,
    ): ProfileDto

    @PUT("api/profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body request: ProfileUpdateRequestDto,
    ): ProfileDto

    @PUT("api/profile/password")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body request: PasswordChangeRequestDto,
    ): ApiStatusDto

    @GET("api/favorites")
    suspend fun listFavorites(
        @Header("Authorization") authorization: String,
    ): FavoritesResponseDto

    @POST("api/favorites")
    suspend fun addFavorite(
        @Header("Authorization") authorization: String,
        @Body request: FavoriteCreateRequestDto,
    ): FavoriteDto

    @DELETE("api/favorites")
    suspend fun removeFavorite(
        @Header("Authorization") authorization: String,
        @Query("external_id") externalId: String,
        @Query("source") source: String,
    ): ApiStatusDto

    @POST("api/recommendations/{recommendation_id}/favorite")
    suspend fun favoriteRecommendation(
        @Header("Authorization") authorization: String,
        @Path("recommendation_id") recommendationId: Long,
    ): FavoriteDto

    @DELETE("api/recommendations/{recommendation_id}/favorite")
    suspend fun unfavoriteRecommendation(
        @Header("Authorization") authorization: String,
        @Path("recommendation_id") recommendationId: Long,
    ): ApiStatusDto
}
