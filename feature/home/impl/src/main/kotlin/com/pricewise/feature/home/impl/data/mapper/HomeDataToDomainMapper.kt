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

        val marketplace = mapMarketplace(item)
        return HomeProduct(
            id = id,
            source = item.source?.trim().orEmpty().ifBlank { marketplace.name },
            title = title,
            price = price,
            isFavorite = item.isFavorite == true,
            thumbnailUrl = item.thumbnailUrl
                .orImageFallback()
                ?: item.imageUrl.orImageFallback()
                ?: item.image.orImageFallback(),
            productUrl = item.productUrl,
            marketplace = marketplace,
        )
    }

    private fun mapMarketplace(
        item: ProductDto,
    ): HomeMarketplace {
        val merchant = item.merchant
        val merchantName = merchant?.name?.trim().orEmpty()
            .ifBlank { item.merchantName?.trim().orEmpty() }
            .ifBlank { item.source?.trim().orEmpty() }

        return HomeMarketplace(
            name = merchantName,
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

    private companion object {
        const val MAX_QUERIES = 10
        const val MAX_BANNERS = 4
    }
}
