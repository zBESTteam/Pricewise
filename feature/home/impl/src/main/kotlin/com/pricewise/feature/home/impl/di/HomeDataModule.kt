package com.pricewise.feature.home.impl.di

import com.pricewise.feature.home.impl.data.repository.HomeRepositoryImpl
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl,
    ): HomeRepository
}
