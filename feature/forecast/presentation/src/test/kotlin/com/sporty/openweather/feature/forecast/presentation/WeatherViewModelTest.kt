package com.sporty.openweather.feature.forecast.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherViewModelTest {

    @Test
    fun `initial state is default`() {
        val viewModel = WeatherViewModel()
        assertEquals(WeatherState(), viewModel.state.value)
    }
}
