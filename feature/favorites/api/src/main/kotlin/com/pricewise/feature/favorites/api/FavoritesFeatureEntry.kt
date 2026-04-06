package com.pricewise.feature.favorites.api

import com.pricewise.navigation.api.NavigationFeatureEntry

interface FavoritesFeatureEntry : NavigationFeatureEntry {
    val favoritesRoute: String
}
