package org.aboutus.project.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val AppBlue = Color(0xFF3A8DFF)
val AppInk = Color(0xFF010207)
val AppMist = Color(0xFFB7CAE3)

fun Modifier.appGradientBackground() = background(
    Brush.linearGradient(
        colorStops = arrayOf(
            0f to Color(0xFF0A2B5B),
            0.16f to Color(0xFF06162E),
            0.40f to Color(0xFF020814),
            0.68f to AppInk,
            1f to Color.Black
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )
)
