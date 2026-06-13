package com.sporty.openweather.feature.forecast.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.usecase.GetWeatherUseCase
import com.sporty.openweather.feature.forecast.domain.usecase.GetWeeklyForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetWeatherUseCase,
    private val getWeeklyForecast: GetWeeklyForecastUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()


    private val _effect = Channel<WeatherEffect>(Channel.BUFFERED)
    val effect: Flow<WeatherEffect> = _effect.receiveAsFlow()

    init {
        // SavedStateHandle. When present, load that place's weather instead of GPS.
        selectedCoordinates()?.let(::loadWeather)
    }

    fun onIntent(intent: WeatherIntent) {
        when (intent) {
            // Skip GPS when a place was already picked from search.
            WeatherIntent.Retry -> if (selectedCoordinates() == null) viewModelScope.launch {
                _effect.send(WeatherEffect.RequestLocationPermission)
            }

            WeatherIntent.UseCurrentLocation -> viewModelScope.launch {
                _effect.send(WeatherEffect.RequestLocationPermission)
            }

            WeatherIntent.PermissionGranted -> loadWeather()
            WeatherIntent.PermissionDenied -> _state.update {
                it.copy(
                    isLoading = false,
                    error = "Location permission is required to show the weather."
                )
            }
        }
    }

    /** Coordinates from the type-safe nav route, or null when launched for the device's location. */
    private fun selectedCoordinates(): Coordinates? {
        val route = savedStateHandle.toRoute<WeatherRoute>()
        val lat = route.lat ?: return null
        val lon = route.lon ?: return null
        return Coordinates(lat, lon)
    }

    private fun loadWeather(coordinates: Coordinates? = null) {
        combine(
            getWeather(coordinates),
            getWeeklyForecast(coordinates).catch { emit(emptyList()) },
        ) { current, weekly ->
            current to weekly
        }
            .onStart { _state.update { it.copy(isLoading = true, error = null) } }
            .onEach { (current, weekly) ->
                _state.update {
                    it.copy(isLoading = false, weather = current, weeklyForecast = weekly)
                }
            }
            .catch { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
