package com.sporty.openweather.feature.forecast.data.dto

import com.google.gson.annotations.SerializedName

data class DailyForecastDto(
    @SerializedName("city")
    val city: City,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val list: List<Day>
) {
    data class City(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("country")
        val country: String
    )

    data class Day(
        @SerializedName("dt")
        val dt: Long,
        @SerializedName("temp")
        val temp: Temp,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("weather")
        val weather: List<Weather>,
        @SerializedName("speed")
        val speed: Double,
        @SerializedName("pop")
        val pop: Double
    )

    data class Temp(
        @SerializedName("day")
        val day: Double,
        @SerializedName("min")
        val min: Double,
        @SerializedName("max")
        val max: Double,
        @SerializedName("night")
        val night: Double,
        @SerializedName("eve")
        val eve: Double,
        @SerializedName("morn")
        val morn: Double
    )

    data class Weather(
        @SerializedName("id")
        val id: Int,
        @SerializedName("main")
        val main: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("icon")
        val icon: String
    )
}
