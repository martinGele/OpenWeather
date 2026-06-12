package com.sporty.openweather.feature.forecast.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.model.Weather
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import com.sporty.openweather.feature.forecast.domain.repository.WeatherRepository
import com.sporty.openweather.feature.forecast.domain.usecase.GetWeatherUseCase
import com.sporty.openweather.feature.forecast.domain.usecase.GetWeeklyForecastUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val weather = Weather("London", 18.0, "https://openweathermap.org/img/wn/01d@2x.png")
    private val coordinates = Coordinates(latitude = 51.5, longitude = -0.12)

    @Test
    fun `initial state is default`() {
        assertEquals(WeatherState(), viewModel(coordinates = coordinates).state.value)
    }

    @Test
    fun `PermissionGranted loads weather then leaves a non-loading success state`() = runTest {
        val viewModel = viewModel(coordinates = coordinates, weatherFlow = flowOf(weather))

        viewModel.onIntent(WeatherIntent.PermissionGranted)
        advanceUntilIdle()

        assertEquals(WeatherState(isLoading = false, weather = weather, error = null), viewModel.state.value)
    }

    @Test
    fun `PermissionGranted surfaces an error when loading fails`() = runTest {
        val failing = flow<Weather> { throw IllegalStateException("Network error") }
        val viewModel = viewModel(coordinates = coordinates, weatherFlow = failing)

        viewModel.onIntent(WeatherIntent.PermissionGranted)
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertEquals(false, isLoading)
            assertNull(weather)
            assertEquals("Network error", error)
        }
    }

    @Test
    fun `PermissionGranted populates the weekly forecast`() = runTest {
        val week = List(7) { weather }
        val viewModel = viewModel(coordinates = coordinates, weeklyFlow = flowOf(week))

        viewModel.onIntent(WeatherIntent.PermissionGranted)
        advanceUntilIdle()

        assertEquals(week, viewModel.state.value.weeklyForecast)
    }

    @Test
    fun `PermissionDenied sets a permission error without loading`() = runTest {
        val viewModel = viewModel(coordinates = coordinates)

        viewModel.onIntent(WeatherIntent.PermissionDenied)

        with(viewModel.state.value) {
            assertEquals(false, isLoading)
            assertTrue(error?.contains("permission", ignoreCase = true) == true)
        }
    }

    @Test
    fun `Retry emits the RequestLocationPermission effect`() = runTest {
        val viewModel = viewModel(coordinates = coordinates)

        viewModel.effect.test {
            viewModel.onIntent(WeatherIntent.Retry)
            assertEquals(WeatherEffect.RequestLocationPermission, awaitItem())
        }
    }

    private fun viewModel(
        coordinates: Coordinates?,
        weatherFlow: Flow<Weather> = flowOf(weather),
        weeklyFlow: Flow<List<Weather>> = flowOf(emptyList()),
    ): WeatherViewModel {
        val repository = object : WeatherRepository {
            override fun forecastCurrentDay(coordinates: Coordinates): Flow<Weather> = weatherFlow

            override fun forecastNextDays(coordinates: Coordinates): Flow<List<Weather>> = weeklyFlow
        }
        val locationProvider = object : LocationProvider {
            override suspend fun currentCoordinates(): Coordinates? = coordinates
        }
        return WeatherViewModel(
            GetWeatherUseCase(repository, locationProvider),
            GetWeeklyForecastUseCase(repository, locationProvider),
            SavedStateHandle(),
        )
    }
}
