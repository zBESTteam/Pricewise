package com.pricewise.feature.search.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.impl.presentation.ui.Delivery
import com.pricewise.feature.search.impl.presentation.ui.FiltersState
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
import com.pricewise.feature.search.impl.presentation.ui.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val searchSessionCache: SearchSessionCache,
) : BaseViewModel() {

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
    private var canRewriteFilters = true
    private val _chosenSort = MutableStateFlow(Sort.DEFAULT)
    val chosenSort: StateFlow<Sort> = _chosenSort.asStateFlow()

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

    init {
        viewModelScopeSafe.launch {
            favoritesFeatureApi.favoriteStates.collect { favoriteStates ->
                applyFavoriteStates(favoriteStates = favoriteStates)
            }
        }

        resumePendingSearchIfNeeded()
    }

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

    fun onProductFavoriteClick(productId: String, source: String) {
        val normalizedSource = normalizeSource(source)
        val product = allItemsState.value.firstOrNull { item ->
            item.id == productId && normalizeSource(item.source) == normalizedSource
        } ?: return
        val updatedFavoriteState = !product.isFavorite
        updateProductFavoriteState(
            productId = productId,
            source = normalizedSource,
            isFavorite = updatedFavoriteState,
        )

        viewModelScopeSafe.launch(Dispatchers.IO) {
            runCatching {
                if (updatedFavoriteState) {
                    favoritesFeatureApi.addToFavorites(
                        request = FavoriteMutationRequest(
                            externalId = product.id,
                            source = normalizedSource,
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
                        source = normalizedSource,
                    )
                }
            }.onFailure {
                updateProductFavoriteState(
                    productId = productId,
                    source = normalizedSource,
                    isFavorite = product.isFavorite,
                )
                uiStateState.update { state -> state.copy(isError = true) }
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
                if (
                    product.id == productId &&
                    (normalizedSource == null || normalizeSource(product.source) == normalizedSource)
                ) {
                    product.copy(isFavorite = isFavorite)
                } else {
                    product
                }
            }
        }
        uiStateState.update { state ->
            state.copy(
                items = state.items.map { product ->
                    if (
                        product.id == productId &&
                        (normalizedSource == null || normalizeSource(product.source) == normalizedSource)
                    ) {
                        product.copy(isFavorite = isFavorite)
                    } else {
                        product
                    }
                },
            )
        }
    }

    fun onQueryChange(query: String) {
        uiStateState.update { state ->
            state.copy(query = query)
        }
    }

    fun clearQuery() {
        uiStateState.update { state -> state.copy(query = "") }
    }

    fun initializeSearch(searchQuery: String) {
        val trimmedSearchQuery = searchQuery.trim()

        if (trimmedSearchQuery.isEmpty()) {
            return
        }

        if (
            uiStateState.value.submittedQuery == trimmedSearchQuery &&
            uiStateState.value.query == trimmedSearchQuery
        ) {
            return
        }

        uiStateState.update { state ->
            state.copy(query = trimmedSearchQuery)
        }
        submitSearch()
    }

    fun submitSearch() {
        canRewriteFilters = true
        searchJob?.cancel()
        searchAttempts = 0
        viewModelScope.launch(Dispatchers.IO) {
            searchJob = viewModelScope.launch {
                setChosenSort(Sort.DEFAULT)
                resetAllFilters()
                performSearch(_uiState.value.query.trim())
            }
            performSearchLoop(query)
        }
        _uiState.update { state ->
            state.copy(
                checkedSources = 0
            )
        }
    }

    private suspend fun performSearchLoop(query: String) {
        try {
            var attempts = 0
            while (attempts <= MAX_SEARCH_ATTEMPTS) {
                val result = withContext(Dispatchers.IO) {
                    repository.search(
                        query = query,
                        limit = defaultLimit,
                        offset = 0,
                        perSource = true,
                        partial = true,
                        sources = sources,
                        sort = _chosenSort.value.text,
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
                val items = applyFavoriteStatesToItems(
                    items = result.items,
                    favoriteStates = favoritesFeatureApi.favoriteStates.value,
                )
                allItemsState.value = items
                val shouldSearchAgain = result.pendingSources.isNotEmpty() && attempts < MAX_SEARCH_ATTEMPTS
                uiStateState.update { state ->
                    val checked = result.checkedSources ?: state.checkedSources
                    val totalSources = result.totalSources
                    resolvedTotalState.value = when {
                        totalSources != null && totalSources > 0 -> {
                            maxOf(totalSources, checked)
                        }
                        checked > state.totalSources -> checked
                        else -> state.totalSources
                    }
                    state.copy(
                        isLoading = shouldSearchAgain,
                        items = items,
                        hasMore = result.hasMore,
                        checkedSources = checked,
                        totalSources = resolvedTotal.value,
                        pendingSources = result.pendingSources,
                        minPrice = if (canRewriteFilters) result.items.minByOrNull { it.price }?.price ?: 0 else state.minPrice,
                        maxPrice = if (canRewriteFilters) result.items.maxByOrNull { it.price }?.price ?: 0 else state.maxPrice
                        )
                }
                if (shouldSearchAgain) {
                    repeatSearch(query = query)
                }
                else {
                    canRewriteFilters = false
                }
            } catch (_: Exception) {
                _uiState.update { state -> state.copy(isError = true) }
            }
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (_: Exception) {
            uiStateState.update { state -> state.copy(isError = true, isLoading = false) }
        }
    }

    private fun applyFavoriteStates(favoriteStates: Map<String, Boolean>) {
        val items = applyFavoriteStatesToItems(
            items = allItemsState.value,
            favoriteStates = favoriteStates,
        )
        allItemsState.value = items
        uiStateState.update { state ->
            state.copy(items = applyFavoriteStatesToItems(items = state.items, favoriteStates = favoriteStates))
        }
    }

    private fun applyFavoriteStatesToItems(
        items: List<Product>,
        favoriteStates: Map<String, Boolean>,
    ): List<Product> {
        return items.map { product ->
            val favoriteKey = makeFavoriteKey(
                productId = product.id,
                source = product.source,
            )
            val isFavorite = favoriteStates[favoriteKey] ?: product.isFavorite
            product.copy(isFavorite = isFavorite)
        }
    }

    private fun resumePendingSearchIfNeeded() {
        val state = uiStateState.value
        if (!state.isLoading || state.submittedQuery.isBlank()) {
            return
        }
        val query = state.submittedQuery
        searchJob = viewModelScopeSafe.launch {
            performSearchLoop(query)
        }
    }

    fun setChosenSort(newSort: Sort) {
        _chosenSort.value = newSort
    }
}

private const val MAX_SEARCH_ATTEMPTS = 8
private const val SEARCH_INTERVAL_MS: Long = 1000

