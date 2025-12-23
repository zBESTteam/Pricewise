package com.example.pricewise.feature.main.presentation

import com.example.pricewise.feature.main.domain.model.PopularQuery
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.main.domain.model.PromoBanner

data class MainUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val banners: List<PromoBanner> = emptyList(),
    val popularQueries: List<PopularQuery> = emptyList(),
    val recommendations: List<ProductRecommendation> = emptyList(),
)
