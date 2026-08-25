package org.aboutus.project.features.splash.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.aboutus.project.core.presentation.components.appGradientBackground
import org.aboutus.project.core.presentation.components.AmbientFeelingLights
import org.aboutus.project.features.splash.presentation.components.SplashBrand

@Composable
fun SplashScreen(viewModel: SplashViewModel, onFinished: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.waitForSplash(); onFinished() }
    Box(
        Modifier.fillMaxSize().appGradientBackground().navigationBarsPadding(),
        Alignment.Center
    ) {
        AmbientFeelingLights()
        SplashBrand()
    }
}
