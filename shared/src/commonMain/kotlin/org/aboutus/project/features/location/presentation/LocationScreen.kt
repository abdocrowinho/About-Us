package org.aboutus.project.features.location.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.aboutus.project.core.presentation.components.appGradientBackground
import org.aboutus.project.features.location.domain.rememberDeviceLocationProvider
import org.aboutus.project.features.location.presentation.components.LocationRequestContent

@Composable fun LocationScreen(viewModel: LocationViewModel, onFinished: () -> Unit) {
    val provider = rememberDeviceLocationProvider(); val state by viewModel.state.collectAsState()
    Box(Modifier.fillMaxSize().appGradientBackground(), Alignment.Center) { LocationRequestContent(state, { viewModel.request(provider, onFinished) }, { viewModel.skip(onFinished) }) }
}
