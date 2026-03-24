package com.pricewise.feature.home.impl.presentation.viewmodel

import com.pricewise.core.ui.viewmodel.BaseViewModel
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getHomeFeedUseCase: GetHomeFeedUseCase,
    private val homeScreenMapper: HomeScreenMapper,
) : BaseViewModel() {

    private val userInputState = MutableStateFlow(
        HomeScreenUserInput(
            searchQuery = "",
            favoriteProductIds = emptySet(),
        ),
    )

    private val homeFeedAsync = viewModelScopeSafe.async(Dispatchers.IO) {
        getHomeFeedUseCase()
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

    fun onProductFavoriteClick(productId: String) {
        userInputState.update { currentState ->
            val isFavorite = currentState.favoriteProductIds.contains(productId)
            val updatedFavoriteIds = if (isFavorite) {
                currentState.favoriteProductIds - productId
            } else {
                currentState.favoriteProductIds + productId
            }
            currentState.copy(favoriteProductIds = updatedFavoriteIds)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
