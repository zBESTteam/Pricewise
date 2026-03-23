package com.pricewise.feature.home.impl.data.mapper

import com.pricewise.core.network.ApiConfig
import com.pricewise.core.network.dto.BannerDto
import com.pricewise.core.network.dto.MainResponseDto
import com.pricewise.core.network.dto.ProductDto
import com.pricewise.core.network.dto.TrendingResponseDto
import com.pricewise.feature.home.impl.domain.model.HomeFeed
import com.pricewise.feature.home.impl.domain.model.HomeMarketplace
import com.pricewise.feature.home.impl.domain.model.HomePopularQuery
import com.pricewise.feature.home.impl.domain.model.HomeProduct
import com.pricewise.feature.home.impl.domain.model.HomeQuickAction
import com.pricewise.feature.home.impl.domain.model.HomeQuickActionIconType
import java.util.Locale

class HomeDataToDomainMapper {

    fun map(
        main: MainResponseDto,
        trending: TrendingResponseDto,
    ): HomeFeed {
        return HomeFeed(
            quickActions = main.banners.orEmpty()
                .mapIndexedNotNull(::mapQuickAction)
                .take(MAX_BANNERS),
            popularQueries = trending.items.orEmpty()
                .mapNotNull { item ->
                    val query = item.query?.trim().orEmpty()
                    if (query.isEmpty()) null else HomePopularQuery(id = query, title = query)
                }
                .take(MAX_QUERIES),
            products = main.recommendations.orEmpty()
                .mapIndexedNotNull(::mapProduct),
        )
    }

    private fun mapQuickAction(
        index: Int,
        item: BannerDto,
    ): HomeQuickAction? {
        val imageUrl = item.imageUrl.orImageFallback() ?: return null
        val id = item.id.asStableId().ifBlank { "banner_$index" }
        val title = item.title?.trim().orEmpty().ifBlank { "Banner ${index + 1}" }
        return HomeQuickAction(
            id = id,
            title = title,
            imageUrl = imageUrl,
            iconType = resolveQuickActionIconType(title),
        )
    }

    private fun mapProduct(
        index: Int,
        item: ProductDto,
    ): HomeProduct? {
        val id = item.id.asStableId().ifBlank {
            item.productUrl.asStableId().ifBlank { "product_$index" }
        }
        val title = item.title?.trim().orEmpty()
        val price = item.price
        if (id.isEmpty() || title.isEmpty() || price == null) return null

        return HomeProduct(
            id = id,
            title = title,
            price = price,
            isFavorite = item.isFavorite == true,
            thumbnailUrl = item.thumbnailUrl
                .orImageFallback()
                ?: item.imageUrl.orImageFallback()
                ?: item.image.orImageFallback(),
            productUrl = item.productUrl,
            marketplace = mapMarketplace(item),
        )
    }

    private fun mapMarketplace(
        item: ProductDto,
    ): HomeMarketplace {
        val merchant = item.merchant
        val merchantName = merchant?.name?.trim().orEmpty()
            .ifBlank { item.merchantName?.trim().orEmpty() }
            .ifBlank { item.source?.trim().orEmpty() }
            .ifBlank { DEFAULT_MARKETPLACE_NAME }

        return HomeMarketplace(
            name = merchantName.ensureMarketplaceDomain(),
            logoUrl = merchant?.logoUrl.orImageFallback()
                ?: item.merchantLogoUrl.orImageFallback()
                ?: item.logoUrl.orImageFallback(),
        )
    }

    private fun Any?.asStableId(): String {
        return when (this) {
            null -> ""
            is Number -> toLong().toString()
            else -> toString()
        }
    }

    private fun String?.orImageFallback(): String? {
        val value = this?.trim().orEmpty()
        if (value.isBlank()) return null
        return when {
            value.startsWith("//") -> "https:$value"
            value.startsWith("http://") || value.startsWith("https://") -> value
            value.startsWith("/") -> "${ApiConfig.BASE_URL}$value"
            else -> "${ApiConfig.BASE_URL}/${value.removePrefix("/")}"
        }
    }

    private fun String.ensureMarketplaceDomain(): String {
        return if (contains(".")) this else "$this.ru"
    }

    private fun resolveQuickActionIconType(
        title: String,
    ): HomeQuickActionIconType {
        val normalizedTitle = title.lowercase(Locale.ROOT)
        return when {
            "настрой" in normalizedTitle -> HomeQuickActionIconType.SearchSettings
            "ии" in normalizedTitle -> HomeQuickActionIconType.AiRecommendations
            "избран" in normalizedTitle -> HomeQuickActionIconType.Favorites
            else -> HomeQuickActionIconType.SearchGuide
        }
    }

    private companion object {
        const val MAX_QUERIES = 10
        const val MAX_BANNERS = 4
        const val DEFAULT_MARKETPLACE_NAME = "ozon.ru"
    }
}
