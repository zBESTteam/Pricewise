package com.pricewise.feature.home.impl.domain.repository

import com.pricewise.feature.home.impl.domain.model.HomeFeed
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val homeFeed: Flow<HomeFeed>
    val isLoading: Flow<Boolean>

    suspend fun refreshHomeFeed()

    suspend fun search(query: String)

    suspend fun selectQuickAction(actionId: String)

    suspend fun toggleFavorite(productId: String)
}
