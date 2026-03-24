package com.pricewise.feature.home.impl.domain.usecase

import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository

class GetHomeFeedUseCaseImpl(
    private val repository: HomeRepository,
) : GetHomeFeedUseCase {
    override suspend fun invoke(): HomeFeed = repository.getHomeFeed()
}
