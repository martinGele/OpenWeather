package com.sporty.openweather.feature.forecast.domain.usecase

import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    operator fun invoke(city: String): Flow<Weather> =
        repository.observeWeather(city)
}
