package com.sporty.openweather.feature.forecast.data.api

import com.sporty.openweather.feature.forecast.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("noIdeaForNow")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
    ): WeatherDto
}
