package com.pricewise.feature.home.impl.di

import com.pricewise.feature.home.api.HomeScreenApi
import com.pricewise.feature.home.impl.data.mapper.HomeDataToDomainMapper
import com.pricewise.feature.home.impl.data.repository.HomeRepositoryImpl
import com.pricewise.feature.home.impl.domain.repository.HomeRepository
import com.pricewise.feature.home.impl.domain.usecase.GetHomeFeedUseCase
import com.pricewise.feature.home.impl.domain.usecase.GetHomeFeedUseCaseImpl
import com.pricewise.feature.home.impl.presentation.mapper.HomeScreenMapper
import com.pricewise.feature.home.impl.presentation.HomeScreenApiImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeFeatureModule {

    @Binds
    @Singleton
    abstract fun bindHomeScreenApi(
        impl: HomeScreenApiImpl,
    ): HomeScreenApi

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl,
    ): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun provideHomeDataToDomainMapper(): HomeDataToDomainMapper {
            return HomeDataToDomainMapper()
        }

        @Provides
        @Singleton
        fun provideHomeScreenMapper(): HomeScreenMapper {
            return HomeScreenMapper()
        }

        @Provides
        @Singleton
        fun provideGetHomeFeedUseCase(
            repository: HomeRepository,
        ): GetHomeFeedUseCase {
            return GetHomeFeedUseCaseImpl(repository = repository)
        }
    }
}
