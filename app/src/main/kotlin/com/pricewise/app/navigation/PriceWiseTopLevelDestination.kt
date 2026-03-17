package com.pricewise.app.navigation

import androidx.annotation.StringRes
import com.pricewise.app.R

enum class PriceWiseTopLevelDestination(
    val route: String,
    @param:StringRes val contentDescriptionRes: Int,
) {
    Home(
        route = "home",
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
