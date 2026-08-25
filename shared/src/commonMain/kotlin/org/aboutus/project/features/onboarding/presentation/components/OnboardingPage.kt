package org.aboutus.project.features.onboarding.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.presentation.components.AppBlue
import org.aboutus.project.core.presentation.components.TypewriterText

data class OnboardingPage(
    val title: StringResource,
    val body: StringResource,
    val icon: ImageVector
)

@Composable
fun OnboardingPageContent(page: OnboardingPage) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            page.icon,
            null,
            tint = AppBlue,
            modifier = Modifier.size(64.dp)
        ); Spacer(Modifier.height(30.dp)); Text(
        stringResource(page.title),
        color = Color.White,
        fontSize = 27.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ); Spacer(Modifier.height(14.dp)); TypewriterText(stringResource(page.body))
    }
