package com.pricewise.feature.search.api.domain.model

data class SearchResult(
    val items: List<Product>,
    val hasMore: Boolean,
    val checkedSources: Int? = null,
    val totalSources: Int? = null,
    val pendingSources: List<String> = emptyList(),
)