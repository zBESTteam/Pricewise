package com.pricewise.navigation.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pricewise.navigation.api.registerFeature

@Composable
fun PriceWiseNavHost(
    appState: PriceWiseAppState,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val homeFeatureEntry = PriceWiseFeatureProvider.homeFeatureEntry

    NavHost(
        navController = appState.navController,
        startDestination = homeFeatureEntry.homeRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        registerFeature(
            featureEntry = homeFeatureEntry,
            contentPadding = contentPadding,
            modifier = Modifier,
        )
        composable(PriceWiseTopLevelDestination.Favorites.route) {
            PlaceholderScreen(
                title = stringResource(R.string.nav_favorites),
                contentPadding = contentPadding,
                modifier = Modifier,
            )
        }
        composable(PriceWiseTopLevelDestination.Profile.route) {
            PlaceholderScreen(
                title = stringResource(R.string.nav_profile),
                contentPadding = contentPadding,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color(0xFFFEFEFE)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            color = Color(0xFF232323),
            fontWeight = FontWeight.SemiBold,
        )
    }
}
