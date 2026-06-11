package com.sporty.openweather.feature.forecast.data.dto

import com.google.gson.annotations.SerializedName


data class WeatherDto(
    @SerializedName("name") val city: String,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<ConditionDto> = emptyList(),
)

data class MainDto(
    @SerializedName("temp") val tempCelsius: Double,
)

data class ConditionDto(
    @SerializedName("main") val condition: String,
)
