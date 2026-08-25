package org.aboutus.project

import androidx.compose.runtime.Composable
import org.koin.compose.KoinIsolatedContext
import org.koin.compose.koinInject
import org.koin.dsl.koinApplication
import org.aboutus.project.di.dataModule
import org.aboutus.project.di.presentation
import org.aboutus.project.features.earth.peresntation.MapScreen
import org.aboutus.project.features.welcome.domain.WelcomePreferences
import org.aboutus.project.features.welcome.presentation.WelcomeFlow
import org.aboutus.project.features.welcome.presentation.WelcomeCoordinatorViewModel
import org.aboutus.project.features.splash.presentation.SplashViewModel
import org.aboutus.project.features.onboarding.presentation.OnboardingViewModel
import org.aboutus.project.features.auth.presentation.AuthViewModel
import org.aboutus.project.features.location.presentation.LocationViewModel

private val appKoin = koinApplication {
        modules(
            dataModule,
            presentation
        )
}

@Composable
fun App() {
    KoinIsolatedContext(context = appKoin) {
        val coordinator: WelcomeCoordinatorViewModel = koinInject()
        val splash: SplashViewModel = koinInject()
        val onboarding: OnboardingViewModel = koinInject()
        val location: LocationViewModel = koinInject()
        val auth: AuthViewModel = koinInject()
        val preferences: WelcomePreferences = koinInject()
        WelcomeFlow(coordinator, splash, onboarding, location, auth) {
            MapScreen(
                showAppTour = !preferences.tourSeen,
                onAppTourComplete = { preferences.tourSeen = true }
            )
        }
    }
}
