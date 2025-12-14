package com.vkedu.pricewise

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vkedu.pricewise.screens.RegistrationScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.MAIN.route,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) }) {
        composable(route = AppScreen.MAIN.route) {
            RegistrationScreen() // Для теста, нужно заменить на главный экран
        }
        composable(route = AppScreen.FAVORITES.route) {
            Text("Favorites")
        }
        composable(route = AppScreen.PROFILE.route) {
            Text("Profile")
        }
    }
}