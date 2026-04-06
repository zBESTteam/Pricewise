package com.pricewise.navigation.impl

import com.pricewise.feature.auth.api.AuthFeatureEntry
import com.pricewise.feature.auth.impl.navigation.AuthFeatureEntryImpl
import com.pricewise.feature.favorites.api.FavoritesFeatureEntry
import com.pricewise.feature.favorites.impl.navigation.FavoritesFeatureEntryImpl
import com.pricewise.feature.home.api.HomeFeatureEntry
import com.pricewise.feature.home.impl.navigation.HomeFeatureEntryImpl
import com.pricewise.feature.search.api.SearchFeatureEntry
import com.pricewise.feature.search.impl.navigation.SearchFeatureEntryImpl

object PriceWiseFeatureProvider {
    val authFeatureEntry: AuthFeatureEntry = AuthFeatureEntryImpl()
    val searchFeatureEntry: SearchFeatureEntry = SearchFeatureEntryImpl()
    val homeFeatureEntry: HomeFeatureEntry = HomeFeatureEntryImpl(
        searchFeatureEntry = searchFeatureEntry,
    )
    val favoritesFeatureEntry: FavoritesFeatureEntry = FavoritesFeatureEntryImpl()
}
