package com.pricewise.feature.search.api

import com.pricewise.navigation.api.NavigationFeatureEntry

interface SearchFeatureEntry : NavigationFeatureEntry {
    val searchRoute: String

    fun createRoute(searchQuery: String): String
}
