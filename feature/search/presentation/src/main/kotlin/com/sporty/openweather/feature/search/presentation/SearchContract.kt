package com.sporty.openweather.feature.search.presentation

import com.sporty.openweather.feature.search.domain.model.Place

data class SearchState(
    val isLoading: Boolean = false,
    val results: List<Place> = emptyList(),
    val error: String? = null,
)

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
}
