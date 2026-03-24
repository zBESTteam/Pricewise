package com.pricewise.feature.home.impl.data.repository

import com.pricewise.feature.home.impl.data.mapper.HomeDataToDomainMapper
import com.pricewise.feature.home.impl.data.source.remote.HomeRemoteDataSource
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val homeDataToDomainMapper: HomeDataToDomainMapper,
) : HomeRepository {

    override suspend fun getHomeFeed(): HomeFeed {
        val mainResponse = withContext(kotlinx.coroutines.Dispatchers.IO) {
            remoteDataSource.loadMain()
        }
        val trendingResponse = withContext(kotlinx.coroutines.Dispatchers.IO) {
            remoteDataSource.loadTrending()
        }
        return homeDataToDomainMapper.map(
            main = mainResponse,
            trending = trendingResponse,
        )
    }
}
