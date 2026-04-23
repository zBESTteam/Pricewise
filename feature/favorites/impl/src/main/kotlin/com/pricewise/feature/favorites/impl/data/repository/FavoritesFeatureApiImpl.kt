package com.pricewise.feature.favorites.impl.data.repository

import com.pricewise.feature.favorites.api.FavoriteMutationRequest
import com.pricewise.feature.favorites.api.FavoriteMutationEvent
import com.pricewise.feature.favorites.api.FavoritesFeatureApi
import com.pricewise.feature.favorites.impl.domain.model.FavoriteCreate
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FavoritesFeatureApiImpl @Inject constructor(
    private val repository: FavoritesRepository,
) : FavoritesFeatureApi {

    private val _favoriteMutations = MutableSharedFlow<FavoriteMutationEvent>(extraBufferCapacity = 64)
    override val favoriteMutations: SharedFlow<FavoriteMutationEvent> = _favoriteMutations.asSharedFlow()
    private val _favoriteStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    override val favoriteStates: StateFlow<Map<String, Boolean>> = _favoriteStates.asStateFlow()

    override suspend fun syncFavorites() {
        val favorites = repository.list()
        val stateMap = favorites.associate { item ->
            makeFavoriteKey(externalId = item.externalId, source = item.source) to true
        }
        _favoriteStates.value = stateMap
    }

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
        updateFavoriteState(
            externalId = request.externalId,
            source = request.source,
            isFavorite = true,
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
        updateFavoriteState(
            externalId = externalId,
            source = source,
            isFavorite = false,
        )
        _favoriteMutations.tryEmit(
            FavoriteMutationEvent(
                externalId = externalId,
                source = source,
                isFavorite = false,
            ),
        )
    }

    private fun updateFavoriteState(
        externalId: String,
        source: String,
        isFavorite: Boolean,
    ) {
        val favoriteKey = makeFavoriteKey(
            externalId = externalId,
            source = source,
        )
        _favoriteStates.update { currentState ->
            currentState + (favoriteKey to isFavorite)
        }
    }

    private fun makeFavoriteKey(
        externalId: String,
        source: String,
    ): String {
        val normalizedSource = source.trim().ifBlank { "unknown" }
        return "$externalId|$normalizedSource"
    }
}
