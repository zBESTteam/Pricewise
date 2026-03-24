package com.pricewise.navigation.impl

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: PriceWiseTopLevelDestination?
        @Composable get() = PriceWiseTopLevelDestination.entries.firstOrNull { destination ->
            currentDestination.isRouteInHierarchy(destination)
        }

    val shouldUseLightStatusIcons: Boolean
        @Composable get() = currentTopLevelDestination == PriceWiseTopLevelDestination.Home

    fun navigateToTopLevelDestination(destination: PriceWiseTopLevelDestination) {
        if (navController.currentDestination.isRouteInHierarchy(destination)) {
            return
        }
        navController.navigate(destination.route) {
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
        navDestination.route == destination.route
    } == true
}
