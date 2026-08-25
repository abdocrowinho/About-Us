package org.aboutus.project.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class JvmImagePickerController : ImagePickerController {
    override suspend fun pickFromGallery(): ByteArray? = null

    override suspend fun captureFromCamera(): ByteArray? = null
}

@Composable
actual fun rememberImagePickerController(): ImagePickerController {
    return remember { JvmImagePickerController() }
}
