package com.example.pricewise

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
            modifier = contentModifier
        ) {
            composable(AppDestinations.SEARCH.route) {
                if (searchState.submittedQuery.isBlank()) {
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
