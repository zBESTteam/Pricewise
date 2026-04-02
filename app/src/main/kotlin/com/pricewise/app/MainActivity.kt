package com.pricewise.app

import PriceWiseTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pricewise.core.ui.ThemeManager
import com.pricewise.navigation.impl.PriceWiseAppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeManager.loadTheme(LocalContext.current)
            PriceWiseTheme(ThemeManager.isDarkTheme) {
                PriceWiseAppNavigation(
                    window = window,
                    modifier = Modifier,
                )
            }
        }
    }
}
