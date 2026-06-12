package com.sporty.openweather.feature.forecast.domain.model

data class Weather(
    val city: String,
    val temperatureCelsius: Double,
    val iconUrl: String,
)
