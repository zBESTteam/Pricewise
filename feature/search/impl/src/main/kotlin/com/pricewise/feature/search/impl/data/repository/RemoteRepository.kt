package com.pricewise.feature.search.impl.data.repository

import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.di.NetworkModule
import com.pricewise.core.network.di.NetworkModule.DEFAULT_SOURCES
import com.pricewise.core.network.dto.MerchantDto
import com.pricewise.core.network.dto.ProductDto
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.api.domain.model.Merchant
import com.pricewise.feature.search.api.domain.model.Product
import com.pricewise.feature.search.api.domain.model.SearchResult

class RemoteRepository(
    private val api: PriceWiseApi = NetworkModule.providePriceWiseApi(retrofit = NetworkModule.provideRetrofit(
        moshi = NetworkModule.provideMoshi(),
        okHttpClient = NetworkModule.provideOkHttpClient()
    )),
    private val sources: List<String> = DEFAULT_SOURCES,
) : SearchFeatureApi {

    override suspend fun search(
        query: String,
        limit: Int,
        offset: Int,
        perSource: Boolean,
        partial: Boolean,
        sort: String,
        priceMin: Long,
        priceMax: Long,
        delivery: String,
        onlyOriginal: Boolean,
        onlyNew: Boolean,
        onlyUsed: Boolean,
        marketplaceOnly: Boolean,
        offlineOnly: Boolean,
        playLaterOnly: Boolean
    ): SearchResult {
        val response = api.search(
            query = query,
            limit = limit,
            offset = offset,
            perSource = if (perSource) true else null,
            partial = if (partial) true else null,
            sources = sources.takeIf { it.isNotEmpty() }?.joinToString(","),
            sort = sort,
            priceMin = priceMin,
            priceMax = priceMax,
            delivery = delivery,
            onlyOriginal = onlyOriginal,
            onlyNew = onlyNew,
            onlyUsed = onlyUsed,
            marketplaceOnly = marketplaceOnly,
            offlineOnly = offlineOnly,
            payLaterOnly = playLaterOnly
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

    private fun parseItems(items: List<ProductDto>): List<Product> {
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
}

private fun Any?.asStringId(): String {
    return when (this) {
        null -> ""
        is Number -> this.toLong().toString()
        else -> this.toString()
    }
}