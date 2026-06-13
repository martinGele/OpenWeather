package com.sporty.openweather.feature.forecast.domain.usecase

import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetWeeklyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val resolveCoordinates: ResolveCoordinatesUseCase,
) {
    operator fun invoke(coordinates: Coordinates? = null): Flow<List<Weather>> = flow {
        emitAll(repository.forecastNextDays(resolveCoordinates(coordinates)))
    }
}
