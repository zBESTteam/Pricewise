package com.pricewise.feature.search.impl.data.mapper

import com.pricewise.core.network.dto.MerchantDto
import com.pricewise.core.network.dto.ProductDto
import com.pricewise.feature.search.api.domain.model.Merchant
import com.pricewise.feature.search.api.domain.model.Product
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlin.text.ifEmpty

@Singleton
class ProductMapper @Inject constructor() {

    fun parseItems(items: List<ProductDto>): List<Product> {
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

    private fun Any?.asStringId(): String {
        return when (this) {
            null -> ""
            is Number -> this.toLong().toString()
            else -> this.toString()
        }
    }
}