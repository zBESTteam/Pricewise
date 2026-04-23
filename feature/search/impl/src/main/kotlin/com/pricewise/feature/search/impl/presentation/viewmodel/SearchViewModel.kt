package com.pricewise.feature.search.impl.presentation.viewmodel

import com.pricewise.core.ui.viewmodel.BaseViewModel
import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
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
    private val resolvedTotalState = searchSessionCache.resolvedTotal
    val resolvedTotal: StateFlow<Int> = resolvedTotalState.asStateFlow()
    private val allItemsState = searchSessionCache.allItems
    private val uiStateState = searchSessionCache.uiState
    val uiState: StateFlow<SearchUiState> = uiStateState.asStateFlow()

    private val isProductChosenState = searchSessionCache.isProductChosen
    private val deliveryChosenState = searchSessionCache.deliveryChosen
    private val onlyOriginalsState = searchSessionCache.onlyOriginals
    private val onlyNewState = searchSessionCache.onlyNew
    private val onlyUsedState = searchSessionCache.onlyUsed
    private val onlyMarketplacesState = searchSessionCache.onlyMarketplaces
    private val onlyOfflineShopsState = searchSessionCache.onlyOfflineShops
    private val priceFromState = searchSessionCache.priceFrom
    private val priceToState = searchSessionCache.priceTo
    private val popularDiapasonChosenState = searchSessionCache.popularDiapasonChosen
    private val canPayLaterState = searchSessionCache.canPayLater
    val isProductChosen: StateFlow<Boolean> = isProductChosenState.asStateFlow()
    val deliveryChosen: StateFlow<Int> = deliveryChosenState.asStateFlow()
    val onlyOriginals: StateFlow<Boolean> = onlyOriginalsState.asStateFlow()
    val onlyNew: StateFlow<Boolean> = onlyNewState.asStateFlow()
    val onlyUsed: StateFlow<Boolean> = onlyUsedState.asStateFlow()
    val onlyMarketplaces: StateFlow<Boolean> = onlyMarketplacesState.asStateFlow()
    val onlyOfflineShops: StateFlow<Boolean> = onlyOfflineShopsState.asStateFlow()
    val priceFrom: StateFlow<Long> = priceFromState.asStateFlow()
    val priceTo: StateFlow<Long> = priceToState.asStateFlow()
    val popularDiapasonChosen: StateFlow<Int> = popularDiapasonChosenState.asStateFlow()
    val canPayLater: StateFlow<Boolean> = canPayLaterState.asStateFlow()
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
        isProductChosenState.value = value
    }

    fun setDeliveryChosen(value: Int) {
        deliveryChosenState.value = value
    }

    fun setOnlyOriginals(value: Boolean) {
        onlyOriginalsState.value = value
    }

    fun setOnlyNew(value: Boolean) {
        onlyNewState.value = value
    }

    fun setOnlyUsed(value: Boolean) {
        onlyUsedState.value = value
    }

    fun setOnlyMarketplaces(value: Boolean) {
        onlyMarketplacesState.value = value
    }

    fun setOnlyOfflineShops(value: Boolean) {
        onlyOfflineShopsState.value = value
    }

    fun setPriceFrom(value: Long) {
        priceFromState.value = value
    }

    fun setPriceTo(value: Long) {
        priceToState.value = value
    }

    fun setPopularDiapasonChosen(value: Int) {
        popularDiapasonChosenState.value = value
    }

    fun setCanPayLater(value: Boolean) {
        canPayLaterState.value = value
    }

    fun resetAllFilters() {
        isProductChosenState.value = true
        deliveryChosenState.value = 0
        onlyOriginalsState.value = false
        onlyNewState.value = false
        onlyUsedState.value = false
        onlyMarketplacesState.value = false
        onlyOfflineShopsState.value = false
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

    private fun updateProductFavoriteState(productId: String, source: String?, isFavorite: Boolean) {
        val normalizedSource = source?.let(::normalizeSource)

        allItemsState.update { items ->
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
        searchJob?.cancel()
        searchSessionCache.searchAttempts = 0
        val query = uiStateState.value.query.trim()
        if (query.isEmpty()) return
        searchJob = viewModelScopeSafe.launch {
            resetAllFilters()
            allItemsState.value = emptyList()
            uiStateState.update { state ->
                state.copy(
                    isLoading = true,
                    submittedQuery = query,
                    isError = false,
                    items = emptyList(),
                    checkedSources = 0,
                    pendingSources = emptyList(),
                )
            }
            performSearchLoop(query)
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
                        sort = "",
                        priceMin = priceFromState.value,
                        priceMax = priceToState.value,
                        delivery = deliveryChosenState.value.toString(),
                        onlyOriginal = onlyOriginalsState.value,
                        onlyNew = onlyNewState.value,
                        onlyUsed = onlyUsedState.value,
                        marketplaceOnly = onlyMarketplacesState.value,
                        offlineOnly = onlyOfflineShopsState.value,
                        playLaterOnly = canPayLaterState.value,
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
                    )
                }
                if (!shouldSearchAgain) break
                attempts++
                searchSessionCache.searchAttempts = attempts
                delay(SEARCH_INTERVAL_MS)
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

    private fun makeFavoriteKey(productId: String, source: String): String {
        return "$productId|${normalizeSource(source)}"
    }

    private fun normalizeSource(source: String): String {
        return source.trim().ifBlank { "unknown" }
    }
}

private const val MAX_SEARCH_ATTEMPTS = 8
private const val SEARCH_INTERVAL_MS: Long = 1000

