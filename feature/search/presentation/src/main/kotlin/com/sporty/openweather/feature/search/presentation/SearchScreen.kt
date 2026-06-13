package com.sporty.openweather.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sporty.openweather.core.ui.components.SearchFieldPill
import com.sporty.openweather.core.ui.components.WeatherSkyAnimation
import com.sporty.openweather.core.ui.theme.ErrorOnSky
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.OnSkyMuted
import com.sporty.openweather.core.ui.theme.Sky
import com.sporty.openweather.core.ui.theme.Spacing
import com.sporty.openweather.feature.search.domain.model.Coordinates
import com.sporty.openweather.feature.search.domain.model.Place

@Composable
fun LocationSearch(
    modifier: Modifier = Modifier,
    onPlaceSelected: (Coordinates) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onPlaceSelected = onPlaceSelected,
        modifier = modifier,
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onIntent: (SearchIntent) -> Unit,
    onPlaceSelected: (Coordinates) -> Unit,
    modifier: Modifier = Modifier,
) {

    var query by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(Sky.default)),
    ) {

        WeatherSkyAnimation(condition = "Clear", isDay = true, sunHeightFraction = 0.45f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.base),
        ) {
            Text(
                text = "Where to?",
                style = MaterialTheme.typography.displayMedium,
                color = OnSky,
                modifier = Modifier.padding(top = Spacing.lg),
            )

            SearchFieldPill(
                value = query,
                onValueChange = {
                    query = it
                    onIntent(SearchIntent.QueryChanged(it))
                },
                placeholder = "Search for a city",
                searchIcon = painterResource(id = com.sporty.openweather.core.ui.R.drawable.ic_search),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator(
                        color = OnSky,
                        modifier = Modifier.padding(top = Spacing.lg),
                    )

                    state.error != null -> Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ErrorOnSky,
                        modifier = Modifier.padding(top = Spacing.lg),
                    )

                    else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = state.results,
                            key = { "${it.name},${it.state},${it.country},${it.coordinates.latitude},${it.coordinates.longitude}" },
                        ) { place ->
                            PlaceRow(
                                place = place,
                                onClick = { onPlaceSelected(place.coordinates) },
                            )
                            HorizontalDivider(color = GlassBorder)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceRow(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Text(
            text = place.name,
            style = MaterialTheme.typography.titleMedium,
            color = OnSky,
        )
        Text(
            text = listOfNotNull(place.state, place.country).joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            color = OnSkyMuted,
        )
    }
}
