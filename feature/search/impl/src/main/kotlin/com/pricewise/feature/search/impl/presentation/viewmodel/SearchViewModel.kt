package com.pricewise.feature.search.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.impl.presentation.ui.Delivery
import com.pricewise.feature.search.impl.presentation.ui.FiltersState
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchFeatureApi,
    private val favoritesFeatureApi: FavoritesFeatureApi,
) : ViewModel() {

    init {
        viewModelScope.launch {
            favoritesFeatureApi.favoriteMutations.collect { event ->
                updateProductFavoriteState(
                    productId = event.externalId,
                    source = event.source,
                    isFavorite = event.isFavorite,
                )
            }
        }
    }

    private val defaultLimit: Int = 20
    private var searchJob: Job? = null
    private var searchAttempts = 0
    private val _resolvedTotal = MutableStateFlow(0)
    val resolvedTotal: StateFlow<Int> = _resolvedTotal.asStateFlow()
    private val _allItems = MutableStateFlow(listOf<Product>())
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private val _isProductChosen = MutableStateFlow(true)
    val isProductChosen: StateFlow<Boolean> = _isProductChosen.asStateFlow()
    private val _filtersState = MutableStateFlow(
        FiltersState(
            priceFrom = 0L,
            priceTo = 0L
        )
    )
    val filtersState: StateFlow<FiltersState> = _filtersState.asStateFlow()

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
        _filtersState.value = _filtersState.value.copy(isProductChosen = value)
    }

    fun setDeliveryChosen(value: Delivery) {
        _filtersState.value = _filtersState.value.copy(deliveryChosen = value)
    }

    fun setOnlyOriginals(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(onlyOriginals = value)
    }

    fun setOnlyNew(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(onlyNew = value)
    }

    fun setOnlyUsed(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(onlyUsed = value)
    }

    fun setOnlyMarketplaces(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(onlyMarketplaces = value)
    }

    fun setOnlyOfflineShops(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(onlyOfflineShops = value)
    }

    fun setPriceFrom(value: Long) {
        _filtersState.value = _filtersState.value.copy(priceFrom = value)
    }

    fun setPriceTo(value: Long) {
        _filtersState.value = _filtersState.value.copy(priceTo = value)
    }

    fun setPopularDiapasonChosen(value: Int) {
        _filtersState.value = _filtersState.value.copy(popularDiapasonChosen = value)
    }

    fun setCanPayLater(value: Boolean) {
        _filtersState.value = _filtersState.value.copy(canPayLater = value)
    }

    fun resetAllFilters() {
        _isProductChosen.value = true
        _filtersState.value = FiltersState(
            priceFrom = 0L,
            priceTo = 0L
        )
    }

    fun onProductFavoriteClick(productId: String) {
        val product = _allItems.value.firstOrNull { it.id == productId } ?: return
        val updatedFavoriteState = !product.isFavorite
        updateProductFavoriteState(
            productId = productId,
            source = product.source,
            isFavorite = updatedFavoriteState,
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                if (updatedFavoriteState) {
                    favoritesFeatureApi.addToFavorites(
                        request = FavoriteMutationRequest(
                            externalId = product.id,
                            source = product.source,
                            title = product.title,
                            price = product.price,
                            thumbnailUrl = product.thumbnailUrl,
                            productUrl = product.productUrl,
                            merchantLogoUrl = product.merchant.logoUrl,
                        ),
                    )
                } else {
                    favoritesFeatureApi.removeFromFavorites(
                        externalId = product.id,
                        source = product.source,
                    )
                }
            }.onFailure {
                updateProductFavoriteState(
                    productId = productId,
                    source = product.source,
                    isFavorite = product.isFavorite,
                )
                _uiState.update { state -> state.copy(isError = true) }
            }
        }
    }

    private fun updateProductFavoriteState(
        productId: String,
        source: String?,
        isFavorite: Boolean
    ) {
        _allItems.update { items ->
            items.map { product ->
                if (product.id == productId && (source == null || product.source == source)) {
                    product.copy(isFavorite = isFavorite)
                } else {
                    product
                }
            }
        }
        _uiState.update { state ->
            state.copy(
                items = state.items.map { product ->
                    if (product.id == productId && (source == null || product.source == source)) {
                        product.copy(isFavorite = isFavorite)
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
                        priceMin = filtersState.value.priceFrom,
                        priceMax = filtersState.value.priceTo,
                        delivery = filtersState.value.deliveryChosen.text,
                        onlyOriginal = filtersState.value.onlyOriginals,
                        onlyNew = filtersState.value.onlyNew,
                        onlyUsed = filtersState.value.onlyUsed,
                        marketplaceOnly = filtersState.value.onlyMarketplaces,
                        offlineOnly = filtersState.value.onlyOfflineShops,
                        playLaterOnly = filtersState.value.canPayLater,
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
