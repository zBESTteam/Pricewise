package com.pricewise.feature.home.api

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface HomeScreenApi {
    @Composable
    fun HomeScreen(
        contentPadding: PaddingValues,
        modifier: Modifier,
        onSearchRequest: (String) -> Unit,
        onPhotoSearchRequest: () -> Unit,
    )
}
