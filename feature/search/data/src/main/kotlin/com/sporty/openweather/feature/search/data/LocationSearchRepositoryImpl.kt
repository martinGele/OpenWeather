package com.sporty.openweather.feature.search.data

import com.sporty.openweather.core.common.dispatchers.IoDispatcher
import com.sporty.openweather.core.network.NetworkResult
import com.sporty.openweather.core.network.safeApiCall
import com.sporty.openweather.feature.search.data.api.GeocodingApi
import com.sporty.openweather.feature.search.data.mapper.toDomain
import com.sporty.openweather.feature.search.domain.model.Place
import com.sporty.openweather.feature.search.domain.repository.LocationSearchRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocationSearchRepositoryImpl @Inject constructor(
    private val api: GeocodingApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocationSearchRepository {

    override suspend fun search(query: String): List<Place> = withContext(ioDispatcher) {
        when (val result = safeApiCall { api.searchPlaces(query) }) {
            is NetworkResult.Success -> result.data.map { it.toDomain() }
            is NetworkResult.Error -> throw Exception(result.message)
        }
    }
}
