package com.pricewise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.api.HomeScreenApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var homeScreenApi: HomeScreenApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            homeScreenApi.HomeScreen(
                contentPadding = PaddingValues(),
                modifier = Modifier,
                onSearchRequest = {},
                onPhotoSearchRequest = {},
            )
        }
    }
}
