package com.pricewise.feature.home.impl.presentation.mapper

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pricewise.feature.home.impl.domain.model.HomeFeed
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
                    background = Brush.linearGradient(quickActionGradient),
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

    private companion object {
        const val SHORT_NAME_LENGTH = 2
        val quickActionGradient = listOf(Color(0xFFFFAB35), Color(0xFFFF2424))
        val rubleFormatter: NumberFormat =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))
    }
}
