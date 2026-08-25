package org.aboutus.project.features.location.domain

import androidx.compose.runtime.Composable

data class UserLocation(val latitude: Double, val longitude: Double)

interface UserLocationPreferences {
    val location: UserLocation?
    fun save(location: UserLocation)
}

interface DeviceLocationProvider {
    suspend fun requestLocation(): Result<UserLocation>
}

@Composable
expect fun rememberDeviceLocationProvider(): DeviceLocationProvider
