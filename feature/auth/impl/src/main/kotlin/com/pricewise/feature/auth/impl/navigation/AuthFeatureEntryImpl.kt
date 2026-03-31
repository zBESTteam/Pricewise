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
                    navController.navigate("home") { // Replace with actual home route if different
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
                    navController.navigate("home") {
                        popUpTo(loginRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}
