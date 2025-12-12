package com.vkedu.pricewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.vkedu.pricewise.elements.EmailInputField
import com.vkedu.pricewise.screens.RegistrationScreen
import com.vkedu.pricewise.theme.PricewiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        enableEdgeToEdge()
        setContent {
            PricewiseTheme {
                PricewiseApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricewiseApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppDestinations.MAIN) } //If authorised

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(
                            topStart = 35.dp,
                            topEnd = 35.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                    containerColor = colorResource(R.color.bar_color)
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppDestinations.MAIN,
                        onClick = { currentScreen = AppDestinations.MAIN },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                        icon = {
                            Icon(
                                painter = if (currentScreen == AppDestinations.MAIN) {
                                    painterResource(id = AppDestinations.MAIN.selectedIcon)
                                } else painterResource(id = AppDestinations.MAIN.defaultIcon),
                                contentDescription = stringResource(id = AppDestinations.MAIN.contentDescriptionId),
                                tint = Color.Unspecified
                            )
                        },
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppDestinations.FAVORITES,
                        onClick = { currentScreen = AppDestinations.FAVORITES },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                        icon = {
                            Icon(
                                painter = if (currentScreen == AppDestinations.FAVORITES) {
                                    painterResource(id = AppDestinations.FAVORITES.selectedIcon)
                                } else painterResource(id = AppDestinations.FAVORITES.defaultIcon),
                                contentDescription = stringResource(id = AppDestinations.FAVORITES.contentDescriptionId),
                                tint = Color.Unspecified
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppDestinations.PROFILE,
                        onClick = { currentScreen = AppDestinations.PROFILE },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                        icon = {
                            Icon(
                                painter = if (currentScreen == AppDestinations.PROFILE) {
                                    painterResource(id = AppDestinations.PROFILE.selectedIcon)
                                } else painterResource(id = AppDestinations.PROFILE.defaultIcon),
                                contentDescription = stringResource(id = AppDestinations.PROFILE.contentDescriptionId),
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            },
            content = { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (currentScreen) {
                        AppDestinations.MAIN -> RegistrationScreen()
                        AppDestinations.FAVORITES -> Text("Favorite")
                        AppDestinations.PROFILE -> Text("Profile")
                    }
                }
            }
        )
    }
}