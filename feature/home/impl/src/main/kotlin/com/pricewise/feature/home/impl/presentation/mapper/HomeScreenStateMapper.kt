package com.pricewise.feature.home.impl.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ManageSearch
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.model.HomeProductThumbnailStyle
import com.pricewise.feature.home.impl.domain.model.HomeQuickActionIconType
import com.pricewise.feature.home.impl.presentation.ui.MainScreenState
import com.pricewise.feature.home.impl.presentation.ui.MarketplaceUiModel
import com.pricewise.feature.home.impl.presentation.ui.PopularQueryUiModel
import com.pricewise.feature.home.impl.presentation.ui.ProductThumbnailStyle
import com.pricewise.feature.home.impl.presentation.ui.ProductUiModel
import com.pricewise.feature.home.impl.presentation.ui.QuickActionUiModel
import com.pricewise.feature.home.impl.presentation.viewmodel.MainScreenUserInput
import javax.inject.Inject

class HomeScreenStateMapper @Inject constructor() {

    fun getScreenState(
        homeFeed: HomeFeed,
        userInput: MainScreenUserInput,
        isLoading: Boolean,
    ): MainScreenState {
        return MainScreenState(
            searchQuery = userInput.searchQuery,
            isLoading = isLoading,
            quickActions = homeFeed.quickActions.map { item ->
                QuickActionUiModel(
                    id = item.id,
                    title = item.title,
                    imageUrl = item.imageUrl,
                    icon = when (item.iconType) {
                        HomeQuickActionIconType.SearchGuide -> Icons.AutoMirrored.Outlined.ManageSearch
                        HomeQuickActionIconType.SearchSettings -> Icons.Outlined.Tune
                        HomeQuickActionIconType.AiRecommendations -> Icons.Outlined.AutoAwesome
                        HomeQuickActionIconType.Favorites -> Icons.Outlined.FavoriteBorder
                    },
                    background = Brush.linearGradient(item.gradientColors.map(::Color)),
                )
            },
            popularQueries = homeFeed.popularQueries.map { query ->
                PopularQueryUiModel(
                    id = query.id,
                    title = query.title,
                )
            },
            products = homeFeed.products.map { product ->
                ProductUiModel(
                    id = product.id,
                    title = product.title,
                    price = product.price,
                    isFavorite = product.isFavorite,
                    thumbnailUrl = product.thumbnailUrl,
                    productUrl = product.productUrl,
                    marketplace = MarketplaceUiModel(
                        name = product.marketplace.name,
                        shortName = product.marketplace.shortName,
                        logoUrl = product.marketplace.logoUrl,
                        badgeBrush = Brush.linearGradient(product.marketplace.badgeColors.map(::Color)),
                    ),
                    thumbnailStyle = when (product.thumbnailStyle) {
                        HomeProductThumbnailStyle.Phone -> ProductThumbnailStyle.Phone
                        HomeProductThumbnailStyle.Keyboard -> ProductThumbnailStyle.Keyboard
                    },
                    thumbnailBackground = Brush.linearGradient(product.thumbnailColors.map(::Color)),
                )
            },
        )
    }
}
