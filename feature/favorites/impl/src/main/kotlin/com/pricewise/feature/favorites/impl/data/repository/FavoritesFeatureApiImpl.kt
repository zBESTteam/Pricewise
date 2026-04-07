package com.pricewise.feature.favorites.impl.data.repository

import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoriteMutationEvent
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.favorites.impl.domain.model.FavoriteCreate
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FavoritesFeatureApiImpl @Inject constructor(
    private val repository: FavoritesRepository,
) : FavoritesFeatureApi {

    private val _favoriteMutations = MutableSharedFlow<FavoriteMutationEvent>(extraBufferCapacity = 64)
    override val favoriteMutations: SharedFlow<FavoriteMutationEvent> = _favoriteMutations.asSharedFlow()

    override suspend fun addToFavorites(request: FavoriteMutationRequest) {
        repository.add(
            favorite = FavoriteCreate(
                externalId = request.externalId,
                source = request.source,
                title = request.title,
                price = request.price,
                thumbnailUrl = request.thumbnailUrl.orEmpty(),
                productUrl = request.productUrl.orEmpty(),
                merchantLogoUrl = request.merchantLogoUrl.orEmpty(),
            ),
        )
        _favoriteMutations.tryEmit(
            FavoriteMutationEvent(
                externalId = request.externalId,
                source = request.source,
                isFavorite = true,
            ),
        )
    }

    override suspend fun removeFromFavorites(externalId: String, source: String) {
        val removed = repository.remove(externalId = externalId, source = source)
        check(removed) { "Не удалось удалить товар из избранного" }
        _favoriteMutations.tryEmit(
            FavoriteMutationEvent(
                externalId = externalId,
                source = source,
                isFavorite = false,
            ),
        )
    }
}


