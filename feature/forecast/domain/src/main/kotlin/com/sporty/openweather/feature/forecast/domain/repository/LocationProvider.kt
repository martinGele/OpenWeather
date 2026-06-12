package com.sporty.openweather.feature.forecast.domain.repository

import com.sporty.openweather.feature.forecast.domain.model.Coordinates

interface LocationProvider {
    suspend fun currentCoordinates(): Coordinates?
}
