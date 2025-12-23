package com.example.pricewise.feature.favorites.domain.repository

import com.example.pricewise.feature.favorites.domain.model.FavoriteCreate
import com.example.pricewise.feature.favorites.domain.model.FavoriteItem

interface FavoritesRepository {
    suspend fun list(token: String): List<FavoriteItem>
    suspend fun add(token: String, favorite: FavoriteCreate): FavoriteItem
    suspend fun remove(token: String, externalId: String, source: String): Boolean
    suspend fun favoriteRecommendation(token: String, recommendationId: Long): FavoriteItem
    suspend fun unfavoriteRecommendation(token: String, recommendationId: Long): Boolean
}
