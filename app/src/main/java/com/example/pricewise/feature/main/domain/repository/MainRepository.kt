package com.example.pricewise.feature.main.domain.repository

import com.example.pricewise.feature.main.domain.model.PopularQuery
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.main.domain.model.PromoBanner

interface MainRepository {
    suspend fun loadBanners(): List<PromoBanner>
    suspend fun loadPopularQueries(): List<PopularQuery>
    suspend fun loadRecommendations(): List<ProductRecommendation>
}
