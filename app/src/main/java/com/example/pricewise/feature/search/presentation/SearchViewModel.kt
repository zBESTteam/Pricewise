package com.example.pricewise.feature.search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.feature.search.data.ApiSearch
import com.example.pricewise.feature.search.data.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class SearchViewModel(
    private val repository: ApiSearch = RemoteRepository()
) : ViewModel() {

    private val defaultLimit: Int = 100
    private var searchJob: Job? = null
    private var searchAttempts = 0
    private val _uiState = MutableStateFlow(SearchUiState())
    private val _resolvedTotal = MutableStateFlow(0)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    val resolvedTotal: StateFlow<Int> = _resolvedTotal.asStateFlow()

    private val _isProductChosen = MutableStateFlow(true)
    private val _deliveryChosen = MutableStateFlow(0)
    private val _onlyOriginals = MutableStateFlow(false)
    private val _onlyNew = MutableStateFlow(false)
    private val _onlyBU = MutableStateFlow(false)
    private val _onlyMarketplaces = MutableStateFlow(false)
    private val _onlyOfflineShops = MutableStateFlow(false)

    val isProductChosen: StateFlow<Boolean> = _isProductChosen.asStateFlow()
    val deliveryChosen: StateFlow<Int> = _deliveryChosen.asStateFlow()
    val onlyOriginals: StateFlow<Boolean> = _onlyOriginals.asStateFlow()
    val onlyNew: StateFlow<Boolean> = _onlyNew.asStateFlow()
    val onlyBU: StateFlow<Boolean> = _onlyBU.asStateFlow()
    val onlyMarketplaces: StateFlow<Boolean> = _onlyMarketplaces.asStateFlow()
    val onlyOfflineShops: StateFlow<Boolean> = _onlyOfflineShops.asStateFlow()


    fun setIsProductChosen(value: Boolean) {
        _isProductChosen.value = value
    }

    fun setDeliveryChosen(value: Int) {
        _deliveryChosen.value = value
    }

    fun setOnlyOriginals(value: Boolean) {
        _onlyOriginals.value = value
    }

    fun setOnlyNew(value: Boolean) {
        _onlyNew.value = value
    }

    fun setOnlyBU(value: Boolean) {
        _onlyBU.value = value
    }

    fun setOnlyMarketplaces(value: Boolean) {
        _onlyMarketplaces.value = value
    }

    fun setOnlyOfflineShops(value: Boolean) {
        _onlyOfflineShops.value = value
    }

    fun resetAllFilters() {
        _isProductChosen.value = true
        _deliveryChosen.value = 0
        _onlyOriginals.value = false
        _onlyNew.value = false
        _onlyBU.value = false
        _onlyMarketplaces.value = false
        _onlyOfflineShops.value = false
    }

    fun onQueryChange(query: String) {
        val trimmedQuery = query.trim()
        _uiState.update { state ->
            when {
                trimmedQuery.isEmpty() -> {
                    searchJob?.cancel()
                    state.copy(
                        query = query,
                        submittedQuery = "",
                        isLoading = false,
                        items = emptyList(),
                        hasMore = false,
                        checkedSources = 0,
                        totalSources = DEFAULT_TOTAL_SOURCES,
                        pendingSources = emptyList(),
                    )
                }

                else -> state.copy(query = query)
            }
        }
    }

    fun submitSearch() {
        searchJob?.cancel()
        searchAttempts = 0
        viewModelScope.launch(Dispatchers.IO) {
            searchJob = viewModelScope.launch {
                resetAllFilters()
                performSearch(_uiState.value.query.trim())
            }
        }
    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, submittedQuery = query) }
                val result = withContext(Dispatchers.IO) {
                    repository.search(
                        query = query,
                        limit = defaultLimit,
                        offset = 0,
                        perSource = true,
                        partial = true
                    )
                }
                val shouldSearchAgain =
                    result.pendingSources.isNotEmpty() && searchAttempts < MAX_SEARCH_ATTEMPTS
                _uiState.update { state ->
                    val checked = result.checkedSources ?: state.checkedSources
                    _resolvedTotal.value = when {
                        result.totalSources != null && result.totalSources > 0 -> {
                            maxOf(result.totalSources, checked)
                        }

                        checked > state.totalSources -> checked
                        else -> state.totalSources
                    }
                    state.copy(
                        isLoading = shouldSearchAgain,
                        items = result.items,
                        hasMore = result.hasMore,
                        checkedSources = checked,
                        totalSources = resolvedTotal.value,
                        pendingSources = result.pendingSources,
                    )
                }
                if (shouldSearchAgain) {
                    repeatSearch(query = query)
                }
            } catch (exception: Exception) {
                _uiState.update { state -> state.copy(isError = true) }
            }
        }
    }

    fun fixError() {
        _uiState.update { state -> state.copy(isError = false) }
    }

    fun repeatSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchAttempts += 1
            delay(SEARCH_INTERVAL_MS)
            performSearch(query)
        }
    }
}

private const val MAX_SEARCH_ATTEMPTS = 8
private const val SEARCH_INTERVAL_MS: Long = 1000