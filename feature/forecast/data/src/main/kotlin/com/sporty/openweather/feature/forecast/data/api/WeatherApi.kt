package com.sporty.openweather.feature.forecast.data.api

import com.sporty.openweather.feature.forecast.data.dto.DailyForecastDto
import com.sporty.openweather.feature.forecast.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
    ): WeatherDto

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") count: Int = 7,
        @Query("units") units: String = "metric",
    ): DailyForecastDto
}
