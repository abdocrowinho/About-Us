package org.aboutus.project.core.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(text: String) {
    var count by remember(text) { mutableIntStateOf(0) }
    LaunchedEffect(text) { while (count < text.length) { delay(18); count++ } }
    Text(text.take(count), color = AppMist, fontSize = 17.sp, lineHeight = 27.sp, textAlign = TextAlign.Center)
}
