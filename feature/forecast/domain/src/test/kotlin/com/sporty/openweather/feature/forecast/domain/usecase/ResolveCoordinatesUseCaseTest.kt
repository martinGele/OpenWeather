package com.sporty.openweather.feature.forecast.domain.usecase

import com.sporty.openweather.feature.forecast.domain.location.LocationUnavailableException
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolveCoordinatesUseCaseTest {

    private val picked = Coordinates(latitude = 51.5, longitude = -0.12)
    private val device = Coordinates(latitude = 40.71, longitude = -74.0)

    @Test
    fun `returns the supplied coordinates without consulting the device location`() = runTest {
        val provider = FakeLocationProvider(coordinates = device)
        val resolve = ResolveCoordinatesUseCase(provider)

        assertEquals(picked, resolve(picked))
        assertEquals(0, provider.callCount)
    }

    @Test
    fun `falls back to the device location when no coordinates are supplied`() = runTest {
        val resolve = ResolveCoordinatesUseCase(FakeLocationProvider(coordinates = device))

        assertEquals(device, resolve())
    }

    @Test
    fun `throws LocationUnavailableException when neither source has coordinates`() = runTest {
        val resolve = ResolveCoordinatesUseCase(FakeLocationProvider(coordinates = null))

        val error = runCatching { resolve() }.exceptionOrNull()
        assertTrue(error is LocationUnavailableException)
    }

    private class FakeLocationProvider(private val coordinates: Coordinates?) : LocationProvider {
        var callCount = 0
            private set

        override suspend fun currentCoordinates(): Coordinates? {
            callCount++
            return coordinates
        }
    }
}
