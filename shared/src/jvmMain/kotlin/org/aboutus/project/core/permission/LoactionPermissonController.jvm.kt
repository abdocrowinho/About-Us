package org.aboutus.project.core.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class JvmLocationPermissionController : LocationPermissionController {
    override suspend fun getStatus(): PermissionStatus = PermissionStatus.GRANTED
    override suspend fun requestPermission(): PermissionStatus = PermissionStatus.GRANTED
    override fun openAppSettings() {
    }
}

@Composable
actual fun rememberLocationPermissionController(): LocationPermissionController {
    return remember { JvmLocationPermissionController() }
}