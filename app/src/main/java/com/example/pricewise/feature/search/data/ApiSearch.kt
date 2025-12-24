package com.example.pricewise.feature.search.data

import com.example.pricewise.feature.main.domain.model.ProductRecommendation

data class SearchResult(
    val items: List<ProductRecommendation>,
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
