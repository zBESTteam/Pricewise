package com.pricewise.feature.home.impl.presentation.ui.components

import LocalCustomColors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import com.pricewise.feature.home.impl.presentation.ui.HomeColors
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeLoadingDefaults
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes
import com.pricewise.feature.home.impl.presentation.ui.QuickActionUiModel
import com.pricewise.feature.home.impl.presentation.ui.RemoteImage
import com.pricewise.feature.home.impl.presentation.ui.placeholders.PlaceholderBox

@Composable
internal fun QuickActionCarousel(
    actions: List<QuickActionUiModel>,
    onActionClick: (String) -> Unit,
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDimens.SmallSpacing),
        contentPadding = PaddingValues(horizontal = HomeDimens.ScreenHorizontalPadding),
    ) {
        items(actions, key = { action -> action.id }) { action ->
            QuickActionCard(
                action = action,
                onClick = { onActionClick(action.id) },
                modifier = Modifier,
            )
        }
    }
}

@Composable
internal fun QuickActionCard(
    action: QuickActionUiModel,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .size(HomeDimens.QuickActionCardSize)
            .border(
                width = HomeDimens.BannerBorderWidth,
                color = LocalCustomColors.current.quickActionBorder,
                shape = HomeShapes.QuickAction,
            ),
        shape = HomeShapes.QuickAction,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = HomeDimens.ZeroElevation),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(HomeDimens.QuickActionImageSize)
                    .background(action.background, HomeShapes.QuickAction),
                    contentAlignment = Alignment.Center,
            ) {
                if (action.imageUrl == null) {
                    PlaceholderBox(
                        modifier = Modifier.fillMaxSize(),
                        shape = HomeShapes.QuickAction,
                        delayMillis = HomeLoadingDefaults.NoDelayMillis,
                        useShimmer = false,
                    )
                } else {
                    RemoteImage(
                        imageUrl = action.imageUrl,
                        contentDescription = action.title,
                        modifier = Modifier.fillMaxSize(),
                        shape = HomeShapes.QuickAction,
                        contentScale = ContentScale.Crop,
                        requestSize = DpSize(
                            width = HomeDimens.QuickActionImageSize,
                            height = HomeDimens.QuickActionImageSize,
                        ),
                    )
                }
            }
        }
    }
}
