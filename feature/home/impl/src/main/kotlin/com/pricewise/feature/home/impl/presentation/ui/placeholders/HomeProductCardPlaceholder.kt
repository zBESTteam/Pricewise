package com.pricewise.feature.home.impl.presentation.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeLoadingDefaults
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes

@Composable
internal fun LoadingProductCard(
    modifier: Modifier,
    delayMillis: Int,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = HomeShapes.ProductCard,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = HomeDimens.ZeroElevation),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HomeDimens.MediumSpacing),
            horizontalArrangement = Arrangement.spacedBy(HomeDimens.MediumSpacing),
            verticalAlignment = Alignment.Top,
        ) {
            PlaceholderBox(
                modifier = Modifier
                    .width(HomeDimens.ProductThumbnailWidth)
                    .height(HomeDimens.ProductThumbnailHeight),
                shape = HomeShapes.ProductThumbnail,
                delayMillis = delayMillis,
                useShimmer = true,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(HomeDimens.LoadingCardTextSpacing),
            ) {
                PlaceholderBox(
                    modifier = Modifier
                        .width(HomeDimens.LoadingMarketplaceWidth)
                        .height(HomeDimens.LoadingMarketplaceHeight),
                    shape = HomeShapes.Placeholder,
                    delayMillis = delayMillis + HomeLoadingDefaults.ProductTextDelayStepMillis,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDimens.LoadingTextLineHeight),
                    shape = HomeShapes.Placeholder,
                    delayMillis = delayMillis + HomeLoadingDefaults.ProductTextDelayStepMillis * 2,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .width(HomeDimens.LoadingProductTitleWidth)
                        .height(HomeDimens.LoadingTextLineHeight),
                    shape = HomeShapes.Placeholder,
                    delayMillis = delayMillis + HomeLoadingDefaults.ProductTextDelayStepMillis * 3,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .width(HomeDimens.LoadingPriceWidth)
                        .height(HomeDimens.LoadingPriceHeight),
                    shape = HomeShapes.Placeholder,
                    delayMillis = delayMillis + HomeLoadingDefaults.ProductTextDelayStepMillis * 4,
                    useShimmer = true,
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(top = HomeDimens.LoadingFavoriteTopPadding)
                    .requiredSize(HomeDimens.TouchTargetSize),
            )
        }
    }
}
