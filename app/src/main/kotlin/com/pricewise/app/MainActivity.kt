package com.pricewise.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pricewise.feature.auth.impl.presentation.auth.AuthorizationScreen
import com.pricewise.feature.auth.impl.presentation.auth.RegistrationScreen
import com.pricewise.feature.home.impl.presentation.ui.MainScreen
import com.pricewise.feature.home.impl.ui.theme.PriceWiseComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var toRegisterScreen = mutableStateOf(false)
        setContent {
            PriceWiseComposeTheme {
                Surface {
                    val context = LocalContext.current
                    if (!toRegisterScreen.value) {
                        AuthorizationScreen(
                            { toRegisterScreen.value = true
                                Toast.makeText(context, "toReg", Toast.LENGTH_SHORT).show()
                            },
                            onNavigateToMain = {},
                        )
                    } else {
                        RegistrationScreen(
                            {toRegisterScreen.value = false},
                            onNavigateToMain = {},
                        )
                    }
                }
            }
        }
    }
}