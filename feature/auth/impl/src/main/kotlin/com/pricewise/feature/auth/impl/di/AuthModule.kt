package com.pricewise.feature.auth.impl.di

import com.pricewise.feature.auth.impl.data.repository.AuthRepositoryImpl
import com.pricewise.feature.auth.impl.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
