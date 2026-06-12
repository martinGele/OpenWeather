package com.sporty.openweather.feature.forecast.domain.usecase

import app.cash.turbine.test
import com.sporty.openweather.feature.forecast.domain.location.LocationUnavailableException
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetWeatherUseCaseTest {

    private val weather = Weather("London", 18.0, "https://openweathermap.org/img/wn/01d@2x.png")
    private val coordinates = Coordinates(latitude = 51.5, longitude = -0.12)

    @Test
    fun `emits weather for the current coordinates`() = runTest {
        val repository = FakeWeatherRepository(weather)
        val useCase = GetWeatherUseCase(repository, FakeLocationProvider(coordinates = coordinates))

        useCase().test {
            assertEquals(weather, awaitItem())
            awaitComplete()
        }
        assertEquals(coordinates, repository.requestedCoordinates)
    }

    @Test
    fun `throws LocationUnavailableException when coordinates are unavailable`() = runTest {
        val useCase = GetWeatherUseCase(FakeWeatherRepository(weather), FakeLocationProvider(coordinates = null))

        useCase().test {
            assertTrue(awaitError() is LocationUnavailableException)
        }
    }

    private class FakeLocationProvider(private val coordinates: Coordinates?) : LocationProvider {
        override suspend fun currentCoordinates(): Coordinates? = coordinates
    }

    private class FakeWeatherRepository(private val weather: Weather) : WeatherRepository {
        var requestedCoordinates: Coordinates? = null
            private set

        override fun forecastCurrentDay(coordinates: Coordinates): Flow<Weather> {
            requestedCoordinates = coordinates
            return flowOf(weather)
        }

        override fun forecastNextDays(coordinates: Coordinates): Flow<List<Weather>> = flowOf(listOf(weather))
    }
}
