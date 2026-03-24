package com.pricewise.feature.search.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.pricewise.feature.search.api.SearchFeatureApi
import com.pricewise.feature.search.impl.data.repository.RemoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {
    @Binds
    @Singleton
    abstract fun bindSearchFeatureApi(impl: RemoteRepository): SearchFeatureApi
}