package com.example.pricewise.feature.main.domain.usecase

import com.example.pricewise.feature.main.domain.model.MainScreenData
import com.example.pricewise.feature.main.domain.repository.MainRepository

class GetMainScreenDataUseCase(
    private val repository: MainRepository,
) {
    suspend operator fun invoke(): MainScreenData {
        return MainScreenData(
            banners = repository.loadBanners(),
            popularQueries = repository.loadPopularQueries(),
            recommendations = repository.loadRecommendations()
        )
    }
}
