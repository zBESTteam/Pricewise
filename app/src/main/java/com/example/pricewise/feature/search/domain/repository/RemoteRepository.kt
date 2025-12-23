package com.example.pricewise.feature.search.domain.repository

import com.example.pricewise.core.network.NetworkModule
import com.example.pricewise.core.network.PricewiseApi
import com.example.pricewise.core.network.dto.MerchantDto
import com.example.pricewise.core.network.dto.ProductDto
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.search.data.ApiSearch
import com.example.pricewise.feature.search.data.SearchResult

class RemoteRepository(
    private val api: PricewiseApi = NetworkModule.api,
    private val sources: List<String> = DEFAULT_SOURCES,
) : ApiSearch {

    override suspend fun search(
        query: String,
        limit: Int,
        offset: Int
    ): SearchResult {
        val response = api.search(
            query = query,
            limit = limit,
            offset = offset,
            sources = sources.takeIf { it.isNotEmpty() }?.joinToString(","),
        )
        val items = parseItems(response.items.orEmpty())
        return SearchResult(
            items = items,
            hasMore = response.hasMore ?: false,
            checkedSources = response.checkedSources,
            totalSources = response.totalSources,
            pendingSources = response.pendingSources.orEmpty(),
        )
    }

    private fun parseItems(items: List<ProductDto>): List<ProductRecommendation> {
        return items.mapNotNull { item ->
            val id = item.id.asStringId()
            val title = item.title?.trim().orEmpty()
            if (id.isEmpty() || title.isEmpty()) {
                return@mapNotNull null
            }
            val merchant = parseMerchant(item)
            ProductRecommendation(
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
        val id = item.merchantId?.trim().orEmpty().ifBlank { source.ifBlank { name } }
        return Merchant(id = id, name = name, logoUrl = logoUrl)
    }

    private fun parseMerchantObject(obj: MerchantDto): Merchant {
        val id = obj.id.asStringId()
        val name = obj.name?.trim().orEmpty()
        val logoUrl = obj.logoUrl?.trim().orEmpty()
        val fallbackId = id.ifEmpty { name }
        return Merchant(id = fallbackId, name = name, logoUrl = logoUrl)
    }

    private fun resolveImageUrl(item: ProductDto): String {
        return item.thumbnailUrl?.trim().orEmpty()
            .ifBlank { item.imageUrl?.trim().orEmpty() }
            .ifBlank { item.image?.trim().orEmpty() }
    }

    private companion object {
        val DEFAULT_SOURCES = listOf(
            "market.yandex.ru",
            "mvideo.ru",
            "citilink.ru",
            "eldorado.ru",
            "avito.ru",
            "cdek.shopping",
            "aliexpress.ru",
            "xcom-shop.ru",
        )
    }
}

private fun Any?.asStringId(): String {
    return when (this) {
        null -> ""
        is Number -> this.toLong().toString()
        else -> this.toString()
    }
}
