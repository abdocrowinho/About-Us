package org.aboutus.project.features.onboarding.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.about_us.project.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.presentation.components.*
import org.aboutus.project.features.onboarding.presentation.components.*

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onFinished: () -> Unit) {
    var index by remember { mutableIntStateOf(0) };
    val page = pages[index]
    Column(
        Modifier
            .fillMaxSize()
            .appGradientBackground()
            .navigationBarsPadding()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PageIndicator(pages.size, index); AnimatedContent(
        page,
        label = "onboarding"
    ) { OnboardingPageContent(it) }
        Button(
            {
                if (index == pages.lastIndex) {
                    viewModel.complete(); onFinished()
                } else index++
            },
            Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(AppBlue, AppInk)
        ) {
            Text(
                stringResource(if (index == pages.lastIndex) Res.string.start_calmly else Res.string.next),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private val pages = listOf(
    OnboardingPage(
        Res.string.onboarding_one_title,
        Res.string.onboarding_one_body,
        Icons.Default.Public
    ),
    OnboardingPage(
        Res.string.onboarding_two_title,
        Res.string.onboarding_two_body,
        Icons.Default.Waves
    ),
    OnboardingPage(
        Res.string.onboarding_three_title,
        Res.string.onboarding_three_body,
        Icons.Default.AutoAwesome
    )
)
