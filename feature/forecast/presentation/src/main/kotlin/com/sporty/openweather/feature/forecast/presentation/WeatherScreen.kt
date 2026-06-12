package com.sporty.openweather.feature.forecast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sporty.openweather.core.ui.components.PrimaryButton
import com.sporty.openweather.core.ui.components.SearchBarPill
import com.sporty.openweather.core.ui.components.StatTile
import com.sporty.openweather.core.ui.theme.ErrorOnSky
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.OnSkyMuted
import com.sporty.openweather.core.ui.theme.Rausch
import com.sporty.openweather.core.ui.theme.Sky
import com.sporty.openweather.core.ui.theme.Spacing
import com.sporty.openweather.feature.forecast.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    // The backdrop IS the weather: pick the gradient from the current conditions,
    // falling back to a calm clear-day sky while loading or on error.
    val sky = state.weather
        ?.let { Sky.gradientFor(it.condition, it.isDay()) }
        ?: Sky.default

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(sky)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg),
        ) {
            Spacer(Modifier.height(Spacing.base))
            SearchBarPill(
                placeholder = "Search for a city",
                searchIcon = painterResource(id = R.drawable.baseline_search_24),
                onClick = goToSearch,
            )
            Spacer(Modifier.height(Spacing.lg))

            when {
                state.isLoading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = OnSky)
                }

                state.error != null -> ErrorState(message = state.error, onRetry = onRetry)

                state.weather != null -> Column {
                    CurrentWeather(weather = state.weather)

                    if (state.weeklyForecast.isNotEmpty()) {
                        Spacer(Modifier.height(Spacing.xl))
                        WeeklyForecast(days = state.weeklyForecast)
                    }
                    Spacer(Modifier.height(Spacing.section))
                }
            }
        }
    }
}

/** Day when the observation falls between sunrise and sunset; day by default. */
private fun Weather.isDay(): Boolean {
    val t = timestampEpochSeconds ?: return true
    val sunrise = sunriseEpochSeconds ?: return true
    val sunset = sunsetEpochSeconds ?: return true
    return t in sunrise..sunset
}

/**
 * The hero — location + condition, the loud 64sp temperature beside the live icon,
 * and a glass grid of the richer metrics.
 */
@Composable
private fun CurrentWeather(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = listOfNotNull(weather.city, weather.country).joinToString(", "),
            style = MaterialTheme.typography.displaySmall,
            color = OnSky,
        )
        if (weather.description.isNotBlank()) {
            Text(
                text = weather.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                color = OnSkyMuted,
            )
        }

        Spacer(Modifier.height(Spacing.base))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "${weather.temperatureCelsius.toInt()}°",
                    style = MaterialTheme.typography.displayLarge,
                    color = OnSky,
                )
                val hl = listOfNotNull(
                    weather.maxTempCelsius?.let { "H: ${it.toInt()}°" },
                    weather.minTempCelsius?.let { "L: ${it.toInt()}°" },
                ).joinToString("   ")
                if (hl.isNotBlank()) {
                    Text(text = hl, style = MaterialTheme.typography.bodyMedium, color = OnSkyMuted)
                }
            }
            AsyncImage(
                model = weather.iconUrl,
                contentDescription = weather.condition,
                modifier = Modifier.size(120.dp),
            )
        }

        Spacer(Modifier.height(Spacing.lg))
        StatGrid(stats = weather.toStats())
    }
}

/** Builds the labelled metrics that this observation actually carries. */
private fun Weather.toStats(): List<Pair<String, String>> = buildList {
    feelsLikeCelsius?.let { add("Feels like" to "${it.toInt()}°") }
    humidityPercent?.let { add("Humidity" to "$it%") }
    windSpeedMs?.let { add("Wind" to "${it.toInt()} m/s") }
    cloudinessPercent?.let { add("Cloudiness" to "$it%") }
    pressureHpa?.let { add("Pressure" to "$it hPa") }
    visibilityMeters?.let { add("Visibility" to "${"%.1f".format(it / 1000.0)} km") }
    sunriseEpochSeconds?.let { add("Sunrise" to it.toClockTime()) }
    sunsetEpochSeconds?.let { add("Sunset" to it.toClockTime()) }
}

@Composable
private fun StatGrid(
    stats: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        stats.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                row.forEach { (label, value) ->
                    StatTile(label = label, value = value, modifier = Modifier.weight(1f))
                }
                // Keep the last odd tile half-width rather than stretching it full.
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun WeeklyForecast(
    days: List<Weather>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "7-day forecast",
            style = MaterialTheme.typography.headlineMedium,
            color = OnSky,
        )
        Spacer(Modifier.height(Spacing.sm))
        days.forEachIndexed { index, day ->
            DayRow(day = day)
            if (index < days.lastIndex) HorizontalDivider(color = GlassBorder)
        }
    }
}

@Composable
private fun DayRow(
    day: Weather,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        AsyncImage(
            model = day.iconUrl,
            contentDescription = day.condition,
            modifier = Modifier.size(44.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = day.timestampEpochSeconds.toDayName(),
                style = MaterialTheme.typography.titleMedium,
                color = OnSky,
            )
            val sub = listOfNotNull(
                day.description.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() },
                day.precipitationChance?.let { "${(it * 100).toInt()}% rain" },
            ).joinToString(" · ")
            if (sub.isNotBlank()) {
                Text(
                    text = sub,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSkyMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Text(
            text = listOfNotNull(
                day.maxTempCelsius?.let { "${it.toInt()}°" },
                day.minTempCelsius?.let { "${it.toInt()}°" },
            ).joinToString("  "),
            style = MaterialTheme.typography.titleSmall,
            color = OnSky,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.base, Alignment.CenterVertically),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = ErrorOnSky,
            textAlign = TextAlign.Center,
        )
        PrimaryButton(text = "Try again", onClick = onRetry)
    }
}

private fun Long.toClockTime(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this * 1000))

private fun Long?.toDayName(): String =
    if (this == null) "—" else SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(this * 1000))
