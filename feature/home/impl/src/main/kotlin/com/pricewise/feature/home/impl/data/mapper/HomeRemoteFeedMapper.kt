package com.pricewise.feature.home.impl.data.mapper

import com.pricewise.core.network.ApiConfig
import com.pricewise.core.network.dto.MainResponseDto
import com.pricewise.core.network.dto.ProductDto
import com.pricewise.core.network.dto.SearchResponseDto
import com.pricewise.core.network.dto.TrendingResponseDto
import com.pricewise.feature.home.impl.data.model.dto.HomeFeedDto
import com.pricewise.feature.home.impl.data.model.dto.MarketplaceDto
import com.pricewise.feature.home.impl.data.model.dto.PopularQueryDto
import com.pricewise.feature.home.impl.data.model.dto.ProductDto as HomeProductDto
import com.pricewise.feature.home.impl.data.model.dto.ProductThumbnailStyleDto
import com.pricewise.feature.home.impl.data.model.dto.QuickActionDto
import com.pricewise.feature.home.impl.data.model.dto.QuickActionIconTypeDto
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class HomeRemoteFeedMapper @Inject constructor() {

    fun map(
        main: MainResponseDto,
        trending: TrendingResponseDto,
    ): HomeFeedDto {
        return HomeFeedDto(
            searchQuery = "",
            quickActions = main.banners.orEmpty()
                .mapIndexedNotNull(::mapQuickAction)
                .take(MAX_BANNERS),
            popularQueries = trending.items.orEmpty()
                .mapNotNull { item ->
                    val query = item.query?.trim().orEmpty()
                    if (query.isEmpty()) null else PopularQueryDto(id = query, title = query)
                }
                .take(MAX_QUERIES),
            products = main.recommendations.orEmpty()
                .mapIndexedNotNull(::mapProduct),
        )
    }

    fun mapSearch(
        response: SearchResponseDto,
        currentFeed: HomeFeedDto,
        query: String,
    ): HomeFeedDto {
        return currentFeed.copy(
            searchQuery = query,
            products = response.items.orEmpty().mapIndexedNotNull(::mapProduct),
        )
    }

    private fun mapProduct(
        index: Int,
        item: ProductDto,
    ): HomeProductDto? {
        val id = item.id.asStableId().ifBlank {
            item.productUrl.asStableId().ifBlank { "product_$index" }
        }
        val title = item.title?.trim().orEmpty()
        if (id.isEmpty() || title.isEmpty()) return null

        return HomeProductDto(
            id = id,
            title = title,
            price = item.price.formatRubles(),
            isFavorite = item.isFavorite == true,
            thumbnailUrl = item.thumbnailUrl
                .orImageFallback()
                ?: item.imageUrl.orImageFallback()
                ?: item.image.orImageFallback(),
            productUrl = item.productUrl,
            marketplace = mapMarketplace(item),
            thumbnailStyle = resolveThumbnailStyle(title),
            thumbnailColors = productPalettes[index % productPalettes.size],
        )
    }

    private fun mapQuickAction(
        index: Int,
        item: com.pricewise.core.network.dto.BannerDto,
    ): QuickActionDto? {
        val imageUrl = item.imageUrl.orImageFallback() ?: return null
        val id = item.id.asStableId().ifBlank { "banner_$index" }
        val title = item.title?.trim().orEmpty()
            .ifBlank { "Banner ${index + 1}" }
        val iconType = resolveQuickActionIconType(title)
        return QuickActionDto(
            id = id,
            title = title,
            imageUrl = imageUrl,
            iconType = iconType,
            gradientColors = gradientFor(iconType),
        )
    }

    private fun mapMarketplace(item: ProductDto): MarketplaceDto {
        val merchant = item.merchant
        val merchantName = merchant?.name?.trim().orEmpty()
            .ifBlank { item.merchantName?.trim().orEmpty() }
            .ifBlank { item.source?.trim().orEmpty() }
            .ifBlank { DEFAULT_MARKETPLACE_NAME }
        val normalizedName = merchantName.ensureMarketplaceDomain()

        return MarketplaceDto(
            name = normalizedName,
            shortName = normalizedName.take(SHORT_NAME_LENGTH).lowercase(Locale.ROOT),
            logoUrl = merchant?.logoUrl.orImageFallback()
                ?: item.merchantLogoUrl.orImageFallback()
                ?: item.logoUrl.orImageFallback(),
            badgeColors = marketplacePalette(normalizedName),
        )
    }

    private fun resolveThumbnailStyle(title: String): ProductThumbnailStyleDto {
        val normalizedTitle = title.lowercase(Locale.ROOT)
        return if (KEYBOARD_HINTS.any(normalizedTitle::contains)) {
            ProductThumbnailStyleDto.Keyboard
        } else {
            ProductThumbnailStyleDto.Phone
        }
    }

    private fun marketplacePalette(name: String): List<Long> {
        val normalizedName = name.lowercase(Locale.ROOT)
        return when {
            "wildberries" in normalizedName || normalizedName.startsWith("wb") -> listOf(0xFF9B00FF, 0xFFFF2EA6)
            else -> listOf(0xFF4A00FF, 0xFFFF2EA6)
        }
    }

    private fun Long?.formatRubles(): String {
        val safeValue = this ?: 0L
        val formattedNumber = rubleFormatter.format(safeValue)
        return "$formattedNumber ₽"
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

    private fun resolveQuickActionIconType(title: String): QuickActionIconTypeDto {
        val normalizedTitle = title.lowercase(Locale.ROOT)
        return when {
            "настрой" in normalizedTitle -> QuickActionIconTypeDto.SearchSettings
            "ии" in normalizedTitle -> QuickActionIconTypeDto.AiRecommendations
            "избран" in normalizedTitle -> QuickActionIconTypeDto.Favorites
            else -> QuickActionIconTypeDto.SearchGuide
        }
    }

    private fun gradientFor(iconType: QuickActionIconTypeDto): List<Long> {
        return when (iconType) {
            QuickActionIconTypeDto.SearchGuide -> listOf(0xFF585858, 0xFF121212)
            QuickActionIconTypeDto.SearchSettings -> listOf(0xFFFFAB35, 0xFFE62727)
            QuickActionIconTypeDto.AiRecommendations -> listOf(0xFFFFDA74, 0xFFCE6A08)
            QuickActionIconTypeDto.Favorites -> listOf(0xFFFF5959, 0xFF8B2215)
        }
    }

    private companion object {
        private const val MAX_QUERIES = 10
        private const val MAX_BANNERS = 4
        private const val SHORT_NAME_LENGTH = 2
        private const val DEFAULT_MARKETPLACE_NAME = "ozon.ru"
        private val KEYBOARD_HINTS = listOf("keyboard", "клав", "nuphy")
        private val rubleFormatter = NumberFormat.getIntegerInstance(Locale("ru", "RU"))

        private val productPalettes = listOf(
            listOf(0xFFF8F1E5, 0xFFD8D3CB),
            listOf(0xFF8AD7C9, 0xFFE5CE71),
            listOf(0xFFF0DCC8, 0xFF3F1D12),
            listOf(0xFFF3F0EA, 0xFFD8D1C8),
        )
    }
}
