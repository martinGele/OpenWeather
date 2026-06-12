package com.sporty.openweather.feature.forecast.domain.model


data class Forecast(
    val currentWeather: Weather,
    val nextDaysWeather: List<Weather>,
    val isLoading: Boolean = false,
    val error: String? = null,
)


/**
 * A single weather observation — either the current conditions or one day of the
 * weekly forecast. The three original fields stay first for source compatibility;
 * the rest are optional because the "current weather" and "daily forecast"
 * endpoints each populate a different subset (e.g. only current carries
 * sunrise/sunset; only daily carries [precipitationChance] and min/max).
 */
data class Weather(
    val city: String,
    val temperatureCelsius: Double,
    val iconUrl: String,
    val country: String? = null,
    /** Short condition group, e.g. "Clouds". */
    val condition: String = "",
    /** Human description, e.g. "scattered clouds". */
    val description: String = "",
    val feelsLikeCelsius: Double? = null,
    val minTempCelsius: Double? = null,
    val maxTempCelsius: Double? = null,
    val humidityPercent: Int? = null,
    val windSpeedMs: Double? = null,
    val pressureHpa: Int? = null,
    val visibilityMeters: Int? = null,
    val cloudinessPercent: Int? = null,
    /** Probability of precipitation, 0.0..1.0 (daily forecast only). */
    val precipitationChance: Double? = null,
    val sunriseEpochSeconds: Long? = null,
    val sunsetEpochSeconds: Long? = null,
    /** Observation time, epoch seconds — used to label forecast days. */
    val timestampEpochSeconds: Long? = null,
)
