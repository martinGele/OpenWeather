package com.sporty.openweather.feature.forecast.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Looper
import com.sporty.openweather.core.common.dispatchers.IoDispatcher
import com.sporty.openweather.feature.forecast.domain.model.Coordinates
import com.sporty.openweather.feature.forecast.domain.repository.LocationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.seconds

class DeviceLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocationProvider {

    private val locationManager: LocationManager
        get() = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    override suspend fun currentCoordinates(): Coordinates? = withContext(ioDispatcher) {
        if (!hasLocationPermission()) return@withContext null

        lastKnownLocation()?.let { return@withContext Coordinates(it.latitude, it.longitude) }

        val fresh = withTimeoutOrNull(FRESH_LOCATION_TIMEOUT) { requestFreshLocation() }
        fresh?.let { Coordinates(it.latitude, it.longitude) }
    }

    @SuppressLint("MissingPermission")
    private fun lastKnownLocation(): Location? =
        enabledProviders()
            .mapNotNull { provider -> runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull() }
            .maxByOrNull { it.time }

    @SuppressLint("MissingPermission")
    private suspend fun requestFreshLocation(): Location? {
        val provider = enabledProviders().firstOrNull() ?: return null

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            suspendCancellableCoroutine { cont ->
                val signal = CancellationSignal()
                cont.invokeOnCancellation { signal.cancel() }
                locationManager.getCurrentLocation(
                    provider,
                    signal,
                    context.mainExecutor
                ) { location ->
                    cont.resume(location)
                }
            }
        } else {
            suspendCancellableCoroutine { cont ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationManager.removeUpdates(this)
                        if (cont.isActive) cont.resume(location)
                    }

                    override fun onProviderDisabled(provider: String) = Unit
                    override fun onProviderEnabled(provider: String) = Unit

                    @Deprecated("Required by LocationListener on older APIs")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) =
                        Unit
                }
                cont.invokeOnCancellation { locationManager.removeUpdates(listener) }
                locationManager.requestLocationUpdates(
                    provider,
                    0L,
                    0f,
                    listener,
                    Looper.getMainLooper()
                )
            }
        }
    }

    private fun enabledProviders(): List<String> =
        listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .filter { it in locationManager.allProviders && locationManager.isProviderEnabled(it) }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private companion object {
        val FRESH_LOCATION_TIMEOUT = 10.seconds
    }
}
