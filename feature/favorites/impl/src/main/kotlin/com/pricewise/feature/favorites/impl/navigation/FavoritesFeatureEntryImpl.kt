package com.pricewise.feature.favorites.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pricewise.feature.favorites.api.FavoritesFeatureEntry
import com.pricewise.feature.favorites.impl.presentation.ui.FavoritesScreen

class FavoritesFeatureEntryImpl : FavoritesFeatureEntry {

    override val favoritesRoute: String = "favorites"
    override val route: String = favoritesRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        contentPadding: PaddingValues,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(route = favoritesRoute) {
            FavoritesScreen(contentPadding = contentPadding)
        }
    }
}
