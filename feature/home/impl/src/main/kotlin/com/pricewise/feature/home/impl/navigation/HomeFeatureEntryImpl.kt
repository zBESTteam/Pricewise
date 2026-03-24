package com.pricewise.feature.home.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pricewise.feature.home.api.HomeFeatureEntry
import com.pricewise.feature.home.api.HomeRoutes
import com.pricewise.feature.home.impl.presentation.ui.MainScreen

class HomeFeatureEntryImpl : HomeFeatureEntry {
    override val homeRoute: String = HomeRoutes.Home
    override val route: String = homeRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        contentPadding: PaddingValues,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(route = homeRoute) {
            MainScreen(
                contentPadding = contentPadding,
                modifier = modifier,
            )
        }
    }
}
