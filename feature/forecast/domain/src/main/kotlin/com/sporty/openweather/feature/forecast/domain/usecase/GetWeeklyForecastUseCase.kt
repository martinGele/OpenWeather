package com.sporty.openweather.feature.forecast.domain.usecase

import com.sporty.openweather.feature.forecast.domain.location.LocationUnavailableException
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetWeeklyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider,
) {
    operator fun invoke(): Flow<List<Weather>> = flow {
        val coordinates = locationProvider.currentCoordinates() ?: throw LocationUnavailableException()
        emitAll(repository.forecastNextDays(coordinates))
    }
}
