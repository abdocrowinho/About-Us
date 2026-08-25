package org.aboutus.project.features.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.aboutus.project.core.presentation.components.appGradientBackground
import org.aboutus.project.features.auth.presentation.components.AnonymousAuthContent

@Composable
fun AuthScreen(viewModel: AuthViewModel, onFinished: () -> Unit) {
    val state by viewModel.state.collectAsState(); Box(
        Modifier.fillMaxSize().appGradientBackground().navigationBarsPadding(), Alignment.Center
    ) {
        AnonymousAuthContent(
            state,
            { viewModel.enter(onFinished) },
            { viewModel.restore(onFinished) })
    }
}
