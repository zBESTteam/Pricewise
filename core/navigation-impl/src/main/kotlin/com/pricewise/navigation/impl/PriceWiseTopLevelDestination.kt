package com.pricewise.navigation.impl

import androidx.annotation.StringRes

enum class PriceWiseTopLevelDestination(
    val route: String,
    val hierarchyRoutes: Set<String>,
    @param:StringRes val contentDescriptionRes: Int,
) {
    Home(
        route = PriceWiseFeatureProvider.homeFeatureEntry.homeRoute,
        hierarchyRoutes = setOf(
            PriceWiseFeatureProvider.homeFeatureEntry.homeRoute,
            PriceWiseFeatureProvider.searchFeatureEntry.route,
        ),
        contentDescriptionRes = R.string.nav_home,
    ),
    Favorites(
        route = "favorites",
        hierarchyRoutes = setOf("favorites"),
        contentDescriptionRes = R.string.nav_favorites,
    ),
    Profile(
        route = "profile",
        hierarchyRoutes = setOf("profile"),
        contentDescriptionRes = R.string.nav_profile,
    ),
}
