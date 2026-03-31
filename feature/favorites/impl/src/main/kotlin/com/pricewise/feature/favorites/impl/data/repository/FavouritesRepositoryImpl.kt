package com.pricewise.feature.favorites.impl.data.repository

import com.pricewise.core.auth.TokenManager
import com.pricewise.core.network.PriceWiseApi
import com.pricewise.core.network.dto.ApiStatusDto
import com.pricewise.core.network.dto.FavoriteCreateRequestDto
import com.pricewise.core.network.dto.FavoriteDto
import com.pricewise.feature.favorites.impl.domain.model.FavoriteCreate
import com.pricewise.feature.favorites.impl.domain.model.FavoriteItem
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val api: PriceWiseApi,
    private val tokenManager: TokenManager
) : FavoritesRepository {

    override suspend fun list(token: String): List<FavoriteItem> {
        val response = api.listFavorites(authorization = token.asBearer())
        return response.items.orEmpty().mapNotNull { it.toDomainOrNull() }
    }

    override suspend fun add(token: String, favorite: FavoriteCreate): FavoriteItem {
        val response = api.addFavorite(
            authorization = token.asBearer(),
            request = FavoriteCreateRequestDto(
                externalId = favorite.externalId,
                source = favorite.source,
                title = favorite.title,
                price = favorite.price,
                thumbnailUrl = favorite.thumbnailUrl.takeIf { it.isNotBlank() },
                productUrl = favorite.productUrl.takeIf { it.isNotBlank() },
                merchantLogoUrl = favorite.merchantLogoUrl.takeIf { it.isNotBlank() },
            ),
        )
        return response.toDomainOrNull() ?: throw IllegalStateException("Invalid favorite response")
    }

    override suspend fun remove(token: String, externalId: String, source: String): Boolean {
        val response = api.removeFavorite(
            authorization = token.asBearer(),
            externalId = externalId,
            source = source,
        )
        return response.isOk()
    }

    override suspend fun favoriteRecommendation(token: String, recommendationId: Long): FavoriteItem {
        val response = api.favoriteRecommendation(
            authorization = token.asBearer(),
            recommendationId = recommendationId,
        )
        return response.toDomainOrNull() ?: throw IllegalStateException("Invalid favorite response")
    }

    override suspend fun unfavoriteRecommendation(token: String, recommendationId: Long): Boolean {
        val response = api.unfavoriteRecommendation(
            authorization = token.asBearer(),
            recommendationId = recommendationId,
        )
        return response.isOk()
    }
}

private fun FavoriteDto.toDomainOrNull(): FavoriteItem? {
    val idValue = id ?: return null
    val extId = externalId?.trim().orEmpty()
    val sourceValue = source?.trim().orEmpty()
    val titleValue = title?.trim().orEmpty()
    if (extId.isEmpty() || sourceValue.isEmpty() || titleValue.isEmpty()) {
        return null
    }
    return FavoriteItem(
        id = idValue,
        externalId = extId,
        source = sourceValue,
        title = titleValue,
        price = price ?: 0L,
        thumbnailUrl = thumbnailUrl?.trim().orEmpty(),
        productUrl = productUrl?.trim().orEmpty(),
        merchantLogoUrl = merchantLogoUrl?.trim().orEmpty(),
    )
}

private fun ApiStatusDto.isOk(): Boolean {
    return status?.trim().equals("ok", ignoreCase = true)
}

private fun String.asBearer(): String {
    val trimmed = trim()
    return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
        trimmed
    } else {
        "Bearer $trimmed"
    }
}
