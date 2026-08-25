package org.aboutus.project.features.location.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.aboutus.project.features.location.domain.DeviceLocationProvider

// Android is the active mobile target at the moment. The existing iOS permission
// description remains in Info.plist; this safe fallback keeps the KMP target buildable.
@Composable actual fun rememberDeviceLocationProvider(): DeviceLocationProvider = remember {
    org.aboutus.project.features.location.domain.DeviceLocationProvider {
        Result.failure(
            IllegalStateException("Location is not available on this platform")
        )
    }
}
