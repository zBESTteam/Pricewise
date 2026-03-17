package com.pricewise.app.navigation

import android.view.Window
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController

@Composable
fun PriceWiseAppNavigation(
    window: Window,
    modifier: Modifier,
) {
    val appState = rememberPriceWiseAppState(
        navController = rememberNavController(),
    )

    PriceWiseSystemBars(
        window = window,
        useLightStatusIcons = appState.shouldUseLightStatusIcons,
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFEFEFE),
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
        ),
        bottomBar = {
            PriceWiseBottomBar(
                destinations = PriceWiseTopLevelDestination.entries,
                currentDestination = appState.currentTopLevelDestination,
                onDestinationSelected = appState::navigateToTopLevelDestination,
                modifier = Modifier,
            )
        },
    ) { innerPadding ->
        PriceWiseNavHost(
            appState = appState,
            contentPadding = innerPadding,
            modifier = Modifier,
        )
    }
}
