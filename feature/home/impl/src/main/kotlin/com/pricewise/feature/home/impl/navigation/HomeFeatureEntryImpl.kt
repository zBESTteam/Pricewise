package com.pricewise.feature.home.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pricewise.feature.home.api.HomeFeatureEntry
import com.pricewise.feature.home.api.HomeRoutes
import com.pricewise.feature.home.impl.presentation.HomeScreenApiImpl
import com.pricewise.feature.search.api.SearchFeatureEntry

class HomeFeatureEntryImpl(
    private val searchFeatureEntry: SearchFeatureEntry,
) : HomeFeatureEntry {
    private val homeScreenApi = HomeScreenApiImpl()

    override val homeRoute: String = HomeRoutes.Home
    override val route: String = homeRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        contentPadding: PaddingValues,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(route = homeRoute) {
            homeScreenApi.HomeScreen(
                contentPadding = contentPadding,
                modifier = modifier,
                onSearchRequest = { searchQuery: String ->
                    navController.navigate(searchFeatureEntry.createRoute(searchQuery = searchQuery)) {
                        launchSingleTop = true
                    }
                },
                onPhotoSearchRequest = {},
            )
        }
    }
}
