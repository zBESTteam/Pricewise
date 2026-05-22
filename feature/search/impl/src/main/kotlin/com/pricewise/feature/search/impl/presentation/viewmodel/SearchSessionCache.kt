package com.pricewise.feature.search.impl.presentation.viewmodel

import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow

@Singleton
class SearchSessionCache @Inject constructor() {

    val resolvedTotal = MutableStateFlow(0)
    val allItems = MutableStateFlow<List<Product>>(emptyList())
    val uiState = MutableStateFlow(SearchUiState())

    val isProductChosen = MutableStateFlow(true)
    val deliveryChosen = MutableStateFlow(0)
    val onlyOriginals = MutableStateFlow(false)
    val onlyNew = MutableStateFlow(false)
    val onlyUsed = MutableStateFlow(false)
    val onlyMarketplaces = MutableStateFlow(false)
    val onlyOfflineShops = MutableStateFlow(false)
    val priceFrom = MutableStateFlow(0L)
    val priceTo = MutableStateFlow(0L)
    val popularDiapasonChosen = MutableStateFlow(0)
    val canPayLater = MutableStateFlow(false)

    private val searchAttemptsRef = AtomicInteger(0)
    var searchAttempts: Int
        get() = searchAttemptsRef.get()
        set(value) {
            searchAttemptsRef.set(value)
        }
}
