package com.pricewise.feature.home.impl.data.repository

import com.pricewise.feature.home.impl.data.mapper.HomeFeedDomainMapper
import com.pricewise.feature.home.impl.data.model.dto.HomeFeedDto
import com.pricewise.feature.home.impl.data.source.remote.HomeRemoteDataSource
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val homeFeedDomainMapper: HomeFeedDomainMapper,
) : HomeRepository {

    private val homeFeedState = MutableStateFlow(createInitialFeed())
    private val loadingState = MutableStateFlow(false)

    override val homeFeed: Flow<HomeFeed> = homeFeedState.map(homeFeedDomainMapper::map)
    override val isLoading: Flow<Boolean> = loadingState.asStateFlow()

    override suspend fun refreshHomeFeed() {
        loadingState.update { true }
        try {
            val currentFeed = homeFeedState.value
            val remoteFeed = remoteDataSource.loadHomeFeed()
            homeFeedState.update {
                remoteFeed.copy(searchQuery = currentFeed.searchQuery)
            }
        } finally {
            loadingState.update { false }
        }
    }

    override suspend fun search(query: String) {
        if (query.isBlank()) {
            refreshHomeFeed()
            return
        }

        loadingState.update { true }
        try {
            homeFeedState.update { currentFeed ->
                remoteDataSource.search(
                    query = query,
                    currentFeed = currentFeed,
                )
            }
        } finally {
            loadingState.update { false }
        }
    }

    override suspend fun selectQuickAction(actionId: String) = Unit

    override suspend fun toggleFavorite(productId: String) = Unit

    private fun createInitialFeed(): HomeFeedDto {
        return HomeFeedDto(
            searchQuery = "",
            quickActions = emptyList(),
            popularQueries = emptyList(),
            products = emptyList(),
        )
    }
}
