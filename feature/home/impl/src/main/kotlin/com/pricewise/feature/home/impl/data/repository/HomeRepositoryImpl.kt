package com.pricewise.feature.home.impl.data.repository

import com.pricewise.feature.home.impl.data.mapper.HomeDataToDomainMapper
import com.pricewise.feature.home.impl.data.source.remote.HomeRemoteDataSource
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val homeDataToDomainMapper: HomeDataToDomainMapper,
) : HomeRepository {

    override suspend fun getHomeFeed(): HomeFeed = coroutineScope {
        val mainDeferred = async(Dispatchers.IO) { remoteDataSource.loadMain() }
        val trendingDeferred = async(Dispatchers.IO) { remoteDataSource.loadTrending() }
        homeDataToDomainMapper.map(
            main = mainDeferred.await(),
            trending = trendingDeferred.await(),
        )
    }
}

