package com.sporty.openweather.feature.forecast.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sporty.openweather.feature.forecast.domain.model.Weather

@Composable
fun WeatherForecast(
    goToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val requestLocationPermission = rememberLocationPermissionRequest { granted ->
        viewModel.onIntent(
            if (granted) WeatherIntent.PermissionGranted else WeatherIntent.PermissionDenied,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WeatherEffect.RequestLocationPermission -> requestLocationPermission()
            }
        }
    }

    // Triggers GPS-based weather on first show; ignored by the VM if a place was picked from search.
    LaunchedEffect(Unit) { viewModel.onIntent(WeatherIntent.Retry) }

    WeatherScreen(
        state = state,
        onRetry = { viewModel.onIntent(WeatherIntent.Retry) },
        modifier = modifier,
        goToSearch = goToSearch,
    )
}

@Composable
fun WeatherScreen(
    state: WeatherState,
    onRetry: () -> Unit,
    goToSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            state.isLoading -> CircularProgressIndicator()

            state.error != null -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = state.error,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                )
                Button(onClick = onRetry) { Text(text = "Retry") }
            }

            state.weather != null -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { goToSearch() },
                    )
                    Text(text = state.weather.city, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "${state.weather.temperatureCelsius}°C",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    AsyncImage(
                        model = state.weather.iconUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                    )
                }

                if (state.weeklyForecast.isNotEmpty()) {
                    WeeklyForecast(days = state.weeklyForecast)
                }
            }
        }
    }
}

@Composable
private fun WeeklyForecast(
    days: List<Weather>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "7-day forecast", style = MaterialTheme.typography.titleMedium)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(days) { day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    AsyncImage(
                        model = day.iconUrl,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = "${day.temperatureCelsius.toInt()}°C",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
