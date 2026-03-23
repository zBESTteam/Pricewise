package com.pricewise.feature.home.impl.data.source.remote

import com.pricewise.core.network.PriceWiseApi
import com.pricewise.feature.home.impl.data.mapper.HomeRemoteFeedMapper
import com.pricewise.feature.home.impl.data.model.dto.HomeFeedDto
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: PriceWiseApi,
    private val homeRemoteFeedMapper: HomeRemoteFeedMapper,
) {

    suspend fun loadHomeFeed(): HomeFeedDto {
        val mainResponse = api.getMain(
            limit = DEFAULT_LIMIT,
            offset = DEFAULT_OFFSET,
        )
        val trendingResponse = api.getTrending(
            limit = TRENDING_LIMIT,
            days = TRENDING_DAYS,
        )
        return homeRemoteFeedMapper.map(
            main = mainResponse,
            trending = trendingResponse,
        )
    }

    suspend fun search(
        query: String,
        currentFeed: HomeFeedDto,
    ): HomeFeedDto {
        val response = api.search(
            query = query,
            limit = DEFAULT_LIMIT,
            offset = DEFAULT_OFFSET,
            perSource = true,
            partial = true,
            sources = null,
            sort = null,
            priceMin = null,
            priceMax = null,
            delivery = null,
            onlyOriginal = null,
            onlyNew = null,
            onlyUsed = null,
            marketplaceOnly = null,
            offlineOnly = null,
            payLaterOnly = null,
        )
        return homeRemoteFeedMapper.mapSearch(
            response = response,
            currentFeed = currentFeed,
            query = query,
        )
    }

    private companion object {
        const val DEFAULT_LIMIT = 20
        const val DEFAULT_OFFSET = 0
        const val TRENDING_LIMIT = 10
        const val TRENDING_DAYS = 7
    }
}
