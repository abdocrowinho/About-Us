package org.aboutus.project.core.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class JvmMicrophonePermissionController : MicrophonePermissionController {
    override suspend fun getStatus(): PermissionStatus = PermissionStatus.GRANTED

    override suspend fun requestPermission(): PermissionStatus = PermissionStatus.GRANTED

    override fun openAppSettings() = Unit
}

@Composable
actual fun rememberMicrophonePermissionController(): MicrophonePermissionController {
    return remember { JvmMicrophonePermissionController() }
}
