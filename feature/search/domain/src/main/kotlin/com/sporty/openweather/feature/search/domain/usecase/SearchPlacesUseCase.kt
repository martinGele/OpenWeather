package com.sporty.openweather.feature.search.domain.usecase

import com.sporty.openweather.feature.search.domain.model.Place
import com.sporty.openweather.feature.search.domain.repository.LocationSearchRepository
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val repository: LocationSearchRepository,
) {
    suspend operator fun invoke(query: String): List<Place> {
        val trimmed = query.trim()
        return if (trimmed.isBlank()) emptyList() else repository.search(trimmed)
    }
}
