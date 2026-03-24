package com.pricewise.feature.home.impl.domain.repository

import com.pricewise.feature.home.impl.domain.model.HomeFeed

interface HomeRepository {
    suspend fun getHomeFeed(): HomeFeed
}
