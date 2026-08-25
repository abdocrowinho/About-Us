package org.aboutus.project.features.location.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.aboutus.project.features.location.domain.DeviceLocationProvider

@Composable actual fun rememberDeviceLocationProvider(): DeviceLocationProvider = remember {
    org.aboutus.project.features.location.domain.DeviceLocationProvider {
        Result.failure(
            IllegalStateException("Location unavailable on desktop")
        )
    }
}
