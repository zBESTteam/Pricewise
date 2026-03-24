package com.pricewise.navigation.impl

import androidx.annotation.StringRes

enum class PriceWiseTopLevelDestination(
    val route: String,
    @param:StringRes val contentDescriptionRes: Int,
) {
    Home(
        route = PriceWiseFeatureProvider.homeFeatureEntry.homeRoute,
        contentDescriptionRes = R.string.nav_home,
    ),
    Favorites(
        route = "favorites",
        contentDescriptionRes = R.string.nav_favorites,
    ),
    Profile(
        route = "profile",
        contentDescriptionRes = R.string.nav_profile,
    ),
}
