package com.example.pricewise.feature.main.domain.model

data class MainScreenData(
    val banners: List<PromoBanner>,
    val popularQueries: List<PopularQuery>,
    val recommendations: List<ProductRecommendation>,
)
