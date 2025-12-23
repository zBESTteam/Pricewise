package com.example.pricewise

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pricewise.feature.main.presentation.MainScreen
import com.example.pricewise.feature.search.presentation.SearchScreen
import com.example.pricewise.feature.search.presentation.SearchViewModel
import com.example.pricewise.navigation.PricewiseBottomBar
import com.example.pricewise.ui.theme.PricewiseTheme

@PreviewScreenSizes
@Composable
fun PricewiseApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val searchViewModel: SearchViewModel = viewModel()
    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            PricewiseBottomBar(
                currentDestination = currentDestination,
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        NavHost(
            navController = navController,
            startDestination = AppDestinations.SEARCH.route,
            modifier = contentModifier,
            enterTransition = { defaultEnterTransition() },
            exitTransition = { defaultExitTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() },
        ) {
            composable(AppDestinations.SEARCH.route) {
                AnimatedContent(
                    targetState = searchState.submittedQuery.isBlank(),
                    transitionSpec = { searchContentTransform() },
                    modifier = Modifier.fillMaxSize(),
                    label = "SearchContent",
                ) { showMain ->
                    if (showMain) {
                        MainScreen(
                            modifier = Modifier.fillMaxSize(),
                            searchQueryOverride = searchState.query,
                            onSearchQueryChangeOverride = searchViewModel::onQueryChange,
                            onSearchSubmitOverride = searchViewModel::submitSearch,
                        )
                    } else {
                        SearchScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = searchViewModel,
                        )
                    }
                }
            }
            composable(AppDestinations.FAVORITES.route) {
                PlaceholderScreen(
                    label = stringResource(id = AppDestinations.FAVORITES.contentDescriptionId),
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(AppDestinations.PROFILE.route) {
                PlaceholderScreen(
                    label = stringResource(id = AppDestinations.PROFILE.contentDescriptionId),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(text = label)
    }
}

@Composable
@Preview(showBackground = true)
fun MainPreview() {
    PricewiseTheme {
        PricewiseApp()
    }
}

private fun defaultEnterTransition(): androidx.compose.animation.EnterTransition {
    return fadeIn(animationSpec = androidx.compose.animation.core.tween(220)) +
        scaleIn(
            initialScale = 0.98f,
            animationSpec = androidx.compose.animation.core.tween(220),
        )
}

private fun defaultExitTransition(): androidx.compose.animation.ExitTransition {
    return fadeOut(animationSpec = androidx.compose.animation.core.tween(180)) +
        scaleOut(
            targetScale = 0.98f,
            animationSpec = androidx.compose.animation.core.tween(180),
        )
}

private fun defaultPopEnterTransition(): androidx.compose.animation.EnterTransition {
    return fadeIn(animationSpec = androidx.compose.animation.core.tween(180)) +
        scaleIn(
            initialScale = 0.98f,
            animationSpec = androidx.compose.animation.core.tween(180),
        )
}

private fun defaultPopExitTransition(): androidx.compose.animation.ExitTransition {
    return fadeOut(animationSpec = androidx.compose.animation.core.tween(160)) +
        scaleOut(
            targetScale = 0.98f,
            animationSpec = androidx.compose.animation.core.tween(160),
        )
}

private fun searchContentTransform(): ContentTransform {
    val enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(200)) +
        slideInVertically(
            animationSpec = androidx.compose.animation.core.tween(200),
            initialOffsetY = { it / 12 },
        )
    val exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(150)) +
        slideOutVertically(
            animationSpec = androidx.compose.animation.core.tween(150),
            targetOffsetY = { -it / 12 },
        )
    return enter togetherWith exit
}
