package com.sporty.openweather.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    // The text field owns its value locally so typing is instant and doesn't round-trip
    // through the ViewModel; each change feeds the debounced search.
    var query by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onIntent(SearchIntent.QueryChanged(it))
            },
            label = { Text("Search for a city") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter,
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.padding(top = 24.dp),
                )

                state.error != null -> Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 24.dp),
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
                        HorizontalDivider()
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
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(text = place.name, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = listOfNotNull(place.state, place.country).joinToString(", "),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
