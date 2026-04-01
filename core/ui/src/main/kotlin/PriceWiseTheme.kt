import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val startGradient: Color,
    val endGradient: Color,
    val middleGradient: Color,
    val midDark: Color,
    val secondaryText: Color,
    val buttonColor: Color,
    val disabledFilterButtonColor: Color,
    val iconsColor: Color,
    val disabledFilterButtonTextColor: Color,
    val cardBackgroundColor: Color,
    val lightGray: Color,
    val handleColor: Color,
    val primaryText: Color,
    val thumbnailPlaceholder: Color,
    val iconTint: Color,
    val quickActionBorder: Color
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors> {
    error("No custom colors provided")
}

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF682D),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF5F6368),
    background = Color(0xFFFEFEFE),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F),

)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF682D),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF9AA0A6),
    background = Color(0xFF171717),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
)

private val LightCustomColors = CustomColors(
    startGradient = Color(0xFFFFAB35),
    endGradient = Color(0xFFFF2424),
    middleGradient = Color(0xFFFF682D),
    midDark = Color(0xFF232323),
    buttonColor = Color(0xFFF8F8F8),
    disabledFilterButtonColor = Color(0xFFF8F8F8),
    iconsColor = Color(0xFF727272),
    disabledFilterButtonTextColor = Color(0xFF727272),
    cardBackgroundColor = Color(0xFFFCFCFC),
    lightGray = Color(0xFFCCCCCC),
    handleColor = Color(0xFF272727),
    primaryText = Color(0xFF000000),
    secondaryText = Color(0xFF8D9094),
    thumbnailPlaceholder = Color(0xFFF5F5F5),
    iconTint = Color(0xFF292929),
    quickActionBorder = Color(0xFF2B2B2B)
)

private val DarkCustomColors = CustomColors(
    startGradient = Color(0xFFFFAB35),
    endGradient = Color(0xFFFF2424),
    middleGradient = Color(0xFFFF682D),
    midDark = Color(0xFFE0E0E0),
    secondaryText = Color(0xFFB0B0B0),
    buttonColor = Color(0xFF2C2C2C),
    disabledFilterButtonColor = Color(0xFF2C2C2C),
    iconsColor = Color(0xFFB0B0B0),
    disabledFilterButtonTextColor = Color(0xFF9E9E9E),
    cardBackgroundColor = Color(0xFF2C2C2C),
    lightGray = Color(0xFF3C3C3C),
    handleColor = Color(0xFFE0E0E0),
    primaryText = Color(0xFFFFFFFF),
    thumbnailPlaceholder = Color(0xFF3A3A3A),
    iconTint = Color(0xFF9A9A9A),
    quickActionBorder = Color(0xFFB0B0B0),
)

@Composable
fun PriceWiseTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            CompositionLocalProvider(LocalCustomColors provides customColors) {
                content()
            }
        }
    )
}