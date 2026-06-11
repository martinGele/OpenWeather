package com.sporty.openweather.feature.forecast.data.mapper

import com.sporty.openweather.feature.forecast.data.dto.WeatherDto
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.model.WeatherCondition

/** Maps the serializable network DTO to the domain model at the data-layer boundary. */
internal fun WeatherDto.toDomain(): Weather = Weather(
    city = city,
    temperatureCelsius = main.tempCelsius,
    condition = weather.firstOrNull()?.condition.toWeatherCondition(),
)

private fun String?.toWeatherCondition(): WeatherCondition = when (this?.lowercase()) {
    "clear" -> WeatherCondition.CLEAR
    "clouds", "cloudy" -> WeatherCondition.CLOUDY
    "rain", "drizzle" -> WeatherCondition.RAIN
    "snow" -> WeatherCondition.SNOW
    "thunderstorm" -> WeatherCondition.THUNDERSTORM
    else -> WeatherCondition.UNKNOWN
}
