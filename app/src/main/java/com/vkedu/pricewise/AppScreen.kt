package com.vkedu.pricewise

enum class AppScreen(
    val route: String,
    val contentDescriptionId: Int,
    val defaultIcon: Int,
    val selectedIcon: Int
) {
    MAIN(
        "main",
        R.string.main_icon_description,
        R.drawable.main_icon_default,
        R.drawable.main_icon_selected
    ),
    FAVORITES(
        "favorites",
        R.string.favorites_icon_description,
        R.drawable.favorites_icon_default,
        R.drawable.favorites_icon_selected
    ),
    PROFILE(
        "profile",
        R.string.profile_icon_description,
        R.drawable.profile_icon_default,
        R.drawable.profile_icon_selected
    ),
}