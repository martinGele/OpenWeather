package com.sporty.openweather.feature.search.domain.model

data class Place(
    val name: String,
    val country: String,
    val state: String? = null,
    val coordinates: Coordinates,
)
