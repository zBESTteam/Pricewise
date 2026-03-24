package com.pricewise.feature.search.impl.navigation

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pricewise.feature.search.api.SearchFeatureEntry
import com.pricewise.feature.search.api.SearchRoutes
import com.pricewise.feature.search.impl.presentation.ui.SearchScreen

class SearchFeatureEntryImpl : SearchFeatureEntry {
    override val searchRoute: String = SearchRoutes.searchRoute
    override val route: String = searchRoute

    override fun createRoute(searchQuery: String): String {
        val trimmedSearchQuery = searchQuery.trim()

        return if (trimmedSearchQuery.isEmpty()) {
            SearchRoutes.searchBaseRoute
        } else {
            SearchRoutes.searchBaseRoute +
                "?${SearchRoutes.searchQueryArgument}=${Uri.encode(trimmedSearchQuery)}"
        }
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        contentPadding: PaddingValues,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(
            route = searchRoute,
            arguments = listOf(
                navArgument(name = SearchRoutes.searchQueryArgument) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            SearchScreen(
                contentPadding = contentPadding,
                modifier = modifier,
                initialQuery = backStackEntry.arguments
                    ?.getString(SearchRoutes.searchQueryArgument)
                    .orEmpty(),
            )
        }
    }
}
