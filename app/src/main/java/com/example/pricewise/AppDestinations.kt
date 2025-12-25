package com.example.pricewise


enum class AppDestinations(
    val route: String,
    val contentDescriptionId: Int,
    val activeIconAsset: String,
    val inactiveIconAsset: String,
) {
    SEARCH("search", R.string.search_icon_description, "nav/search_active.svg", "nav/search_unactive.svg"),
    FAVORITES("favorites", R.string.favorites_icon_description, "nav/favorite_active.svg", "nav/favorite_unactive.svg"),
    PROFILE("profile", R.string.profile_icon_description, "nav/profile_active.svg", "nav/profile_unactive.svg"),
    LOGIN("login", R.string.profile_icon_description, "nav/profile_active.svg", "nav/profile_unactive.svg"),
    REGISTRATION("registration", R.string.profile_icon_description, "nav/profile_active.svg", "nav/profile_unactive.svg"),
}
