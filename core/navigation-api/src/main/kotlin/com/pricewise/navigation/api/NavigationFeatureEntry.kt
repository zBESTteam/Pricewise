package com.pricewise.navigation.api

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavigationFeatureEntry {
    val route: String

    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        contentPadding: PaddingValues,
        modifier: Modifier,
    )
}

fun NavGraphBuilder.registerFeature(
    featureEntry: NavigationFeatureEntry,
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    featureEntry.registerGraph(
        navGraphBuilder = this,
        navController = navController,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}
