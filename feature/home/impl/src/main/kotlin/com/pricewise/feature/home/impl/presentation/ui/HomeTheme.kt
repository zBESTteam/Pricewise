package com.pricewise.feature.home.impl.presentation.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pricewise.feature.home.impl.R

internal object HomeColors {
    val ScreenBackground = Color(0xFFFEFEFE)
    val HeaderGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFAB35), Color(0xFFFF2424)),
    )
    val QuickActionBorder = Color(0xFF2B2B2B)
    val SectionTitle = Color(0xFF232323)
    val PrimaryText = Color(0xFF000000)
    val SecondaryText = Color(0xFF8D9094)
    val ChipBackground = Color(0xFFF8F8F8)
    val PlaceholderBase = Color(0xFFEDEDED)
    val PlaceholderHighlight = Color(0xFFFFFFFF)
    val ThumbnailFallbackBackground = Color(0xFFF5F5F5)
}

internal object HomeShapes {
    val Header = RoundedCornerShape(
        bottomStart = HomeDimens.HeaderCornerRadius,
        bottomEnd = HomeDimens.HeaderCornerRadius,
    )
    val SearchField = RoundedCornerShape(HomeDimens.CommonCornerRadius)
    val ProductCard = RoundedCornerShape(HomeDimens.CommonCornerRadius)
    val ProductThumbnail = RoundedCornerShape(HomeDimens.CommonCornerRadius)
    val QuickAction = RoundedCornerShape(HomeDimens.QuickActionCornerRadius)
    val Chip = RoundedCornerShape(HomeDimens.CommonCornerRadius)
    val Placeholder = RoundedCornerShape(HomeDimens.PlaceholderCornerRadius)
}

internal object HomeTextStyles {
    private val priceWiseFontFamily = FontFamily(
        Font(resId = R.font.inter_regular, weight = FontWeight.Normal),
        Font(resId = R.font.inter_regular, weight = FontWeight.Medium),
        Font(resId = R.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(resId = R.font.inter_semibold, weight = FontWeight.Bold),
    )

    val SearchField
        @Composable get() = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.Medium,
        )

    val Chip
        @Composable get() = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.SemiBold,
        )

    val SectionTitle
        @Composable get() = TextStyle(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
        )

    val ProductTitle
        @Composable get() = TextStyle(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = HomeColors.PrimaryText,
        )

    val ProductPrice
        @Composable get() = TextStyle(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = HomeColors.PrimaryText,
        )

    val Marketplace
        @Composable get() = TextStyle(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = HomeColors.PrimaryText,
        )

    val MarketplaceBadge
        @Composable get() = MaterialTheme.typography.labelSmall.copy(
            fontFamily = priceWiseFontFamily,
            fontWeight = FontWeight.Bold,
        )
}

internal object HomeLoadingDefaults {
    const val NoDelayMillis = 0
    const val ShimmerDurationMillis = 850
    const val ShimmerDelayStepMillis = 120
    const val ProductTextDelayStepMillis = 70
    const val PopularQueriesChipDelayStepMillis = 90
    const val BannerSkeletonCount = 4
    const val PopularQuerySkeletonCount = 4
    const val RecommendationSkeletonCount = 3
    const val ShimmerStartOffset = -420f
    const val ShimmerTargetOffset = 920f
    const val ShimmerHighlightSize = 320f
    val PopularQueryPlaceholderWidths = listOf(84.dp, 94.dp, 104.dp, 114.dp)
}
