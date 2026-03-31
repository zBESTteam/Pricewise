package com.pricewise.feature.favorites.impl.domain.repository

import com.pricewise.feature.favorites.impl.domain.model.FavoriteCreate
import com.pricewise.feature.favorites.impl.domain.model.FavoriteItem

interface FavoritesRepository {
    suspend fun list(token: String): List<FavoriteItem>
    suspend fun add(token: String, favorite: FavoriteCreate): FavoriteItem
    suspend fun remove(token: String, externalId: String, source: String): Boolean
    suspend fun favoriteRecommendation(token: String, recommendationId: Long): FavoriteItem
    suspend fun unfavoriteRecommendation(token: String, recommendationId: Long): Boolean
}
