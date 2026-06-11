package com.sporty.openweather.feature.forecast.data.di

import com.sporty.openweather.feature.forecast.data.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create

/** The feature owns only its endpoint surface — the shared Retrofit comes from :core:network. */
@Module
@InstallIn(SingletonComponent::class)
object WeatherApiModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create()
}
