package com.sporty.openweather.feature.search.data.mapper

import com.sporty.openweather.feature.search.data.dto.PlaceDto
import com.sporty.openweather.feature.search.domain.model.Coordinates
import com.sporty.openweather.feature.search.domain.model.Place

internal fun PlaceDto.toDomain(): Place = Place(
    name = name,
    country = country,
    state = state,
    coordinates = Coordinates(latitude = lat, longitude = lon),
)
