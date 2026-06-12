package com.sporty.openweather.feature.search.domain.repository

import com.sporty.openweather.feature.search.domain.model.Place

interface LocationSearchRepository {
    suspend fun search(query: String): List<Place>
}
