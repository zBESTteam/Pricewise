package com.pricewise.feature.search.impl.presentation.ui

import com.pricewise.feature.search.api.domain.model.Product

data class SearchUiState(
    val query: String = "",
    val submittedQuery: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val items: List<Product> = emptyList(),
    val hasMore: Boolean = false,
    val checkedSources: Int = 0,
    val totalSources: Int = DEFAULT_TOTAL_SOURCES,
    val pendingSources: List<String> = emptyList(),
    val minPrice: Long = 0,
    val maxPrice: Long = 0
)

internal const val DEFAULT_TOTAL_SOURCES = 8
