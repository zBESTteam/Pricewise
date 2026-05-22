package com.pricewise.feature.auth.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pricewise.feature.auth.api.AuthFeatureEntry
import com.pricewise.feature.auth.api.AuthRoutes
import com.pricewise.feature.auth.impl.presentation.ui.AuthorizationScreen
import com.pricewise.feature.auth.impl.presentation.ui.RegistrationScreen
import com.pricewise.feature.home.api.HomeRoutes
import javax.inject.Inject

class AuthFeatureEntryImpl @Inject constructor() : AuthFeatureEntry {
    override val loginRoute: String = AuthRoutes.Login
    override val registerRoute: String = AuthRoutes.Register
    override val route: String = loginRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        contentPadding: PaddingValues,
        modifier: Modifier,
    ) {
        navGraphBuilder.composable(route = loginRoute) {
            AuthorizationScreen(
                onNavigateToRegistration = {
                    navController.navigate(registerRoute)
                },
                onNavigateToMain = {
                    navController.navigate(HomeRoutes.Home) {
                        popUpTo(loginRoute) { inclusive = true }
                    }
                }
            )
        }

        navGraphBuilder.composable(route = registerRoute) {
            RegistrationScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToMain = {
                    navController.navigate(HomeRoutes.Home) {
                        popUpTo(loginRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}
