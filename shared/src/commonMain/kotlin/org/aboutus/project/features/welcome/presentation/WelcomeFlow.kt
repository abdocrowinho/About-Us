package org.aboutus.project.features.welcome.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.aboutus.project.features.auth.presentation.AuthScreen
import org.aboutus.project.features.auth.presentation.AuthViewModel
import org.aboutus.project.features.onboarding.presentation.OnboardingScreen
import org.aboutus.project.features.onboarding.presentation.OnboardingViewModel
import org.aboutus.project.features.splash.presentation.SplashScreen
import org.aboutus.project.features.splash.presentation.SplashViewModel
import org.aboutus.project.features.location.presentation.LocationScreen
import org.aboutus.project.features.location.presentation.LocationViewModel

@Composable fun WelcomeFlow(coordinator: WelcomeCoordinatorViewModel,
                            splash: SplashViewModel, onboarding: OnboardingViewModel,
                            location: LocationViewModel, auth: AuthViewModel,
                            onReady: @Composable () -> Unit) {
    val destination by coordinator.destination.collectAsState()
    when (destination) {
        WelcomeDestination.SPLASH -> SplashScreen(splash, coordinator::afterSplash)
        WelcomeDestination.ONBOARDING -> OnboardingScreen(onboarding, coordinator::goToLocation)
        WelcomeDestination.LOCATION -> LocationScreen(location, coordinator::goToAuth)
        WelcomeDestination.AUTH -> AuthScreen(auth, coordinator::goToEarth)
        WelcomeDestination.EARTH -> onReady()
    }
}
