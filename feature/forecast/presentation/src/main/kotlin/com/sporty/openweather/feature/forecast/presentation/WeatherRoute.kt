package com.sporty.openweather.feature.forecast.presentation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes. Each `@Serializable` type *is* a destination: you navigate with an
 * instance (`navigate(WeatherRoute(lat, lon))`) and register with the type (`composable<WeatherRoute>`),
 * so the compiler checks the destination and its arguments — no route strings, no `navArgument`
 * declarations, no string parsing. Navigation serializes the instance into the destination's
 * SavedStateHandle; the ViewModel reads it back with `savedStateHandle.toRoute<WeatherRoute>()`.
 */

/**
 * Weather screen destination. Carries the location to show: `null` lat/lon means the device's (GPS)
 * weather, while a place picked from search supplies both. Because the fields are typed `Double?`,
 * the ViewModel gets real coordinates with no `toDoubleOrNull()` parsing, and the defaults make the
 * arguments optional so `WeatherRoute()` is a valid start destination.
 */
@Serializable
data class WeatherRoute(val lat: Double? = null, val lon: Double? = null)

/**
 * Search screen destination. An `object` (not a `data class`) because the screen takes no arguments —
 * it's purely a destination marker, the type-safe replacement for a `"search"` route string.
 */
@Serializable
object SearchRoute
