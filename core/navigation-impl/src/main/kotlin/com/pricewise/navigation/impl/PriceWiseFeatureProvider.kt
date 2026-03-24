package com.pricewise.navigation.impl

import com.pricewise.feature.home.api.HomeFeatureEntry
import com.pricewise.feature.home.impl.navigation.HomeFeatureEntryImpl
import com.pricewise.feature.search.api.SearchFeatureEntry
import com.pricewise.feature.search.impl.navigation.SearchFeatureEntryImpl

object PriceWiseFeatureProvider {
    val searchFeatureEntry: SearchFeatureEntry = SearchFeatureEntryImpl()
    val homeFeatureEntry: HomeFeatureEntry = HomeFeatureEntryImpl(
        searchFeatureEntry = searchFeatureEntry,
    )
}
