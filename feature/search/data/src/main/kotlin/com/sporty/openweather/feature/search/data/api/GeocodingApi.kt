package com.sporty.openweather.feature.search.data.api

import com.sporty.openweather.feature.search.data.dto.PlaceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("geo/1.0/direct")
    suspend fun searchPlaces(
        @Query("q") query: String,
        @Query("limit") limit: Int = 15,
    ): List<PlaceDto>
}
