package com.sporty.openweather.feature.forecast.presentation

import com.sporty.openweather.feature.forecast.domain.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val weeklyForecast: List<Weather> = emptyList(),
    val error: String? = null,
)

sealed interface WeatherIntent {
    data object Retry : WeatherIntent

    data object PermissionGranted : WeatherIntent

    data object PermissionDenied : WeatherIntent
}

sealed interface WeatherEffect {
    data object RequestLocationPermission : WeatherEffect
}
