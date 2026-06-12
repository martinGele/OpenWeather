package com.sporty.openweather.feature.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    // One-time effects (e.g. permission requests). A Channel is consume-once, so events
    // aren't replayed on recomposition/config change the way persistent state would be.
    private val _effect = Channel<WeatherEffect>(Channel.BUFFERED)
    val effect: Flow<WeatherEffect> = _effect.receiveAsFlow()

    fun onIntent(intent: WeatherIntent) {
        when (intent) {
            WeatherIntent.Retry -> viewModelScope.launch {
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

    private fun loadWeather() {
        combine(
            getWeather(),
            getWeeklyForecast().catch { emit(emptyList()) },
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
