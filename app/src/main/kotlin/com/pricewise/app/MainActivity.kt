package com.pricewise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.impl.presentation.ui.MainScreen
import com.pricewise.feature.home.impl.ui.theme.PriceWiseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriceWiseComposeTheme {
                Surface {
                    MainScreen(
                        contentPadding = PaddingValues(),
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}
