package com.pricewise.navigation.impl

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pricewise.feature.search.api.SearchRoutes

@Composable
fun rememberPriceWiseAppState(
    navController: NavHostController,
): PriceWiseAppState {
    return remember(navController) {
        PriceWiseAppState(navController = navController)
    }
}

@Stable
class PriceWiseAppState internal constructor(
    val navController: NavHostController,
) {
    private var lastHomeRoute: String = PriceWiseFeatureProvider.homeFeatureEntry.homeRoute

    val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val currentDestination: NavDestination?
        @Composable get() = currentBackStackEntry?.destination

    val currentTopLevelDestination: PriceWiseTopLevelDestination?
        @Composable get() = PriceWiseTopLevelDestination.entries.firstOrNull { destination ->
            currentDestination.isRouteInHierarchy(destination)
        }

    val shouldUseLightStatusIcons: Boolean
        @Composable get() = currentTopLevelDestination == PriceWiseTopLevelDestination.Home ||
            currentTopLevelDestination == PriceWiseTopLevelDestination.Favorites

    fun rememberHomeRoute(backStackEntry: NavBackStackEntry?) {
        val destination = backStackEntry?.destination ?: return
        if (!destination.isRouteInHierarchy(PriceWiseTopLevelDestination.Home)) {
            return
        }

        lastHomeRoute = backStackEntry.toHomeRoute()
    }

    fun navigateToTopLevelDestination(destination: PriceWiseTopLevelDestination) {
        if (navController.currentDestination.isExactTopLevelDestination(destination)) {
            return
        }

        if (
            navController.currentDestination.isRouteInHierarchy(destination) &&
            navController.popBackStack(route = destination.route, inclusive = false)
        ) {
            return
        }

        val targetRoute = if (destination == PriceWiseTopLevelDestination.Home) {
            lastHomeRoute
        } else {
            destination.route
        }

        navController.navigate(targetRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun PriceWiseSystemBars(
    window: Window,
    useLightStatusIcons: Boolean,
) {
    val view = LocalView.current
    SideEffect {
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = !useLightStatusIcons
        controller.isAppearanceLightNavigationBars = true
    }
}

private fun NavDestination?.isRouteInHierarchy(
    destination: PriceWiseTopLevelDestination,
): Boolean {
    return this?.hierarchy?.any { navDestination ->
        navDestination.route in destination.hierarchyRoutes
    } == true
}

private fun NavDestination?.isExactTopLevelDestination(
    destination: PriceWiseTopLevelDestination,
): Boolean {
    return this?.hierarchy?.any { navDestination ->
        navDestination.route == destination.route
    } == true
}

private fun NavBackStackEntry.toHomeRoute(): String {
    val route = destination.route

    return when (route) {
        PriceWiseFeatureProvider.homeFeatureEntry.homeRoute -> {
            PriceWiseFeatureProvider.homeFeatureEntry.homeRoute
        }

        SearchRoutes.searchRoute,
        SearchRoutes.searchBaseRoute,
        -> {
            val query = arguments?.getString(SearchRoutes.searchQueryArgument).orEmpty()
            PriceWiseFeatureProvider.searchFeatureEntry.createRoute(searchQuery = query)
        }

        else -> {
            route.orEmpty().ifBlank { PriceWiseFeatureProvider.homeFeatureEntry.homeRoute }
        }
    }
}
