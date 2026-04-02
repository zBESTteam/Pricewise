package com.pricewise.app

import PriceWiseTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pricewise.core.ui.viewmodel.ThemeViewModel
import com.pricewise.navigation.impl.PriceWiseAppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            PriceWiseTheme(themeViewModel.isDarkTheme) {
                PriceWiseAppNavigation(
                    window = window,
                    modifier = Modifier,
                )
            }
        }
    }
}
