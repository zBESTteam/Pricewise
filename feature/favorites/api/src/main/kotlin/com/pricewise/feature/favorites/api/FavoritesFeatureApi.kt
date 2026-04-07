package com.pricewise.feature.favorites.api

import kotlinx.coroutines.flow.SharedFlow

data class FavoriteMutationRequest(
	val externalId: String,
	val source: String,
	val title: String,
	val price: Long,
	val thumbnailUrl: String? = null,
	val productUrl: String? = null,
	val merchantLogoUrl: String? = null,
)

data class FavoriteMutationEvent(
	val externalId: String,
	val source: String,
	val isFavorite: Boolean,
)

interface FavoritesFeatureApi {
	val favoriteMutations: SharedFlow<FavoriteMutationEvent>

	suspend fun addToFavorites(request: FavoriteMutationRequest)

	suspend fun removeFromFavorites(externalId: String, source: String)
}
