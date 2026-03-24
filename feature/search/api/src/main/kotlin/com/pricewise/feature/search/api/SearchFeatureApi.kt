package com.pricewise.feature.search.api

import com.pricewise.feature.search.api.domain.model.SearchResult

interface SearchFeatureApi {
    suspend fun search(
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
    ): SearchResult
}
