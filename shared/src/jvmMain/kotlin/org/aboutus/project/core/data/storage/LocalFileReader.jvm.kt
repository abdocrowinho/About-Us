package org.aboutus.project.core.data.storage

import java.io.File

actual object LocalFileReader {
    actual suspend fun readBytes(path: String): ByteArray? {
        return runCatching {
            File(path).takeIf { it.exists() && it.isFile }?.readBytes()
        }.getOrNull()
    }
}
