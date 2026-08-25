package org.aboutus.project.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import org.aboutus.project.features.welcome.domain.WelcomePreferences

class OnboardingViewModel(private val preferences: WelcomePreferences) : ViewModel() {
    fun complete() { preferences.onboardingSeen = true }
}
