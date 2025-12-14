package com.vkedu.pricewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
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
    val navController = rememberNavController()

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) },
            content = { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BottomNavGraph(navController = navController)
                }
            }
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = AppScreen.entries.toTypedArray()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: AppScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { destination ->
            destination.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
        icon = {
            Icon(
                painter = if (currentDestination?.hierarchy?.any { destination ->
                        destination.route == screen.route
                    } == true) {
                    painterResource(id = screen.selectedIcon)
                } else painterResource(id = screen.defaultIcon),
                contentDescription = stringResource(id = screen.contentDescriptionId),
                tint = Color.Unspecified
            )
        },
    )
}