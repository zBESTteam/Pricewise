package com.pricewise.feature.home.impl.data.mapper

import com.pricewise.feature.home.impl.data.model.dto.HomeFeedDto
import com.pricewise.feature.home.impl.data.model.dto.MarketplaceDto
import com.pricewise.feature.home.impl.data.model.dto.PopularQueryDto
import com.pricewise.feature.home.impl.data.model.dto.ProductDto
import com.pricewise.feature.home.impl.data.model.dto.ProductThumbnailStyleDto
import com.pricewise.feature.home.impl.data.model.dto.QuickActionDto
import com.pricewise.feature.home.impl.data.model.dto.QuickActionIconTypeDto
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.model.HomeMarketplace
import com.pricewise.feature.home.impl.domain.model.HomePopularQuery
import com.pricewise.feature.home.impl.domain.model.HomeProduct
import com.pricewise.feature.home.impl.domain.model.HomeProductThumbnailStyle
import com.pricewise.feature.home.impl.domain.model.HomeQuickAction
import com.pricewise.feature.home.impl.domain.model.HomeQuickActionIconType
import javax.inject.Inject

class HomeFeedDomainMapper @Inject constructor() {

    fun map(feed: HomeFeedDto): HomeFeed {
        return HomeFeed(
            searchQuery = feed.searchQuery,
            quickActions = feed.quickActions.map(::mapQuickAction),
            popularQueries = feed.popularQueries.map(::mapPopularQuery),
            products = feed.products.map(::mapProduct),
        )
    }

    private fun mapQuickAction(item: QuickActionDto): HomeQuickAction {
        return HomeQuickAction(
            id = item.id,
            title = item.title,
            imageUrl = item.imageUrl,
            iconType = when (item.iconType) {
                QuickActionIconTypeDto.SearchGuide -> HomeQuickActionIconType.SearchGuide
                QuickActionIconTypeDto.SearchSettings -> HomeQuickActionIconType.SearchSettings
                QuickActionIconTypeDto.AiRecommendations -> HomeQuickActionIconType.AiRecommendations
                QuickActionIconTypeDto.Favorites -> HomeQuickActionIconType.Favorites
            },
            gradientColors = item.gradientColors,
        )
    }

    private fun mapPopularQuery(item: PopularQueryDto): HomePopularQuery {
        return HomePopularQuery(
            id = item.id,
            title = item.title,
        )
    }

    private fun mapProduct(item: ProductDto): HomeProduct {
        return HomeProduct(
            id = item.id,
            title = item.title,
            price = item.price,
            isFavorite = item.isFavorite,
            thumbnailUrl = item.thumbnailUrl,
            productUrl = item.productUrl,
            marketplace = mapMarketplace(item.marketplace),
            thumbnailStyle = when (item.thumbnailStyle) {
                ProductThumbnailStyleDto.Phone -> HomeProductThumbnailStyle.Phone
                ProductThumbnailStyleDto.Keyboard -> HomeProductThumbnailStyle.Keyboard
            },
            thumbnailColors = item.thumbnailColors,
        )
    }

    private fun mapMarketplace(item: MarketplaceDto): HomeMarketplace {
        return HomeMarketplace(
            name = item.name,
            shortName = item.shortName,
            logoUrl = item.logoUrl,
            badgeColors = item.badgeColors,
        )
    }
}
