package com.pricewise.feature.favorites.impl.di

import com.pricewise.feature.favorites.impl.data.repository.FavoritesRepositoryImpl
import com.pricewise.feature.favorites.impl.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesModule {
    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
