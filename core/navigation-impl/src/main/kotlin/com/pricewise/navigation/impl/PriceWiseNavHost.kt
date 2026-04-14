package com.pricewise.navigation.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pricewise.core.ui.ThemeManager
import com.pricewise.feature.profile.impl.ProfileScreenRoot
import com.pricewise.navigation.api.registerFeature

@Composable
fun PriceWiseNavHost(
    appState: PriceWiseAppState,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val authFeatureEntry = PriceWiseFeatureProvider.authFeatureEntry
    val homeFeatureEntry = PriceWiseFeatureProvider.homeFeatureEntry
    val searchFeatureEntry = PriceWiseFeatureProvider.searchFeatureEntry
    val favoritesFeatureEntry = PriceWiseFeatureProvider.favoritesFeatureEntry

    NavHost(
        navController = appState.navController,
        startDestination = authFeatureEntry.loginRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        registerFeature(
            featureEntry = authFeatureEntry,
            navController = appState.navController,
            contentPadding = contentPadding,
            modifier = Modifier,
        )
        registerFeature(
            featureEntry = homeFeatureEntry,
            navController = appState.navController,
            contentPadding = contentPadding,
            modifier = Modifier,
        )
        registerFeature(
            featureEntry = searchFeatureEntry,
            navController = appState.navController,
            contentPadding = contentPadding,
            modifier = Modifier,
        )
        registerFeature(
            featureEntry = favoritesFeatureEntry,
            navController = appState.navController,
            contentPadding = contentPadding,
            modifier = Modifier,
        )
        composable(PriceWiseTopLevelDestination.Profile.route) {
            ProfileScreenRoot(navController = appState.navController)
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    contentPadding: PaddingValues,
    modifier: Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        if (title == stringResource(R.string.nav_profile)) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    ThemeManager.toggleTheme(context)
                }
            ) {

            }
        }
    }
}







                
            ) {

            }
        }
    }
}
