package com.pricewise.feature.home.impl.data.repository

import com.pricewise.feature.home.impl.data.mapper.HomeFeedDomainMapper
import com.pricewise.feature.home.impl.data.model.dto.HomeFeedDto
import com.pricewise.feature.home.impl.data.source.remote.HomeRemoteDataSource
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val homeFeedDomainMapper: HomeFeedDomainMapper,
) : HomeRepository {

    private val homeFeedState = MutableStateFlow(createInitialFeed())
    private val loadingState = MutableStateFlow(false)

    override val homeFeed: Flow<HomeFeed> = homeFeedState
        .map { homeFeedDto ->
            homeFeedDomainMapper.map(homeFeedDto)
        }
        .flowOn(Dispatchers.Default)
    override val isLoading: Flow<Boolean> = loadingState.asStateFlow()

    override suspend fun refreshHomeFeed() {
        loadingState.update { true }
        try {
            val currentFeed = homeFeedState.value
            val remoteFeed = withContext(Dispatchers.IO) {
                remoteDataSource.loadHomeFeed()
            }
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
            val currentFeed = homeFeedState.value
            val searchedFeed = withContext(Dispatchers.IO) {
                remoteDataSource.search(
                    query = query,
                    currentFeed = currentFeed,
                )
            }
            homeFeedState.update {
                searchedFeed
            }
        } finally {
            loadingState.update { false }
        }
    }

    override suspend fun selectQuickAction(actionId: String) = Unit

    override suspend fun toggleFavorite(productId: String) {
        homeFeedState.update { currentFeed ->
            currentFeed.copy(
                products = currentFeed.products.map { product ->
                    if (product.id == productId) {
                        product.copy(isFavorite = !product.isFavorite)
                    } else {
                        product
                    }
                },
            )
        }
    }

    private fun createInitialFeed(): HomeFeedDto {
        return HomeFeedDto(
            searchQuery = "",
            quickActions = emptyList(),
            popularQueries = emptyList(),
            products = emptyList(),
        )
    }
}
