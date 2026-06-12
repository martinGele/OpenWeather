package com.sporty.openweather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sporty.openweather.feature.forecast.presentation.SearchRoute
import com.sporty.openweather.feature.forecast.presentation.WeatherForecast
import com.sporty.openweather.feature.forecast.presentation.WeatherRoute
import com.sporty.openweather.feature.search.presentation.LocationSearch


@Composable
fun OpenWeatherNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = WeatherRoute(),
    ) {
        composable<WeatherRoute> {
            WeatherForecast(
                goToSearch = { navController.navigate(SearchRoute) },
            )
        }
        composable<SearchRoute> {
            LocationSearch(
                onPlaceSelected = { coordinates ->
                    // Re-open the weather screen for the picked place; nav args seed its ViewModel.
                    navController.navigate(
                        WeatherRoute(
                            coordinates.latitude,
                            coordinates.longitude
                        )
                    ) {
                        popUpTo<WeatherRoute> { inclusive = true }
                    }
                },
            )
        }
    }
}
