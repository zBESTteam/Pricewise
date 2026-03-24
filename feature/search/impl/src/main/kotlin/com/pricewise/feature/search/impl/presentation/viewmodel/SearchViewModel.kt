package com.pricewise.feature.search.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchFeatureApi,
) : ViewModel() {

    private val defaultLimit: Int = 20
    private var searchJob: Job? = null
    private var searchAttempts = 0
    private val _resolvedTotal = MutableStateFlow(0)
    val resolvedTotal: StateFlow<Int> = _resolvedTotal.asStateFlow()
    private val _allItems = MutableStateFlow(listOf<Product>())
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _isProductChosen = MutableStateFlow(true)
    private val _deliveryChosen = MutableStateFlow(0)
    private val _onlyOriginals = MutableStateFlow(false)
    private val _onlyNew = MutableStateFlow(false)
    private val _onlyUsed = MutableStateFlow(false)
    private val _onlyMarketplaces = MutableStateFlow(false)
    private val _onlyOfflineShops = MutableStateFlow(false)
    private val _priceFrom = MutableStateFlow(0L)
    private val _priceTo = MutableStateFlow(0L)
    private val _popularDiapasonChosen = MutableStateFlow(0)
    private val _canPayLater = MutableStateFlow(false)
    val isProductChosen: StateFlow<Boolean> = _isProductChosen.asStateFlow()
    val deliveryChosen: StateFlow<Int> = _deliveryChosen.asStateFlow()
    val onlyOriginals: StateFlow<Boolean> = _onlyOriginals.asStateFlow()
    val onlyNew: StateFlow<Boolean> = _onlyNew.asStateFlow()
    val onlyUsed: StateFlow<Boolean> = _onlyUsed.asStateFlow()
    val onlyMarketplaces: StateFlow<Boolean> = _onlyMarketplaces.asStateFlow()
    val onlyOfflineShops: StateFlow<Boolean> = _onlyOfflineShops.asStateFlow()
    val priceFrom: StateFlow<Long> = _priceFrom.asStateFlow()
    val priceTo: StateFlow<Long> = _priceTo.asStateFlow()
    val popularDiapasonChosen: StateFlow<Int> = _popularDiapasonChosen.asStateFlow()
    val canPayLater: StateFlow<Boolean> = _canPayLater.asStateFlow()
    val sources = listOf(
        "market.yandex.ru",
        "mvideo.ru",
        "citilink.ru",
        "eldorado.ru",
        "avito.ru",
        "cdek.shopping",
        "aliexpress.ru",
        "xcom-shop.ru",
    )

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

    fun setOnlyUsed(value: Boolean) {
        _onlyUsed.value = value
    }

    fun setOnlyMarketplaces(value: Boolean) {
        _onlyMarketplaces.value = value
    }

    fun setOnlyOfflineShops(value: Boolean) {
        _onlyOfflineShops.value = value
    }

    fun setPriceFrom(value: Long) {
        _priceFrom.value = value
    }

    fun setPriceTo(value: Long) {
        _priceTo.value = value
    }

    fun setPopularDiapasonChosen(value: Int) {
        _popularDiapasonChosen.value = value
    }

    fun setCanPayLater(value: Boolean) {
        _canPayLater.value = value
    }

    fun resetAllFilters() {
        _isProductChosen.value = true
        _deliveryChosen.value = 0
        _onlyOriginals.value = false
        _onlyNew.value = false
        _onlyUsed.value = false
        _onlyMarketplaces.value = false
        _onlyOfflineShops.value = false
    }

    fun onProductFavoriteClick(productId: String) {
        _allItems.update { items ->
            items.map { product ->
                if (product.id == productId) {
                    product.copy(isFavorite = !product.isFavorite)
                } else {
                    product
                }
            }
        }
        _uiState.update { state ->
            state.copy(
                items = state.items.map { product ->
                    if (product.id == productId) {
                        product.copy(isFavorite = !product.isFavorite)
                    } else {
                        product
                    }
                },
            )
        }
    }

    fun onQueryChange(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) clearQuery()
        else {
            _uiState.update { state ->
                state.copy(query = query)
            }
        }
    }

    fun clearQuery() {
        _uiState.update { state -> state.copy(query = "") }
    }

    fun initializeSearch(searchQuery: String) {
        val trimmedSearchQuery = searchQuery.trim()

        if (trimmedSearchQuery.isEmpty()) {
            return
        }

        if (_uiState.value.submittedQuery == trimmedSearchQuery && _uiState.value.query == trimmedSearchQuery) {
            return
        }

        _uiState.update { state ->
            state.copy(query = trimmedSearchQuery)
        }
        submitSearch()
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
                        partial = true,
                        sources = sources,
                        sort = "",
                        priceMin = priceFrom.value,
                        priceMax = priceTo.value,
                        delivery = deliveryChosen.value.toString(),
                        onlyOriginal = onlyOriginals.value,
                        onlyNew = onlyNew.value,
                        onlyUsed = onlyUsed.value,
                        marketplaceOnly = onlyMarketplaces.value,
                        offlineOnly = onlyOfflineShops.value,
                        playLaterOnly = canPayLater.value,
                    )
                }
                _allItems.value = result.items
                val shouldSearchAgain =
                    result.pendingSources.isNotEmpty() && searchAttempts < MAX_SEARCH_ATTEMPTS
                _uiState.update { state ->
                    val checked = result.checkedSources ?: state.checkedSources
                    val totalSources = result.totalSources
                    _resolvedTotal.value = when {
                        totalSources != null && totalSources > 0 -> {
                            maxOf(totalSources, checked)
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
            } catch (_: Exception) {
                _uiState.update { state -> state.copy(isError = true) }
            }
        }
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
