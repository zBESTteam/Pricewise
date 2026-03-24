package com.pricewise.feature.home.impl.presentation.viewmodel

import com.pricewise.core.ui.viewmodel.BaseViewModel
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import com.pricewise.feature.home.impl.presentation.mapper.HomeScreenStateMapper
import com.pricewise.feature.home.impl.presentation.ui.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val homeScreenStateMapper: HomeScreenStateMapper,
) : BaseViewModel() {

    private val userInputState = MutableStateFlow(
        MainScreenUserInput(
            searchQuery = "",
        ),
    )

    val uiState: StateFlow<MainScreenState> = combine(
        homeRepository.homeFeed,
        userInputState,
        homeRepository.isLoading,
    ) { homeFeed: HomeFeed, userInput: MainScreenUserInput, isLoading: Boolean ->
        homeScreenStateMapper.getScreenState(
            homeFeed = homeFeed,
            userInput = userInput,
            isLoading = isLoading,
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = EMPTY_STATE,
    )

    init {
        refresh()
    }

    fun onSearchQueryChange(query: String) {
        userInputState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    fun onPhotoSearchClick() = Unit

    fun onQuickActionClick(actionId: String) {
        viewModelScopeSafe.launch {
            homeRepository.selectQuickAction(actionId)
        }
    }

    fun onPopularQueryClick(queryId: String) {
        val queryTitle = uiState.value.popularQueries.firstOrNull { query ->
            query.id == queryId
        }?.title.orEmpty()
        userInputState.update { currentState ->
            currentState.copy(searchQuery = queryTitle)
        }
    }

    fun onProductFavoriteClick(productId: String) {
        viewModelScopeSafe.launch {
            homeRepository.toggleFavorite(productId)
        }
    }

    private fun refresh() {
        viewModelScopeSafe.launch {
            homeRepository.refreshHomeFeed()
        }
    }
    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L

        val EMPTY_STATE = MainScreenState(
            searchQuery = "",
            isLoading = true,
            quickActions = emptyList(),
            popularQueries = emptyList(),
            products = emptyList(),
        )
    }
}
