package org.aboutus.project.features.welcome.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.aboutus.project.features.welcome.domain.WelcomePreferences

enum class WelcomeDestination { SPLASH, ONBOARDING, LOCATION, AUTH, EARTH }
class WelcomeCoordinatorViewModel(private val preferences: WelcomePreferences) : ViewModel() {
    private val _destination = MutableStateFlow(WelcomeDestination.SPLASH)
    val destination = _destination.asStateFlow()
    fun afterSplash() { _destination.value = if (!preferences.onboardingSeen) WelcomeDestination.ONBOARDING else if (!preferences.loggedIn) WelcomeDestination.LOCATION else WelcomeDestination.EARTH }
    fun goToLocation() { _destination.value = WelcomeDestination.LOCATION }
    fun goToAuth() { _destination.value = WelcomeDestination.AUTH }
    fun goToEarth() { _destination.value = WelcomeDestination.EARTH }
}
