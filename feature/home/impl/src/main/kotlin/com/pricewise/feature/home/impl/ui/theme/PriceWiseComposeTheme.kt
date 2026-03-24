package com.pricewise.feature.home.impl.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pricewise.core.ui.R as CoreUiR

private val PriceWiseFontFamily = FontFamily(
    Font(resId = CoreUiR.font.inter_regular, weight = FontWeight.Normal),
    Font(resId = CoreUiR.font.inter_medium, weight = FontWeight.Medium),
    Font(resId = CoreUiR.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(resId = CoreUiR.font.inter_bold, weight = FontWeight.Bold),
)

private val PriceWiseColorScheme = lightColorScheme(
    primary = Color(0xFFFF7A1A),
    onPrimary = Color.White,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF171717),
    onSurface = Color(0xFF171717),
)

private val PriceWiseTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = PriceWiseFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.sp,
    ),
)

@Composable
fun PriceWiseComposeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = PriceWiseColorScheme,
        typography = PriceWiseTypography,
        content = content,
    )
}
