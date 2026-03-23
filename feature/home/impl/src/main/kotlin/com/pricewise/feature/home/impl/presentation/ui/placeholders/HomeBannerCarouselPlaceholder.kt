package com.pricewise.feature.home.impl.presentation.ui.placeholders

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.impl.presentation.ui.HomeColors
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeLoadingDefaults
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes

@Composable
internal fun QuickActionCarouselSkeleton(
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDimens.SmallSpacing),
        contentPadding = PaddingValues(horizontal = HomeDimens.ScreenHorizontalPadding),
    ) {
        items(count = HomeLoadingDefaults.BannerSkeletonCount) { index ->
            Box(
                modifier = Modifier
                    .size(HomeDimens.QuickActionCardSize)
                    .border(
                        width = HomeDimens.BannerBorderWidth,
                        color = HomeColors.QuickActionBorder,
                        shape = HomeShapes.QuickAction,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                PlaceholderBox(
                    modifier = Modifier.size(HomeDimens.QuickActionImageSize),
                    shape = HomeShapes.QuickAction,
                    delayMillis = index * HomeLoadingDefaults.ShimmerDelayStepMillis,
                    useShimmer = true,
                )
            }
        }
    }
}
