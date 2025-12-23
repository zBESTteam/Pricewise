package com.example.pricewise.core.network

import com.example.pricewise.core.network.dto.ApiStatusDto
import com.example.pricewise.core.network.dto.AuthResponseDto
import com.example.pricewise.core.network.dto.FavoriteCreateRequestDto
import com.example.pricewise.core.network.dto.FavoriteDto
import com.example.pricewise.core.network.dto.FavoritesResponseDto
import com.example.pricewise.core.network.dto.LoginRequestDto
import com.example.pricewise.core.network.dto.MainResponseDto
import com.example.pricewise.core.network.dto.RegisterRequestDto
import com.example.pricewise.core.network.dto.SearchResponseDto
import com.example.pricewise.core.network.dto.TrendingResponseDto
import com.example.pricewise.core.network.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PricewiseApi {
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
        @Query("per_source") perSource: Int? = null,
        @Query("partial") partial: Int? = null,
        @Query("sources") sources: String? = null,
    ): SearchResponseDto

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
