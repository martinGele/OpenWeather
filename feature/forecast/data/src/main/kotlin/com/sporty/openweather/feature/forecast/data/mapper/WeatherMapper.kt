package com.sporty.openweather.feature.forecast.data.mapper

import com.sporty.openweather.feature.forecast.data.dto.DailyForecastDto
import com.sporty.openweather.feature.forecast.data.dto.WeatherDto
import com.sporty.openweather.feature.forecast.domain.model.Weather

internal fun WeatherDto.toDomain(): Weather {
    val condition = weather.firstOrNull()
    return Weather(
        city = name,
        temperatureCelsius = main.temp,
        iconUrl = condition?.icon.toIconUrl(),
        country = sys.country,
        condition = condition?.main.orEmpty(),
        description = condition?.description.orEmpty(),
        feelsLikeCelsius = main.feelsLike,
        minTempCelsius = main.tempMin,
        maxTempCelsius = main.tempMax,
        humidityPercent = main.humidity,
        windSpeedMs = wind.speed,
        pressureHpa = main.pressure,
        visibilityMeters = visibility,
        cloudinessPercent = clouds.all,
        sunriseEpochSeconds = sys.sunrise.toLong(),
        sunsetEpochSeconds = sys.sunset.toLong(),
        timestampEpochSeconds = dt.toLong(),
    )
}

internal fun DailyForecastDto.toDomain(): List<Weather> = list.map { day ->
    val condition = day.weather.firstOrNull()
    Weather(
        city = city.name,
        temperatureCelsius = day.temp.day,
        iconUrl = condition?.icon.toIconUrl(),
        country = city.country,
        condition = condition?.main.orEmpty(),
        description = condition?.description.orEmpty(),
        minTempCelsius = day.temp.min,
        maxTempCelsius = day.temp.max,
        humidityPercent = day.humidity,
        windSpeedMs = day.speed,
        precipitationChance = day.pop,
        timestampEpochSeconds = day.dt,
    )
}

private fun String?.toIconUrl(): String =
    "https://openweathermap.org/img/wn/${this ?: "01d"}@2x.png"
