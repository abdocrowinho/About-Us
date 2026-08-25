package org.aboutus.project.features.welcome.data

import com.russhwolf.settings.Settings
import org.aboutus.project.features.welcome.domain.WelcomePreferences

class SettingsWelcomePreferences(private val settings: Settings = Settings()) : WelcomePreferences {
    override var onboardingSeen: Boolean
        get() = settings.getBoolean("welcome_onboarding_seen", false)
        set(value) = settings.putBoolean("welcome_onboarding_seen", value)
    override var tourSeen: Boolean
        get() = settings.getBoolean("welcome_tour_seen", false)
        set(value) = settings.putBoolean("welcome_tour_seen", value)
    override var loggedIn: Boolean
        get() = settings.getBoolean("welcome_anonymous_logged_in", false)
        set(value) = settings.putBoolean("welcome_anonymous_logged_in", value)
}
