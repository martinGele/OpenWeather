package com.sporty.openweather.feature.forecast.domain.usecase

import com.sporty.openweather.feature.forecast.domain.location.LocationUnavailableException
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import javax.inject.Inject

class ResolveCoordinatesUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(coordinates: Coordinates? = null): Coordinates =
        coordinates ?: locationProvider.currentCoordinates() ?: throw LocationUnavailableException()
}
