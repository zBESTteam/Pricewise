package com.example.pricewise.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OrangeStart,
    secondary = AccentPurple,
    tertiary = OrangeEnd,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = SurfacePrimary,
    onSecondary = SurfacePrimary,
    onBackground = SurfacePrimary,
    onSurface = SurfacePrimary,
    outline = CardOutline
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeStart,
    secondary = AccentPurple,
    tertiary = OrangeEnd,
    background = SurfaceMuted,
    surface = SurfacePrimary,
    onPrimary = SurfacePrimary,
    onSecondary = SurfacePrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = CardOutline
)

@Composable
fun PricewiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
