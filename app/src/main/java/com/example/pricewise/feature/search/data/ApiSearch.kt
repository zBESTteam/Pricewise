package com.example.pricewise.feature.search.data

import com.example.pricewise.feature.main.domain.model.Product

data class SearchResult(
    val items: List<Product>,
    val hasMore: Boolean,
    val checkedSources: Int? = null,
    val totalSources: Int? = null,
    val pendingSources: List<String> = emptyList(),
)

interface ApiSearch {
    suspend fun search(
        query: String,
        limit: Int,
        offset: Int,
        perSource: Boolean,
        partial: Boolean,
    ): SearchResult
}
