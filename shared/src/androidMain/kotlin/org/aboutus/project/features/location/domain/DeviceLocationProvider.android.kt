package org.aboutus.project.features.location.domain

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationListener
import android.os.Looper
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

private class AndroidLocationProvider(
    private val context: Context,
    private val askPermission: (suspend () -> Boolean)
) : DeviceLocationProvider {
    @SuppressLint("MissingPermission") // Permission is checked immediately before requesting a location.
    override suspend fun requestLocation(): Result<UserLocation> = runCatching {
        val granted = hasLocationPermission() || askPermission()
        check(granted) { "Location permission denied" }

        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        ).filter { provider ->
            runCatching { manager.isProviderEnabled(provider) }.getOrDefault(false)
        }
        check(providers.isNotEmpty()) { "Location services are disabled" }

        val cachedLocation = providers
            .mapNotNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }
            .maxByOrNull { it.time }

        val location = cachedLocation ?: withTimeoutOrNull(12_000L) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(result: android.location.Location) {
                        if (continuation.isActive) continuation.resume(result)
                    }

                    @Deprecated("Deprecated in Android")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit
                }
                runCatching {
                    manager.requestSingleUpdate(providers.first(), listener, Looper.getMainLooper())
                }.onFailure { error ->
                    if (continuation.isActive) continuation.resumeWith(Result.failure(error))
                }
                continuation.invokeOnCancellation { manager.removeUpdates(listener) }
            }
        } ?: error("Location unavailable")

        UserLocation(location.latitude, location.longitude)
    }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

@Composable
actual fun rememberDeviceLocationProvider(): DeviceLocationProvider {
    val context = LocalContext.current
    var permissionContinuation by remember {
        mutableStateOf<kotlinx.coroutines.CancellableContinuation<Boolean>?>(null)
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        permissionContinuation?.resume(
            result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        )
        permissionContinuation = null
    }
    return remember(context) {
        AndroidLocationProvider(context) {
            suspendCancellableCoroutine { pending ->
                permissionContinuation = pending
                launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                pending.invokeOnCancellation { permissionContinuation = null }
            }
        }
    }
}
