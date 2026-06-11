package com.sporty.openweather.feature.forecast.data

import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor() : WeatherRepository {

    override fun observeWeather(city: String): Flow<Weather> = emptyFlow()
}
