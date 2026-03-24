package com.pricewise.navigation.api

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder

interface NavigationFeatureEntry {
    val route: String

    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        contentPadding: PaddingValues,
        modifier: Modifier,
    )
}

fun NavGraphBuilder.registerFeature(
    featureEntry: NavigationFeatureEntry,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    featureEntry.registerGraph(
        navGraphBuilder = this,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}
