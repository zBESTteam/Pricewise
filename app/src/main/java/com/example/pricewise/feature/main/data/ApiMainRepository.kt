package com.example.pricewise.feature.main.data

import com.example.pricewise.core.network.NetworkModule
import com.example.pricewise.core.network.dto.BannerDto
import com.example.pricewise.core.network.dto.ProductDto
import com.example.pricewise.core.network.dto.MerchantDto
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.PopularQuery
import com.example.pricewise.feature.main.domain.model.Product
import com.example.pricewise.feature.main.domain.model.PromoBanner
import com.example.pricewise.feature.main.domain.repository.MainRepository

class ApiMainRepository(
    private val api: com.example.pricewise.core.network.PricewiseApi = NetworkModule.api,
    private val defaultLimit: Int = 20,
) : MainRepository {

    private var cachedBanners: List<PromoBanner>? = null
    private var cachedRecommendations: List<Product>? = null

    override suspend fun loadBanners(): List<PromoBanner> {
        ensureMainLoaded()
        return cachedBanners.orEmpty()
    }

    override suspend fun loadPopularQueries(): List<PopularQuery> {
        val response = api.getTrending(limit = 10, days = 7)
        return response.items.orEmpty()
            .mapNotNull { it.query?.trim() }
            .filter { it.isNotEmpty() }
            .map { PopularQuery(id = it, query = it) }
    }

    override suspend fun loadRecommendations(): List<Product> {
        ensureMainLoaded()
        return cachedRecommendations.orEmpty()
    }

    private suspend fun ensureMainLoaded() {
        if (cachedBanners != null && cachedRecommendations != null) {
            return
        }
        val response = api.getMain(limit = defaultLimit, offset = 0)
        cachedBanners = parseBanners(response.banners.orEmpty())
        cachedRecommendations = parseRecommendations(response.recommendations.orEmpty())
    }

    private fun parseBanners(items: List<BannerDto>): List<PromoBanner> {
        return items.mapNotNull { item ->
            val id = item.id.asStringId()
            val title = item.title?.trim().orEmpty()
            val imageUrl = item.imageUrl?.trim().orEmpty()
            if (id.isEmpty() || title.isEmpty()) {
                null
            } else {
                PromoBanner(id = id, title = title, imageUrl = imageUrl)
            }
        }
    }

    private fun parseRecommendations(items: List<ProductDto>): List<Product> {
        return items.mapNotNull { item ->
            val id = item.id.asStringId()
            val title = item.title?.trim().orEmpty()
            if (id.isEmpty() || title.isEmpty()) {
                return@mapNotNull null
            }
            val merchant = parseMerchant(item)
            Product(
                id = id,
                title = title,
                price = item.price ?: 0L,
                merchant = merchant,
                thumbnailUrl = resolveImageUrl(item),
                isFavorite = item.isFavorite ?: false,
            )
        }
    }

    private fun parseMerchant(item: ProductDto): Merchant {
        val merchantObj = item.merchant
        if (merchantObj != null) {
            return parseMerchantObject(merchantObj)
        }
        val source = item.source?.trim().orEmpty()
        val name = item.merchantName?.trim().orEmpty().ifBlank { source }
        val logoUrl = item.merchantLogoUrl?.trim().orEmpty()
            .ifBlank { item.logoUrl?.trim().orEmpty() }
        val id = item.merchantId?.trim().orEmpty().ifBlank { name.ifBlank { source } }
        return Merchant(id = id, name = name, logoUrl = logoUrl)
    }

    private fun parseMerchantObject(obj: MerchantDto): Merchant {
        val id = obj.id.asStringId()
        val name = obj.name?.trim().orEmpty()
        val logoUrl = obj.logoUrl?.trim().orEmpty()
        val fallbackId = if (id.isNotEmpty()) id else name
        return Merchant(id = fallbackId, name = name, logoUrl = logoUrl)
    }

    private fun resolveImageUrl(item: ProductDto): String {
        return item.thumbnailUrl?.trim().orEmpty()
            .ifBlank { item.imageUrl?.trim().orEmpty() }
            .ifBlank { item.image?.trim().orEmpty() }
    }
}

private fun Any?.asStringId(): String {
    return when (this) {
        null -> ""
        is Number -> this.toLong().toString()
        else -> this.toString()
    }
}
