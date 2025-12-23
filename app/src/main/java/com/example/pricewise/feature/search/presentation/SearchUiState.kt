package com.example.pricewise.feature.search.presentation

import com.example.pricewise.feature.main.domain.model.ProductRecommendation

data class SearchUiState(
    val query: String = "",
    val submittedQuery: String = "",
    val isLoading: Boolean = false,
    val items: List<ProductRecommendation> = emptyList(),
    val hasMore: Boolean = false,
    val checkedSources: Int = 0,
    val totalSources: Int = DEFAULT_TOTAL_SOURCES,
    val pendingSources: List<String> = emptyList(),
)

internal const val DEFAULT_TOTAL_SOURCES = 8
