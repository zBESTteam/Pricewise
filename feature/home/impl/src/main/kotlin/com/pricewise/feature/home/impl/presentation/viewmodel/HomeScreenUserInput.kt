package com.pricewise.feature.home.impl.presentation.viewmodel

data class HomeScreenUserInput(
    val searchQuery: String,
    val favoriteProductIds: Map<String, Boolean>,
)
