package com.sporty.openweather.feature.forecast.data.di

import com.sporty.openweather.feature.forecast.data.location.DeviceLocationProvider
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindLocationProvider(impl: DeviceLocationProvider): LocationProvider
}
