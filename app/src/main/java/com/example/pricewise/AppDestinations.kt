package com.example.pricewise

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestinations(
    val contentDescriptionId: Int,
    val icon: ImageVector,
) {
    MAIN(R.string.main_icon_description, Icons.Default.Home),
    FAVORITES(R.string.favorites_icon_description, Icons.Default.Favorite),
    PROFILE(R.string.profile_icon_description, Icons.Default.AccountBox),
}