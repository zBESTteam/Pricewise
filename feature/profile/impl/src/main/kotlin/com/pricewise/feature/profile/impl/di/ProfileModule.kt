package com.pricewise.feature.profile.impl.di

import com.pricewise.feature.profile.impl.data.repository.ProfileRepositoryImpl
import com.pricewise.feature.profile.impl.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {
    @Binds
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
