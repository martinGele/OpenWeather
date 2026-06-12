package com.sporty.openweather.feature.forecast.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.sporty.openweather.core.common.dispatchers.IoDispatcher
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeviceLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocationProvider {

    private val locationManager: LocationManager
        get() = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    override suspend fun currentCoordinates(): Coordinates? = withContext(ioDispatcher) {
        if (!hasLocationPermission()) return@withContext null

        val location = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .filter { it in locationManager.allProviders }
            .mapNotNull { provider -> runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull() }
            .maxByOrNull { it.time }

        location?.let { Coordinates(it.latitude, it.longitude) }
    }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
