package com.pricewise.feature.home.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.api.HomeScreenApi
import com.pricewise.feature.home.impl.presentation.ui.HomeScreen as HomeScreenContent
import com.pricewise.feature.home.impl.ui.theme.PriceWiseComposeTheme
import javax.inject.Inject

class HomeScreenApiImpl @Inject constructor() : HomeScreenApi {

    @Composable
    override fun HomeScreen(
        contentPadding: PaddingValues,
        modifier: Modifier,
        onSearchRequest: (String) -> Unit,
        onPhotoSearchRequest: () -> Unit,
    ) {
        PriceWiseComposeTheme {
            Surface {
                HomeScreenContent(
                    contentPadding = contentPadding,
                    modifier = modifier,
                    onSearchRequest = onSearchRequest,
                    onPhotoSearchRequest = onPhotoSearchRequest,
                )
            }
        }
    }
}
