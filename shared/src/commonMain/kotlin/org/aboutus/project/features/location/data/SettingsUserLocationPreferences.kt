package org.aboutus.project.features.location.data

import com.russhwolf.settings.Settings
import org.aboutus.project.features.location.domain.UserLocation
import org.aboutus.project.features.location.domain.UserLocationPreferences

class SettingsUserLocationPreferences(private val settings: Settings = Settings()) : UserLocationPreferences {
    override val location: UserLocation?
        get() = if (settings.getBoolean("user_location_saved", false)) UserLocation(settings.getDouble("user_lat", 0.0), settings.getDouble("user_lng", 0.0)) else null
    override fun save(location: UserLocation) {
        settings.putDouble("user_lat", location.latitude)
        settings.putDouble("user_lng", location.longitude)
        settings.putBoolean("user_location_saved", true)
    }
}
