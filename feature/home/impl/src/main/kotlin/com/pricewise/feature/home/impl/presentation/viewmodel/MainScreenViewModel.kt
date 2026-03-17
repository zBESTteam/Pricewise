package com.pricewise.feature.home.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import com.pricewise.feature.home.impl.presentation.mapper.HomeScreenStateMapper
import com.pricewise.feature.home.impl.presentation.ui.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val homeScreenStateMapper: HomeScreenStateMapper,
) : ViewModel() {

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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = EMPTY_STATE,
    )

    init {
        refresh()
        observeSearchQueries()
    }

    fun onSearchQueryChange(query: String) {
        userInputState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    fun onPhotoSearchClick() = Unit

    fun onQuickActionClick(actionId: String) {
        viewModelScope.launch {
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
        viewModelScope.launch {
            homeRepository.toggleFavorite(productId)
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                homeRepository.refreshHomeFeed()
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueries() {
        viewModelScope.launch {
            userInputState
                .map { userInput -> userInput.searchQuery }
                .drop(1)
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .collectLatest { query ->
                    homeRepository.search(query)
                }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val SEARCH_DEBOUNCE_MILLIS = 350L

        val EMPTY_STATE = MainScreenState(
            searchQuery = "",
            isLoading = true,
            quickActions = emptyList(),
            popularQueries = emptyList(),
            products = emptyList(),
        )
    }
}
