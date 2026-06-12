package com.sporty.openweather.feature.forecast.domain.model


data class Forecast(
    val currentWeather: Weather,
    val nextDaysWeather: List<Weather>,
    val isLoading: Boolean = false,
    val error: String? = null,
)


data class Weather(
    val city: String,
    val temperatureCelsius: Double,
    val iconUrl: String,
)
