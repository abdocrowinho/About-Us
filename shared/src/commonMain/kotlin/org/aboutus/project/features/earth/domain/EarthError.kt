package org.aboutus.project.features.earth.domain

sealed interface EarthError {
    data object NoInternet : EarthError
    data object ServiceUnavailable : EarthError
    data object Unknown : EarthError
}

fun Throwable.toEarthError(): EarthError {
    val details = message.orEmpty().lowercase()
    return when {
        "unknownhost" in details || "network" in details || "timeout" in details ||
            "connect" in details || "offline" in details || "dns" in details -> EarthError.NoInternet
        "5" in details || "service" in details || "server" in details -> EarthError.ServiceUnavailable
        else -> EarthError.Unknown
    }
}
