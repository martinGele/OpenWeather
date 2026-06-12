package com.sporty.openweather.feature.forecast.domain.repository

import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(coordinates: Coordinates): Flow<Weather>
}
