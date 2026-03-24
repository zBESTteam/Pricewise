package com.pricewise.feature.home.impl.domain.usecase

import com.pricewise.feature.home.impl.domain.model.HomeFeed

interface GetHomeFeedUseCase {
    suspend operator fun invoke(): HomeFeed
}
