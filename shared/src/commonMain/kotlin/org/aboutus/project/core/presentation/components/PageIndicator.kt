package org.aboutus.project.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable fun PageIndicator(count: Int, activeIndex: Int) = Row {
    repeat(count) { index -> androidx.compose.foundation.layout.Box(Modifier.size(if (index == activeIndex) 28.dp else 8.dp, 8.dp).clip(CircleShape).background(if (index == activeIndex) AppBlue else Color.White.copy(.25f))) }
}
