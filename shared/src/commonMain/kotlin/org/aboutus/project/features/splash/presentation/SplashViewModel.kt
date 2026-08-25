package org.aboutus.project.features.splash.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class SplashViewModel : ViewModel() {
    suspend fun waitForSplash() = delay(1700)
}
