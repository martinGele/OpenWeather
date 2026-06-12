package com.sporty.openweather.feature.forecast.data.mapper

import com.sporty.openweather.feature.forecast.data.dto.WeatherDto
import com.sporty.openweather.feature.forecast.domain.model.Weather

internal fun WeatherDto.toDomain(): Weather = Weather(
    city = name,
    temperatureCelsius = main.temp,
    iconUrl = weather.firstOrNull()?.icon.toIconUrl(),
)

private fun String?.toIconUrl(): String =
    "https://openweathermap.org/img/wn/${this ?: "01d"}@2x.png"
