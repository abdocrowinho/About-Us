package org.aboutus.project.features.location.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.about_us.project.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.presentation.components.*
import org.aboutus.project.features.location.presentation.LocationUiState

@Composable fun LocationRequestContent(state: LocationUiState, onAllow: () -> Unit, onSkip: () -> Unit) = Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(Icons.Default.MyLocation, null, tint = AppBlue, modifier = Modifier.size(56.dp)); Spacer(Modifier.height(22.dp))
    Text(stringResource(Res.string.location_title), color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(14.dp))
    Text(stringResource(Res.string.location_body), color = AppMist, textAlign = TextAlign.Center, lineHeight = 25.sp); Spacer(Modifier.height(30.dp))
    Button(onAllow, Modifier.fillMaxWidth().height(54.dp), enabled = !state.loading, shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(AppBlue, AppInk)) { Text(stringResource(if (state.loading) Res.string.location_loading else Res.string.location_allow), fontWeight = FontWeight.Bold) }
    TextButton(onSkip) { Text(stringResource(Res.string.location_skip), color = AppMist) }
    if (state.failed) Text(stringResource(Res.string.location_error), color = Color(0xFFFFB4AB), textAlign = TextAlign.Center)
}
