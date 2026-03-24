package com.pricewise.feature.home.impl.data.source.remote

import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.dto.MainResponseDto
import com.pricewise.core.network.dto.TrendingResponseDto
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: PriceWiseApi,
) {

    suspend fun loadMain(): MainResponseDto {
        return api.getMain(
            limit = DEFAULT_LIMIT,
            offset = DEFAULT_OFFSET,
        )
    }

    suspend fun loadTrending(): TrendingResponseDto {
        return api.getTrending(
            limit = TRENDING_LIMIT,
            days = TRENDING_DAYS,
        )
    }

    private companion object {
        const val DEFAULT_LIMIT = 20
        const val DEFAULT_OFFSET = 0
        const val TRENDING_LIMIT = 10
        const val TRENDING_DAYS = 7
    }
}
