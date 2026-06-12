package com.sporty.openweather.feature.forecast.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun WeatherForecast(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onIntent(
            if (granted) WeatherIntent.PermissionGranted else WeatherIntent.PermissionDenied,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WeatherEffect.RequestLocationPermission -> permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.onIntent(WeatherIntent.Retry) }

    WeatherScreen(
        state = state,
        onRetry = { viewModel.onIntent(WeatherIntent.Retry) },
        modifier = modifier,
    )
}

@Composable
fun WeatherScreen(
    state: WeatherState,
    onRetry: () -> Unit,
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

            state.weather != null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }
    }
}
