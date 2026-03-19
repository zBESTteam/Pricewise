package com.pricewise.feature.search.api.domain.repository

import com.pricewise.feature.search.api.domain.model.Product

data class SearchResult(
    val items: List<Product>,
    val hasMore: Boolean,
    val checkedSources: Int? = null,
    val totalSources: Int? = null,
    val pendingSources: List<String> = emptyList(),
)

interface SearchRepository {
    suspend fun search(
        query: String,
        limit: Int,
        offset: Int,
        perSource: Boolean,
        partial: Boolean,
    ): SearchResult
}
