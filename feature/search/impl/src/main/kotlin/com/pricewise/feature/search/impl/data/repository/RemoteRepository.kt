package com.pricewise.feature.search.impl.data.repository

import com.pricewise.core.network.PriceWiseApi
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.SearchResult
import com.pricewise.feature.search.impl.data.mapper.ProductMapper
import jakarta.inject.Inject

class RemoteRepository @Inject constructor(
    private val api: PriceWiseApi,
    private val productMapper: ProductMapper
) : SearchFeatureApi {

    override suspend fun search(
        query: String,
        limit: Int,
        offset: Int,
        perSource: Boolean,
        partial: Boolean,
        sources: List<String>,
        sort: String,
        priceMin: Long,
        priceMax: Long,
        delivery: String,
        onlyOriginal: Boolean,
        onlyNew: Boolean,
        onlyUsed: Boolean,
        marketplaceOnly: Boolean,
        offlineOnly: Boolean,
        playLaterOnly: Boolean
    ): SearchResult {
        val response = api.search(
            query = query,
            limit = limit,
            offset = offset,
            perSource = if (perSource) true else null,
            partial = if (partial) true else null,
            sources = sources.takeIf { it.isNotEmpty() }?.joinToString(","),
            sort = sort,
            priceMin = priceMin,
            priceMax = priceMax,
            delivery = delivery,
            onlyOriginal = onlyOriginal,
            onlyNew = onlyNew,
            onlyUsed = onlyUsed,
            marketplaceOnly = marketplaceOnly,
            offlineOnly = offlineOnly,
            payLaterOnly = playLaterOnly
        )
        val items = productMapper.parseItems(response.items.orEmpty())
        return SearchResult(
            items = items,
            hasMore = response.hasMore ?: false,
            checkedSources = response.checkedSources,
            totalSources = response.totalSources,
            pendingSources = response.pendingSources.orEmpty(),
        )
    }
}