package com.vkedu.pricewise

enum class AppDestinations(
    val contentDescriptionId: Int,
    val defaultIcon: Int,
    val selectedIcon: Int
) {
    MAIN(
        R.string.main_icon_description,
        R.drawable.main_icon_default,
        R.drawable.main_icon_selected
    ),
    FAVORITES(
        R.string.favorites_icon_description,
        R.drawable.favorites_icon_default,
        R.drawable.favorites_icon_selected
    ),
    PROFILE(
        R.string.profile_icon_description,
        R.drawable.profile_icon_default,
        R.drawable.profile_icon_selected
    ),
}