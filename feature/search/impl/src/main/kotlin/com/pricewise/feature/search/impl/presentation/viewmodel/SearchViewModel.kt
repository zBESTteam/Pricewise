package com.pricewise.feature.search.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pricewise.feature.search.impl.presentation.ui.SearchUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _isProductChosen = MutableStateFlow(true)
    private val _deliveryChosen = MutableStateFlow(0)
    private val _onlyOriginals = MutableStateFlow(false)
    private val _onlyNew = MutableStateFlow(false)
    private val _onlyBU = MutableStateFlow(false)
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
    val onlyBU: StateFlow<Boolean> = _onlyBU.asStateFlow()
    val onlyMarketplaces: StateFlow<Boolean> = _onlyMarketplaces.asStateFlow()
    val onlyOfflineShops: StateFlow<Boolean> = _onlyOfflineShops.asStateFlow()
    val priceFrom: StateFlow<Long> = _priceFrom.asStateFlow()
    val priceTo: StateFlow<Long> = _priceTo.asStateFlow()
    val popularDiapasonChosen: StateFlow<Int> = _popularDiapasonChosen.asStateFlow()
    val canPayLater: StateFlow<Boolean> = _canPayLater.asStateFlow()


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

    fun makeFilter() {
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
        // Сделать
    }

    fun submitSearch() {
        // Сделать
    }

    fun performSearch(query: String) {
        // Сделать
    }

}