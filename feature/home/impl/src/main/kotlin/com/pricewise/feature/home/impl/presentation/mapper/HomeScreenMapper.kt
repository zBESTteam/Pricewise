package com.pricewise.feature.home.impl.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ManageSearch
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.model.HomeQuickActionIconType
import com.pricewise.feature.home.impl.presentation.ui.HomeScreenState
import com.pricewise.feature.home.impl.presentation.ui.MarketplaceUiModel
import com.pricewise.feature.home.impl.presentation.ui.PopularQueryUiModel
import com.pricewise.feature.home.impl.presentation.ui.ProductUiModel
import com.pricewise.feature.home.impl.presentation.ui.QuickActionUiModel
import com.pricewise.feature.home.impl.presentation.viewmodel.HomeScreenUserInput
import java.text.NumberFormat
import java.util.Locale

class HomeScreenMapper {

    fun getScreenState(
        homeFeed: HomeFeed,
        userInput: HomeScreenUserInput,
    ): HomeScreenState {
        return HomeScreenState.Loaded(
            searchQuery = userInput.searchQuery,
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
                    background = Brush.linearGradient(gradientFor(item.iconType)),
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
                    price = rubleFormatter.format(product.price) + " ₽",
                    isFavorite = product.isFavorite || userInput.favoriteProductIds.contains(product.id),
                    thumbnailUrl = product.thumbnailUrl,
                    productUrl = product.productUrl,
                    marketplace = MarketplaceUiModel(
                        name = product.marketplace.name,
                        shortName = product.marketplace.name.take(SHORT_NAME_LENGTH).lowercase(Locale.ROOT),
                        logoUrl = product.marketplace.logoUrl,
                    ),
                )
            },
        )
    }

    fun getScreenState(
        throwable: Throwable,
    ): HomeScreenState {
        return HomeScreenState.Error(throwable = throwable)
    }

    private fun gradientFor(
        iconType: HomeQuickActionIconType,
    ): List<Color> {
        return when (iconType) {
            HomeQuickActionIconType.SearchGuide -> listOf(Color(0xFF585858), Color(0xFF121212))
            HomeQuickActionIconType.SearchSettings -> listOf(Color(0xFFFFAB35), Color(0xFFE62727))
            HomeQuickActionIconType.AiRecommendations -> listOf(Color(0xFFFFDA74), Color(0xFFCE6A08))
            HomeQuickActionIconType.Favorites -> listOf(Color(0xFFFF5959), Color(0xFF8B2215))
        }
    }

    private companion object {
        const val SHORT_NAME_LENGTH = 2
        val rubleFormatter: NumberFormat =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))
    }
}
