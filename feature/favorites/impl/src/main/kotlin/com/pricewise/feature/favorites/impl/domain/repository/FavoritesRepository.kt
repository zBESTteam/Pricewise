package com.pricewise.feature.favorites.impl.domain.repository

import com.pricewise.feature.favorites.impl.domain.model.FavoriteCreate
import com.pricewise.feature.favorites.impl.domain.model.FavoriteItem

interface FavoritesRepository {
    suspend fun list(): List<FavoriteItem>
    suspend fun add(favorite: FavoriteCreate): FavoriteItem
    suspend fun remove(externalId: String, source: String): Boolean
    suspend fun favoriteRecommendation(recommendationId: Long): FavoriteItem
    suspend fun unfavoriteRecommendation(recommendationId: Long): Boolean
    suspend fun getAuthHeader(): String
}
