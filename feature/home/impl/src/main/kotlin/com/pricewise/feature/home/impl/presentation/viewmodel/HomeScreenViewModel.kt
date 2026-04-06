package com.pricewise.feature.home.impl.presentation.viewmodel

import com.pricewise.core.ui.viewmodel.BaseViewModel
import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.usecase.GetHomeFeedUseCase
import com.pricewise.feature.home.impl.presentation.mapper.HomeScreenMapper
import com.pricewise.feature.home.impl.presentation.ui.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getHomeFeedUseCase: GetHomeFeedUseCase,
    private val homeScreenMapper: HomeScreenMapper,
    private val favoritesFeatureApi: FavoritesFeatureApi,
) : BaseViewModel() {

    private val userInputState = MutableStateFlow(
        HomeScreenUserInput(
            searchQuery = "",
            favoriteProductIds = emptyMap(),
        ),
    )

    private val homeFeedAsync = viewModelScopeSafe.async(Dispatchers.IO) {
        getHomeFeedUseCase()
    }

    init {
        viewModelScopeSafe.launch {
            favoritesFeatureApi.favoriteMutations.collect { event ->
                val favoriteKey = makeFavoriteKey(
                    productId = event.externalId,
                    source = normalizeSource(event.source),
                )
                userInputState.update { currentState ->
                    currentState.copy(
                        favoriteProductIds = currentState.favoriteProductIds + (favoriteKey to event.isFavorite),
                    )
                }
            }
        }
    }

    val screenState: StateFlow<HomeScreenState> = userInputState.map { userInput: HomeScreenUserInput ->
        val homeFeed: HomeFeed = homeFeedAsync.await()
        homeScreenMapper.getScreenState(homeFeed = homeFeed, userInput = userInput)
    }
        .catch { throwable ->
            emit(homeScreenMapper.getScreenState(throwable = throwable))
        }
        .stateIn(
            scope = viewModelScopeSafe,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = HomeScreenState.Loading,
        )

    fun onSearchQueryChange(query: String) {
        userInputState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    fun onPhotoSearchClick() = Unit

    fun onQuickActionClick(actionId: String) = Unit

    fun onPopularQueryClick(queryId: String) {
        val loadedState = screenState.value as? HomeScreenState.Loaded ?: return
        val queryTitle = loadedState.popularQueries.firstOrNull { query -> query.id == queryId }
            ?.title
            .orEmpty()
        userInputState.update { currentState ->
            currentState.copy(searchQuery = queryTitle)
        }
    }

    fun onProductFavoriteClick(productId: String, source: String) {
        if (productId.isBlank()) return
        val loadedState = screenState.value as? HomeScreenState.Loaded ?: return
        val normalizedSource = normalizeSource(source)
        val productKey = makeFavoriteKey(productId = productId, source = normalizedSource)
        val uiProduct = loadedState.products.firstOrNull { product ->
            makeFavoriteKey(productId = product.id, source = product.source) == productKey
        } ?: return
        val updatedFavoriteState = !uiProduct.isFavorite

        userInputState.update { currentState ->
            currentState.copy(
                favoriteProductIds = currentState.favoriteProductIds + (productKey to updatedFavoriteState),
            )
        }

        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching {
                if (updatedFavoriteState) {
                    favoritesFeatureApi.addToFavorites(
                        request = FavoriteMutationRequest(
                            externalId = uiProduct.id,
                            source = normalizedSource,
                            title = uiProduct.title,
                            price = uiProduct.price.toPriceLong(),
                            thumbnailUrl = uiProduct.thumbnailUrl,
                            productUrl = uiProduct.productUrl,
                            merchantLogoUrl = uiProduct.marketplace.logoUrl,
                        ),
                    )
                } else {
                    favoritesFeatureApi.removeFromFavorites(
                        externalId = uiProduct.id,
                        source = normalizedSource,
                    )
                }
            }.onFailure {
                userInputState.update { currentState ->
                    currentState.copy(
                        favoriteProductIds = currentState.favoriteProductIds + (productKey to uiProduct.isFavorite),
                    )
                }
            }
        }
    }

    private fun String.toPriceLong(): Long {
        return this.filter { symbol -> symbol.isDigit() }.toLongOrNull() ?: 0L
    }

    private fun makeFavoriteKey(productId: String, source: String): String {
        return "$productId|$source"
    }

    private fun normalizeSource(source: String): String {
        return source.trim().ifBlank { "unknown" }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
