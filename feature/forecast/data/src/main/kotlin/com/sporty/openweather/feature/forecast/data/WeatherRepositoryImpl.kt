package com.sporty.openweather.feature.forecast.data

import com.sporty.openweather.core.common.dispatchers.IoDispatcher
import com.sporty.openweather.core.network.NetworkResult
import com.sporty.openweather.core.network.safeApiCall
import com.sporty.openweather.feature.forecast.data.api.WeatherApi
import com.sporty.openweather.feature.forecast.data.mapper.toDomain
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {

    override fun observeWeather(coordinates: Coordinates): Flow<Weather> = flow {
        when (val result = safeApiCall { api.getWeather(coordinates.latitude, coordinates.longitude) }) {
            is NetworkResult.Success -> emit(result.data.toDomain())
            is NetworkResult.Error -> throw IllegalStateException(result.message)
        }
    }.flowOn(ioDispatcher)
}
