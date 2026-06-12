package com.sporty.openweather.feature.search.data.di

import com.sporty.openweather.feature.search.data.LocationSearchRepositoryImpl
import com.sporty.openweather.feature.search.domain.repository.LocationSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchDataModule {

    @Binds
    @Singleton
    abstract fun bindLocationSearchRepository(
        impl: LocationSearchRepositoryImpl,
    ): LocationSearchRepository
}
