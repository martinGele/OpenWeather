package com.sporty.openweather.feature.forecast.domain.model

/** Domain model — a plain data class that data-layer DTOs map onto. */
data class Weather(
    val city: String,
    val temperatureCelsius: Double,
    val condition: WeatherCondition,
)

enum class WeatherCondition {
    CLEAR,
    CLOUDY,
    RAIN,
    SNOW,
    THUNDERSTORM,
    UNKNOWN,
}
