package com.pricewise.feature.home.impl.presentation.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeLoadingDefaults
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes

@Composable
internal fun LoadingFeedSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeDimens.SectionContentSpacing),
    ) {
        LoadingPopularQueriesSection(
            modifier = Modifier,
        )
        LoadingRecommendationsSection(
            modifier = Modifier,
        )
    }
}

@Composable
private fun LoadingPopularQueriesSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeDimens.SectionContentSpacing),
    ) {
        QuickActionCarouselSkeleton(
            modifier = Modifier,
        )
        LoadingSectionTitlePlaceholder(
            width = HomeDimens.LoadingPopularQueriesTitleWidth,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HomeDimens.SmallSpacing),
            contentPadding = PaddingValues(horizontal = HomeDimens.ScreenHorizontalPadding),
        ) {
            itemsIndexed(HomeLoadingDefaults.PopularQueryPlaceholderWidths) { index, width ->
                PlaceholderBox(
                    modifier = Modifier
                        .width(width)
                        .height(HomeDimens.LoadingChipHeight),
                    shape = HomeShapes.Chip,
                    delayMillis = index * HomeLoadingDefaults.PopularQueriesChipDelayStepMillis,
                    useShimmer = true,
                )
            }
        }
    }
}

@Composable
private fun LoadingRecommendationsSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeDimens.SectionContentSpacing),
    ) {
        LoadingSectionTitlePlaceholder(
            width = HomeDimens.LoadingRecommendationsTitleWidth,
        )
        Column(
            modifier = Modifier.padding(horizontal = HomeDimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(HomeDimens.SectionContentSpacing),
        ) {
            repeat(HomeLoadingDefaults.RecommendationSkeletonCount) { index ->
                LoadingProductCard(
                    modifier = Modifier,
                    delayMillis = index * HomeLoadingDefaults.ShimmerDelayStepMillis,
                )
            }
        }
    }
}

@Composable
private fun LoadingSectionTitlePlaceholder(
    width: Dp,
) {
    PlaceholderBox(
        modifier = Modifier
            .padding(horizontal = HomeDimens.ScreenHorizontalPadding)
            .width(width)
            .height(HomeDimens.LoadingTitleHeight),
        shape = HomeShapes.Placeholder,
        delayMillis = HomeLoadingDefaults.NoDelayMillis,
        useShimmer = true,
    )
}
